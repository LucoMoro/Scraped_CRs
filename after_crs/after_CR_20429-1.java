/*Add prefix constants for LayoutLog tags.

Some tags have "categories", for instance "resources.resolve"
I created a new constant that contains "resources."

Change-Id:Ifda535b3378021a275e73d988bcf5707e9ab39ff*/




//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/LayoutLog.java b/layoutlib_api/src/com/android/ide/common/rendering/api/LayoutLog.java
//Synthetic comment -- index d2b3dba..3a0ab06 100644

//Synthetic comment -- @@ -17,14 +17,16 @@
package com.android.ide.common.rendering.api;

public class LayoutLog {
    public final static String TAG_RESOURCES_PREFIX = "resources.";
    public final static String TAG_MATRIX_PREFIX = "matrix.";

public final static String TAG_UNSUPPORTED = "unsupported";
public final static String TAG_BROKEN = "broken";
    public final static String TAG_RESOURCES_RESOLVE = TAG_RESOURCES_PREFIX + "resolve";
    public final static String TAG_RESOURCES_READ = TAG_RESOURCES_PREFIX + "read";
    public final static String TAG_RESOURCES_FORMAT = TAG_RESOURCES_PREFIX + "format";
    public final static String TAG_MATRIX_AFFINE = TAG_MATRIX_PREFIX + "affine";
    public final static String TAG_MATRIX_INVERSE = TAG_MATRIX_PREFIX + "inverse";
public final static String TAG_MASKFILTER = "maskfilter";
public final static String TAG_DRAWFILTER = "drawfilter";
public final static String TAG_PATHEFFECT = "patheffect";







