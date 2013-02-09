# --- !Ups

alter table experience
add book_now varchar(500)
after original_source;

# --- !Downs

alter table experience
drop column book_now;
