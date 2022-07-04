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
        primary key,
    from_time timestamp,
    to_time   timestamp
);

alter table report_param_entity
    owner to postgres;

create table report_param_account
(
    report_param_uuid uuid not null
        constraint fkp71quvwoxb0k96a3kwha7vfnc
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
        constraint fk4voyjtkjynsabxjyi8w3v6jj4
            references report_param_entity,
    category_uuid     uuid not null
        constraint fkas7lte63n1ubihytik9ssuxss
            references category_entity
);

alter table report_param_categories
    owner to postgres;

create table schedule_entity
(
    uuid       uuid not null
        primary key,
    dt_create  timestamp,
    dt_update  timestamp,
    interval   bigint,
    start_time timestamp,
    stop_time  timestamp,
    time_unit  varchar(255)
);

alter table schedule_entity
    owner to postgres;

create table scheduled_report_entity
(
    uuid                     uuid not null
        primary key,
    dt_create                timestamp,
    dt_update                timestamp,
    report_type              varchar(255),
    finance_user             varchar(255),
    report_param_entity_uuid uuid
        constraint fkq27y5fqo781xcs43qiqrrd4ri
            references report_param_entity,
    schedule_entity_uuid     uuid
        constraint fksyg0c1e2qpj1x45qor91w8e36
            references schedule_entity
);

alter table scheduled_report_entity
    owner to postgres;


