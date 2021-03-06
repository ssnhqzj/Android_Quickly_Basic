package comm.lib.photoview;

import android.content.Context;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class PhotoView extends ImageView
  implements IPhotoView
{
  private final PhotoViewAttacher mAttacher;
  private ScaleType mPendingScaleType;

  public PhotoView(Context paramContext)
  {
    this(paramContext, null);
  }

  public PhotoView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public PhotoView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    super.setScaleType(ScaleType.MATRIX);
    this.mAttacher = new PhotoViewAttacher(this);
    if (this.mPendingScaleType != null)
    {
      setScaleType(this.mPendingScaleType);
      this.mPendingScaleType = null;
    }
  }

  public boolean canZoom()
  {
    return this.mAttacher.canZoom();
  }

  public RectF getDisplayRect()
  {
    return this.mAttacher.getDisplayRect();
  }

  public float getMaxScale()
  {
    return this.mAttacher.getMaxScale();
  }

  public float getMidScale()
  {
    return this.mAttacher.getMidScale();
  }

  public float getMinScale()
  {
    return this.mAttacher.getMinScale();
  }

  public float getScale()
  {
    return this.mAttacher.getScale();
  }

  public ScaleType getScaleType()
  {
    return this.mAttacher.getScaleType();
  }

  protected void onDetachedFromWindow()
  {
    this.mAttacher.cleanup();
    super.onDetachedFromWindow();
  }

  public void setAllowParentInterceptOnEdge(boolean paramBoolean)
  {
    this.mAttacher.setAllowParentInterceptOnEdge(paramBoolean);
  }

  public void setImageDrawable(Drawable paramDrawable)
  {
    super.setImageDrawable(paramDrawable);
    if (this.mAttacher != null)
      this.mAttacher.update();
  }

  public void setImageResource(int paramInt)
  {
    super.setImageResource(paramInt);
    if (this.mAttacher != null)
      this.mAttacher.update();
  }

  public void setImageURI(Uri paramUri)
  {
    super.setImageURI(paramUri);
    if (this.mAttacher != null)
      this.mAttacher.update();
  }

  public void setMaxScale(float paramFloat)
  {
    this.mAttacher.setMaxScale(paramFloat);
  }

  public void setMidScale(float paramFloat)
  {
    this.mAttacher.setMidScale(paramFloat);
  }

  public void setMinScale(float paramFloat)
  {
    this.mAttacher.setMinScale(paramFloat);
  }

  public void setOnLongClickListener(OnLongClickListener paramOnLongClickListener)
  {
    this.mAttacher.setOnLongClickListener(paramOnLongClickListener);
  }

  public void setOnMatrixChangeListener(PhotoViewAttacher.OnMatrixChangedListener paramOnMatrixChangedListener)
  {
    this.mAttacher.setOnMatrixChangeListener(paramOnMatrixChangedListener);
  }

  public void setOnPhotoTapListener(PhotoViewAttacher.OnPhotoTapListener paramOnPhotoTapListener)
  {
    this.mAttacher.setOnPhotoTapListener(paramOnPhotoTapListener);
  }

  public void setOnScaleListener(PhotoViewAttacher.onScaleListener paramonScaleListener)
  {
    this.mAttacher.setOnScaleListener(paramonScaleListener);
  }

  public void setOnViewTapListener(PhotoViewAttacher.OnViewTapListener paramOnViewTapListener)
  {
    this.mAttacher.setOnViewTapListener(paramOnViewTapListener);
  }

  public void setScaleType(ScaleType paramScaleType)
  {
    if (this.mAttacher != null)
    {
      this.mAttacher.setScaleType(paramScaleType);
      return;
    }
    this.mPendingScaleType = paramScaleType;
  }

  public void setZoomable(boolean paramBoolean)
  {
    this.mAttacher.setZoomable(paramBoolean);
  }

  public void zoomTo(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    this.mAttacher.zoomTo(paramFloat1, paramFloat2, paramFloat3);
  }
}