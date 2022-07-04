create table balance_entity
(
    uuid      uuid not null
        primary key,
    balance   numeric(19, 2),
    dt_create timestamp(3),
    dt_update timestamp(3)
);

alter table balance_entity
    owner to postgres;

create table account_entity
(
    uuid          uuid not null
        primary key,
    description   varchar(255),
    dt_create     timestamp(3),
    dt_update     timestamp(3),
    title         varchar(255),
    type          varchar(255),
    balance_uuid  uuid
        constraint fkrxfa39dwesf0r38txgk21ur1t
            references balance_entity,
    currency_uuid uuid,
    finance_user  varchar
);

alter table account_entity
    owner to postgres;

create table operation_entity
(
    uuid                uuid not null
        primary key,
    date_operation      timestamp(3),
    description         varchar(255),
    dt_create           timestamp(3),
    dt_update           timestamp(3),
    value               numeric(19, 2),
    account_entity_uuid uuid
        constraint fkn2c4nk9okkfh42v7cc5863kru
            references account_entity,
    category_uuid       uuid,
    currency_uuid       uuid
);

alter table operation_entity
    owner to postgres;


