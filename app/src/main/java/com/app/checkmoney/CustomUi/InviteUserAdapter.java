package com.app.checkmoney.CustomUi;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.checkmoney.Items.ContactUserItem;
import com.app.checkmoney.Items.UserItem;
import com.app.checkmoney.Util.AppUtility;
import com.mogua.localization.KoreanTextMatcher;
import com.moneycheck.checkmoneyapp.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Mhwan on 2016. 9. 6..
 */
public class InviteUserAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private ArrayList<ContactUserItem> contactList;
    private ArrayList<ContactUserItem> fiterorigList;

    public InviteUserAdapter(Context context, ArrayList<ContactUserItem> list){
        this.context = context;
        this.contactList = list;
        this.fiterorigList = list;
    }
    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public ContactUserItem getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ui_item_invite_user, parent, false);
            holder = new ViewHolder();
            holder.text_name = (TextView) convertView.findViewById(R.id.user_name);
            holder.text_phnum = (TextView) convertView.findViewById(R.id.user_phone);
            holder.image_profile = (CircleImageView) convertView.findViewById(R.id.image_profile);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.select_checkbox);
            holder.background = (RelativeLayout) convertView.findViewById(R.id.bg_user_list);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        ContactUserItem item = contactList.get(position);
        holder.text_name.setText(item.getName());
        holder.text_phnum.setText(AppUtility.getInstance().changeNumberFormat(item.getPhoneNumber()));
        if (item.getPhoto_id()>0)
            holder.image_profile.setImageBitmap(AppUtility.getInstance().loadContactPhoto(context.getContentResolver(), item.getPerson_id(), item.getPhoto_id()));
        else
            holder.image_profile.setImageResource(R.drawable.image_default_profile);
        //enable로 선택할수 있는지를 받아옴 -> 선택할수 없으면 체크상태로, 백그라운드 바꿈, 클릭어블 바꿈
        if (!item.isenabled()){
            holder.background.setBackgroundColor(ContextCompat.getColor(context, R.color.colorDarkbg));
            holder.background.setClickable(false);
            holder.background.setEnabled(false);
            holder.checkBox.setChecked(true);
            holder.checkBox.setEnabled(false);
        } else {
            holder.background.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
            holder.checkBox.setEnabled(true);
            holder.checkBox.setChecked(item.ischecked());
        }


        return convertView;
    }

    class ViewHolder{
        TextView text_name, text_phnum;
        CircleImageView image_profile;
        RelativeLayout background;
        CheckBox checkBox;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults return_filter = new FilterResults();
                final ArrayList<ContactUserItem> results = new ArrayList<>();
                if (constraint != null){
                    if (fiterorigList != null && fiterorigList.size()>0){
                        for (final ContactUserItem c : fiterorigList){
                            if (c.getName().toLowerCase().contains(constraint.toString()))
                                results.add(c);
                            else if (c.getPhoneNumber().contains(constraint.toString()))
                                results.add(c);
                            else if (c.getPhoneNumber().contains(constraint.toString()))
                                results.add(c);
                            else if (KoreanTextMatcher.isMatch(c.getName(), constraint.toString()))
                                results.add(c);
                        }
                    }
                    return_filter.values = results;
                }
                return return_filter;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                contactList = (ArrayList<ContactUserItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public int getCheckedItemCount(){
        int a = 0;
        for (int i=0; i<fiterorigList.size(); i++){
            if (fiterorigList.get(i).ischecked())
                a++;
        }

        return a;
    }

    public ArrayList<UserItem> getAllCheckedItem(){
        ArrayList<UserItem> list = new ArrayList<>();

        for (int i=0; i<fiterorigList.size(); i++){
            if (fiterorigList.get(i).ischecked()){
                list.add(new UserItem(fiterorigList.get(i).getName(), fiterorigList.get(i).getPhoneNumber()));
            }
        }

        return list;
    }
    public void setOriginalItemChecked(int id, boolean value){
        fiterorigList.get(id).setIschecked(value);
    }

    public boolean getOriginalItemChecked(int id){
        return fiterorigList.get(id).ischecked();
    }
}
