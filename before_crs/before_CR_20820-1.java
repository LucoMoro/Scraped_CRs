/*Guard against possible NPE.

Change-Id:I4be9b1659aa1394bea0f24f34fb4681e781b96ae*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index cb5c849..eda6514 100644

//Synthetic comment -- @@ -1519,7 +1519,7 @@
canvas.setSession(session, explodeNodes);

// update the UiElementNode with the layout info.
        if (session.getResult().isSuccess() == false) {
// An error was generated. Print it (and any other accumulated warnings)
String errorMessage = session.getResult().getErrorMessage();
Throwable exception = session.getResult().getException();







