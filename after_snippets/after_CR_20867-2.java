
//<Beginning of snippet n. 0>

old mode 100644
new mode 100755

static final String SC_PUK          = "05";
static final String SC_PUK2         = "052";

    //Allow 2 Digit code for Croatia - MCC code for Croatia
    static final String MCC_CROATIA     = "219";
    // Allow 2 Digit code for Serbia - MCC code for Serbia nypark_20100709
    static final String MCC_SERBIA     = "220";
//***** Event Constants

static final int EVENT_SET_COMPLETE         = 1;

ret = new GsmMmiCode(phone);
ret.poundString = dialString;
        } else if (isTwoDigitShortCode(dialString)) {
            // this may be a short code, as defined in TS 22.030, 6.5.3.2
            ret = null;
} else if (isShortCode(dialString, phone)) {
// this may be a short code, as defined in TS 22.030, 6.5.3.2
ret = new GsmMmiCode(phone);

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

//<End of snippet n. 0>










//<Beginning of snippet n. 1>

old mode 100644
new mode 100755

return null;
}

        if (imsi.length() >= 6) {
            final String[] IndExceptMccMnc = { "405805", "405799", "405025", "405026", "405027",
                                               "405028", "405029", "405030", "405031", "405032",
                                               "405033", "405034", "405035", "405036", "405037",
                                               "405038", "405039", "405040", "405041", "405042",
                                               "405043", "405044", "405045", "405046", "405047",
                                               "405800", "405802", "405803", "405807", "405804",
                                               "405809", "405852", "405853", "405846", "405848",
                                               "405849", "405812", "405806", "405750", "405751",
                                               "405752", "405753", "405754", "405755", "405756",
                                               "405808", "405810", "405811", "405813", "405814",
                                               "405815", "405816", "405817", "405801", "405820",
                                               "405819", "405821", "405925", "405875", "405876",
                                               "405877", "405879", "405818", "405878", "405927",
                                               "405928", "405929", "405926", "405932", "405850",
                                               "405823", "405824", "405825", "405826", "405827",
                                               "405828", "405829", "405830", "405831", "405832",
                                               "405833", "405834", "405835", "405836", "405837",
                                               "405838", "405839", "405840", "405841", "405842",
                                               "405843", "405844", "405845", "405847", "405851",
                                               "405822", "405880", "405881", "405882", "405883",
                                               "405884", "405885", "405886", "405908", "405909",
                                               "405910", "405911"};

            for (int i = 0; i < IndExceptMccMnc.length; i++)
                if (IndExceptMccMnc[i].equals(imsi.substring(0, 6)))
                    return imsi.substring(0, 6);
        }

// Length = length of MCC + length of MNC
// length of mcc = 3 (TS 23.003 Section 2.2)
return imsi.substring(0, 3 + mncLength);

//<End of snippet n. 1>








