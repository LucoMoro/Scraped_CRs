/*Fix of tearDown() in SingleLaunchActivityTestCase.

Before a suite of SingleLaunchActivityTestCase tests are run,
an activity is started. The activity is supposed to close
when all the tests in the suite have run. The activity is
however finished one test too early. This causes problems
for the last test.

Change-Id:Ie9fb2df350a70f827495a0dc30952e30fbe491e0*/




//Synthetic comment -- diff --git a/test-runner/src/android/test/SingleLaunchActivityTestCase.java b/test-runner/src/android/test/SingleLaunchActivityTestCase.java
//Synthetic comment -- index b63b3ce..79c554a 100644

//Synthetic comment -- @@ -75,7 +75,7 @@
protected void tearDown() throws Exception {
// If it is the last test case, call finish on the activity.
sTestCaseCounter --;
        if (sTestCaseCounter == 0) {
sActivity.finish();
}        
super.tearDown();







