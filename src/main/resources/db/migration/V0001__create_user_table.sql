create table USER (
    id   int(11) NOT NULL AUTO_INCREMENT,
    name varchar(50) not null,
    hashedPassword varchar(100) not null
);

CREATE TABLE USER_AUTHORITY (
  id int(11) NOT NULL AUTO_INCREMENT,
  userid int(11) NOT NULL,
  name varchar(50) NOT NULL
);

insert into USER (name, hashedPassword) values ('user1', '$2a$10$qAn7vKlXkipcEStzK0pH5uReZL2IeshCCBflS/ol9SAlk/M6Q/ASW');
insert into USER (name, hashedPassword) values ('user2', '$2a$10$qAn7vKlXkipcEStzK0pH5uReZL2IeshCCBflS/ol9SAlk/M6Q/ASW');
insert into USER (name, hashedPassword) values ('admin', '$2a$10$ovDbmGgS3cRdUVZSj6kXdujuqCNurCX.d5gMYWVbfLykgCCyu9X9S');

insert into USER_AUTHORITY (userid, name) values (1, 'ROLE_USER');
insert into USER_AUTHORITY (userid, name) values (2, 'ROLE_USER');
insert into USER_AUTHORITY (userid, name) values (3, 'ROLE_ADMIN');
