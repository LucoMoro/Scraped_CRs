/*Allow three digit phone numbers

The comment for the PHONE pattern says:

"<digit><digit|sdd>+<digit>"

But the actual pattern requires that the string contains
more than that. A phone number should be allowed to be
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








//Synthetic comment -- diff --git a/core/tests/coretests/src/android/util/PatternsTest.java b/core/tests/coretests/src/android/util/PatternsTest.java
//Synthetic comment -- index 9519b9f..ebdbb0e 100644

//Synthetic comment -- @@ -156,6 +156,8 @@
"Me: 16505551212 this\n",
"Me: 6505551212 this\n",
"Me: 5551212 this\n",
                "Me: 2211 this\n",
                "Me: 112 this\n",

"Me: 1-650-555-1212 this\n",
"Me: (650) 555-1212 this\n",







