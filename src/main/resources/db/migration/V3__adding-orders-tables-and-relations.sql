drop table if exists beer_order;
drop table if exists beer_order_line;

create table beer_order
(
    id           VARCHAR(36) NOT NULL,
    create_date  datetime(6),
    update_date  datetime(6),
    customer_ref VARCHAR(255),
    version      bigint,
    customer_id  VARCHAR(36) NOT NULL,
    primary key (id),
    constraint customer_relation
        foreign key (customer_id) references customer (id)
) ENGINE = InnoDB;

create table beer_order_line
(
    id                 VARCHAR(36) NOT NULL,
    beer_id            VARCHAR(36) NOT NULL,
    create_date        datetime(6),
    update_date        datetime(6),
    version            bigint,
    order_quantity     int,
    allocated_quantity int,
    beer_order_id      VARCHAR(36) NOT NULL,
    primary key (id),
    constraint beer_relation
        foreign key (beer_id) references beer (id),
    constraint beer_order_relation
        foreign key (beer_order_id) references beer_order(id)
) ENGINE = InnoDB;