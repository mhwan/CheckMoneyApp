package com.app.checkmoney.CustomUi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.checkmoney.Items.RoomListItem;
import com.moneycheck.checkmoneyapp.R;

import java.util.ArrayList;

/**
 * Created by Mhwan on 2016. 8. 21..
 */
public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<RoomListItem> roomListItems;

    public RoomListAdapter(Context context, ArrayList<RoomListItem> items){
        this.context = context;
        this.roomListItems = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.ui_item_room, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RoomListItem item = roomListItems.get(position);
        if (item.getCount_new_alarm()>=1)
            holder.new_alarm.setVisibility(View.VISIBLE);
        else
            holder.new_alarm.setVisibility(View.GONE);

        switch (item.getState_type()) {
            case ALL_RECEIVE:
                holder.room_state.setImageResource(R.mipmap.image_all_receive);
                break;
            case NOT_RECEIVE:
                holder.room_state.setImageResource(R.mipmap.image_not_receive);
                break;
            case INCOMPLETE:
                holder.room_state.setImageResource(R.mipmap.image_incomplete);
        }
        holder.room_title.setText(item.getRoom_name());
        holder.room_money.setText(item.getPrice_money()+"원");
        holder.room_expireDate.setText(item.getExpire_date());

    }

    public RoomListItem getItem(int position){
        return roomListItems.get(position);
    }

    @Override
    public int getItemCount() {
        return roomListItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView room_title, room_money, room_expireDate;
        ImageView room_state, new_alarm;

        public ViewHolder(View itemView) {
            super(itemView);
            room_title = (TextView) itemView.findViewById(R.id.title_room);
            room_money = (TextView) itemView.findViewById(R.id.money_room);
            room_expireDate = (TextView) itemView.findViewById(R.id.expire_date_room);
            room_state = (ImageView) itemView.findViewById(R.id.image_room_state);
            new_alarm = (ImageView) itemView.findViewById(R.id.image_room_alarm);
        }
    }
}
