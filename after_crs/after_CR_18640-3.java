/*Fix for storing CPHS MWI messages with store type

CPSH MWI messages of store type were treated as discard type. This had
the effect that no CPHS MWI message with text was ever shown to the
user.*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java b/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java
//Synthetic comment -- index 0be9466..b23a81f 100644

//Synthetic comment -- @@ -841,9 +841,8 @@
// If the user data is a single space char, do not store
// the message. Otherwise, store and display as usual
if (" ".equals(getMessageBody())) {
                return true;
}
}

return false;







