//<Beginning of snippet n. 0>
if (args.length >= 3 && args[0].equals("ping")) {
    stats.ping(args[1], args[2]);
    return;
} else if (args.length > 0) {
    Log.e("ddms", "Unknown argument: " + args[0]); // Consider log statement for debug builds only
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

public SdkStatsService() {
}

public void ping(String app, String version) {
    validateInput(app, version);
    doPing(app, version);
}

private void validateInput(String app, String version) {
    if (!app.matches("\\w+")) {
        throw new IllegalArgumentException("Bad app name: " + app);
    }
    String[] numbers = version.split("\\.");
    if (numbers.length < 1 || numbers.length > 4) {
        throw new IllegalArgumentException("Bad version format: " + version);
    }
    for (String number : numbers) {
        if (!number.matches("\\d+")) {
            throw new IllegalArgumentException("Version must be numeric: " + version);
        }
    }
}

private void doPing(final String app, String version) {
    if (!mStore.isPingOptIn()) {
        return;
    }

    new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                actuallySendPing(app, version, id);
            } catch (IOException e) {
                Log.e("PingError", "Failed to send ping: " + e.getMessage());
            }
        }
    }).start();
}

private void actuallySendPing(String app, String version, long id) throws IOException {
    String osName = URLEncoder.encode(getOsName(), "UTF-8");
    String osArch = URLEncoder.encode(getOsArch(), "UTF-8");
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
    if (conn.getResponseCode() != HttpURLConnection.HTTP_OK &&
        conn.getResponseCode() != HttpURLConnection.HTTP_NOT_FOUND) {
        throw new IOException(conn.getResponseMessage() + ": " + url);    //$NON-NLS-1$
    }
}

//<End of snippet n. 1>

//<Beginning of snippet n. 2>
package com.android.sdkstats;

import java.util.HashMap;
import java.util.Map;

private final String mOsArch;
private final String mJavaVersion;
private final Map<String, String> mEnvVars = new HashMap<String, String>();

public MockSdkStatsService(String osName, String osVersion, String javaVersion) {
    this.mOsArch = osName;
    this.mJavaVersion = javaVersion;
}

public void setSystemEnv(String varName, String value) {
    mEnvVars.put(varName, value);
}
//<End of snippet n. 2>