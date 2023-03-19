package com.toy.overall_practice.domain.role;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static lombok.AccessLevel.*;


@Entity
@Getter
@Table(name = "roles")
@NoArgsConstructor(access = PROTECTED)
public class Role {

    @Id
    @Column(name = "role_name")
    @Enumerated(EnumType.STRING)
    private RoleType roleName;

    public Role(RoleType roleType) {
        this.roleName = roleType;
    }

}
