/*Don't warn about missing SDK when bundled

Change-Id:I88d76329ab5f3afa4fda338c170a78bef1939165*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index e08d7ea..796ef87 100644

//Synthetic comment -- @@ -46,6 +46,7 @@
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.Sdk.ITargetChangeListener;
import com.android.ide.eclipse.adt.internal.ui.EclipseUiHelper;
import com.android.ide.eclipse.base.InstallDetails;
import com.android.ide.eclipse.ddms.DdmsPlugin;
import com.android.io.StreamException;
import com.android.resources.ResourceFolderType;
//Synthetic comment -- @@ -1271,6 +1272,10 @@
public boolean checkSdkLocationAndId(@Nullable String osSdkLocation,
@NonNull CheckSdkErrorHandler errorHandler) {
if (osSdkLocation == null || osSdkLocation.trim().length() == 0) {
            if (InstallDetails.isAndroidIdePackage()) {
                // Handled through AdtStartup instead
                return false;
            }
return errorHandler.handleError(
Solution.OPEN_ANDROID_PREFS,
"Location of the Android SDK has not been setup in the preferences.");







