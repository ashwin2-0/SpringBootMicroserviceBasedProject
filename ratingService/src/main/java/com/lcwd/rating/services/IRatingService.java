package com.lcwd.rating.services;

import com.lcwd.rating.entities.Rating;

import java.util.List;

public interface IRatingService {

    // creating rating
    Rating createRating(Rating rating);

    // updating reating
    Rating updateRating(Long ratingId, Rating rating);

    // delete rating
    Rating deleteRating(Long ratingId);

    // get all ratings

    List<Rating> getAllRatings();

    // get single rating
    Rating getRatingById(Long ratingId);

    // get all ratings by user id

    List<Rating> getRatingsByUserId(Long userId);

    // get all ratings by hotel id
    List<Rating> getRatingsByHotelId(Long hotelId);

    // get all ratings by hotel id and user id

    // get all ratings by rating value

    List<Rating> getRatingsByUserIds(List<Long> userIds);


}
