/*Change test string without a space character

The test string should not include any space character in case of
a numeric keyboard. Because the key which represents a space
character is different for each countries.

Change-Id:I8a7a3cd5fffa644441be94916d8fa9321fde7360*/




//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/AutoCompleteTextViewTest.java b/tests/tests/widget/src/android/widget/cts/AutoCompleteTextViewTest.java
//Synthetic comment -- index 8bf0c5b..40e1c74 100755

//Synthetic comment -- @@ -619,17 +619,16 @@
assertNull(filter.getResult());
// 12-key support
if (mNumeric) {
            // "numeric" in case of 12-key(NUMERIC) keyboard
            mInstrumentation.sendStringSync("6688633777444222");
            Thread.sleep(100);
            assertEquals("numeric", filter.getResult());
} else {
mInstrumentation.sendStringSync(STRING_TEST);
            // give some time for UI to settle
            Thread.sleep(100);
            assertEquals(STRING_TEST, filter.getResult());
}
}

@TestTargets({







