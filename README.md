# App is deployed and running!
- Visit the official Swagger UI API Docs: https://flashcards-rest-api-production.up.railway.app/swagger-ui/index.html
- Or call the endpoints with some http client, base URL is: https://flashcards-rest-api-production.up.railway.app
- If the app is slow, please be patient, it is running on a free service!

# Overview 
This is a REST API that allows users to organize their flashcards to study for their exams or learn new information about a topic they are interested in.

# Technologies:
- Java 17
- SpringBoot with Spring MVC, Spring JPA, Spring Security  
- MySQL
- JWT Authentication
- Swagger UI (springdoc library)
- Deployed for free on Railway

# Basic instructions to interact with the API:
1) Create a new user with role "ROLE_ADMIN" (the other role is "ROLE_USER", but it has less privileges)
2) Use your JWT and user id to create a new study session
3) Use your user id and the id of your study session to create some flashcards
4) Use any other endpoint you like

# Use cases of this API
1) User can register
2) User can log in
3) User can get all categories
4) User can perform CRUD operations for her own study sessions
5) User can perform CRUD operations for her own flashcards

# Testing 
This API was developed following the Test Driven Development (TDD) guidelines, that is the reason why it has almost 100% code coverage. 
