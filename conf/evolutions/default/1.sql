# --- !Ups

CREATE TABLE voicebank(
  id int AUTO_INCREMENT PRIMARY KEY,
  name char(255) NOT NULL,
  timestamp timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  index(id)
) engine=InnoDB;

CREATE TABLE activity(
  id int,
  filename char(255) NOT NULL,
  count int default 1 NOT NULL,
  timestamp timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  index(id),
  foreign key(id) references voicebank(id)
) engine=InnoDB;

# --- !Downs
  drop table activity;
  drop table voicebank;