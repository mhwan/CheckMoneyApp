package com.app.checkmoney.Util;

import com.moneycheck.checkmoneyapp.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by Mhwan on 2016. 8. 20..
 */
public class ImageLoaderUtility {
    private static ImageLoaderUtility instance;
    private ImageLoaderUtility(){}

    public static ImageLoaderUtility getInstance(){
        if (instance == null){
            instance = new ImageLoaderUtility();
        }
        return instance;
    }

    public void initImageLoader(){
        if (!ImageLoader.getInstance().isInited()) {
            //custom init image loader
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(AppContext.getContext())
                    .threadPriority(Thread.NORM_PRIORITY - 2)
                    .denyCacheImageMultipleSizesInMemory()
                    .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                    .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                    .tasksProcessingOrder(QueueProcessingType.LIFO)
                    //			.writeDebugLogs() // Remove for release app
                    .build();

            ImageLoader.getInstance().init(config);

            /**
             * default init image loader
             * 위 또는 아래 중에 둘 중 성능이 더 좋은것으로..
             */
            //ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(AppContext.getContext()));
        }
    }

    public DisplayImageOptions getDefaultOptions(){
        return new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
    }

    public DisplayImageOptions getProfileImageOptions(){
        return new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .showImageForEmptyUri(R.drawable.image_default_profile)
                .showImageOnFail(R.drawable.image_default_profile)
                .build();
    }
}
