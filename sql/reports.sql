create table account_entity
(
    uuid uuid not null
        primary key
);

alter table account_entity
    owner to postgres;

create table category_entity
(
    uuid uuid not null
        primary key
);

alter table category_entity
    owner to postgres;

create table report_param_entity
(
    uuid      uuid not null
        constraint report_param_entity_pk
            primary key,
    from_time timestamp,
    to_time   timestamp
);

alter table report_param_entity
    owner to postgres;

create table report_param_account
(
    report_param_uuid uuid not null
        constraint report_param_account_report_param_entity__fk
            references report_param_entity,
    account_uuid      uuid not null
        constraint fki87gie0ql0iy75c4k1n17nop1
            references account_entity
);

alter table report_param_account
    owner to postgres;

create table report_param_categories
(
    report_param_uuid uuid not null
        constraint report_param_categories_report_param_entity__fk
            references report_param_entity,
    category_uuid     uuid not null
        constraint fkas7lte63n1ubihytik9ssuxss
            references category_entity
);

alter table report_param_categories
    owner to postgres;

create table report_entity
(
    uuid                     uuid not null
        primary key,
    description              varchar(255),
    dt_create                timestamp,
    dt_update                timestamp,
    status                   varchar,
    report_param_entity_uuid uuid
        constraint report_entity_report_param_entity__fk
            references report_param_entity,
    report_type              varchar,
    finance_user             varchar
);

alter table report_entity
    owner to postgres;


