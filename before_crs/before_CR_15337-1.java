/*Replaced /sdcard with Environment.getExternalStorageDirectory()

Change-Id:I92fca47bf9362a4cbd8b07b6f5ec5a5a6c0cd13b*/
//Synthetic comment -- diff --git a/samples/BrowserPlugin/src/com/android/sampleplugin/SamplePluginStub.java b/samples/BrowserPlugin/src/com/android/sampleplugin/SamplePluginStub.java
//Synthetic comment -- index 3c0a0c7..1e0fd0a 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
//Synthetic comment -- @@ -37,15 +38,15 @@
//needed for jni calls
System.loadLibrary("sampleplugin");
}
    
public View getEmbeddedView(final int npp, Context context) {
        
final SurfaceView view = new SurfaceView(context);

/* You can do all sorts of interesting operations on the surface view
* here. We illustrate a few of the important operations below.
*/
        
//TODO get pixel format from the subplugin (via jni)
view.getHolder().setFormat(PixelFormat.RGBA_8888);
view.getHolder().addCallback(new Callback() {
//Synthetic comment -- @@ -61,23 +62,23 @@
public void surfaceDestroyed(SurfaceHolder holder) {
nativeSurfaceDestroyed(npp);
}
            
});
        
if (nativeIsFixedSurface(npp)) {
            //TODO get the fixed dimensions from the plugin 
//view.getHolder().setFixedSize(width, height);
}
        
return view;
}
    
public View getFullScreenView(int npp, Context context) {
        
/* TODO make this aware of the plugin instance and get the video file
* from the plugin.
*/
        
FrameLayout layout = new FrameLayout(context);
LayoutParams fp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
layout.setLayoutParams(fp);
//Synthetic comment -- @@ -89,11 +90,11 @@
GLSurfaceView gl = new GLSurfaceView(context);
LayoutParams gp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
layout.setLayoutParams(gp);
        
layout.addView(video);
layout.addView(gl);
        
        // We want an 8888 pixel format because that's required for a translucent 
// window. And we want a depth buffer.
gl.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
// Tell the cube renderer that we want to render a translucent version
//Synthetic comment -- @@ -102,15 +103,15 @@
// Use a surface format with an Alpha channel:
gl.getHolder().setFormat(PixelFormat.TRANSLUCENT);
gl.setWindowType(WindowManager.LayoutParams.TYPE_APPLICATION_MEDIA_OVERLAY);
        
        
        video.setVideoPath("/sdcard/test_video.3gp");
video.setMediaController(new MediaController(context));
video.requestFocus();
        
return layout;
}
    
private native void nativeSurfaceCreated(int npp, View surfaceView);
private native void nativeSurfaceChanged(int npp, int format, int width, int height);
private native void nativeSurfaceDestroyed(int npp);







