# --- !Ups

CREATE TABLE voicebank(
  id int AUTO_INCREMENT PRIMARY KEY,
  name char(255) UNIQUE,
  timestamp timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  index(id)
) engine=InnoDB;

CREATE TABLE activity(
  id int,
  filename char(255),
  count int default 1 NOT NULL,
  timestamp timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
   PRIMARY KEY (id, filename),
  foreign key(id) references voicebank(id)
) engine=InnoDB;

# --- !Downs
  drop table activity;
  drop table voicebank;