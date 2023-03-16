insert into ROLES (ROLE_NAME) values ('MEMBER');

insert into WALLET values (10,0,now(),'my wallet',now());
insert into WALLET values (11,0,now(),'my wallet',now());

insert into MEMBER values (10,'123','$2a$10$Egma42qHHvItgprXUolTA.jpM4FD57EuwSQU3tIRRl2.cHyZ3ClTu', 10);
insert into MEMBER_ROLE values (10, 10, 'MEMBER');

insert into MEMBER values (13,'1234','$2a$10$Egma42qHHvItgprXUolTA.jpM4FD57EuwSQU3tIRRl2.cHyZ3ClTu', null);
insert into MEMBER_ROLE values (13, 13, 'MEMBER');

insert into MEMBER values (11,'MemberC','$2a$10$Egma42qHHvItgprXUolTA.jpM4FD57EuwSQU3tIRRl2.cHyZ3ClTu', 11);
insert into MEMBER_ROLE values (11, 11, 'MEMBER');

insert into MEMBER values (12,'MemberE','$2a$10$Egma42qHHvItgprXUolTA.jpM4FD57EuwSQU3tIRRl2.cHyZ3ClTu', null);
insert into MEMBER_ROLE values (12, 12, 'MEMBER');