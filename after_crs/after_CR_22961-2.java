/*Add revision number to the layoutlib.

Change-Id:I9a990c41c46ce9901a241b18aee00d1fc4c84383*/




//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/Bridge.java b/layoutlib_api/src/com/android/ide/common/rendering/api/Bridge.java
//Synthetic comment -- index e21d039..d5a1829 100644

//Synthetic comment -- @@ -44,6 +44,14 @@
public abstract int getApiLevel();

/**
     * Returns the revision of the library inside a given (layoutlib) API level.
     * The true revision number of the library is {@link #getApiLevel()}.{@link #getRevision()}
     */
    public int getRevision() {
        return 0;
    }

    /**
* Returns an {@link EnumSet} of the supported {@link Capability}.
* @return an {@link EnumSet} with the supported capabilities.
*







