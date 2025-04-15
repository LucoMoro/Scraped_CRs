/*Corrected SPN and PLMN name handling

SPDI-file on SIM and BCD encoded PLMN-entries wasn't parsed correctly
causing us to display wrong networknames.

Change-Id:I09d80deec45b4b3ad525a9359b4866de7549e39e*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccUtils.java b/telephony/java/com/android/internal/telephony/IccUtils.java
//Synthetic comment -- index 3e7094e..1034bda 100644

//Synthetic comment -- @@ -51,6 +51,8 @@
ret.append((char)('0' + v));

v = (data[i] >> 4) & 0xf;
            // Some PLMNs have 'f' as high nibble, ignore it
            if (v == 0xf) continue;
if (v > 9)  break;
ret.append((char)('0' + v));
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java b/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index d711a80..d99a348 100644

//Synthetic comment -- @@ -93,6 +93,7 @@
static final int SPN_RULE_SHOW_PLMN = 0x02;

// From TS 51.011 EF[SPDI] section
    static final int TAG_SPDI = 0xA3;
static final int TAG_SPDI_PLMN_LIST = 0x80;

// Full Name IEI from TS 24.008
//Synthetic comment -- @@ -1426,8 +1427,12 @@

byte[] plmnEntries = null;

for ( ; tlv.isValidObject() ; tlv.nextObject()) {
            // Skip SPDI tag, if existant
            if (tlv.getTag() == TAG_SPDI) {
              tlv = new SimTlv(tlv.getData(), 0, tlv.getData().length);
            }
            // There should only be one TAG_SPDI_PLMN_LIST
if (tlv.getTag() == TAG_SPDI_PLMN_LIST) {
plmnEntries = tlv.getData();
break;







