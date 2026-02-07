package com.lcwd.hotel.demo.controllers;


import com.lcwd.hotel.demo.entities.Hotel;
import com.lcwd.hotel.demo.services.IHotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    @Autowired
    private IHotelService hotelService;

    //create api
    @PostMapping
    public ResponseEntity<Hotel> createHotel(@RequestBody Hotel hotel) {
        Hotel createdHotel = hotelService.create(hotel);
        return ResponseEntity.status(HttpStatus.CREATED).body(hotel);
    }
    // get single hotel api

    @GetMapping("/{hotelId}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable Long hotelId) {
        Hotel hotel = hotelService.get(hotelId);
        return ResponseEntity.ok(hotel);
    }

    // get all hotel api
    @GetMapping()
    public ResponseEntity<List<Hotel>> getAllHotels() {
        java.util.List<Hotel> hotels = hotelService.getAll();
        return ResponseEntity.ok(hotels);
    }


    // Use POST because we are sending a Body (the list of IDs)
    /*
    Why use @PostMapping and @RequestBody?
    @PostMapping: Since you are sending a Set<Long> of IDs, this data is sent
                 in the Request Body. Standard HTTP rules prefer POST for
                 requests that carry a payload.

    @RequestBody: This tells Spring to take the JSON list of IDs coming
                   from the User Service and turn it into a Java Set
     */
    @PostMapping("/batch")
    public ResponseEntity<List<Hotel>> getHotelsByIds(@RequestBody Set<Long> hotelIds) {
        List<Hotel> hotels = hotelService.getHotelsByIds(hotelIds);
        return ResponseEntity.ok(hotels);
    }
}
