package com.lcwd.hotel.demo.services;

import com.lcwd.hotel.demo.entities.Hotel;

import java.util.List;
import java.util.Set;

public interface IHotelService {

    // create Hoel

    Hotel create(Hotel hotel);

    // get All

    List<Hotel> getAll();

    // get Single hotel

    Hotel get(Long hotelId);

    public List<Hotel> getHotelsByIds(Set<Long> hotelIds);
}
