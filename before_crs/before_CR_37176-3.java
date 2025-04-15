/*ForceClose: Launcher crashed if you touch & hold one item on the Home screen when PlayStore are installing apps.

Reproduced on Galaxy Nexus
1. Add widgets to Home screen, until no more room on this Home screen, on my Galaxy Nexus, 4 Analog clock widgets fulfill it.
2. Wifi toggled on and connected to the Internet.
3. Launch Play Store, install one app, e.g. Apps->Editors'Choice->BBC NEWS.
4. Go back to the Home screen which is full of widgets.
5. When the downloading is almost done (e.g. approaching 90%), touch and hold one widget, seeing the Remove icon on the top of the screen.
6. Wait a sencond, you will see notifications on Status Bar like this Installing "BBC News"..., then, Successfully installed "BBC News".
7. Then, you will see toast notifications: "No more room in this Home screen", Shortcut "BBC News" created.
8. Release the widget you have been holding so long.
Unexpected Force Close dialog popup: Unfortunately, Launcher has stopped.

Analysis
When dragging item(touch & hold one item), the target item view is kept in mDragInfo.cell.
In this case, PlayStore adds a new shortcut into the database.
Observing the database change, LauncherApplication will refresh all the items shown on the workspace.
First, removeAllViewsInLayout() in startBinding() will set all previous items view's parent to null.
Then rebind all items, reinflate all the items view, ...
When the dragging item is released, onDrop() in Launcher.java will be executed.
The statement below uses mDragInfo.cell whose parent is null, it fails with "NullPointerException".
    final CellLayout parent = (CellLayout) cell.getParent().getParent();

Solution
Cancel the dragging operation before rebind starts

Change-Id:I5ca3f7a39399b8512d52a267738665f6941977deAuthor: Erjun Ding <erjunx.ding@intel.com>
Signed-off-by: Guobin Zhang <guobin.zhang@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>*/
//Synthetic comment -- diff --git a/src/com/android/launcher2/Launcher.java b/src/com/android/launcher2/Launcher.java
//Synthetic comment -- index 8e074b5..007b96d 100644

//Synthetic comment -- @@ -2915,6 +2915,11 @@
final Workspace workspace = mWorkspace;

mWorkspace.clearDropTargets();
int count = workspace.getChildCount();
for (int i = 0; i < count; i++) {
// Use removeAllViewsInLayout() to avoid an extra requestLayout() and invalidate().







