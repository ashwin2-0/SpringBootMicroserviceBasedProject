package com.lcwd.hotel.demo.repositories;

import com.lcwd.hotel.demo.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

}
