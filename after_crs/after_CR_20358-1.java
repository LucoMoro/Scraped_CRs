/*Reorder animation submenu

List local animations first, followed by the Create button, followed
by the frameworks animation submenu. Also drop the recent-item.

Change-Id:I238ab85f319383c138ce8e63fd19c7f97a23ea5e*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PlayAnimationMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PlayAnimationMenu.java
//Synthetic comment -- index 878c50e..03a446a 100644

//Synthetic comment -- @@ -59,8 +59,6 @@
private final LayoutCanvas mCanvas;
/** Whether this menu is showing local animations or framework animations */
private boolean mFramework;

/**
* Creates a "Play Animation" menu
//Synthetic comment -- @@ -97,15 +95,26 @@

GraphicalEditorPart graphicalEditor = mCanvas.getLayoutEditor().getGraphicalEditor();
if (graphicalEditor.renderingSupports(Capability.PLAY_ANIMATION)) {
            // List of animations
            Collection<String> animationNames = graphicalEditor.getResourceNames(mFramework,
                    ResourceType.ANIMATOR);
            if (animationNames.size() > 0) {
                // Sort alphabetically
                List<String> sortedNames = new ArrayList<String>(animationNames);
                Collections.sort(sortedNames);

                for (String animation : sortedNames) {
                    String title = animation;
                    IAction action = new PlayAnimationAction(title, animation, mFramework);
                    new ActionContributionItem(action).fill(menu, -1);
                }

                new Separator().fill(menu, -1);
            }

if (!mFramework) {
// Not in the framework submenu: include recent list and create new actions

// "Create New" action
new ActionContributionItem(new CreateAnimationAction()).fill(menu, -1);

//Synthetic comment -- @@ -114,23 +123,6 @@
PlayAnimationMenu sub = new PlayAnimationMenu(mCanvas, "Android Builtin", true);
new ActionContributionItem(sub).fill(menu, -1);
}
} else {
addDisabledMessageItem("Not supported on platform");
}
//Synthetic comment -- @@ -148,8 +140,6 @@

@Override
public void run() {
SelectionManager selectionManager = mCanvas.getSelectionManager();
List<SelectionItem> selection = selectionManager.getSelections();
SelectionItem canvasSelection = selection.get(0);







