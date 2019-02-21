DROP TABLE  sys_log IF EXISTS;

create table sys_log
(
	id bigint(200) NOT NULL PRIMARY KEY,
	username varchar(50) null ,
	operation varchar(50) null ,
	method varchar(200) null ,
	params varchar(5000) null ,
	time bigint not null ,
	ip varchar(64) null,
	create_date datetime null
)
;

INSERT INTO sys_log (id,username, operation, method, params, time, ip, create_date) VALUES (1000000000001,'admin', '菜单1', 'com()', '', 6, '0:1', '2018-02-24 16:49:08');
INSERT INTO sys_log (id,username, operation, method, params, time, ip, create_date) VALUES (2000000000002,'admin', '菜单2', 'com()', '', 7, '0:1', '2018-02-24 16:49:08');
INSERT INTO sys_log (id,username, operation, method, params, time, ip, create_date) VALUES (2000000000003,'admin', '菜单2', 'com()', '', 7, '0:1', '2018-02-24 16:49:08');
INSERT INTO sys_log (id,username, operation, method, params, time, ip, create_date) VALUES (2000000000004,'admin', '菜单2', 'com()', '', 7, '0:1', '2018-02-24 16:49:08');
INSERT INTO sys_log (id,username, operation, method, params, time, ip, create_date) VALUES (2000000000005,'admin', '菜单2', 'com()', '', 7, '0:1', '2018-02-24 16:49:08');
INSERT INTO sys_log (id,username, operation, method, params, time, ip, create_date) VALUES (2000000000006,'admin', '菜单2', 'com()', '', 7, '0:1', '2018-02-24 16:49:08');
INSERT INTO sys_log (id,username, operation, method, params, time, ip, create_date) VALUES (2000000000007,'admin', '菜单2', 'com()', '', 7, '0:1', '2018-02-24 16:49:08');
INSERT INTO sys_log (id,username, operation, method, params, time, ip, create_date) VALUES (2000000000008,'admin', '菜单2', 'com()', '', 7, '0:1', '2018-02-24 16:49:08');
INSERT INTO sys_log (id,username, operation, method, params, time, ip, create_date) VALUES (2000000000009,'admin', '菜单2', 'com()', '', 7, '0:1', '2018-02-24 16:49:08');
INSERT INTO sys_log (id,username, operation, method, params, time, ip, create_date) VALUES (2000000000010,'admin', '菜单2', 'com()', '', 7, '0:1', '2018-02-24 16:49:08');
INSERT INTO sys_log (id,username, operation, method, params, time, ip, create_date) VALUES (2000000000011,'admin', '菜单2', 'com()', '', 7, '0:1', '2018-02-24 16:49:08');
INSERT INTO sys_log (id,username, operation, method, params, time, ip, create_date) VALUES (2000000000012,'admin', '菜单2', 'com()', '', 7, '0:1', '2018-02-24 16:49:08');
INSERT INTO sys_log (id,username, operation, method, params, time, ip, create_date) VALUES (2000000000013,'admin', '菜单2', 'com()', '', 7, '0:1', '2018-02-24 16:49:08');
INSERT INTO sys_log (id,username, operation, method, params, time, ip, create_date) VALUES (2000000000014,'admin', '菜单2', 'com()', '', 7, '0:1', '2018-02-24 16:49:08');
INSERT INTO sys_log (id,username, operation, method, params, time, ip, create_date) VALUES (2000000000015,'admin', '菜单2', 'com()', '', 7, '0:1', '2018-02-24 16:49:08');
INSERT INTO sys_log (id,username, operation, method, params, time, ip, create_date) VALUES (2000000000016,'admin', '菜单2', 'com()', '', 7, '0:1', '2018-02-24 16:49:08');
INSERT INTO sys_log (id,username, operation, method, params, time, ip, create_date) VALUES (2000000000017,'admin', '菜单2', 'com()', '', 7, '0:1', '2018-02-24 16:49:08');
INSERT INTO sys_log (id,username, operation, method, params, time, ip, create_date) VALUES (2000000000018,'admin', '菜单2', 'com()', '', 7, '0:1', '2018-02-24 16:49:08');
INSERT INTO sys_log (id,username, operation, method, params, time, ip, create_date) VALUES (2000000000019,'admin', '菜单2', 'com()', '', 7, '0:1', '2018-02-24 16:49:08');
INSERT INTO sys_log (id,username, operation, method, params, time, ip, create_date) VALUES (2000000000020,'admin', '菜单2', 'com()', '', 7, '0:1', '2018-02-24 16:49:08');
INSERT INTO sys_log (id,username, operation, method, params, time, ip, create_date) VALUES (2000000000021,'admin', '菜单2', 'com()', '', 7, '0:1', '2018-02-24 16:49:08');
INSERT INTO sys_log (id,username, operation, method, params, time, ip, create_date) VALUES (2000000000022,'admin', '菜单2', 'com()', '', 7, '0:1', '2018-02-24 16:49:08');
INSERT INTO sys_log (id,username, operation, method, params, time, ip, create_date) VALUES (2000000000023,'admin', '菜单2', 'com()', '', 7, '0:1', '2018-02-24 16:49:08');