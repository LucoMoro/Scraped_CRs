/*Preserve '+' in phone numbers imported from SIM.

Contacts with phonenumbers beginning with '+' lose the '+' in the
phonebook when imported from SIM.

This was only noticable on ADN-records with unknown NPI-values which
isn't very usual.

Change-Id:I181249759ae3d4181dd3cf627c7a588394b80419*/
//Synthetic comment -- diff --git a/telephony/java/android/telephony/PhoneNumberUtils.java b/telephony/java/android/telephony/PhoneNumberUtils.java
//Synthetic comment -- index 1aa1c76..3f08676 100644

//Synthetic comment -- @@ -689,7 +689,8 @@
return "";
}

        if ((bytes[offset] & 0xff) == TOA_International) {
prependPlus = true;
}









//Synthetic comment -- diff --git a/tests/CoreTests/com/android/internal/telephony/PhoneNumberUtilsTest.java b/tests/CoreTests/com/android/internal/telephony/PhoneNumberUtilsTest.java
//Synthetic comment -- index 20ea4d7..e14240f 100644

//Synthetic comment -- @@ -82,6 +82,16 @@
assertEquals("17005550020",
PhoneNumberUtils.calledPartyBCDToString(b, 0, 7));

b[0] = (byte) 0x91; b[1] = (byte) 0x71; b[2] = (byte) 0x00; b[3] = (byte) 0x55;
b[4] = (byte) 0x05; b[5] = (byte) 0x20; b[6] = (byte) 0xF0;
assertEquals("+17005550020",







