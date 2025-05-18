//<Beginning of snippet n. 0>

public static final Pattern PHONE
        = Pattern.compile(                                  // sdd = space, dot, or dash
                "(\\+[0-9]+[\\- \\.]*)?"                    // +<digits><sdd>*
                + "(\\([0-9]+\\)[\\- \\.]*)?"               // (<digits>)<sdd>*
                + "([0-9][0-9\\- \\.]+[0-9]|\\b[0-9]{3}\\b)"); // <digit><digit|sdd>+<digit> or exactly three digits

//<End of snippet n. 0>