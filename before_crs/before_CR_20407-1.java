/*Misc fix in ProjectCallback.

Change-Id:I35098d93f54c27c5488e1119fe8dab75492c6899*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index 3656e4d..8575a24 100644

//Synthetic comment -- @@ -75,6 +75,7 @@
public Object loadView(String className, Class[] constructorSignature,
Object[] constructorParameters)
throws ClassNotFoundException, Exception {

// look for a cached version
Class<?> clazz = mLoadedClasses.get(className);
//Synthetic comment -- @@ -90,29 +91,27 @@
}
clazz = mLoader.loadClass(className);
} catch (Exception e) {
            // Log this error with the class name we're trying to load
            AdtPlugin.log(e, "ProjectCallback.loadView failed to find class %1$s", //$NON-NLS-1$
                    className);

// Add the missing class to the list so that the renderer can print them later.
mMissingClasses.add(className);
}

try {
if (clazz != null) {
                mUsed = true;
mLoadedClasses.put(className, clazz);
                return instantiateClass(clazz, constructorSignature, constructorParameters);
}
} catch (Throwable e) {
            // Find root cause
while (e.getCause() != null) {
e = e.getCause();
}

            AdtPlugin.log(e,
                    "ProjectCallback.loadView failed to instantiate class %1$s", //$NON-NLS-1$
                    className);

// Add the missing class to the list so that the renderer can print them later.
mBrokenClasses.add(className);
//Synthetic comment -- @@ -128,7 +127,6 @@
Method m = view.getClass().getMethod("setText",
new Class<?>[] { CharSequence.class });
m.invoke(view, getShortClassName(className));
            mUsed = true;
return view;
} catch (Exception e) {
// We failed to create and return a mock view.







