/*Update documentation for getVideoWidth and getVideoHeight.

Ref:http://code.google.com/p/android/issues/detail?id=3024*/




//Synthetic comment -- diff --git a/media/java/android/media/MediaPlayer.java b/media/java/android/media/MediaPlayer.java
//Synthetic comment -- index 19ab0ad..2cb9ac9 100644

//Synthetic comment -- @@ -781,18 +781,22 @@
/**
* Returns the width of the video.
* 
     * @return the width of the video, or 0 if there is no video, 
     * no display surface was set, or the width has not been determined 
     * yet. The OnVideoSizeChangedListener can be registered via 
     * {@link #setOnVideoSizeChangedListener(OnVideoSizeChangedListener)} 
     * to provide a notification when the width is available. 
*/
public native int getVideoWidth();

/**
* Returns the height of the video.
* 
     * @return the height of the video, or 0 if there is no video, 
     * no display surface was set, or the height has not been determined 
     * yet. The OnVideoSizeChangedListener can be registered via 
     * {@link #setOnVideoSizeChangedListener(OnVideoSizeChangedListener)} 
     * to provide a notification when the height is available. 
*/
public native int getVideoHeight();








