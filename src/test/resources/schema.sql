
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
    LOGIN_ID  CHARACTER VARYING(255)
        constraint UK_ENFM5PATWJQULW8K4WWUO6F60
            unique,
    PASSWORD  CHARACTER VARYING(255),
    WALLET_ID BIGINT,
    constraint FK15LB3SU6CSGSAU55LUUBVT6CQ
        foreign key (WALLET_ID) references WALLET
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

drop table if exists WALLET CASCADE;
create table WALLET
(
    WALLET_ID    BIGINT auto_increment
        primary key,
    BALANCE      BIGINT                 default '0',
    CREATED_DATE TIMESTAMP,
    WALLET_NAME  CHARACTER VARYING(255) default 'My Wallet',
    UPDATED_DATE TIMESTAMP
);




