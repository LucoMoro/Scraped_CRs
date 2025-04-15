/*Fix problem with Intent being found as hidden

On some builds (even with identical source trees) the build
could fail reporting that Intent was a hidden type.  However,
Intent is not hidden and the error was wrong.

The problem lies in the Java system somewhere in that the
indexOf method was returning an index beyond the end of the
string.  The resolution here is simply to copy the string to
a new string and search that.  This resolution proved to be
stable and reliable on gingerbread and while it occurs much
less frequently on ice cream sandwich, it has still been
spotted a few times.

Change-Id:Iea620680d688635645f4ce3e67ba28ff4ec53314*/
//Synthetic comment -- diff --git a/src/com/google/doclava/Comment.java b/src/com/google/doclava/Comment.java
//Synthetic comment -- index 20e5e56..51652a6 100644

//Synthetic comment -- @@ -462,7 +462,22 @@
mHidden = 0;
return false;
}
      boolean b = mText.indexOf("@hide") >= 0 || mText.indexOf("@pending") >= 0;
mHidden = b ? 1 : 0;
return b;
}







