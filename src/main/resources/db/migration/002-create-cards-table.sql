CREATE TABLE cards (
    id bigint NOT NULL PRIMARY KEY,
    card_number VARCHAR(16) NOT NULL UNIQUE,
	user_id bigint NOT NULL,
	date_expiration DATE,
	status VARCHAR(255) not null check (status in ('ACTIVE','BLOCKED','EXPIRED')),
	balance numeric(38,2),
	date_of_add timestamp(6),
	date_of_update timestamp(6)
);

ALTER TABLE cards
ADD CONSTRAINT FKcmanafgwbibfijy2o5isfk3d5
FOREIGN KEY (user_id)
REFERENCES users (id);