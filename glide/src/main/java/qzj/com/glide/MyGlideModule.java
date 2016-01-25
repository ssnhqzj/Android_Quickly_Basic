package qzj.com.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;

import java.io.InputStream;

import qzj.com.glide.loader.UrlLoader;
import qzj.com.glide.mode.UrlMode;

/**
 * Glide全局配置
 */
public class MyGlideModule implements GlideModule {

    // 默认图片缓存路径
    public static final String cacheDirectoryName = "glide_disk_cache";

    @Override public void applyOptions(Context context, GlideBuilder builder) {
        // Apply options to the builder here.

        // 设置磁盘缓存路径、大小
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, cacheDirectoryName,
                DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE));

        // 设置图片格式
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override public void registerComponents(Context context, Glide glide) {
        // register ModelLoaders here.
        glide.register(UrlMode.class, InputStream.class, new UrlLoader.Factory());
    }
}
