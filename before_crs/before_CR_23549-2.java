/*Fix for Intent droiddoc failure

In some infrequent cases, the two uses of indexOf in isHidden appear to return
true to indicate that @hide or @pending is present in the doc string for Intent
when this is not the case.  This change replaces the use of indexOf with a
custom coded verson which handles less cases and proves more reliable under
test.

Change-Id:I0cfd37b00e320e1fe6aa39bbe443e362d36aaf1a*/
//Synthetic comment -- diff --git a/tools/droiddoc/src/Comment.java b/tools/droiddoc/src/Comment.java
//Synthetic comment -- index 68b6d20..fa6329e 100644

//Synthetic comment -- @@ -299,6 +299,35 @@
return mBriefTags;
}

public boolean isHidden()
{
if (mHidden >= 0) {
//Synthetic comment -- @@ -308,9 +337,26 @@
mHidden = 0;
return false;
}
            boolean b = mText.indexOf("@hide") >= 0 || mText.indexOf("@pending") >= 0;
            mHidden = b ? 1 : 0;
            return b;
}
}








