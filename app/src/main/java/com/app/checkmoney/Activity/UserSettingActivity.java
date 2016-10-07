package com.app.checkmoney.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.app.checkmoney.CustomBase.BaseActivity;
import com.app.checkmoney.CustomBase.RequestPermission;
import com.app.checkmoney.CustomUi.CustomAlertDialog;
import com.app.checkmoney.CustomUi.CustomInputPreference;
import com.app.checkmoney.Util.AppUtility;
import com.app.checkmoney.Util.ImageLoaderUtility;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.moneycheck.checkmoneyapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserSettingActivity extends BaseActivity {
    public static final String KEY_PREFERENCE_NAME = "key_preference_name";
    public static final String KEY_PREFERENCE_PHONE = "key_preference_number";
    public static final String KEY_PREFERENCE_ALARM = "key_preference_check";
    public static final String KEY_PREFERENCE_APP_VER = "key_preference_app_version";
    public static final String KEY_PREFERENCE_APP_INFO = "key_preference_app_info";
    public static final String KEY_PREFERENCE_LOGOUT = "key_preference_logout";
    public static final String KEY_PREFERENCE_REMOVE_ACCOUNT = "key_preference_remove_account";

    private CircleImageView image_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFromBaseView(R.layout.activity_user_setting);
        setToolbar(R.layout.layout_base_toolbar, R.id.toolbar_close, ToolbarType.SUB_TYPE);

        image_profile = (CircleImageView) findViewById(R.id.image_profile);
        findViewById(R.id.button_edit_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new RequestPermission(getContext(), RequestPermission.WRITE_EXTERNAL_STORAGE_TYPE).isGranted()) {
                    openPhotoPicker();
                }
            }
        });
        getFragmentManager().beginTransaction()
                .replace(R.id.frame_setting, new SettingFragment()).commit();
    }

    private void openPhotoPicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.text_pick_image)), AppUtility.BaseDataType.PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppUtility.BaseDataType.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //로더가init되지 않았을 수 있으므로 init
            ImageLoaderUtility.getInstance().initImageLoader();
            ImageLoader.getInstance().displayImage(data.getData().toString(), image_profile, ImageLoaderUtility.getInstance().getProfileImageOptions());
        }
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.text_setting);
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
            pref_appInfo.setOnPreferenceClickListener(this);
            pref_logout.setOnPreferenceClickListener(this);
            pref_leave_member.setOnPreferenceClickListener(this);

            //이름, 휴대폰번호 서머리 내용을 프리퍼런스 값 넣어줘야함
            pref_appver.setSummary(AppUtility.getInstance().getInstalledVersion());
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            View rootView = getView();
            ListView list = (ListView) rootView.findViewById(android.R.id.list);
            list.setDivider(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.colorPrimarybg)));
            list.setDividerHeight(3);

        }

        private boolean islastestAppVersion(){
            return false;
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            String key = preference.getKey();

            if (key.equals(pref_name.getKey())) {
                final SharedPreferences preferences = getPreferenceManager().getSharedPreferences();
                String name = preferences.getString(KEY_PREFERENCE_NAME, "");
                final CustomAlertDialog dialog = new CustomAlertDialog(getActivity());
                dialog.setTitle(getString(R.string.text_name_setting));
                dialog.setEditMessage(getString(R.string.hint_name), name);
                dialog.setNegativeButton(getString(R.string.text_cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.setPositiveButton(getString(R.string.text_okay), new View.OnClickListener() {
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
                dialog.setTitle(getString(R.string.text_number_setting));
                dialog.setEditMessage(getString(R.string.hint_phone_number), name);
                dialog.setInputType(InputType.TYPE_CLASS_PHONE);
                dialog.setNegativeButton(getString(R.string.text_cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.setPositiveButton(getString(R.string.text_okay), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setValue(preferences, pref_phone, KEY_PREFERENCE_PHONE, dialog.getEdittextMessage());
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return true;
            } else if (key.equals(pref_appver.getKey())) {
                if (islastestAppVersion())
                    Toast.makeText(getActivity(), getString(R.string.text_lastest_app_version), Toast.LENGTH_SHORT).show();
                else
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.app.mhwan.easymessage")));
                return true;
            } else if (key.equals(pref_appInfo.getKey())) {
                return true;
            } else if (key.equals(pref_logout.getKey())) {
                //logoutUser();

                return true;
            } else if (key.equals(pref_leave_member.getKey())) {
                return true;
            }
            return false;
        }

        private void logoutUser(){
            //카카오 유저일 경우
            UserManagement.requestLogout(new LogoutResponseCallback() {
                @Override
                public void onCompleteLogout() {
                    Toast.makeText(getActivity(), getString(R.string.text_logout_kakao_account), Toast.LENGTH_SHORT).show();
                }
            });

            Intent intent = new Intent(getActivity(), SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }

        private void setValue(SharedPreferences sharedPreferences, Preference preference, String key, String value){
            sharedPreferences.edit().putString(key, value).commit();
            preference.setSummary(value);
        }
    }
}
