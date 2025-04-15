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
When dragging items(touch & hold one item), Play Store create a new shortcut for the app which has just been installed.
This event triggers that the workspace rebind all items from the database, with old workspace widget items unbinded, in the case above, LauncherAppWidgetInfo.hostView = null.
When the item is released, onDrop() fails with "NullPointerException" upon the following statement is executed.
    final CellLayout parent = (CellLayout) cell.getParent().getParent();

Solution
Cancel the dragging operation when rebind happens.

Change-Id:I5ca3f7a39399b8512d52a267738665f6941977deAuthor: Erjun Ding <erjunx.ding@intel.com>
Signed-off-by: Guobin Zhang <guobin.zhang@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>*/




//Synthetic comment -- diff --git a/src/com/android/launcher2/Launcher.java b/src/com/android/launcher2/Launcher.java
//Synthetic comment -- index 8e074b5..77e078f 100644

//Synthetic comment -- @@ -2935,6 +2935,11 @@
public void bindItems(ArrayList<ItemInfo> shortcuts, int start, int end) {
setLoadOnResume();

        // Before all item views are re-created, cancel the ongoing dragging action which keeps old items view.
        if (mDragController.isDragging()) {
            mDragController.cancelDrag();
        }

final Workspace workspace = mWorkspace;
for (int i=start; i<end; i++) {
final ItemInfo item = shortcuts.get(i);







