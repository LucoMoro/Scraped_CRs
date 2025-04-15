/*gltrace: Support OES_EGL_image_external GL extension

The OES_EGL_image_external extension adds an additional state,
TEXTURE_EXTERNAL, to each texture unit.

Change-Id:I78c4cc1b1344a393183fd9004b5d85e1837da5bc*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/StateViewPage.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/StateViewPage.java
//Synthetic comment -- index 7c71d91..64a200b 100644

//Synthetic comment -- @@ -235,7 +235,7 @@
} catch (Exception e) {
GlTracePlugin.getDefault().logMessage("Error applying transformations for "
+ call);
                    GlTracePlugin.getDefault().logMessage(e.getMessage());
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLState.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLState.java
//Synthetic comment -- index aac5ad3..6433095 100644

//Synthetic comment -- @@ -23,7 +23,7 @@

public class GLState {
/** # of texture units modelled in the GL State. */
    public static final int TEXTURE_UNIT_COUNT = 8;

/** # of vertex attributes */
private static final int MAX_VERTEX_ATTRIBS = 8;
//Synthetic comment -- @@ -253,8 +253,10 @@
Integer.valueOf(0));
IGLProperty bindingCubeMap = new GLIntegerProperty(GLStateType.TEXTURE_BINDING_CUBE_MAP,
Integer.valueOf(0));
IGLProperty perTextureUnitState = new GLCompositeProperty(
                GLStateType.PER_TEXTURE_UNIT_STATE, binding2D, bindingCubeMap);
IGLProperty textureUnitState = new GLListProperty(GLStateType.TEXTURE_UNITS,
perTextureUnitState, TEXTURE_UNIT_COUNT);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLStateType.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLStateType.java
//Synthetic comment -- index f0e30f4..330e4f5 100644

//Synthetic comment -- @@ -116,6 +116,7 @@
PER_TEXTURE_UNIT_STATE("Texture Unit Properties"),
TEXTURE_BINDING_2D("TEXTURE_2D Binding"),
TEXTURE_BINDING_CUBE_MAP("TEXTURE_CUBE_MAP Binding"),
TEXTURES("Textures"),
PER_TEXTURE_STATE("Per Texture State"),
TEXTURE_MIN_FILTER("Minification Function"),








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/transforms/StateTransformFactory.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/transforms/StateTransformFactory.java
//Synthetic comment -- index f5aa164..f69f28d 100644

//Synthetic comment -- @@ -947,8 +947,11 @@
}

private static GLStateType getTextureUnitTargetName(GLEnum target) {
        if (target == GLEnum.GL_TEXTURE_BINDING_CUBE_MAP) {
return GLStateType.TEXTURE_BINDING_CUBE_MAP;
} else {
return GLStateType.TEXTURE_BINDING_2D;
}







