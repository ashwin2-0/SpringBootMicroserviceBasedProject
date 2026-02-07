package com.lcwd.rating.repository;

import com.lcwd.rating.entities.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RatingRepository extends MongoRepository<Rating, Long> {
    // custom finder methods can be defined here

    List<Rating> findByUserId(Long userId);

    List<Rating> findByHotelId(Long hotelId);

    List<Rating> findByUserIdIn(List<Long> userIds);

}
