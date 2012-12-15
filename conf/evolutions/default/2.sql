# --- !Ups

alter table user
add constraint uq_user_email unique (email);

# --- !Downs

alter table user
drop index uq_user_email;



