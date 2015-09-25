package comm.lib.photoview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

public class HackyViewPager extends ViewPager
{
  public HackyViewPager(Context paramContext)
  {
    super(paramContext);
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    try
    {
      boolean bool = super.onInterceptTouchEvent(paramMotionEvent);
      return bool;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      localIllegalArgumentException.printStackTrace();
    }
    return false;
  }
}