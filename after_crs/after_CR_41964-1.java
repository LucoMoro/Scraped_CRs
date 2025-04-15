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
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.sdklib.internal.repository;

/**
 * Exception thrown by {@link DownloadCache} and {@link UrlOpener} when a user
 * cancels an HTTP Basic authentication or NTML authentication dialog.
 */
public class CanceledByUserException extends Exception {
    private static final long serialVersionUID = -7669346110926032403L;

    public CanceledByUserException(String message) {
        super(message);
    }
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DownloadCache.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DownloadCache.java
//Synthetic comment -- index e449297..01a2078 100755

//Synthetic comment -- @@ -22,7 +22,6 @@
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.utils.Pair;

import org.apache.http.Header;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/UrlOpener.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/UrlOpener.java
//Synthetic comment -- index dd0761c..72ec65f 100644

//Synthetic comment -- @@ -65,22 +65,20 @@
import java.util.Properties;

/**
 * This class holds static methods for downloading URL resources.
* @see #openUrl(String, boolean, ITaskMonitor, Header[])
 * <p/>
 * Implementation detail: callers should use {@link DownloadCache} instead of this class.
 * {@link DownloadCache#openDirectUrl(String, ITaskMonitor)} is a direct pass-through to
 * {@link UrlOpener} since there's no caching. However from an implementation perspective
 * it's still recommended to pass down a {@link DownloadCache} instance, which will let us
 * override the implementation later on (for testing, for example.)
*/
class UrlOpener {

private static final boolean DEBUG =
System.getenv("ANDROID_DEBUG_URL_OPENER") != null; //$NON-NLS-1$

private static Map<String, UserCredentials> sRealmCache =
new HashMap<String, UserCredentials>();

//Synthetic comment -- @@ -120,6 +118,13 @@
}

/**
     * This class cannot be instantiated.
     * @see #openUrl(String, boolean, ITaskMonitor, Header[])
     */
    private UrlOpener() {
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
import com.android.sdklib.internal.repository.CanceledByUserException;
import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.io.FileOp;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkSource.java
//Synthetic comment -- index bcc45ea..2558e71 100755

//Synthetic comment -- @@ -19,11 +19,10 @@
import com.android.annotations.Nullable;
import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.sdklib.internal.repository.CanceledByUserException;
import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.IDescription;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.packages.AddonPackage;
import com.android.sdklib.internal.repository.packages.DocPackage;
import com.android.sdklib.internal.repository.packages.ExtraPackage;
//Synthetic comment -- @@ -621,7 +620,6 @@
* @param monitor {@link ITaskMonitor} related to this URL.
* @param outException If non null, where to store any exception that
*            happens during the fetch.
*/
private InputStream fetchXmlUrl(String urlString,
DownloadCache cache,







