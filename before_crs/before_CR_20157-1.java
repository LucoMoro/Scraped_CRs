/*Add log to layout lib init method.

Change-Id:I28efe429925a77fd10b76bb54519ae9d42900e52*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index cae30fe..49b0884 100644

//Synthetic comment -- @@ -103,7 +103,6 @@
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
//Synthetic comment -- @@ -867,6 +866,10 @@
* {@link String#format(String, Object...)}.
*/
public static void log(int severity, String format, Object ... args) {
String message = String.format(format, args);
Status status = new Status(severity, PLUGIN_ID, message);

//Synthetic comment -- @@ -890,7 +893,12 @@
* {@link String#format(String, Object...)}.
*/
public static void log(Throwable exception, String format, Object ... args) {
        String message = String.format(format, args);
Status status = new Status(IStatus.ERROR, PLUGIN_ID, message, exception);

if (getDefault() != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java
//Synthetic comment -- index e214407..b75d6fd 100644

//Synthetic comment -- @@ -17,7 +17,9 @@
package com.android.ide.eclipse.adt.internal.sdk;

import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.internal.editors.descriptors.IDescriptorProvider;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.editors.manifest.descriptors.AndroidManifestDescriptors;
//Synthetic comment -- @@ -29,6 +31,8 @@
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.IAndroidTarget.IOptionalLibrary;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
//Synthetic comment -- @@ -248,7 +252,31 @@
*/
public synchronized LayoutLibrary getLayoutLibrary() {
if (mLayoutBridgeInit == false && mLayoutLibrary.getStatus() == LoadStatus.LOADED) {
            mLayoutLibrary.init(new File(mTarget.getPath(IAndroidTarget.FONTS)), getEnumValueMap());
mLayoutBridgeInit = true;
}









//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java b/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java
//Synthetic comment -- index 878f9fd..c9d9f13 100644

//Synthetic comment -- @@ -227,13 +227,15 @@
* @param fontLocation the location of the fonts in the SDK target.
* @param enumValueMap map attrName => { map enumFlagName => Integer value }. This is typically
*          read from attrs.xml in the SDK target.
* @return true if success.
*
* @see Bridge#init(String, Map)
*/
    public boolean init(File fontLocation, Map<String, Map<String, Integer>> enumValueMap) {
if (mBridge != null) {
            return mBridge.init(fontLocation, enumValueMap);
} else if (mLegacyBridge != null) {
return mLegacyBridge.init(fontLocation.getAbsolutePath(), enumValueMap);
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/Bridge.java b/layoutlib_api/src/com/android/ide/common/rendering/api/Bridge.java
//Synthetic comment -- index b2c2109..79741ea 100644

//Synthetic comment -- @@ -51,10 +51,13 @@
* Initializes the Bridge object.
*
* @param fontLocation the location of the fonts.
     * @param enumValueMap map attrName => { map enumFlagName => Integer value }.
* @return true if success.
*/
    public boolean init(File fontLocation, Map<String, Map<String, Integer>> enumValueMap) {
return false;
}








