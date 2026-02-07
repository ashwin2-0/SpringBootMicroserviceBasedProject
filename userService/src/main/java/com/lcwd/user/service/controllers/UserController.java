package com.lcwd.user.service.controllers;


import com.lcwd.user.service.entities.Hotel;
import com.lcwd.user.service.entities.Rating;
import com.lcwd.user.service.entities.User;
import com.lcwd.user.service.external.services.IFeignHotelService;
import com.lcwd.user.service.external.services.IFeignRatingService;
import com.lcwd.user.service.payload.ApiResponse;
import com.lcwd.user.service.services.IUserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IFeignRatingService ratingFeignClient; // Your Feign Client

    @Autowired
    private IFeignHotelService hotelFeignCleint; // Feign Client for Hotel Service


    //create user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    // get one user
    @GetMapping("/{userId}")
    //    @CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
    //   @Retry(name = "retryRatingHotel", fallbackMethod = "ratingHotelRetryFallback")
    @RateLimiter(name = "userRateLimiter", fallbackMethod = "ratingHotelRetryFallback")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        log.info("Attempting to fetch user: {}", userId);

        User user = userService.getUserById(userId);

        return ResponseEntity.ok(user);
    }

    // creating fallback method for circuit breaker
    public ResponseEntity<User> ratingHotelFallback(Long userId, Exception ex) {
        log.info("Fallback is executing because service is down :", ex.getMessage());
      /*  User user = userService.getUserById(userId);
        user.builder*/
        User user = User.builder().
                userId(userId).
                name("Dummy User").
                email("dummy@gmail.com").about("this user is created because some service is down in circuit Breaker fallback method").build();
        return ResponseEntity.ok(user);
    }


    // creating fallback method for retry
    public ResponseEntity<User> ratingHotelRetryFallback(Long userId, Exception ex) {
        log.info("Retry Fallback is executing because service is down :", ex.getMessage());
        //retryCount++;
        User user = User.builder().
                userId(userId).
                name("Dummy retry User").
                email("dummy@gmail.com").about("this user is created inside retry method because some service is down").build();
        //   return ResponseEntity.ok(user);
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(user);
    
    }

    // get all user
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // update one user
// update one user
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User user) {
        User updatedUser = userService.updateUser(userId, user);
        return ResponseEntity.ok(updatedUser);
    }

    //delete one user
    // delete one user
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        userService.deleteUserById(userId);
        ApiResponse response = ApiResponse.builder()
                .message("User deleted successfully with ID: " + userId)
                .success(true)
                .staus(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);


    }

    // this is just another method to get all users in bulk, can be used if needed
    // so that we can have different endpoints for different purposes
    // also to demonstrate multiple ways of achieving the same functionality
    // by using different endpoint names and response timing
    @GetMapping("/all-bulk")
    public ResponseEntity<List<User>> getAllUsersBulk() {
        List<User> users = userService.getAllUsersBulk();
        return ResponseEntity.ok(users);
    }

    // Below are the methods which are using Feign Client to call Rating Service APIs

    @PostMapping("/ratings")
    public ResponseEntity<Rating> createRating(@RequestBody Rating rating) {
        // This calls the Feign Client, which calls the Rating Service
        // 1. Call Rating Service to create the rating
        // We use .getBody() to extract the Rating object from the ResponseEntity
        Rating savedRating = ratingFeignClient.createRating(rating).getBody();
        // 2. Fetch Hotel details using the hotelId from the savedRating
        if (savedRating != null) {
            // Use your Hotel Feign Client here
            Hotel hotel = hotelFeignCleint.getHotel(savedRating.getHotelId());

            // 3. Set the hotel information into the rating object
            savedRating.setHotel(hotel);
        }

        // 4. Return the enriched Rating object
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRating);
    }


    @GetMapping("feign/ratings/{userId}")
    public List<Rating> getRatingsByUserId(@PathVariable Long userId) {

        return ratingFeignClient.getRatingsByUserId(userId);
    }

    ///  get rating by rating id
    @GetMapping("/ratings/{ratingId}")
    public ResponseEntity<Rating> getRatingById(@PathVariable Long ratingId) {
        // 1. Get the Rating from Rating Service via Feign
        Rating rating = ratingFeignClient.getRatingById(ratingId).getBody();

        // 2. Use the hotelId from the rating to get Hotel info via Feign
        if (rating != null) {
            Hotel hotel = hotelFeignCleint.getHotel(rating.getHotelId());
            rating.setHotel(hotel);
        }
        return ResponseEntity.ok(rating);
    }

    // get all ratings
    @GetMapping("/ratings")
    public ResponseEntity<List<Rating>> getAllRatings() {
        return ratingFeignClient.getAllRatings();
    }

    // get ratings by hotel id
    @GetMapping("/ratings/hotels/{hotelId}")
    public ResponseEntity<List<Rating>> getRatingsByHotelId(@PathVariable Long hotelId) {
        return ratingFeignClient.getRatingsByHotelId(hotelId);
    }

}

