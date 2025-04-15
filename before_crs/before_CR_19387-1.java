/*Set STK header if icon is null and selfexplanatory

As per spec, if title icon is null and selfexplanatory the header
should still be set. Header must be either an icon, default app
name or a title text provided by the SIM.

Change-Id:Ife3ee1b633dd94e818e8692fc6cdf6b53e92f0d1*/
//Synthetic comment -- diff --git a/src/com/android/stk/StkMenuActivity.java b/src/com/android/stk/StkMenuActivity.java
//Synthetic comment -- index aac1a12..870ca58 100644

//Synthetic comment -- @@ -263,22 +263,20 @@
private void displayMenu() {

if (mStkMenu != null) {
// Display title & title icon
if (mStkMenu.titleIcon != null) {
mTitleIconView.setImageBitmap(mStkMenu.titleIcon);
mTitleIconView.setVisibility(View.VISIBLE);
            } else {
                mTitleIconView.setVisibility(View.GONE);
            }
            if (!mStkMenu.titleIconSelfExplanatory) {
                mTitleTextView.setVisibility(View.VISIBLE);
                if (mStkMenu.title == null) {
                    mTitleTextView.setText(R.string.app_name);
                } else {
                    mTitleTextView.setText(mStkMenu.title);
}
} else {
                mTitleTextView.setVisibility(View.INVISIBLE);
}
// create an array adapter for the menu list
StkMenuAdapter adapter = new StkMenuAdapter(this,







