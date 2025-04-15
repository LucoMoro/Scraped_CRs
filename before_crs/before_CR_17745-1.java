/*SDK Manager: manager URL sites grouped in categories.

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
    public static final String PROP_USER_SOURCE  = "Pkg.UserSrc";      //$NON-NLS-1$
public static final String PROP_OBSOLETE     = "Pkg.Obsolete";     //$NON-NLS-1$

private final int mRevision;
//Synthetic comment -- @@ -142,12 +141,10 @@
// a package comes from.
String srcUrl = getProperty(props, PROP_SOURCE_URL, null);
if (props != null && source == null && srcUrl != null) {
            boolean isUser = Boolean.parseBoolean(props.getProperty(PROP_USER_SOURCE,
                                                                    Boolean.TRUE.toString()));
            if (isUser || (this instanceof AddonPackage)) {
                source = new SdkAddonSource(srcUrl, isUser);
} else {
                source = new SdkRepoSource(srcUrl);
}
}
mSource = source;
//Synthetic comment -- @@ -200,7 +197,6 @@

if (mSource != null) {
props.setProperty(PROP_SOURCE_URL,  mSource.getUrl());
            props.setProperty(PROP_USER_SOURCE, Boolean.toString(mSource.isUserSource()));
}
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkAddonSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkAddonSource.java
//Synthetic comment -- index ab24587..78dfd69 100755

//Synthetic comment -- @@ -34,10 +34,10 @@
* Constructs a new source for the given repository URL.
* @param url The source URL. Cannot be null. If the URL ends with a /, the default
*            repository.xml filename will be appended automatically.
     * @param userSource True if this a user source (add-ons & packages only.)
*/
    public SdkAddonSource(String url, boolean userSource) {
        super(url, false);
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkRepoSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkRepoSource.java
//Synthetic comment -- index 52e2c0a..85838aa 100755

//Synthetic comment -- @@ -47,9 +47,10 @@
* Constructs a new source for the given repository URL.
* @param url The source URL. Cannot be null. If the URL ends with a /, the default
*            repository.xml filename will be appended automatically.
*/
    public SdkRepoSource(String url) {
        super(url, false);
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java
//Synthetic comment -- index 0ef4a32..0ebe7fe 100755

//Synthetic comment -- @@ -59,19 +59,19 @@
public abstract class SdkSource implements IDescription {

private String mUrl;
    private final boolean mUserSource;

private Package[] mPackages;
private String mDescription;
private String mFetchError;

/**
* Constructs a new source for the given repository URL.
* @param url The source URL. Cannot be null. If the URL ends with a /, the default
*            repository.xml filename will be appended automatically.
     * @param userSource True if this a user source (add-ons & packages only.)
*/
    public SdkSource(String url, boolean userSource) {

// if the URL ends with a /, it must be "directory" resource,
// in which case we automatically add the default file that will
//Synthetic comment -- @@ -82,7 +82,7 @@
}

mUrl = url;
        mUserSource = userSource;
setDefaultDescription();
}

//Synthetic comment -- @@ -127,25 +127,27 @@
throws IOException;

/**
     * Two repo source are equal if they have the same userSource flag and the same URL.
*/
@Override
public boolean equals(Object obj) {
if (obj instanceof SdkSource) {
SdkSource rs = (SdkSource) obj;
            return  rs.isUserSource() == this.isUserSource() && rs.getUrl().equals(this.getUrl());
}
return false;
}

@Override
public int hashCode() {
        return mUrl.hashCode() ^ Boolean.valueOf(mUserSource).hashCode();
}

    /** Returns true if this is a user source. */
    public boolean isUserSource() {
        return mUserSource;
}

/** Returns the URL of the XML file for this source. */
//Synthetic comment -- @@ -390,7 +392,7 @@
}

private void setDefaultDescription() {
        if (mUserSource) {
mDescription = String.format("Add-on Source: %1$s", mUrl);
} else {
mDescription = String.format("SDK Source: %1$s", mUrl);
//Synthetic comment -- @@ -663,7 +665,7 @@
} else if (RepoConstants.NODE_EXTRA.equals(name)) {
p = new ExtraPackage(this, child, nsUri, licenses);

                        } else if (!mUserSource) {
// We only load platform, doc and tool packages from internal
// sources, never from user sources.
if (SdkRepoConstants.NODE_PLATFORM.equals(name)) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSourceCategory.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSourceCategory.java
new file mode 100755
//Synthetic comment -- index 0000000..29d27a7

//Synthetic comment -- @@ -0,0 +1,62 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSources.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSources.java
//Synthetic comment -- index 2bb22f4..becdc97 100755

//Synthetic comment -- @@ -25,11 +25,15 @@
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

/**
 * A list of sdk-repository and sdk-addon sources.
*/
public class SdkSources {

//Synthetic comment -- @@ -39,7 +43,8 @@

private static final String SRC_FILENAME = "repositories.cfg"; //$NON-NLS-1$

    private ArrayList<SdkSource> mSources = new ArrayList<SdkSource>();

public SdkSources() {
}
//Synthetic comment -- @@ -47,37 +52,130 @@
/**
* Adds a new source to the Sources list.
*/
    public void add(SdkSource source) {
        mSources.add(source);
}

/**
* Removes a source from the Sources list.
*/
public void remove(SdkSource source) {
        mSources.remove(source);
}

/**
     * Returns the sources list array. This is never null.
*/
    public SdkSource[] getSources() {
        return mSources.toArray(new SdkSource[mSources.size()]);
}

/**
* Loads all user sources. This <em>replaces</em> all existing user sources
* by the ones from the property file.
*/
    public void loadUserSources(ISdkLog log) {

// Remove all existing user sources
        for (Iterator<SdkSource> it = mSources.iterator(); it.hasNext(); ) {
            SdkSource s = it.next();
            if (s.isUserSource()) {
                it.remove();
            }
        }

// Load new user sources from property file
FileInputStream fis = null;
//Synthetic comment -- @@ -95,9 +193,9 @@
for (int i = 0; i < count; i++) {
String url = props.getProperty(String.format("%s%02d", KEY_SRC, i));  //$NON-NLS-1$
if (url != null) {
                        SdkSource s = new SdkAddonSource(url, true /*userSource*/);
if (!hasSource(s)) {
                            mSources.add(s);
}
}
}
//Synthetic comment -- @@ -123,24 +221,10 @@
}

/**
     * Returns true if there's already a similar source in the sources list.
     * <p/>
     * The search is O(N), which should be acceptable on the expectedly small source list.
     */
    public boolean hasSource(SdkSource source) {
        for (SdkSource s : mSources) {
            if (s.equals(source)) {
                return true;
            }
        }
        return false;
    }

    /**
* Saves all the user sources.
* @param log Logger. Cannot be null.
*/
    public void saveUserSources(ISdkLog log) {
FileOutputStream fos = null;
try {
String folder = AndroidLocation.getFolder();
//Synthetic comment -- @@ -151,11 +235,9 @@
Properties props = new Properties();

int count = 0;
            for (SdkSource s : mSources) {
                if (s.isUserSource()) {
                    count++;
                    props.setProperty(String.format("%s%02d", KEY_SRC, count), s.getUrl());  //$NON-NLS-1$
                }
}
props.setProperty(KEY_COUNT, Integer.toString(count));









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/SdkAddonSourceTest.java b/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/SdkAddonSourceTest.java
//Synthetic comment -- index 0d8c90f..5b2a3ab 100755

//Synthetic comment -- @@ -38,7 +38,7 @@
*/
private static class MockSdkAddonSource extends SdkAddonSource {
public MockSdkAddonSource() {
            super("fake-url", true /*userSource*/);
}

public Document _findAlternateToolsXml(InputStream xml) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/SdkRepoSourceTest.java b/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/SdkRepoSourceTest.java
//Synthetic comment -- index f51cc79..2bbbd8c 100755

//Synthetic comment -- @@ -38,7 +38,7 @@
*/
private static class MockSdkRepoSource extends SdkRepoSource {
public MockSdkRepoSource() {
            super("fake-url");
}

public Document _findAlternateToolsXml(InputStream xml) throws IOException {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/IPageSwitchListener.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/IPageSwitchListener.java
new file mode 100755
//Synthetic comment -- index 0000000..84f9a94

//Synthetic comment -- @@ -0,0 +1,30 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RemotePackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RemotePackagesPage.java
//Synthetic comment -- index 099826a..90dc888 100755

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.sdklib.internal.repository.Package;
import com.android.sdklib.internal.repository.SdkAddonSource;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdkuilib.repository.UpdaterWindow.ISdkListener;

import org.eclipse.jface.dialogs.IInputValidator;
//Synthetic comment -- @@ -52,10 +53,12 @@
import java.util.ArrayList;


public class RemotePackagesPage extends Composite implements ISdkListener {

private final UpdaterData mUpdaterData;

private CheckboxTreeViewer mTreeViewerSources;
private Tree mTreeSources;
private TreeColumn mColumnSource;
//Synthetic comment -- @@ -189,6 +192,24 @@
// Disable the check that prevents subclassing of SWT components
}

// -- Start of internal part ----------
// Hide everything down-below from SWT designer
//$hide>>$
//Synthetic comment -- @@ -336,7 +357,7 @@

private void onAddSiteSelected() {

        final SdkSource[] knowSources = mUpdaterData.getSources().getSources();
String title = "Add Add-on Site URL";

String msg =
//Synthetic comment -- @@ -378,7 +399,9 @@

if (dlg.open() == Window.OK) {
String url = dlg.getValue();
            mUpdaterData.getSources().add(new SdkAddonSource(url, true /*userSource*/));
onRefreshSelected();
}
}
//Synthetic comment -- @@ -389,17 +412,20 @@
ISelection sel = mTreeViewerSources.getSelection();
if (mUpdaterData != null && sel instanceof ITreeSelection) {
for (Object c : ((ITreeSelection) sel).toList()) {
                if (c instanceof SdkSource && ((SdkSource) c).isUserSource()) {
SdkSource source = (SdkSource) c;

                    String title = "Delete Add-on Site?";

                    String msg = String.format("Are you sure you want to delete the add-on site '%1$s'?",
                            source.getUrl());

                    if (MessageDialog.openQuestion(getShell(), title, msg)) {
                        mUpdaterData.getSources().remove(source);
                        changed = true;
}
}
}
//Synthetic comment -- @@ -418,14 +444,6 @@
updateButtonsState();
}

    public void onSdkChange(boolean init) {
        RepoSourcesAdapter sources = mUpdaterData.getSourcesAdapter();
        mTreeViewerSources.setContentProvider(sources.getContentProvider());
        mTreeViewerSources.setLabelProvider(  sources.getLabelProvider());
        mTreeViewerSources.setInput(sources);
        onTreeSelected();
    }

private void updateButtonsState() {
// We install archives, so there should be at least one checked archive.
// Having sites or packages checked does not count.
//Synthetic comment -- @@ -446,7 +464,8 @@
if (sel instanceof ITreeSelection) {
for (Object c : ((ITreeSelection) sel).toList()) {
if (c instanceof SdkSource &&
                        ((SdkSource) c).isUserSource()) {
hasSelectedUserSource = true;
break;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RepoSourcesAdapter.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RepoSourcesAdapter.java
//Synthetic comment -- index 3a44bb4..191f2e1 100755

//Synthetic comment -- @@ -166,7 +166,7 @@
*/
public Object[] getChildren(Object parentElement) {
if (parentElement == RepoSourcesAdapter.this) {
                return mUpdaterData.getSources().getSources();

} else if (parentElement instanceof SdkSource) {
return getRepoSourceChildren((SdkSource) parentElement);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index 25ed30f..ccea745 100755

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.repository.AddonPackage;
import com.android.sdklib.internal.repository.Archive;
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskFactory;
//Synthetic comment -- @@ -31,8 +32,11 @@
import com.android.sdklib.internal.repository.SdkAddonSource;
import com.android.sdklib.internal.repository.SdkRepoSource;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdklib.internal.repository.SdkSources;
import com.android.sdklib.internal.repository.ToolPackage;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.repository.UpdaterWindow.ISdkListener;
//Synthetic comment -- @@ -276,16 +280,22 @@
/**
* Sets up the default sources: <br/>
* - the default google SDK repository, <br/>
     * - the extra repo URLs from the environment, <br/>
* - the user sources from prefs <br/>
* - and finally the extra user repo URLs from the environment.
*/
public void setupDefaultSources() {
SdkSources sources = getSources();
        sources.add(new SdkRepoSource(SdkRepoConstants.URL_GOOGLE_SDK_SITE));

        // TODO load addons_list
        //--sources.add(new SdkAddonSource(SdkAddonConstants.URL_GOOGLE_SDK_SITE, false /*userSource*/));

// SDK_UPDATER_URLS is a semicolon-separated list of URLs that can be used to
// seed the SDK Updater list for full repositories.
//Synthetic comment -- @@ -294,21 +304,14 @@
String[] urls = str.split(";");
for (String url : urls) {
if (url != null && url.length() > 0) {
                    SdkSource s = new SdkRepoSource(url);
if (!sources.hasSource(s)) {
                        sources.add(s);
                    }
                    s = new SdkAddonSource(url, false /*userSource*/);
                    if (!sources.hasSource(s)) {
                        sources.add(s);
}
}
}
}

        // Load user sources
        sources.loadUserSources(getSdkLog());

// SDK_UPDATER_USER_URLS is a semicolon-separated list of URLs that can be used to
// seed the SDK Updater list for user-only repositories. User sources can only provide
// add-ons and extra packages.
//Synthetic comment -- @@ -317,9 +320,9 @@
String[] urls = str.split(";");
for (String url : urls) {
if (url != null && url.length() > 0) {
                    SdkSource s = new SdkAddonSource(url, true /*userSource*/);
if (!sources.hasSource(s)) {
                        sources.add(s);
}
}
}
//Synthetic comment -- @@ -783,7 +786,7 @@

mTaskFactory.start("Refresh Sources", new ITask() {
public void run(ITaskMonitor monitor) {
                SdkSource[] sources = mSources.getSources();
monitor.setProgressMax(sources.length);
for (SdkSource source : sources) {
if (forceFetching ||
//Synthetic comment -- @@ -796,4 +799,49 @@
}
});
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java
//Synthetic comment -- index 9c26388..2bf377a 100755

//Synthetic comment -- @@ -64,7 +64,7 @@

ArrayList<ArchiveInfo> archives = new ArrayList<ArchiveInfo>();
ArrayList<Package> remotePkgs = new ArrayList<Package>();
        SdkSource[] remoteSources = sources.getSources();

// Create ArchiveInfos out of local (installed) packages.
ArchiveInfo[] localArchives = createLocalArchives(localPkgs);
//Synthetic comment -- @@ -137,7 +137,7 @@
}
}

        SdkSource[] remoteSources = sources.getSources();
ArrayList<Package> remotePkgs = new ArrayList<Package>();
fetchRemotePackages(remotePkgs, remoteSources);









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java
//Synthetic comment -- index 730ee8f..876c2dc 100755

//Synthetic comment -- @@ -300,7 +300,7 @@
* Called by the main loop when the window has been disposed.
*/
private void dispose() {
        mUpdaterData.getSources().saveUserSources(mUpdaterData.getSdkLog());
}

// --- page switching ---
//Synthetic comment -- @@ -383,6 +383,10 @@
mPageList.setSelection(index);
mInternalPageChange = false;
}
}
}








