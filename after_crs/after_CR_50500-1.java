/*fix typo for comment

About version 7, change comment from protocol to bearer.
And change file permission

Change-Id:Ia09403a46a0c800f84c612713f020a9f31bf0205*/




//Synthetic comment -- diff --git a/src/com/android/providers/telephony/TelephonyProvider.java b/src/com/android/providers/telephony/TelephonyProvider.java
old mode 100755
new mode 100644
//Synthetic comment -- index 040a3d7..3f22e2d

//Synthetic comment -- @@ -168,7 +168,7 @@
parser.close();
}

            // Read external APNS data (partner-provided)
XmlPullParser confparser = null;
// Environment.getRootDirectory() is a fancy way of saying ANDROID_ROOT or "/system".
File confFile = new File(Environment.getRootDirectory(), PARTNER_APNS_PATH);
//Synthetic comment -- @@ -226,7 +226,7 @@
oldVersion = 6 << 16 | 6;
}
if (oldVersion < (7 << 16 | 6)) {
                // Add carrier_enabled, bearer fields to the APN. The XML file does not change.
db.execSQL("ALTER TABLE " + CARRIERS_TABLE +
" ADD COLUMN carrier_enabled BOOLEAN DEFAULT 1;");
db.execSQL("ALTER TABLE " + CARRIERS_TABLE +







