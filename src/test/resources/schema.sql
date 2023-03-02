
drop table if exists ROLES CASCADE;
create table ROLES
(
    ROLE_NAME CHARACTER VARYING(255) not null
        primary key
);

drop table if exists MEMBER CASCADE;
create table MEMBER
(
    MEMBER_ID BIGINT auto_increment
        primary key,
    LOGIN_ID  CHARACTER VARYING(255),
    PASSWORD  CHARACTER VARYING(255)
);

drop table if exists MEMBER_ROLE CASCADE;
create table MEMBER_ROLE
(
    MEMBER_ROLE_ID BIGINT auto_increment
        primary key,
    MEMBER_ID      BIGINT,
    ROLE_NAME      CHARACTER VARYING(255),
    constraint FK2G1TG0FXYHJT5AYOMIOVEWWQW
        foreign key (ROLE_NAME) references ROLES,
    constraint FK34G7EPQLCXQLOEWKU3AOQHHMG
        foreign key (MEMBER_ID) references MEMBER
);



