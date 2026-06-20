A Spring Boot application for managing users and their card status. The project is built as a multi-module Maven project, split into three modules following a layered architecture.

The application is divided into three modules:

rba-repo – the persistence layer. Holds the domain model (JPA entities) and the repositories used for CRUD operations against the database.
rba-services – the service layer. Contains the business logic, DTOs, mappers (MapStruct), shared utilities and the Kafka consumer. It orchestrates operations and talks to the repository layer.
rba-web – the web layer. Exposes the REST controllers that receive requests from the client, delegate the work to the service layer and return the response. This module also contains the Spring Boot application entry point.
REST API
All endpoints are exposed under the base path /api/users. Users are identified by their OIB (identificationNumber).

Method	Endpoint	Description
POST	/api/users	Create a new user
GET	/api/users/{identificationNumber}	Fetch a user by OIB
DELETE	/api/users/{identificationNumber}	Delete a user by OIB
Responses are wrapped in a common ResponseMessage envelope and errors are handled through a global exception handler.

Card status updates (Kafka)
The application includes a Kafka consumer (CardStatusConsumer) that listens for card-status messages and updates the matching user's card status by OIB. Kafka topic creation is optional and enabled via the app.kafka.enabled=true property.
