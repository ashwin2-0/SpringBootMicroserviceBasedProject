package com.lcwd.user.service.external.services;

import com.lcwd.user.service.entities.Rating;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "RATING-SERVICE")
public interface IFeignRatingService {
    // get ratings by user id

    @GetMapping("/ratings/{ratingId}")
    public ResponseEntity<Rating> getRatingById(@PathVariable Long ratingId);

    // create Rating api
    @PostMapping("/ratings")
    public ResponseEntity<Rating> createRating(@RequestBody Rating rating);

    // get All the ratings
    @GetMapping("/ratings")
    public ResponseEntity<List<Rating>> getAllRatings();

    // get Single ratings by user id
    // 4. Get ratings for a specific user (Path updated to avoid conflict)
    @GetMapping("/ratings/users/{userId}")
    List<Rating> getRatingsByUserId(@PathVariable("userId") Long userId);

    // get Rating by hotel id

    // get Rating as per hotelId
    @GetMapping("/hotels/{hotelId}")
    public ResponseEntity<List<Rating>> getRatingsByHotelId(@PathVariable Long hotelId);


   // see below are the methods which we are calling With the Help of Feign Client



}
