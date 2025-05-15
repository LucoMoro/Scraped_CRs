//<Beginning of snippet n. 0>
public static final Pattern PHONE
        = Pattern.compile(                                  // sdd = space, dot, or dash
                "(\\+[0-9]+[\\- \\.]*)?"                    // +<digits><sdd>*
                + "(\\([0-9]{3}\\)[\\- \\.]*)?"              // (<3 digits>)<sdd>*
                + "([0-9]{3})"                               // Exactly three digits
                + "|([0-9]{3}[\\- \\.]?[0-9]{3})"           // 3 digits optional sdd and 3 digits
                + "|([0-9]{3})");                            // Standalone three digits

/**
*  Convenience method to take all of the non-null matching groups in a
//<End of snippet n. 0>