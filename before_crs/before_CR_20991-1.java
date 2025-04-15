/*Adding the delay time of 1000ms for waiting the dialogue to show up.

Change-Id:Ia96a9dce8e42d8e80faaa99244ed1a4fe92776d9*/
//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/TimePickerDialogTest.java b/tests/tests/app/src/android/app/cts/TimePickerDialogTest.java
//Synthetic comment -- index 01f1ec9..bb0051c 100644

//Synthetic comment -- @@ -132,6 +132,7 @@
public void testOnTimeChanged() throws Throwable {
final int minute = 34;
popDialog(DialogStubActivity.TEST_TIMEPICKERDIALOG);
final TimePickerDialog d = (TimePickerDialog) mActivity.getDialog();

runTestOnUiThread(new Runnable() {







