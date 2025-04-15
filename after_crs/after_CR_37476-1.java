/*Telephony: Fix SmsManager to throw IllegalArgumentException

IllegalArgumentException condition for:
enableCellBroadcastRange() and disableCellBroadcastRange() is
if endMessageId < startMessageId.

IllegalArgumentException condition for divideMessage() is if input text
is null, for copyMessageToIcc() it is if pdu is null.

Change-Id:Ib75ab4bef137bc9265cd219ed3de5d5e8d387266*/




//Synthetic comment -- diff --git a/telephony/java/android/telephony/SmsManager.java b/telephony/java/android/telephony/SmsManager.java
//Synthetic comment -- index 44bdaeb..deffebc 100644

//Synthetic comment -- @@ -98,8 +98,13 @@
* @param text the original message.  Must not be null.
* @return an <code>ArrayList</code> of strings that, in order,
*   comprise the original message
     *
     * @throws IllegalArgumentException if text is null
*/
public ArrayList<String> divideMessage(String text) {
        if (null == text) {
            throw new IllegalArgumentException("text is null");
        }
return SmsMessage.fragmentText(text);
}

//Synthetic comment -- @@ -242,11 +247,15 @@
*               STATUS_ON_ICC_SENT, STATUS_ON_ICC_UNSENT)
* @return true for success
*
     * @throws IllegalArgumentException if pdu is NULL
* {@hide}
*/
public boolean copyMessageToIcc(byte[] smsc, byte[] pdu, int status) {
boolean success = false;

        if (null == pdu) {
            throw new IllegalArgumentException("pdu is NULL");
        }
try {
ISms iccISms = ISms.Stub.asInterface(ServiceManager.getService("isms"));
if (iccISms != null) {
//Synthetic comment -- @@ -414,11 +423,15 @@
* @return true if successful, false otherwise
* @see #disableCellBroadcastRange(int, int)
*
     * @throws IllegalArgumentException if endMessageId < startMessageId
* {@hide}
*/
public boolean enableCellBroadcastRange(int startMessageId, int endMessageId) {
boolean success = false;

        if (endMessageId < startMessageId) {
            throw new IllegalArgumentException("endMessageId < startMessageId");
        }
try {
ISms iccISms = ISms.Stub.asInterface(ServiceManager.getService("isms"));
if (iccISms != null) {
//Synthetic comment -- @@ -445,11 +458,15 @@
*
* @see #enableCellBroadcastRange(int, int)
*
     * @throws IllegalArgumentException if endMessageId < startMessageId
* {@hide}
*/
public boolean disableCellBroadcastRange(int startMessageId, int endMessageId) {
boolean success = false;

        if (endMessageId < startMessageId) {
            throw new IllegalArgumentException("endMessageId < startMessageId");
        }
try {
ISms iccISms = ISms.Stub.asInterface(ServiceManager.getService("isms"));
if (iccISms != null) {







