package com.dependent.guardian.entity.address;

import lombok.Data;

import java.util.ArrayList;

@Data
public class AddressMap {

    private String addressBig;
    private ArrayList<String> addressSmall;

}
