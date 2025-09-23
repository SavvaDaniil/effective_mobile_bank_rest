CREATE TABLE card_transfers (
    id bigint NOT NULL PRIMARY KEY,
    card_id_from bigint not null,
    card_id_to bigint not null,
    amount numeric(38,2),
    date_of_add timestamp(6)
);

alter table card_transfers add constraint FKey59ni7awfv5vo7ufh8ipghoi foreign key (card_id_from) references cards (id);
alter table card_transfers add constraint FKowsooe1mmd2my9hisjss1cibx foreign key (card_id_to) references cards (id);