/*ExternalStorageFormatter: check storage path before update state

Disconnect U-Disk while erasing SD card will let ExternalStorageFormatter
get an event to notice that external storage state is changed, and its state
needs to be updated.

ExternalStorageFormatter will update the state of SD card wrongly. So before
update state, check whether the event is for the real external SD card. If not,
ignore this event.

Change-Id:I603c7290a616d7fca89877bda39becee88bda8a4Author: Chuanxiao Dong <chuanxiao.dong@intel.com>
Signed-off-by: Chuanxiao Dong <chuanxiao.dong@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 36979*/




//Synthetic comment -- diff --git a/core/java/com/android/internal/os/storage/ExternalStorageFormatter.java b/core/java/com/android/internal/os/storage/ExternalStorageFormatter.java
//Synthetic comment -- index 3905c88..549db03 100644

//Synthetic comment -- @@ -57,7 +57,11 @@
Log.i(TAG, "Received storage state changed notification that " +
path + " changed state from " + oldState +
" to " + newState);
            if (getExternalStoragePath().equals(path))
                updateProgressState();
            else
                Log.i(TAG, "Ignore " + path + " since it is not external storage path "
                           + getExternalStoragePath());
}
};








