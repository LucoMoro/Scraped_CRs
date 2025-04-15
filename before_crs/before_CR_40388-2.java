/*MtpStorage: correct the size of reserve space for MTP

According to description in frameworks/base/core/res/res/xml/storage_list.xml,
"mtpReserve: (integer) number of megabytes of storage MTP should reserve for free storage".
Mtpstorage class use it directly and doesn't change it to correct size in megabyes.
Mtp initor can not get correct storage information from android.

Change-Id:Icf59eb1eb478e67ea5990be96a9decb41aa55504Signed-off-by: Bo Huang <bo.b.huang@intel.com>
Signed-off-by: Jack Ren<jack.ren@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>*/
//Synthetic comment -- diff --git a/media/java/android/mtp/MtpStorage.java b/media/java/android/mtp/MtpStorage.java
//Synthetic comment -- index 2f47aad..9cf65a3 100644

//Synthetic comment -- @@ -39,7 +39,7 @@
mStorageId = volume.getStorageId();
mPath = volume.getPath();
mDescription = context.getResources().getString(volume.getDescriptionId());
        mReserveSpace = volume.getMtpReserveSpace();
mRemovable = volume.isRemovable();
mMaxFileSize = volume.getMaxFileSize();
}
//Synthetic comment -- @@ -87,7 +87,7 @@
* Returns the amount of space to reserve on the storage file system.
* This can be set to a non-zero value to prevent MTP from filling up the entire storage.
*
     * @return the storage unit description
*/
public final long getReserveSpace() {
return mReserveSpace;







