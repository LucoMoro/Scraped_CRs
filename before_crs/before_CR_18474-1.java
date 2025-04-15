/*Workaround failure to collect test info for large test suites.

Add a small delay between tests when collecting test info.

Bug 1796494

Change-Id:I57061d7a21f8c8517c03101e28a3de82cdd61cd8*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/runtime/RemoteAdtTestRunner.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/runtime/RemoteAdtTestRunner.java
//Synthetic comment -- index de3ea38..6d0bfac 100755

//Synthetic comment -- @@ -43,6 +43,10 @@
@SuppressWarnings("restriction")
public class RemoteAdtTestRunner extends RemoteTestRunner {

private AndroidJUnitLaunchInfo mLaunchInfo;
private TestExecution mExecution;

//Synthetic comment -- @@ -104,6 +108,9 @@
// set log only to first collect test case info, so Eclipse has correct test case count/
// tree info
runner.setLogOnly(true);
TestCollector collector = new TestCollector();
try {
AdtPlugin.printToConsole(mLaunchInfo.getProject(), "Collecting test information");
//Synthetic comment -- @@ -124,6 +131,7 @@

// now do real execution
runner.setLogOnly(false);
if (mLaunchInfo.isDebugMode()) {
runner.setDebug(true);
}







