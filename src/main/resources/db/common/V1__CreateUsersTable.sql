CREATE TABLE users
(
    name   VARCHAR(255) NOT NULL,
    salary DOUBLE       NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (name)
);
