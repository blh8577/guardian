package com.dependent.guardian.service.member;

import com.dependent.guardian.entity.member.Member;

import java.util.Optional;

public interface MemberService {

    Optional<Member> signIn(String id);

    Member signUp(Member member);





}
