/*Fix NPE when opening layout and target is still loading.

Change-Id:Ie4541646447669162ee303ef02d9bb81513e8e1e*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java
//Synthetic comment -- index c059a41..a250c76 100644

//Synthetic comment -- @@ -505,7 +505,10 @@
IAndroidTarget target = currentSdk.getTarget(project);
if (target != null) {
AndroidTargetData data = currentSdk.getTargetData(target);
                    desc = data.getLayoutDescriptors().getBaseViewDescriptor();
}
}








