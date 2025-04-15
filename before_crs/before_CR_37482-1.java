/*Fix potential NPE

Change-Id:Ied6d4b2e03cf13078f0468c6203ca1c8bac71499*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index 48cebee..42423b1 100644

//Synthetic comment -- @@ -1499,7 +1499,10 @@
IWorkspace ws = ResourcesPlugin.getWorkspace();
GlobalProjectMonitor.stopMonitoring(ws);

        mRed.dispose();
}

/**







