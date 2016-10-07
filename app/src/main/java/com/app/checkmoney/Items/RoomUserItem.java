package com.app.checkmoney.Items;

import java.io.Serializable;

/**
 * Created by Mhwan on 2016. 8. 25..
 */
public class RoomUserItem extends UserItem implements Serializable{
    private ManageType mtype;
    private ExchangeType etype;
    private boolean ispay = false;
    private boolean newNotify = false;
    private boolean isRegister = true;

    public RoomUserItem(String name, String phoneNumber, ManageType mtype, ExchangeType etype) {
        super(name, phoneNumber);
        this.mtype = mtype;
        this.etype = etype;
    }

    public RoomUserItem(String name, String phoneNumber, ManageType mtype, ExchangeType etype, boolean ispay) {
        super(name, phoneNumber);
        this.mtype = mtype;
        this.etype = etype;
        this.ispay = ispay;
    }

    public boolean isRegister() {
        return isRegister;
    }

    public void setRegister(boolean register) {
        isRegister = register;
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

    public boolean isNewNotify() {
        return newNotify;
    }

    public void setNewNotify(boolean newNotify) {
        this.newNotify = newNotify;
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
