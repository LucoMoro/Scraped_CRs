/*gltrace: ES1 also supports array buffers

Change-Id:I448d7962e9b709658095240160f16abc8b040ecc*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLState.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLState.java
//Synthetic comment -- index 6433095..74c6fcd 100644

//Synthetic comment -- @@ -362,6 +362,7 @@

public static IGLProperty createDefaultES1State() {
GLCompositeProperty glState = new GLCompositeProperty(GLStateType.GL_STATE_ES1,
                sGLState.createVertexArrayData(),
sGLState.createFramebufferState(),
sGLState.createTransformationState(),
sGLState.createRasterizationState(),







