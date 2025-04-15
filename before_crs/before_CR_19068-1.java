/*Remove some asserts.

Change-Id:I8458dc9237506c542c766e6119261ae4e13b453b*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RepoSourcesAdapter.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RepoSourcesAdapter.java
//Synthetic comment -- index 05d0d43..e7fc74c 100755

//Synthetic comment -- @@ -145,7 +145,7 @@

// Called when the input is set or changed on the provider
public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            assert newInput == RepoSourcesAdapter.this;
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdateNoWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdateNoWindow.java
//Synthetic comment -- index 53ddd1e..f390e72 100755

//Synthetic comment -- @@ -193,9 +193,9 @@
* Increments the current value of the progress bar.
*/
public void incProgress(int delta) {
            assert mIncCoef > 0;
            assert delta > 0;
            internalIncProgress(delta * mIncCoef);
}

private void internalIncProgress(double realDelta) {
//Synthetic comment -- @@ -309,8 +309,9 @@
}

public void incProgress(int delta) {
            assert mSubCoef > 0;
            subIncProgress(delta * mSubCoef);
}

public void subIncProgress(double realDelta) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressTask.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressTask.java
//Synthetic comment -- index f958a40..db2b781 100755

//Synthetic comment -- @@ -93,9 +93,9 @@
* This method can be invoked from a non-UI thread.
*/
public void incProgress(int delta) {
        assert mIncCoef > 0;
        assert delta > 0;
        internalIncProgress(delta * mIncCoef);
}

private void internalIncProgress(double realDelta) {
//Synthetic comment -- @@ -235,8 +235,9 @@
}

public void incProgress(int delta) {
            assert mSubCoef > 0;
            subIncProgress(delta * mSubCoef);
}

public void subIncProgress(double realDelta) {







