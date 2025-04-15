/*Fix NPE

Raphael ran into this; not sure how to reproduce, but the API for the
method called says it can return null so guard against it.

Change-Id:I2cf758aa68c6ab7e3b1d2f68110c8fc058735406*/




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







