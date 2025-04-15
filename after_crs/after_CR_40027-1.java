/*Clear lint warnings when the min/target SDK levels change

When you edit Java files, we automatically mark API errors - calling
an API which requires a higher minSdkVersion than the current one
specified in the manifest.

If you then switch to the maniest editor, change the value to the new
desired API level, save the file and come back, the API errors are
still marked in the Java file.

The reason this happens is that the API checker runs incrementally on
Java files, but it's affected by manifest values. This has been
reported by multiple users.

We can't easily perform incremental analysis in this case, and we
don't want to kick off a global rescan of lint, but to avoid
confusion, we can *clear* all lint markers when the minSdkVersion or
targetSdkVersion change. This will make confusing errors go away, and
incremental warnings will be shown when the user next edits the file.
It has the downside that a user who runs Lint on the whole project,
then edits the SDK version and saves the file will suddenly find all
the lint errors cleared, but they can just re-run lint, which is
hopefully a smaller problem than seeing confusing error messages (at
least two users reasonably did not realize that these were stale
errors and thought there was a remaining problem).

Finally, tweak the ManifestInfo class a bit; add nullness annotations,
fix an incorrect usage from the project wizard, fix bug in version
name computation and add a getter to return the code name of the
version name.

Change-Id:I5872733d2f12a4950264d08e689c7c0c025c586e*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java
//Synthetic comment -- index 6f7c5d3..a2c032d 100644

//Synthetic comment -- @@ -1189,6 +1189,7 @@
/**
* Returns the {@link IProject} for the edited file.
*/
    @Nullable
public IProject getProject() {
IFile file = getInputFile();
if (file != null) {
//Synthetic comment -- @@ -1201,6 +1202,7 @@
/**
* Returns the {@link AndroidTargetData} for the edited file.
*/
    @Nullable
public AndroidTargetData getTargetData() {
IProject project = getProject();
if (project != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeProxy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeProxy.java
//Synthetic comment -- index 8479219..19d5e16 100755

//Synthetic comment -- @@ -41,6 +41,7 @@
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.project.SupportLibraryHelper;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Rectangle;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
//Synthetic comment -- @@ -267,7 +268,10 @@
if (editor != null) {
// Possibly replace the tag with a compatibility version if the
// minimum SDK requires it
            IProject project = editor.getProject();
            if (project != null) {
                viewFqcn = SupportLibraryHelper.getTagFor(project, viewFqcn);
            }
}

// Find the descriptor for this FQCN








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestEditor.java
//Synthetic comment -- index a31c329..cf210d1 100644

//Synthetic comment -- @@ -16,9 +16,9 @@

package com.android.ide.eclipse.adt.internal.editors.manifest;

import static com.android.ide.common.layout.LayoutConstants.ATTR_NAME;
import static com.android.ide.eclipse.adt.internal.editors.manifest.descriptors.AndroidManifestDescriptors.USES_PERMISSION;
import static com.android.util.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.ide.eclipse.adt.AdtConstants;
//Synthetic comment -- @@ -32,6 +32,7 @@
import com.android.ide.eclipse.adt.internal.editors.manifest.pages.PermissionPage;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.lint.EclipseLintClient;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IFileListener;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
//Synthetic comment -- @@ -39,9 +40,11 @@
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.ui.IEditorInput;
//Synthetic comment -- @@ -131,6 +134,49 @@
return true;
}

    @Override
    public void doSave(IProgressMonitor monitor) {
        // Look up the current (pre-save) values of minSdkVersion and targetSdkVersion
        int prevMinSdkVersion = -1;
        int prevTargetSdkVersion = -1;
        IProject project = null;
        ManifestInfo info = null;
        try {
            project = getProject();
            if (project != null) {
                info = ManifestInfo.get(project);
                prevMinSdkVersion = info.getMinSdkVersion();
                prevTargetSdkVersion = info.getTargetSdkVersion();
                info.clear();
            }
        } catch (Throwable t) {
            // We don't expect exceptions from the above calls, but we *really*
            // need to make sure that nothing can prevent the save function from
            // getting called!
            AdtPlugin.log(t, null);
        }

        // Actually save
        super.doSave(monitor);

        // If the target/minSdkVersion has changed, clear all lint warnings (since many
        // of them are tied to the min/target sdk levels), in order to avoid showing stale
        // results
        try {
            if (info != null) {
                int newMinSdkVersion = info.getMinSdkVersion();
                int newTargetSdkVersion = info.getTargetSdkVersion();
                if (newMinSdkVersion != prevMinSdkVersion
                        || newTargetSdkVersion != prevTargetSdkVersion) {
                    assert project != null;
                    EclipseLintClient.clearMarkers(project);
                }
            }
        } catch (Throwable t) {
            AdtPlugin.log(t, null);
        }
    }

/**
* Creates the various form pages.
*/








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfo.java
//Synthetic comment -- index 993b6fc..d9fd476 100644

//Synthetic comment -- @@ -135,6 +135,14 @@
}

/**
     * Clears the cached manifest information. The next get call on one of the
     * properties will cause the information to be refreshed.
     */
    public void clear() {
        mLastChecked = 0;
    }

    /**
* Returns the {@link ManifestInfo} for the given project
*
* @param project the project the finder is associated with
//Synthetic comment -- @@ -194,7 +202,7 @@
mManifestTheme = null;
mTargetSdk = 1; // Default when not specified
mMinSdk = 1; // Default when not specified
        mMinSdkName = "1"; // Default when not specified
mPackage = ""; //$NON-NLS-1$
mApplicationIcon = null;
mApplicationLabel = null;
//Synthetic comment -- @@ -263,6 +271,9 @@
String valueString = null;
if (usesSdk.hasAttributeNS(NS_RESOURCES, attribute)) {
valueString = usesSdk.getAttributeNS(NS_RESOURCES, attribute);
            if (attribute.equals(ATTRIBUTE_MIN_SDK_VERSION)) {
                mMinSdkName = valueString;
            }
}

if (valueString != null) {
//Synthetic comment -- @@ -279,10 +290,6 @@
apiLevel = target.getVersion().getApiLevel() + 1;
}
}
}

return apiLevel;
//Synthetic comment -- @@ -400,16 +407,38 @@

/**
* Returns the minimum SDK version name (which may not be a numeric string, e.g.
     * it could be a codename). It will never be null or empty; if no min sdk version
     * was specified in the manifest, the return value will be "1". Use
     * {@link #getCodeName()} instead if you want to look up whether there is a code name.
*
* @return the minimum SDK version
*/
    @NonNull
public String getMinSdkName() {
sync();
        if (mMinSdkName == null || mMinSdkName.isEmpty()) {
            mMinSdkName = "1"; //$NON-NLS-1$
        }

return mMinSdkName;
}

/**
     * Returns the code name used for the minimum SDK version, if any.
     *
     * @return the minSdkVersion codename or null
     */
    @Nullable
    public String getMinSdkCodeName() {
        String minSdkName = getMinSdkName();
        if (!Character.isDigit(minSdkName.charAt(0))) {
            return minSdkName;
        }

        return null;
    }

    /**
* Returns the {@link IPackageFragment} for the package registered in the manifest
*
* @return the {@link IPackageFragment} for the package registered in the manifest








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplateWizardState.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplateWizardState.java
//Synthetic comment -- index 5deae30..00183d2 100644

//Synthetic comment -- @@ -142,8 +142,8 @@

ManifestInfo manifest = ManifestInfo.get(project);
parameters.put(ATTR_PACKAGE_NAME, manifest.getPackage());
        parameters.put(ATTR_MIN_API, manifest.getMinSdkName());
        parameters.put(ATTR_MIN_API_LEVEL, manifest.getMinSdkVersion());
parameters.put(ATTR_TARGET_API, manifest.getTargetSdkVersion());
parameters.put(ATTR_BUILD_API, getBuildApi());
parameters.put(ATTR_COPY_ICONS, mIconState == null);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfoTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfoTest.java
//Synthetic comment -- index 5b107cd..faf8ada 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import java.io.InputStream;
import java.util.Map;

@SuppressWarnings("javadoc")
public class ManifestInfoTest extends AdtProjectTest {
@Override
protected boolean testCaseNeedsUniqueProject() {
//Synthetic comment -- @@ -114,10 +115,10 @@
public void testGetActivityThemes5() throws Exception {
ManifestInfo info = getManifestInfo(
"<manifest xmlns:android='http://schemas.android.com/apk/res/android'\n" +
                "    package='com.android.unittest'>\n" +
"    <application\n" +
"        android:label='@string/app_name'\n" +
                "        android:theme='@style/NoBackground'\n" +
"        android:name='.app.TestApp' android:icon='@drawable/app_icon'>\n" +
"\n" +
"        <activity\n" +
//Synthetic comment -- @@ -212,7 +213,41 @@
} else {
file.create(bstream, false /* force */, new NullProgressMonitor());
}
        ManifestInfo info = ManifestInfo.get(getProject());
        info.clear();
        return info;
    }

    public void testGetMinSdkVersionName() throws Exception {
        ManifestInfo info;

        info = getManifestInfo(
                "<manifest xmlns:android='http://schemas.android.com/apk/res/android'\n" +
                "    package='com.android.unittest'>\n" +
                "    <uses-sdk android:minSdkVersion='3' android:targetSdkVersion='4'/>\n" +
                "</manifest>\n");
        assertEquals(3, info.getMinSdkVersion());
        assertEquals("3", info.getMinSdkName());
        assertEquals(4, info.getTargetSdkVersion());
        assertNull(info.getMinSdkCodeName());

        info = getManifestInfo(
                "<manifest xmlns:android='http://schemas.android.com/apk/res/android'\n" +
                "    package='com.android.unittest'>\n" +
                "    <uses-sdk android:targetSdkVersion='4'/>\n" +
                "</manifest>\n");
        assertEquals("1", info.getMinSdkName());
        assertEquals(1, info.getMinSdkVersion());
        assertEquals(4, info.getTargetSdkVersion());
        assertNull(info.getMinSdkCodeName());

        info = getManifestInfo(
                "<manifest xmlns:android='http://schemas.android.com/apk/res/android'\n" +
                "    package='com.android.unittest'>\n" +
                "    <uses-sdk android:minSdkVersion='JellyBean' />\n" +
                "</manifest>\n");
        assertEquals("JellyBean", info.getMinSdkName());
        assertEquals("JellyBean", info.getMinSdkCodeName());
}

private static class TestAndroidTarget implements IAndroidTarget {







