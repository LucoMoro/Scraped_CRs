/*Merge 677a2489 from master. do not merge.

Add revision number to the layoutlib.

Change-Id:Icfceb6b6328ed445ecc1bdefe7fc14192ba39328*/
//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/Bridge.java b/layoutlib_api/src/com/android/ide/common/rendering/api/Bridge.java
//Synthetic comment -- index e21d039..d5a1829 100644

//Synthetic comment -- @@ -44,6 +44,14 @@
public abstract int getApiLevel();

/**
* Returns an {@link EnumSet} of the supported {@link Capability}.
* @return an {@link EnumSet} with the supported capabilities.
*







