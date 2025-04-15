/*Fix issue #18053

Removes useless confirmation after cancelling USSD response.http://code.google.com/p/android/issues/detail?id=18053Change-Id:I762a963da0f37929757c50ba39e01df2b03eb699gned-off-by: Cuihtlauac ALVARADO <cuihtlauac.alvarado@orange.com>*/




//Synthetic comment -- diff --git a/src/com/android/phone/PhoneUtils.java b/src/com/android/phone/PhoneUtils.java
//Synthetic comment -- index 0bed83d..f9f87fc 100644

//Synthetic comment -- @@ -943,7 +943,7 @@
if (DBG) log("- using text from PENDING MMI message: '" + text + "'");
break;
case CANCELLED:
                text = null;
break;
case COMPLETE:
if (app.getPUKEntryActivity() != null) {
//Synthetic comment -- @@ -1010,10 +1010,10 @@
} catch (RemoteException e) {
mNwService = null;
}
}
                if (DBG) log("Extended NW displayMMIInitiate (" + text + ")");
                if (text == null || text.length() == 0)
                    return;

// displaying system alert dialog on the screen instead of
// using another activity to display the message.  This







