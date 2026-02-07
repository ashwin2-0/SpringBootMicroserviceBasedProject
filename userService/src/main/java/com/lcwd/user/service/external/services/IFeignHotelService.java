package com.lcwd.user.service.external.services;

import com.lcwd.user.service.entities.Hotel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

@FeignClient(name = "HOTEL-SERVICE")
public interface IFeignHotelService {

    // This must match the endpoint in your Hotel Service
    @PostMapping("/hotels/batch")
    List<Hotel> getHotelsByIds(@RequestBody Set<Long> hotelIds);

    // You can also add single fetch here
    @GetMapping("/hotels/{hotelId}")
    Hotel getHotel(@PathVariable("hotelId") Long hotelId);

    @PostMapping("/hotels")
    public ResponseEntity<Hotel> createHotel(@RequestBody Hotel hotel);

    // get all hotel api
    @GetMapping()
    public ResponseEntity<List<Hotel>> getAllHotels();

}
