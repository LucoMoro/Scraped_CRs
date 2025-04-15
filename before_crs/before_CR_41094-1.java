/*gltrace: restore checkbox settings between invocations

Change-Id:Ic910902519eed907a184f5f808aa2aabaa558fa9*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/GLTraceOptionsDialog.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/GLTraceOptionsDialog.java
//Synthetic comment -- index 43db5d8..c7772a2 100644

//Synthetic comment -- @@ -72,9 +72,9 @@
private String mActivityToTrace = "";
private String mTraceFilePath = "";

    private boolean mCollectFbOnEglSwap = true;
    private boolean mCollectFbOnGlDraw = false;
    private boolean mCollectTextureData = false;
private IDevice[] mDevices;

public GLTraceOptionsDialog(Shell parentShell) {
//Synthetic comment -- @@ -188,22 +188,22 @@

final Button readFbOnEglSwapCheckBox = new Button(c, SWT.CHECK);
readFbOnEglSwapCheckBox.setText("Read back framebuffer 0 on eglSwapBuffers()");
        readFbOnEglSwapCheckBox.setSelection(mCollectFbOnEglSwap);

final Button readFbOnGlDrawCheckBox = new Button(c, SWT.CHECK);
readFbOnGlDrawCheckBox.setText("Read back currently bound framebuffer On glDraw*()");
        readFbOnGlDrawCheckBox.setSelection(mCollectFbOnGlDraw);

final Button readTextureDataCheckBox = new Button(c, SWT.CHECK);
readTextureDataCheckBox.setText("Collect texture data submitted using glTexImage*()");
        readTextureDataCheckBox.setSelection(mCollectTextureData);

SelectionListener l = new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                mCollectFbOnEglSwap = readFbOnEglSwapCheckBox.getSelection();
                mCollectFbOnGlDraw = readFbOnGlDrawCheckBox.getSelection();
                mCollectTextureData = readTextureDataCheckBox.getSelection();
}
};

//Synthetic comment -- @@ -356,6 +356,6 @@

public TraceOptions getTraceOptions() {
return new TraceOptions(mSelectedDevice, mAppPackageToTrace, mActivityToTrace,
                mTraceFilePath, mCollectFbOnEglSwap, mCollectFbOnGlDraw, mCollectTextureData);
}
}







