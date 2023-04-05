CREATE SCHEMA IF NOT EXISTS flyway;

CREATE SCHEMA IF NOT EXISTS copycat;

CREATE TABLE copycat.persons
(
  id varchar(128) NOT NULL
    CONSTRAINT persons_pk
      PRIMARY KEY,
  name varchar(128) NOT NULL,
  age integer NOT NULL
);
