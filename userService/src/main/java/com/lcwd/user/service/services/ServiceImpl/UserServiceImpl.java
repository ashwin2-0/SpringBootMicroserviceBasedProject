package com.lcwd.user.service.services.ServiceImpl;

import com.lcwd.user.service.entities.Hotel;
import com.lcwd.user.service.entities.Rating;
import com.lcwd.user.service.entities.User;
import com.lcwd.user.service.exceptions.ResourceNotFoundException;
import com.lcwd.user.service.external.services.IFeignHotelService;
import com.lcwd.user.service.repositores.UserRepository;
import com.lcwd.user.service.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    // this is feign client interface which will call hotel service
    @Autowired
    private IFeignHotelService hotelService;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    @Override
    public User getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        //now before this we want the ratings of the user from rating service
        // http://localhost:8083/ratings/users/5
        // this  is how I am getting the ratings of the user from rating service using RestTemplate
        // Calling the Rating Service
        // Spring will now correctly resolve "RATING-SERVICE" thanks to @LoadBalanced
        // 2. Fetch the ratings from RATING-SERVICE
        // Use an array of Rating objects to avoid ClassCastExceptions with generic Lists
        Rating[] ratingsOfUser = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/" + user.getUserId(), Rating[].class);
        List<Rating> ratingList = Arrays.stream(ratingsOfUser).toList();
        // 3. For each rating, fetch the Hotel information
        for (Rating rating : ratingList) {
            // API Call to HOTEL-SERVICE: http://HOTEL-SERVICE/hotels/{hotelId}
            // now lsiten carefully here we are calling hotel service to get hotel info for each rating by using rest template
            // Spring will now correctly resolve "HOTEL-SERVICE" thanks to @LoadBalanced
           // Hotel hotel = restTemplate.getForObject("http://HOTEL-SERVICEE/hotels/" + rating.getHotelId(), Hotel.class);
            // but now instead of rest template we will use feign client to call hotel service
             Hotel hotel = hotelService.getHotel(rating.getHotelId());

            // Set the fetched hotel into the current rating
            rating.setHotel(hotel);

            logger.info("Fetched hotel {} for rating {}", hotel.getName(), rating.getRatingId());
        }
        // 4. Attach the fully populated ratings list to the user
        user.setRatings(ratingList);

        return user;
    }

    @Override
    public User saveUser(User user) {


        // 3. Save the user with the new ID
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {

        // 1. Fetch all users from the local MySQL database
        List<User> userList = userRepository.findAll();
        // here also we can fetch ratings for each user if needed
        for (User user : userList) {
            // 2. Fetch Ratings for this specific user from RATING-SERVICE
            // It's better to use an array for type safety during deserialization
            Rating[] ratings = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/" + user.getUserId(), Rating[].class);
            List<Rating> ratingList = Arrays.stream(ratings).toList();
            // 3. Nested loop to fetch Hotel details for each Rating
            for (Rating rating : ratingList) {
                // API Call to HOTEL-SERVICE using logical name from Eureka
                Hotel hotel = restTemplate.getForObject("http://HOTEL-SERVICE/hotels/" + rating.getHotelId(), Hotel.class);

                // Set the Hotel information into the Rating object
                rating.setHotel(hotel);
            }
            // 4. Set the fully populated ratings list (with hotels) back to the user
            user.setRatings(ratingList);
        }
        return userList;
    }

    public List<User> getAllUsersBulk() {
        // 1. Get all Users from DB
        List<User> userList = userRepository.findAll();

        // 2. Collect all User IDs into a list to send to Rating Service
        List<Long> userIds = userList.stream().map(User::getUserId).toList();

        // 3. CALL 2: Fetch ALL Ratings for these User IDs in one go
        // Note: You must implement /ratings/users-batch in Rating Service

        /*
        If you were not using Eureka or Service Discovery, your code would look like this:
        Rating[] allRatingsArray = restTemplate.postForObject(
                            "http://localhost:8083/ratings/users-batch", // Hardcoded Address
                            userIds,                                     // Request Body
                            Rating[].class                               // Expected Response Type
                        );
          Now that you have Eureka and @LoadBalanced set up, your code looks like this:
            Rating[] allRatingsArray = restTemplate.postForObject(
                                    "http://RATING-SERVICE/ratings/users-batch", // Logical Service Name
                                    userIds,                                     // Request Body
                                    Rating[].class                               // Response Mapping
                                );

         */
        Rating[] allRatingsArray = restTemplate.postForObject(
                "http://RATING-SERVICE/ratings/users-batch", userIds, Rating[].class);
        List<Rating> allRatings = Arrays.asList(allRatingsArray);

        // 4. Collect all unique Hotel IDs from the ratings
        Set<Long> hotelIds = allRatings.stream().map(Rating::getHotelId).collect(Collectors.toSet());

        // 5. CALL 3: Fetch ALL Hotels for these Hotel IDs in one go
        // Note: You must implement /hotels/batch in Hotel Service
        Hotel[] allHotelsArray = restTemplate.postForObject(
                "http://HOTEL-SERVICE/hotels/batch", hotelIds, Hotel[].class);

        // 6. Create a Map of Hotels for O(1) lookup in memory
        Map<Long, Hotel> hotelMap = Arrays.stream(allHotelsArray)
                .collect(Collectors.toMap(Hotel::getHotelId, h -> h));

        // 7. Data Assembly: Map Hotels into Ratings
        allRatings.forEach(rating -> rating.setHotel(hotelMap.get(rating.getHotelId())));

        // 8. Data Assembly: Group Ratings by UserID and Map into Users
        Map<Long, List<Rating>> ratingsByUserId = allRatings.stream()
                .collect(Collectors.groupingBy(Rating::getUserId));

        userList.forEach(user -> user.setRatings(ratingsByUserId.getOrDefault(user.getUserId(), new ArrayList<>())));

        return userList;
    }


    @Override
    public boolean deleteUserById(Long userId) {
        return false;
    }

    @Override
    public User updateUser(Long userId, User user) {
        return null;
    }
}
