package com.dependent.guardian.entity.address;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest    //jpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//내장 db안쓰기옵션
class AddressJpaRepositoryTest {

    AddressRepository addressRepository;

    @Autowired
    public AddressJpaRepositoryTest(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Test
    void findAll() {
        //given
        addressRepository.save(getAddress());
        addressRepository.save(getAddress());
        addressRepository.save(getAddress());

        //when
        List<Address> all = addressRepository.findAll();

        //then
        assertThat(all.size()).isEqualTo(3);

    }

    Address getAddress() {
        Address address = new Address();
        address.setAddress1("서울");
        address.setAddress2("광주");


        return address;
    }
}