/*Remove unused imports from some PTS filesystemperf tests

Remove unused imports from AlmostFullTest.java and RandomRWTest.java in the src.com.android.pts.filesystemperf package.
Signed-off-by: Emma Sajic <esajic@effectivelateralsolutions.co.uk>

Change-Id:I04f3c0ce0c65626a55ff1a4fac7b4f5842192136*/
//Synthetic comment -- diff --git a/suite/pts/deviceTests/filesystemperf/src/com/android/pts/filesystemperf/AlmostFullTest.java b/suite/pts/deviceTests/filesystemperf/src/com/android/pts/filesystemperf/AlmostFullTest.java
//Synthetic comment -- index 89c2f7c..5161697 100644

//Synthetic comment -- @@ -17,18 +17,11 @@
package com.android.pts.filesystemperf;

import android.cts.util.TimeoutReq;
import com.android.pts.util.MeasureRun;
import com.android.pts.util.MeasureTime;
import com.android.pts.util.PtsAndroidTestCase;
import com.android.pts.util.ReportLog;
import com.android.pts.util.Stat;
import com.android.pts.util.SystemUtil;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

//Synthetic comment -- @@ -57,7 +50,8 @@
super.setUp();
if (mDiskFilled.compareAndSet(false, true)) {
Log.i(TAG, "Filling disk");
            // initial fill done in two stage as disk can be filled by other components
long freeDisk = SystemUtil.getFreeDiskSize(getContext());
long diskToFill = freeDisk - FREE_SPACE_FINAL;
Log.i(TAG, "free disk " + freeDisk + ", to fill " + diskToFill);
//Synthetic comment -- @@ -106,8 +100,8 @@
BUFFER_SIZE, NUMBER_REPETITION);
}

    //TODO: file size too small and caching will give wrong better result.
    //      needs to flush cache by reading big files per each read.
@TimeoutReq(minutes = 60)
public void testRandomRead() throws Exception {
final int BUFFER_SIZE = 4 * 1024;








//Synthetic comment -- diff --git a/suite/pts/deviceTests/filesystemperf/src/com/android/pts/filesystemperf/RandomRWTest.java b/suite/pts/deviceTests/filesystemperf/src/com/android/pts/filesystemperf/RandomRWTest.java
//Synthetic comment -- index 537d900..e52af08 100644

//Synthetic comment -- @@ -17,16 +17,8 @@
package com.android.pts.filesystemperf;

import android.cts.util.TimeoutReq;
import com.android.pts.util.MeasureRun;
import com.android.pts.util.PtsAndroidTestCase;
import com.android.pts.util.ReportLog;
import com.android.pts.util.Stat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;


public class RandomRWTest extends PtsAndroidTestCase {







