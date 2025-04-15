/*Use LayoutLog.TAG_RESOURCES_PREFIX

Change-Id:I17f9030debd05cd4a8c3e17c3656e4b5eeec3b00*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 263b6c8..4f3403e 100644

//Synthetic comment -- @@ -1834,8 +1834,7 @@
AdtPlugin.log(e, null);
}

            // From BridgeConstants.TAG_RESOURCES_RESOLVE, TAG_RESOURCES_READ, etc:
            if (hasAaptErrors && logger.seenTagPrefix("resources.")) { //$NON-NLS-1$
// Text will automatically be wrapped by the error widget so no reason
// to insert linebreaks in this error message:
String message =







