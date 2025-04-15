/*packages/apps/Phone: Set initial focus for SimContacts.

Unable to navigate the contacts stored in SIM card thru keypad due to there is no intial focus.
Add mInitialSelection to zero, which is used to set the initial focus.

Change-Id:I3855fed212c6aea282b40345e0c48c2ede2bccaf*/
//Synthetic comment -- diff --git a/src/com/android/phone/SimContacts.java b/src/com/android/phone/SimContacts.java
//Synthetic comment -- index 3ee010b..668d069 100644

//Synthetic comment -- @@ -262,6 +262,8 @@
if (Intent.ACTION_PICK.equals(intent.getAction())) {
// "index" is 1-based
mInitialSelection = intent.getIntExtra("index", 0) - 1;
}
return intent.getData();
}







