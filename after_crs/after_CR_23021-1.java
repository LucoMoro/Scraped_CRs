/*Add method to LayoutLibrary to query layoutlib api level and rev.

Change-Id:Ib70e560e7d0be88305383aac18726858d91990ae*/




//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java b/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java
//Synthetic comment -- index dd1a3dc..3d1c5db 100644

//Synthetic comment -- @@ -219,6 +219,33 @@
// ------ Layout Lib API proxy

/**
     * Returns the API level of the layout library.
     */
    public int getApiLevel() {
        if (mBridge != null) {
            return mBridge.getApiLevel();
        }

        if (mLegacyBridge != null) {
            return getLegacyApiLevel();
        }

        return 0;
    }

    /**
     * Returns the revision of the library inside a given (layoutlib) API level.
     * The true version number of the library is {@link #getApiLevel()}.{@link #getRevision()}
     */
    public int getRevision() {
        if (mBridge != null) {
            return mBridge.getRevision();
        }

        return 0;
    }

    /**
* Returns whether the LayoutLibrary supports a given {@link Capability}.
* @return true if it supports it.
*







