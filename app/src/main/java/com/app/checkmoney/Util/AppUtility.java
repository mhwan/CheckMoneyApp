package com.app.checkmoney.Util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.WindowManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Mhwan on 2016. 8. 19..
 */
public class AppUtility {
    private static AppUtility instance;
    private ArrayList<Activity> activityList;

    private AppUtility(){
    }

    public synchronized static AppUtility getInstance(){
        if (instance == null) {
            instance = new AppUtility();
        }
        return instance;
    }

    public void addActivity(Activity activity){
        if (activityList == null)
            activityList = new ArrayList<>();
        activityList.add(activity);
        DevelopeLog.d("ADD ACTIVITY");
        DevelopeLog.d(activityList.toString());
    }

    public void finishActivity(){
        if (activityList == null)
            return;
        int lastindex = activityList.size()-1;
        if (lastindex >= 0)
            activityList.remove(lastindex);
        DevelopeLog.d("FINISH ACTIVITY");
        DevelopeLog.d(activityList.toString());
    }

    public void finishApplication() {
        if (activityList == null)
            return;

        for (Activity activity : activityList){
            if (activity!=null)
                activity.finish();
        }
    }

    public Date getDate(String sDate, String sFormat){
        Date date = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sFormat);
        try {
            date = simpleDateFormat.parse(sDate);

            if (date == null)
                throw new IllegalArgumentException("Date is null");
        } catch (ParseException | IllegalArgumentException e){
            e.printStackTrace();
        }

        return date;
    }

    public String getMyPhoneNumber() {
        TelephonyManager tm = (TelephonyManager) AppContext.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String number = tm.getLine1Number();

        return number.replace("-", "");
    }

    //뷰의 사이즈를 알아내기 어려운 경우, 임의적으로 이미지 축소
    public Bitmap decodeBitmapFromResource(Resources res, int resId, int down_scale){
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
        DevelopeLog.d("original width "+width+", original height "+height);
        DevelopeLog.d("required width "+reqWidth+", required height "+height);
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            do {
                inSampleSize *= 2;
            }while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth);
        }

        DevelopeLog.d("sample size : "+inSampleSize);
        return inSampleSize;
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

    public String changeDateTimeFormat(String time, String original_format, String new_format){
        SimpleDateFormat fOriginal = new SimpleDateFormat(original_format);
        SimpleDateFormat fNew = new SimpleDateFormat(new_format);
        String newtime;
        try {
            Date d = fOriginal.parse(time);
            newtime = fNew.format(d);
        } catch (ParseException e){
            e.printStackTrace();
            newtime = time;
        }

        return newtime;
    }

    public class BaseDataType {
        public static final String DB_DATETIME_FORMAT = "yyyy/MM/dd HH:mm:ss";
        public static final String DB_DATE_FORMAT = "yyyy/MM/dd";
    }
}
