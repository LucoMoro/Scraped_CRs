/*SDK Lib: minor cleanup in UrlOpener.

Makes UrlOpener private. All callers should use the
DownloadCache class, and UrlOpener must remain
purely an implementation detail.
That's because UrlOpener is just a bunch of static
whereas DownloadCache is an instance that gets passed
around and can be overriden for testing.

Change-Id:Idacd328616f6c11df298f4e2b8f4ac7668cf6ffd*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/CanceledByUserException.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/CanceledByUserException.java
new file mode 100755
//Synthetic comment -- index 0000000..a0a74d8

//Synthetic comment -- @@ -0,0 +1,30 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DownloadCache.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DownloadCache.java
//Synthetic comment -- index e449297..01a2078 100755

//Synthetic comment -- @@ -22,7 +22,6 @@
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.internal.repository.UrlOpener.CanceledByUserException;
import com.android.utils.Pair;

import org.apache.http.Header;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/UrlOpener.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/UrlOpener.java
//Synthetic comment -- index dd0761c..72ec65f 100644

//Synthetic comment -- @@ -65,22 +65,20 @@
import java.util.Properties;

/**
 * This class holds methods for adding URLs management.
* @see #openUrl(String, boolean, ITaskMonitor, Header[])
*/
public class UrlOpener {

private static final boolean DEBUG =
System.getenv("ANDROID_DEBUG_URL_OPENER") != null; //$NON-NLS-1$

    public static class CanceledByUserException extends Exception {
        private static final long serialVersionUID = -7669346110926032403L;

        public CanceledByUserException(String message) {
            super(message);
        }
    }

private static Map<String, UserCredentials> sRealmCache =
new HashMap<String, UserCredentials>();

//Synthetic comment -- @@ -120,6 +118,13 @@
}

/**
* Opens a URL. It can be a simple URL or one which requires basic
* authentication.
* <p/>








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/archives/ArchiveInstaller.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/archives/ArchiveInstaller.java
//Synthetic comment -- index 612f74e..e7c10d4 100755

//Synthetic comment -- @@ -21,9 +21,9 @@
import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.UrlOpener.CanceledByUserException;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.io.FileOp;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkSource.java
//Synthetic comment -- index bcc45ea..2558e71 100755

//Synthetic comment -- @@ -19,11 +19,10 @@
import com.android.annotations.Nullable;
import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.IDescription;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.UrlOpener;
import com.android.sdklib.internal.repository.UrlOpener.CanceledByUserException;
import com.android.sdklib.internal.repository.packages.AddonPackage;
import com.android.sdklib.internal.repository.packages.DocPackage;
import com.android.sdklib.internal.repository.packages.ExtraPackage;
//Synthetic comment -- @@ -621,7 +620,6 @@
* @param monitor {@link ITaskMonitor} related to this URL.
* @param outException If non null, where to store any exception that
*            happens during the fetch.
     * @see UrlOpener UrlOpener, which handles all URL logic.
*/
private InputStream fetchXmlUrl(String urlString,
DownloadCache cache,







