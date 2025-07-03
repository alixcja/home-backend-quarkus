alter table address
    alter column street_number drop not null,
    alter column street_name drop not null,
    alter column postal_code drop not null,
    alter column city drop not null;