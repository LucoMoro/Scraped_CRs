/*Hide ClassNotFoundExceptions from the error console.

When a class is not found during rendering, the name of the class (all
by itself, with no other explanation) will show up in the error
console. That's because we include all the render errors there.

However, class loading failures is handled specially in the project
callback, where the names of missing and broken classes are recorded
and later presented in a special way with hyperlinks. Therefore, we
don't need this first error listing of the missing class, especially
since it's confusing since it just lists the class name all by itself.

This changeset simply hides ClassNotFoundExceptions from the error
console since they will be listed in a cleaner way by the separate
mechanism.

NOTE: The exception *will* go to the IDE log as before, it just won't
appear in the user-visible error console shown below the layout
editor.

Change-Id:I34e90ba26ee77f8b3c56f528aaaf7fddca6620ad*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderLogger.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderLogger.java
//Synthetic comment -- index 5ec50c9..ea9e568 100644

//Synthetic comment -- @@ -102,6 +102,17 @@
String description = describe(message);
AdtPlugin.log(throwable, "%1$s: %2$s", mName, description);
if (throwable != null) {
mHaveExceptions = true;
}








