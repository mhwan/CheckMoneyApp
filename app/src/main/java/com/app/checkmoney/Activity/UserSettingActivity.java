package com.app.checkmoney.Activity;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ListView;

import com.app.checkmoney.CustomBase.BaseActivity;
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

    public static class SettingFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.setting_preference);

        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            View rootView = getView();
            ListView list = (ListView) rootView.findViewById(android.R.id.list);
            list.setDivider(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.colorPrimarybg)));
            list.setDividerHeight(3);

        }
    }
}
