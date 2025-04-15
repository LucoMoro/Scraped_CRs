/*Fixes the touch issues related to phone icon on the contactslist

When phone icon in "call log" is long pressed after ending the call,
the touch events are not properly handled - it feels as though
the user clicked a wrong place.

Change-Id:I6793abea7ff99e647c7cd6b0515968ebe5efc9ecSigned-off-by: madan ankapura <mankapur@sta.samsung.com>*/
//Synthetic comment -- diff --git a/src/com/android/contacts/RecentCallsListActivity.java b/src/com/android/contacts/RecentCallsListActivity.java
//Synthetic comment -- index abda325..374681e 100644

//Synthetic comment -- @@ -873,7 +873,7 @@

super.onResume();

        mAdapter.mPreDrawListener = null; // Let it restart the thread after next draw
}

@Override







