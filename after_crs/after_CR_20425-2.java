/*Add Layoutlib log tag constants to LayoutLog

Change-Id:Idf50886132913728c40c23026c1b55172015904b*/




//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/LayoutLog.java b/layoutlib_api/src/com/android/ide/common/rendering/api/LayoutLog.java
//Synthetic comment -- index 979b873..d2b3dba 100644

//Synthetic comment -- @@ -18,6 +18,22 @@

public class LayoutLog {

    public final static String TAG_UNSUPPORTED = "unsupported";
    public final static String TAG_BROKEN = "broken";
    public final static String TAG_RESOURCES_RESOLVE = "resources.resolve";
    public final static String TAG_RESOURCES_READ = "resources.read";
    public final static String TAG_RESOURCES_FORMAT = "resources.format";
    public final static String TAG_MATRIX_AFFINE = "matrix.affine";
    public final static String TAG_MATRIX_INVERSE = "matrix.inverse";
    public final static String TAG_MASKFILTER = "maskfilter";
    public final static String TAG_DRAWFILTER = "drawfilter";
    public final static String TAG_PATHEFFECT = "patheffect";
    public final static String TAG_COLORFILTER = "colorfilter";
    public final static String TAG_RASTERIZER = "rasterizer";
    public final static String TAG_SHADER = "shader";
    public final static String TAG_XFERMODE = "xfermode";


public void warning(String tag, String message) {
}








