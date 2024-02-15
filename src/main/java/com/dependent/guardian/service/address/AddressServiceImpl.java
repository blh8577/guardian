package com.dependent.guardian.service.address;

import com.dependent.guardian.entity.address.Address;
import com.dependent.guardian.entity.address.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService{

    private final AddressRepository addressRepository;

    @Override
    public List<Address> getAddressList() {
        return addressRepository.findAll();
    }
}
