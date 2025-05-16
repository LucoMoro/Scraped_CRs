//<Beginning of snippet n. 0>
if (args.length >= 3 && args[0].equals("ping")) {
    stats.ping(args[1], args[2]);
    return;
} else if (args.length > 0) {
    Log.e("ddms", "Unknown argument: " + args[0]);
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
import org.eclipse.swt.widgets.Shell;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

private static final long PING_INTERVAL_MSEC = 86400 * 1000;  // 1 day

private DdmsPreferenceStore mStore = new DdmsPreferenceStore();

public SdkStatsService() {}

/**
 * Send a "ping" to the Google toolbar server, if enough time has
 * elapsed since the last ping, and if the user has not opted out.
 * The ping will not be sent if the user opt out dialog has not been shown yet.
 * Use {@link #checkUserPermissionForPing(Shell)} to display the dialog requesting
 * user permissions.
 *
 * @param app name to report in the ping
 * @param version to report in the ping
 */
public void ping(String app, String version) {
    doPing(app, version);
}

/**
 * Pings the usage stats server, as long as the prefs contain the opt-in boolean
 *
 * @param app name to report in the ping
 * @param version to report in the ping
 */
private void doPing(final String app, String version) {
    final String normalVersion = normalizeVersion(app, version);

    if (!mStore.isPingOptIn()) {
        return;
    }
    
    new Thread() {
        @Override
        public void run() {
            try {
                actuallySendPing(app, normalVersion, id);
            } catch (IOException e) {
                Log.e("Ddms", "Ping failed: " + e.getMessage());
            }
        }
    }.start();
}

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

    URL url = new URL(
        "https",                                         //$NON-NLS-1$
        "tools.google.com",                             //$NON-NLS-1$
        "/service/update?as=androidsdk_" + app +        //$NON-NLS-1$
            "&id=" + Long.toHexString(id) +             //$NON-NLS-1$
            "&version=" + version +                     //$NON-NLS-1$
            "&os=" + osName +                           //$NON-NLS-1$
            "&osa=" + osArch +                          //$NON-NLS-1$
            "&vma=" + jvmArch);                         //$NON-NLS-1$

    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    int responseCode = conn.getResponseCode();
    if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_NOT_FOUND) {
        throw new IOException(conn.getResponseMessage() + ": " + url);    //$NON-NLS-1$
    }
}

/**
 * Validate the supplied application version, and normalize the version.
 *
 * @param app to report
 * @param version supplied by caller
 * @return normalized dotted quad version
 */
private String normalizeVersion(String app, String version) {
    if (!app.matches("^(knownAppName1|knownAppName2|knownAppName3)$")) {
        throw new IllegalArgumentException("Bad app name: " + app);         //$NON-NLS-1$
    }

    String[] numbers = version.split("\\.");
    if (numbers.length > 4) {
        throw new IllegalArgumentException("Version must not exceed 4 parts: " + version);
    }

    for (String number : numbers) {
        if (number.length() == 0 || number.length() > 10) {
            throw new IllegalArgumentException("Invalid version component length: " + number);
        }
    }
    return version;
}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
package com.android.sdkstats;

import java.util.HashMap;
import java.util.Map;

private final String mOsArch;
private final String mJavaVersion;
private final Map<String, String> mEnvVars = new HashMap<String, String>();

public MockSdkStatsService(String osName, String osVersion) {
    this.mOsArch = osArch;
    this.mJavaVersion = javaVersion;
}

public void setSystemEnv(String varName, String value) {
    mEnvVars.put(varName, value);
}
//<End of snippet n. 2>