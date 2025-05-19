//<Beginning of snippet n. 0>
public static final Pattern PHONE
        = Pattern.compile(                                  // sdd = space, dot, or dash
                "(\\+[0-9]+[\\- \\.]*)?"                    // +<digits><sdd>*
                + "(\\([0-9]+\\)[\\- \\.]*)?"               // (<digits>)<sdd>*
                + "(\\b\\d{3}\\b[\\- \\.]*)?"                // three-digit standalone format
                + "|([0-9]{3}[\\- \\.]?[0-9]{3,})(?![0-9])");  // distinct digits grouping
//<End of snippet n. 0>