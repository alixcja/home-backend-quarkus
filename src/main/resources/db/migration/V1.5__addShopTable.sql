create sequence shop_seq INCREMENT BY 50;
create sequence address_seq INCREMENT BY 50;
create sequence openinghours_seq INCREMENT BY 50;

create table Shop (
    id integer not null,
    name varchar(255) not null,
    website varchar(255),
    phone_number varchar(255),
    address_id integer,
    last_updated timestamp not null,
    is_archived boolean not null,
    primary key (id)
);

create table Address(
    id integer not null,
    street_number varchar(255) not null,
    street_name varchar(255) not null,
    postal_code varchar(255) not null,
    city varchar(255) not null,
    primary key (id)
);

create table OpeningHours(
    id integer not null,
    shop_id integer not null,
    weekday varchar(255) not null,
    from_date time without time zone not null,
    to_date time without time zone not null,
    last_updated date not null,
    primary key (id)
);

create table Shop_Tags(
    id integer not null,
    shop_id integer not null,
    tag integer not null,
    primary key (id)
);