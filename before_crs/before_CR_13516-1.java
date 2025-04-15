/*Corrected SPN and PLMN name handling

SPDI-file on SIM and BCD encoded PLMN-entries wasn't parsed correctly
causing us to display wrong networknames.*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccUtils.java b/telephony/java/com/android/internal/telephony/IccUtils.java
//Synthetic comment -- index 3e7094e..4e65020 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -51,6 +52,8 @@
ret.append((char)('0' + v));

v = (data[i] >> 4) & 0xf;
if (v > 9)  break;
ret.append((char)('0' + v));
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java b/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index d711a80..c455d04 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -93,6 +94,7 @@
static final int SPN_RULE_SHOW_PLMN = 0x02;

// From TS 51.011 EF[SPDI] section
static final int TAG_SPDI_PLMN_LIST = 0x80;

// Full Name IEI from TS 24.008
//Synthetic comment -- @@ -1426,8 +1428,12 @@

byte[] plmnEntries = null;

        // There should only be one TAG_SPDI_PLMN_LIST
for ( ; tlv.isValidObject() ; tlv.nextObject()) {
if (tlv.getTag() == TAG_SPDI_PLMN_LIST) {
plmnEntries = tlv.getData();
break;







