/* start create source table */
create table if not exists source
(
    source_id		int auto_increment primary key,
    name			varchar(50)							not null,
    description 	varchar(256)						not null,
    url				varchar(100)						not null
);
/* finish create source table */

/* start create city table */
create table if not exists city
(
    city_id			int auto_increment primary key,
    name			varchar(50)							not null,
    value			varchar(50)							not null,
    fk_source_id	int                                 not null,
    constraint fk_source_id_city foreign key (fk_source_id) references source(source_id)
);
create index ix_fk_source_id_city on city(fk_source_id);
/* finish create city table */

/* start create settlement table */
create table if not exists settlement
(
    settlement_id  	int auto_increment primary key,
    name			varchar(50)							not null,
    value			varchar(50)							not null,
    fk_city_id		int									not null,
    constraint fk_city_id_settlement foreign key (fk_city_id) references city(city_id)
);
create index ix_fk_city_id_settlement on settlement(fk_city_id);
/* finish create settlement table */

/* start create user table */
create table if not exists user
(
    user_id 			int auto_increment primary key,
    user_tg_id			varchar(20)							    not null,
    fk_settlement_id	int 								    null,
    user_status         tinyint   default 1                     not null comment '0 - Stopped; 1 - Active;',
    created_at          timestamp default CURRENT_TIMESTAMP     null,
    constraint fk_settlement_id_user foreign key (fk_settlement_id) references settlement(settlement_id)
);
create index ix_fk_settlement_id_user on user(fk_settlement_id);
/* finish create user table */
