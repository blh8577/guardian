package com.dependent.guardian.service.member;

import com.dependent.guardian.entity.member.Member;
import com.dependent.guardian.entity.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Optional<Member> signIn(String id) {
        return memberRepository.findByUserId(id);
    }

    @Override
    public Member signUp(Member member) {
        return memberRepository.save(member);
    }


}
