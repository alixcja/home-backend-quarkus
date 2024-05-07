create sequence BOOKING_ENTITY_TABLE_SEQ start WITH 1 increment 1;
create sequence BOOKING_TABLE_SEQ start WITH 1 increment 1;
create sequence FAVORITE_TABLE_SEQ start WITH 1 increment 1;

    create table Booking (
        id int8 not null,
        user_id int8 not null,
        bookingEntity_id int8 not null,
        startDate timestamp not null,
        endDate timestamp not null,
        bookingDate timestamp not null,
        isCancelled boolean not null,
        isReturned boolean not null,
        primary key (id)
    );

    create table BookingEntity (
            id int8 not null,
            name varchar(35) not null,
            description varchar(100),
            type varchar(31) not null,
            isArchived boolean not null,
            addedOn timestamp not null,
            consoleType varchar(25),
            primary key (id)
    );

    create table Favorite (
       id int8 not null,
        bookingEntity_id int8,
        user_id int8,
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