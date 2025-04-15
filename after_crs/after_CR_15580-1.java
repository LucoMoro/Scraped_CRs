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

        View test = null;


            test.bringToFront();
    }

    public void testIllegalCharacters() {
        RuntimeException e = new RuntimeException();
        StackTraceElement[] trace = new StackTraceElement[] {
                new StackTraceElement("Hello\0", "World\1", "WTF\3", 0),
        };
        e.setStackTrace(trace);
        throw e;
}

@TestTargetNew(







