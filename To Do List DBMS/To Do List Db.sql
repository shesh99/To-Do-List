create database todoList;

use todoList;

create table user(
	id varchar(5) primary key,
	name varchar(100)not null,
	password varchar(20)not null,
	email varchar(30)not null
);



create table todo(
	id varchar(5)primary key,
	description varchar(100) not null,
	user_id varchar(5),
	constraint foreign key(user_id) references user(id)
);
