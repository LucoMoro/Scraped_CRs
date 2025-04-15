/*SDK: Send android gl info with ping emulator stats.

Related emulator change: 733fffaac9ccebfc424fccf9467b22475f71a2f8

Change-Id:Ie7948a9be6c1289306968cda91d1e0f0cf7cb61c*/
//Synthetic comment -- diff --git a/ddms/app/src/com/android/ddms/Main.java b/ddms/app/src/com/android/ddms/Main.java
//Synthetic comment -- index a7e0a2b..bfdb78b 100644

//Synthetic comment -- @@ -87,7 +87,7 @@
// the "ping" argument means to check in with the server and exit
// the application name and version number must also be supplied
if (args.length >= 3 && args[0].equals("ping")) {
            stats.ping(args[1], args[2]);
return;
} else if (args.length > 0) {
Log.e("ddms", "Unknown argument: " + args[0]);








//Synthetic comment -- diff --git a/sdkstats/src/com/android/sdkstats/SdkStatsService.java b/sdkstats/src/com/android/sdkstats/SdkStatsService.java
//Synthetic comment -- index 9c3a008..c490fed 100644

//Synthetic comment -- @@ -20,10 +20,15 @@
import org.eclipse.swt.widgets.Shell;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -38,6 +43,8 @@
/** Minimum interval between ping, in milliseconds. */
private static final long PING_INTERVAL_MSEC = 86400 * 1000;  // 1 day

private DdmsPreferenceStore mStore = new DdmsPreferenceStore();

public SdkStatsService() {
//Synthetic comment -- @@ -45,19 +52,97 @@

/**
* Send a "ping" to the Google toolbar server, if enough time has
     * elapsed since the last ping, and if the user has not opted out.<br>
*
     * The ping will not be sent if the user opt out dialog has not been shown yet.
     * Use {@link #checkUserPermissionForPing(Shell)} to display the dialog requesting
     * user permissions.<br>
     *
     * Note: The actual ping (if any) is sent in a <i>non-daemon</i> background thread.
     *
     * @param app name to report in the ping
     * @param version to report in the ping
*/
public void ping(String app, String version) {
        doPing(app, version);
}

/**
//Synthetic comment -- @@ -95,12 +180,19 @@
/**
* Pings the usage stats server, as long as the prefs contain the opt-in boolean
*
     * @param app name to report in the ping
     * @param version to report in the ping
*/
    private void doPing(final String app, String version) {
// Validate the application and version input.
        final String normalVersion = normalizeVersion(app, version);

// If the user has not opted in, do nothing and quietly return.
if (!mStore.isPingOptIn()) {
//Synthetic comment -- @@ -126,7 +218,8 @@
@Override
public void run() {
try {
                    actuallySendPing(app, normalVersion, id);
} catch (IOException e) {
e.printStackTrace();
}
//Synthetic comment -- @@ -136,31 +229,17 @@


/**
     * Unconditionally send a "ping" request to the Google toolbar server.
*
     * @param app name to report in the ping
     * @param version to report in the ping (dotted numbers, no more than four)
     * @param id of the local installation
     * @throws IOException if the ping failed
*/
    private void actuallySendPing(String app, String version, long id)
                throws IOException {
        String osName  = URLEncoder.encode(getOsName(),  "UTF-8");
        String osArch  = URLEncoder.encode(getOsArch(),  "UTF-8");
        String jvmArch = URLEncoder.encode(getJvmInfo(), "UTF-8");

        // Include the application's name as part of the as= value.
        // Share the user ID for all apps, to allow unified activity reports.

        URL url = new URL(
            "http",                                         //$NON-NLS-1$
            "tools.google.com",                             //$NON-NLS-1$
            "/service/update?as=androidsdk_" + app +        //$NON-NLS-1$
                "&id=" + Long.toHexString(id) +             //$NON-NLS-1$
                "&version=" + version +                     //$NON-NLS-1$
                "&os=" + osName +                           //$NON-NLS-1$
                "&osa=" + osArch +                          //$NON-NLS-1$
                "&vma=" + jvmArch);                         //$NON-NLS-1$

// Discard the actual response, but make sure it reads OK
HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//Synthetic comment -- @@ -170,11 +249,53 @@
if (conn.getResponseCode() != HttpURLConnection.HTTP_OK &&
conn.getResponseCode() != HttpURLConnection.HTTP_NOT_FOUND) {
throw new IOException(
                conn.getResponseMessage() + ": " + url);    //$NON-NLS-1$
}
}

/**
* Detects and reports the host OS: "linux", "win" or "mac".
* For Windows and Mac also append the version, so for example
* Win XP will return win-5.1.
//Synthetic comment -- @@ -361,16 +482,28 @@
}

/**
     * Validate the supplied application version, and normalize the version.
* @param app to report
* @param version supplied by caller
* @return normalized dotted quad version
*/
    private String normalizeVersion(String app, String version) {
        // Application name must contain only word characters (no punctuation)
        if (!app.matches("\\w+")) {                                             //$NON-NLS-1$
            throw new IllegalArgumentException("Bad app name: " + app);         //$NON-NLS-1$
        }

// Version must be between 1 and 4 dotted numbers
String[] numbers = version.split("\\.");                                //$NON-NLS-1$








//Synthetic comment -- diff --git a/sdkstats/tests/com/android/sdkstats/SdkStatsServiceTest.java b/sdkstats/tests/com/android/sdkstats/SdkStatsServiceTest.java
//Synthetic comment -- index 6b8a4eb..b1db42b 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdkstats;

import java.util.HashMap;
import java.util.Map;

//Synthetic comment -- @@ -30,6 +31,7 @@
private final String mOsArch;
private final String mJavaVersion;
private final Map<String, String> mEnvVars = new HashMap<String, String>();

public MockSdkStatsService(String osName,
String osVersion,
//Synthetic comment -- @@ -41,6 +43,10 @@
mJavaVersion = javaVersion;
}

public void setSystemEnv(String varName, String value) {
mEnvVars.put(varName, value);
}
//Synthetic comment -- @@ -71,6 +77,29 @@
return null;
}

}

@Override
//Synthetic comment -- @@ -276,4 +305,107 @@
"42", "x86_64", "1.7.8_09");
assertEquals("one3456789ten3456789twenty6789th", m.getOsName());
}
}







