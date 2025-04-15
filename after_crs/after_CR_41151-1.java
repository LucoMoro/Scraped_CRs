/*Force close_crash happened when add new mail from Email widget

rootcause: The widget store a email account id when Launcher add a email widget.
If the email account is removed, but the widget acount id is still exsit.

Change-Id:I5b5b79d31651d8d2d803c537011e58c812c82d1cAuthor: Erjun Ding <erjunx.ding@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 36835*/




//Synthetic comment -- diff --git a/src/com/android/email/activity/MessageCompose.java b/src/com/android/email/activity/MessageCompose.java
//Synthetic comment -- index 0ac7301..1d51e3a 100644

//Synthetic comment -- @@ -371,7 +371,9 @@

private void setAccount(Account account) {
if (account == null) {
            Utility.showToast(this, R.string.widget_no_accounts);
            Log.d(Logging.LOG_TAG, "The account has been deleted, force finish it");
            finish();
}
mAccount = account;
mFromView.setText(account.mEmailAddress);








//Synthetic comment -- diff --git a/src/com/android/email/widget/EmailWidget.java b/src/com/android/email/widget/EmailWidget.java
//Synthetic comment -- index a589a22..ff0b32b 100644

//Synthetic comment -- @@ -317,7 +317,8 @@

if (isCursorValid()) {
// Show compose icon & message list
            if (mAccountId == Account.ACCOUNT_ID_COMBINED_VIEW
                || Account.restoreAccountWithId(mContext, mAccountId) == null) {
// Don't allow compose for "combined" view
views.setViewVisibility(R.id.widget_compose, View.INVISIBLE);
} else {







