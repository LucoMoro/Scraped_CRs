/*gltrace: Add support for mipmap textures

Change-Id:I39782b06d6ebbfdde55cc76ed3c09424fef3dcda*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLSparseArrayProperty.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLSparseArrayProperty.java
//Synthetic comment -- index c2836b1..6bf4e96 100644

//Synthetic comment -- @@ -23,20 +23,34 @@

public class GLSparseArrayProperty implements IGLProperty {
private final GLStateType mType;
    private final IGLProperty mDefaultValue;
    private final boolean mCreateOnAccess;
private final SparseArray<IGLProperty> mSparseArray;
    private IGLProperty mParent;

public GLSparseArrayProperty(GLStateType type, IGLProperty defaultValue) {
        this(type, defaultValue, false);
    }

    /**
     * Constructs a sparse array property.
     * @param type GL state corresponding to this property
     * @param defaultValue default value of each item
     * @param createOnAccess create an item on access if it is not present
     */
    public GLSparseArrayProperty(GLStateType type, IGLProperty defaultValue,
            boolean createOnAccess) {
mType = type;
mDefaultValue = defaultValue;
        mCreateOnAccess = createOnAccess;
mSparseArray = new SparseArray<IGLProperty>(20);
}

private GLSparseArrayProperty(GLStateType type, IGLProperty defaultValue,
            boolean createOnAccess, SparseArray<IGLProperty> contents) {
mType = type;
mDefaultValue = defaultValue;
        mCreateOnAccess = createOnAccess;
mSparseArray = contents;
}

//Synthetic comment -- @@ -51,7 +65,12 @@
}

public IGLProperty getProperty(int key) {
        IGLProperty p = mSparseArray.get(key);
        if (p == null && mCreateOnAccess) {
            add(key);
            p = mSparseArray.get(key);
        }
        return p;
}

public int keyFor(IGLProperty element) {
//Synthetic comment -- @@ -108,7 +127,7 @@
copy.put(key, value);
}

        return new GLSparseArrayProperty(mType, mDefaultValue, mCreateOnAccess, copy);
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLState.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLState.java
//Synthetic comment -- index aeb9d89..522130b 100644

//Synthetic comment -- @@ -264,6 +264,7 @@
GLEnum.GL_NEAREST);
IGLProperty wrapS = new GLEnumProperty(GLStateType.TEXTURE_WRAP_S, GLEnum.GL_REPEAT);
IGLProperty wrapT = new GLEnumProperty(GLStateType.TEXTURE_WRAP_T, GLEnum.GL_REPEAT);

IGLProperty width = new GLIntegerProperty(GLStateType.TEXTURE_WIDTH, Integer.valueOf(-1));
IGLProperty height = new GLIntegerProperty(GLStateType.TEXTURE_HEIGHT,
Integer.valueOf(-1));
//Synthetic comment -- @@ -272,8 +273,15 @@
IGLProperty imageType = new GLEnumProperty(GLStateType.TEXTURE_IMAGE_TYPE,
GLEnum.GL_UNSIGNED_BYTE);
IGLProperty image = new GLStringProperty(GLStateType.TEXTURE_IMAGE, null);

        IGLProperty perTextureLevelState = new GLCompositeProperty(
                GLStateType.PER_TEXTURE_LEVEL_STATE,
                width, height, format, imageType, image);
        IGLProperty mipmapState = new GLSparseArrayProperty(GLStateType.TEXTURE_MIPMAPS,
                perTextureLevelState, true);

IGLProperty textureDefaultState = new GLCompositeProperty(GLStateType.PER_TEXTURE_STATE,
                minFilter, magFilter, wrapS, wrapT, mipmapState);
GLSparseArrayProperty textures = new GLSparseArrayProperty(GLStateType.TEXTURES,
textureDefaultState);
textures.add(0);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLStateType.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLStateType.java
//Synthetic comment -- index a07edc2..f0e30f4 100644

//Synthetic comment -- @@ -122,11 +122,13 @@
TEXTURE_MAG_FILTER("Magnification Function"),
TEXTURE_WRAP_S("Texcoord s Wrap Mode"),
TEXTURE_WRAP_T("Texcoord t Wrap Mode"),
                TEXTURE_MIPMAPS("Texture Mipmap State"),
                    PER_TEXTURE_LEVEL_STATE("Per Texture Level State"),
                        TEXTURE_FORMAT("Format"),
                        TEXTURE_WIDTH("Width"),
                        TEXTURE_HEIGHT("Height"),
                        TEXTURE_IMAGE_TYPE("Image Type"),
                        TEXTURE_IMAGE("Image"),

PROGRAM_STATE("Program Object State"),
CURRENT_PROGRAM("Current Program"),








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/transforms/StateTransformFactory.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/transforms/StateTransformFactory.java
//Synthetic comment -- index df92ea7..47a924f 100644

//Synthetic comment -- @@ -989,6 +989,7 @@
private static List<IStateTransform> transformsForGlTexImage(GLMessage msg, int widthArgIndex,
int heightArgIndex, int xOffsetIndex, int yOffsetIndex) {
GLEnum target = GLEnum.valueOf(msg.getArgs(0).getIntValue(0));
        int level = msg.getArgs(1).getIntValue(0);
Integer width = Integer.valueOf(msg.getArgs(widthArgIndex).getIntValue(0));
Integer height = Integer.valueOf(msg.getArgs(heightArgIndex).getIntValue(0));
GLEnum format = GLEnum.valueOf(msg.getArgs(6).getIntValue(0));
//Synthetic comment -- @@ -998,21 +999,25 @@
transforms.add(new PropertyChangeTransform(
new TexturePropertyAccessor(msg.getContextId(),
getTextureUnitTargetName(target),
                                            level,
GLStateType.TEXTURE_WIDTH),
width));
transforms.add(new PropertyChangeTransform(
new TexturePropertyAccessor(msg.getContextId(),
getTextureUnitTargetName(target),
                                            level,
GLStateType.TEXTURE_HEIGHT),
height));
transforms.add(new PropertyChangeTransform(
new TexturePropertyAccessor(msg.getContextId(),
getTextureUnitTargetName(target),
                                            level,
GLStateType.TEXTURE_FORMAT),
format));
transforms.add(new PropertyChangeTransform(
new TexturePropertyAccessor(msg.getContextId(),
getTextureUnitTargetName(target),
                                            level,
GLStateType.TEXTURE_IMAGE_TYPE),
type));

//Synthetic comment -- @@ -1042,6 +1047,7 @@
transforms.add(new TexImageTransform(
new TexturePropertyAccessor(msg.getContextId(),
getTextureUnitTargetName(target),
                        level,
GLStateType.TEXTURE_IMAGE),
f, format, xOffset, yOffset, width, height));

//Synthetic comment -- @@ -1066,6 +1072,14 @@
GLEnum pname = GLEnum.valueOf(msg.getArgs(1).getIntValue(0));
GLEnum pvalue = GLEnum.valueOf(msg.getArgs(2).getIntValue(0));

        if (pname != GLEnum.GL_TEXTURE_MIN_FILTER
                && pname != GLEnum.GL_TEXTURE_MAG_FILTER
                && pname != GLEnum.GL_TEXTURE_WRAP_S
                && pname != GLEnum.GL_TEXTURE_WRAP_T) {
            throw new IllegalArgumentException(
                    String.format("Unsupported parameter (%s) for glTexParameter()", pname));
        }

IStateTransform transform = new PropertyChangeTransform(
new TexturePropertyAccessor(msg.getContextId(),
getTextureUnitTargetName(target),








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/transforms/TexturePropertyAccessor.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/transforms/TexturePropertyAccessor.java
//Synthetic comment -- index 2116283..10758d8 100644

//Synthetic comment -- @@ -29,19 +29,25 @@
public class TexturePropertyAccessor implements IGLPropertyAccessor {
private final int mContextId;
private final GLStateType mTargetUnitType;
    private final int mMipmapLevel;
private final GLStateType mTextureType;
private TextureUnitPropertyAccessor mTextureUnitPropertyAccessor;

    public TexturePropertyAccessor(int contextId, GLStateType textureUnitTarget, int level,
GLStateType textureTargetName) {
mContextId = contextId;
mTargetUnitType = textureUnitTarget;
        mMipmapLevel = level;
mTextureType = textureTargetName;
mTextureUnitPropertyAccessor = new TextureUnitPropertyAccessor(mContextId,
mTargetUnitType);
}

    public TexturePropertyAccessor(int contextId, GLStateType textureUnitTarget,
            GLStateType textureTargetName) {
        this(contextId, textureUnitTarget, -1, textureTargetName);
    }

@Override
public IGLProperty getProperty(IGLProperty state) {
// identify the texture that is bound in the current active texture unit
//Synthetic comment -- @@ -52,11 +58,22 @@
Integer textureId = (Integer) targetTexture.getValue();

// now extract the required property from the selected texture
        IGLPropertyAccessor textureAccessor;
        if (mMipmapLevel >= 0) {
            textureAccessor = GLPropertyAccessor.makeAccessor(mContextId,
                    GLStateType.TEXTURE_STATE,
                    GLStateType.TEXTURES,
                    textureId,
                    GLStateType.TEXTURE_MIPMAPS,
                    Integer.valueOf(mMipmapLevel),
                    mTextureType);
        } else {
            textureAccessor = GLPropertyAccessor.makeAccessor(mContextId,
                    GLStateType.TEXTURE_STATE,
                    GLStateType.TEXTURES,
                    textureId,
                    mTextureType);
        }

return textureAccessor.getProperty(state);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/views/detail/TextureImageDetailsProvider.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/views/detail/TextureImageDetailsProvider.java
//Synthetic comment -- index 0c17d4c..61f3dc4 100644

//Synthetic comment -- @@ -94,13 +94,13 @@
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







