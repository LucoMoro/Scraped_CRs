/*traceview: It is a context switch only if id == -1

Currently, method id's are assumed to be positive integer
values, with a context switch receiving the special id -1.
However, it is possible that id's > 0x8000_0000 are
generated, in which case they are represented as negative
integers.

This patch simply fixes the check for whether an id
denotes a context switch to be (id == -1) rather than
(id < 0).

Change-Id:I75ada085f875d8c6bdc54c6f132ba4600633a062*/




//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/Call.java b/traceview/src/com/android/traceview/Call.java
//Synthetic comment -- index f786805..0330b05 100644

//Synthetic comment -- @@ -118,7 +118,7 @@

@Override
public boolean isContextSwitch() {
        return mMethodData.getId() == -1;
}

@Override







