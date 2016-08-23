package com.app.checkmoney.Activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.View;
import android.widget.ListView;

import com.app.checkmoney.CustomBase.BaseActivity;
import com.app.checkmoney.CustomUi.CustomAlertDialog;
import com.app.checkmoney.CustomUi.CustomInputPreference;
import com.moneycheck.checkmoneyapp.R;

public class UserSettingActivity extends BaseActivity {
    public static final String KEY_PREFERENCE_NAME = "key_preference_name";
    public static final String KEY_PREFERENCE_PHONE = "key_preference_number";
    public static final String KEY_PREFERENCE_ALARM = "key_preference_check";
    public static final String KEY_PREFERENCE_APP_VER = "key_preference_app_version";
    public static final String KEY_PREFERENCE_APP_INFO = "key_preference_app_info";
    public static final String KEY_PREFERENCE_LOGOUT = "key_preference_logout";
    public static final String KEY_PREFERENCE_REMOVE_ACCOUNT = "key_preference_remove_account";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFromBaseView(R.layout.activity_user_setting);
        setToolbar(R.layout.layout_base_toolbar, R.id.toolbar_close, ToolbarType.SUB_TYPE);
        getFragmentManager().beginTransaction()
                .replace(R.id.frame_setting, new SettingFragment()).commit();
    }

    @Override
    protected String getToolbarTitle() {
        return "설정";
    }

    @Override
    protected boolean getResult() {
        return true;
    }

    @Override
    protected Activity getActivity() {
        return UserSettingActivity.this;
    }

    public static class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener{
        private Preference pref_name, pref_phone, pref_alarm, pref_appInfo, pref_logout, pref_leave_member;
        private CustomInputPreference pref_appver;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.setting_preference);

            pref_name = findPreference(KEY_PREFERENCE_NAME);
            pref_phone = findPreference(KEY_PREFERENCE_PHONE);
            pref_alarm = findPreference(KEY_PREFERENCE_ALARM);
            pref_appver = (CustomInputPreference) findPreference(KEY_PREFERENCE_APP_VER);
            pref_appInfo = findPreference(KEY_PREFERENCE_APP_INFO);
            pref_logout = findPreference(KEY_PREFERENCE_LOGOUT);
            pref_leave_member = findPreference(KEY_PREFERENCE_REMOVE_ACCOUNT);

            pref_name.setOnPreferenceClickListener(this);
            pref_phone.setOnPreferenceClickListener(this);
            pref_appver.setOnPreferenceClickListener(this);
            pref_appver.setNewSignView();
            pref_appInfo.setOnPreferenceClickListener(this);
            pref_logout.setOnPreferenceClickListener(this);
            pref_leave_member.setOnPreferenceClickListener(this);

            //이름, 휴대폰번호 서머리 내용을 프리퍼런스 값 넣어줘야함

        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            View rootView = getView();
            ListView list = (ListView) rootView.findViewById(android.R.id.list);
            list.setDivider(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.colorPrimarybg)));
            list.setDividerHeight(3);

        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            String key = preference.getKey();

            if (key.equals(pref_name.getKey())) {
                final SharedPreferences preferences = getPreferenceManager().getSharedPreferences();
                String name = preferences.getString(KEY_PREFERENCE_NAME, "");
                final CustomAlertDialog dialog = new CustomAlertDialog(getActivity());
                dialog.setTitle("이름 설정");
                dialog.setEditMessage(getString(R.string.hint_name), name);
                dialog.setNegativeButton("취소", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.setPositiveButton("확인", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setValue(preferences, pref_name, KEY_PREFERENCE_NAME, dialog.getEdittextMessage());
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return true;
            } else if (key.equals(pref_phone.getKey())) {
                final SharedPreferences preferences = getPreferenceManager().getSharedPreferences();
                String name = preferences.getString(KEY_PREFERENCE_PHONE, "");
                final CustomAlertDialog dialog = new CustomAlertDialog(getActivity());
                dialog.setTitle("번호 설정");
                dialog.setEditMessage(getString(R.string.hint_phone_number), name);
                dialog.setInputType(InputType.TYPE_CLASS_PHONE);
                dialog.setNegativeButton("취소", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.setPositiveButton("확인", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setValue(preferences, pref_phone, KEY_PREFERENCE_PHONE, dialog.getEdittextMessage());
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return true;
            } else if (key.equals(pref_appver.getKey())) {
                return true;
            } else if (key.equals(pref_appInfo.getKey())) {
                return true;
            } else if (key.equals(pref_logout.getKey())) {
                return true;
            } else if (key.equals(pref_leave_member.getKey())) {
                return true;
            }
            return false;
        }

        private void setValue(SharedPreferences sharedPreferences, Preference preference, String key, String value){
            sharedPreferences.edit().putString(key, value).commit();
            preference.setSummary(value);
        }
    }
}
