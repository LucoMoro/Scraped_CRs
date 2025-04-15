/*Enhance Android Classpath Container - code style 1

Change-Id:If14e5d27b5255f891523d39763685bea5815a917*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/AndroidClasspathContainerPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/AndroidClasspathContainerPage.java
//Synthetic comment -- index 4407ab4..53f5809 100644

//Synthetic comment -- @@ -51,11 +51,11 @@

private IStatus mCurrStatus;

    private boolean mPageVisible;

public AndroidClasspathContainerPage() {
super("AndroidClasspathContainerPage"); //$NON-NLS-1$
        mPageVisible = false;
mCurrStatus = new StatusInfo();
setTitle("Android Libraries");
setDescription("This container manages classpath entries for Android container");
//Synthetic comment -- @@ -132,7 +132,7 @@

public void setVisible(boolean visible) {
super.setVisible(visible);
        mPageVisible = visible;
// policy: wizards are not allowed to come up with an error message
if (visible && mCurrStatus.matches(IStatus.ERROR)) {
StatusInfo status = new StatusInfo();
//Synthetic comment -- @@ -150,7 +150,7 @@
protected void updateStatus(IStatus status) {
mCurrStatus = status;
setPageComplete(!status.matches(IStatus.ERROR));
        if (mPageVisible) {
StatusUtil.applyToStatusLine(this, status);
}
}







