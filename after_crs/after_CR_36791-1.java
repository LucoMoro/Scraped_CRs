/*Fix an NullPointerException when pressed BACK

No need to continue the querying when pressed BACK.
Otherwise it will be thrown an NullPointerException,
since RecipientEditor need to be initialized after
the querying completed.

Change-Id:I6c07109d408157039bf808a59ba84f4d3ae1c50aSigned-off-by: Roger Chen <cxr514033970@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index 085822f..0c01661 100644

//Synthetic comment -- @@ -2209,6 +2209,9 @@
}
break;
case KeyEvent.KEYCODE_BACK:
                //No need to do the querying when pressed BACK
                mBackgroundQueryHandler.cancelOperation(MESSAGE_LIST_QUERY_TOKEN);

exitComposeMessageActivity(new Runnable() {
public void run() {
finish();







