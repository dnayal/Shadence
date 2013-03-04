# --- !Ups

create table provider_experience (
  id                        varchar(100) not null,
  name                      varchar(100),
  email                     varchar(100),
  address                   varchar(200),
  url                       varchar(500),
  description               varchar(2000),
  create_timestamp          bigint,
  constraint pk_provider_experience primary key (id));

# --- !Downs

drop table provider_experience;