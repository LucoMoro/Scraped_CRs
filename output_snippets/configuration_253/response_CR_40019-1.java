//<Beginning of snippet n. 0>

private boolean isGraphicalMode = false; // Example boolean for mode tracking

/** Called when this editor is activated */
public void activated() {
    if (isGraphicalMode) {
        return; // Prevent action bindings in graphical mode
    }
    updateActionBindings();
}

/** Called when this editor is deactivated */

//<End of snippet n. 0>