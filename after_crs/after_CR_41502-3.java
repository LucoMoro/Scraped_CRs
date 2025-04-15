/*Fix Incorrect description for "SD card" and "USB Storage"

While getVolumeList(), Read VolumeList from storage_list.xml again to load localized Description string.

Change-Id:Ia7f2d4fb39ca29b01cdacb4b14784d18d96b1d36Author: Gang Li <gang.g.li@intel.com>
Signed-off-by: Gang Li <gang.g.li@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 39390*/




//Synthetic comment -- diff --git a/services/java/com/android/server/MountService.java b/services/java/com/android/server/MountService.java
//Synthetic comment -- index 04267a3..3bc3bc8 100644

//Synthetic comment -- @@ -1071,6 +1071,9 @@
int id = com.android.internal.R.xml.storage_list;
XmlResourceParser parser = resources.getXml(id);
AttributeSet attrs = Xml.asAttributeSet(parser);
        mVolumes.clear();
        mVolumeMap.clear();
        mPrimaryVolume = null;

try {
XmlUtils.beginDocument(parser, TAG_STORAGE_LIST);
//Synthetic comment -- @@ -1937,6 +1940,7 @@

public Parcelable[] getVolumeList() {
synchronized(mVolumes) {
            readStorageList();
int size = mVolumes.size();
Parcelable[] result = new Parcelable[size];
for (int i = 0; i < size; i++) {







