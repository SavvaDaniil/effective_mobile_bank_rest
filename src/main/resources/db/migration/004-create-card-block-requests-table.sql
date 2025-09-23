CREATE TABLE card_block_requests (
    id bigint NOT NULL PRIMARY KEY,
    user_id bigint not null,
    card_id bigint not null,
    date_of_add timestamp(6),
    date_of_blocked timestamp(6)
 );

alter table card_block_requests add constraint FKneoesffjvyhidu7b2e53vvrhj foreign key (card_id) references cards (id);
alter table card_block_requests add constraint FKr71murwo3icr675qgv6ti7tqp foreign key (user_id) references users (id);