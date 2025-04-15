/*gltrace: Default to displaying texture mipmap level 0.

Textures can have multiple mipmap levels. To view the uploaded
texture requires that you explicitly select a particular mipmap
level. However, in many cases, there is only 1 level (level 0).
In such cases, rather than having to drill down the hierarchy
and then select mipmap 0 to view the texture, this CL allows
just selecting the texture to trigger a display of the zeroth
mipmap if it is available.

Change-Id:I9f7f8f10f99945377c6cb7dfab19db75cea383e0*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/views/detail/TextureImageDetailsProvider.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/views/detail/TextureImageDetailsProvider.java
//Synthetic comment -- index 61f3dc4..f29bbef 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.ide.eclipse.gltrace.views.detail;

import com.android.ide.eclipse.gltrace.state.GLCompositeProperty;
import com.android.ide.eclipse.gltrace.state.GLStateType;
import com.android.ide.eclipse.gltrace.state.GLStringProperty;
import com.android.ide.eclipse.gltrace.state.IGLProperty;
//Synthetic comment -- @@ -84,9 +85,13 @@
* the state hierarchy.
* @param state any node in the GL state hierarchy
* @return The {@link GLStateType#TEXTURE_IMAGE} property if a unique instance
     *         of it can be accessed from the given node, null otherwise.
     *         A unique instance can be accessed if the given node is
     *         either the requested node itself, or its parent or sibling.
*/
private IGLProperty getTextureImageProperty(IGLProperty state) {
if (state.getType() == GLStateType.TEXTURE_IMAGE) {
//Synthetic comment -- @@ -94,18 +99,51 @@
return state;
}

        if (state.getType() != GLStateType.PER_TEXTURE_LEVEL_STATE) {
            // if it is not the parent, then it could be a sibling, in which case
            // we go up a level to its parent
state = state.getParent();
}

        if (state != null && state.getType() == GLStateType.PER_TEXTURE_LEVEL_STATE) {
            // if it is the parent, we can access the required property
            return ((GLCompositeProperty) state).getProperty(GLStateType.TEXTURE_IMAGE);
}

        return null;
}

@Override







