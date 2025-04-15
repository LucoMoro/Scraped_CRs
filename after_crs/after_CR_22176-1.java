/*Make theme selection take rendering target into account

If the current rendering target is less than API level 11, then don't
use the Holo theme even if the project itself has >= 11 as an SDK
target.

Change-Id:I23c8a4865fa4af3c46fbfa34f0a83b18e7ed9b63*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index f941ceb..d3b1f4a 100644

//Synthetic comment -- @@ -1387,7 +1387,7 @@
// Look up the default/fallback theme to use for this project (which
// depends on the screen size when no particular theme is specified
// in the manifest)
                String defaultTheme = manifest.getDefaultTheme(mState.target, screenSize);

Map<String, String> activityThemes = manifest.getActivityThemes();
String pkg = manifest.getPackage();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfo.java
//Synthetic comment -- index 05c4a5e..5e71822 100644

//Synthetic comment -- @@ -93,11 +93,11 @@

private final IProject mProject;
private String mPackage;
    private String mManifestTheme;
private Map<String, String> mActivityThemes;
private IAbstractFile mManifestFile;
private long mLastModified;
    private int mTargetSdk;

/**
* Qualified name for the per-project non-persistent property storing the
//Synthetic comment -- @@ -164,10 +164,10 @@
mLastModified = fileModified;

mActivityThemes = new HashMap<String, String>();
        mManifestTheme = null;
        mTargetSdk = 1; // Default when not specified
mPackage = ""; //$NON-NLS-1$

Document document = null;
try {
DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//Synthetic comment -- @@ -227,17 +227,11 @@
}
}

                        mTargetSdk = apiLevel;
}
}
} else {
                mManifestTheme = defaultTheme;
}
} catch (SAXException e) {
AdtPlugin.log(e, "Malformed manifest");
//Synthetic comment -- @@ -269,18 +263,31 @@

/**
* Returns the default theme for this project, by looking at the manifest default
     * theme registration, target SDK, rendering target, etc.
*
     * @param renderingTarget the rendering target use to render the theme, or null
* @param screenSize the screen size to obtain a default theme for, or null if unknown
* @return the theme to use for this project, never null
*/
    public String getDefaultTheme(IAndroidTarget renderingTarget, ScreenSize screenSize) {
sync();

        if (mManifestTheme != null) {
            return mManifestTheme;
        }

        int renderingTargetSdk = mTargetSdk;
        if (renderingTarget != null) {
            renderingTargetSdk = renderingTarget.getVersion().getApiLevel();
        }

        int apiLevel = Math.min(mTargetSdk, renderingTargetSdk);
        // For now this theme works only on XLARGE screens. When it works for all sizes,
        // add that new apiLevel to this check.
        if (apiLevel >= 11 && screenSize == ScreenSize.XLARGE) {
            return PREFIX_ANDROID_STYLE + "Theme.Holo"; //$NON-NLS-1$
} else {
            return PREFIX_ANDROID_STYLE + "Theme"; //$NON-NLS-1$
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfoTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfoTest.java
//Synthetic comment -- index 590c611..1166ab5 100644

//Synthetic comment -- @@ -21,6 +21,8 @@

import com.android.ide.eclipse.adt.internal.editors.layout.refactoring.AdtProjectTest;
import com.android.ide.eclipse.adt.internal.resources.ResourceHelper;
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.NullProgressMonitor;
//Synthetic comment -- @@ -44,9 +46,9 @@
Map<String, String> map = info.getActivityThemes();
assertEquals(map.toString(), 0, map.size());
assertEquals("com.android.unittest", info.getPackage());
        assertEquals("Theme", ResourceHelper.styleToTheme(info.getDefaultTheme(null, NORMAL)));
        assertEquals("@android:style/Theme", info.getDefaultTheme(null, null));
        assertEquals("Theme", ResourceHelper.styleToTheme(info.getDefaultTheme(null, XLARGE)));
}

public void testGetActivityThemes2() throws Exception {
//Synthetic comment -- @@ -58,8 +60,9 @@
Map<String, String> map = info.getActivityThemes();
assertEquals(map.toString(), 0, map.size());
assertEquals("com.android.unittest", info.getPackage());
        assertEquals("Theme.Holo", ResourceHelper.styleToTheme(info.getDefaultTheme(null,
                XLARGE)));
        assertEquals("Theme", ResourceHelper.styleToTheme(info.getDefaultTheme(null, LARGE)));
}

public void testGetActivityThemes3() throws Exception {
//Synthetic comment -- @@ -71,8 +74,9 @@
Map<String, String> map = info.getActivityThemes();
assertEquals(map.toString(), 0, map.size());
assertEquals("com.android.unittest", info.getPackage());
        assertEquals("Theme.Holo", ResourceHelper.styleToTheme(info.getDefaultTheme(null,
                XLARGE)));
        assertEquals("Theme", ResourceHelper.styleToTheme(info.getDefaultTheme(null, NORMAL)));
}

public void testGetActivityThemes4() throws Exception {
//Synthetic comment -- @@ -97,7 +101,7 @@
""
);
assertEquals("com.android.unittest", info.getPackage());
        assertEquals("Theme", ResourceHelper.styleToTheme(info.getDefaultTheme(null, XLARGE)));

Map<String, String> map = info.getActivityThemes();
assertEquals(map.toString(), 1, map.size());
//Synthetic comment -- @@ -129,18 +133,41 @@
""
);

        assertEquals("@style/NoBackground", info.getDefaultTheme(null, XLARGE));
        assertEquals("@style/NoBackground", info.getDefaultTheme(null, NORMAL));
        assertEquals("NoBackground", ResourceHelper.styleToTheme(info.getDefaultTheme(null,
                NORMAL)));

Map<String, String> map = info.getActivityThemes();
assertEquals(map.toString(), 1, map.size());
assertNull(map.get("com.android.unittest.prefs.PrefsActivity"));
assertEquals("@android:style/Theme.Dialog",
map.get("com.android.unittest.app.IntroActivity"));
    }

    public void testGetActivityThemes6() throws Exception {
        // Ensures that when the *rendering* target is less than version 11, we don't
        // use Holo even though the manifest SDK version calls for it.
        ManifestInfo info = getManifestInfo(
                "<manifest xmlns:android='http://schemas.android.com/apk/res/android'\n" +
                "    package='com.android.unittest'>\n" +
                "    <uses-sdk android:minSdkVersion='3' android:targetSdkVersion='11'/>\n" +
                "</manifest>\n");
        Map<String, String> map = info.getActivityThemes();
        assertEquals(map.toString(), 0, map.size());
        assertEquals("com.android.unittest", info.getPackage());
        assertEquals("Theme.Holo", ResourceHelper.styleToTheme(info.getDefaultTheme(null,
                XLARGE)));

        // Here's the check
        IAndroidTarget olderVersion = new TestAndroidTarget(4);
        assertEquals("Theme", ResourceHelper.styleToTheme(info.getDefaultTheme(olderVersion,
                XLARGE)));

}



private ManifestInfo getManifestInfo(String manifestContents) throws Exception {
InputStream bstream = new ByteArrayInputStream(
manifestContents.getBytes("UTF-8")); //$NON-NLS-1$
//Synthetic comment -- @@ -153,4 +180,116 @@
}
return ManifestInfo.get(getProject());
}

    private static class TestAndroidTarget implements IAndroidTarget {
        private final int mApiLevel;

        public TestAndroidTarget(int apiLevel) {
            mApiLevel = apiLevel;
        }

        public boolean canRunOn(IAndroidTarget target) {
            return false;
        }

        public String[] getAbiList() {
            return null;
        }

        public String getClasspathName() {
            return null;
        }

        public String getDefaultSkin() {
            return null;
        }

        public String getDescription() {
            return null;
        }

        public String getFullName() {
            return null;
        }

        public String getImagePath(String abiType) {
            return null;
        }

        public String getLocation() {
            return null;
        }

        public String getName() {
            return null;
        }

        public IOptionalLibrary[] getOptionalLibraries() {
            return null;
        }

        public IAndroidTarget getParent() {
            return null;
        }

        public String getPath(int pathId) {
            return null;
        }

        public String[] getPlatformLibraries() {
            return null;
        }

        public Map<String, String> getProperties() {
            return null;
        }

        public String getProperty(String name) {
            return null;
        }

        public Integer getProperty(String name, Integer defaultValue) {
            return null;
        }

        public Boolean getProperty(String name, Boolean defaultValue) {
            return null;
        }

        public int getRevision() {
            return 0;
        }

        public String[] getSkins() {
            return null;
        }

        public int getUsbVendorId() {
            return 0;
        }

        public String getVendor() {
            return null;
        }

        public AndroidVersion getVersion() {
            return new AndroidVersion(mApiLevel, "TestOnly");
        }

        public String getVersionName() {
            return null;
        }

        public String hashString() {
            return null;
        }

        public boolean isPlatform() {
            return false;
        }

        public int compareTo(IAndroidTarget o) {
            return 0;
        }
    }
}







