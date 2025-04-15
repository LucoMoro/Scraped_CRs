/*Update NumberKeyListenerTest for AZERTY devices

Need to check META key state in lookup function to take care
of case where one hardware key is used for alpha & digit with
function key.

Change-Id:I602523c7f30647d0ef55197f74c6edec356e51c0*/




//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/method/cts/NumberKeyListenerTest.java b/tests/tests/text/src/android/text/method/cts/NumberKeyListenerTest.java
//Synthetic comment -- index 5c0b9f1..a98899d 100644

//Synthetic comment -- @@ -219,7 +219,13 @@

@Override
protected int lookup(KeyEvent event, Spannable content) {
            int keyData = event.getUnicodeChar(getMetaState(content));

            if (ok(getAcceptedChars(), (char)keyData)) {
               return keyData;
            }

            return 0;
}

public boolean callOk(char[] accept, char c) {







