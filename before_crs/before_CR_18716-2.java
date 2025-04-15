/*Added check for input parameters in sendPdu

This change is part of change "I8a677afe: Return error code when
composing PDU fails", seehttps://review.source.android.com/17721*/
//Synthetic comment -- diff --git a/src/com/android/mms/transaction/Transaction.java b/src/com/android/mms/transaction/Transaction.java
//Synthetic comment -- index e12299d..d69d3bf 100644

//Synthetic comment -- @@ -162,6 +162,10 @@
*         an HTTP error code(>=400) returned from the server.
*/
protected byte[] sendPdu(long token, byte[] pdu, String mmscUrl) throws IOException {
ensureRouteToHost(mmscUrl, mTransactionSettings);
return HttpUtils.httpConnection(
mContext, token,







