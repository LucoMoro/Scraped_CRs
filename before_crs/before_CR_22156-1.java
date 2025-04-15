/*To prevent the reference to null pointer

When the 'imsi' obejct is null, the reference to member method using that object should not be executed.
It is possible that the 'imsi' object has null because of the upper code (in case imsi.length() < 6 or imsi.length() > 15)

Change-Id:I54346547a064ef53842ea74ccb20a87e3f380506*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java b/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index 3b133da..ad51d43 100755

//Synthetic comment -- @@ -518,7 +518,9 @@
imsi = null;
}

                Log.d(LOG_TAG, "IMSI: " + imsi.substring(0, 6) + "xxxxxxx");

if (((mncLength == UNKNOWN) || (mncLength == 2)) &&
((imsi != null) && (imsi.length() >= 6))) {







