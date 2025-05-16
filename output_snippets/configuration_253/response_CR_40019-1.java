//<Beginning of snippet n. 0>

private boolean isGraphicalMode; // Assume this flag indicates graphical mode

/** Called when this editor is activated */
public void activated() {
        if (isGraphicalMode) {
            return;
        }
        updateActionBindings();
}

/** Called when this editor is deactivated */

//<End of snippet n. 0>