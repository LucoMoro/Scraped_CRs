/*Fix bug in project-wide render target selection

I ran into a scenario where selecting a render target caused the new
target to be loaded, but then the selection jumped back to the
previous value. The reason for this is that there is a codepath from
the dropdown callback which ends up calling "syncRenderState" before
the old value has been saved, so syncRenderState will read the
previous value and sync it back to the dropdown. The fix is easy --
make sure we save the render state before notifying clients of the
change.

Change-Id:I28d8d8ec1e9c23311892ca05ab4348ae5aed90ae*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 0124111..4c6bac8 100644

//Synthetic comment -- @@ -2178,12 +2178,12 @@
mListener.onRenderingTargetPostChange(mRenderingTarget);
}

        // Store project-wide render-target setting
        saveRenderState();

if (computeOk &&  mListener != null) {
mListener.onConfigurationChange();
}
}

/**







