/*Add nullness annotations on overridden methods

Eclipse 4.x requires it.

Change-Id:Iababca14d8edc8d62bce42f23ee26633dc49cf8c*/




//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/ManifestMergerTask.java b/anttasks/src/com/android/ant/ManifestMergerTask.java
//Synthetic comment -- index 00939dc..7339a08 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ant;

import com.android.annotations.NonNull;
import com.android.manifmerger.ICallback;
import com.android.manifmerger.ManifestMerger;
import com.android.manifmerger.MergerLog;
//Synthetic comment -- @@ -132,7 +133,7 @@
new ICallback() {
SdkManager mManager;
@Override
                        public int queryCodenameApiLevel(@NonNull String codename) {
if (mManager == null) {
File sdkDir = TaskHelper.getSdkLocation(getProject());
mManager = SdkManager.createManager(sdkDir.getPath(),








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AdtManifestMergeCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AdtManifestMergeCallback.java
//Synthetic comment -- index 265552b..df0c873 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.sdk;

import com.android.annotations.NonNull;
import com.android.manifmerger.ICallback;
import com.android.manifmerger.ManifestMerger;
import com.android.sdklib.IAndroidTarget;
//Synthetic comment -- @@ -26,7 +27,7 @@
*/
public class AdtManifestMergeCallback implements ICallback {
@Override
    public int queryCodenameApiLevel(@NonNull String codename) {
Sdk sdk = Sdk.getCurrent();
if (sdk != null) {
IAndroidTarget t = sdk.getTargetFromHashString(








//Synthetic comment -- diff --git a/manifmerger/tests/src/com/android/manifmerger/ManifestMergerTestCase.java b/manifmerger/tests/src/com/android/manifmerger/ManifestMergerTestCase.java
//Synthetic comment -- index 8e854f9..8fca091 100755

//Synthetic comment -- @@ -379,7 +379,7 @@
IMergerLog mergerLog = MergerLog.wrapSdkLog(log);
ManifestMerger merger = new ManifestMerger(mergerLog, new ICallback() {
@Override
            public int queryCodenameApiLevel(@NonNull String codename) {
if ("ApiCodename1".equals(codename)) {
return 1;
} else if ("ApiCodename10".equals(codename)) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/MockDownloadCache.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/MockDownloadCache.java
//Synthetic comment -- index 4adb9e2..04f2b04 100755

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.sdkuilib.internal.repository;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.sdklib.internal.repository.CanceledByUserException;
import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.ITaskMonitor;
//Synthetic comment -- @@ -156,9 +158,9 @@
*/
@Override
public Pair<InputStream, HttpResponse> openDirectUrl(
            @NonNull String urlString,
            @Nullable Header[] headers,
            @NonNull ITaskMonitor monitor) throws IOException, CanceledByUserException {

synchronized (mDirectHits) {
Integer count = mDirectHits.get(urlString);







