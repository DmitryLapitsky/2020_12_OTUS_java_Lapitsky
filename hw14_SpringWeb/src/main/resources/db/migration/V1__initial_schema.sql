create table Client
(
    id   bigserial not null primary key,
    name varchar(50)
);

create table Phone
(
    id   bigserial not null primary key,
    phone varchar(50) not null,
    client_id bigserial not null references Client (id)
);
create index idx_phone_client_id on phone(client_id);

create table Address
(
    id   bigserial not null primary key,
    address varchar(50) not null,
    client_id bigserial not null references Client (id)
);
create index idx_address_client_id on address(client_id);
