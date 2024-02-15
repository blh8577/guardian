package com.dependent.guardian.entity.address;

import java.util.List;

public interface AddressRepository {

    //주소 불러오기
    List<Address> findAll();

    //주소 저장 - 쓰는 곳은 테스트 뿐
    Address save(Address address);
}
