package com.yao.zhihudaily.tool;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;

import androidx.annotation.NonNull;

/**
 *
 * @author Yao
 * @date 2016/9/24
 */
public class MyGlideModule implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        MemorySizeCalculator memorySizeCalculator = new MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(2)//设置内存缓存大小
                .setBitmapPoolScreens(3)//设置Bitmap池大小
                .build();
        //设置内存缓存大小
        builder.setMemoryCache(new LruResourceCache(memorySizeCalculator.getMemoryCacheSize()));
        //设置Bitmap池大小
        builder.setBitmapPool(new LruBitmapPool(memorySizeCalculator.getBitmapPoolSize()));

        //设置硬盘缓存大小
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context));
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {

    }
}
