/*Fix to edit window with WindowsBuilder.

Change-Id:I19e6933abac980cf688860e37436455bd5787909*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java
//Synthetic comment -- index 88fd754..b5122d7 100755

//Synthetic comment -- @@ -143,9 +143,7 @@
mStackLayout = new StackLayout();
mPagesRootComposite.setLayout(mStackLayout);

        mAvdManagerPage = new AvdManagerPage(mPagesRootComposite, mUpdaterData);
        mLocalPackagePage = new LocalPackagesPage(mPagesRootComposite, mUpdaterData);
        mRemotePackagesPage = new RemotePackagesPage(mPagesRootComposite, mUpdaterData);

mSashForm.setWeights(new int[] {150, 576});
}
//Synthetic comment -- @@ -212,6 +210,20 @@


/**
* Helper to return the SWT shell.
*/
private Shell getShell() {







