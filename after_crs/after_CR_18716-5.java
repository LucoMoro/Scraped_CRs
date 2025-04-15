/*Added check for input parameters in sendPdu

This change is part of change "I8a677afe: Return error code when
composing PDU fails", seehttps://review.source.android.com/17721*/




//Synthetic comment -- diff --git a/src/com/android/mms/transaction/Transaction.java b/src/com/android/mms/transaction/Transaction.java
//Synthetic comment -- index e12299d..f3225e3 100644

//Synthetic comment -- @@ -18,6 +18,7 @@
package com.android.mms.transaction;

import com.android.mms.util.SendingProgressTokenManager;
import com.google.android.mms.MmsException;

import android.content.Context;
import android.net.Uri;
//Synthetic comment -- @@ -116,8 +117,9 @@
*         If an HTTP error code is returned, an IOException will be thrown.
* @throws IOException if any error occurred on network interface or
*         an HTTP error code(>=400) returned from the server.
     * @throws MmsException if pdu is null.
*/
    protected byte[] sendPdu(byte[] pdu) throws IOException, MmsException {
return sendPdu(SendingProgressTokenManager.NO_TOKEN, pdu,
mTransactionSettings.getMmscUrl());
}
//Synthetic comment -- @@ -131,8 +133,9 @@
*         If an HTTP error code is returned, an IOException will be thrown.
* @throws IOException if any error occurred on network interface or
*         an HTTP error code(>=400) returned from the server.
     * @throws MmsException if pdu is null.
*/
    protected byte[] sendPdu(byte[] pdu, String mmscUrl) throws IOException, MmsException {
return sendPdu(SendingProgressTokenManager.NO_TOKEN, pdu, mmscUrl);
}

//Synthetic comment -- @@ -145,8 +148,9 @@
*         If an HTTP error code is returned, an IOException will be thrown.
* @throws IOException if any error occurred on network interface or
*         an HTTP error code(>=400) returned from the server.
     * @throws MmsException if pdu is null.
*/
    protected byte[] sendPdu(long token, byte[] pdu) throws IOException, MmsException {
return sendPdu(token, pdu, mTransactionSettings.getMmscUrl());
}

//Synthetic comment -- @@ -160,8 +164,14 @@
*         If an HTTP error code is returned, an IOException will be thrown.
* @throws IOException if any error occurred on network interface or
*         an HTTP error code(>=400) returned from the server.
     * @throws MmsException if pdu is null.
*/
    protected byte[] sendPdu(long token, byte[] pdu,
            String mmscUrl) throws IOException, MmsException {
        if (pdu == null) {
            throw new MmsException();
        }

ensureRouteToHost(mmscUrl, mTransactionSettings);
return HttpUtils.httpConnection(
mContext, token,







