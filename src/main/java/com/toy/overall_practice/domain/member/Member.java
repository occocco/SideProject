package com.toy.overall_practice.domain.member;

import com.toy.overall_practice.common.MemberRole;
import com.toy.overall_practice.common.RoleType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "login_id")
    private String loginId;

    private String password;
    @OneToMany(mappedBy = "member", cascade = ALL)
    private Set<MemberRole> role = new HashSet<>();

    public static Member createMember(String loginId, String password, RoleType roleType) {
        Member member = new Member(loginId,password);
        MemberRole memberRole = new MemberRole(roleType);
        member.addRole(memberRole);
        return member;
    }

    private Member(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }

    private void addRole(MemberRole memberRole) {
        memberRole.connectionMember(this);
        role.add(memberRole);
    }
}
