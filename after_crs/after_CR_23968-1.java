/*Fix issue 17837http://code.google.com/p/android/issues/detail?id=17837Change-Id:I8c2cd489c98c1c2346ce265f7ba4695e43cce5d0*/




//Synthetic comment -- diff --git a/src/com/android/contacts/TwelveKeyDialer.java b/src/com/android/contacts/TwelveKeyDialer.java
//Synthetic comment -- index 5219d99..e2c1c6d 100644

//Synthetic comment -- @@ -799,7 +799,7 @@
} else if (!TextUtils.isEmpty(mLastNumberDialed)) {
// Otherwise, pressing the Dial button without entering
// any digits means "recall the last number dialed".
                mDigits.append(mLastNumberDialed);
return;
} else {
// Rare case: there's no "last number dialed".  There's







