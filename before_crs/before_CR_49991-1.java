/*Fix dangling comma in 'to view' when filled from intent.

When using the ACTION_SENDTO intent with a scheme of mailto: a dangling comma was inserted into the 'to view' causing
an invalid email address error when attempting to send. This fix simply checks if the string is empty before splitting
on a comma and adding the addresses. The use case here is if the intent only includes bcc or cc fields and not the to field.

Change-Id:Iefc6a8fc1070da00a23f46a97a4b3229243cd0ccSigned-off-by: Michael Novak <michael.novakjr@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/email/activity/MessageCompose.java b/src/com/android/email/activity/MessageCompose.java
//Synthetic comment -- index 0ac7301..487f7f5 100644

//Synthetic comment -- @@ -2066,7 +2066,10 @@
} else {
to = decode(mailToString.substring(length, index));
}
            addAddresses(mToView, to.split(" ,"));
} catch (UnsupportedEncodingException e) {
Log.e(Logging.LOG_TAG, e.getMessage() + " while decoding '" + mailToString + "'");
}







