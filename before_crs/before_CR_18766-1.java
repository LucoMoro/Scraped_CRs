/*JPtextinput: Dont break emoji characters when cutting strings.

In the LengthFilter make sure that we do not cut the
string so a last emoji character gets broken and replaced
by some garbage character.

We need to check if the last char is the high surrogate and
if so drop the high surrogate also.

Change-Id:I210e7e41aa0761ecbb2b32e3ebf680a04382c287*/
//Synthetic comment -- diff --git a/core/java/android/text/InputFilter.java b/core/java/android/text/InputFilter.java
//Synthetic comment -- index 2f55677..185e34b 100644

//Synthetic comment -- @@ -88,7 +88,14 @@
} else if (keep >= end - start) {
return null; // keep original
} else {
                return source.subSequence(start, start + keep);
}
}








