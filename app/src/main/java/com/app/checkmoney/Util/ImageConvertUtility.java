package com.app.checkmoney.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by Mhwan on 2016. 9. 14..
 */
public class ImageConvertUtility {
    public static Bitmap decode(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public static String decodedUri(String encodedImage) {
        Bitmap bitmap = decode(encodedImage);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(AppContext.getContext().getContentResolver(), bitmap, "Title", null);
        return path;
    }
    public static String encode(String imagePath) {
        return encode(Bitmap.CompressFormat.JPEG, imagePath);
    }

    public static String encodePNG(String imagePath) {
        return encode(Bitmap.CompressFormat.PNG, imagePath);
    }



    private static String encode(Bitmap.CompressFormat format, String imagePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(format, 100, baos);
        byte[] byteArrayImage = baos.toByteArray();
        String encodedImaged = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        return encodedImaged;
    }
}
