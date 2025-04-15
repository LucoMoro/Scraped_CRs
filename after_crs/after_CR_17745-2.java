/*SDK Manager: manage URL sites grouped in categories.

Sources are now grouped in categories:
- android official site
- all 3rd party addons which list is remotely fetched
- local overrides (from getenv SDK_UPDATER_(USER_)URLS)
- user-added add-on URLs

SdkSources (plural Sources) is the collection of all URL
sites (each one being an SdkSource that contains a collection
of packages).
SdkSourceCategory is the category of a given SdkSource.

Each source has its download URL but also an UI-visible name
which is displayed if known -- this is used for our default
android source and for 3rd party remote addons.
For all source overrides (e.g. user added or from the getenv
vars) we'll simply display the URL in the tree.

Change-Id:Idc3e9ba3395818e33fc7505ead7b0bd0be867ea3*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java
//Synthetic comment -- index 1bcac3f..48c3e79 100755

//Synthetic comment -- @@ -49,7 +49,6 @@
public static final String PROP_RELEASE_NOTE = "Pkg.RelNote";      //$NON-NLS-1$
public static final String PROP_RELEASE_URL  = "Pkg.RelNoteUrl";   //$NON-NLS-1$
public static final String PROP_SOURCE_URL   = "Pkg.SourceUrl";    //$NON-NLS-1$
public static final String PROP_OBSOLETE     = "Pkg.Obsolete";     //$NON-NLS-1$

private final int mRevision;
//Synthetic comment -- @@ -142,12 +141,10 @@
// a package comes from.
String srcUrl = getProperty(props, PROP_SOURCE_URL, null);
if (props != null && source == null && srcUrl != null) {
            if (this instanceof AddonPackage) {
                source = new SdkAddonSource(srcUrl, null /*uiName*/);
} else {
                source = new SdkRepoSource(srcUrl, null /*uiName*/);
}
}
mSource = source;
//Synthetic comment -- @@ -200,7 +197,6 @@

if (mSource != null) {
props.setProperty(PROP_SOURCE_URL,  mSource.getUrl());
}
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkAddonSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkAddonSource.java
//Synthetic comment -- index ab24587..78dfd69 100755

//Synthetic comment -- @@ -34,10 +34,10 @@
* Constructs a new source for the given repository URL.
* @param url The source URL. Cannot be null. If the URL ends with a /, the default
*            repository.xml filename will be appended automatically.
     * @param uiName The UI-visible name of the source. Can be null.
*/
    public SdkAddonSource(String url, String uiName) {
        super(url, uiName);
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkRepoSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkRepoSource.java
//Synthetic comment -- index 52e2c0a..85838aa 100755

//Synthetic comment -- @@ -47,9 +47,10 @@
* Constructs a new source for the given repository URL.
* @param url The source URL. Cannot be null. If the URL ends with a /, the default
*            repository.xml filename will be appended automatically.
     * @param uiName The UI-visible name of the source. Can be null.
*/
    public SdkRepoSource(String url, String uiName) {
        super(url, uiName);
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java
//Synthetic comment -- index 0ef4a32..b3b3bb5 100755

//Synthetic comment -- @@ -59,19 +59,19 @@
public abstract class SdkSource implements IDescription {

private String mUrl;

private Package[] mPackages;
private String mDescription;
private String mFetchError;
    private final String mUiName;

/**
* Constructs a new source for the given repository URL.
* @param url The source URL. Cannot be null. If the URL ends with a /, the default
*            repository.xml filename will be appended automatically.
     * @param uiName The UI-visible name of the source. Can be null.
*/
    public SdkSource(String url, String uiName) {

// if the URL ends with a /, it must be "directory" resource,
// in which case we automatically add the default file that will
//Synthetic comment -- @@ -82,7 +82,7 @@
}

mUrl = url;
        mUiName = uiName;
setDefaultDescription();
}

//Synthetic comment -- @@ -127,25 +127,27 @@
throws IOException;

/**
     * Two repo source are equal if they have the same URL.
*/
@Override
public boolean equals(Object obj) {
if (obj instanceof SdkSource) {
SdkSource rs = (SdkSource) obj;
            return  rs.getUrl().equals(this.getUrl());
}
return false;
}

@Override
public int hashCode() {
        return mUrl.hashCode();
}

    /**
     * Returns the UI-visible name of the source. Can be null.
     */
    public String getUiName() {
        return mUiName;
}

/** Returns the URL of the XML file for this source. */
//Synthetic comment -- @@ -170,6 +172,9 @@
}

public String getShortDescription() {
        if (mUiName != null && mUiName.length() > 0) {
            return mUiName;
        }
return mUrl;
}

//Synthetic comment -- @@ -390,7 +395,7 @@
}

private void setDefaultDescription() {
        if (isAddonSource()) {
mDescription = String.format("Add-on Source: %1$s", mUrl);
} else {
mDescription = String.format("SDK Source: %1$s", mUrl);
//Synthetic comment -- @@ -663,7 +668,7 @@
} else if (RepoConstants.NODE_EXTRA.equals(name)) {
p = new ExtraPackage(this, child, nsUri, licenses);

                        } else if (!isAddonSource()) {
// We only load platform, doc and tool packages from internal
// sources, never from user sources.
if (SdkRepoConstants.NODE_PLATFORM.equals(name)) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSourceCategory.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSourceCategory.java
new file mode 100755
//Synthetic comment -- index 0000000..3afa086

//Synthetic comment -- @@ -0,0 +1,85 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
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


/**
 * The category of a given {@link SdkSource} (which represents a download site).
 */
public enum SdkSourceCategory implements IDescription {

    /**
     * The default canonical and official Android repository.
     */
    ANDROID_REPO("Android Repository", true),

    /**
     * Repositories contributed by the SDK_UPDATER_URLS env var,
     * only used for local debugging.
     */
    GETENV_REPOS("Custom Repositories", false),

    /**
     * All third-party add-ons fetched from the Android repository.
     */
    ADDONS_3RD_PARTY("Third party Add-ons", true),

    /**
     * All add-ons contributed locally by the user via the "Add Add-on Site" button.
     */
    USER_ADDONS("User Add-ons", false),

    /**
     * Add-ons contributed by the SDK_UPDATER_USER_URLS env var,
     * only used for local debugging.
     */
    GETENV_ADDONS("Custom Add-ons", false);


    private final String mUiName;
    private final boolean mAlwaysDisplay;

    private SdkSourceCategory(String uiName, boolean alwaysDisplay) {
        mUiName = uiName;
        mAlwaysDisplay = alwaysDisplay;
    }

    /**
     * Returns the UI-visible name of the cateogry. Displayed in the available package tree.
     * Cannot be null nor empty.
     */
    public String getUiName() {
        return mUiName;
    }

    /**
     * True if this category must always be displayed by the available package tree, even
     * if empty.
     * When false, the category must not be displayed when empty.
     */
    public boolean getAlwaysDisplay() {
        return mAlwaysDisplay;
    }

    public String getLongDescription() {
        return getUiName();
    }

    public String getShortDescription() {
        return getUiName();
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSources.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSources.java
//Synthetic comment -- index 2bb22f4..c2ad057 100755

//Synthetic comment -- @@ -25,11 +25,13 @@
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;

/**
 * A list of sdk-repository and sdk-addon sources, sorted by {@link SdkSourceCategory}.
*/
public class SdkSources {

//Synthetic comment -- @@ -39,7 +41,8 @@

private static final String SRC_FILENAME = "repositories.cfg"; //$NON-NLS-1$

    private final EnumMap<SdkSourceCategory, ArrayList<SdkSource>> mSources =
        new EnumMap<SdkSourceCategory, ArrayList<SdkSource>>(SdkSourceCategory.class);

public SdkSources() {
}
//Synthetic comment -- @@ -47,37 +50,171 @@
/**
* Adds a new source to the Sources list.
*/
    public void add(SdkSourceCategory category, SdkSource source) {

        ArrayList<SdkSource> list = mSources.get(category);
        if (list == null) {
            list = new ArrayList<SdkSource>();
            mSources.put(category, list);
        }

        list.add(source);
}

/**
* Removes a source from the Sources list.
*/
public void remove(SdkSource source) {
        Iterator<Entry<SdkSourceCategory, ArrayList<SdkSource>>> it =
            mSources.entrySet().iterator();
        while (it.hasNext()) {
            Entry<SdkSourceCategory, ArrayList<SdkSource>> entry = it.next();
            ArrayList<SdkSource> list = entry.getValue();

            if (list.remove(source)) {
                if (list.isEmpty()) {
                    // remove the entry since the source list became empty
                    it.remove();
                }
            }
        }
}

/**
     * Removes all the sources in the given category.
*/
    public void removeAll(SdkSourceCategory category) {
        mSources.remove(category);
    }

    /**
     * Returns a set of all categories that must be displayed. This includes all
     * categories that are to be always displayed as well as all categories which
     * have at least one source.
     * Might return a empty array, but never returns null.
     */
    public SdkSourceCategory[] getCategories() {
        ArrayList<SdkSourceCategory> cats = new ArrayList<SdkSourceCategory>();

        for (SdkSourceCategory cat : SdkSourceCategory.values()) {
            if (cat.getAlwaysDisplay()) {
                cats.add(cat);
            } else {
                ArrayList<SdkSource> list = mSources.get(cat);
                if (list != null && !list.isEmpty()) {
                    cats.add(cat);
                }
            }
        }

        return cats.toArray(new SdkSourceCategory[cats.size()]);
    }

    /**
     * Returns an array of sources attached to the given category.
     * Might return an empty array, but never returns null.
     */
    public SdkSource[] getSources(SdkSourceCategory category) {
        ArrayList<SdkSource> list = mSources.get(category);
        if (list == null) {
            return new SdkSource[0];
        } else {
            return list.toArray(new SdkSource[list.size()]);
        }
    }

    /**
     * Returns an array of the sources across all categories. This is never null.
     */
    public SdkSource[] getAllSources() {
        int n = 0;

        for (ArrayList<SdkSource> list : mSources.values()) {
            n += list.size();
        }

        SdkSource[] sources = new SdkSource[n];

        int i = 0;
        for (ArrayList<SdkSource> list : mSources.values()) {
            for (SdkSource source : list) {
                sources[i++] = source;
            }
        }

        return sources;
    }

    /**
     * Returns the category of a given source, or null if the source is unknown.
     * <p/>
     * Note that this method uses object identity to find a given source, and does
     * not identify sources by their URL like {@link #hasSourceUrl(SdkSource)} does.
     * <p/>
     * The search is O(N), which should be acceptable on the expectedly small source list.
     */
    public SdkSourceCategory getCategory(SdkSource source) {
        if (source != null) {
            for (Entry<SdkSourceCategory, ArrayList<SdkSource>> entry : mSources.entrySet()) {
                if (entry.getValue().contains(source)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    /**
     * Returns true if there's already a similar source in the sources list
     * under any category.
     * <p/>
     * Important: The match is NOT done on object identity.
     * Instead, this searches for a <em>similar</em> source, based on
     * {@link SdkSource#equals(Object)} which compares the source URLs.
     * <p/>
     * The search is O(N), which should be acceptable on the expectedly small source list.
     */
    public boolean hasSourceUrl(SdkSource source) {
        for (ArrayList<SdkSource> list : mSources.values()) {
            for (SdkSource s : list) {
                if (s.equals(source)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if there's already a similar source in the sources list
     * under the specified category.
     * <p/>
     * Important: The match is NOT done on object identity.
     * Instead, this searches for a <em>similar</em> source, based on
     * {@link SdkSource#equals(Object)} which compares the source URLs.
     * <p/>
     * The search is O(N), which should be acceptable on the expectedly small source list.
     */
    public boolean hasSourceUrl(SdkSourceCategory category, SdkSource source) {
        ArrayList<SdkSource> list = mSources.get(category);
        if (list != null) {
            for (SdkSource s : list) {
                if (s.equals(source)) {
                    return true;
                }
            }
        }
        return false;
}

/**
* Loads all user sources. This <em>replaces</em> all existing user sources
* by the ones from the property file.
*/
    public void loadUserAddons(ISdkLog log) {

// Remove all existing user sources
        removeAll(SdkSourceCategory.USER_ADDONS);

// Load new user sources from property file
FileInputStream fis = null;
//Synthetic comment -- @@ -95,9 +232,9 @@
for (int i = 0; i < count; i++) {
String url = props.getProperty(String.format("%s%02d", KEY_SRC, i));  //$NON-NLS-1$
if (url != null) {
                        SdkSource s = new SdkAddonSource(url, null/*uiName*/);
                        if (!hasSourceUrl(s)) {
                            add(SdkSourceCategory.USER_ADDONS, s);
}
}
}
//Synthetic comment -- @@ -123,24 +260,10 @@
}

/**
* Saves all the user sources.
* @param log Logger. Cannot be null.
*/
    public void saveUserAddons(ISdkLog log) {
FileOutputStream fos = null;
try {
String folder = AndroidLocation.getFolder();
//Synthetic comment -- @@ -151,11 +274,9 @@
Properties props = new Properties();

int count = 0;
            for (SdkSource s : getSources(SdkSourceCategory.USER_ADDONS)) {
                count++;
                props.setProperty(String.format("%s%02d", KEY_SRC, count), s.getUrl());  //$NON-NLS-1$
}
props.setProperty(KEY_COUNT, Integer.toString(count));









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/SdkAddonSourceTest.java b/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/SdkAddonSourceTest.java
//Synthetic comment -- index 0d8c90f..5b2a3ab 100755

//Synthetic comment -- @@ -38,7 +38,7 @@
*/
private static class MockSdkAddonSource extends SdkAddonSource {
public MockSdkAddonSource() {
            super("fake-url", null /*uiName*/);
}

public Document _findAlternateToolsXml(InputStream xml) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/SdkRepoSourceTest.java b/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/SdkRepoSourceTest.java
//Synthetic comment -- index f51cc79..2bbbd8c 100755

//Synthetic comment -- @@ -38,7 +38,7 @@
*/
private static class MockSdkRepoSource extends SdkRepoSource {
public MockSdkRepoSource() {
            super("fake-url", null /*uiName*/);
}

public Document _findAlternateToolsXml(InputStream xml) throws IOException {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RemotePackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RemotePackagesPage.java
//Synthetic comment -- index 099826a..d1f8a09 100755

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.sdklib.internal.repository.Package;
import com.android.sdklib.internal.repository.SdkAddonSource;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdklib.internal.repository.SdkSourceCategory;
import com.android.sdkuilib.repository.UpdaterWindow.ISdkListener;

import org.eclipse.jface.dialogs.IInputValidator;
//Synthetic comment -- @@ -189,6 +190,14 @@
// Disable the check that prevents subclassing of SWT components
}

    public void onSdkChange(boolean init) {
        RepoSourcesAdapter sources = mUpdaterData.getSourcesAdapter();
        mTreeViewerSources.setContentProvider(sources.getContentProvider());
        mTreeViewerSources.setLabelProvider(  sources.getLabelProvider());
        mTreeViewerSources.setInput(sources);
        onTreeSelected();
    }

// -- Start of internal part ----------
// Hide everything down-below from SWT designer
//$hide>>$
//Synthetic comment -- @@ -267,12 +276,17 @@
ITreeContentProvider provider =
(ITreeContentProvider) mTreeViewerSources.getContentProvider();

        // When selecting, we want to only select compatible archives
        // and expand the super nodes.
        expandItem(elem, provider);
    }

    private void expandItem(Object elem, ITreeContentProvider provider) {
        if (elem instanceof SdkSource || elem instanceof SdkSourceCategory) {
mTreeViewerSources.setExpandedState(elem, true);
for (Object pkg : provider.getChildren(elem)) {
mTreeViewerSources.setChecked(pkg, true);
                expandItem(pkg, provider);
}
} else if (elem instanceof Package) {
selectCompatibleArchives(elem, provider);
//Synthetic comment -- @@ -336,7 +350,7 @@

private void onAddSiteSelected() {

        final SdkSource[] knowSources = mUpdaterData.getSources().getAllSources();
String title = "Add Add-on Site URL";

String msg =
//Synthetic comment -- @@ -378,7 +392,9 @@

if (dlg.open() == Window.OK) {
String url = dlg.getValue();
            mUpdaterData.getSources().add(
                    SdkSourceCategory.USER_ADDONS,
                    new SdkAddonSource(url, null/*uiName*/));
onRefreshSelected();
}
}
//Synthetic comment -- @@ -389,17 +405,20 @@
ISelection sel = mTreeViewerSources.getSelection();
if (mUpdaterData != null && sel instanceof ITreeSelection) {
for (Object c : ((ITreeSelection) sel).toList()) {
                if (c instanceof SdkSource) {
SdkSource source = (SdkSource) c;

                    if (mUpdaterData.getSources().hasSourceUrl(
                            SdkSourceCategory.USER_ADDONS, source)) {
                        String title = "Delete Add-on Site?";

                        String msg = String.format("Are you sure you want to delete the add-on site '%1$s'?",
                                source.getUrl());

                        if (MessageDialog.openQuestion(getShell(), title, msg)) {
                            mUpdaterData.getSources().remove(source);
                            changed = true;
                        }
}
}
}
//Synthetic comment -- @@ -418,14 +437,6 @@
updateButtonsState();
}

private void updateButtonsState() {
// We install archives, so there should be at least one checked archive.
// Having sites or packages checked does not count.
//Synthetic comment -- @@ -446,7 +457,8 @@
if (sel instanceof ITreeSelection) {
for (Object c : ((ITreeSelection) sel).toList()) {
if (c instanceof SdkSource &&
                        mUpdaterData.getSources().hasSourceUrl(
                                SdkSourceCategory.USER_ADDONS, (SdkSource) c)) {
hasSelectedUserSource = true;
break;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RepoSourcesAdapter.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RepoSourcesAdapter.java
//Synthetic comment -- index 3a44bb4..05d0d43 100755

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.Package;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdklib.internal.repository.SdkSourceCategory;
import com.android.sdklib.internal.repository.Package.UpdateInfo;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;

//Synthetic comment -- @@ -166,7 +167,28 @@
*/
public Object[] getChildren(Object parentElement) {
if (parentElement == RepoSourcesAdapter.this) {
                return mUpdaterData.getSources().getCategories();

            } else if (parentElement instanceof SdkSourceCategory) {
                SdkSourceCategory cat = (SdkSourceCategory) parentElement;
                if (cat == SdkSourceCategory.ADDONS_3RD_PARTY) {
                    mUpdaterData.loadRemoteAddonsList();
                }

                SdkSource[] sources = mUpdaterData.getSources().getSources(cat);

                if (sources.length == 1 && sources[0] != null) {
                    // If a source has a single element and this element has the same
                    // uiName as the category, collapse both.
                    // Basically this is a kludge so that we don't end up with
                    //  Android Repository > Android Repository
                    // at the top level.
                    if (cat.getUiName().equals(sources[0].getUiName())) {
                        return getRepoSourceChildren(sources[0]);
                    }
                }

                return sources;

} else if (parentElement instanceof SdkSource) {
return getRepoSourceChildren((SdkSource) parentElement);
//Synthetic comment -- @@ -254,9 +276,12 @@
*/
public Object getParent(Object element) {

            if (element instanceof SdkSourceCategory) {
return RepoSourcesAdapter.this;

            } else if (element instanceof SdkSource) {
                return mUpdaterData.getSources().getCategory((SdkSource) element);

} else if (element instanceof Package) {
return ((Package) element).getParentSource();

//Synthetic comment -- @@ -269,11 +294,14 @@
/**
* Returns true if a given element has children, which is used to display a
* "+/expand" box next to the tree node.
         * All non-terminal elements are expandable, whether they actually have any children
         * or not. This is necessary on windows in order for the tree to display the "triangle"
         * icon so that users can actually expand it, which will fill the node at runtime.
*/
public boolean hasChildren(Object element) {
            return element instanceof SdkSourceCategory ||
                   element instanceof SdkSource ||
                   element instanceof Package;
}
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index 25ed30f..8c77e3a 100755

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.repository.AddonPackage;
import com.android.sdklib.internal.repository.AddonsListFetcher;
import com.android.sdklib.internal.repository.Archive;
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskFactory;
//Synthetic comment -- @@ -31,8 +32,11 @@
import com.android.sdklib.internal.repository.SdkAddonSource;
import com.android.sdklib.internal.repository.SdkRepoSource;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdklib.internal.repository.SdkSourceCategory;
import com.android.sdklib.internal.repository.SdkSources;
import com.android.sdklib.internal.repository.ToolPackage;
import com.android.sdklib.internal.repository.AddonsListFetcher.Site;
import com.android.sdklib.repository.SdkAddonsListConstants;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.repository.UpdaterWindow.ISdkListener;
//Synthetic comment -- @@ -78,6 +82,14 @@
private AndroidLocationException mAvdManagerInitError;

/**
     * 0 = need to fetch remote addons list once..
     * 1 = fetch succeeded, don't need to do it any more.
     * -1= fetch failed, do it again only if the user requests a refresh
     *     or changes the force-http setting.
     */
    private int mStateFetchRemoteAddonsList;

    /**
* Creates a new updater data.
*
* @param sdkLog Logger. Cannot be null.
//Synthetic comment -- @@ -276,16 +288,22 @@
/**
* Sets up the default sources: <br/>
* - the default google SDK repository, <br/>
* - the user sources from prefs <br/>
     * - the extra repo URLs from the environment, <br/>
* - and finally the extra user repo URLs from the environment.
     * <p/>
     * Note that the "remote add-ons" list is not loaded from here. Instead
     * it is fetched the first time the {@link RemotePackagesPage} is displayed.
*/
public void setupDefaultSources() {
SdkSources sources = getSources();

        sources.add(SdkSourceCategory.ANDROID_REPO,
                new SdkRepoSource(SdkRepoConstants.URL_GOOGLE_SDK_SITE,
                                  SdkSourceCategory.ANDROID_REPO.getUiName()));

        // Load user sources
        sources.loadUserAddons(getSdkLog());

// SDK_UPDATER_URLS is a semicolon-separated list of URLs that can be used to
// seed the SDK Updater list for full repositories.
//Synthetic comment -- @@ -294,21 +312,14 @@
String[] urls = str.split(";");
for (String url : urls) {
if (url != null && url.length() > 0) {
                    SdkSource s = new SdkRepoSource(url, null/*uiName*/);
                    if (!sources.hasSourceUrl(s)) {
                        sources.add(SdkSourceCategory.GETENV_REPOS, s);
}
}
}
}

// SDK_UPDATER_USER_URLS is a semicolon-separated list of URLs that can be used to
// seed the SDK Updater list for user-only repositories. User sources can only provide
// add-ons and extra packages.
//Synthetic comment -- @@ -317,9 +328,9 @@
String[] urls = str.split(";");
for (String url : urls) {
if (url != null && url.length() > 0) {
                    SdkSource s = new SdkAddonSource(url, null/*uiName*/);
                    if (!sources.hasSourceUrl(s)) {
                        sources.add(SdkSourceCategory.GETENV_ADDONS, s);
}
}
}
//Synthetic comment -- @@ -783,8 +794,13 @@

mTaskFactory.start("Refresh Sources", new ITask() {
public void run(ITaskMonitor monitor) {

                if (mStateFetchRemoteAddonsList <= 0) {
                    loadRemoteAddonsListInTask(monitor);
                }

                SdkSource[] sources = mSources.getAllSources();
                monitor.setProgressMax(monitor.getProgress() + sources.length);
for (SdkSource source : sources) {
if (forceFetching ||
source.getPackages() != null ||
//Synthetic comment -- @@ -796,4 +812,51 @@
}
});
}

    /**
     * Loads the remote add-ons list.
     */
    public void loadRemoteAddonsList() {

        if (mStateFetchRemoteAddonsList != 0) {
            return;
        }

        mTaskFactory.start("Load Add-ons List", new ITask() {
            public void run(ITaskMonitor monitor) {
                loadRemoteAddonsListInTask(monitor);
            }
        });
    }

    private void loadRemoteAddonsListInTask(ITaskMonitor monitor) {
        mStateFetchRemoteAddonsList = -1;

        /*
         * This env var can be defined to override the default addons_list.xml
         * location, useful for debugging.
         */
        String url = System.getenv("SDK_UPDATER_ADDONS_LIST");

        if (url == null) {
            url = SdkAddonsListConstants.URL_ADDON_LIST;
        }
        if (getSettingsController().getForceHttp()) {
            url = url.replaceAll("https://", "http://");  //$NON-NLS-1$ //$NON-NLS-2$
        }

        AddonsListFetcher fetcher = new AddonsListFetcher();
        Site[] sites = fetcher.fetch(monitor, url);
        if (sites != null) {
            mSources.removeAll(SdkSourceCategory.ADDONS_3RD_PARTY);

            for (Site s : sites) {
                mSources.add(SdkSourceCategory.ADDONS_3RD_PARTY,
                             new SdkAddonSource(s.getUrl(), s.getUiName()));
            }

            mStateFetchRemoteAddonsList = 1;
        }
    }

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java
//Synthetic comment -- index 9c26388..2bf377a 100755

//Synthetic comment -- @@ -64,7 +64,7 @@

ArrayList<ArchiveInfo> archives = new ArrayList<ArchiveInfo>();
ArrayList<Package> remotePkgs = new ArrayList<Package>();
        SdkSource[] remoteSources = sources.getAllSources();

// Create ArchiveInfos out of local (installed) packages.
ArchiveInfo[] localArchives = createLocalArchives(localPkgs);
//Synthetic comment -- @@ -137,7 +137,7 @@
}
}

        SdkSource[] remoteSources = sources.getAllSources();
ArrayList<Package> remotePkgs = new ArrayList<Package>();
fetchRemotePackages(remotePkgs, remoteSources);









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java
//Synthetic comment -- index 730ee8f..88fd754 100755

//Synthetic comment -- @@ -300,7 +300,7 @@
* Called by the main loop when the window has been disposed.
*/
private void dispose() {
        mUpdaterData.getSources().saveUserAddons(mUpdaterData.getSdkLog());
}

// --- page switching ---








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/icons/ImageFactory.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/icons/ImageFactory.java
//Synthetic comment -- index b4ccaf2..00de0b9 100755

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.sdklib.internal.repository.Archive;
import com.android.sdklib.internal.repository.Package;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdklib.internal.repository.SdkSourceCategory;
import com.android.sdkuilib.internal.repository.RepoSourcesAdapter;

import org.eclipse.swt.SWTException;
//Synthetic comment -- @@ -97,7 +98,10 @@
return getImageByName(name);
}

        if (object instanceof SdkSourceCategory) {
            return getImageByName("source_cat_icon16.png");                     //$NON-NLS-1$

        } else if (object instanceof SdkSource) {
return getImageByName("source_icon16.png");                         //$NON-NLS-1$

} else if (object instanceof RepoSourcesAdapter.RepoSourceError) {







