create table category_entity
(
    uuid      uuid not null
        primary key,
    dt_create timestamp,
    dt_update timestamp,
    title     varchar(255)
);

alter table category_entity
    owner to postgres;

create unique index category_entity_title_uindex
    on category_entity (title);

create table currency_entity
(
    uuid      uuid not null
        primary key,
    dt_create timestamp,
    dt_update timestamp,
    title     varchar(255)
);

alter table currency_entity
    owner to postgres;

create unique index currency_entity_title_uindex
    on currency_entity (title);


