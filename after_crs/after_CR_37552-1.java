/*logcat find dialog: Add missing trim()

Change-Id:I137994ea4723299461c695559d664230a98b082f*/




//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/FindDialog.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/FindDialog.java
//Synthetic comment -- index f86d967..cfd1ee8 100644

//Synthetic comment -- @@ -71,7 +71,7 @@
mSearchText.addModifyListener(new ModifyListener() {
@Override
public void modifyText(ModifyEvent e) {
                boolean hasText = !mSearchText.getText().trim().isEmpty();
mFindNext.setEnabled(hasText);
mFindPrevious.setEnabled(hasText);
}







