package com.app.checkmoney.CustomBase;

import android.content.Context;

import com.kakao.kakaolink.AppActionBuilder;
import com.kakao.kakaolink.AppActionInfoBuilder;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;

import java.util.Map;

/**
 * Created by Mhwan on 2016. 9. 6..
 */
public class KakaoMessageBuilder {
    private Context context;
    private KakaoTalkLinkMessageBuilder builder;
    private KakaoLink kakaoLink;
    private String message;
    private MessageType type;
    private Map<String, String> excuteparams;

    public KakaoMessageBuilder(Context context, MessageType type) {
        this.context = context;
        this.type = type;
        initBuilder();
    }

    private void initBuilder() {
        try {
            kakaoLink = KakaoLink.getKakaoLink(context);
            builder = kakaoLink.createKakaoTalkLinkMessageBuilder();
        } catch (KakaoParameterException e) {
            e.printStackTrace();
        }
    }


    public boolean sendMessage(String message) {
        this.message = message;
        boolean issuccess = true;
        buildMessage();
        try {
            kakaoLink.sendMessage(builder, context);
        } catch (KakaoParameterException e) {
            e.printStackTrace();
            issuccess = false;
        } finally {
            return issuccess;
        }
    }

    public boolean sendMessage(String message, Map<String, String> excuteParams){
        this.excuteparams = excuteParams;
        return sendMessage(message);
    }

    private void buildMessage() {
        try {
            builder.addText(message);
            if (type.equals(MessageType.INVITE_WITH_APPLINK)) {
                builder.addImage("http://i68.tinypic.com/2j28d43.jpg", 100, 100);
                builder.addAppLink("앱으로 이동", new AppActionBuilder()
                        .addActionInfo(AppActionInfoBuilder
                                .createAndroidActionInfoBuilder()
                                .setExecuteParam("execparamkey1=1111")
                                .setMarketParam("referrer=kakaotalklink")
                                .build())
                        //.setUrl("https://play.google.com/store/apps/details?id=com.app.mhwan.easymessage") //pc에서 사용하는 주소
                .build());
            }
        } catch (KakaoParameterException e) {
            e.printStackTrace();
        }
    }
    public enum MessageType {INVITE_WITH_APPLINK, ONLY_TEXT}
}
