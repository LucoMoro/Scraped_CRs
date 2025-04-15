/*NullPointerException at handleMessage(GSMPhone.java)

NullPointerException at
com.android.internal.telephony.gsm.GSMPhone.handleMessage(GSMPhone.java)
Failing to retrieved the IMSI number from SIM card could lead to
an exception. A null pointer check will prevent this.

Change-Id:I26760543484504c8d35215bfb1e8f1ae664aeade*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java b/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index 6665632..49de5f9 100644

//Synthetic comment -- @@ -1227,7 +1227,8 @@
// Check if this is a different SIM than the previous one. If so unset the
// voice mail number.
String imsi = getVmSimImsi();
                if (imsi != null && !getSubscriberId().equals(imsi)) {
storeVoiceMailNumber(null);
setVmSimImsi(null);
}







