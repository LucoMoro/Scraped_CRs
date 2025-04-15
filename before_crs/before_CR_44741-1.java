/*nativeheap: do not set layout on parent

Change-Id:If5f0c6073cfe847c8d04360cfe7b2f5e4cd58766*/
//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/heap/NativeHeapPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/heap/NativeHeapPanel.java
//Synthetic comment -- index 5f7abe2..f6631b7 100644

//Synthetic comment -- @@ -475,8 +475,6 @@

@Override
protected Control createControl(Composite parent) {
        parent.setLayout(new GridLayout(1, false));

Composite c = new Composite(parent, SWT.NONE);
c.setLayout(new GridLayout(1, false));
c.setLayoutData(new GridData(GridData.FILL_BOTH));







