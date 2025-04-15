/*resolver list is not cleared on rebuild

Refactoring ResolverActivity.java

Change-Id:I84e6420ff98ddcafd27a52b041ddb1c50e7d7362Signed-off-by: You Kim <you.kim72@gmail.com>*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/app/ResolverActivity.java b/core/java/com/android/internal/app/ResolverActivity.java
//Synthetic comment -- index e63c57f..c22cd26 100644

//Synthetic comment -- @@ -35,7 +35,6 @@
import android.net.Uri;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.os.Process;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
//Synthetic comment -- @@ -64,6 +63,7 @@
*/
public class ResolverActivity extends AlertActivity implements AdapterView.OnItemClickListener {
private static final String TAG = "ResolverActivity";

private int mLaunchedFromUid;
private ResolveListAdapter mAdapter;
//Synthetic comment -- @@ -323,7 +323,7 @@
|| (!"file".equals(data.getScheme())
&& !"content".equals(data.getScheme()))) {
filter.addDataScheme(data.getScheme());
    
// Look through the resolved filter to determine which part
// of it matched the original Intent.
Iterator<IntentFilter.AuthorityEntry> aIt = ri.filter.authoritiesIterator();
//Synthetic comment -- @@ -402,7 +402,6 @@
private final int mLaunchedFromUid;
private final LayoutInflater mInflater;

        private List<ResolveInfo> mCurrentResolveList;
private List<DisplayResolveInfo> mList;

public ResolveListAdapter(Context context, Intent intent,
//Synthetic comment -- @@ -413,6 +412,7 @@
mBaseResolveList = rList;
mLaunchedFromUid = launchedFromUid;
mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
rebuildList();
}

//Synthetic comment -- @@ -420,22 +420,23 @@
final int oldItemCount = getCount();
rebuildList();
notifyDataSetChanged();
            if (mList.size() <= 0) {
// We no longer have any items...  just finish the activity.
finish();
            }

            final int newItemCount = getCount();
            if (newItemCount != oldItemCount) {
resizeGrid();
}
}

private void rebuildList() {
if (mBaseResolveList != null) {
                mCurrentResolveList = mBaseResolveList;
} else {
                mCurrentResolveList = mPm.queryIntentActivities(
mIntent, PackageManager.MATCH_DEFAULT_ONLY
| (mAlwaysUseOption ? PackageManager.GET_RESOLVED_FILTER : 0));
// Filter out any activities that the launched uid does not
//Synthetic comment -- @@ -443,36 +444,36 @@
// list of resolved activities, because that only happens when
// we are being subclassed, so we can safely launch whatever
// they gave us.
                if (mCurrentResolveList != null) {
                    for (int i=mCurrentResolveList.size()-1; i >= 0; i--) {
                        ActivityInfo ai = mCurrentResolveList.get(i).activityInfo;
int granted = ActivityManager.checkComponentPermission(
ai.permission, mLaunchedFromUid,
ai.applicationInfo.uid, ai.exported);
if (granted != PackageManager.PERMISSION_GRANTED) {
// Access not allowed!
                            mCurrentResolveList.remove(i);
}
}
}
}
int N;
            if ((mCurrentResolveList != null) && ((N = mCurrentResolveList.size()) > 0)) {
// Only display the first matches that are either of equal
// priority or have asked to be default options.
                ResolveInfo r0 = mCurrentResolveList.get(0);
for (int i=1; i<N; i++) {
                    ResolveInfo ri = mCurrentResolveList.get(i);
                    if (false) Log.v(
"ResolveListActivity",
r0.activityInfo.name + "=" +
r0.priority + "/" + r0.isDefault + " vs " +
ri.activityInfo.name + "=" +
ri.priority + "/" + ri.isDefault);
                   if (r0.priority != ri.priority ||
r0.isDefault != ri.isDefault) {
while (i < N) {
                            mCurrentResolveList.remove(i);
N--;
}
}
//Synthetic comment -- @@ -480,11 +481,8 @@
if (N > 1) {
ResolveInfo.DisplayNameComparator rComparator =
new ResolveInfo.DisplayNameComparator(mPm);
                    Collections.sort(mCurrentResolveList, rComparator);
}
                
                mList = new ArrayList<DisplayResolveInfo>();
                
// First put the initial items at the top.
if (mInitialIntents != null) {
for (int i=0; i<mInitialIntents.length; i++) {
//Synthetic comment -- @@ -512,10 +510,10 @@
ri.loadLabel(getPackageManager()), null, ii));
}
}
                
// Check for applications with same name and use application name or
// package name if necessary
                r0 = mCurrentResolveList.get(0);
int start = 0;
CharSequence r0Label =  r0.loadLabel(mPm);
mShowExtended = false;
//Synthetic comment -- @@ -523,7 +521,7 @@
if (r0Label == null) {
r0Label = r0.activityInfo.packageName;
}
                    ResolveInfo ri = mCurrentResolveList.get(i);
CharSequence riLabel = ri.loadLabel(mPm);
if (riLabel == null) {
riLabel = ri.activityInfo.packageName;
//Synthetic comment -- @@ -531,13 +529,13 @@
if (riLabel.equals(r0Label)) {
continue;
}
                    processGroup(mCurrentResolveList, start, (i-1), r0, r0Label);
r0 = ri;
r0Label = riLabel;
start = i;
}
// Process last group
                processGroup(mCurrentResolveList, start, (N-1), r0, r0Label);
}
}

//Synthetic comment -- @@ -589,18 +587,10 @@
}

public ResolveInfo resolveInfoForPosition(int position) {
            if (mList == null) {
                return null;
            }

return mList.get(position).ri;
}

public Intent intentForPosition(int position) {
            if (mList == null) {
                return null;
            }

DisplayResolveInfo dri = mList.get(position);

Intent intent = new Intent(dri.origIntent != null
//Synthetic comment -- @@ -614,11 +604,11 @@
}

public int getCount() {
            return mList != null ? mList.size() : 0;
}

public Object getItem(int position) {
            return position;
}

public long getItemId(int position) {







