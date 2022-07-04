create table user_entity
(
    uuid     uuid not null
        primary key,
    email    varchar(255),
    login    varchar(255)
        constraint uk_1dnnlrof07tq8gn7s3om9bhao
            unique,
    password varchar(255),
    role     varchar(255)
);

alter table user_entity
    owner to postgres;


