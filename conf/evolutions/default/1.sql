# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table city (
  city_id                   varchar(100) not null,
  name                      varchar(100),
  state_or_county           varchar(100),
  country                   varchar(100),
  create_timestamp          bigint,
  constraint pk_city primary key (city_id))
;

create table entity_photo (
  photo_id                  varchar(100) not null,
  entity_city_id            varchar(100),
  entity_experience_id      varchar(100),
  entity_user_id            varchar(100),
  user_id                   varchar(100),
  location                  varchar(500),
  original_photo            varchar(500),
  large_photo               varchar(500),
  medium_photo              varchar(500),
  small_photo               varchar(500),
  alternate_text            varchar(200),
  photo_order               varchar(3),
  create_timestamp          bigint,
  constraint pk_entity_photo primary key (photo_id))
;

create table experience (
  experience_id             varchar(100) not null,
  venue_id                  varchar(100),
  user_id                   varchar(100),
  category_id               varchar(100),
  name                      varchar(100),
  email                     varchar(100),
  twitter                   varchar(100),
  phone                     varchar(20),
  description               varchar(5000),
  price_description         varchar(500),
  price_rating              integer,
  duration                  integer,
  schedule_description      varchar(500),
  original_source           varchar(500),
  tags                      varchar(200),
  start_date                datetime,
  end_date                  datetime,
  hidden                    tinyint(1) default 0,
  create_timestamp          bigint,
  constraint pk_experience primary key (experience_id))
;

create table experience_category (
  category_id               varchar(100) not null,
  name                      varchar(100),
  create_timestamp          bigint,
  constraint pk_experience_category primary key (category_id))
;

create table user (
  user_id                   varchar(100) not null,
  name                      varchar(100),
  email                     varchar(100),
  password                  varchar(100),
  gender                    varchar(5),
  create_timestamp          bigint,
  constraint pk_user primary key (user_id))
;

create table venue (
  venue_id                  varchar(100) not null,
  city_id                   varchar(100),
  name                      varchar(100),
  address                   varchar(100),
  postcode                  varchar(100),
  latitude                  varchar(100),
  longitude                 varchar(100),
  create_timestamp          bigint,
  constraint pk_venue primary key (venue_id))
;

alter table entity_photo add constraint fk_entity_photo_entityCity_1 foreign key (entity_city_id) references city (city_id) on delete restrict on update restrict;
create index ix_entity_photo_entityCity_1 on entity_photo (entity_city_id);
alter table entity_photo add constraint fk_entity_photo_entityExperien_2 foreign key (entity_experience_id) references experience (experience_id) on delete restrict on update restrict;
create index ix_entity_photo_entityExperien_2 on entity_photo (entity_experience_id);
alter table entity_photo add constraint fk_entity_photo_entityUser_3 foreign key (entity_user_id) references user (user_id) on delete restrict on update restrict;
create index ix_entity_photo_entityUser_3 on entity_photo (entity_user_id);
alter table entity_photo add constraint fk_entity_photo_user_4 foreign key (user_id) references user (user_id) on delete restrict on update restrict;
create index ix_entity_photo_user_4 on entity_photo (user_id);
alter table experience add constraint fk_experience_venue_5 foreign key (venue_id) references venue (venue_id) on delete restrict on update restrict;
create index ix_experience_venue_5 on experience (venue_id);
alter table experience add constraint fk_experience_user_6 foreign key (user_id) references user (user_id) on delete restrict on update restrict;
create index ix_experience_user_6 on experience (user_id);
alter table experience add constraint fk_experience_category_7 foreign key (category_id) references experience_category (category_id) on delete restrict on update restrict;
create index ix_experience_category_7 on experience (category_id);
alter table venue add constraint fk_venue_city_8 foreign key (city_id) references city (city_id) on delete restrict on update restrict;
create index ix_venue_city_8 on venue (city_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table city;

drop table entity_photo;

drop table experience;

drop table experience_category;

drop table user;

drop table venue;

SET FOREIGN_KEY_CHECKS=1;

