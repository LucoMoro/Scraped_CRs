<<Beginning of snippet n. 0>>
/** Called when this editor is activated */
public void activated() {
    if (isTextMode()) {
        updateActionBindings();
    } else {
        preventTextActionBindings();
    }
}

/** Prevents text action bindings when in graphical mode */
private void preventTextActionBindings() {
    disableCut();
    disableCopy();
    disablePaste();
    disableSelect();
}

private void disableCut() {
    // Logic to disable cut action
}

private void disableCopy() {
    // Logic to disable copy action
}

private void disablePaste() {
    // Logic to disable paste action
}

private void disableSelect() {
    // Logic to disable select action
}

/** Method to check if the editor is in text mode */
private boolean isTextMode() {
    // Enhanced logic to determine if the editor is in text mode
    // Example implementation
    return this.editorMode == EditorMode.TEXT; // Assuming editorMode is a field representing the current mode
}

/** Called when this editor is deactivated */
<<End of snippet n. 0>>