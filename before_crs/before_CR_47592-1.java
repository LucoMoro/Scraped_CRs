/*Force close_crash happened when add new mail from Email widget

rootcause: The widget store a email account id when Launcher add a email widget.
If the email account is removed, but the widget acount id is still exsit.

Change-Id:I4b18e19506ed6afe5d24ab44585cd58c37e221f0Author: Erjun Ding <erjunx.ding@intel.com>
Signed-off-by: Erjun Ding <erjunx.ding@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 36835*/
//Synthetic comment -- diff --git a/src/com/android/email/activity/MessageCompose.java b/src/com/android/email/activity/MessageCompose.java
//Synthetic comment -- index 0ac7301..1d51e3a 100644

//Synthetic comment -- @@ -371,7 +371,9 @@

private void setAccount(Account account) {
if (account == null) {
            throw new IllegalArgumentException();
}
mAccount = account;
mFromView.setText(account.mEmailAddress);








//Synthetic comment -- diff --git a/src/com/android/email/widget/EmailWidget.java b/src/com/android/email/widget/EmailWidget.java
//Synthetic comment -- index 4856305..748e30e 100644

//Synthetic comment -- @@ -317,7 +317,8 @@

if (isCursorValid()) {
// Show compose icon & message list
            if (mAccountId == Account.ACCOUNT_ID_COMBINED_VIEW) {
// Don't allow compose for "combined" view
views.setViewVisibility(R.id.widget_compose, View.INVISIBLE);
} else {







