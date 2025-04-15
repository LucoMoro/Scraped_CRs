/*SDK: Send android gl info with ping emulator stats.

Related emulator change: 733fffaac9ccebfc424fccf9467b22475f71a2f8

Change-Id:Ie7948a9be6c1289306968cda91d1e0f0cf7cb61c*/




//Synthetic comment -- diff --git a/ddms/app/src/com/android/ddms/Main.java b/ddms/app/src/com/android/ddms/Main.java
//Synthetic comment -- index a7e0a2b..bfdb78b 100644

//Synthetic comment -- @@ -87,7 +87,7 @@
// the "ping" argument means to check in with the server and exit
// the application name and version number must also be supplied
if (args.length >= 3 && args[0].equals("ping")) {
            stats.ping(args);
return;
} else if (args.length > 0) {
Log.e("ddms", "Unknown argument: " + args[0]);








//Synthetic comment -- diff --git a/sdkstats/src/com/android/sdkstats/SdkStatsService.java b/sdkstats/src/com/android/sdkstats/SdkStatsService.java
//Synthetic comment -- index 9c3a008..78641ec 100644

//Synthetic comment -- @@ -20,10 +20,14 @@
import org.eclipse.swt.widgets.Shell;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -38,6 +42,8 @@
/** Minimum interval between ping, in milliseconds. */
private static final long PING_INTERVAL_MSEC = 86400 * 1000;  // 1 day

    private static final boolean DEBUG = System.getenv("ANDROID_DEBUG_PING") != null; //$NON-NLS-1$

private DdmsPreferenceStore mStore = new DdmsPreferenceStore();

public SdkStatsService() {
//Synthetic comment -- @@ -45,19 +51,88 @@

/**
* Send a "ping" to the Google toolbar server, if enough time has
     * elapsed since the last ping, and if the user has not opted out.
     * <p/>
     * This is a simplified version of {@link #ping(String[])} that only
     * sends an "application" name and a "version" string. See the explanation
     * there for details.
*
     * @param app The application name that reports the ping (e.g. "emulator" or "ddms".)
     *          Valid characters are a-zA-Z0-9 only.
     * @param version The version string (e.g. "12" or "1.2.3.4", 4 groups max.)
     * @see #ping(String[])
*/
public void ping(String app, String version) {
        doPing(app, version, null);
    }

    /**
     * Send a "ping" to the Google toolbar server, if enough time has
     * elapsed since the last ping, and if the user has not opted out.
     * <p/>
     * The ping will not be sent if the user opt out dialog has not been shown yet.
     * Use {@link #checkUserPermissionForPing(Shell)} to display the dialog requesting
     * user permissions.
     * <p/>
     * Note: The actual ping (if any) is sent in a <i>non-daemon</i> background thread.
     * <p/>
     * The arguments are defined as follow:
     * <ul>
     * <li>Argument 0 is the "ping" command and is ignored.</li>
     * <li>Argument 1 is the application name that reports the ping (e.g. "emulator" or "ddms".)
     *          Valid characters are a-zA-Z0-9 only.</li>
     * <li>Argument 2 is the version string (e.g. "12" or "1.2.3.4", 4 groups max.)</li>
     * <li>Arguments 3+ are optional and depend on the application name.</li>
     * <li>"emulator" application currently has 3 optional arguments:
     *      <ul>
     *      <li>Arugment 3: android_gl_vendor</li>
     *      <li>Arugment 4: android_gl_renderer</li>
     *      <li>Arugment 5: android_gl_version</li>
     *      </ul>
     * </li>
     * </ul>
     *
     * @param arguments A non-empty non-null array of arguments to the ping as described above.
     */
    public void ping(String[] arguments) {
        if (arguments == null || arguments.length < 3) {
            throw new IllegalArgumentException(
                    "Invalid ping arguments: expected ['ping', app, version] but got " +
                    (arguments == null ? "null" : Arrays.toString(arguments)));
        }
        int len = arguments.length;
        String app = arguments[1];
        String version = arguments[2];

        Map<String, String> extras = new HashMap<String, String>();

        if ("emulator".equals(app)) {                                   //$NON-NLS-1$
            if (len > 3) {
                extras.put("agl-vend", sanitizeArgument(arguments[3])); //$NON-NLS-1$ vendor
            }
            if (len > 4) {
                extras.put("agl-rend", sanitizeArgument(arguments[4])); //$NON-NLS-1$ renderer
            }
            if (len > 5) {
                extras.put("agl-ver",  sanitizeArgument(arguments[5])); //$NON-NLS-1$ version
            }
        }

        doPing(app, version, extras);
    }

    private String sanitizeArgument(String arg) {
        if (arg == null) {
        arg = "";                                                       //$NON-NLS-1$
        } else {
            try {
                arg = URLEncoder.encode(arg.trim(), "UTF-8");           //$NON-NLS-1$
            } catch (UnsupportedEncodingException e) {
                arg = "";                                               //$NON-NLS-1$
            }
        }

        return arg;
}

/**
//Synthetic comment -- @@ -95,12 +170,16 @@
/**
* Pings the usage stats server, as long as the prefs contain the opt-in boolean
*
     * @param app The application name that reports the ping (e.g. "emulator" or "ddms".)
     *          Will be normalized.  Valid characters are a-zA-Z0-9 only.
     * @param version The version string (e.g. "12" or "1.2.3.4", 4 groups max.)
     * @param extras Extra key/value parameters to send. They are send as-is and must
     *  already be well suited and escaped using {@link URLEncoder#encode(String, String)}.
*/
    private void doPing(String app, String version, final Map<String, String> extras) {
// Validate the application and version input.
        final String nApp = normalizeAppName(app);
        final String nVersion = normalizeVersion(version);

// If the user has not opted in, do nothing and quietly return.
if (!mStore.isPingOptIn()) {
//Synthetic comment -- @@ -126,7 +205,7 @@
@Override
public void run() {
try {
                    actuallySendPing(nApp, nVersion, id, extras);
} catch (IOException e) {
e.printStackTrace();
}
//Synthetic comment -- @@ -138,29 +217,46 @@
/**
* Unconditionally send a "ping" request to the Google toolbar server.
*
     * @param app The application name that reports the ping (e.g. "emulator" or "ddms".)
     *          Valid characters are a-zA-Z0-9 only.
     * @param version The version string already formatted as a 4 dotted group (e.g. "1.2.3.4".)
* @param id of the local installation
     * @param extras Extra key/value parameters to send. They are send as-is and must
     *  already be well suited and escaped using {@link URLEncoder#encode(String, String)}.
* @throws IOException if the ping failed
*/
    private void actuallySendPing(String app, String version, long id, Map<String, String> extras)
throws IOException {
        String osName  = URLEncoder.encode(getOsName(),  "UTF-8");  //$NON-NLS-1$
        String osArch  = URLEncoder.encode(getOsArch(),  "UTF-8");  //$NON-NLS-1$
        String jvmArch = URLEncoder.encode(getJvmInfo(), "UTF-8");  //$NON-NLS-1$

// Include the application's name as part of the as= value.
// Share the user ID for all apps, to allow unified activity reports.

        String extraStr = "";                                       //$NON-NLS-1$
        if (extras != null && !extras.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : extras.entrySet()) {
                sb.append('&').append(entry.getKey()).append('=').append(entry.getValue());
            }
            extraStr = sb.toString();
        }

URL url = new URL(
            "http",                                                 //$NON-NLS-1$
            "tools.google.com",                                     //$NON-NLS-1$
            "/service/update?as=androidsdk_" + app +                //$NON-NLS-1$
                "&id=" + Long.toHexString(id) +                     //$NON-NLS-1$
                "&version=" + version +                             //$NON-NLS-1$
                "&os=" + osName +                                   //$NON-NLS-1$
                "&osa=" + osArch +                                  //$NON-NLS-1$
                "&vma=" + jvmArch +                                 //$NON-NLS-1$
                extraStr);

        if (DEBUG) {
            System.err.println("Ping: " + url.toString());          //$NON-NLS-1$
        }

// Discard the actual response, but make sure it reads OK
HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//Synthetic comment -- @@ -170,7 +266,7 @@
if (conn.getResponseCode() != HttpURLConnection.HTTP_OK &&
conn.getResponseCode() != HttpURLConnection.HTTP_NOT_FOUND) {
throw new IOException(
                conn.getResponseMessage() + ": " + url);            //$NON-NLS-1$
}
}

//Synthetic comment -- @@ -361,16 +457,28 @@
}

/**
     * Normalize the supplied application name.
     *
* @param app to report
     */
    private String normalizeAppName(String app) {
        // Filter out \W , non-word character: [^a-zA-Z_0-9]
        String app2 = app.replaceAll("\\W", "");                  //$NON-NLS-1$ //$NON-NLS-2$

        if (app.length() == 0) {
            throw new IllegalArgumentException("Bad app name: " + app);         //$NON-NLS-1$
        }

        return app2;
    }

    /**
     * Validate the supplied application version, and normalize the version.
     *
* @param version supplied by caller
* @return normalized dotted quad version
*/
    private String normalizeVersion(String version) {

// Version must be between 1 and 4 dotted numbers
String[] numbers = version.split("\\.");                                //$NON-NLS-1$







