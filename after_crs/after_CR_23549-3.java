/*Fix for Intent droiddoc failure

In some infrequent cases, the two uses of indexOf in isHidden appear to return
true to indicate that @hide or @pending is present in the doc string for Intent
when this is not the case.  This change replaces the use of indexOf with a
custom coded verson which handles less cases and proves more reliable under
test.

Change-Id:I0cfd37b00e320e1fe6aa39bbe443e362d36aaf1a*/




//Synthetic comment -- diff --git a/tools/droiddoc/src/Comment.java b/tools/droiddoc/src/Comment.java
//Synthetic comment -- index 68b6d20..634b402 100644

//Synthetic comment -- @@ -299,6 +299,35 @@
return mBriefTags;
}

    // This method is used to replace indexOf usage below which seems to cause
    // some problems with the Java environment.  See the comment in isHidden
    // to understand the background.
    private static boolean isFoundIn(String needle, String haystack)
    {
        char[] n = needle.toCharArray();
        char[] h = haystack.toCharArray();
        int last = haystack.length() - needle.length();
        int nlen = needle.length();
        int i;
        int j;
        boolean match = false;

        for (i = 0; !match && i < last; i++) {
            j = 0;
            if (h[i] == n[j]) {
                match = true;
                for (; j < nlen; j++) {
                    if (h[i+j] != n[j]) {
                        match = false;
                        break;
                    }
                }
            }
        }
        return match;
    }


public boolean isHidden()
{
if (mHidden >= 0) {
//Synthetic comment -- @@ -308,9 +337,26 @@
mHidden = 0;
return false;
}
            // The original code from droiddoc had this perfectly reasonable use
            // of indexOf.
            // boolean b = mText.indexOf("@hide") >= 0 || mText.indexOf("@pending") >= 0;
            // mHidden = b ? 1 : 0;
            // if (b) {
            // Unfortunately under some conditions, this code mysteriously returns
            // true for an mText that does not contain @hide or @pending.
            // This seems to apply with the Intent class and cause
            // document generation to fail.  This failure was not reliable and could
            // only be reproduced on (between) 50-150 builds.  The change to use
            // the new method isFoundIn is stable over a much higher number of builds
            // although the true root cause is not yet discovered.
            if (isFoundIn("@hide", mText) || isFoundIn("@pending", mText)) {
                mHidden = 1;
                return true;
            } else {
                mHidden = 0;
                return false;
            }
            //return b;
}
}








