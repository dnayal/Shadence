# --- !Ups

alter table venue
add venue_city varchar(100)
after address;

# --- !Downs

alter table venue
drop column venue_city;
