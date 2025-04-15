/*Make CTS host capture JUnit errors too.

Bug 3125695

Change-Id:Id33205f3761f09d97eab9287a6a9ed953e4db09a*/
//Synthetic comment -- diff --git a/tools/host/src/com/android/cts/CtsTestResult.java b/tools/host/src/com/android/cts/CtsTestResult.java
//Synthetic comment -- index 0aea74b3..851b07d 100644

//Synthetic comment -- @@ -157,7 +157,7 @@
int resCode = CODE_PASS;
String failedMessage = null;
String stackTrace = null;
        if ((testResult != null) && (testResult.failureCount() > 0)) {
resCode = CODE_FAIL;
Enumeration<TestFailure> failures = testResult.failures();
while (failures.hasMoreElements()) {
//Synthetic comment -- @@ -165,6 +165,12 @@
failedMessage += failure.exceptionMessage();
stackTrace += failure.trace();
}
}
mResultCode = resCode;
mFailedMessage = failedMessage;







