/*SDK Manager: simplify getenv var to override URLs.

SDK_TEST_URLS is a semicolon-separated list of URLs that can be used to
seed the SDK Updater list. This is only meant as a debugging and QA testing
tool and not for user usage.
To be used, the URLs must either end with the / or end with the canonical
filename expected for an addon list, addon repo or full repo.
This lets QA use URLs ending with / to cover all cases.

Change-Id:Ia7232d5d8a5b6e85d98e735b93a969e42624e919*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkAddonSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkAddonSource.java
//Synthetic comment -- index 78dfd69..049dad8 100755

//Synthetic comment -- @@ -51,7 +51,7 @@

@Override
protected String getUrlDefaultXmlFile() {
        return SdkAddonConstants.URL_DEFAULT_XML_FILE;
}

@Override








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkRepoSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkRepoSource.java
//Synthetic comment -- index 85838aa..9b63be3 100755

//Synthetic comment -- @@ -64,7 +64,7 @@

@Override
protected String getUrlDefaultXmlFile() {
        return SdkRepoConstants.URL_DEFAULT_XML_FILE;
}

@Override








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java
//Synthetic comment -- index 7c19fab..cee7fd6 100755

//Synthetic comment -- @@ -286,7 +286,7 @@
} else if (version < 1 && tryOtherUrl == 0 && !usingAlternateUrl) {
// This is obviously not one of our documents.
mFetchError = String.format(
                            "Failed to find an XML for the repository at URL '%1$s'",
url);

// If we haven't already tried the alternate URL, let's do it now.
//Synthetic comment -- @@ -310,7 +310,7 @@
// The alternate URL is obviously not a valid XML either.
// We only report the error if we failed to produce one earlier.
mFetchError = String.format(
                            "Failed to find an XML for the repository at URL '%1$s'",
url);
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/SdkAddonConstants.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/SdkAddonConstants.java
//Synthetic comment -- index 707fe01..fa75261 100755

//Synthetic comment -- @@ -28,7 +28,7 @@

/** The default name looked for by {@link SdkSource} when trying to load an
* sdk-addon XML if the URL doesn't match an existing resource. */
    public static final String URL_DEFAULT_XML_FILE = "addon.xml";         //$NON-NLS-1$

/** The base of our sdk-addon XML namespace. */
private static final String NS_BASE =








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/SdkAddonsListConstants.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/SdkAddonsListConstants.java
//Synthetic comment -- index 1793f1f..f8ed181 100755

//Synthetic comment -- @@ -28,6 +28,9 @@
public static final String URL_ADDON_LIST =
"https://dl-ssl.google.com/android/repository/addons_list.xml";     //$NON-NLS-1$

/** The base of our sdk-addons-list XML namespace. */
private static final String NS_BASE =
"http://schemas.android.com/sdk/android/addons-list/";              //$NON-NLS-1$








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/SdkRepoConstants.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/SdkRepoConstants.java
//Synthetic comment -- index 626f6d9..f08aa21 100755

//Synthetic comment -- @@ -32,7 +32,7 @@

/** The default name looked for by {@link SdkSource} when trying to load an
* sdk-repository XML if the URL doesn't match an existing resource. */
    public static final String URL_DEFAULT_XML_FILE = "repository.xml";         //$NON-NLS-1$

/** The base of our sdk-repository XML namespace. */
private static final String NS_BASE =








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/SdkAddonSourceTest.java b/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/SdkAddonSourceTest.java
//Synthetic comment -- index 5b2a3ab..d05c967 100755

//Synthetic comment -- @@ -151,7 +151,7 @@

Boolean[] validatorFound = new Boolean[] { Boolean.FALSE };
String[] validationError = new String[] { null };
        String url = "not-a-valid-url://" + SdkAddonConstants.URL_DEFAULT_XML_FILE;

String uri = mSource._validateXml(xmlStream, url, version, validationError, validatorFound);
assertEquals(Boolean.TRUE, validatorFound[0]);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/SdkRepoSourceTest.java b/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/SdkRepoSourceTest.java
//Synthetic comment -- index 2bbbd8c..c6d6eb3 100755

//Synthetic comment -- @@ -169,7 +169,7 @@

Boolean[] validatorFound = new Boolean[] { Boolean.FALSE };
String[] validationError = new String[] { null };
        String url = "not-a-valid-url://" + SdkRepoConstants.URL_DEFAULT_XML_FILE;

String uri = mSource._validateXml(xmlStream, url, version, validationError, validatorFound);
assertEquals(Boolean.TRUE, validatorFound[0]);
//Synthetic comment -- @@ -220,7 +220,7 @@

Boolean[] validatorFound = new Boolean[] { Boolean.FALSE };
String[] validationError = new String[] { null };
        String url = "not-a-valid-url://" + SdkRepoConstants.URL_DEFAULT_XML_FILE;

String uri = mSource._validateXml(xmlStream, url, version, validationError, validatorFound);
assertEquals(Boolean.TRUE, validatorFound[0]);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index 8c77e3a..744bb8d 100755

//Synthetic comment -- @@ -36,6 +36,7 @@
import com.android.sdklib.internal.repository.SdkSources;
import com.android.sdklib.internal.repository.ToolPackage;
import com.android.sdklib.internal.repository.AddonsListFetcher.Site;
import com.android.sdklib.repository.SdkAddonsListConstants;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
//Synthetic comment -- @@ -305,32 +306,41 @@
// Load user sources
sources.loadUserAddons(getSdkLog());

        // SDK_UPDATER_URLS is a semicolon-separated list of URLs that can be used to
        // seed the SDK Updater list for full repositories.
        String str = System.getenv("SDK_UPDATER_URLS");
if (str != null) {
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
        str = System.getenv("SDK_UPDATER_USER_URLS");
        if (str != null) {
            String[] urls = str.split(";");
            for (String url : urls) {
                if (url != null && url.length() > 0) {
                    SdkSource s = new SdkAddonSource(url, null/*uiName*/);
                    if (!sources.hasSourceUrl(s)) {
                        sources.add(SdkSourceCategory.GETENV_ADDONS, s);
}
}
}
//Synthetic comment -- @@ -832,30 +842,55 @@
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








