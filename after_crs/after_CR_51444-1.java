/*Fix sdkcommon/lint API usage in ADT.

Change-Id:I4672ec7504daf1bf1e5c0880e5aa2003c082e7da*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index 223e5e5..697a0bc 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.sdk.SdkVersionInfo;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
//Synthetic comment -- @@ -989,7 +990,7 @@
* @return the highest known API number
*/
public static int getHighestKnownApiLevel() {
        return SdkVersionInfo.HIGHEST_KNOWN_API;
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/AddSuppressAnnotation.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/AddSuppressAnnotation.java
//Synthetic comment -- index d7a0758..365c73d 100644

//Synthetic comment -- @@ -25,6 +25,7 @@

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.sdk.SdkVersionInfo;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
//Synthetic comment -- @@ -409,7 +410,7 @@
// @TargetApi is only valid on methods and classes, not fields etc
&& (body instanceof MethodDeclaration
|| body instanceof TypeDeclaration)) {
                    String apiString = SdkVersionInfo.getBuildCode(api);
if (apiString == null) {
apiString = Integer.toString(api);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/AddSuppressAttribute.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/AddSuppressAttribute.java
//Synthetic comment -- index 0d751cb..0d8fd33 100644

//Synthetic comment -- @@ -22,6 +22,7 @@

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.sdk.SdkVersionInfo;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
//Synthetic comment -- @@ -186,7 +187,7 @@
if (matcher.find()) {
api = Integer.parseInt(matcher.group(1));
String targetApi;
                    String buildCode = SdkVersionInfo.getBuildCode(api);
if (buildCode != null) {
targetApi = buildCode.toLowerCase(Locale.US);
fixes.add(new AddSuppressAttribute(editor, id, marker, element,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateHandlerTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateHandlerTest.java
//Synthetic comment -- index 4fc2fbe..48681d0 100644

//Synthetic comment -- @@ -17,7 +17,6 @@

import static com.android.SdkConstants.CURRENT_PLATFORM;
import static com.android.SdkConstants.FD_TOOLS;
import static com.android.SdkConstants.PLATFORM_WINDOWS;
import static com.android.ide.eclipse.adt.internal.wizards.templates.NewProjectWizard.ATTR_MIN_API;
import static com.android.ide.eclipse.adt.internal.wizards.templates.NewProjectWizard.ATTR_MIN_BUILD_API;
//Synthetic comment -- @@ -25,6 +24,7 @@

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.sdk.SdkVersionInfo;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.lint.EclipseLintClient;
//Synthetic comment -- @@ -329,14 +329,16 @@
}

for (int minSdk = 1;
                     minSdk <= SdkVersionInfo.HIGHEST_KNOWN_API;
                     minSdk++) {
// Don't bother checking *every* single minSdk, just pick some interesting ones
if (!isInterestingApiLevel(minSdk)) {
continue;
}

                for (int targetSdk = minSdk;
                         targetSdk <= SdkVersionInfo.HIGHEST_KNOWN_API;
                         targetSdk++) {
if (!isInterestingApiLevel(targetSdk)) {
continue;
}
//Synthetic comment -- @@ -440,14 +442,16 @@
}

for (int minSdk = 1;
                     minSdk <= SdkVersionInfo.HIGHEST_KNOWN_API;
                     minSdk++) {
// Don't bother checking *every* single minSdk, just pick some interesting ones
if (!isInterestingApiLevel(minSdk)) {
continue;
}

                for (int targetSdk = minSdk;
                         targetSdk <= SdkVersionInfo.HIGHEST_KNOWN_API;
                         targetSdk++) {
if (!isInterestingApiLevel(targetSdk)) {
continue;
}







