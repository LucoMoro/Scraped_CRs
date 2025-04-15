/*Fix a StaleDataException issue when pressed MENU in busy

Need to check the state before got the count of mCursor,
since it will be closed in refeshing the list.

Change-Id:Ib636fd8802ced47c6b30fa4cb6fde8173386dd7bSigned-off-by: Roger Chen <cxr514033970@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/ManageSimMessages.java b/src/com/android/mms/ui/ManageSimMessages.java
//Synthetic comment -- index ac2ceea..2f75211 100644

//Synthetic comment -- @@ -280,7 +280,7 @@
public boolean onPrepareOptionsMenu(Menu menu) {
menu.clear();

        if (mState == SHOW_LIST && (null != mCursor) && (mCursor.getCount() > 0)) {
menu.add(0, OPTION_MENU_DELETE_ALL, 0, R.string.menu_delete_messages).setIcon(
android.R.drawable.ic_menu_delete);
}







