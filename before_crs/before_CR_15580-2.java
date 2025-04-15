/*Strip Invalid Characters from XML

Sometimes the Dalvik tests print control characters in their
exceptions for known failures. This makes the report unviewable
in FireFox so strip out the control characters before writing
the XML.

Change-Id:I36707b669c0d7f17ed6bdf86f7d4bf1199c8a915*/
//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/cts/ViewTest.java b/tests/tests/view/src/android/view/cts/ViewTest.java
//Synthetic comment -- index 3b9d751..7ce81d3 100644

//Synthetic comment -- @@ -151,6 +151,20 @@
fail("should throw NullPointerException");
} catch (NullPointerException e) {
}
}

@TestTargetNew(








//Synthetic comment -- diff --git a/tools/host/src/com/android/cts/TestSessionLog.java b/tools/host/src/com/android/cts/TestSessionLog.java
//Synthetic comment -- index 0cb997c..ae0d88c 100644

//Synthetic comment -- @@ -523,7 +523,7 @@
testNode.appendChild(failedMessageNode);
setAttribute(doc, failedMessageNode,TAG_FAILED_MESSAGE, failedMessage);

                    String stackTrace = result.getStackTrace();
if (stackTrace != null) {
Node stackTraceNode = doc.createElement(TAG_STACK_TRACE);
failedMessageNode.appendChild(stackTraceNode);
//Synthetic comment -- @@ -542,6 +542,18 @@
}

/**
* Fetch failed file name and line number
*
* @param failedResult failed message







