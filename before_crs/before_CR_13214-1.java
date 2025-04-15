/*Added GLWallpaperService which is an adapation of GLSurfaceView but using WallpaperService.Engine instead.  It provides a GLEngine implementation which uses most of the same code as GLSurfaceView but modified to handle multiple thread instances better as Engine requires.  I recommend that this class be accepted but reviewed alongside GLSurfaceView and that both be refactored to use a common set of classes for easy maintenance going forward.  For public interface, I'd like to see a single GLRenderer interface that both use so that developers can develop a Renderer using a GLSurfaceView and then move it to a Live Wallpaper using the same interface.*/
//Synthetic comment -- diff --git a/opengl/java/android/opengl/GLWallpaperService.java b/opengl/java/android/opengl/GLWallpaperService.java
new file mode 100755
//Synthetic comment -- index 0000000..7bf4704

//Synthetic comment -- @@ -0,0 +1,884 @@
\ No newline at end of file







