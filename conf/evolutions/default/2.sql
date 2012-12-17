# --- !Ups

alter table user 
add constraint uq_user_email unique (email);

alter table user
add birthdate datetime
after gender;

alter table user
add city varchar(100)
after birthdate;

alter table user
add country varchar(100)
after city;

alter table user
add roles varchar(500)
after country;

# --- !Downs

alter table user
drop index uq_user_email;

alter table user
drop column birthdate;

alter table user
drop column city;

alter table user
drop column country;

alter table user
drop column roles;
