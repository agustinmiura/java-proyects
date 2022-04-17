
# Functional Requirements

* You are required to create a microservice that can CREATE, READ, UPDATE and DELETE a user entity from the database. In other words, a CRUD application.
The database will have 2 entities: one for USER, and one for ROLE. Make sure you are able to associate users with their chosen roles based on a foreign key.
* The roles table will be static and will have 3 types of roles in the system: Level 1, Level 2, and Level 3.
A user will be assigned exactly one role.
* READ: This endpoint will retrieve all the existing USERs and send them back to the client. The front end will show this data in a tabular form.
A user with role “Level 1” will only be able to view the user data
The USER entity will have the following columns: “username”, “fullName”, “role”, “emailAddress”, and “status” (status will be either “pending” or “active”)
* CREATE: this endpoint will be used to create a new user. The endpoint request should have all the data required by the USER entity to be able to submit, otherwise, it will throw an error (which should be properly displayed on the frontend).
If the CREATE request is successful, the server will send out an email to the user on their email address with a unique activation link.
Once the user clicks on that link, they will be redirected to <your application url>/create-password screen.
The user will create a password here. They will then be redirected to the system dashboard. On the backend, you will create an endpoint for UPDATE where you change the status of the user from “pending” to “active” and also you will store their password.
The user should be able to log into the system using their email and password.
* DELETE: This endpoint will delete the USER with a particular ID.
This is how the permissions change depending on the role assigned to the user:

Commands
========

* Go to the folder *docker* and run the command :
`docker-compose up`

* When you aren't using anymore the infrastructure type the command :
`docker-compose down`

Features:
=========

* Spring boot 2 api with unit tests and integration tests .
* Jpa connection with a Postgresql db .
* Connectivity against an external api to check emails .
* Layer separation .
* Circuit breaker features with Resilience4J .
* Caching with Redis.

Run unit tests
==============

* Inside the api directory execute the command :
```
gradle test
```

Setup the email account for the api request
===========================================

* Sign up to *https://rapidapi.com/leeann/api/zerobounce1/*
* Create an app .
* Signup for the *ZeroBounce API* *https://rapidapi.com/leeann/api/zerobounce1*
* Fill the *application.properties* like this :
```
rapidapi.email.api=https://url.com
rapidapi.hosts=host.p.rapidapi.com
rapidapi.key=keyHere
```

Setup
=====

* Copy the *application.properties.sample* to *application.properties*
* Setup the api account (see above).
* Start the docker compose (see above).
* Config the *application.properties* .
* Run the api : 
```
gradle bootRun
```


