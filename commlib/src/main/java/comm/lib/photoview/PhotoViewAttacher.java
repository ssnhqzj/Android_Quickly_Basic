package comm.lib.photoview;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import java.lang.ref.WeakReference;

public class PhotoViewAttacher
  implements IPhotoView, View.OnTouchListener, VersionedGestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, ViewTreeObserver.OnGlobalLayoutListener
{
  static final boolean DEBUG = false;
  public static final float DEFAULT_MAX_SCALE = 3.0F;
  public static final float DEFAULT_MID_SCALE = 1.75F;
  public static final float DEFAULT_MIN_SCALE = 1.0F;
  static final int EDGE_BOTH = 2;
  static final int EDGE_LEFT = 0;
  static final int EDGE_NONE = -1;
  static final int EDGE_RIGHT = 1;
  static final String LOG_TAG = "PhotoViewAttacher";
  private boolean mAllowParentInterceptOnEdge = true;
  private final Matrix mBaseMatrix = new Matrix();
  private FlingRunnable mCurrentFlingRunnable;
  private final RectF mDisplayRect = new RectF();
  private final Matrix mDrawMatrix = new Matrix();
  private GestureDetector mGestureDetector;
  private WeakReference<ImageView> mImageView;
  private int mIvBottom;
  private int mIvLeft;
  private int mIvRight;
  private int mIvTop;
  private View.OnLongClickListener mLongClickListener;
  private OnMatrixChangedListener mMatrixChangeListener;
  private final float[] mMatrixValues = new float[9];
  private float mMaxScale = 3.0F;
  private float mMidScale = 1.75F;
  private float mMinScale = 1.0F;
  private OnPhotoTapListener mPhotoTapListener;
  private VersionedGestureDetector mScaleDragDetector;
  private ScaleType mScaleType = ScaleType.FIT_CENTER;
  private int mScrollEdge = 2;
  private final Matrix mSuppMatrix = new Matrix();
  private OnViewTapListener mViewTapListener;
  private ViewTreeObserver mViewTreeObserver;
  private boolean mZoomEnabled;
  private onScaleListener scaleListener;

  public PhotoViewAttacher(ImageView paramImageView)
  {
    this.mImageView = new WeakReference(paramImageView);
    paramImageView.setOnTouchListener(this);
    this.mViewTreeObserver = paramImageView.getViewTreeObserver();
    this.mViewTreeObserver.addOnGlobalLayoutListener(this);
    setImageViewScaleTypeMatrix(paramImageView);
    if (!paramImageView.isInEditMode())
    {
      this.mScaleDragDetector = VersionedGestureDetector.newInstance(paramImageView.getContext(), this);
      this.mGestureDetector = new GestureDetector(paramImageView.getContext(), new GestureDetector.SimpleOnGestureListener()
      {
        public void onLongPress(MotionEvent paramAnonymousMotionEvent)
        {
          if (PhotoViewAttacher.this.mLongClickListener != null)
            PhotoViewAttacher.this.mLongClickListener.onLongClick((View)PhotoViewAttacher.this.mImageView.get());
        }
      });
      this.mGestureDetector.setOnDoubleTapListener(this);
      setZoomable(true);
    }
  }

  private void cancelFling()
  {
    if (this.mCurrentFlingRunnable != null)
    {
      this.mCurrentFlingRunnable.cancelFling();
      this.mCurrentFlingRunnable = null;
    }
  }

  private void checkAndDisplayMatrix()
  {
    checkMatrixBounds();
    setImageViewMatrix(getDisplayMatrix());
  }

  private void checkImageViewScaleType()
  {
    ImageView localImageView = getImageView();
    if ((localImageView != null) && (!(localImageView instanceof PhotoView)) && (localImageView.getScaleType() != ScaleType.MATRIX))
      throw new IllegalStateException("The ImageView's ScaleType has been changed since attaching a PhotoViewAttacher");
  }

  private void checkMatrixBounds()
  {
	  final ImageView imageView = getImageView();
		if (null == imageView) {
			return;
		}

		final RectF rect = getDisplayRect(getDisplayMatrix());
		if (null == rect) {
			return;
		}

		final float height = rect.height(), width = rect.width();
		float deltaX = 0, deltaY = 0;

		final int viewHeight = imageView.getHeight();
		if (height <= viewHeight) {
			switch (mScaleType) {
				case FIT_START:
					deltaY = -rect.top;
					break;
				case FIT_END:
					deltaY = viewHeight - height - rect.top;
					break;
				default:
					deltaY = (viewHeight - height) / 2 - rect.top;
					break;
			}
		} else if (rect.top > 0) {
			deltaY = -rect.top;
		} else if (rect.bottom < viewHeight) {
			deltaY = viewHeight - rect.bottom;
		}

		final int viewWidth = imageView.getWidth();
		if (width <= viewWidth) {
			switch (mScaleType) {
				case FIT_START:
					deltaX = -rect.left;
					break;
				case FIT_END:
					deltaX = viewWidth - width - rect.left;
					break;
				default:
					deltaX = (viewWidth - width) / 2 - rect.left;
					break;
			}
			mScrollEdge = EDGE_BOTH;
		} else if (rect.left > 0) {
			mScrollEdge = EDGE_LEFT;
			deltaX = -rect.left;
		} else if (rect.right < viewWidth) {
			deltaX = viewWidth - rect.right;
			mScrollEdge = EDGE_RIGHT;
		} else {
			mScrollEdge = EDGE_NONE;
		}

		// Finally actually translate the matrix
		mSuppMatrix.postTranslate(deltaX, deltaY);
  }

  private static void checkZoomLevels(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (paramFloat1 >= paramFloat2)
      throw new IllegalArgumentException("MinZoom should be less than MidZoom");
    if (paramFloat2 >= paramFloat3)
      throw new IllegalArgumentException("MidZoom should be less than MaxZoom");
  }

  private RectF getDisplayRect(Matrix paramMatrix)
  {
    ImageView localImageView = getImageView();
    if (localImageView != null)
    {
      Drawable localDrawable = localImageView.getDrawable();
      if (localDrawable != null)
      {
        this.mDisplayRect.set(0.0F, 0.0F, localDrawable.getIntrinsicWidth(), localDrawable.getIntrinsicHeight());
        paramMatrix.mapRect(this.mDisplayRect);
        return this.mDisplayRect;
      }
    }
    return null;
  }

  private float getValue(Matrix paramMatrix, int paramInt)
  {
    paramMatrix.getValues(this.mMatrixValues);
    return this.mMatrixValues[paramInt];
  }

  private static boolean hasDrawable(ImageView paramImageView)
  {
    return (paramImageView != null) && (paramImageView.getDrawable() != null);
  }

  private static boolean isSupportedScaleType(final ScaleType scaleType) {
		if (null == scaleType) {
			return false;
		}

		switch (scaleType) {
			case MATRIX:
				throw new IllegalArgumentException(scaleType.name() + " is not supported in PhotoView");

			default:
				return true;
		}
  }

  private void resetMatrix()
  {
    this.mSuppMatrix.reset();
    setImageViewMatrix(getDisplayMatrix());
    checkMatrixBounds();
  }

  private void setImageViewMatrix(Matrix paramMatrix)
  {
    ImageView localImageView = getImageView();
    if (localImageView != null)
    {
      checkImageViewScaleType();
      localImageView.setImageMatrix(paramMatrix);
      if (this.mMatrixChangeListener != null)
      {
        RectF localRectF = getDisplayRect(paramMatrix);
        if (localRectF != null)
          this.mMatrixChangeListener.onMatrixChanged(localRectF);
      }
    }
  }

  private static void setImageViewScaleTypeMatrix(ImageView paramImageView)
  {
    if ((paramImageView != null) && (!(paramImageView instanceof PhotoView)))
      paramImageView.setScaleType(ScaleType.MATRIX);
  }

  /**
	 * Calculate Matrix for FIT_CENTER
	 * 
	 * @param d - Drawable being displayed
	 */
	private void updateBaseMatrix(Drawable d) {
		ImageView imageView = getImageView();
		if (null == imageView || null == d) {
			return;
		}

		final float viewWidth = imageView.getWidth();
		final float viewHeight = imageView.getHeight();
		final int drawableWidth = d.getIntrinsicWidth();
		final int drawableHeight = d.getIntrinsicHeight();

		mBaseMatrix.reset();

		final float widthScale = viewWidth / drawableWidth;
		final float heightScale = viewHeight / drawableHeight;

		if (mScaleType == ScaleType.CENTER) {
			mBaseMatrix.postTranslate((viewWidth - drawableWidth) / 2F, (viewHeight - drawableHeight) / 2F);

		} else if (mScaleType == ScaleType.CENTER_CROP) {
			float scale = Math.max(widthScale, heightScale);
			mBaseMatrix.postScale(scale, scale);
			mBaseMatrix.postTranslate((viewWidth - drawableWidth * scale) / 2F,
					(viewHeight - drawableHeight * scale) / 2F);

		} else if (mScaleType == ScaleType.CENTER_INSIDE) {
			float scale = Math.min(1.0f, Math.min(widthScale, heightScale));
			mBaseMatrix.postScale(scale, scale);
			mBaseMatrix.postTranslate((viewWidth - drawableWidth * scale) / 2F,
					(viewHeight - drawableHeight * scale) / 2F);

		} else {
			RectF mTempSrc = new RectF(0, 0, drawableWidth, drawableHeight);
			RectF mTempDst = new RectF(0, 0, viewWidth, viewHeight);

			switch (mScaleType) {
				case FIT_CENTER:
					mBaseMatrix.setRectToRect(mTempSrc, mTempDst, ScaleToFit.CENTER);
					break;

				case FIT_START:
					mBaseMatrix.setRectToRect(mTempSrc, mTempDst, ScaleToFit.START);
					break;

				case FIT_END:
					mBaseMatrix.setRectToRect(mTempSrc, mTempDst, ScaleToFit.END);
					break;

				case FIT_XY:
					mBaseMatrix.setRectToRect(mTempSrc, mTempDst, ScaleToFit.FILL);
					break;

				default:
					break;
			}
		}

		resetMatrix();
	}

  public final boolean canZoom()
  {
    return this.mZoomEnabled;
  }

  public final void cleanup()
  {
    /*if (this.mImageView != null)
      ((ImageView)this.mImageView.get()).getViewTreeObserver().removeGlobalOnLayoutListener(this);
    this.mViewTreeObserver = null;
    this.mMatrixChangeListener = null;
    this.mPhotoTapListener = null;
    this.mViewTapListener = null;
    this.mImageView = null;*/
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
    {
      if (null != mImageView) {
        mImageView.get().getViewTreeObserver().removeOnGlobalLayoutListener(this);
      }

      if (null != mViewTreeObserver && mViewTreeObserver.isAlive()) {
        mViewTreeObserver.removeOnGlobalLayoutListener(this);

        mViewTreeObserver = null;

// Clear listeners too

        mMatrixChangeListener = null;
        mPhotoTapListener = null;
        mViewTapListener = null;
// Finally, clear ImageView
        mImageView = null;
      }

    }
    else
    {
      if (null != mImageView) {
        mImageView.get().getViewTreeObserver().removeGlobalOnLayoutListener(this);
      }

      if (null != mViewTreeObserver && mViewTreeObserver.isAlive()) {
        mViewTreeObserver.removeGlobalOnLayoutListener(this);

        mViewTreeObserver = null;
// Clear listeners too
        mMatrixChangeListener = null;
        mPhotoTapListener = null;
        mViewTapListener = null;
// Finally, clear ImageView
        mImageView = null;
      }
    }
  }

  protected Matrix getDisplayMatrix()
  {
    this.mDrawMatrix.set(this.mBaseMatrix);
    this.mDrawMatrix.postConcat(this.mSuppMatrix);
    return this.mDrawMatrix;
  }

  public final RectF getDisplayRect()
  {
    checkMatrixBounds();
    return getDisplayRect(getDisplayMatrix());
  }

  public final ImageView getImageView()
  {
    WeakReference localWeakReference = this.mImageView;
    ImageView localImageView = null;
    if (localWeakReference != null)
      localImageView = (ImageView)this.mImageView.get();
    if (localImageView == null)
    {
      cleanup();
      throw new IllegalStateException("ImageView no longer exists. You should not use this PhotoViewAttacher any more.");
    }
    return localImageView;
  }

  public float getMaxScale()
  {
    return this.mMaxScale;
  }

  public float getMidScale()
  {
    return this.mMidScale;
  }

  public float getMinScale()
  {
    return this.mMinScale;
  }

  public final float getScale()
  {
    return getValue(this.mSuppMatrix, 0);
  }

  public final ScaleType getScaleType()
  {
    return this.mScaleType;
  }

  public final boolean onDoubleTap(MotionEvent ev) {
		try {
			float scale = getScale();
			float x = ev.getX();
			float y = ev.getY();

			if (scale < mMidScale) {
				zoomTo(mMidScale, x, y);
			} else if (scale >= mMidScale && scale < mMaxScale) {
				zoomTo(mMaxScale, x, y);
			} else {
				zoomTo(mMinScale, x, y);
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// Can sometimes happen when getX() and getY() is called
		}

		return true;
	}

  public final boolean onDoubleTapEvent(MotionEvent paramMotionEvent)
  {
    return false;
  }

  public final void onDrag(float paramFloat1, float paramFloat2)
  {
    if (DEBUG)
    {
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = Float.valueOf(paramFloat1);
      arrayOfObject[1] = Float.valueOf(paramFloat2);
      Log.d("PhotoViewAttacher", String.format("onDrag: dx: %.2f. dy: %.2f", arrayOfObject));
    }
    ImageView localImageView = getImageView();
    if ((localImageView != null) && (hasDrawable(localImageView)))
    {
      this.mSuppMatrix.postTranslate(paramFloat1, paramFloat2);
      checkAndDisplayMatrix();
      if ((this.mAllowParentInterceptOnEdge) && (!this.mScaleDragDetector.isScaling()) && ((this.mScrollEdge == 2) || ((this.mScrollEdge == 0) && (paramFloat1 >= 1.0F)) || ((this.mScrollEdge == 1) && (paramFloat1 <= -1.0F))))
        localImageView.getParent().requestDisallowInterceptTouchEvent(false);
    }
  }

  public final void onFling(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if (DEBUG)
      Log.d("PhotoViewAttacher", "onFling. sX: " + paramFloat1 + " sY: " + paramFloat2 + " Vx: " + paramFloat3 + " Vy: " + paramFloat4);
    ImageView localImageView = getImageView();
    if (hasDrawable(localImageView))
    {
      this.mCurrentFlingRunnable = new FlingRunnable(localImageView.getContext());
      this.mCurrentFlingRunnable.fling(localImageView.getWidth(), localImageView.getHeight(), (int)paramFloat3, (int)paramFloat4);
      localImageView.post(this.mCurrentFlingRunnable);
    }
  }

  public final void onGlobalLayout()
  {
    ImageView localImageView = getImageView();
    if ((localImageView != null) && (this.mZoomEnabled))
    {
      int i = localImageView.getTop();
      int j = localImageView.getRight();
      int k = localImageView.getBottom();
      int m = localImageView.getLeft();
      if ((i != this.mIvTop) || (k != this.mIvBottom) || (m != this.mIvLeft) || (j != this.mIvRight))
      {
        updateBaseMatrix(localImageView.getDrawable());
        this.mIvTop = i;
        this.mIvRight = j;
        this.mIvBottom = k;
        this.mIvLeft = m;
      }
    }
  }

  public final void onScale(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (DEBUG)
    {
      Object[] arrayOfObject = new Object[3];
      arrayOfObject[0] = Float.valueOf(paramFloat1);
      arrayOfObject[1] = Float.valueOf(paramFloat2);
      arrayOfObject[2] = Float.valueOf(paramFloat3);
      Log.d("PhotoViewAttacher", String.format("onScale: scale: %.2f. fX: %.2f. fY: %.2f", arrayOfObject));
    }
    if ((hasDrawable(getImageView())) && ((getScale() < this.mMaxScale) || (paramFloat1 < 1.0F)))
    {
      this.mSuppMatrix.postScale(paramFloat1, paramFloat1, paramFloat2, paramFloat3);
      checkAndDisplayMatrix();
    }
  }

  public void onScaleEnd(ScaleGestureDetector paramScaleGestureDetector)
  {
    this.scaleListener.onScaleEnd(paramScaleGestureDetector);
  }

  public final boolean onSingleTapConfirmed(MotionEvent paramMotionEvent)
  {
    ImageView localImageView = getImageView();
    if (localImageView != null)
    {
      if (this.mPhotoTapListener != null)
      {
        RectF localRectF = getDisplayRect();
        if (localRectF != null)
        {
          float f1 = paramMotionEvent.getX();
          float f2 = paramMotionEvent.getY();
          if (localRectF.contains(f1, f2))
          {
            float f3 = (f1 - localRectF.left) / localRectF.width();
            float f4 = (f2 - localRectF.top) / localRectF.height();
            this.mPhotoTapListener.onPhotoTap(localImageView, f3, f4);
            return true;
          }
        }
      }
      if (this.mViewTapListener != null)
        this.mViewTapListener.onViewTap(localImageView, paramMotionEvent.getX(), paramMotionEvent.getY());
    }
    return false;
  }

  @Override
	public final boolean onTouch(View v, MotionEvent ev) {
		boolean handled = false;

		if (mZoomEnabled) {
			switch (ev.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// First, disable the Parent from intercepting the touch
					// event
					v.getParent().requestDisallowInterceptTouchEvent(true);

					// If we're flinging, and the user presses down, cancel
					// fling
					cancelFling();
					break;

				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP:
					// If the user has zoomed less than min scale, zoom back
					// to min scale
					if (getScale() < mMinScale) {
						RectF rect = getDisplayRect();
						if (null != rect) {
							v.post(new AnimatedZoomRunnable(getScale(), mMinScale, rect.centerX(), rect.centerY()));
							handled = true;
						}
					}
					break;
			}

			// Check to see if the user double tapped
			if (null != mGestureDetector && mGestureDetector.onTouchEvent(ev)) {
				handled = true;
			}

			// Finally, try the Scale/Drag detector
			if (null != mScaleDragDetector && mScaleDragDetector.onTouchEvent(ev)) {
				handled = true;
			}
		}

		return handled;
	}

  public void setAllowParentInterceptOnEdge(boolean paramBoolean)
  {
    this.mAllowParentInterceptOnEdge = paramBoolean;
  }

  public void setMaxScale(float paramFloat)
  {
    checkZoomLevels(this.mMinScale, this.mMidScale, paramFloat);
    this.mMaxScale = paramFloat;
  }

  public void setMidScale(float paramFloat)
  {
    checkZoomLevels(this.mMinScale, paramFloat, this.mMaxScale);
    this.mMidScale = paramFloat;
  }

  public void setMinScale(float paramFloat)
  {
    checkZoomLevels(paramFloat, this.mMidScale, this.mMaxScale);
    this.mMinScale = paramFloat;
  }

  public final void setOnLongClickListener(View.OnLongClickListener paramOnLongClickListener)
  {
    this.mLongClickListener = paramOnLongClickListener;
  }

  public final void setOnMatrixChangeListener(OnMatrixChangedListener paramOnMatrixChangedListener)
  {
    this.mMatrixChangeListener = paramOnMatrixChangedListener;
  }

  public final void setOnPhotoTapListener(OnPhotoTapListener paramOnPhotoTapListener)
  {
    this.mPhotoTapListener = paramOnPhotoTapListener;
  }

  public void setOnScaleListener(onScaleListener paramonScaleListener)
  {
    this.scaleListener = paramonScaleListener;
  }

  public final void setOnViewTapListener(OnViewTapListener paramOnViewTapListener)
  {
    this.mViewTapListener = paramOnViewTapListener;
  }

  public final void setScaleType(ScaleType paramScaleType)
  {
    if ((isSupportedScaleType(paramScaleType)) && (paramScaleType != this.mScaleType))
    {
      this.mScaleType = paramScaleType;
      update();
    }
  }

  public final void setZoomable(boolean paramBoolean)
  {
    this.mZoomEnabled = paramBoolean;
    update();
  }

  public final void update()
  {
    ImageView localImageView = getImageView();
    if (localImageView != null)
    {
      if (this.mZoomEnabled)
      {
        setImageViewScaleTypeMatrix(localImageView);
        updateBaseMatrix(localImageView.getDrawable());
      }
    }
    else
      return;
    resetMatrix();
  }

  public final void zoomTo(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    ImageView localImageView = getImageView();
    if (localImageView != null)
      localImageView.post(new AnimatedZoomRunnable(getScale(), paramFloat1, paramFloat2, paramFloat3));
  }

  private class AnimatedZoomRunnable implements Runnable {

		// These are 'postScale' values, means they're compounded each iteration
		static final float ANIMATION_SCALE_PER_ITERATION_IN = 1.07f;
		static final float ANIMATION_SCALE_PER_ITERATION_OUT = 0.93f;

		private final float mFocalX, mFocalY;
		private final float mTargetZoom;
		private final float mDeltaScale;

		public AnimatedZoomRunnable(final float currentZoom, final float targetZoom, final float focalX,
				final float focalY) {
			mTargetZoom = targetZoom;
			mFocalX = focalX;
			mFocalY = focalY;

			if (currentZoom < targetZoom) {
				mDeltaScale = ANIMATION_SCALE_PER_ITERATION_IN;
			} else {
				mDeltaScale = ANIMATION_SCALE_PER_ITERATION_OUT;
			}
		}

		public void run() {
			ImageView imageView = getImageView();

			if (null != imageView) {
				mSuppMatrix.postScale(mDeltaScale, mDeltaScale, mFocalX, mFocalY);
				checkAndDisplayMatrix();

				final float currentScale = getScale();

				if ((mDeltaScale > 1f && currentScale < mTargetZoom)
						|| (mDeltaScale < 1f && mTargetZoom < currentScale)) {
					// We haven't hit our target scale yet, so post ourselves
					// again
					Compat.postOnAnimation(imageView, this);

				} else {
					// We've scaled past our target zoom, so calculate the
					// necessary scale so we're back at target zoom
					final float delta = mTargetZoom / currentScale;
					mSuppMatrix.postScale(delta, delta, mFocalX, mFocalY);
					checkAndDisplayMatrix();
				}
			}
		}
	}

  private class FlingRunnable implements Runnable {

		private final ScrollerProxy mScroller;
		private int mCurrentX, mCurrentY;

		public FlingRunnable(Context context) {
			mScroller = ScrollerProxy.getScroller(context);
		}

		public void cancelFling() {
			if (DEBUG) {
				Log.d(LOG_TAG, "Cancel Fling");
			}
			mScroller.forceFinished(true);
		}

		public void fling(int viewWidth, int viewHeight, int velocityX, int velocityY) {
			final RectF rect = getDisplayRect();
			if (null == rect) {
				return;
			}

			final int startX = Math.round(-rect.left);
			final int minX, maxX, minY, maxY;

			if (viewWidth < rect.width()) {
				minX = 0;
				maxX = Math.round(rect.width() - viewWidth);
			} else {
				minX = maxX = startX;
			}

			final int startY = Math.round(-rect.top);
			if (viewHeight < rect.height()) {
				minY = 0;
				maxY = Math.round(rect.height() - viewHeight);
			} else {
				minY = maxY = startY;
			}

			mCurrentX = startX;
			mCurrentY = startY;

			if (DEBUG) {
				Log.d(LOG_TAG, "fling. StartX:" + startX + " StartY:" + startY + " MaxX:" + maxX + " MaxY:" + maxY);
			}

			// If we actually can move, fling the scroller
			if (startX != maxX || startY != maxY) {
				mScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, 0, 0);
			}
		}

		@Override
		public void run() {
			ImageView imageView = getImageView();
			if (null != imageView && mScroller.computeScrollOffset()) {

				final int newX = mScroller.getCurrX();
				final int newY = mScroller.getCurrY();

				if (DEBUG) {
					Log.d(LOG_TAG, "fling run(). CurrentX:" + mCurrentX + " CurrentY:" + mCurrentY + " NewX:" + newX
							+ " NewY:" + newY);
				}

				mSuppMatrix.postTranslate(mCurrentX - newX, mCurrentY - newY);
				setImageViewMatrix(getDisplayMatrix());

				mCurrentX = newX;
				mCurrentY = newY;

				// Post On animation
				Compat.postOnAnimation(imageView, this);
			}
		}
	}

  public static abstract interface OnMatrixChangedListener
  {
    public abstract void onMatrixChanged(RectF paramRectF);
  }

  public static abstract interface OnPhotoTapListener
  {
    public abstract void onPhotoTap(View paramView, float paramFloat1, float paramFloat2);
  }

  public static abstract interface OnViewTapListener
  {
    public abstract void onViewTap(View paramView, float paramFloat1, float paramFloat2);
  }

  public static abstract interface onScaleListener
  {
    public abstract void onScaleEnd(ScaleGestureDetector paramScaleGestureDetector);
  }
}