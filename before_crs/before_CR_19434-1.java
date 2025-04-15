/*Remove broken assert.

The assert can be false in normal condition.

Change-Id:I7181556e3ba32f765eca8e14997622557402a2da*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/LocalSdkAdapter.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/LocalSdkAdapter.java
//Synthetic comment -- index 444e998..83aa0c9 100755

//Synthetic comment -- @@ -84,7 +84,6 @@

// Called when the input is set or changed on the provider
public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            assert newInput == LocalSdkAdapter.this;
}

/**







