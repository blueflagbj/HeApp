package com.heclient.heapp.xlstable;

import com.heclient.heapp.util.Phone;

import java.util.ArrayList;
import java.util.List;

public class GroupPhone {
    private Long       id;
    private String     name;
    private List<Phone> phones = new ArrayList<Phone>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones= phones;
    }

    public void addPhone(Phone phone) {
        phones.add(phone);
    }
}
