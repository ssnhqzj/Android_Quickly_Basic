package comm.lib.photoview;

import android.graphics.RectF;
import android.view.View;
import android.widget.ImageView;

public abstract interface IPhotoView
{
  public abstract boolean canZoom();

  public abstract RectF getDisplayRect();

  public abstract float getMaxScale();

  public abstract float getMidScale();

  public abstract float getMinScale();

  public abstract float getScale();

  public abstract ImageView.ScaleType getScaleType();

  public abstract void setAllowParentInterceptOnEdge(boolean paramBoolean);

  public abstract void setMaxScale(float paramFloat);

  public abstract void setMidScale(float paramFloat);

  public abstract void setMinScale(float paramFloat);

  public abstract void setOnLongClickListener(View.OnLongClickListener paramOnLongClickListener);

  public abstract void setOnMatrixChangeListener(PhotoViewAttacher.OnMatrixChangedListener paramOnMatrixChangedListener);

  public abstract void setOnPhotoTapListener(PhotoViewAttacher.OnPhotoTapListener paramOnPhotoTapListener);

  public abstract void setOnViewTapListener(PhotoViewAttacher.OnViewTapListener paramOnViewTapListener);

  public abstract void setScaleType(ImageView.ScaleType paramScaleType);

  public abstract void setZoomable(boolean paramBoolean);

  public abstract void zoomTo(float paramFloat1, float paramFloat2, float paramFloat3);
}