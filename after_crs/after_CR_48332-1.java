/*CertFileList: Fix installing certificates on SDcard's error.

The path to install cert file is not correct.
Title of prefrence has contained the External Storage Directory.

Change-Id:Ibb1163fac7e372e1ba6a4f92dbb25c73b6654b85Author: Liang Wang <liangx.wang@intel.com>
Borqs-Contact: b619 <b619@borqs.com>
Signed-off-by: Liang Wang <liangx.wang@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 51128*/




//Synthetic comment -- diff --git a/src/com/android/certinstaller/CertFileList.java b/src/com/android/certinstaller/CertFileList.java
//Synthetic comment -- index 5e2b681..b83da3c 100644

//Synthetic comment -- @@ -84,8 +84,7 @@
}

public boolean onPreferenceClick(Preference pref) {
        File file = new File(pref.getTitle().toString());
if (file.isDirectory()) {
Log.w(TAG, "impossible to pick a directory! " + file);
} else {







