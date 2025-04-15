/*Fix issue #18053http://code.google.com/p/android/issues/detail?id=18053Change-Id:Ie3c327a4e5871394f728d725703f2640d68e60d5*/
//Synthetic comment -- diff --git a/src/com/android/phone/PhoneUtils.java b/src/com/android/phone/PhoneUtils.java
//Synthetic comment -- index 6148a2d..149b684 100644

//Synthetic comment -- @@ -943,7 +943,7 @@
if (DBG) log("- using text from PENDING MMI message: '" + text + "'");
break;
case CANCELLED:
                text = context.getText(R.string.mmiCancelled);
break;
case COMPLETE:
if (PhoneApp.getInstance().getPUKEntryActivity() != null) {
//Synthetic comment -- @@ -1011,10 +1011,10 @@
} catch (RemoteException e) {
mNwService = null;
}
                    if (DBG) log("Extended NW displayMMIInitiate (" + text + ")");
                    if (text == null || text.length() == 0)
                        return;
}

// displaying system alert dialog on the screen instead of
// using another activity to display the message.  This







