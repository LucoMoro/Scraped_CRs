/*Add proper styleable support.

Change-Id:I2dc79e71521f93d798fd4a9b33aa59979bef379d*/
//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/ResourceResolver.java b/ide_common/src/com/android/ide/common/resources/ResourceResolver.java
//Synthetic comment -- index 938a6ef..c847e99 100644

//Synthetic comment -- @@ -310,26 +310,27 @@
}

@Override
    public ResourceValue resolveResValue(ResourceValue value) {
        if (value == null) {
return null;
}

        // if the resource value is a style, we simply return it.
        if (value instanceof StyleResourceValue) {
            return value;
}

// else attempt to find another ResourceValue referenced by this one.
        ResourceValue resolvedValue = findResValue(value.getValue(), value.isFramework());

// if the value did not reference anything, then we simply return the input value
        if (resolvedValue == null) {
            return value;
}

// otherwise, we attempt to resolve this new value as well
        return resolveResValue(resolvedValue);
}

// ---- Private helper methods.








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/ValueResourceParser.java b/ide_common/src/com/android/ide/common/resources/ValueResourceParser.java
//Synthetic comment -- index cda6587..e52c026 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.common.resources;

import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.StyleResourceValue;
import com.android.resources.ResourceType;
//Synthetic comment -- @@ -35,6 +36,7 @@
private final static String ATTR_NAME = "name";
private final static String ATTR_TYPE = "type";
private final static String ATTR_PARENT = "parent";

private final static String DEFAULT_NS_PREFIX = "android:";
private final static int DEFAULT_NS_PREFIX_LEN = DEFAULT_NS_PREFIX.length();
//Synthetic comment -- @@ -45,8 +47,10 @@

private boolean inResources = false;
private int mDepth = 0;
    private StyleResourceValue mCurrentStyle = null;
private ResourceValue mCurrentValue = null;
private IValueResourceRepository mRepository;
private final boolean mIsFramework;

//Synthetic comment -- @@ -66,8 +70,10 @@
} else if (mDepth == 2) {
mCurrentValue = null;
mCurrentStyle = null;
} else if (mDepth == 3) {
mCurrentValue = null;
}

mDepth--;
//Synthetic comment -- @@ -101,29 +107,53 @@
// get the resource name
String name = attributes.getValue(ATTR_NAME);
if (name != null) {
                            if (type == ResourceType.STYLE) {
                                String parent = attributes.getValue(ATTR_PARENT);
                                mCurrentStyle = new StyleResourceValue(type, name, parent,
                                        mIsFramework);
                                mRepository.addResourceValue(type, mCurrentStyle);
                            } else {
                                mCurrentValue = new ResourceValue(type, name, mIsFramework);
                                mRepository.addResourceValue(type, mCurrentValue);
}
}
}
}
            } else if (mDepth == 3 && mCurrentStyle != null) {
// get the resource name
String name = attributes.getValue(ATTR_NAME);
if (name != null) {
                    // the name can, in some cases, contain a prefix! we remove it.
                    if (name.startsWith(DEFAULT_NS_PREFIX)) {
                        name = name.substring(DEFAULT_NS_PREFIX_LEN);
                    }

                    mCurrentValue = new ResourceValue(null, name, mIsFramework);
                    mCurrentStyle.addValue(mCurrentValue);
}
}
} finally {








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/DeclareStyleableResourceValue.java b/layoutlib_api/src/com/android/ide/common/rendering/api/DeclareStyleableResourceValue.java
new file mode 100644
//Synthetic comment -- index 0000000..2b13d0b

//Synthetic comment -- @@ -0,0 +1,71 @@







