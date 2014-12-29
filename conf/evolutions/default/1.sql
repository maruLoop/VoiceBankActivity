# --- !Ups

CREATE TABLE voicebanks(
  id int auto_increment,
  name char(255),
  index(id)
) engine=InnoDB;

CREATE TABLE logs(
  id int,
  filename char(255),
  index(id),
  foreign key(id) references voicebanks(id)
) engine=InnoDB;

# --- !Downs
  drop table voicebanks
  drop table logs