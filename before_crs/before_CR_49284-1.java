/*Telephony: Fix for Failing OMA test SUPL-1.0-con-13

The WAP Push message from an SLP (SLC for non-proxy mode) to a PPG
SHALL contain the SUPL INIT message and SHALL follow [WAP PAP]. And
when the device receive a NI request and normally we shall not answer
to it. Currently this is answered resulting in test failure.

This patch corrects this issue.

Change-Id:Ie4fd6e4d13c0913c62f55a611ad6a53058e17a7eAuthor: jerome Pantaloni <jeromex.pantaloni@intel.com>
Signed-off-by: jerome Pantaloni <jeromex.pantaloni@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 55252*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/WapPushOverSms.java b/src/java/com/android/internal/telephony/WapPushOverSms.java
//Synthetic comment -- index 1b174ab..2847f9f 100755

//Synthetic comment -- @@ -37,6 +37,8 @@
*/
public class WapPushOverSms {
private static final String LOG_TAG = "WAP PUSH";

private final Context mContext;
private WspTypeDecoder pduDecoder;
//Synthetic comment -- @@ -218,6 +220,14 @@
Long.toString(binaryContentType) : mimeType);
if (false) Rlog.v(LOG_TAG, "appid found: " + wapAppId + ":" + contentType);

try {
boolean processFurther = true;
IWapPushManager wapPushMan = mWapConn.getWapPushManager();








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/WspTypeDecoder.java b/src/java/com/android/internal/telephony/WspTypeDecoder.java
//Synthetic comment -- index a19546a..4018c42 100755

//Synthetic comment -- @@ -195,6 +195,7 @@
public static final String CONTENT_TYPE_B_PUSH_CO = "application/vnd.wap.coc";
public static final String CONTENT_TYPE_B_MMS = "application/vnd.wap.mms-message";
public static final String CONTENT_TYPE_B_PUSH_SYNCML_NOTI = "application/vnd.syncml.notification";

byte[] wspData;
int    dataLength;







