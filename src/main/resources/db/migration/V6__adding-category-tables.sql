create table category
(
    id          varchar(36) not null primary key,
    description varchar(50),
    create_date datetime(6),
    update_date datetime(6) DEFAULT NULL,
    version     bigint      DEFAULT NULL
) ENGINE = InnoDB;

create table beer_category
(
    beer_id     VARCHAR(36) NOT NULL,
    category_id VARCHAR(36) not null,
    primary key (beer_id, category_id),
    constraint beer_mapping
        foreign key (beer_id) REFERENCES beer (id),
    constraint category_mapping
        foreign key (category_id) REFERENCES category (id)
) ENGINE = InnoDB;