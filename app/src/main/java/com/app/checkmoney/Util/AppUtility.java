package com.app.checkmoney.Util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.app.checkmoney.Items.ContactUserItem;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;

/**
 * Created by Mhwan on 2016. 8. 19..
 */
public class AppUtility {
    private static AppUtility instance;

    private AppUtility() {
    }

    public synchronized static AppUtility getInstance() {
        if (instance == null) {
            instance = new AppUtility();
        }
        return instance;
    }

    public void finishApplication(Activity activity) {
        activity.moveTaskToBack(true);
        activity.finish();
    }

    public Date getDate(String sDate, String sFormat) {
        Date date = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sFormat);
        try {
            date = simpleDateFormat.parse(sDate);

            if (date == null)
                throw new IllegalArgumentException("Date is null");
        } catch (ParseException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        return date;
    }

    public boolean isNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) AppContext.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            if (networks != null) {
                for (Network mNetwork : networks) {
                    networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                    if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                        return true;
                    }
                }
            }
        } else {
            if (connectivityManager != null) {
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;*/
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = AppContext.getContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public ArrayList<ContactUserItem> getContactList() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.Contacts._ID
        };

        String[] selectionArgs = null;

        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                + " COLLATE LOCALIZED ASC";

        Cursor cursor = AppContext.getContext().getContentResolver().query(uri, projection, null,
                selectionArgs, sortOrder);

        LinkedHashSet<ContactUserItem> hashlist = new LinkedHashSet<>();
        if (cursor.moveToFirst()) {
            do {
                long photo_id = 0;
                long person_id = cursor.getLong(3);
                photo_id = cursor.getLong(2);

                ContactUserItem contactItem = new ContactUserItem(cursor.getString(1), (cursor.getString(0).replace("-", "")));
                contactItem.setPhoto_id(photo_id);
                contactItem.setPerson_id(person_id);

                boolean result = hashlist.add(contactItem);
            } while (cursor.moveToNext());
        }

        ArrayList<ContactUserItem> list = new ArrayList<>(hashlist);

        for (int i = 0; i < list.size(); i++) {
            list.get(i).setId(i);
        }
        return list;
    }

    public String changeNumberFormat(String number) {
        number = number.replaceAll("-", "");
        //집 전화번호
        if (number.length() == 10 || number.startsWith("02")) {
            //서울
            if (number.startsWith("02")) {
                int length = number.length();
                number = number.substring(0, 2) + "-" + number.substring(2, length - 4) + "-" + number.substring(length - 4, length);
            }
            //나머지
            else
                number = number.substring(0, 3) + "-" + number.substring(3, 6) + "-" + number.substring(6);
        } else if (number.length() > 8) {
            number = number.substring(0, 3) + "-" + number.substring(3, 7) + "-" + number.substring(7);
        } else if (number.length() == 8) {
            number = number.substring(0, 4) + "-" + number.substring(4);
        }

        return number;
    }

    public Bitmap loadContactPhoto(ContentResolver cr, long id, long photo_id) {
        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
        if (input != null)
            return resizingBitmap(BitmapFactory.decodeStream(input));
        else
            DevelopeLog.d("PHOTO : first try failed to load photo");

        byte[] photoBytes = null;
        Uri photoUri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, photo_id);
        Cursor c = cr.query(photoUri, new String[]{ContactsContract.CommonDataKinds.Photo.PHOTO}, null, null, null);
        try {
            if (c.moveToFirst())
                photoBytes = c.getBlob(0);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            c.close();
        }

        if (photoBytes != null)
            return resizingBitmap(BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length));

        else
            DevelopeLog.d("PHOTO : second try also failed");
        return null;
    }

    public Bitmap resizingBitmap(Bitmap oBitmap) {
        if (oBitmap == null)
            return null;
        float width = oBitmap.getWidth();
        float height = oBitmap.getHeight();
        float resizing_size = 120;
        Bitmap rBitmap = null;
        if (width > resizing_size) {
            float mWidth = (float) (width / 100);
            float fScale = (float) (resizing_size / mWidth);
            width *= (fScale / 100);
            height *= (fScale / 100);

        } else if (height > resizing_size) {
            float mHeight = (float) (height / 100);
            float fScale = (float) (resizing_size / mHeight);
            width *= (fScale / 100);
            height *= (fScale / 100);
        }

        rBitmap = Bitmap.createScaledBitmap(oBitmap, (int) width, (int) height, true);
        return rBitmap;
    }

    public String getMyPhoneNumber() {
        TelephonyManager tm = (TelephonyManager) AppContext.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String number = tm.getLine1Number();

        return number.replace("-", "");
    }

    //뷰의 사이즈를 알아내기 어려운 경우, 임의적으로 이미지 축소
    public Bitmap decodeBitmapFromResource(Resources res, int resId, int down_scale) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inSampleSize = down_scale;
        options.inPurgeable = true;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    //뷰의 사이즈를 알아낼 수 있는 경우, 가변적으로 축소
    public Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        DevelopeLog.d("original width " + width + ", original height " + height);
        DevelopeLog.d("required width " + reqWidth + ", required height " + height);
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            do {
                inSampleSize *= 2;
            }
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth);
        }

        DevelopeLog.d("sample size : " + inSampleSize);
        return inSampleSize;
    }

    public boolean isAfterDay(Calendar set_calendar){
        Calendar today_calendar = Calendar.getInstance();
        DevelopeLog.d(today_calendar.toString());
        DevelopeLog.d(set_calendar.toString());
        return today_calendar.before(set_calendar);
    }

    public String getInstalledVersion(){
        PackageManager manager = AppContext.getContext().getPackageManager();
        PackageInfo info = null;
        String result;
        try {
            info = manager.getPackageInfo(
                    AppContext.getContext().getPackageName(), 0);
            result = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            result = "버전 정보 없음";
        }

        DevelopeLog.d("Version Name :: "+info.versionName);
        DevelopeLog.d("Version Code :: "+info.versionCode);
        return result;
    }

    public int getScreenHeight() {
        Context context = AppContext.getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        if (Build.VERSION.SDK_INT >= 13) {
            Point size = new Point();
            display.getSize(size);
            return size.y;
        } else {
            return display.getHeight();  // deprecated
        }
    }

    public int getScreenWidth() {
        Context context = AppContext.getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        if (Build.VERSION.SDK_INT >= 13) {
            Point size = new Point();
            display.getSize(size);
            return size.x;
        } else {
            return display.getWidth();  // deprecated
        }
    }

    public String changeDateTimeFormat(String time, String original_format, String new_format) {
        SimpleDateFormat fOriginal = new SimpleDateFormat(original_format);
        SimpleDateFormat fNew = new SimpleDateFormat(new_format);
        String newtime;
        try {
            Date d = fOriginal.parse(time);
            newtime = fNew.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            newtime = time;
        }

        return newtime;
    }

    public class BaseDataType {
        public static final String DB_DATETIME_FORMAT = "yyyy/MM/dd HH:mm:ss";
        public static final String DB_DATE_FORMAT = "yyyy/MM/dd";

        //카카오 로그인과 관련된 키값
        public static final String KEY_KAKAO_NAME = "kakao_name";
        public static final String KEY_KAKAO_ID = "kakao_id";
        public static final String KEY_KAKAO_IMAGE_PATH = "kakao_image";


        //인텐트와 관련된 키값
        public static final String NAME_ROOM = "key_name_room";
        public static final String MONEY_ROOM = "key_money_room";
        public static final String DATE_ROOM = "key_date_room";
        public static final String MANAGE_USER_LIST = "key_userlist_leaveuser";
        public static final String ALREADY_ADDED_LIST = "key_already_added_contacts";
        public static final String SELECTED_CONTACT_LIST = "key_selected_list";

        public static final int PICK_IMAGE_REQUEST = 0x13;
        public static final int USERMANAGE_REQUEST = 0x15;
        public static final int ROOMMANAGE_REQUEST = 0x17;
        public static final int USERINVITE_REQUEST = 0x18;

        public static final int ROOM_USER_MANAGE_RESULT = 0x31;
        public static final int ROOM_MANAGE_RESULT = 0x32;
    }
}
