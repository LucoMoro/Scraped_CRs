/*Add proper styleable support.

Change-Id:I2dc79e71521f93d798fd4a9b33aa59979bef379d*/




//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/ResourceResolver.java b/ide_common/src/com/android/ide/common/resources/ResourceResolver.java
//Synthetic comment -- index 938a6ef..c847e99 100644

//Synthetic comment -- @@ -310,26 +310,27 @@
}

@Override
    public ResourceValue resolveResValue(ResourceValue resValue) {
        if (resValue == null) {
return null;
}

        // if the resource value is null, we simply return it.
        String value = resValue.getValue();
        if (value == null) {
            return resValue;
}

// else attempt to find another ResourceValue referenced by this one.
        ResourceValue resolvedResValue = findResValue(value, resValue.isFramework());

// if the value did not reference anything, then we simply return the input value
        if (resolvedResValue == null) {
            return resValue;
}

// otherwise, we attempt to resolve this new value as well
        return resolveResValue(resolvedResValue);
}

// ---- Private helper methods.








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/ValueResourceParser.java b/ide_common/src/com/android/ide/common/resources/ValueResourceParser.java
//Synthetic comment -- index cda6587..e52c026 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.common.resources;

import com.android.ide.common.rendering.api.DeclareStyleableResourceValue;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.StyleResourceValue;
import com.android.resources.ResourceType;
//Synthetic comment -- @@ -35,6 +36,7 @@
private final static String ATTR_NAME = "name";
private final static String ATTR_TYPE = "type";
private final static String ATTR_PARENT = "parent";
    private final static String ATTR_VALUE = "value";

private final static String DEFAULT_NS_PREFIX = "android:";
private final static int DEFAULT_NS_PREFIX_LEN = DEFAULT_NS_PREFIX.length();
//Synthetic comment -- @@ -45,8 +47,10 @@

private boolean inResources = false;
private int mDepth = 0;
private ResourceValue mCurrentValue = null;
    private StyleResourceValue mCurrentStyle = null;
    private DeclareStyleableResourceValue mCurrentDeclareStyleable = null;
    private String mCurrentAttribute = null;
private IValueResourceRepository mRepository;
private final boolean mIsFramework;

//Synthetic comment -- @@ -66,8 +70,10 @@
} else if (mDepth == 2) {
mCurrentValue = null;
mCurrentStyle = null;
            mCurrentDeclareStyleable = null;
} else if (mDepth == 3) {
mCurrentValue = null;
            mCurrentAttribute = null;
}

mDepth--;
//Synthetic comment -- @@ -101,29 +107,53 @@
// get the resource name
String name = attributes.getValue(ATTR_NAME);
if (name != null) {
                            switch (type) {
                                case STYLE:
                                    String parent = attributes.getValue(ATTR_PARENT);
                                    mCurrentStyle = new StyleResourceValue(type, name, parent,
                                            mIsFramework);
                                    mRepository.addResourceValue(type, mCurrentStyle);
                                    break;
                                case DECLARE_STYLEABLE:
                                    mCurrentDeclareStyleable = new DeclareStyleableResourceValue(
                                            type, name, mIsFramework);
                                    mRepository.addResourceValue(type, mCurrentDeclareStyleable);
                                    break;
                                default:
                                    mCurrentValue = new ResourceValue(type, name, mIsFramework);
                                    mRepository.addResourceValue(type, mCurrentValue);
                                    break;
}
}
}
}
            } else if (mDepth == 3) {
// get the resource name
String name = attributes.getValue(ATTR_NAME);
if (name != null) {

                    if (mCurrentStyle != null) {
                        // the name can, in some cases, contain a prefix! we remove it.
                        if (name.startsWith(DEFAULT_NS_PREFIX)) {
                            name = name.substring(DEFAULT_NS_PREFIX_LEN);
                        }

                        mCurrentValue = new ResourceValue(null, name, mIsFramework);
                        mCurrentStyle.addValue(mCurrentValue);
                    } else if (mCurrentDeclareStyleable != null) {
                        mCurrentAttribute = name;
                    }
                }
            } else if (mDepth == 4 && mCurrentDeclareStyleable != null) {
                // get the enum/flag name
                String name = attributes.getValue(ATTR_NAME);
                String value = attributes.getValue(ATTR_VALUE);

                try {
                    mCurrentDeclareStyleable.addValue(mCurrentAttribute,
                            name, Integer.decode(value));
                } catch (NumberFormatException e) {
                    // pass, we'll just ignore this value
}
}
} finally {








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/DeclareStyleableResourceValue.java b/layoutlib_api/src/com/android/ide/common/rendering/api/DeclareStyleableResourceValue.java
new file mode 100644
//Synthetic comment -- index 0000000..b2fabb9

//Synthetic comment -- @@ -0,0 +1,59 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.common.rendering.api;

import com.android.resources.ResourceType;

import java.util.HashMap;
import java.util.Map;

public class DeclareStyleableResourceValue extends ResourceValue {

    private Map<String, Map<String, Integer>> mEnumMap;

    public DeclareStyleableResourceValue(ResourceType type, String name, boolean isFramework) {
        super(type, name, isFramework);

    }

    public Map<String, Integer> getEnumValues(String name) {
        if (mEnumMap != null) {
            return mEnumMap.get(name);
        }

        return null;
    }

    public void addValue(String attribute, String name, Integer value) {
        Map<String, Integer> map;

        if (mEnumMap == null) {
            mEnumMap = new HashMap<String, Map<String,Integer>>();

            map = new HashMap<String, Integer>();
            mEnumMap.put(attribute, map);
        } else {
            map = mEnumMap.get(attribute);
            if (map == null) {
                map = new HashMap<String, Integer>();
                mEnumMap.put(attribute, map);
            }
        }

        map.put(name, value);
    }
}







