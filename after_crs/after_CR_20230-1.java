/*Fix 'New Missed Calls' count in PullvCardListing

When the phone receives a PullvCardListing request about mch
(Missed Call History), PBAP application sends a corresponding
response which contains the number of 'New Missed Calls' in
the header. Currently, the number of 'New Missed Calls' is
included into a response against the other type of
PullvCardListing request after the phone receives mch
PullvCardListing request which contains  MaxListCount = 0 and
ListStartOffset = 0 (these parameters mean PCE only wants the
number of index), because any of PullvCardListing requests
are miss-detected as mch in this case. And then the internal
value for 'New Missed Calls' is also updated, so the number
of 'New Missed Calls' becomes incorrect when answering the
next PullvCardListing about mch.

The solution is to fix the miss detection.

Change-Id:Ic09257fc554d8b7af9498d2ff79e017f6e56510c*/




//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapObexServer.java b/src/com/android/bluetooth/pbap/BluetoothPbapObexServer.java
old mode 100644
new mode 100755
//Synthetic comment -- index 7cd9492..fa645ff

//Synthetic comment -- @@ -741,6 +741,7 @@
ApplicationParameter.TRIPLET_LENGTH.PHONEBOOKSIZE_LENGTH, pbsize);

if (mNeedNewMissedCallsNum) {
                mNeedNewMissedCallsNum = false;
int nmnum = size - mMissedCallSize;
mMissedCallSize = size;








