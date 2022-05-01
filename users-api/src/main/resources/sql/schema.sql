-- users."user" definition

-- Drop table

-- DROP TABLE users."user";

CREATE TABLE users."user" (
	id varchar(36) NOT NULL,
	creation_date timestamp NOT NULL,
	delete_time timestamp NULL,
	email_address varchar(255) NOT NULL,
	full_name varchar(255) NOT NULL,
	status varchar(255) NOT NULL,
	username varchar(255) NOT NULL,
	CONSTRAINT user_pkey PRIMARY KEY (id)
);

-- users."role" definition

-- Drop table

-- DROP TABLE users."role";

CREATE TABLE users."role" (
	id varchar(36) NOT NULL,
	creation_date timestamp NOT NULL,
	delete_time timestamp NULL,
	"name" varchar(255) NOT NULL,
	CONSTRAINT role_pkey PRIMARY KEY (id)
);

-- users.user_roles definition

-- Drop table

-- DROP TABLE users.user_roles;

CREATE TABLE users.user_roles (
	user_id varchar(36) NOT NULL,
	role_id varchar(36) NOT NULL,
	CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id)
);

-- users.user_roles foreign keys

ALTER TABLE users.user_roles ADD CONSTRAINT fk55itppkw3i07do3h7qoclqd4k FOREIGN KEY (user_id) REFERENCES users."user"(id);
ALTER TABLE users.user_roles ADD CONSTRAINT fkrhfovtciq1l558cw6udg0h0d3 FOREIGN KEY (role_id) REFERENCES users."role"(id);