/*"Connection problem or invalid MMI code" occrued when executing USSD
USSD(Maxis *100#, Celcom *118#, Umobile *118#, Digi *128#)

	When pressing touch key, USSD request is occured twice(UP/DOWN) respectively
	So block a up key event

Change-Id:Ie8c5f59bdc405048c2f941f44fa7db75cd69122eSigned-off-by: Sang-Jun Park <sj2202.park@samsung.com>*/
//Synthetic comment -- diff --git a/src/com/android/phone/PhoneUtils.java b/src/com/android/phone/PhoneUtils.java
//Synthetic comment -- index 4fe6d50..70fae3a 100644

//Synthetic comment -- @@ -1098,8 +1098,10 @@
switch (keyCode) {
case KeyEvent.KEYCODE_CALL:
case KeyEvent.KEYCODE_ENTER:
                                    phone.sendUssdResponse(inputText.getText().toString());
                                    newDialog.dismiss();
return true;
}
return false;







