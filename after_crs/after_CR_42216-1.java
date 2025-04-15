/*ParcelFileDescriptor finalization crash

This fix prevents a crash that could happen if a ParcelFileDescriptor
was created with a null reference as a parameter to its constructor.
This would cause the contructor to throw a NPE and leave the
ParcelFileDescriptor instance in an uninitialized state. The crash
happened when the instance was later finalized.

Change-Id:Id2abda84e043335c66d75e343cba539ba093b896*/




//Synthetic comment -- diff --git a/core/java/android/os/ParcelFileDescriptor.java b/core/java/android/os/ParcelFileDescriptor.java
//Synthetic comment -- index 3e90dfc..aaa3f99 100644

//Synthetic comment -- @@ -316,7 +316,7 @@
// If this is a proxy to another file descriptor, just call through to its
// close method.
mParcelDescriptor.close();
        } else if (mFileDescriptor != null ) {
Parcel.closeFileDescriptor(mFileDescriptor);
}
}








//Synthetic comment -- diff --git a/core/tests/coretests/src/android/os/OsTests.java b/core/tests/coretests/src/android/os/OsTests.java
//Synthetic comment -- index 582bf1a..4eabd51 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
public static TestSuite suite() {
TestSuite suite = new TestSuite(OsTests.class.getName());

        suite.addTestSuite(ParcelFileDescriptorTest.class);
suite.addTestSuite(AidlTest.class);
suite.addTestSuite(BroadcasterTest.class);
suite.addTestSuite(FileObserverTest.class);








//Synthetic comment -- diff --git a/core/tests/coretests/src/android/os/ParcelFileDescriptorTest.java b/core/tests/coretests/src/android/os/ParcelFileDescriptorTest.java
new file mode 100644
//Synthetic comment -- index 0000000..2ce6770

//Synthetic comment -- @@ -0,0 +1,69 @@
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

package android.os;

import android.os.ParcelFileDescriptor;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.AndroidTestCase;
import dalvik.system.VMRuntime;

public class ParcelFileDescriptorTest extends AndroidTestCase {

    private final Object waiter = new Object();

    private boolean finalizerHasRun;

    /*
     * Tests that there are no crashes when running the finalizer if the
     * ParcelFileDescriptor was created using a null reference.
     */
    @MediumTest
    public void testNullFinalization() throws Exception {
        finalizerHasRun = false;
        try {
            ParcelFileDescriptor nullReference = null;
            ExtendedParcelFileDescriptor pfd = new ExtendedParcelFileDescriptor(nullReference);
            fail("Expected NullPointerException was not thrown.");
        } catch (NullPointerException e) {
            // Expected exception received.
        }
        System.gc();
        final VMRuntime runtime = VMRuntime.getRuntime();
        runtime.runFinalizationSync();

        synchronized (waiter) {
            // Wait for heap worker thread to run.
            waiter.wait(1000);
            assertTrue(finalizerHasRun);
        }
    }

    class ExtendedParcelFileDescriptor extends ParcelFileDescriptor {
        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            finalizerHasRun = true;
            synchronized (waiter) {
                waiter.notify();
            }
        }

        public ExtendedParcelFileDescriptor(ParcelFileDescriptor descriptor) {
            super(descriptor);
        }
    }
}







