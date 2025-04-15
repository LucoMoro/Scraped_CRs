/*Adding the delay time of 1000ms for waiting the dialogue to show up.

Change-Id:I6d8e5a45331141e9dcab733293ce11c37845bb7e*/
//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/DialogTest.java b/tests/tests/app/src/android/app/cts/DialogTest.java
//Synthetic comment -- index 6a48358..ee8e2e6 100644

//Synthetic comment -- @@ -244,6 +244,10 @@
})
public void testAccessOwnerActivity() {
popDialog(DialogStubActivity.TEST_DIALOG_WITHOUT_THEME);
Dialog d = mActivity.getDialog();
assertNotNull(d);
assertSame(mActivity, d.getOwnerActivity());







