package com.toy.overall_practice.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Getter
@Table(name = "roles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {

    @Id
    @Column(name = "role_name")
    @Enumerated(EnumType.STRING)
    private RoleType roleName;

    public Role(RoleType roleType) {
        this.roleName = roleType;
    }

}
