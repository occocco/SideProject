package com.toy.overall_practice.domain.member;

import com.toy.overall_practice.domain.goods.Goods;
import com.toy.overall_practice.domain.role.MemberRole;
import com.toy.overall_practice.domain.role.RoleType;
import com.toy.overall_practice.domain.wallet.Wallet;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "login_id", unique = true)
    private String loginId;

    private String password;
    @OneToMany(mappedBy = "member", cascade = ALL)
    private Set<MemberRole> role = new HashSet<>();

    @OneToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @OneToMany(mappedBy = "seller", cascade = ALL)
    private List<Goods> goods = new ArrayList<>();

    public static Member createMember(String loginId, String password, RoleType roleType) {
        Member member = new Member(loginId, password);
        MemberRole memberRole = new MemberRole(roleType);
        member.addRole(memberRole);
        return member;
    }

    public void modifyMember(String password) {
        this.password = password;
    }

    private Member(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }

    private void addRole(MemberRole memberRole) {
        memberRole.ConnectMember(this);
        role.add(memberRole);
    }

    public void ConnectWallet(Wallet wallet) {
        this.wallet = wallet;
    }
}
