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
        } else {
Parcel.closeFileDescriptor(mFileDescriptor);
}
}








//Synthetic comment -- diff --git a/core/tests/coretests/src/android/os/OsTests.java b/core/tests/coretests/src/android/os/OsTests.java
//Synthetic comment -- index 582bf1a..4eabd51 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
public static TestSuite suite() {
TestSuite suite = new TestSuite(OsTests.class.getName());

suite.addTestSuite(AidlTest.class);
suite.addTestSuite(BroadcasterTest.class);
suite.addTestSuite(FileObserverTest.class);








//Synthetic comment -- diff --git a/core/tests/coretests/src/android/os/ParcelFileDescriptorTest.java b/core/tests/coretests/src/android/os/ParcelFileDescriptorTest.java
new file mode 100644
//Synthetic comment -- index 0000000..2ce6770

//Synthetic comment -- @@ -0,0 +1,69 @@







