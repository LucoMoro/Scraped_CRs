/*Merge: SDK Manager: specify proxy on no-UI command-line.

This adds 2 flags to specify the http/https proxy host/port on
the command line when using the console-base "no-ui" SDK update.
The command-line proxy values override settings if defined.

Also revamped the argument help display to support larger
command-line long argument sizes and mandate that arguments
can have one of ther short or long argument name optional.

Change-Id:I013a1e5e9aa8e71f2f3e5f807a0363af86092ac1*/




//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/CommandLineProcessor.java b/sdkmanager/app/src/com/android/sdkmanager/CommandLineProcessor.java
//Synthetic comment -- index 5276bed..73b0f6b 100644

//Synthetic comment -- @@ -536,6 +536,18 @@
*/
protected void listOptions(String verb, String directObject) {
int numOptions = 0;
        int longArgLen = 8;

        for (Entry<String, Arg> entry : mArguments.entrySet()) {
            Arg arg = entry.getValue();
            if (arg.getVerb().equals(verb) && arg.getDirectObject().equals(directObject)) {
                int n = arg.getLongArg().length();
                if (n > longArgLen) {
                    longArgLen = n;
                }
            }
        }

for (Entry<String, Arg> entry : mArguments.entrySet()) {
Arg arg = entry.getValue();
if (arg.getVerb().equals(verb) && arg.getDirectObject().equals(directObject)) {
//Synthetic comment -- @@ -564,10 +576,25 @@
}
}

                // Java doesn't support * for printf variable width, so we'll insert the long arg
                // width "manually" in the printf format string.
                String longArgWidth = Integer.toString(longArgLen + 2);

                // For multi-line descriptions, pad new lines with the necessary whitespace.
                String desc = arg.getDescription();
                desc = desc.replaceAll("\n",                                           //$NON-NLS-1$
                        String.format("\n      %" + longArgWidth + "s", " ")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

                // Print a line in the form " -1_letter_arg --long_arg description"
                // where either the 1-letter arg or the long arg are optional.
                stdout("  %1$-2s %2$-" + longArgWidth + "s %3$s%4$s%5$s",              //$NON-NLS-1$
                        arg.getShortArg().length() > 0 ?
                                "-" + arg.getShortArg() :                              //$NON-NLS-1$
                                "",                                                    //$NON-NLS-1$
                        arg.getLongArg().length() > 0 ?
                                "--" + arg.getLongArg() :                              //$NON-NLS-1$
                                "",                                                    //$NON-NLS-1$
                        desc,
value,
required);
numOptions++;
//Synthetic comment -- @@ -707,8 +734,8 @@
* @param mode The {@link Mode} for the argument.
* @param mandatory True if this argument is mandatory for this action.
* @param directObject The action name. Can be #NO_VERB_OBJECT or #INTERNAL_FLAG.
         * @param shortName The one-letter short argument name. Can be empty but not null.
         * @param longName The long argument name. Can be empty but not null.
* @param description The description. Cannot be null.
* @param defaultValue The default value (or values), which depends on the selected {@link Mode}.
*/
//Synthetic comment -- @@ -803,8 +830,8 @@
* @param mode The {@link Mode} for the argument.
* @param verb The verb name. Can be #INTERNAL_VERB.
* @param directObject The action name. Can be #NO_VERB_OBJECT or #INTERNAL_FLAG.
     * @param shortName The one-letter short argument name. Can be empty but not null.
     * @param longName The long argument name. Can be empty but not null.
* @param description The description. Cannot be null.
* @param defaultValue The default value (or values), which depends on the selected {@link Mode}.
*/
//Synthetic comment -- @@ -816,6 +843,11 @@
String description, Object defaultValue) {
assert(!(mandatory && mode == Mode.BOOLEAN)); // a boolean mode cannot be mandatory

        // We should always have at least a short or long name, ideally both but never none.
        assert shortName != null;
        assert longName != null;
        assert shortName.length() > 0 || longName.length()  > 0;

if (directObject == null) {
directObject = NO_VERB_OBJECT;
}








//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index ddb7979..e951cc5 100644

//Synthetic comment -- @@ -321,6 +321,8 @@
boolean useHttp = mSdkCommandLine.getFlagNoHttps();
boolean dryMode = mSdkCommandLine.getFlagDryMode();
boolean obsolete = mSdkCommandLine.getFlagObsolete();
        String proxyHost = mSdkCommandLine.getProxyHost();
        String proxyPort = mSdkCommandLine.getProxyPort();

// Check filter types.
ArrayList<String> pkgFilter = new ArrayList<String>();
//Synthetic comment -- @@ -350,7 +352,8 @@
}
}

        UpdateNoWindow upd = new UpdateNoWindow(mOsSdkFolder, mSdkManager, mSdkLog,
                force, useHttp, proxyHost, proxyPort);
upd.updateAll(pkgFilter, obsolete, dryMode);
}









//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java b/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java
//Synthetic comment -- index 2cc721d..7062fae 100644

//Synthetic comment -- @@ -74,6 +74,8 @@
public static final String KEY_MAIN_PROJECT = "main";
public static final String KEY_NO_UI        = "no-ui";
public static final String KEY_NO_HTTPS     = "no-https";
    public static final String KEY_PROXY_PORT   = "proxy-port";
    public static final String KEY_PROXY_HOST   = "proxy-host";
public static final String KEY_DRY_MODE     = "dry-mode";
public static final String KEY_OBSOLETE     = "obsolete";

//Synthetic comment -- @@ -199,6 +201,16 @@
VERB_UPDATE, OBJECT_SDK, "s", KEY_NO_HTTPS,
"Uses HTTP instead of HTTPS (the default) for downloads", false);

        define(Mode.STRING, false,
                VERB_UPDATE, OBJECT_SDK, "", KEY_PROXY_PORT,
                "HTTP/HTTPS proxy port (overrides settings if defined)",
                null);

        define(Mode.STRING, false,
                VERB_UPDATE, OBJECT_SDK, "", KEY_PROXY_HOST,
                "HTTP/HTTPS proxy host (overrides settings if defined)",
                null);

define(Mode.BOOLEAN, false,
VERB_UPDATE, OBJECT_SDK, "f", KEY_FORCE,
"Forces replacement of a package or its parts, even if something has been modified",
//Synthetic comment -- @@ -207,7 +219,7 @@
define(Mode.STRING, false,
VERB_UPDATE, OBJECT_SDK, "t", KEY_FILTER,
"A filter that limits the update to the specified types of packages in the form of\n" +
                "a comma-separated list of " + Arrays.toString(SdkRepoConstants.NODES),
null);

define(Mode.BOOLEAN, false,
//Synthetic comment -- @@ -467,4 +479,14 @@
public String getParamFilter() {
return ((String) getValue(null, null, KEY_FILTER));
}

    /** Helper to retrieve the --proxy-host value. */
    public String getProxyHost() {
        return ((String) getValue(null, null, KEY_PROXY_HOST));
    }

    /** Helper to retrieve the --proxy-port value. */
    public String getProxyPort() {
        return ((String) getValue(null, null, KEY_PROXY_PORT));
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsController.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsController.java
//Synthetic comment -- index 34b2472..009e1e5 100755

//Synthetic comment -- @@ -258,7 +258,7 @@
String newHttpsSetting = mProperties.getProperty(ISettingsPage.KEY_FORCE_HTTP,
Boolean.FALSE.toString());
if (!newHttpsSetting.equals(oldHttpsSetting)) {
            // In case the HTTP/HTTPS setting changes, force sources to be reloaded
// (this only refreshes sources that the user has already tried to open.)
mUpdaterData.refreshSources(false /*forceFetching*/);
}
//Synthetic comment -- @@ -276,11 +276,18 @@
String proxyPort = mProperties.getProperty(ISettingsPage.KEY_HTTP_PROXY_PORT,
""); //$NON-NLS-1$

        // Set both the HTTP and HTTPS proxy system properties.
        // The system property constants can be found in the Java SE documentation at
        // http://download.oracle.com/javase/6/docs/technotes/guides/net/proxies.html
        final String JAVA_PROP_HTTP_PROXY_HOST =  "http.proxyHost";      //$NON-NLS-1$
        final String JAVA_PROP_HTTP_PROXY_PORT =  "http.proxyPort";      //$NON-NLS-1$
        final String JAVA_PROP_HTTPS_PROXY_HOST = "https.proxyHost";     //$NON-NLS-1$
        final String JAVA_PROP_HTTPS_PROXY_PORT = "https.proxyPort";     //$NON-NLS-1$

        props.setProperty(JAVA_PROP_HTTP_PROXY_HOST,  proxyHost);
        props.setProperty(JAVA_PROP_HTTP_PROXY_PORT,  proxyPort);
        props.setProperty(JAVA_PROP_HTTPS_PROXY_HOST, proxyHost);
        props.setProperty(JAVA_PROP_HTTPS_PROXY_PORT, proxyPort);
}

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdateNoWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdateNoWindow.java
//Synthetic comment -- index fb2c9a4..11fc3e8 100755

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.sdklib.repository.SdkRepoConstants;

import java.util.ArrayList;
import java.util.Properties;

/**
* Performs an update using only a non-interactive console output with no GUI.
//Synthetic comment -- @@ -53,20 +54,26 @@
* @param force The reply to any question asked by the update process. Currently this will
*   be yes/no for ability to replace modified samples or restart ADB.
* @param useHttp True to force using HTTP instead of HTTPS for downloads.
     * @param proxyPort An optional HTTP/HTTPS proxy port. Can be null.
     * @param proxyHost An optional HTTP/HTTPS proxy host. Can be null.
*/
public UpdateNoWindow(String osSdkRoot,
SdkManager sdkManager,
ISdkLog sdkLog,
boolean force,
            boolean useHttp,
            String proxyHost,
            String proxyPort) {
mSdkLog = sdkLog;
mForce = force;
mUpdaterData = new UpdaterData(osSdkRoot, sdkLog);

// Read and apply settings from settings file, so that http/https proxy is set
        // and let the command line args override them as necessary.
SettingsController settingsController = mUpdaterData.getSettingsController();
settingsController.loadSettings();
settingsController.applySettings();
        setupProxy(proxyHost, proxyPort);

// Change the in-memory settings to force the http/https mode
settingsController.setSetting(ISettingsPage.KEY_FORCE_HTTP, useHttp);
//Synthetic comment -- @@ -86,7 +93,6 @@
mUpdaterData.setupDefaultSources();

mUpdaterData.getLocalSdkParser().parseSdk(osSdkRoot, sdkManager, sdkLog);
}

/**
//Synthetic comment -- @@ -105,6 +111,33 @@
mUpdaterData.updateOrInstallAll_NoGUI(pkgFilter, includeObsoletes, dryMode);
}

    // -----

    /**
     * Sets both the HTTP and HTTPS proxy system properties, overriding the ones
     * from the settings with these values if they are defined.
     */
    private void setupProxy(String proxyHost, String proxyPort) {

        // The system property constants can be found in the Java SE documentation at
        // http://download.oracle.com/javase/6/docs/technotes/guides/net/proxies.html
        final String JAVA_PROP_HTTP_PROXY_HOST =  "http.proxyHost";      //$NON-NLS-1$
        final String JAVA_PROP_HTTP_PROXY_PORT =  "http.proxyPort";      //$NON-NLS-1$
        final String JAVA_PROP_HTTPS_PROXY_HOST = "https.proxyHost";     //$NON-NLS-1$
        final String JAVA_PROP_HTTPS_PROXY_PORT = "https.proxyPort";     //$NON-NLS-1$

        Properties props = System.getProperties();

        if (proxyHost != null && proxyHost.length() > 0) {
            props.setProperty(JAVA_PROP_HTTP_PROXY_HOST,  proxyHost);
            props.setProperty(JAVA_PROP_HTTPS_PROXY_HOST, proxyHost);
        }
        if (proxyPort != null && proxyPort.length() > 0) {
            props.setProperty(JAVA_PROP_HTTP_PROXY_PORT,  proxyPort);
            props.setProperty(JAVA_PROP_HTTPS_PROXY_PORT, proxyPort);
        }
    }

/**
* A custom implementation of {@link ITaskFactory} that provides {@link ConsoleTask} objects.
*/







