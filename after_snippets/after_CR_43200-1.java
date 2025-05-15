
//<Beginning of snippet n. 0>


* </ul>
*/
public static final Pattern PHONE
        = Pattern.compile(                      // sdd = space, dot, or dash
                "(\\+[0-9]+[\\- \\.]*)?"        // +<digits><sdd>*
                + "(\\([0-9]+\\)[\\- \\.]*)?"   // (<digits>)<sdd>*
                + "([0-9][0-9\\- \\.]+[0-9])"); // <digit><digit|sdd>+<digit>

/**
*  Convenience method to take all of the non-null matching groups in a

//<End of snippet n. 0>








