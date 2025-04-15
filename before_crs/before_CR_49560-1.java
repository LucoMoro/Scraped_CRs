/*Sync sdk=>tools/base: "ddmlib: Add controls for OpenGL tracing via jdwp"

Merged from platforms/sdk.git 94e177b50dc1ad3739f563668e1f2e43a9f97f46
and only merging the ddmlib changes.

Original description:

Currently, applications have to be launched with gltrace
enabled for OpenGL tracing to work. This patch provides the host side
support for dynamically enabling/disabling tracing on running apps.

At a high level, the functionality is similar to traceview:
    - ClientData#FEATURE_OPENGL_TRACING indicates whether the VM on
      the device supports this feature.
    - If the feature is supported, then JDWP is used to send the
      enable or disable messages.
    - Users can trigger OpenGL tracing via a toolbar item in the DDMS
      device view.

Change-Id:Ib6f1764ef74fb0e71a062501cd8b64d6a9d5c97e*/
//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/Client.java b/ddmlib/src/main/java/com/android/ddmlib/Client.java
//Synthetic comment -- index 8ea1193..15acd88 100644

//Synthetic comment -- @@ -271,6 +271,36 @@
}
}

/**
* Sends a request to the VM to send the enable status of the method profiling.
* This is asynchronous.








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/ClientData.java b/ddmlib/src/main/java/com/android/ddmlib/ClientData.java
//Synthetic comment -- index bec4c61..aa383cf 100644

//Synthetic comment -- @@ -130,6 +130,12 @@
public static final String FEATURE_PROFILING_STREAMING = "method-trace-profiling-streaming"; //$NON-NLS-1$

/**
* String for feature allowing to dump hprof files
* @see #hasFeature(String)
*/








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/OpenGlTraceChunkHandler.java b/ddmlib/src/main/java/com/android/ddmlib/OpenGlTraceChunkHandler.java
new file mode 100644
//Synthetic comment -- index 0000000..12ba142

//Synthetic comment -- @@ -0,0 +1,63 @@







