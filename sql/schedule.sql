create table operation_entity
(
    uuid          uuid not null
        primary key,
    description   varchar(255),
    dt_create     timestamp(3),
    dt_update     timestamp(3),
    value         numeric(19, 2),
    account_uuid  uuid,
    currency_uuid uuid,
    category_uuid uuid
);

alter table operation_entity
    owner to postgres;

create table schedule_entity
(
    uuid       uuid not null
        primary key,
    dt_create  timestamp(3),
    dt_update  timestamp(3),
    interval   bigint,
    start_time timestamp(3),
    time_unit  varchar(255),
    stop_time  timestamp(3)
);

alter table schedule_entity
    owner to postgres;

create table scheduled_operation_entity
(
    uuid                  uuid not null
        primary key,
    dt_create             timestamp(3),
    dt_update             timestamp(3),
    operation_entity_uuid uuid
        constraint fkhxtbx4odyuy64duqfxggdcr7n
            references operation_entity,
    schedule_entity_uuid  uuid
        constraint fkohrpjlk42npenjbssbtk52uh2
            references schedule_entity,
    finance_user          varchar
);

alter table scheduled_operation_entity
    owner to postgres;


