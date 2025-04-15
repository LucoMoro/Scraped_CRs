/*Fix Force Close when enable airplane mode

In GSMPhone handler for message EVENT_RADIO_OFF_OR_NOT_AVAILABLE,
the for loop was trying to remove all pending MMI code starting
from the index 0 of the ArrayList mPendingMMIs. When mPendingMMIs
has more than 1 item in the list, after the 1st one in the list was
removed, the rest in the list were shifted. The 2nd one became 1st.
Assume the list size is 2, if now the for loop goes to index 1,
access to mPendingMMIs.get(1) will result a null pointer access,
and cause a Force Close.

To fix it, make the for loop to begin with the last one in the
ArrayList mPendingMMIs.

Change-Id:I3e60086186851b1d6c10fefdb086aa0ae3e16048*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java b/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index e1f4c4b..5c95e7d 100644

//Synthetic comment -- @@ -1240,7 +1240,7 @@
// If the radio shuts off or resets while one of these
// is pending, we need to clean up.

                for (int i = 0, s = mPendingMMIs.size() ; i < s; i++) {
if (mPendingMMIs.get(i).isPendingUSSD()) {
mPendingMMIs.get(i).onUssdFinishedError();
}







