/*Strip Invalid Characters from XML

Sometimes the Dalvik tests print control characters in their
exceptions for known failures. This makes the report unviewable
in FireFox so strip out the control characters before writing
the XML.

Change-Id:Ifeb4bb5ff04c95e934f32240e9459f9a33cbd545*/




//Synthetic comment -- diff --git a/tools/host/src/com/android/cts/TestSessionLog.java b/tools/host/src/com/android/cts/TestSessionLog.java
//Synthetic comment -- index 0cb997c..ae0d88c 100644

//Synthetic comment -- @@ -523,7 +523,7 @@
testNode.appendChild(failedMessageNode);
setAttribute(doc, failedMessageNode,TAG_FAILED_MESSAGE, failedMessage);

                    String stackTrace = sanitizeStackTrace(result.getStackTrace());
if (stackTrace != null) {
Node stackTraceNode = doc.createElement(TAG_STACK_TRACE);
failedMessageNode.appendChild(stackTraceNode);
//Synthetic comment -- @@ -542,6 +542,18 @@
}

/**
     * Strip out any invalid XML characters that might cause the report to be unviewable.
     * http://www.w3.org/TR/REC-xml/#dt-character
     */
    private static String sanitizeStackTrace(String trace) {
        if (trace != null) {
            return trace.replaceAll("[^\\u0009\\u000A\\u000D\\u0020-\\uD7FF\\uE000-\\uFFFD]", "");
        } else {
            return null;
        }
    }

    /**
* Fetch failed file name and line number
*
* @param failedResult failed message







