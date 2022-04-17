create table role (id varchar(36) not null, creation_date timestamp not null, delete_time timestamp, name varchar(255) not null, primary key (id));
create table user (id varchar(36) not null, creation_date timestamp not null, delete_time timestamp, email_address varchar(255) not null, full_name varchar(255) not null, status varchar(255) not null, username varchar(255) not null, primary key (id));
create table user_roles (user_id varchar(36) not null, role_id varchar(36) not null, primary key (user_id, role_id));
alter table user_roles add constraint FKrhfovtciq1l558cw6udg0h0d3 foreign key (role_id) references role;
alter table user_roles add constraint FK55itppkw3i07do3h7qoclqd4k foreign key (user_id) references user;