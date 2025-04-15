/*fix for supporting 3 digits MNC codeDefault Android MNC value has a 2 digit but it should be supported a 3 digitMNC in India. (should be supported both 2 and 3 digits MNC)Change-Id: I69373d196b29bccd06653841f24cbfe3886834fbSigned-off-by: Sang-Jun Park <sj2202.park@samsung.com>

Change-Id:I0f0191e1c6446029c5f7d2957778fa743ee39e9a*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java b/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java
old mode 100644
new mode 100755
//Synthetic comment -- index b14896a..00f3419

//Synthetic comment -- @@ -141,6 +141,27 @@
private static final int EVENT_SIM_REFRESH = 31;
private static final int EVENT_GET_CFIS_DONE = 32;

    /**
     * Lookup table for finding MccMnc having 3 digits MNC
     * This is for only SimCard which has the incorrect MNC length.
     */
    private static final String[] MCCMNC_CODES_HAVING_3DIGITS_MNC = {
        "405025", "405026", "405027", "405028", "405029", "405030", "405031", "405032",
        "405033", "405034", "405035", "405036", "405037", "405038", "405039", "405040",
        "405041", "405042", "405043", "405044", "405045", "405046", "405047", "405750",
        "405751", "405752", "405753", "405754", "405755", "405756", "405799", "405800",
        "405801", "405802", "405803", "405804", "405805", "405806", "405807", "405808",
        "405809", "405810", "405811", "405812", "405813", "405814", "405815", "405816",
        "405817", "405818", "405819", "405820", "405821", "405822", "405823", "405824",
        "405825", "405826", "405827", "405828", "405829", "405830", "405831", "405832",
        "405833", "405834", "405835", "405836", "405837", "405838", "405839", "405840",
        "405841", "405842", "405843", "405844", "405845", "405846", "405847", "405848",
        "405849", "405850", "405851", "405852", "405853", "405875", "405876", "405877",
        "405878", "405879", "405880", "405881", "405882", "405883", "405884", "405885",
        "405886", "405908", "405909", "405910", "405911", "405925", "405926", "405927",
        "405928", "405929", "405932"
    };

// ***** Constructor

SIMRecords(GSMPhone p) {
//Synthetic comment -- @@ -498,6 +519,16 @@

Log.d(LOG_TAG, "IMSI: " + imsi.substring(0, 6) + "xxxxxxx");

                if ((imsi != null) && (imsi.length() >= 6)) {
                    String mccmncCode = imsi.substring(0, 6);
                    for (String mccmnc : MCCMNC_CODES_HAVING_3DIGITS_MNC) {
                        if (mccmnc.equals(mccmncCode)) {
                            mncLength = 3;
                            break;
                        }
                    }
				}

if (mncLength == UNKNOWN) {
// the SIM has told us all it knows, but it didn't know the mnc length.
// guess using the mcc
//Synthetic comment -- @@ -742,6 +773,16 @@
mncLength = UNKNOWN;
}
} finally {
                    if ((imsi != null) && (imsi.length() >= 6)) {
                        String mccmncCode = imsi.substring(0, 6);
                        for (String mccmnc : MCCMNC_CODES_HAVING_3DIGITS_MNC) {
                            if (mccmnc.equals(mccmncCode)) {
                                mncLength = 3;
                                break;
                            }
                        }
                    }

if (mncLength == UNKNOWN || mncLength == UNINITIALIZED) {
if (imsi != null) {
try {







