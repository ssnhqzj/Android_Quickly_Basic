package comm.lib.photoview;

import android.os.Build;
import android.view.View;

public class Compat
{
  private static final int SIXTY_FPS_INTERVAL = 16;

  public static void postOnAnimation(View paramView, Runnable paramRunnable)
  {
    if (Build.VERSION.SDK_INT >= 16)
    {
      SDK16.postOnAnimation(paramView, paramRunnable);
      return;
    }
    paramView.postDelayed(paramRunnable, 16L);
  }
}