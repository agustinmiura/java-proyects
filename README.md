
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

Interface Requirements
======================

Since the Spring Boot backend microservice is a RESTful API and it abstracts the technology choices used on the client, you are welcome to use ANY front-end framework of your choice (React, Angular, Vue, etc.)
* The interface will have 3 screens. When a user logs in, they will see a table of all the other users (including themself). There will be a “delete” button against all rows to delete that particular user.
* There will be a button on the top “create a new user”. This button will redirect the user to the second screen (Create User Screen) where they will be to create a new user
* Create User Screen: This will be a form that takes the information aligned with the USER entity in the database (username, email, full name, and the role that this user should be assigned). On submit, the CREATE API endpoint will be called which sends the new user an email for them to create a password.
* Create Password Screen: When the new user clicks on the unique link to activate and create a password for their account, they are redirected to this create password screen. They will enter their new password and click on the activate button.
* The user should be able to log in after activating their account.

Commands
========

* Go to the folder *docker* and run the command :
`docker-compose up`

* When you aren't using anymore the infrastructure type the command :
`docker-compose down`

TODO
====

* Fix the UUID mapping for the table entities and all the entities.
* data.sql to be executed and load all the data.
* Use environment variables for the api .


