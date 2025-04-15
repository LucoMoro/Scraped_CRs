/*Don't serialize a Throwable's stackState field.

In some situations this field may reference a non-serializable value.

(cherry-pick of 6c15570a27c2b7aad18ae2064db2eae921be27e0.)

Change-Id:I78607bbcb707fc1ab43cfbaba4647d91b9d90ad4*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/Throwable.java b/luni/src/main/java/java/lang/Throwable.java
//Synthetic comment -- index e88d6ce..31eb538 100644

//Synthetic comment -- @@ -70,7 +70,7 @@
* An intermediate representation of the stack trace.  This field may
* be accessed by the VM; do not rename.
*/
    private volatile Object stackState;

/**
* A fully-expanded representation of the stack trace.







