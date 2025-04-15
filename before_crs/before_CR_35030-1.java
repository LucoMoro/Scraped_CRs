/*Fix testVideoSurfaceResetting for low resolution screens

testVideoSurfaceRestting from MediaPlayer suite had a bug when run on devices
with low resolution screens.
The test attempts to display video on 3 SurfaceView instances.
First and second SurfaceViews are set to be 320 x 240 pixels,
third one is not initialized.
If this is run on high resolution screen, (e.g. 480 x 854 pixels),
first and second objects use about 2/3 of the screen  – 480 pixels height +
app header. The rest of the screen (a little less than 1/3) is available
for the third object, and it uses that area.
If this test is run on low resolution screen, (e.g. 320 x 480 pixels),
first and second objects use again 480 pixels +  the app header,
but the bottom part of the second object is below the physical display
bottom. There is no area available for the third SurfaceView, so video
cannot be played on it, and the test fails due to exception.

Change-Id:If357c811abd27adfc37f000be127ab4e2cf3a894*/
//Synthetic comment -- diff --git a/tests/src/android/media/cts/MediaStubActivity.java b/tests/src/android/media/cts/MediaStubActivity.java
//Synthetic comment -- index f0ca755..e6741d9 100644

//Synthetic comment -- @@ -21,10 +21,14 @@
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

public class MediaStubActivity extends Activity {
public static final int WIDTH = 320;
public static final int HEIGHT = 240;
private SurfaceHolder mHolder;
private SurfaceHolder mHolder2;

//Synthetic comment -- @@ -33,10 +37,24 @@
super.onCreate(savedInstanceState);
setContentView(R.layout.mediaplayer);

SurfaceView surfaceV = (SurfaceView)findViewById(R.id.surface);
ViewGroup.LayoutParams lp = surfaceV.getLayoutParams();
        lp.width = WIDTH;
        lp.height = HEIGHT;
surfaceV.setLayoutParams(lp);
mHolder = surfaceV.getHolder();
mHolder.setFixedSize(WIDTH, HEIGHT);
//Synthetic comment -- @@ -44,8 +62,8 @@

SurfaceView surfaceV2 = (SurfaceView)findViewById(R.id.surface2);
ViewGroup.LayoutParams lp2 = surfaceV2.getLayoutParams();
        lp2.width = WIDTH;
        lp2.height = HEIGHT;
surfaceV2.setLayoutParams(lp2);
mHolder2 = surfaceV2.getHolder();
mHolder2.setFixedSize(WIDTH, HEIGHT);







