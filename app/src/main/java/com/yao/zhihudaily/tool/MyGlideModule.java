package com.yao.zhihudaily.tool;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by Administrator on 2016/9/24.
 */

public class MyGlideModule implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //设置图片编码 颜色8888
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);

        //设置内存缓存大小
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();
//        Log.e("YAO", "MyGlideModule.java - applyOptions() ---------- defaultMemoryCacheSize:defaultBitmapPoolSize  " + defaultMemoryCacheSize + ":" + defaultBitmapPoolSize);
//        defaultMemoryCacheSize:defaultBitmapPoolSize  16588800:33177600
        int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);
        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));

        //设置硬盘缓存大小
        int cacheSize100MegaBytes = 100*1024*1024;
        String path = Constants.STORAGE_DIR;
        builder.setDiskCache(new DiskLruCacheFactory(path, cacheSize100MegaBytes));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
