package com.dependent.guardian.entity.member;

import java.util.Optional;

public interface MemberRepository {

    //보호자 인덱스로 찾기
    Optional<Member> findById(Integer memberIdx);

    //보호자 아이디로 찾기
    Optional<Member> findByUserId(String userId);

    //보호자 삽입(회원가입)
    Member save(Member member);



}
