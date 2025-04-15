/*fix the null pointer issue in OPP

There is a dereference issue concerning
the 'db' variable in the 'query' function of
the BluetoothOppProvider class.
The fix prevents the call indicated as been to origin
of the dereferencement problem is this reference is null.

Change-Id:Iff5963ba8fde179505d4188ab76a1e3651138774Author: Ovidiu Beldie <ovidiux.beldie@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 34345*/
//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppProvider.java b/src/com/android/bluetooth/opp/BluetoothOppProvider.java
//Synthetic comment -- index 864d6a2..6bcf95e 100644

//Synthetic comment -- @@ -374,14 +374,17 @@
Log.v(TAG, sb.toString());
}

        Cursor ret = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        if (ret != null) {
            ret.setNotificationUri(getContext().getContentResolver(), uri);
            if (V) Log.v(TAG, "created cursor " + ret + " on behalf of ");// +
        } else {
            if (D) Log.d(TAG, "query failed in downloads database");
}

return ret;
}







