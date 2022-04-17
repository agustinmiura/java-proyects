/*
Roles
*/
insert into users.role values ('role_1', now(), null , 'role_1');
insert into users.role values ('role_2', now(), null , 'role_2');
insert into users.role values ('role_3', now(), null , 'role_3');

/*
Users
*/
INSERT INTO users."user"
(id, creation_date, delete_time, email_address, full_name,status, username)
VALUES('id1', now(), NULL, 'name1@gmail.com', 'full name1', 'status', 'username1');
INSERT INTO users."user"
(id, creation_date, delete_time, email_address, full_name,status, username)
VALUES('id2', now(), NULL, 'name2@gmail.com', 'full name2', 'status', 'username2');
INSERT INTO users."user"
(id, creation_date, delete_time, email_address, full_name,status, username)
VALUES('id3', now(), NULL, 'name3@gmail.com', 'full name3', 'status', 'username3');
INSERT INTO users."user"
(id, creation_date, delete_time, email_address, full_name,status, username)
VALUES('id4', now(), NULL, 'name4@gmail.com', 'full name4', 'status', 'username4');
INSERT INTO users."user"
(id, creation_date, delete_time, email_address, full_name,status, username)
VALUES('id5', now(), NULL, 'name5@gmail.com', 'full name5', 'status', 'username5');

/*
user_roles
*/
insert into users.user_roles values ('id1', 'role_1');
insert into users.user_roles values ('id2', 'role_2');
insert into users.user_roles values ('id3', 'role_3');
insert into users.user_roles values ('id4', 'role_1');
insert into users.user_roles values ('id5', 'role_2');