package com.lcwd.hotel.demo.services.impl;

import com.lcwd.hotel.demo.entities.Hotel;
import com.lcwd.hotel.demo.exceptions.ResourceNotFoundException;
import com.lcwd.hotel.demo.repositories.HotelRepository;
import com.lcwd.hotel.demo.services.IHotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class HotelServiceImpl implements IHotelService {

    @Autowired
   private HotelRepository hotelRepository;

    @Override
    public Hotel create(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    @Override
    public List<Hotel> getAll() {
        return hotelRepository.findAll();
    }

    @Override
    public Hotel get(Long hotelId) {
        return hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));
    }
    // purpose of this method is to get multiple hotels by their ids so that we can use it in user service to get hotel details for multiple hotels at once
    // whcih will eventually reduce the number of api calls between user service and hotel service
    public List<Hotel> getHotelsByIds(Set<Long> hotelIds) {
        // Spring Data JPA's built-in method handles "WHERE id IN (...)"
        return hotelRepository.findAllById(hotelIds);
    }
}
