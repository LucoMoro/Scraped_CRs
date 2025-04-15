/*MTBF: ANR com.android.settings (getInstalledApplications)

1,The ANR is caused there is no touch window to dispatch key event.
2,No touch window is caused the settings app have no memory to continue the resume process,
  so that touch window can not be created for more than 5s.
3,From the MTBF, the settings activity com.android.settings/.applications.InstalledAppDetails
 will leak about 210k. memory every pause/resume process
4,the mSession will be created on resume process and will not be destroyed on pause process

Change-Id:Ibfa78a71c89941548d8dba5c8d1dc51b16abe5bbAuthor: Erjun Ding <erjunx.ding@intel.com>
Signed-off-by: Erjun Ding <erjunx.ding@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 51006*/




//Synthetic comment -- diff --git a/src/com/android/settings/applications/InstalledAppDetails.java b/src/com/android/settings/applications/InstalledAppDetails.java
//Synthetic comment -- index 7241fdc..145ad5b 100644

//Synthetic comment -- @@ -572,6 +572,12 @@
}

@Override
    public void onDestroy() {
        super.onDestroy();
        mSession.release();
    }

    @Override
public void onAllSizesComputed() {
}








