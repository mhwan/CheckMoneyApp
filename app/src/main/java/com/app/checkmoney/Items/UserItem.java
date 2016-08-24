package com.app.checkmoney.Items;

/**
 * Created by Mhwan on 2016. 8. 25..
 */
public class UserItem {
    private String name, phoneNumber;
    private ManageType mtype;
    private ExchangeType etype;
    private boolean ispay = false;

    public UserItem(String name, String phoneNumber, ManageType mtype, ExchangeType etype) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.mtype = mtype;
        this.etype = etype;
    }

    public UserItem(String name, String phoneNumber, ManageType mtype, ExchangeType etype, boolean ispay) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.mtype = mtype;
        this.etype = etype;
        this.ispay = ispay;
    }

    public ExchangeType getEtype() {
        return etype;
    }

    public void setEtype(ExchangeType etype) {
        this.etype = etype;
    }

    public boolean ispay() {
        return ispay;
    }

    public void setIspay(boolean ispay) {
        this.ispay = ispay;
    }

    public ManageType getMtype() {
        return mtype;
    }

    public void setMtype(ManageType mtype) {
        this.mtype = mtype;
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

    /**
     * 사용자 유형
     * 돈 받는 사람 + 방 관리자
     * 돈 받는 사람 + 일반
     * 돈 내는 사람 + 방 관리자
     * 돈 내는 사람 + 일반
     */
    public enum ManageType { MANAGER, NO_MANAGER }
    public enum ExchangeType { RECEIVER, GIVER }
}
