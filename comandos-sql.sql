create table department(
   id serial primary key,
   name varchar(30) not null
)

create table tb_user(
    id serial primary key,
    name varchar(50) not null,
    username varchar(15) not null,
    password varchar(250) not null,
    role varchar(10) not null,
    created_date timestamp,
    last_modified_date timestamp,
    is_active boolean not null default true,
    id_department int references department(id)
)

create table client(
   id serial primary key,
   client_id varchar(100) not null,
   client_secret varchar(128) not null,
   redirect_uri varchar(255) not null,
   scope varchar(255) not null,
   created_date timestamp,
   last_modified_date timestamp,
   is_active boolean not null default true
)
create unique index idx_client_clientId on client(clientId);