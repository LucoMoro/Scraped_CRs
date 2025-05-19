//<Beginning of snippet n. 0>
if (args.length >= 3 && args[0].equals("ping")) {
    stats.ping(args[1], args[2]);
    return;
} else if (args.length > 0) {
    Logger.getLogger(SdkStatsService.class.getName()).warning("Unknown argument: [REDACTED]");
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
import org.eclipse.swt.widgets.Shell;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

private static final long PING_INTERVAL_MSEC = 86400 * 1000;  // 1 day
private DdmsPreferenceStore mStore = new DdmsPreferenceStore();
private ExecutorService executorService = Executors.newFixedThreadPool(2);

public SdkStatsService() {
}

public void ping(String app, String version) {
    doPing(app, version);
}

private void doPing(final String app, String version) {
    final String normalVersion = normalizeVersion(app, version);
    if (!mStore.isPingOptIn()) {
        return;
    }
    executorService.submit(() -> {
        try {
            actuallySendPing(app, normalVersion, id);
        } catch (IOException e) {
            Logger.getLogger(SdkStatsService.class.getName()).severe("Ping failed: An error occurred while sending ping.");
        }
    });
}

private void actuallySendPing(String app, String version, long id) {
    try {
        String osName = URLEncoder.encode(getOsName(), "UTF-8");
        String osArch = URLEncoder.encode(getOsArch(), "UTF-8");
        String jvmArch = URLEncoder.encode(getJvmInfo(), "UTF-8");

        URL url = new URL(
            "https",                                         
            "tools.google.com",                             
            "/service/update?as=androidsdk_" + app +       
                "&id=" + Long.toHexString(id) +             
                "&version=" + version +                     
                "&os=" + osName +                           
                "&osa=" + osArch +                          
                "&vma=" + jvmArch);                         

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_NOT_FOUND) {
            throw new IOException("Unexpected response: [REDACTED]");
        }
    } catch (IOException e) {
        Logger.getLogger(SdkStatsService.class.getName()).severe("Ping failed: An error occurred while sending ping.");
    }
}

private String normalizeVersion(String app, String version) {
    if (!app.matches("[A-Za-z0-9\\-_.]+")) {
        throw new IllegalArgumentException("Bad app name.");         
    }
    String[] numbers = version.split("\\.");
    if (numbers.length < 1 || numbers.length > 4 || !version.matches("\\d+(\\.\\d+){0,3}")) {
        throw new IllegalArgumentException("Bad version format."); 
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

public MockSdkStatsService(String osName, String osVersion, String javaVersion) {
    mJavaVersion = javaVersion;
}

public void setSystemEnv(String varName, String value) {
    mEnvVars.put(varName, value);
}
//<End of snippet n. 2>