/*Make theme selection take rendering target into account

If the current rendering target is less than API level 11, then don't
use the Holo theme even if the project itself has >= 11 as an SDK
target.

Change-Id:I23c8a4865fa4af3c46fbfa34f0a83b18e7ed9b63*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index c6c1d37..d304303 100644

//Synthetic comment -- @@ -1401,7 +1401,7 @@
// Look up the default/fallback theme to use for this project (which
// depends on the screen size when no particular theme is specified
// in the manifest)
                String defaultTheme = manifest.getDefaultTheme(screenSize);

Map<String, String> activityThemes = manifest.getActivityThemes();
String pkg = manifest.getPackage();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfo.java
//Synthetic comment -- index 05c4a5e..5e71822 100644

//Synthetic comment -- @@ -93,11 +93,11 @@

private final IProject mProject;
private String mPackage;
    private String mDefaultTheme;
    private String mLargeDefaultTheme;
private Map<String, String> mActivityThemes;
private IAbstractFile mManifestFile;
private long mLastModified;

/**
* Qualified name for the per-project non-persistent property storing the
//Synthetic comment -- @@ -164,10 +164,10 @@
mLastModified = fileModified;

mActivityThemes = new HashMap<String, String>();
        mDefaultTheme = PREFIX_ANDROID_STYLE + "Theme"; //$NON-NLS-1$
        mLargeDefaultTheme = mDefaultTheme;

mPackage = ""; //$NON-NLS-1$
Document document = null;
try {
DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//Synthetic comment -- @@ -227,17 +227,11 @@
}
}

                        if (apiLevel >= 11) {
                            mLargeDefaultTheme = PREFIX_ANDROID_STYLE + "Theme.Holo"; //$NON-NLS-1$
                        }
                        // When Holo works everywhere:
                        // if (apiLevel >= N) {
                        //    mDefaultTheme = PREFIX_ANDROID_STYLE + "Theme.Holo"; //$NON-NLS-1$
                        // }
}
}
} else {
                mDefaultTheme = mLargeDefaultTheme = defaultTheme;
}
} catch (SAXException e) {
AdtPlugin.log(e, "Malformed manifest");
//Synthetic comment -- @@ -269,18 +263,31 @@

/**
* Returns the default theme for this project, by looking at the manifest default
     * theme registration, target SDK, etc.
*
* @param screenSize the screen size to obtain a default theme for, or null if unknown
* @return the theme to use for this project, never null
*/
    public String getDefaultTheme(ScreenSize screenSize) {
sync();

        if (screenSize == ScreenSize.XLARGE) {
            return mLargeDefaultTheme;
} else {
            return mDefaultTheme;
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfoTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfoTest.java
//Synthetic comment -- index 590c611..66a2482 100644

//Synthetic comment -- @@ -21,6 +21,8 @@

import com.android.ide.eclipse.adt.internal.editors.layout.refactoring.AdtProjectTest;
import com.android.ide.eclipse.adt.internal.resources.ResourceHelper;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.NullProgressMonitor;
//Synthetic comment -- @@ -44,9 +46,9 @@
Map<String, String> map = info.getActivityThemes();
assertEquals(map.toString(), 0, map.size());
assertEquals("com.android.unittest", info.getPackage());
        assertEquals("Theme", ResourceHelper.styleToTheme(info.getDefaultTheme(NORMAL)));
        assertEquals("@android:style/Theme", info.getDefaultTheme(null));
        assertEquals("Theme", ResourceHelper.styleToTheme(info.getDefaultTheme(XLARGE)));
}

public void testGetActivityThemes2() throws Exception {
//Synthetic comment -- @@ -58,8 +60,9 @@
Map<String, String> map = info.getActivityThemes();
assertEquals(map.toString(), 0, map.size());
assertEquals("com.android.unittest", info.getPackage());
        assertEquals("Theme.Holo", ResourceHelper.styleToTheme(info.getDefaultTheme(XLARGE)));
        assertEquals("Theme", ResourceHelper.styleToTheme(info.getDefaultTheme(LARGE)));
}

public void testGetActivityThemes3() throws Exception {
//Synthetic comment -- @@ -71,8 +74,9 @@
Map<String, String> map = info.getActivityThemes();
assertEquals(map.toString(), 0, map.size());
assertEquals("com.android.unittest", info.getPackage());
        assertEquals("Theme.Holo", ResourceHelper.styleToTheme(info.getDefaultTheme(XLARGE)));
        assertEquals("Theme", ResourceHelper.styleToTheme(info.getDefaultTheme(NORMAL)));
}

public void testGetActivityThemes4() throws Exception {
//Synthetic comment -- @@ -97,7 +101,7 @@
""
);
assertEquals("com.android.unittest", info.getPackage());
        assertEquals("Theme", ResourceHelper.styleToTheme(info.getDefaultTheme(XLARGE)));

Map<String, String> map = info.getActivityThemes();
assertEquals(map.toString(), 1, map.size());
//Synthetic comment -- @@ -129,18 +133,41 @@
""
);

        assertEquals("@style/NoBackground", info.getDefaultTheme(XLARGE));
        assertEquals("@style/NoBackground", info.getDefaultTheme(NORMAL));
        assertEquals("NoBackground", ResourceHelper.styleToTheme(info.getDefaultTheme(NORMAL)));

Map<String, String> map = info.getActivityThemes();
assertEquals(map.toString(), 1, map.size());
assertNull(map.get("com.android.unittest.prefs.PrefsActivity"));
assertEquals("@android:style/Theme.Dialog",
map.get("com.android.unittest.app.IntroActivity"));

}

private ManifestInfo getManifestInfo(String manifestContents) throws Exception {
InputStream bstream = new ByteArrayInputStream(
manifestContents.getBytes("UTF-8")); //$NON-NLS-1$
//Synthetic comment -- @@ -153,4 +180,116 @@
}
return ManifestInfo.get(getProject());
}
}







