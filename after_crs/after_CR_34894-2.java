/*Modify size of view for HVGA display.

In the test case "android.media.cts.MediaPlayerTest -- testVideoSurfaceResetting",current CTS demand to display three view (size of 320x240) on LCD.
However, HVGA device can not display three 320x240 view.
Then the test case is failed because 3rd view is displayed on outside of display area of LCD.
This patch is for changing size of view to 160x120.

Change-Id:Ia2ee225cecc9f6e7686e011fa34a9e2502b49474Signed-off-by: nao <nao.tanaka.cy@kyocera.jp>*/




//Synthetic comment -- diff --git a/tests/src/android/media/cts/MediaStubActivity.java b/tests/src/android/media/cts/MediaStubActivity.java
//Synthetic comment -- index f0ca755..bd9703d 100644

//Synthetic comment -- @@ -18,6 +18,7 @@
import com.android.cts.stub.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
//Synthetic comment -- @@ -25,6 +26,8 @@
public class MediaStubActivity extends Activity {
public static final int WIDTH = 320;
public static final int HEIGHT = 240;
    public int WIDTH;
    public int HEIGHT;
private SurfaceHolder mHolder;
private SurfaceHolder mHolder2;

//Synthetic comment -- @@ -33,6 +36,11 @@
super.onCreate(savedInstanceState);
setContentView(R.layout.mediaplayer);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        HEIGHT = metrics.heightPixels / 3;
        WIDTH = HEIGHT * 4 / 3;

SurfaceView surfaceV = (SurfaceView)findViewById(R.id.surface);
ViewGroup.LayoutParams lp = surfaceV.getLayoutParams();
lp.width = WIDTH;







