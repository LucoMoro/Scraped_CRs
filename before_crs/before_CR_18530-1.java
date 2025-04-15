/*Remove PhoneNumberUtilsTest Voicemail Check

Bug 3097462

Remove the PhoneNumberUtils.getNumberFromIntent assertion that
changed from 2.2 to 2.2.1 that would not require a private API
call to check.

Change-Id:I0583c7736c458901b6bd23cf8280d4dd567f786b*/
//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/PhoneNumberUtilsTest.java b/tests/tests/telephony/src/android/telephony/cts/PhoneNumberUtilsTest.java
//Synthetic comment -- index 5e45e67..dad3dd6 100644

//Synthetic comment -- @@ -282,12 +282,6 @@
intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+18005555555"));
assertEquals("+18005555555", PhoneNumberUtils.getNumberFromIntent(intent, getContext()));

        intent = new Intent(Intent.ACTION_DIAL, Uri.parse("voicemail:"));
        TelephonyManager tm =
                (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        assertEquals(tm.getVoiceMailNumber(),
                PhoneNumberUtils.getNumberFromIntent(intent, getContext()));

ContentResolver cr = getContext().getContentResolver();
Uri personRecord = null;
Uri phoneRecord = null;







