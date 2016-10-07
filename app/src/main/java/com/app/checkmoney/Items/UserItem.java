package com.app.checkmoney.Items;

import java.io.Serializable;

/**
 * Created by Mhwan on 2016. 9. 7..
 */
public class UserItem implements Serializable{
    private String name, phoneNumber;

    public UserItem(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
