# --- !Ups

create table collection (
  collection_id             varchar(100) not null,
  name                      varchar(100),
  description               varchar(2000),
  user_id                   varchar(100),
  create_timestamp          bigint,
  constraint pk_collection primary key (collection_id))
;

create table experience_collection (
  experience_experience_id       varchar(100) not null,
  collection_collection_id       varchar(100) not null,
  constraint pk_experience_collection primary key (experience_experience_id, collection_collection_id))
;

alter table collection add constraint fk_collection_user_1 foreign key (user_id) references user (user_id) on delete restrict on update restrict;

create index ix_collection_user_1 on collection (user_id);

alter table experience_collection add constraint fk_experience_collection_expe_01 foreign key (experience_experience_id) references experience (experience_id) on delete restrict on update restrict;

alter table experience_collection add constraint fk_experience_collection_coll_02 foreign key (collection_collection_id) references collection (collection_id) on delete restrict on update restrict;

# --- !Downs

drop table collection;
drop table experience_collection;
