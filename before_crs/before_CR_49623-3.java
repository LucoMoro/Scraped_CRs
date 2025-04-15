/*WIP: hv via DDM

Change-Id:Ic1fffd4fef43ef9b2c5e4d432f16d941323d75f7*/
//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/AndroidDebugBridge.java b/ddmlib/src/main/java/com/android/ddmlib/AndroidDebugBridge.java
//Synthetic comment -- index c787a12..000d92a 100644

//Synthetic comment -- @@ -213,6 +213,7 @@
HandleWait.register(monitorThread);
HandleProfiling.register(monitorThread);
HandleNativeHeap.register(monitorThread);
}

/**








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/ChunkHandler.java b/ddmlib/src/main/java/com/android/ddmlib/ChunkHandler.java
//Synthetic comment -- index 74fa318..2cc6494 100644

//Synthetic comment -- @@ -94,7 +94,7 @@
* This is here because multiple chunk handlers can make use of it,
* and there's nowhere better to put it.
*/
    static String getString(ByteBuffer buf, int len) {
char[] data = new char[len];
for (int i = 0; i < len; i++)
data[i] = buf.getChar();








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/ClientData.java b/ddmlib/src/main/java/com/android/ddmlib/ClientData.java
//Synthetic comment -- index aa383cf..3741df2 100644

//Synthetic comment -- @@ -136,6 +136,12 @@
public final static String FEATURE_OPENGL_TRACING = "opengl-tracing"; //$NON-NLS-1$

/**
* String for feature allowing to dump hprof files
* @see #hasFeature(String)
*/








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/HandleViewDebug.java b/ddmlib/src/main/java/com/android/ddmlib/HandleViewDebug.java
new file mode 100644
//Synthetic comment -- index 0000000..01e4403

//Synthetic comment -- @@ -0,0 +1,191 @@







