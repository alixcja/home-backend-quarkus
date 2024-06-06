create sequence BOOKING_ENTITY_TABLE_SEQ start WITH 1 increment 1;
create sequence BOOKING_TABLE_SEQ start WITH 1 increment 1;
create sequence FAVORITE_TABLE_SEQ start WITH 1 increment 1;

    create table Booking (
        id integer not null,
        user_id varchar(255) not null,
        bookingEntity_id integer not null,
        startDate timestamp not null,
        endDate timestamp not null,
        bookingDate timestamp not null,
        isCancelled boolean not null,
        isReturned boolean not null,
        primary key (id)
    );

    create table BookingEntity (
            id integer not null,
            name varchar(100) not null,
            description varchar,
            type varchar(10) not null,
            isArchived boolean not null,
            addedOn date not null,
            consoleType varchar(50),
            primary key (id)
    );

    create table Favorite (
       id integer not null,
        bookingEntity_id integer,
        user_id varchar(255),
        primary key (id)
    );

    alter table if exists Booking
       add constraint FKfxb379sb83igxsm293upv7cpp
       foreign key (bookingEntity_id)
       references BookingEntity;


    alter table if exists Favorite
       add constraint FKh88flvcnul34rmnce565mcw8t
       foreign key (bookingEntity_id)
       references BookingEntity;