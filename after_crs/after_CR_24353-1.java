/*add test case on broadcast notification of MediaScanner when it start and finish scaning the external storage

Change-Id:I4cd46718e6ed413a29630a78b3734f90e99f7ed7*/




//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/MediaScannerNotificationTest.java b/tests/tests/media/src/android/media/cts/MediaScannerNotificationTest.java
new file mode 100644
//Synthetic comment -- index 0000000..b0387de

//Synthetic comment -- @@ -0,0 +1,120 @@
/*
 * Copyright (C) 2009 The Android Open Source Project
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

package android.media.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

import android.net.Uri;
import android.test.AndroidTestCase;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;


@TestTargetClass(BroadcastReceiver.class)
public class MediaScannerNotificationTest extends AndroidTestCase {
    private static final int TIME_OUT = 2000;

    private static final String MEDIA_SCANNER_STARTED_ACTION = "android.intent.action.MEDIA_SCANNER_STARTED";
    private static final String MEDIA_SCANNER_FINISHED_ACTION = "android.intent.action.MEDIA_SCANNER_FINISHED";
    private static final String MEDIA_MOUNTED_ACTION = "android.intent.action.MEDIA_MOUNTED";
    private ScannerNotificationReceiver mScannerStartedReceiver;
    private ScannerNotificationReceiver mScannerFinishedReceiver;
    private static boolean mScannerStarted;
    private static boolean mScannerFinished;

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "BroadcastReceiver",
            args = {}
        )
    })
    public void testMediaScannerNotification() throws InterruptedException {
        mScannerStarted = false;
        mScannerFinished = false;

        IntentFilter scannerStartedIntentFilter = new IntentFilter(MEDIA_SCANNER_STARTED_ACTION);
        scannerStartedIntentFilter.addDataScheme("file");
        IntentFilter scannerFinshedIntentFilter = new IntentFilter(MEDIA_SCANNER_FINISHED_ACTION);
        scannerFinshedIntentFilter.addDataScheme("file");

        mScannerStartedReceiver = new ScannerNotificationReceiver(MEDIA_SCANNER_STARTED_ACTION);
        mScannerFinishedReceiver = new ScannerNotificationReceiver(MEDIA_SCANNER_FINISHED_ACTION);

        getContext().registerReceiver(mScannerStartedReceiver, scannerStartedIntentFilter);
        getContext().registerReceiver(mScannerFinishedReceiver, scannerFinshedIntentFilter);

        getContext().sendBroadcast(new Intent(MEDIA_MOUNTED_ACTION, Uri.parse("file://"
            + Environment.getExternalStorageDirectory())));
        mScannerStartedReceiver.waitForCalls(1, TIME_OUT);
        mScannerFinishedReceiver.waitForCalls(1, TIME_OUT);

        assertTrue(mScannerStarted);
        assertTrue(mScannerFinished);
    }

    private static class ScannerNotificationReceiver extends BroadcastReceiver {
        private int mCalls;
        private int mExpectedCalls;
        private String mAction;
        private Object mLock;

        ScannerNotificationReceiver(String action) {
            mAction = action;
            reset();
            mLock = new Object();
        }

        void reset() {
            mExpectedCalls = Integer.MAX_VALUE;
            mCalls = 0;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(mAction)) {
                if (mAction.equals(MEDIA_SCANNER_STARTED_ACTION)) {
                    mScannerStarted = true;
                } else if (mAction
                    .equals(MEDIA_SCANNER_FINISHED_ACTION)) {
                    mScannerFinished = true;
                }
                synchronized (mLock) {
                    mCalls += 1;
                    if (mCalls >= mExpectedCalls) {
                        mLock.notify();
                    }
                }
            }
        }
        
        public void waitForCalls(int expectedCalls, long timeout) throws InterruptedException {
            synchronized(mLock) {
                mExpectedCalls = expectedCalls;
                if (mCalls < mExpectedCalls) {
                    mLock.wait(timeout);
                }
            }
        }
    };
}







