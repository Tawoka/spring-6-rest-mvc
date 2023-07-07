drop table if exists beer_order_shipment;

create table beer_order_shipment
(
    id              VARCHAR(36) NOT NULL PRIMARY KEY,
    beer_order_id   VARCHAR(36) Unique,
    tracking_number varchar(50),
    created_date    datetime(6),
    update_date     datetime(6) DEFAULT NULL,
    version         bigint      default null,
    constraint beer_order_to_shipment_relation
        foreign key (beer_order_id) references beer_order (id)
) ENGINE = InnoDB;

ALTER TABLE beer_order
    add column beer_order_shipment_id varchar(36);

ALTER TABLE beer_order
    add constraint beer_order_shipment_relation
        foreign key (beer_order_shipment_id) references beer_order_shipment(id);