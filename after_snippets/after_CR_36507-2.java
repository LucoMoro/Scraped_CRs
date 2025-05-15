
//<Beginning of snippet n. 0>


// the "ping" argument means to check in with the server and exit
// the application name and version number must also be supplied
if (args.length >= 3 && args[0].equals("ping")) {
            stats.ping(args);
return;
} else if (args.length > 0) {
Log.e("ddms", "Unknown argument: " + args[0]);

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


import org.eclipse.swt.widgets.Shell;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Minimum interval between ping, in milliseconds. */
private static final long PING_INTERVAL_MSEC = 86400 * 1000;  // 1 day

    private static final boolean DEBUG = System.getenv("ANDROID_DEBUG_PING") != null; //$NON-NLS-1$

private DdmsPreferenceStore mStore = new DdmsPreferenceStore();

public SdkStatsService() {

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
                extras.put("glm", sanitizeGlArg(arguments[3])); //$NON-NLS-1$ vendor
            }
            if (len > 4) {
                extras.put("glr", sanitizeGlArg(arguments[4])); //$NON-NLS-1$ renderer
            }
            if (len > 5) {
                extras.put("glv", sanitizeGlArg(arguments[5])); //$NON-NLS-1$ version
            }
        }

        doPing(app, version, extras);
    }

    private String sanitizeGlArg(String arg) {
        if (arg == null) {
        arg = "";                                                   //$NON-NLS-1$
        } else {
            try {
                arg = arg.trim();
                arg = arg.replaceAll("[^A-Za-z0-9\\s_()./-]", " "); //$NON-NLS-1$ //$NON-NLS-2$
                arg = arg.replaceAll("\\s\\s+", " ");               //$NON-NLS-1$ //$NON-NLS-2$

                // Guard from arbitrarily long parameters
                if (arg.length() > 128) {
                    arg = arg.substring(0, 128);
                }

                arg = URLEncoder.encode(arg, "UTF-8");              //$NON-NLS-1$
            } catch (UnsupportedEncodingException e) {
                arg = "";                                           //$NON-NLS-1$
            }
        }

        return arg;
}

/**
/**
* Pings the usage stats server, as long as the prefs contain the opt-in boolean
*
     * @param app The application name that reports the ping (e.g. "emulator" or "ddms".)
     *          Will be normalized.  Valid characters are a-zA-Z0-9 only.
     * @param version The version string (e.g. "12" or "1.2.3.4", 4 groups max.)
     * @param extras Extra key/value parameters to send. They are send as-is and must
     *  already be well suited and escaped using {@link URLEncoder#encode(String, String)}.
*/
    protected void doPing(String app, String version, final Map<String, String> extras) {
        // Note: if you change the implementation here, you also need to change
        // the overloaded SdkStatsServiceTest.doPing() used for testing.

// Validate the application and version input.
        final String nApp = normalizeAppName(app);
        final String nVersion = normalizeVersion(version);

// If the user has not opted in, do nothing and quietly return.
if (!mStore.isPingOptIn()) {
@Override
public void run() {
try {
                    URL url = createPingUrl(nApp, nVersion, id, extras);
                    actuallySendPing(url);
} catch (IOException e) {
e.printStackTrace();
}


/**
     * Unconditionally send a "ping" request to the server.
*
     * @param url The URL to send to the server.
     * * @throws IOException if the ping failed
*/
    private void actuallySendPing(URL url) throws IOException {
        assert url != null;

        if (DEBUG) {
            System.err.println("Ping: " + url.toString());          //$NON-NLS-1$
        }

// Discard the actual response, but make sure it reads OK
HttpURLConnection conn = (HttpURLConnection) url.openConnection();
if (conn.getResponseCode() != HttpURLConnection.HTTP_OK &&
conn.getResponseCode() != HttpURLConnection.HTTP_NOT_FOUND) {
throw new IOException(
                conn.getResponseMessage() + ": " + url);            //$NON-NLS-1$
}
}

/**
     * Compute the ping URL to send the data to the server.
     *
     * @param app The application name that reports the ping (e.g. "emulator" or "ddms".)
     *          Valid characters are a-zA-Z0-9 only.
     * @param version The version string already formatted as a 4 dotted group (e.g. "1.2.3.4".)
     * @param id of the local installation
     * @param extras Extra key/value parameters to send. They are send as-is and must
     *  already be well suited and escaped using {@link URLEncoder#encode(String, String)}.
     */
    protected URL createPingUrl(String app, String version, long id, Map<String, String> extras)
            throws UnsupportedEncodingException, MalformedURLException {

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
        return url;
    }

    /**
* Detects and reports the host OS: "linux", "win" or "mac".
* For Windows and Mac also append the version, so for example
* Win XP will return win-5.1.
}

/**
     * Normalize the supplied application name.
     *
* @param app to report
     */
    protected String normalizeAppName(String app) {
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
    protected String normalizeVersion(String version) {

// Version must be between 1 and 4 dotted numbers
String[] numbers = version.split("\\.");                                //$NON-NLS-1$

//<End of snippet n. 1>










//<Beginning of snippet n. 2>



package com.android.sdkstats;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

private final String mOsArch;
private final String mJavaVersion;
private final Map<String, String> mEnvVars = new HashMap<String, String>();
        private URL mPingUrlResult;

public MockSdkStatsService(String osName,
String osVersion,
mJavaVersion = javaVersion;
}

        public URL getPingUrlResult() {
            return mPingUrlResult;
        }

public void setSystemEnv(String varName, String value) {
mEnvVars.put(varName, value);
}
return null;
}

        @Override
        protected void doPing(String app, String version,
                Map<String, String> extras) {
            // The super.doPing() does:
            // 1- normalize input,
            // 2- check the ping time,
            // 3- check/create the pind id,
            // 4- create the ping URL
            // 5- and send the network ping in a thread.
            // In this mock version we just do steps 1 and 4 and record the URL;
            // obvious we don't check the ping time in the prefs nor send the actual ping.

            // Validate the application and version input.
            final String nApp = normalizeAppName(app);
            final String nVersion = normalizeVersion(version);

            long id = 0x42;
            try {
                mPingUrlResult = createPingUrl(nApp, nVersion, id, extras);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
}

@Override
"42", "x86_64", "1.7.8_09");
assertEquals("one3456789ten3456789twenty6789th", m.getOsName());
}

    public void testSdkStatsService_glPing() {
        MockSdkStatsService m;
        m = new MockSdkStatsService("Windows", "6.2", "x86_64", "1.7.8_09");

        // Send emulator ping with just emulator version, no GL stuff
        m.ping("emulator", "12");
        assertEquals(
                "http://tools.google.com/service/update?" +
                "as=androidsdk_emulator&" +
                "id=42&" +
                "version=12.0.0.0&" +
                "os=win-6.2&" +
                "osa=x86_64&" +
                "vma=1.7-x86_64",
                m.getPingUrlResult().toString());

        // Send emulator ping with just emulator version, no GL stuff.
        // This is the same request but using the variable string list API, arg 0 is the "ping" app.
        m.ping(new String[] { "ping", "emulator", "12" });
        assertEquals(
                "http://tools.google.com/service/update?" +
                "as=androidsdk_emulator&" +
                "id=42&" +
                "version=12.0.0.0&" +
                "os=win-6.2&" +
                "osa=x86_64&" +
                "vma=1.7-x86_64",
                m.getPingUrlResult().toString());

        // Send a ping for a non-emulator app with extra parameters, no GL stuff
        m.ping(new String[] { "ping", "not-emulator", "12", "arg1", "arg2", "arg3" });
        assertEquals(
                "http://tools.google.com/service/update?" +
                "as=androidsdk_notemulator&" +
                "id=42&" +
                "version=12.0.0.0&" +
                "os=win-6.2&" +
                "osa=x86_64&" +
                "vma=1.7-x86_64",
                m.getPingUrlResult().toString());

        // Send a ping for the emulator app with extra parameters, GL stuff is added, 3 parameters
        m.ping(new String[] { "ping", "emulator", "12", "Vendor Inc.", "Some cool_GPU!!! (fast one!)", "1.2.3.4_preview" });
        assertEquals(
                "http://tools.google.com/service/update?" +
                "as=androidsdk_emulator&" +
                "id=42&" +
                "version=12.0.0.0&" +
                "os=win-6.2&" +
                "osa=x86_64&" +
                "vma=1.7-x86_64&" +
                "glm=Vendor+Inc.&" +
                "glr=Some+cool_GPU+%28fast+one+%29&" +
                "glv=1.2.3.4_preview",
                m.getPingUrlResult().toString());

        // Send a ping for the emulator app with extra parameters, GL stuff is added, 2 parameters
        m.ping(new String[] { "ping", "emulator", "12", "Vendor Inc.", "Some cool_GPU!!! (fast one!)" });
        assertEquals(
                "http://tools.google.com/service/update?" +
                "as=androidsdk_emulator&" +
                "id=42&" +
                "version=12.0.0.0&" +
                "os=win-6.2&" +
                "osa=x86_64&" +
                "vma=1.7-x86_64&" +
                "glm=Vendor+Inc.&" +
                "glr=Some+cool_GPU+%28fast+one+%29",
                m.getPingUrlResult().toString());

        // Send a ping for the emulator app with extra parameters, GL stuff is added, 1 parameter
        m.ping(new String[] { "ping", "emulator", "12", "Vendor Inc." });
        assertEquals(
                "http://tools.google.com/service/update?" +
                "as=androidsdk_emulator&" +
                "id=42&" +
                "version=12.0.0.0&" +
                "os=win-6.2&" +
                "osa=x86_64&" +
                "vma=1.7-x86_64&" +
                "glm=Vendor+Inc.",
                m.getPingUrlResult().toString());

        // Parameters that are more than 128 chars are cut short.
        m.ping(new String[] { "ping", "emulator", "12",
                // 130 chars each
                "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789",
                "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789",
                "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" });
        assertEquals(
                "http://tools.google.com/service/update?" +
                "as=androidsdk_emulator&" +
                "id=42&" +
                "version=12.0.0.0&" +
                "os=win-6.2&" +
                "osa=x86_64&" +
                "vma=1.7-x86_64&" +
                "glm=01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567&" +
                "glr=01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567&" +
                "glv=01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567",
                m.getPingUrlResult().toString());
    }
}

//<End of snippet n. 2>








