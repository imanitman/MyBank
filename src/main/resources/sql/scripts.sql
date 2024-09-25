create table users(username varchar(50) not null primary key,password varchar(500) not null,enabled boolean not null);
create table authorities (username varchar(50) not null,authority varchar(50) not null,constraint fk_authorities_users foreign key(username) references users(username));
create unique index ix_auth_username on authorities (username,authority);

 INSERT IGNORE INTO `users` VALUES ('user', '{noop}NoahNguyen@12345', '1');
 Insert ignore into `authorities` VALUES ('user', 'read');

 Insert ignore into `users` values ('admin', '{bcrypt}$2a$12$7cChBYPkEsyOnwcgydFr..meLBFKpAmOMrnIPdvU1yZaSrv7E533e', '1');
 insert ignore into `authorities` values ('admin', 'admin');