/*Fix potential NPE during layout XML completion

Change-Id:Iaf9b29b153c688638b65aa16338dd105c2c55309*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java
//Synthetic comment -- index 7c4cba8..b7c95b5 100644

//Synthetic comment -- @@ -462,7 +462,9 @@
choices = currentUiNode.getAttributeDescriptors();
} else {
ElementDescriptor parentDesc = getDescriptor(parent);
                if (parentDesc != null) {
                    choices = parentDesc.getAttributes();
                }
}
}
return choices;







