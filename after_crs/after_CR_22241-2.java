/*Add DownloadManager Test

Bug 3030013

Exercise DownloadManager's enqueue, query, remove methods, and
the ACTION_DOWNLOAD_COMPLETE broadcast receiver.

Change-Id:I93202e99850a9225fcbe76408a59be0b97df5fcc*/




//Synthetic comment -- diff --git a/tests/src/android/webkit/cts/CtsTestServer.java b/tests/src/android/webkit/cts/CtsTestServer.java
//Synthetic comment -- index 075f98d..49603bc 100644

//Synthetic comment -- @@ -79,6 +79,7 @@

public static final String FAVICON_PATH = "/favicon.ico";
public static final String USERAGENT_PATH = "/useragent.html";
    public static final String TEST_DOWNLOAD_PATH = "/download.html";
public static final String ASSET_PREFIX = "/assets/";
public static final String FAVICON_ASSET_PATH = ASSET_PREFIX + "webkit/favicon.png";
public static final String APPCACHE_PATH = "/appcache.html";
//Synthetic comment -- @@ -350,6 +351,10 @@
return sb.toString();
}

    public String getTestDownloadUrl() {
        return getBaseUri() + TEST_DOWNLOAD_PATH;
    }

public String getLastRequestUrl() {
return mLastQuery;
}
//Synthetic comment -- @@ -503,6 +508,10 @@
}
response.setEntity(createEntity("<html><head><title>" + agent + "</title></head>" +
"<body>" + agent + "</body></html>"));
        } else if (path.equals(TEST_DOWNLOAD_PATH)) {
            response = createResponse(HttpStatus.SC_OK);
            response.setHeader("Content-Length", "0");
            response.setEntity(createEntity(""));
} else if (path.equals(SHUTDOWN_PREFIX)) {
response = createResponse(HttpStatus.SC_OK);
// We cannot close the socket here, because we need to respond.








//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/DownloadManagerTest.java b/tests/tests/app/src/android/app/cts/DownloadManagerTest.java
new file mode 100644
//Synthetic comment -- index 0000000..0b3e62c

//Synthetic comment -- @@ -0,0 +1,159 @@
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
package android.app.cts;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.view.animation.cts.DelayedCheck;
import android.webkit.cts.CtsTestServer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class DownloadManagerTest extends AndroidTestCase {

    private DownloadManager mDownloadManager;

    private CtsTestServer mWebServer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        mWebServer = new CtsTestServer(mContext);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mWebServer.shutdown();
    }

    public void testDownloadManager() throws Exception {
        DownloadCompleteReceiver receiver = new DownloadCompleteReceiver();
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        mContext.registerReceiver(receiver, intentFilter);

        try {
            long goodId = mDownloadManager.enqueue(new Request(getGoodUrl()));
            long badId = mDownloadManager.enqueue(new Request(getBadUrl()));

            int allDownloads = getTotalNumberDownloads();
             assertTrue(allDownloads >= 2);

            assertDownloadQueryableById(goodId);
            assertDownloadQueryableById(badId);

            receiver.waitForDownloadComplete();

            assertDownloadQueryableByStatus(DownloadManager.STATUS_SUCCESSFUL);
            assertDownloadQueryableByStatus(DownloadManager.STATUS_FAILED);

            assertRemoveDownload(goodId, allDownloads - 1);
            assertRemoveDownload(badId, allDownloads - 2);
        } finally {
            mContext.unregisterReceiver(receiver);
        }
    }

    private class DownloadCompleteReceiver extends BroadcastReceiver {

        private final CountDownLatch mReceiveLatch = new CountDownLatch(1);

        @Override
        public void onReceive(Context context, Intent intent) {
            mReceiveLatch.countDown();
        }

        public void waitForDownloadComplete() throws InterruptedException {
            assertTrue(mReceiveLatch.await(3, TimeUnit.SECONDS));
        }
    }

    private Uri getGoodUrl() {
        return Uri.parse(mWebServer.getTestDownloadUrl());
    }

    private Uri getBadUrl() {
        return Uri.parse(mWebServer.getBaseUri() + "/nosuchurl");
    }

    private int getTotalNumberDownloads() {
        Cursor cursor = null;
        try {
            Query query = new Query();
            cursor = mDownloadManager.query(query);
            return cursor.getCount();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void assertDownloadQueryableById(long downloadId) {
        Cursor cursor = null;
        try {
            Query query = new Query().setFilterById(downloadId);
            cursor = mDownloadManager.query(query);
            assertEquals(1, cursor.getCount());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void assertDownloadQueryableByStatus(final int status) {
        new DelayedCheck() {
            @Override
            protected boolean check() {
                Cursor cursor= null;
                try {
                    Query query = new Query().setFilterByStatus(status);
                    cursor = mDownloadManager.query(query);
                    return 1 == cursor.getCount();
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        }.run();
    }

    private void assertRemoveDownload(long removeId, int expectedNumDownloads) {
        Cursor cursor = null;
        try {
            assertEquals(1, mDownloadManager.remove(removeId));
            Query query = new Query();
            cursor = mDownloadManager.query(query);
            assertEquals(expectedNumDownloads, cursor.getCount());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}







