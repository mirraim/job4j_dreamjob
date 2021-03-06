drop table post;
drop table candidate;
drop table users;

CREATE TABLE post
(
    id   SERIAL PRIMARY KEY,
    name TEXT
);

CREATE TABLE candidate
(
    id   SERIAL PRIMARY KEY,
    name TEXT
);

CREATE TABLE users
(
  id SERIAL PRIMARY KEY,
  name TEXT,
  email TEXT UNIQUE,
  password TEXT
);

insert into users(name, email, password) VALUES ('Admin', 'root@local', 'root');

select * from candidate;
select * from post;
select * from users;