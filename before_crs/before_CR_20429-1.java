/*Add prefix constants for LayoutLog tags.

Some tags have "categories", for instance "resources.resolve"
I created a new constant that contains "resources."

Change-Id:Ifda535b3378021a275e73d988bcf5707e9ab39ff*/
//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/LayoutLog.java b/layoutlib_api/src/com/android/ide/common/rendering/api/LayoutLog.java
//Synthetic comment -- index d2b3dba..3a0ab06 100644

//Synthetic comment -- @@ -17,14 +17,16 @@
package com.android.ide.common.rendering.api;

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







