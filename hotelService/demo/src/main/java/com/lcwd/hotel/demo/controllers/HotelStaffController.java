package com.lcwd.hotel.demo.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hotelStaff")
public class HotelStaffController {

    @GetMapping
    ResponseEntity<List> getAllHotelStaff() {
        List<String> staff = List.of("Alice - Manager", "Bob - Receptionist", "Charlie - Housekeeping");
        return ResponseEntity.ok(staff);
    }


}
