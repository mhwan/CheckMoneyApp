package com.app.checkmoney.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.checkmoney.CustomBase.BaseActivity;
import com.app.checkmoney.CustomBase.KakaoMessageBuilder;
import com.app.checkmoney.CustomBase.RequestPermission;
import com.app.checkmoney.CustomUi.CustomAlertDialog;
import com.app.checkmoney.CustomUi.CustomProgressDialog;
import com.app.checkmoney.CustomUi.InviteUserAdapter;
import com.app.checkmoney.Items.ContactUserItem;
import com.app.checkmoney.Items.RoomUserItem;
import com.app.checkmoney.Util.AppUtility;
import com.app.checkmoney.Util.DevelopeLog;
import com.app.checkmoney.NetworkUtil.NetworkUtility;
import com.moneycheck.checkmoneyapp.R;

import java.util.ArrayList;

public class UserInviteActivity extends BaseActivity {
    private int num = 0;
    private InviteUserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFromBaseView(R.layout.activity_user_invite);
        setToolbar(R.layout.layout_invite_toolbar, R.id.toolbar_close, ToolbarType.BACK_TYPE);
        if (new RequestPermission(getContext(), RequestPermission.READ_CONTACTS_TYPE).isGranted())
            initView();
    }

    private void initView(){
        final ListView listView = (ListView) findViewById(R.id.select_listview);
        adapter = new InviteUserAdapter(getContext(), getFilteredContactList());
        final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View mView = inflater.inflate(R.layout.header_search_list, null, true);
        listView.addHeaderView(mView);
        listView.setTextFilterEnabled(true);
        SearchView searchView = (SearchView) mView.findViewById(R.id.search_view);
        setSearchViewColor(searchView);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText))
                    listView.clearTextFilter();
                else
                    listView.setFilterText(newText);
                return true;
            }
        });

        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    ContactUserItem item = adapter.getItem(position-1);
                    DevelopeLog.d("seles!!!"+position);
                    if (item.isenabled()) {
                        DevelopeLog.d("selectedasdfas!!!"+position);
                        boolean checked = adapter.getOriginalItemChecked(item.getId());
                        if (checked)
                            num--;
                        else
                            num++;

                        setToolbarTitle();
                        item.setIschecked(!checked);
                        adapter.setOriginalItemChecked(item.getId(), !checked);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private ArrayList<ContactUserItem> getFilteredContactList(){
        ArrayList<ContactUserItem> arrayList = AppUtility.getInstance().getContactList();

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            ArrayList<String> numberlist = bundle.getStringArrayList(AppUtility.BaseDataType.ALREADY_ADDED_LIST);

            for (int i =0; i<arrayList.size(); i++){
                for (int j=0; j<numberlist.size(); j++) {
                    if (arrayList.get(i).getPhoneNumber().equals(numberlist.get(j))) {
                        arrayList.get(i).setIsenabled(false);
                    }
                }
            }
        }

        return arrayList;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RequestPermission.Code.READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initView();
            } else {
                Toast.makeText(getContext(), getString(R.string.text_deny_read_contact), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setToolbarTitle(){
        TextView view = (TextView) toolbarview.findViewById(R.id.toolbar_title);
        if (num > 0) {
            String numb = "<font color='#4e4e4e'>" + String.format(" (%d)", num) + "</font>";
            view.setText(Html.fromHtml(getString(R.string.text_invite)+numb));
        } else
            view.setText(getString(R.string.text_invite));
    }

    private void setSearchViewColor(SearchView searchView) {
        LinearLayout ll = (LinearLayout) searchView.getChildAt(0);
        LinearLayout ll2 = (LinearLayout) ll.getChildAt(2);
        LinearLayout ll3 = (LinearLayout) ll2.getChildAt(1);

        SearchView.SearchAutoComplete autoComplete = ((SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text));
        ImageView searchCloseButton = (ImageView) ll3.getChildAt(1);
        ImageView searchIcon = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        ImageView labelView = (ImageView) ll.getChildAt(1);
        autoComplete.setTextSize(TypedValue.COMPLEX_UNIT_PX, AppUtility.getInstance().dpToPx(18));
        autoComplete.setHintTextColor(getResources().getColor(R.color.colorAccentLight));
        autoComplete.setTextColor(getResources().getColor(R.color.colorAccentBlack));

        searchIcon.getDrawable().setColorFilter(getResources().getColor(R.color.colorPrimaryDarkFont), PorterDuff.Mode.MULTIPLY);
        searchCloseButton.getDrawable().setColorFilter(getResources().getColor(R.color.colorPrimaryDarkFont), PorterDuff.Mode.MULTIPLY);
        labelView.getDrawable().setColorFilter(getResources().getColor(R.color.colorAccentLight), PorterDuff.Mode.MULTIPLY);
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.text_invite);
    }

    @Override
    protected boolean getResult() {
        if (num>0)
            return true;
        return false;
    }

    @Override
    protected void setResultForFinish() {
        if (num>0){
            inviteWork();
        }
    }

    private void showKakaoMessageDialog(){
        final CustomAlertDialog dialog = new CustomAlertDialog(getContext());
        dialog.setTitle(getString(R.string.text_title_kakao));
        dialog.setMessage(getString(R.string.text_invite_kakao_message));
        dialog.setPositiveButton(getString(R.string.okay), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                KakaoMessageBuilder builder = new KakaoMessageBuilder(getContext(), KakaoMessageBuilder.MessageType.INVITE_WITH_APPLINK);
                if (builder.sendMessage("컴온 컴온~ 드루와 췤췤머니")) {
                    finishActivity();
                }
            }
        });
        dialog.setNegativeButton(getString(R.string.text_cancel), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finishActivity();
            }
        });
        dialog.show();
    }

    /**
     * 룸 아이디, 체크한 리스트를 보내고
     * 가입 상태를했는지의 여부를 가진 roomuseritem list를 받는다.
     */
    private void inviteWork(){
        new AsyncTask<Void, Void, ArrayList<RoomUserItem>>() {
            @Override
            protected void onPostExecute(ArrayList<RoomUserItem> list) {
                //성공 했을경우 카카오톡 초대 메시지를 보낼거냐는 다이얼로그를 띄움
                if (list != null){
                    Intent intent = new Intent();
                    intent.putExtra(AppUtility.BaseDataType.SELECTED_CONTACT_LIST, list);
                    setResult(RESULT_OK, intent);
                    showKakaoMessageDialog();
                } else {
                    //오류
                    setResult(RESULT_CANCELED);
                    finishActivity();
                }
            }

            @Override
            protected void onPreExecute() {
                CustomProgressDialog dialog = new CustomProgressDialog(getContext());
                dialog.setMessage("초대 작업중..");
                dialog.show();
            }

            @Override
            protected ArrayList<RoomUserItem> doInBackground(Void... params) {
                return NetworkUtility.getInstance().inviteRoom("", adapter.getAllCheckedItem());
            }
        }.execute();
    }

    @Override
    protected Activity getActivity() {
        return UserInviteActivity.this;
    }
}
