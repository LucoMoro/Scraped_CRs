/*stk: Add support of Help information

According to the 3GPP TS 31.124, Help
information is an optional feature. If help
information is set as supported, then the
proactive command should support Help menu.
If Help menu is supported but not available
then conformance tests cases are considered
as failed(eg: 27.22.4.8.2 sequence 2.1).

This patch adds support for the optional
feature - Help Info.

Change-Id:I27412558853ff2f23b83a924e14efdf2de8d285fAuthor: Guillaume Lucas <guillaumex.lucas@intel.com>
Signed-off-by: Guillaume Lucas <guillaumex.lucas@intel.com>
Signed-off-by: Nizar Haouati <nizar.haouati@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 2883*/
//Synthetic comment -- diff --git a/src/com/android/stk/StkMenuActivity.java b/src/com/android/stk/StkMenuActivity.java
//Synthetic comment -- index aac1a12..898eb5f 100644

//Synthetic comment -- @@ -22,10 +22,13 @@
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
//Synthetic comment -- @@ -41,7 +44,7 @@
* menu content.
*
*/
public class StkMenuActivity extends ListActivity {
private Context mContext;
private Menu mStkMenu = null;
private int mState = STATE_MAIN;
//Synthetic comment -- @@ -59,6 +62,7 @@

// message id for time out
private static final int MSG_ID_TIMEOUT = 1;

Handler mTimeoutHandler = new Handler() {
@Override
//Synthetic comment -- @@ -89,6 +93,8 @@

initFromIntent(getIntent());
mAcceptUsersInput = true;
}

@Override
//Synthetic comment -- @@ -175,6 +181,7 @@

@Override
public void onDestroy() {
super.onDestroy();

CatLog.d(this, "onDestroy");
//Synthetic comment -- @@ -235,6 +242,46 @@
}

@Override
protected void onSaveInstanceState(Bundle outState) {
outState.putInt("STATE", mState);
outState.putParcelable("MENU", mStkMenu);







