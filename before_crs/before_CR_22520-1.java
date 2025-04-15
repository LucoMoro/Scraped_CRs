/*workaround for sometimes generated random call on clicked a call button in the call log

If a user releases a call button when the call log is redrawn
by the contacts information, sometimes wrong number is called
by a cached view with unorder.
So on clicking, a call number is used not a number in current cached view
but a number in a view at a touched moment
this is workaround.

Change-Id:I2d32f9177ab55f5df2bcf1c88d78a7e008609fcfSigned-off-by: SungHyun Kwon <sh.kwon@lge.com>*/
//Synthetic comment -- diff --git a/src/com/android/contacts/RecentCallsListActivity.java b/src/com/android/contacts/RecentCallsListActivity.java
//Synthetic comment -- index abda325..56f3d10 100644

//Synthetic comment -- @@ -64,6 +64,7 @@
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
//Synthetic comment -- @@ -184,9 +185,14 @@
*/
private static int sFormattingType = FORMATTING_TYPE_INVALID;

/** Adapter class to fill in data for the Call Log */
final class RecentCallsAdapter extends GroupingListAdapter
            implements Runnable, ViewTreeObserver.OnPreDrawListener, View.OnClickListener {
HashMap<String,ContactInfo> mContactInfo;
private final LinkedList<CallerInfoQuery> mRequests;
private volatile boolean mDone;
//Synthetic comment -- @@ -210,7 +216,11 @@
private CharArrayBuffer mBuffer2 = new CharArrayBuffer(128);

public void onClick(View view) {
            String number = (String) view.getTag();
if (!TextUtils.isEmpty(number)) {
// Here, "number" can either be a PSTN phone number or a
// SIP address.  So turn it into either a tel: URI or a
//Synthetic comment -- @@ -226,6 +236,13 @@
}
}

public boolean onPreDraw() {
if (mFirst) {
mHandler.sendEmptyMessageDelayed(START_THREAD, 1000);
//Synthetic comment -- @@ -610,6 +627,7 @@
views.iconView = (ImageView) view.findViewById(R.id.call_type_icon);
views.callView = view.findViewById(R.id.call_icon);
views.callView.setOnClickListener(this);
views.groupIndicator = (ImageView) view.findViewById(R.id.groupIndicator);
views.groupSize = (TextView) view.findViewById(R.id.groupSize);
view.setTag(views);







