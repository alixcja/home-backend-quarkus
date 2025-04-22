create sequence menucard_seq INCREMENT BY 50;

create table MenuCard (
    id integer not null,
    shop_id integer not null,
    number integer,
    fileName varchar(255),
    primary key (id)
);