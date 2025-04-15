/*two digit numbers is dialed normally not USSD if users dial 92~96
in croatia and serbia

Change-Id:Ibdef0309d7494d489bcde531cc1bbbe33e003307Signed-off-by: Sang-Jun Park <sj2202.park@samsung.com>*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/telephony/java/com/android/internal/telephony/gsm/GsmMmiCode.java
old mode 100644
new mode 100755
//Synthetic comment -- index aa16fa3..679c628

//Synthetic comment -- @@ -89,6 +89,10 @@
static final String SC_PUK          = "05";
static final String SC_PUK2         = "052";

    //Allow 2 Digit code for Croatia - MCC code for Croatia
    static final String MCC_CROATIA     = "219";
    // Allow 2 Digit code for Serbia - MCC code for Serbia nypark_20100709
    static final String MCC_SERBIA     = "220";
//***** Event Constants

static final int EVENT_SET_COMPLETE         = 1;
//Synthetic comment -- @@ -191,6 +195,9 @@

ret = new GsmMmiCode(phone);
ret.poundString = dialString;
        } else if (isTwoDigitShortCode(dialString)) {
            // this may be a short code, as defined in TS 22.030, 6.5.3.2
            ret = null;
} else if (isShortCode(dialString, phone)) {
// this may be a short code, as defined in TS 22.030, 6.5.3.2
ret = new GsmMmiCode(phone);
//Synthetic comment -- @@ -445,6 +452,23 @@

}

    static private boolean isTwoDigitShortCode(String dialString) {
        String mcc = "000";
        String numeric =  SystemProperties.get(TelephonyProperties.PROPERTY_OPERATOR_NUMERIC);

        if (numeric != null && numeric.length() > 4)
            mcc = numeric.substring(0, 3);
        else
            return false;

        if ((mcc.equals(MCC_CROATIA) ||  mcc.equals(MCC_SERBIA))
                  && (dialString.equals("92") || dialString.equals("93") || dialString.equals("94")
                  || dialString.equals("95") || dialString.equals("96")))
            return true;

        return false;
    }

/**
* Helper function for newFromDialString.  Returns true if dialString appears to be a short code
* AND conditions are correct for it to be treated as such.







