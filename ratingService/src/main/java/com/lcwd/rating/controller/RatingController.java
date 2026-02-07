package com.lcwd.rating.controller;

import com.lcwd.rating.entities.Rating;
import com.lcwd.rating.services.IRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    @Autowired
    private IRatingService ratingService;

    // create Rating api
    @PostMapping
    public ResponseEntity<Rating> createRating(@RequestBody Rating rating) {
        Rating createdRating = ratingService.createRating(rating);
        return ResponseEntity.ok(createdRating);
    }

    //getAll Ratings api
    @GetMapping
    public ResponseEntity<List<Rating>> getAllRatings() {
        java.util.List<Rating> ratings = ratingService.getAllRatings();
        return ResponseEntity.ok(ratings);
    }

    // get single Rating api
    @GetMapping("/{ratingId}")
    public ResponseEntity<Rating> getRatingById(@PathVariable ("ratingId") Long ratingId) {
        Rating rating = ratingService.getRatingById(ratingId);
        return ResponseEntity.ok(rating);
    }

    // getS Single Rating as per userId
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Rating>> getRatingsByUserId(@PathVariable ("userId") Long userId) {
        List<Rating> ratings = ratingService.getRatingsByUserId(userId);
        return ResponseEntity.ok(ratings);
    }

    // get Rating as per hotelId
    @GetMapping("/ratings/hotels/{hotelId}")
    public ResponseEntity<List<Rating>> getRatingsByHotelId(@PathVariable ("hotelId") Long hotelId) {
        List<Rating> ratings = ratingService.getRatingsByHotelId(hotelId);
        return ResponseEntity.ok(ratings);
    }
    // this is to get ratings by list of user ids so that we can use it in user service to get ratings of multiple users at once
    @PostMapping("/users-batch")
    public ResponseEntity<List<Rating>> getRatingsByUserIds(@RequestBody List<Long> userIds) {
        return ResponseEntity.ok(ratingService.getRatingsByUserIds(userIds));
    }

}
