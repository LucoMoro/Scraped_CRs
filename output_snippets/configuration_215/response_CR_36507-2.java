//<Beginning of snippet n. 0>
import java.util.logging.Level;
import java.util.logging.Logger;

private static final Logger logger = Logger.getLogger(SdkStatsService.class.getName());

if (args.length >= 3 && args[0].equals("ping")) {
    if (args[1] == null || args[2] == null) {
        logger.log(Level.WARNING, "Application name or version cannot be null.");
        return;
    }
    stats.ping(args[1], args[2]);
    return;
} else if (args.length > 0) {
    logger.log(Level.WARNING, "Unknown argument: " + args[0]);
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
import java.util.logging.Level;
import java.util.logging.Logger;

private static final long PING_INTERVAL_MSEC = 86400 * 1000;
private DdmsPreferenceStore mStore = new DdmsPreferenceStore();
private static final Logger logger = Logger.getLogger(SdkStatsService.class.getName());

public SdkStatsService() {
}

public void ping(String app, String version) {
    if (app == null || version == null) {
        logger.log(Level.WARNING, "Application name or version cannot be null.");
        return;
    }
    if (!isValidApp(app) || !isValidVersion(version)) {
        logger.log(Level.WARNING, "Invalid application name or version format.");
        return;
    }
    doPing(app, version);
}

private boolean isValidApp(String app) {
    return app.matches("\\w+");
}

private boolean isValidVersion(String version) {
    String[] numbers = version.split("\\.");
    return numbers.length >= 1 && numbers.length <= 4;
}

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
                logger.log(Level.SEVERE, "Failed to send ping.", e);
            }
        }
    }.start();
}

private void actuallySendPing(String app, String version, long id) throws IOException {
    String osName  = URLEncoder.encode(getOsName(),  "UTF-8");
    String osArch  = URLEncoder.encode(getOsArch(),  "UTF-8");
    String jvmArch = URLEncoder.encode(getJvmInfo(), "UTF-8");

    URL url = new URL("https", "tools.google.com", "/service/update?as=androidsdk_" + app +
            "&id=" + Long.toHexString(id) +
            "&version=" + version +
            "&os=" + osName +
            "&osa=" + osArch +
            "&vma=" + jvmArch);

    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    if (conn.getResponseCode() != HttpURLConnection.HTTP_OK &&
        conn.getResponseCode() != HttpURLConnection.HTTP_NOT_FOUND) {
        throw new IOException("Error: " + conn.getResponseMessage() + ": " + url);
    }
}

private String normalizeVersion(String app, String version) {
    if (!isValidApp(app)) {
        throw new IllegalArgumentException("Bad app name.");
    }

    String[] numbers = version.split("\\.");
    if (numbers.length < 1 || numbers.length > 4) {
        throw new IllegalArgumentException("Version must be between 1 and 4 dotted numbers.");
    }
    // Additional normalization logic here
    return version; // Simplified for example
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
    this.mOsArch = osName;
    this.mJavaVersion = osVersion;
}

public void setSystemEnv(String varName, String value) {
    mEnvVars.put(varName, value);
}
// Ensure methods are closed properly
//<End of snippet n. 2>