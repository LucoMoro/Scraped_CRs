/*Modify size of view for HVGA display.

In the test case "android.media.cts.MediaPlayerTest -- testVideoSurfaceResetting",current CTS demand to display three view (size of 320x240) on LCD.
However, HVGA device can not display three 320x240 view.
Then the test case is failed because 3rd view is displayed on outside of display area of LCD.
This patch is for changing size of view to 160x120.

Change-Id:Ia2ee225cecc9f6e7686e011fa34a9e2502b49474Signed-off-by: nao <nao.tanaka.cy@kyocera.jp>*/
//Synthetic comment -- diff --git a/tests/src/android/media/cts/MediaStubActivity.java b/tests/src/android/media/cts/MediaStubActivity.java
//Synthetic comment -- index f0ca755..64255f3 100644

//Synthetic comment -- @@ -23,8 +23,8 @@
import android.view.ViewGroup;

public class MediaStubActivity extends Activity {
    public static final int WIDTH = 320;
    public static final int HEIGHT = 240;
private SurfaceHolder mHolder;
private SurfaceHolder mHolder2;








