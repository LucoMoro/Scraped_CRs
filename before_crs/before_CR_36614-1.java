/*SDK Manager: do not erase default java http proxy properties.

If the sdk manager config file has no proxy port/host
info, do not set the corresponding java properties to
empty strings.

This means if the sdk manager settings are empty, whatever
is the default from Java OR from the Eclispe proxy settings
will be used by the manager.

Change-Id:I17bbc6faed4726fc4b6aa9b29ded7d698ed81283*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/UrlOpener.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/UrlOpener.java
//Synthetic comment -- index 74b023e..84190e8 100644

//Synthetic comment -- @@ -44,12 +44,16 @@
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ProxySelector;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* This class holds methods for adding URLs management.
//Synthetic comment -- @@ -57,6 +61,9 @@
*/
public class UrlOpener {

public static class CanceledByUserException extends Exception {
private static final long serialVersionUID = -7669346110926032403L;

//Synthetic comment -- @@ -68,6 +75,24 @@
private static Map<String, UserCredentials> sRealmCache =
new HashMap<String, UserCredentials>();

/**
* Opens a URL. It can be a simple URL or one which requires basic
* authentication.
//Synthetic comment -- @@ -166,6 +191,23 @@
httpClient.getParams().setParameter(AuthPNames.PROXY_AUTH_PREF, authpref);
httpClient.getParams().setParameter(AuthPNames.TARGET_AUTH_PREF, authpref);

boolean trying = true;
// loop while the response is being fetched
while (trying) {
//Synthetic comment -- @@ -173,6 +215,10 @@
HttpResponse response = httpClient.execute(httpGet, localContext);
int statusCode = response.getStatusLine().getStatusCode();

// check whether any authentication is required
AuthState authenticationState = null;
if (statusCode == HttpStatus.SC_UNAUTHORIZED) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkSource.java
//Synthetic comment -- index 45646f0..c07715b 100755

//Synthetic comment -- @@ -516,10 +516,12 @@
reason = "HTTPS SSL error. You might want to force download through HTTP in the settings.";
mFetchError += ": HTTPS SSL error";
} else if (exception[0].getMessage() != null) {
                reason = exception[0].getMessage();
} else {
                // We don't know what's wrong. Let's give the exception class at least.
                reason = String.format("Unknown (%1$s)", exception[0].getClass().getName());
}

monitor.logError("Failed to fetch URL %1$s, reason: %2$s", url, reason);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsController.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsController.java
//Synthetic comment -- index 672feed..2d8b57e 100755

//Synthetic comment -- @@ -288,10 +288,16 @@
final String JAVA_PROP_HTTPS_PROXY_HOST = "https.proxyHost";     //$NON-NLS-1$
final String JAVA_PROP_HTTPS_PROXY_PORT = "https.proxyPort";     //$NON-NLS-1$

        props.setProperty(JAVA_PROP_HTTP_PROXY_HOST,  proxyHost);
        props.setProperty(JAVA_PROP_HTTP_PROXY_PORT,  proxyPort);
        props.setProperty(JAVA_PROP_HTTPS_PROXY_HOST, proxyHost);
        props.setProperty(JAVA_PROP_HTTPS_PROXY_PORT, proxyPort);
}

}







