/*Finish the window in onItemLongClick() to replace in onStop().

Event will send to new activity to replace "Choose an action" activity.
ANR is voided.

rootcause:
The ResolverActivity is finished in onStop() and causes window state is abnormal.

Change-Id:I926fa4a47ecd04f1f211b29f757dcee720b09deeAuthor: Bing Deng <bingx.deng@intel.com>
Signed-off-by: Bing Deng <bingx.deng@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 59540*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/app/ResolverActivity.java b/core/java/com/android/internal/app/ResolverActivity.java
//Synthetic comment -- index c22cd26..4b76e019 100644

//Synthetic comment -- @@ -228,18 +228,6 @@
mPackageMonitor.unregister();
mRegistered = false;
}
        if ((getIntent().getFlags()&Intent.FLAG_ACTIVITY_NEW_TASK) != 0) {
            // This resolver is in the unusual situation where it has been
            // launched at the top of a new task.  We don't let it be added
            // to the recent tasks shown to the user, and we need to make sure
            // that each time we are launched we get the correct launching
            // uid (not re-using the same resolver from an old launching uid),
            // so we will now finish ourself since being no longer visible,
            // the user probably can't get back to us.
            if (!isChangingConfigurations()) {
                finish();
            }
        }
}

@Override
//Synthetic comment -- @@ -656,6 +644,18 @@
public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
ResolveInfo ri = mAdapter.resolveInfoForPosition(position);
showAppDetails(ri);
return true;
}








