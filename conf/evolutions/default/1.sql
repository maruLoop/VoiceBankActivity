# --- !Ups

CREATE TABLE voicebanks(
  id int AUTO_INCREMENT PRIMARY KEY,
  name char(255) NOT NULL,
  timestamp timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  index(id)
) engine=InnoDB;

CREATE TABLE logs(
  id int,
  filename char(255) NOT NULL,
  count int default 1,
  timestamp timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  index(id),
  foreign key(id) references voicebanks(id)
) engine=InnoDB;

# --- !Downs
  drop table logs
  drop table voicebanks