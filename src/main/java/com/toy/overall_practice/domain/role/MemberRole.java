package com.toy.overall_practice.domain.role;

import com.toy.overall_practice.domain.member.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Entity
@Table(name = "member_role")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class MemberRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_role_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "role_name")
    private Role role;

    public void ConnectMember(Member member) {
        this.member = member;
    }

    public MemberRole(RoleType roleType) {
        this.role = new Role(roleType);
    }

}
