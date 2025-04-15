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
import com.android.sdklib.internal.repository.UrlOpener.CanceledByUserException;
import com.android.sdklib.repository.RepoConstants;
import com.android.sdklib.repository.SdkAddonConstants;
//Synthetic comment -- @@ -35,10 +33,7 @@
import org.xml.sax.SAXParseException;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
//Synthetic comment -- @@ -46,7 +41,6 @@
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -74,8 +68,7 @@
private String mFetchError;
private final String mUiName;

    private static final SdkSourceProperties sSourcesProps = new SdkSourceProperties();

/**
* Constructs a new source for the given repository URL.
//Synthetic comment -- @@ -102,6 +95,12 @@
}
}

        if (uiName == null) {
            uiName = sSourcesProps.getProperty(SdkSourceProperties.KEY_NAME, url, null);
        } else {
            sSourcesProps.setProperty(SdkSourceProperties.KEY_NAME, url, uiName);
        }

mUrl = url;
mUiName = uiName;
setDefaultDescription();
//Synthetic comment -- @@ -221,103 +220,31 @@
* Indicates if the source is enabled.
* <p/>
* A 3rd-party add-on source can be disabled by the user to prevent from loading it.
*
* @return True if the source is enabled (default is true).
*/
public boolean isEnabled() {
        // A URL is enabled if it's not in the disabled list.
        return sSourcesProps.getProperty(SdkSourceProperties.KEY_DISABLED, mUrl, null) == null;
}

/**
* Changes whether the source is marked as enabled.
* <p/>
     * When <em>changing</em> the enable state, the current package list is purged
* and the next {@code load} will either return an empty list (if disabled) or
* the actual package list (if enabled.)
*
* @param enabled True for the source to be enabled (can be loaded), false otherwise.
*/
public void setEnabled(boolean enabled) {
if (enabled != isEnabled()) {
// First we clear the current package list, which will force the
// next load() to actually set the package list as desired.
clearPackages();

            sSourcesProps.setProperty(SdkSourceProperties.KEY_DISABLED, mUrl,
                    enabled ? null /*remove*/ : "disabled"); //$NON-NLS-1$
}
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSourceProperties.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSourceProperties.java
new file mode 100755
//Synthetic comment -- index 0000000..e9da67f

//Synthetic comment -- @@ -0,0 +1,204 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.sdklib.internal.repository;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * Properties for individual sources which are persisted by a local settings file.
 * <p/>
 * All instances of {@link SdkSourceProperties} share the same singleton storage.
 * The persisted setting file is loaded as necessary, however callers must persist
 * it at some point by calling {@link #save()}.
 */
public class SdkSourceProperties {

    /**
     * An internal file version number, in case we want to change the format later.
     */
    private static final String KEY_VERSION  = "@version@";                 //$NON-NLS-1$
    /**
     * The last known UI name of the source.
     */
    public static final String KEY_NAME     = "@name@";                     //$NON-NLS-1$
    /**
     * A non-null string if the source is disabled. Null if the source is enabled.
     */
    public static final String KEY_DISABLED = "@disabled@";                 //$NON-NLS-1$

    private static final Properties sSourcesProperties = new Properties();
    private static final String     SRC_FILENAME = "sites-settings.cfg";    //$NON-NLS-1$

    private static boolean sModified = false;

    public SdkSourceProperties() {
    }

    public void save() {
        synchronized (sSourcesProperties) {
            if (sModified && !sSourcesProperties.isEmpty()) {
                saveLocked();
                sModified = false;
            }
        }
    }

    /**
     * Retrieves a property for the given source URL and the given key type.
     * <p/>
     * Implementation detail: this loads the persistent settings file as needed.
     *
     * @param key The kind of property to retrieve for that source URL.
     * @param sourceUrl The source URL.
     * @param defaultValue The default value to return, if the property isn't found. Can be null.
     * @return The non-null string property for the key/sourceUrl or the default value.
     */
    @Nullable
    public String getProperty(@NonNull String key,
                              @NonNull String sourceUrl,
                              @Nullable String defaultValue) {
        String value = defaultValue;

        synchronized (sSourcesProperties) {
            if (sSourcesProperties.isEmpty()) {
                loadLocked();
            }

            value = sSourcesProperties.getProperty(key + sourceUrl, defaultValue);
        }

        return value;
    }

    /**
     * Sets or remove a property for the given source URL and the given key type.
     * <p/>
     * Implementation detail: this does <em>not</em> save the persistent settings file.
     * Somehow the caller will need to call the {@link #save()} method later.
     *
     * @param key The kind of property to retrieve for that source URL.
     * @param sourceUrl The source URL.
     * @param value The new value to set (if non null) or null to remove an existing property.
     */
    public void setProperty(String key, String sourceUrl, String value) {
        synchronized (sSourcesProperties) {
            if (sSourcesProperties.isEmpty()) {
                loadLocked();
            }

            key += sourceUrl;

            String old = sSourcesProperties.getProperty(key);
            if (value == null) {
                if (old != null) {
                    sSourcesProperties.remove(key);
                    sModified = true;
                }
            } else if (old == null || !old.equals(value)) {
                sSourcesProperties.setProperty(key, value);
                sModified = true;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("<SdkSourceProperties ");      //$NON-NLS-1$
        synchronized (sSourcesProperties) {
            for (Map.Entry<Object, Object> entry : sSourcesProperties.entrySet()) {
                sb.append('\n').append(entry.getKey())
                  .append(" = ").append(entry.getValue());                  //$NON-NLS-1$
            }
        }
        sb.append("\n>");                                                   //$NON-NLS-1$
        return sb.toString();
    }

    private void loadLocked() {
        // Load state from persistent file
        FileInputStream fis = null;
        try {
            String folder = AndroidLocation.getFolder();
            File f = new File(folder, SRC_FILENAME);
            if (f.exists()) {
                fis = new FileInputStream(f);

                sSourcesProperties.load(fis);

                // If it lacks our magic version key, don't use it
                if (sSourcesProperties.getProperty(KEY_VERSION) == null) {
                    sSourcesProperties.clear();
                }

                sModified = false;
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

        if (sSourcesProperties.isEmpty()) {
            // Nothing was loaded. Initialize the storage with a version
            // identified. This isn't currently checked back, but we might
            // want it later if we decide to change the way this works.
            // The version key is choosen on purpose to not match any valid URL.
            sSourcesProperties.setProperty(KEY_VERSION, "1"); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    private void saveLocked() {
        // Persist it to the file
        FileOutputStream fos = null;
        try {
            String folder = AndroidLocation.getFolder();
            File f = new File(folder, SRC_FILENAME);

            fos = new FileOutputStream(f);

            sSourcesProperties.store(fos,"## Sites Settings for Android SDK Manager");//$NON-NLS-1$

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








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AddonSitesDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AddonSitesDialog.java
//Synthetic comment -- index e02d20e..015539c 100755

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.sdklib.internal.repository.SdkAddonSource;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdklib.internal.repository.SdkSourceCategory;
import com.android.sdklib.internal.repository.SdkSourceProperties;
import com.android.sdklib.internal.repository.SdkSources;
import com.android.sdkuilib.internal.repository.UpdaterBaseDialog;
import com.android.sdkuilib.internal.repository.UpdaterData;
//Synthetic comment -- @@ -290,6 +291,8 @@
if (mSources != null && mSourcesChangeListener != null) {
mSources.removeChangeListener(mSourcesChangeListener);
}
        SdkSourceProperties p = new SdkSourceProperties();
        p.save();
super.close();
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PkgContentProvider.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PkgContentProvider.java
//Synthetic comment -- index bd3bd0e..4867ebb 100755

//Synthetic comment -- @@ -213,7 +213,12 @@

@Override
public String getLongDescription() {
            if (mSource.isEnabled()) {
                return mSource.getLongDescription();
            } else {
                return "Loading from this site has been disabled. " +
                       "To enable it, use Tools > Manage Add-ons Sites.";
            }
}

@Override
//Synthetic comment -- @@ -221,7 +226,7 @@
if (mSource.isEnabled()) {
return "No packages found.";
} else {
                return "This site is disabled. ";
}
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/SdkUpdaterWindowImpl2.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/SdkUpdaterWindowImpl2.java
//Synthetic comment -- index e5f26cd..2f77e45 100755

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdklib.internal.repository.SdkSourceProperties;
import com.android.sdkuilib.internal.repository.AboutDialog;
import com.android.sdkuilib.internal.repository.ISdkUpdaterWindow;
import com.android.sdkuilib.internal.repository.MenuBarWrapper;
//Synthetic comment -- @@ -149,6 +150,9 @@
}
}

        SdkSourceProperties p = new SdkSourceProperties();
        p.save();

dispose();  //$hide$
}








