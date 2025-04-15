/*SDK Manager: Remember last seen source names.

One issue in the SDK Manager is that sources that
are partially installed first are loaded first.
However we used to remember the source URL but not
its name, resulting in some sources having no visible
name when loaded from installed packages.

This solves the issue by storing the known source names
locally. This expands on the local settings file that
stores whether a source is enable/disable to add more
attributes.

Change-Id:Ideca00820ccad01b4cacd997b1e43b53270fa1bc*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java
//Synthetic comment -- index 9a04226..38417f7 100755

//Synthetic comment -- @@ -19,8 +19,6 @@
import com.android.annotations.Nullable;
import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.internal.repository.UrlOpener.CanceledByUserException;
import com.android.sdklib.repository.RepoConstants;
import com.android.sdklib.repository.SdkAddonConstants;
//Synthetic comment -- @@ -35,10 +33,7 @@
import org.xml.sax.SAXParseException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
//Synthetic comment -- @@ -46,7 +41,6 @@
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -74,8 +68,7 @@
private String mFetchError;
private final String mUiName;

    private static final Properties sDisabledSourceUrls = new Properties();
    private static final String     SRC_FILENAME = "sites-settings.cfg"; //$NON-NLS-1$

/**
* Constructs a new source for the given repository URL.
//Synthetic comment -- @@ -102,6 +95,12 @@
}
}

mUrl = url;
mUiName = uiName;
setDefaultDescription();
//Synthetic comment -- @@ -221,103 +220,31 @@
* Indicates if the source is enabled.
* <p/>
* A 3rd-party add-on source can be disabled by the user to prevent from loading it.
     * This loads the persistent state from a settings file when first called.
*
* @return True if the source is enabled (default is true).
*/
public boolean isEnabled() {
        synchronized (sDisabledSourceUrls) {
            if (sDisabledSourceUrls.isEmpty()) {
                // Load state from persistent file

                FileInputStream fis = null;
                try {
                    String folder = AndroidLocation.getFolder();
                    File f = new File(folder, SRC_FILENAME);
                    if (f.exists()) {
                        fis = new FileInputStream(f);

                        sDisabledSourceUrls.load(fis);
                    }
                } catch (IOException ignore) {
                    // nop
                } catch (AndroidLocationException ignore) {
                    // nop
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException ignore) {}
                    }
                }

                if (sDisabledSourceUrls.isEmpty()) {
                    // Nothing was loaded. Initialize the storage with a version
                    // identified. This isn't currently checked back, but we might
                    // want it later if we decide to change the way this works.
                    // The version key is choosen on purpose to not match any valid URL.
                    sDisabledSourceUrls.setProperty("@version", "1"); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }

            // A URL is enabled if it's not in the disabled list.
            return sDisabledSourceUrls.getProperty(mUrl) == null;
        }
}

/**
* Changes whether the source is marked as enabled.
* <p/>
     * When <em>changing> the enable state, the current package list is purged
* and the next {@code load} will either return an empty list (if disabled) or
* the actual package list (if enabled.)
     * <p/>
     * This also persistent the change by updating a settings file.
*
* @param enabled True for the source to be enabled (can be loaded), false otherwise.
*/
public void setEnabled(boolean enabled) {
        // Comparing using isEnabled() has the voluntary side-effect of also
        // loading the map from the persistent file the first time.
if (enabled != isEnabled()) {
// First we clear the current package list, which will force the
// next load() to actually set the package list as desired.
clearPackages();

            synchronized (sDisabledSourceUrls) {
                // Change the map
                if (enabled) {
                    sDisabledSourceUrls.remove(mUrl);
                } else {
                    // The "disabled" value is not being checked when reloading the map.
                    // We might want to do something with it later if a URL can have
                    // more attributes than just disabled.
                    sDisabledSourceUrls.setProperty(mUrl, "disabled"); //$NON-NLS-1$
                }

                // Persist it to the file
                FileOutputStream fos = null;
                try {
                    String folder = AndroidLocation.getFolder();
                    File f = new File(folder, SRC_FILENAME);

                    fos = new FileOutputStream(f);

                    sDisabledSourceUrls.store(fos,
                            "## Disabled Sources for Android SDK Manager");  //$NON-NLS-1$

                } catch (AndroidLocationException ignore) {
                    // nop
                } catch (IOException ignore) {
                    // nop
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException ignore) {}
                    }
                }
            }
}
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSourceProperties.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSourceProperties.java
new file mode 100755
//Synthetic comment -- index 0000000..e9da67f

//Synthetic comment -- @@ -0,0 +1,204 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AddonSitesDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AddonSitesDialog.java
//Synthetic comment -- index e02d20e..015539c 100755

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.sdklib.internal.repository.SdkAddonSource;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdklib.internal.repository.SdkSourceCategory;
import com.android.sdklib.internal.repository.SdkSources;
import com.android.sdkuilib.internal.repository.UpdaterBaseDialog;
import com.android.sdkuilib.internal.repository.UpdaterData;
//Synthetic comment -- @@ -290,6 +291,8 @@
if (mSources != null && mSourcesChangeListener != null) {
mSources.removeChangeListener(mSourcesChangeListener);
}
super.close();
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PkgContentProvider.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PkgContentProvider.java
//Synthetic comment -- index bd3bd0e..4867ebb 100755

//Synthetic comment -- @@ -213,7 +213,12 @@

@Override
public String getLongDescription() {
            return mSource.getLongDescription();
}

@Override
//Synthetic comment -- @@ -221,7 +226,7 @@
if (mSource.isEnabled()) {
return "No packages found.";
} else {
                return "This site is disabled.";
}
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/SdkUpdaterWindowImpl2.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/SdkUpdaterWindowImpl2.java
//Synthetic comment -- index e5f26cd..2f77e45 100755

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdkuilib.internal.repository.AboutDialog;
import com.android.sdkuilib.internal.repository.ISdkUpdaterWindow;
import com.android.sdkuilib.internal.repository.MenuBarWrapper;
//Synthetic comment -- @@ -149,6 +150,9 @@
}
}

dispose();  //$hide$
}








