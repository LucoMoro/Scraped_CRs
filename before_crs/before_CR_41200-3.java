/*Fix for storing CPHS MWI messages with store type

CPHS MWI messages of store type were treated as discard type.
This resulted in that no CPHS MWI messages with text was ever
shown to the user.

Change-Id:I42f80a084c35a813933052d4a676e16b2c4e2d9d*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SmsMessage.java b/src/java/com/android/internal/telephony/gsm/SmsMessage.java
//Synthetic comment -- index cca9363..0bf1ea1 100644

//Synthetic comment -- @@ -862,9 +862,8 @@
// If the user data is a single space char, do not store
// the message. Otherwise, store and display as usual
if (" ".equals(getMessageBody())) {
                ;
}
            return true;
}

return false;







