CREATE TABLE users (
    id bigint NOT NULL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
	role VARCHAR(255) not null check (role in ('USER','ADMIN')),
	date_of_add TIMESTAMP(6),
	date_of_update TIMESTAMP(6)
);

