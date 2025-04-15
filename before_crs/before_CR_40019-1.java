/*One more keybinding fix

Fix bug where the activated() method on the AndroidXmlEditor
can also get called on an editor in graphical mode, which
would rebind actions to the text editor when in graphical
mode, meaning that cut/copy/paste/select etc would apply
to the text buffer behind the editor.

Change-Id:I311e5b9a83c988128bcb6ccb042346fc98a9d10b*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java
//Synthetic comment -- index a2c032d..21e3ca1 100644

//Synthetic comment -- @@ -1581,7 +1581,9 @@

/** Called when this editor is activated */
public void activated() {
        updateActionBindings();
}

/** Called when this editor is deactivated */







