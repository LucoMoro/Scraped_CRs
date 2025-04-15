/*One more keybinding fix

Fix bug where the activated() method on the AndroidXmlEditor
can also get called on an editor in graphical mode, which
would rebind actions to the text editor when in graphical
mode, meaning that cut/copy/paste/select etc would apply
to the text buffer behind the editor.

(cherry picked from commit 455d4e450502e038bc03a1959ebfcef9cf14c7ee)

Change-Id:Ieff8d215cb61741c9eef78d867f1f7709e349b0e*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java
//Synthetic comment -- index 6f7c5d3..5a725ef 100644

//Synthetic comment -- @@ -1579,7 +1579,9 @@

/** Called when this editor is activated */
public void activated() {
        if (getActivePage() == mTextPageIndex) {
            updateActionBindings();
        }
}

/** Called when this editor is deactivated */







