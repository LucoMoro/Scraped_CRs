/*Fix ADT to build with the new layoutlib API.

Also make the custom cleaning of the layoutlib looper
only done through API 4. Newer bridge can do their own clean up.

Change-Id:I1ee128e09912df53e110094d8909f81bc6a788e3*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java
//Synthetic comment -- index f18bc6f..5f93387 100644

//Synthetic comment -- @@ -154,6 +154,14 @@
return getCurrentNode();
}

    /**
     * This implementation does nothing for now as all the embedded XML will use a normal KXML
     * parser.
     */
    public IXmlPullParser getParser(String layoutName) {
        return null;
    }

// ------------- XmlPullParser --------

public String getPositionDescription() {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/WidgetPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/WidgetPullParser.java
//Synthetic comment -- index 4343ce7..2d8a2b2 100644

//Synthetic comment -- @@ -53,6 +53,11 @@
return mDescriptor;
}

    public IXmlPullParser getParser(String layoutName) {
        // there's no embedded layout for a single widget.
        return null;
    }

public int getAttributeCount() {
return mAttributes.length; // text attribute
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java
//Synthetic comment -- index c388545..a9dc43e 100644

//Synthetic comment -- @@ -63,19 +63,21 @@
* versions of the layoutlib.
*/
public void cleanUp() {
            if (apiLevel <= 4) {
                try {
                    Class<?> looperClass = classLoader.loadClass("android.os.Looper"); //$NON-NLS-1$
                    Field threadLocalField = looperClass.getField("sThreadLocal"); //$NON-NLS-1$
                    if (threadLocalField != null) {
                        threadLocalField.setAccessible(true);
                        // get object. Field is static so no need to pass an object
                        ThreadLocal<?> threadLocal = (ThreadLocal<?>) threadLocalField.get(null);
                        if (threadLocal != null) {
                            threadLocal.remove();
                        }
}
                } catch (Exception e) {
                    AdtPlugin.log(e, "Failed to clean up bridge for API level %d", apiLevel);
}
}
}
}







