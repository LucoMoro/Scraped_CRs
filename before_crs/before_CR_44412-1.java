/*Just show Mms context menu when it was downloaded

The special context menu such as "View slideshow, copy to SDCard"
are useless when Mms wasn't downloaded.

Change-Id:Ibfef86651ae7c3ce63236c1be63175bd6b0d4641Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index 48be2a9..65f1e63 100644

//Synthetic comment -- @@ -1033,7 +1033,7 @@
.setOnMenuItemClickListener(l);
}

            if (msgItem.isMms()) {
switch (msgItem.mBoxId) {
case Mms.MESSAGE_BOX_INBOX:
break;







