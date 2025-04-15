/*In Email, when composing new message, accept non US ASCII names

In Email, compose a message and try to enter a name which contains
8-bit character, for example an e with accent. This kind of names
were not previously accepted by the validator and were discarded.*/
//Synthetic comment -- diff --git a/src/com/android/email/EmailAddressValidator.java b/src/com/android/email/EmailAddressValidator.java
//Synthetic comment -- index e6aab2f..2e55f94 100644

//Synthetic comment -- @@ -16,11 +16,11 @@

package com.android.email;

import com.android.email.mail.Address;

import android.util.Config;
import android.util.Log;
import android.widget.AutoCompleteTextView.Validator;

public class EmailAddressValidator implements Validator {
public CharSequence fixText(CharSequence invalidText) {
//Synthetic comment -- @@ -28,6 +28,7 @@
}

public boolean isValid(CharSequence text) {
        return Address.parse(text.toString()).length > 0;
}
}







