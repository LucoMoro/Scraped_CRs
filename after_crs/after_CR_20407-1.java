/*Misc fix in ProjectCallback.

Change-Id:I35098d93f54c27c5488e1119fe8dab75492c6899*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index 3656e4d..8575a24 100644

//Synthetic comment -- @@ -75,6 +75,7 @@
public Object loadView(String className, Class[] constructorSignature,
Object[] constructorParameters)
throws ClassNotFoundException, Exception {
        mUsed = true;

// look for a cached version
Class<?> clazz = mLoadedClasses.get(className);
//Synthetic comment -- @@ -90,29 +91,27 @@
}
clazz = mLoader.loadClass(className);
} catch (Exception e) {
// Add the missing class to the list so that the renderer can print them later.
            // no need to log this.
mMissingClasses.add(className);
}

try {
if (clazz != null) {
                // first try to instantiate it because adding it the list of loaded class so that
                // we don't add broken classes.
                Object view = instantiateClass(clazz, constructorSignature, constructorParameters);
mLoadedClasses.put(className, clazz);

                return view;
}
} catch (Throwable e) {
            // Find root cause to log it.
while (e.getCause() != null) {
e = e.getCause();
}

            AdtPlugin.log(e, "%1$s failed to instantiate.", className); //$NON-NLS-1$

// Add the missing class to the list so that the renderer can print them later.
mBrokenClasses.add(className);
//Synthetic comment -- @@ -128,7 +127,6 @@
Method m = view.getClass().getMethod("setText",
new Class<?>[] { CharSequence.class });
m.invoke(view, getShortClassName(className));
return view;
} catch (Exception e) {
// We failed to create and return a mock view.







