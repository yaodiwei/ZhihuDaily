package com.yao.zhihudaily.tool

import android.content.Context

import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.module.GlideModule

/**
 *
 * @author Yao
 * @date 2016/9/24
 */
class MyGlideModule : GlideModule {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val memorySizeCalculator = MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(2f)//设置内存缓存大小
                .setBitmapPoolScreens(3f)//设置Bitmap池大小
                .build()
        //设置内存缓存大小
        builder.setMemoryCache(LruResourceCache(memorySizeCalculator.memoryCacheSize.toLong()))
        //设置Bitmap池大小
        builder.setBitmapPool(LruBitmapPool(memorySizeCalculator.bitmapPoolSize.toLong()))

        //设置硬盘缓存大小
        builder.setDiskCache(ExternalCacheDiskCacheFactory(context))
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {

    }
}
