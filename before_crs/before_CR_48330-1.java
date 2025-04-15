/*Music: Modify the DownloadProvider to support the drm same file problem.

When we download the a drm file such as 1.dm.
we'll verify whether it is already exist and then modify the extension to fl such as 1.fl.
When we download again 1.dm, and verify if it's exist. But it has already been modified to 1.fl.
We should modify the extension first and then getfullpath of the file.

Change-Id:Ic5519065ccab960f623e8421f109ae74afd332a8Author: Huaqiang Chen <huaqiangx.chen@intel.com>
Signed-off-by: b618 <b618@borqs.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 52128*/
//Synthetic comment -- diff --git a/src/com/android/providers/downloads/Helpers.java b/src/com/android/providers/downloads/Helpers.java
//Synthetic comment -- index 359f6fa..94831b6 100644

//Synthetic comment -- @@ -90,10 +90,12 @@
destination);
}
storageManager.verifySpace(destination, path, contentLength);
        path = getFullPath(path, mimeType, destination, base);
if (DownloadDrmHelper.isDrmConvertNeeded(mimeType)) {
path = DownloadDrmHelper.modifyDrmFwLockFileExtension(path);
}
return path;
}








