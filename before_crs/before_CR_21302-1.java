/*fix for #14416: error in sample code of android.app.Activity

Change-Id:I42422a58ea81036df5139392b11a2e861cfaffdb*/
//Synthetic comment -- diff --git a/core/java/android/app/Activity.java b/core/java/android/app/Activity.java
//Synthetic comment -- index f25c4c3..9e1fff6 100644

//Synthetic comment -- @@ -412,14 +412,13 @@
*
*     static final int PICK_CONTACT_REQUEST = 0;
*
 *     protected boolean onKeyDown(int keyCode, KeyEvent event) {
*         if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
*             // When the user center presses, let them pick a contact.
 *             startActivityForResult(
 *                 new Intent(Intent.ACTION_PICK,
 *                 new Uri("content://contacts")),
 *                 PICK_CONTACT_REQUEST);
 *            return true;
*         }
*         return false;
*     }
//Synthetic comment -- @@ -430,7 +429,7 @@
*             if (resultCode == RESULT_OK) {
*                 // A contact was picked.  Here we will just display it
*                 // to the user.
 *                 startActivity(new Intent(Intent.ACTION_VIEW, data));
*             }
*         }
*     }







