/*Automatically safe files before refreshing lint.

Also fix bug where using the quickfix to add a suppress
attribute would replace rather than append to the existing
ignore attribute.

Change-Id:Ia6933a59655e972c49f2e80ac14c6af15acd39d4*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/AddSuppressAttribute.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/AddSuppressAttribute.java
//Synthetic comment -- index 3297756..cd077ec 100644

//Synthetic comment -- @@ -115,7 +115,7 @@

// Use the non-namespace form of set attribute since we can't
// reference the namespace until the model has been reloaded
                mElement.setAttribute(prefix + ':' + ATTR_IGNORE, mId);

UiElementNode rootUiNode = mEditor.getUiRootNode();
if (rootUiNode != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintViewPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintViewPart.java
//Synthetic comment -- index 956ee9d..e9f94cc 100644

//Synthetic comment -- @@ -229,7 +229,7 @@
mRemoveAllAction = new LintViewAction("Remove All", ACTION_REMOVE_ALL,
sharedImages.getImageDescriptor(ISharedImages.IMG_ELCL_REMOVEALL),
sharedImages.getImageDescriptor(ISharedImages.IMG_ELCL_REMOVEALL_DISABLED));
        mRefreshAction = new LintViewAction("Refresh", ACTION_REFRESH,
iconFactory.getImageDescriptor(REFRESH_ICON), null);
mRemoveAllAction.setEnabled(true);
mCollapseAll = new LintViewAction("Collapse All", ACTION_COLLAPSE,
//Synthetic comment -- @@ -461,6 +461,11 @@
public void run() {
switch (mAction) {
case ACTION_REFRESH: {
Job[] jobs = EclipseLintRunner.getCurrentJobs();
if (jobs.length > 0) {
EclipseLintRunner.cancelCurrentJobs(false);
//Synthetic comment -- @@ -471,9 +476,8 @@
}
Job job = EclipseLintRunner.startLint(resources, null,
false /*fatalOnly*/, false /*show*/);
                        if (job != null) {
job.addJobChangeListener(LintViewPart.this);
                            IWorkbench workbench = PlatformUI.getWorkbench();
ISharedImages sharedImages = workbench.getSharedImages();
setImageDescriptor(sharedImages.getImageDescriptor(
ISharedImages.IMG_ELCL_STOP));







