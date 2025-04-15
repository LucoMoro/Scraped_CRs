/*[contacts] repair some native memory leaks

Native part of CursorWindow are not always freed due to lack of close() calls.

Change-Id:I269f3b0d2a92dd36d722c2f433ceb3693229b74fAuthor: Emmanuel Berthier <emmanuel.berthier@intel.com>
Signed-off-by: Emmanuel Berthier <emmanuel.berthier@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 54273*/
//Synthetic comment -- diff --git a/src/com/android/contacts/calllog/CallLogQueryHandler.java b/src/com/android/contacts/calllog/CallLogQueryHandler.java
//Synthetic comment -- index a6382b6..3b70eb3 100644

//Synthetic comment -- @@ -274,6 +274,7 @@
int requestId = ((Integer) cookie).intValue();
if (requestId != mCallsRequestId) {
// Ignore this query since it does not correspond to the latest request.
return;
}

//Synthetic comment -- @@ -285,6 +286,7 @@
int requestId = ((Integer) cookie).intValue();
if (requestId != mCallsRequestId) {
// Ignore this query since it does not correspond to the latest request.
return;
}

//Synthetic comment -- @@ -297,6 +299,7 @@
return;
} else {
Log.w(TAG, "Unknown query completed: ignoring: " + token);
return;
}

//Synthetic comment -- @@ -343,6 +346,9 @@
if (listener != null) {
listener.onCallsFetched(combinedCursor);
}
}

private void updateVoicemailStatus(Cursor statusCursor) {
//Synthetic comment -- @@ -350,6 +356,9 @@
if (listener != null) {
listener.onVoicemailStatusFetched(statusCursor);
}
}

/** Listener to completion of various queries. */







