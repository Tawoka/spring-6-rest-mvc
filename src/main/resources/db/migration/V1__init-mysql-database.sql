SET FOREIGN_KEY_CHECKS = 0;
drop table if exists beer;
drop table if exists customer;
drop table if exists beer_order_line;
drop table if exists beer_order;
drop table if exists beer_category;
drop table if exists category;
drop table if exists beer_order_shipment;
SET FOREIGN_KEY_CHECKS = 1;

create table beer
(
    price            decimal(38, 2) not null,
    quantity_on_hand integer,
    style            smallint       not null,
    version          integer,
    create_date      datetime(6),
    update_date      datetime(6),
    id               varchar(36)    not null,
    name             varchar(50)    not null,
    upc              varchar(255)   not null,
    primary key (id)
) engine = InnoDB;
create table customer
(
    version      integer,
    created_on   datetime(6),
    last_updated datetime(6),
    id           varchar(36)  not null,
    name         varchar(255) not null,
    primary key (id)
) engine = InnoDB;
