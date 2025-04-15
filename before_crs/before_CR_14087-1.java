/*Improved error-handling in Rfc822Tokenizer

The javadoc for the Rfc822Tokenizer states that it will try
to be tolerant to broken syntax instead of returning an error
(as in an unchecked exception). In some rare cases where the
input is clearly incorrect, the tokenizer throws a
StringIndexOutOfBoundsException, which was found during
one of the monkey test runs. This commits fixes that crash,
and teaches the tokenizer to just continue to run anyway. Two
simple junit testcases has also been added for testing the
default and the errornous case.*/
//Synthetic comment -- diff --git a/core/java/android/text/util/Rfc822Tokenizer.java b/core/java/android/text/util/Rfc822Tokenizer.java
//Synthetic comment -- index cb39f7de..3a67e20 100644

//Synthetic comment -- @@ -86,7 +86,9 @@
i++;
break;
} else if (c == '\\') {
                        name.append(text.charAt(i + 1));
i += 2;
} else {
name.append(c);
//Synthetic comment -- @@ -112,7 +114,9 @@
level++;
i++;
} else if (c == '\\') {
                        comment.append(text.charAt(i + 1));
i += 2;
} else {
comment.append(c);








//Synthetic comment -- diff --git a/tests/AndroidTests/src/com/android/unit_tests/TextUtilsTest.java b/tests/AndroidTests/src/com/android/unit_tests/TextUtilsTest.java
//Synthetic comment -- index 7720041..bb32da0 100644

//Synthetic comment -- @@ -29,6 +29,8 @@
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.text.util.Rfc822Validator;
import android.test.MoreAsserts;

//Synthetic comment -- @@ -269,6 +271,24 @@
}
}

@LargeTest
public void testEllipsize() {
CharSequence s1 = "The quick brown fox jumps over \u00FEhe lazy dog.";







