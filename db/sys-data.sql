insert into sys_users values(1,'admin','95657d3e3052fb39d70d610c70a9a575','87cc486c53b49f72f5b96bb55d93bc7f', '超级管理员', false);
insert into sys_users values(2,'tycoding','7de3848c92d39e98f7c74139f1a079d7','6478ccf88032592fe9396f008408400b', '普通管理员', false);
insert into sys_users values(3,'涂陌','7247c372a4aae00c2f78239739384c0b','f0a35a4d99e23ad4a59616bd4d8eea02', '普通用户', false);

insert into sys_roles values(21, 'admin', '总经理', 0, false);

insert into sys_roles values(22, 'personnel', '人事部', 0, false);
insert into sys_roles values(23, 'personnel-resource', '人力资源部部长', 22, false);
insert into sys_roles values(24, 'personnel-administration', '行政部部长', 22, false);

insert into sys_roles values(26, 'technical', '技术部', 0, false);
insert into sys_roles values(27, 'technical-development', '项目经理', 26, false);
insert into sys_roles values(28, 'technical-maintenance', '项目组组长', 26, false);

insert into sys_roles values(29, 'security', '安全部', 0, false);
insert into sys_roles values(30, 'security-net', '网络安全部负责人', 29, false);
insert into sys_roles values(31, 'security-test', '项目安全测试人员', 29, false);

insert into sys_users_roles values(1,1, 21);
insert into sys_users_roles values(2,2, 27);
insert into sys_users_roles values(3,2, 30);
insert into sys_users_roles values(4,3, 33);
insert into sys_users_roles values(5,3, 34);

insert into sys_permissions values(31, 'resource:create','用户新增', 23, false);
insert into sys_permissions values(32, 'user:update','用户修改', 23, false);
insert into sys_permissions values(33, 'user:delete','用户删除', 23, false);
insert into sys_permissions values(34, 'user:view', '用户查看', 23, false);
insert into sys_permissions values(35, 'role:update', '角色更新', 21, false);
insert into sys_permissions values(36, 'role:delete', '角色删除', 21, false);
insert into sys_permissions values(37, 'role:create', '角色创建', 21, false);
insert into sys_permissions values(38, 'role:view', '角色查看', 21, false);
insert into sys_permissions values(39, 'permission:delete', '权限删除', 21, false);
insert into sys_permissions values(40, 'permission:create', '权限创建', 21, false);
insert into sys_permissions values(41, 'permission:view', '权限查看', 21, false);
insert into sys_permissions values(42, 'project:manage', '项目管理', 27, false);
insert into sys_permissions values(43, 'project:distribution', '项目任务分配', 27, false);
insert into sys_permissions values(44, 'project:develop', '项目开发', 28, false);
insert into sys_permissions values(45, 'project:maintain', '项目维护', 28, false);
insert into sys_permissions values(46, 'security:maintain', '安全维护', 30, false);
insert into sys_permissions values(47, 'security:develop', '安全功能设计', 30, false);
insert into sys_permissions values(48, 'security:test', '安全测试', 31, false);
insert into sys_permissions values(49, 'security:bug-test', 'BUG检测', 31, false);

insert into sys_roles_permissions values (1,21, 35);
insert into sys_roles_permissions values (2,21, 36);
insert into sys_roles_permissions values (3,21, 37);
insert into sys_roles_permissions values (4,21, 38);
insert into sys_roles_permissions values (5,21, 39);
insert into sys_roles_permissions values (6,21, 40);
insert into sys_roles_permissions values (7,21, 41);
insert into sys_roles_permissions values (8,23, 31);
insert into sys_roles_permissions values (9,23, 32);
insert into sys_roles_permissions values (10,23, 33);
insert into sys_roles_permissions values (11,23, 34);
insert into sys_roles_permissions values (12,27, 42);
insert into sys_roles_permissions values (13,27, 43);
insert into sys_roles_permissions values (14,28, 44);
insert into sys_roles_permissions values (15,28, 45);
insert into sys_roles_permissions values (16,30, 46);
insert into sys_roles_permissions values (17,30, 47);
insert into sys_roles_permissions values (18,31, 48);
insert into sys_roles_permissions values (19,31, 49);

