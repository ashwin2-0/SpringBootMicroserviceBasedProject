## 👤 Author

**Ashwin Pandurang Ingle** *Java Developer | 4.5+ Years Experience*  in : Spring Boot, Microservices & Banking / Fintech Domain*

🏗️ Project: **Resilient User-Hotel Ecosystem**
Overview: A distributed microservices architecture designed to manage user profiles, ratings, and hotel information.
The system is built with a centralized configuration, service discovery, and advanced resilience patterns.

1. System Architecture

The project follows a decentralized microservices pattern where each service owns its data and communicates via REST using Feign Clients.

**Service Discovery**: Netflix Eureka (The phonebook of our services).
**Centralized Config**: Spring Cloud Config Server (linked to a private GitHub repo).
**Communication**: Feign Client (Declarative REST client).
**Resilience**: Resilience4j (Circuit Breaker, Retry, Rate Limiter).
**Security**: Okta / Spring Security (OAuth2 & JWT).

2. **Service Catalog**
   
Service Name	Port	Primary Responsibility
SERVICE-REGISTRY	**8761**	Netflix Eureka Server; allows services to find each other by name.
CONFIG-SERVER	**8888**	Pulls and serves .yml files from GitHub to all services.
USER-SERVICE	**8081**	Core service for User management; aggregates Ratings and Hotel data.
RATING-SERVICE	**8083**	Manages user reviews and scores for various hotels.
HOTEL-SERVICE	**8082**	Manages hotel details (name, location, about).
4. Data Models & Entities
Each service uses its own database (Database-per-Service) to ensure loose coupling.

👤 **User Service**
Entity: User
Fields: userId (Long), name (String), email (String), about (String).
Transient Field: List<Rating> ratings (Populated at runtime by calling Rating-Service).
🏨 **Hotel Service**
Entity: Hotel
Fields: id (String), name (String), location (String), about (String).
⭐ R**ating Service**
Entity: Rating
Fields: ratingId (String), userId (String), hotelId (String), rating (int), feedback (String).

4. Key Dependencies & "The Why"
These are the most important libraries in your pom.xml:

Dependency	Purpose	Why we added it

## 4. Key Dependencies & "The Why"

Below are the most important libraries included in the `pom.xml` and the reasoning behind their inclusion:

| Dependency | Purpose | Why we added it |
| :--- | :--- | :--- |
| **Spring Cloud Config** | External Config | To change settings (like Rate Limits) in GitHub without restarting code. |
| **Netflix Eureka Client** | Service Discovery | So `USER-SERVICE` can find `HOTEL-SERVICE` without hardcoding IP addresses. |
| **OpenFeign** | REST Client | Makes calling other services as easy as calling a local Java method. |
| **Resilience4j** | Fault Tolerance | To prevent the whole system from crashing if one service (like Rating) goes down. |
| **Okta Starter** | Security | Handles login and JWT validation automatically so we don't store passwords. |
| **Lombok** | Boilerplate | Removes the need for manual Getters, Setters, and Builders. |

5. Resilience Implementation (The "Onion" Layers)
   
We implemented a triple-layer defense on the getUserById endpoint in the User-Service:

Circuit Breaker: Trips the "fuse" if a service is failing too much, returning a "Dummy User" instantly.
Retry: If a call fails due to a network glitch, it re-attempts the call 3 times with Exponential Backoff.
Rate Limiter: Limits users to 2 requests every 10 seconds to prevent server overload.


6. How to Run the Project
To start the system, run the services in this exact order:

Eureka Server (Wait for Dashboard at :8761).
Config Server (Ensure it connects to GitHub).
Hotel & Rating Services.
User Service.

