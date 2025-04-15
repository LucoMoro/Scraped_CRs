/*[BT]: fixes ANR for tethering scenario

The ANR occured due to excessive locking
and a slow native call to set tethering parameters.
This fix reduces the scope of the synchronization
to the section of the code that actually requires it.

Change-Id:I15fd7dc77870c9b3b7254ee1a883270a08d1d9fbAuthor: Ovidiu Beldie <ovidiux.beldie@intel.com>
Signed-off-by: Ovidiu Beldie <ovidiux.beldie@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 35907*/




//Synthetic comment -- diff --git a/core/java/android/server/BluetoothService.java b/core/java/android/server/BluetoothService.java
//Synthetic comment -- index 6296b11..cf2ecdc 100755

//Synthetic comment -- @@ -580,7 +580,7 @@
mAdapterSdpHandles = addReservedServiceRecordsNative(svcIdentifiers);
}

    private void updateSdpRecords() {
ArrayList<ParcelUuid> uuids = new ArrayList<ParcelUuid>();

Resources R = mContext.getResources();
//Synthetic comment -- @@ -617,9 +617,11 @@
}

// Cannot cast uuids.toArray directly since ParcelUuid is parcelable
        synchronized (this) {
            mAdapterUuids = new ParcelUuid[uuids.size()];
            for (int i = 0; i < uuids.size(); i++) {
                mAdapterUuids[i] = uuids.get(i);
            }
}
}








