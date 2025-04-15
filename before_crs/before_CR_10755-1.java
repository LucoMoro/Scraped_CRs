/*Fix documentation example, per issue 895 on android.googlecode.com*/
//Synthetic comment -- diff --git a/core/java/android/app/ListActivity.java b/core/java/android/app/ListActivity.java
//Synthetic comment -- index 5523c18..19b99c8 100644

//Synthetic comment -- @@ -152,7 +152,7 @@
*         // Query for all people contacts using the {@link android.provider.Contacts.People} convenience class.
*         // Put a managed wrapper around the retrieved cursor so we don't have to worry about
*         // requerying or closing it as the activity changes state.
 *         mCursor = People.query(this.getContentResolver(), null);
*         startManagingCursor(mCursor);
* 
*         // Now create a new list adapter bound to the cursor. 
//Synthetic comment -- @@ -161,9 +161,9 @@
*                 this, // Context.
*                 android.R.layout.two_line_list_item,  // Specify the row template to use (here, two columns bound to the two retrieved cursor 
* rows).
 *                 mCursor,                                    // Pass in the cursor to bind to.
 *                 new String[] {People.NAME, People.COMPANY}, // Array of cursor columns to bind to.
 *                 new int[]);                                 // Parallel array of which template objects to bind to those columns.
* 
*         // Bind to our new adapter.
*         setListAdapter(adapter);







