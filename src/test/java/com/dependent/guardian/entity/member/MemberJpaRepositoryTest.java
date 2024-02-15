package com.dependent.guardian.entity.member;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest    //jpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//내장 db안쓰기옵션
class MemberJpaRepositoryTest {

    MemberRepository memberRepository;

    @Autowired
    public MemberJpaRepositoryTest(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Test
    void findById() {
        //given
        Member save = memberRepository.save(getMember());

        //when
        Optional<Member> member = memberRepository.findById(save.getMemberIdx());

        //then
        assertThat(member.isPresent()).isTrue();
        assertThat(save).isEqualTo(member.get());
    }

    @Test
    void findByUserId() {
        //given
        Member save = memberRepository.save(getMember());

        //when
        Optional<Member> member = memberRepository.findByUserId(save.getUserId());

        //then
        assertThat(member.isPresent()).isTrue();
        assertThat(member.get()).isEqualTo(save);

    }

    Member getMember(){
        Member member = new Member();
        member.setName("권형택");
        member.setPhone("01020631003");
        member.setSnsType("kakao");
        member.setUserId("kht");


        return member;
    }


}