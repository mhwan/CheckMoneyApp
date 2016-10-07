package com.app.checkmoney.CustomBase;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Mhwan on 2016. 8. 21..
 */
public class RequestPermission {
    private Context context;
    private int permission_type;
    private String [] PERMISSION_MESSAGE = {
            "외부저장소 쓰기 권한이 필요합니다."
    };
    private static final String[] MANIFEST_PERMISSION = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS};
    private static final int [] REQUEST_PERMISSION = {
            Code.WRITE_EXTERNAL_STORAGE, Code.READ_PHONE_STATE, Code.READ_CONTACTS, Code.CALL_PHONE, Code.SEND_SMS
    };

    public static final int WRITE_EXTERNAL_STORAGE_TYPE = 0;
    public static final int READ_PHONE_STATE_TYPE = 1;
    public static final int READ_CONTACTS_TYPE = 2;
    public static final int CALL_PHONE_TYPE = 3;
    public static final int SEND_SMS_TYPE = 4;


    public RequestPermission(Context context, int permission_type){
        this.context = context;
        this.permission_type = permission_type;
    }


    public static boolean isGranted(Context context, int permission_type){
        if (ActivityCompat.checkSelfPermission(context, MANIFEST_PERMISSION[permission_type])!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{MANIFEST_PERMISSION[permission_type]},
                    REQUEST_PERMISSION[permission_type]);
        }

        return ActivityCompat.checkSelfPermission(context, MANIFEST_PERMISSION[permission_type])
                == PackageManager.PERMISSION_GRANTED;
    }


    public boolean isGranted(){
        if (ActivityCompat.checkSelfPermission(context, MANIFEST_PERMISSION[permission_type])!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{MANIFEST_PERMISSION[permission_type]},
                    REQUEST_PERMISSION[permission_type]);

            /*AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(PERMISSION_MESSAGE[permission_type])
                    .setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context,
                                    new String[]{MANIFEST_PERMISSION[permission_type]},
                                    REQUEST_PERMISSION[permission_type]);
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ((Activity) context).finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();*/
        }

        return ActivityCompat.checkSelfPermission(context, MANIFEST_PERMISSION[permission_type])
                == PackageManager.PERMISSION_GRANTED;
    }

    public class Code{
        public static final int WRITE_EXTERNAL_STORAGE = 0x21;
        public static final int READ_PHONE_STATE = 0x22;
        public static final int READ_CONTACTS = 0x23;
        public static final int CALL_PHONE = 0x24;
        public static final int SEND_SMS = 0x25;
    }
}
