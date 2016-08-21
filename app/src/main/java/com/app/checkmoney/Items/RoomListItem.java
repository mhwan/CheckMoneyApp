package com.app.checkmoney.Items;

/**
 * Created by Mhwan on 2016. 8. 21..
 */

/**
 * 메인에 띄어지는 방 아이템 (이름, 만기일, 돈, 새로운 알람, 방의 현재 상태)
 */
public class RoomListItem {
    private String room_name;
    private String expire_date;
    private int price_money;
    private int count_new_alarm;
    private RoomState state_type;

    public RoomListItem(String room_name, String expire_date, int price_money) {
        this(0, expire_date, price_money, room_name, RoomState.INCOMPLETE);
    }

    public RoomListItem(int count_new_alarm, String expire_date, int price_money, String room_name, RoomState state_type) {
        this.count_new_alarm = count_new_alarm;
        this.expire_date = expire_date;
        this.price_money = price_money;
        this.room_name = room_name;
        this.state_type = state_type;
    }

    public int getCount_new_alarm() {
        return count_new_alarm;
    }

    public void setCount_new_alarm(int count_new_alarm) {
        this.count_new_alarm = count_new_alarm;
    }

    public String getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(String expire_date) {
        this.expire_date = expire_date;
    }

    public int getPrice_money() {
        return price_money;
    }

    public void setPrice_money(int price_money) {
        this.price_money = price_money;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public RoomState getState_type() {
        return state_type;
    }

    public void setState_type(RoomState state_type) {
        this.state_type = state_type;
    }

    public enum RoomState {ALL_RECEIVE, NOT_RECEIVE, INCOMPLETE }
}
