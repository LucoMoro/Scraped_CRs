/*workaround for sometimes generated random call on clicked a call button in the call log

If a user releases a call button when the call log is redrawn
by the contacts information, sometimes wrong number is called
by a cached view with unorder.
So on clicking, a call number is used not a number in current cached view
but a number in a view at a touched moment
this is workaround.

Change-Id:I680d9dcc09269f9c5f23e729b251ea34bf58899eSigned-off-by: SungHyun Kwon <sh.kwon@lge.com>*/




//Synthetic comment -- diff --git a/src/com/android/contacts/RecentCallsListActivity.java b/src/com/android/contacts/RecentCallsListActivity.java
//Synthetic comment -- index 92d5946..8e07152 100644

//Synthetic comment -- @@ -185,7 +185,7 @@
*/
private static int sFormattingType = FORMATTING_TYPE_INVALID;

	/**
	 * Saved a phone number when the call button is touched as action down 
*/
private String mSaveNumber;
//Synthetic comment -- @@ -238,6 +238,7 @@
startActivity(new Intent(Intent.ACTION_CALL_PRIVILEGED, callUri));
}
}

		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				mSaveNumber = (String) v.getTag();







