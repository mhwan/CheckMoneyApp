package com.app.checkmoney.CustomUi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.checkmoney.Items.RoomUserItem;
import com.moneycheck.checkmoneyapp.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Mhwan on 2016. 9. 5..
 */
public class LeaveUserAdapter extends RecyclerView.Adapter<LeaveUserAdapter.ViewHolder> {
    private Context context;
    private ArrayList<RoomUserItem> userList;

    public LeaveUserAdapter(Context context, ArrayList<RoomUserItem> userList) {
        this.context = context;
        this.userList = userList;
    }

    public LeaveUserAdapter(Context context) {
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.ui_item_leave_user, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final RoomUserItem item = userList.get(position);

        String name = item.getName();
        if (!item.isRegister())
            name += " (미가입)";
        holder.user_name.setText(name);
        holder.user_phnum.setText(item.getPhoneNumber());

        if (item.isRegister()) {
            holder.button_leave.setVisibility(View.VISIBLE);
        } else
            holder.button_leave.setVisibility(View.GONE);

        holder.button_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, userList.size());
                Toast.makeText(context, String.format(context.getString(R.string.text_success_user_leave), item.getName()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void refreshAdapter(ArrayList<RoomUserItem> list) {
        if (userList != null) {
            userList.clear();
            userList.addAll(list);
        } else
            userList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void addSelectedItem(ArrayList<RoomUserItem> list) {
        userList.addAll(list);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView button_leave, user_name, user_phnum;
        CircleImageView user_image;

        public ViewHolder(View itemView) {
            super(itemView);
            button_leave = (TextView) itemView.findViewById(R.id.button_leave);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            user_phnum = (TextView) itemView.findViewById(R.id.user_phone);
            user_image = (CircleImageView) itemView.findViewById(R.id.image_profile);
        }
    }
}
