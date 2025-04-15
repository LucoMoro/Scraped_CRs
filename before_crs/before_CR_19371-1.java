/*Nuke Problematic ContextWrapperTest

Change-Id:I8cfef23594ff550ecfc903ddb1e7becb08af79ba*/
//Synthetic comment -- diff --git a/tests/tests/content/src/android/content/cts/ContextWrapperTest.java b/tests/tests/content/src/android/content/cts/ContextWrapperTest.java
//Synthetic comment -- index 265ec06..ac402ef 100644

//Synthetic comment -- @@ -54,13 +54,9 @@
import android.view.animation.cts.DelayedCheck;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
//Synthetic comment -- @@ -353,111 +349,6 @@
mContextWrapper.unregisterReceiver(broadcastReceiver);
}

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "fileList",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getFilesDir",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "openFileOutput",
            args = {String.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "deleteFile",
            args = {String.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "openFileInput",
            args = {String.class}
        )
    })
    @BrokenTest(value="bug 2323047")
    public void testAccessOfFiles() throws IOException, FileNotFoundException {
        int TEST_LENGTH = 10;
        String[] fileLst;
        ArrayList<String> filenameList = new ArrayList<String>();
        String filePath;

        // Test getFilesDir()
        filePath = mContextWrapper.getFilesDir().toString();
        assertNotNull(filePath);

        int originalNumFiles = mContextWrapper.fileList().length;

        // Build test datum
        byte[][] buffers = new byte[3][];
        for (int i = 0; i < 3; i++) {
            buffers[i] = new byte[TEST_LENGTH];
            Arrays.fill(buffers[i], (byte) (i + 1));
        }

        String tmpName = "";
        // Test openFileOutput
        FileOutputStream os = null;
        for (int i = 1; i < 4; i++) {
            try {
                tmpName = "contexttest" + i;
                os = mContextWrapper.openFileOutput(tmpName, ContextWrapper.MODE_WORLD_WRITEABLE);
                os.write(buffers[i - 1]);
                os.flush();
                filenameList.add(tmpName);
            } finally {
                if (null != os) {
                    try {
                        os.close();
                    } catch (IOException e1) {
                    }
                }
            }
        }

        byte[] testBuffer = new byte[TEST_LENGTH];
        // Test openFileInput(String)
        FileInputStream fileIS[] = { null, null, null };
        try {
            for (int i = 0; i < 3; i++) {
                fileIS[i] = mContextWrapper.openFileInput("contexttest" + (i + 1));
                assertNotNull(fileIS[i]);
                fileIS[i].read(testBuffer);
                assertTrue(Arrays.equals(buffers[i], testBuffer));
            }
        } finally {
            for (int i = 0; i < 3; i++) {
                if (null != fileIS[i]) {
                    try {
                        fileIS[i].close();
                    } catch (IOException e1) {
                    }
                }
            }
        }

        // Test fileList()
        fileLst = mContextWrapper.fileList();
        assertEquals(originalNumFiles + 3, fileLst.length);

        List<String> list = Arrays.asList(fileLst);
        assertTrue(list.contains("contexttest1"));
        assertTrue(list.contains("contexttest2"));
        assertTrue(list.contains("contexttest3"));

        for (String file: fileLst) {
            // Test deleteFile(String)
            assertTrue(mContextWrapper.deleteFile(file));
        }
        fileLst = mContextWrapper.fileList();
        assertEquals(originalNumFiles, fileLst.length);
    }

@TestTargetNew(
level = TestLevel.COMPLETE,
method = "enforceCallingOrSelfPermission",







