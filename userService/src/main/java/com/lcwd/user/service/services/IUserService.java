package com.lcwd.user.service.services;

import com.lcwd.user.service.entities.User;

import java.util.List;

public interface IUserService {
    User getUserById(Long userId);

    User saveUser(User user);



    boolean deleteUserById(Long userId);

    User updateUser(Long userId, User user);

    List<User> getAllUsers();
    /**
     * Fetches all users along with their associated ratings and hotel details.
     *
     * <p><b>Refactoring Note (Feb 2026):</b> This method was originally implemented using
     * a sequential 'for-each' loop (the N+1 Problem). The previous version made
     * (1 + N + N*R) network calls, which caused significant latency issues as the
     * user base grew.</p>
     *      getAllUsersBulk
     * <p><b>Optimization:</b> This current implementation uses the <b>Microservice Aggregator
     * Pattern</b> with <b>Batch Fetching</b>. It reduces total network overhead to exactly
     * 3 calls (User DB, Rating Batch API, and Hotel Batch API) by fetching data in bulk
     * and stitching it together in-memory using Java Maps.</p>
     *
     * @return A list of {@link User} objects, each fully populated with its  Rating
     * history and corresponding Hotel information.
     * @see <a href="https://microservices.io/patterns/data/api-composition.html">API Composition Pattern</a>
     */


    List<User> getAllUsersBulk();
    /*
    1. What was earlier?
 We mention the N+1 Problem specifically. This shows that the previous code had a
 "flaw" where it made separate phone calls for every user and every rating.

 2. Why we modified it?
 We mention Latency. By making (1 + N + N*R) calls, the old method was slow.
 If you have 10 users, that's ~40 calls. If you have 100 users, that's ~400 calls.
 We modified it to save the system from crashing under heavy load.

 3. What is the effect?

 The effect is Performance and Scalability
 Old Effect: Response time increased linearly ($O(N)$) with the number of users.
 New Effect: Response time is now Constant ($O(1)$) regarding network calls.
 Whether you have 10 users or 1,000, you only make 3 calls.4.

 How and Why?
 How: We use Batching (fetching everything at once) and In-Memory Mapping (using a Map<Long, Hotel>
 to link data instantly in Java instead of asking the database over and over again).
 Why: Because network travel is the most expensive part of a microservice.
 It is 100x faster to link data in Java memory than to send a request over a network.
     */

}
