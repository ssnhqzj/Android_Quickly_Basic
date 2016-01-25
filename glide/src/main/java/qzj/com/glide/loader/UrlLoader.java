package qzj.com.glide.loader;

import android.content.Context;

import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;

import qzj.com.glide.mode.UrlMode;

/**
 * 加载自定义图片尺寸的ModelLoader
 */
public class UrlLoader extends BaseGlideUrlLoader<UrlMode> {

    public UrlLoader(Context context) {
        super(context);
    }

    @Override
    protected String getUrl(UrlMode model, int width, int height) {
        return model.buildUrl(width,height);
    }

    public static class Factory implements ModelLoaderFactory {

        @Override
        public ModelLoader build(Context context, GenericLoaderFactory factories) {
            return new UrlLoader(context);
        }

        @Override
        public void teardown() {

        }
    }
}
