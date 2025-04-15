/*Always return a valid index from Rfc822Tokenizer.findTokenEnd()

If an invalid input string ends with a backslash inside a comment
or quoted string, the returned index would be past the end of the
string. In one case this would lead to a runtime exception being
thrown from MultiAutoCompleteTextView.performValidation.

Change-Id:If629372b429716c25cdc25764f088e95d4812d57*/




//Synthetic comment -- diff --git a/core/java/android/text/util/Rfc822Tokenizer.java b/core/java/android/text/util/Rfc822Tokenizer.java
//Synthetic comment -- index 69cf93c..68334e4 100644

//Synthetic comment -- @@ -256,7 +256,7 @@
if (c == '"') {
i++;
break;
                    } else if (c == '\\' && i + 1 < len) {
i += 2;
} else {
i++;
//Synthetic comment -- @@ -275,7 +275,7 @@
} else if (c == '(') {
level++;
i++;
                    } else if (c == '\\' && i + 1 < len) {
i += 2;
} else {
i++;








//Synthetic comment -- diff --git a/core/tests/coretests/src/android/text/TextUtilsTest.java b/core/tests/coretests/src/android/text/TextUtilsTest.java
//Synthetic comment -- index e111662..79d57f1 100644

//Synthetic comment -- @@ -255,6 +255,23 @@
assertEquals("Foo Bar", tokens[0].getAddress());
}

    @SmallTest
    public void testRfc822FindToken() {
        Rfc822Tokenizer tokenizer = new Rfc822Tokenizer();
        //                0           1         2           3         4
        //                0 1234 56789012345678901234 5678 90123456789012345
        String address = "\"Foo\" <foo@google.com>, \"Bar\" <bar@google.com>";
        assertEquals(0, tokenizer.findTokenStart(address, 21));
        assertEquals(22, tokenizer.findTokenEnd(address, 21));
        assertEquals(24, tokenizer.findTokenStart(address, 25));
        assertEquals(46, tokenizer.findTokenEnd(address, 25));
    }

    @SmallTest
    public void testRfc822FindTokenWithError() {
        assertEquals(9, new Rfc822Tokenizer().findTokenEnd("\"Foo Bar\\", 0));
    }

@LargeTest
public void testEllipsize() {
CharSequence s1 = "The quick brown fox jumps over \u00FEhe lazy dog.";







