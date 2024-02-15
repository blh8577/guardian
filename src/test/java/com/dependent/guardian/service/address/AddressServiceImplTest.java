package com.dependent.guardian.service.address;

import com.dependent.guardian.entity.address.Address;
import com.dependent.guardian.entity.address.AddressRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @InjectMocks
    AddressServiceImpl addressService;

    @Mock
    AddressRepository addressRepository;

    @Test
    void getAddressList() {
        //given
        List<Address> addressList = new ArrayList<>();
        Address address1 = new Address();
        addressList.add(address1);

        Address address2 = new Address();
        addressList.add(address2);

        Address address3 = new Address();
        addressList.add(address3);

        when(addressRepository.findAll()).thenReturn(addressList);

        //when
        List<Address> all = addressService.getAddressList();

        //then
        assertThat(all).isEqualTo(addressList);
        assertThat(all.size()).isEqualTo(3);

    }
}