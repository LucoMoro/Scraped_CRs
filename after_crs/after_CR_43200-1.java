/*Allow three digit phone numbers

The comment for the PHONE pattern says:

"<digit><digit|sdd>+<digit>"

But the actual pattern requires that the string contains
more that that. A phone number should be allowed to be
three digits.

Change-Id:I86d2f3d634cd0c1654dad9814906f151055dc23a*/




//Synthetic comment -- diff --git a/core/java/android/util/Patterns.java b/core/java/android/util/Patterns.java
//Synthetic comment -- index 152827d..9522112 100644

//Synthetic comment -- @@ -169,10 +169,10 @@
* </ul>
*/
public static final Pattern PHONE
        = Pattern.compile(                      // sdd = space, dot, or dash
                "(\\+[0-9]+[\\- \\.]*)?"        // +<digits><sdd>*
                + "(\\([0-9]+\\)[\\- \\.]*)?"   // (<digits>)<sdd>*
                + "([0-9][0-9\\- \\.]+[0-9])"); // <digit><digit|sdd>+<digit>

/**
*  Convenience method to take all of the non-null matching groups in a







