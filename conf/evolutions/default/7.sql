# --- !Ups

create table featured_entity (
  entity_id                 varchar(100) not null,
  entity_type               varchar(20),
  feature_description       varchar(100),
  specific_information      varchar(200),
  create_timestamp          bigint,
  constraint pk_featured_entity primary key (entity_id));

# --- !Downs

drop table featured_entity;
