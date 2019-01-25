DROP TABLE  person IF EXISTS;

-- create table sys_log
-- (
-- 	id bigint(200) NOT NULL PRIMARY KEY,
-- 	username varchar(50) null ,
-- 	operation varchar(50) null ,
-- 	method varchar(200) null ,
-- 	params varchar(5000) null ,
-- 	time bigint not null ,
-- 	ip varchar(64) null,
-- 	create_date datetime null
-- )
-- ;

create table person
(
-- 	id  int(11) NOT NULL PRIMARY KEY,
	name  varchar(20) null ,
	age  varchar(11) null ,
	nation  varchar(20)  NULL ,
	address  varchar(20)  NULL
)
;