package com.lcwd.rating.entities;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("user_ratings")
public class Rating {

    @Id
    private Long ratingId;
    private long userId;

    private long hotelId;

    private int rating;
    private String feedback;

}
