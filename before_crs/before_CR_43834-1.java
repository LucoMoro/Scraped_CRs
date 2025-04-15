/*gltrace: Show scroll bars.

Change-Id:I2ae46d99d65e527840ef4536665a15453b1fef7b*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/views/detail/ShaderSourceDetailsProvider.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/views/detail/ShaderSourceDetailsProvider.java
//Synthetic comment -- index a3ee578..ca986c3 100644

//Synthetic comment -- @@ -40,7 +40,8 @@

@Override
public void createControl(Composite parent) {
        mTextControl = new Text(parent, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
mTextControl.setEditable(false);
}








