/*Add support for the new width/height resource qualifiers.

Also add Television to the dock mode (which is really UI Mode).

Change-Id:I97e3fbea1806a32d8267d8e462211add2b122ed0*/




//Synthetic comment -- diff --git a/common/src/com/android/resources/DockMode.java b/common/src/com/android/resources/DockMode.java
//Synthetic comment -- index bbae6bf..71515f9 100644

//Synthetic comment -- @@ -23,7 +23,8 @@
public enum DockMode implements ResourceEnum {
NONE("", "No Dock"),
CAR("car", "Car Dock"),
    DESK("desk", "Desk Dock"),
    TELEVISION("television", "Television");

private final String mValue;
private final String mDisplayValue;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AaptParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AaptParser.java
//Synthetic comment -- index 2a2f0d0..01cdde0 100644

//Synthetic comment -- @@ -192,7 +192,12 @@
* @return true if the parsing failed, false if success.
*/
public static boolean parseOutput(List<String> results, IProject project) {
        int size = results.size();
        if (size > 0) {
            return parseOutput(results.toArray(new String[size]), project);
        }

        return true;
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigEditDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigEditDialog.java
//Synthetic comment -- index d9b3911..86b8cd5 100644

//Synthetic comment -- @@ -131,6 +131,7 @@

public void getConfig(FolderConfiguration config) {
config.set(mConfig);
        config.updateScreenWidthAndHeight();
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/LayoutDeviceHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/LayoutDeviceHandler.java
//Synthetic comment -- index 3ad4e61..eb3853a 100644

//Synthetic comment -- @@ -120,6 +120,7 @@
mCurrentDevice = null;
mDefaultConfig = null;
} else if (LayoutDevicesXsd.NODE_CONFIG.equals(localName)) {
            mCurrentConfig.updateScreenWidthAndHeight();
mCurrentConfig = null;
} else if (LayoutDevicesXsd.NODE_COUNTRY_CODE.equals(localName)) {
CountryCodeQualifier ccq = new CountryCodeQualifier(








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ConfigurationSelector.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ConfigurationSelector.java
//Synthetic comment -- index 74120fd..ffef837 100644

//Synthetic comment -- @@ -30,9 +30,12 @@
import com.android.ide.common.resources.configuration.RegionQualifier;
import com.android.ide.common.resources.configuration.ResourceQualifier;
import com.android.ide.common.resources.configuration.ScreenDimensionQualifier;
import com.android.ide.common.resources.configuration.ScreenHeightQualifier;
import com.android.ide.common.resources.configuration.ScreenOrientationQualifier;
import com.android.ide.common.resources.configuration.ScreenRatioQualifier;
import com.android.ide.common.resources.configuration.ScreenSizeQualifier;
import com.android.ide.common.resources.configuration.ScreenWidthQualifier;
import com.android.ide.common.resources.configuration.SmallestScreenWidthQualifier;
import com.android.ide.common.resources.configuration.TextInputMethodQualifier;
import com.android.ide.common.resources.configuration.TouchScreenQualifier;
import com.android.ide.common.resources.configuration.VersionQualifier;
//Synthetic comment -- @@ -422,6 +425,10 @@
mUiMap.put(NetworkCodeQualifier.class, new MNCEdit(mQualifierEditParent));
mUiMap.put(LanguageQualifier.class, new LanguageEdit(mQualifierEditParent));
mUiMap.put(RegionQualifier.class, new RegionEdit(mQualifierEditParent));
            mUiMap.put(SmallestScreenWidthQualifier.class,
                    new SmallestScreenWidthEdit(mQualifierEditParent));
            mUiMap.put(ScreenWidthQualifier.class, new ScreenWidthEdit(mQualifierEditParent));
            mUiMap.put(ScreenHeightQualifier.class, new ScreenHeightEdit(mQualifierEditParent));
mUiMap.put(ScreenSizeQualifier.class, new ScreenSizeEdit(mQualifierEditParent));
mUiMap.put(ScreenRatioQualifier.class, new ScreenRatioEdit(mQualifierEditParent));
mUiMap.put(ScreenOrientationQualifier.class, new OrientationEdit(mQualifierEditParent));
//Synthetic comment -- @@ -966,6 +973,204 @@
}

/**
     * Edit widget for {@link SmallestScreenWidthQualifier}.
     */
    private class SmallestScreenWidthEdit extends QualifierEditBase {

        private Text mSize;

        public SmallestScreenWidthEdit(Composite parent) {
            super(parent, SmallestScreenWidthQualifier.NAME);

            ModifyListener modifyListener = new ModifyListener() {
                public void modifyText(ModifyEvent e) {
                    onSizeChange();
                }
            };

            FocusAdapter focusListener = new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    onSizeChange();
                }
            };

            mSize = new Text(this, SWT.BORDER);
            mSize.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            mSize.addVerifyListener(new DimensionVerifier());
            mSize.addModifyListener(modifyListener);
            mSize.addFocusListener(focusListener);
        }

        private void onSizeChange() {
            // update the current config
            String size = mSize.getText();

            if (size.length() == 0) {
                // if one of the strings is empty, reset to no qualifier.
                // Since the qualifier classes are immutable, and we don't want to
                // remove the qualifier from the configuration, we create a new default one.
                mSelectedConfiguration.setSmallestScreenWidthQualifier(
                        new SmallestScreenWidthQualifier());
            } else {
                SmallestScreenWidthQualifier qualifier = SmallestScreenWidthQualifier.getQualifier(
                        size);

                if (qualifier != null) {
                    mSelectedConfiguration.setSmallestScreenWidthQualifier(qualifier);
                } else {
                    // Failure! Looks like the value is wrong, reset the qualifier
                    // Since the qualifier classes are immutable, and we don't want to
                    // remove the qualifier from the configuration, we create a new default one.
                    mSelectedConfiguration.setSmallestScreenWidthQualifier(
                            new SmallestScreenWidthQualifier());
                }
            }

            // notify of change
            onChange(true /* keepSelection */);
        }

        @Override
        public void setQualifier(ResourceQualifier qualifier) {
            SmallestScreenWidthQualifier q = (SmallestScreenWidthQualifier)qualifier;

            mSize.setText(Integer.toString(q.getValue()));
        }
    }

    /**
     * Edit widget for {@link ScreenWidthQualifier}.
     */
    private class ScreenWidthEdit extends QualifierEditBase {

        private Text mSize;

        public ScreenWidthEdit(Composite parent) {
            super(parent, ScreenWidthQualifier.NAME);

            ModifyListener modifyListener = new ModifyListener() {
                public void modifyText(ModifyEvent e) {
                    onSizeChange();
                }
            };

            FocusAdapter focusListener = new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    onSizeChange();
                }
            };

            mSize = new Text(this, SWT.BORDER);
            mSize.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            mSize.addVerifyListener(new DimensionVerifier());
            mSize.addModifyListener(modifyListener);
            mSize.addFocusListener(focusListener);
        }

        private void onSizeChange() {
            // update the current config
            String size = mSize.getText();

            if (size.length() == 0) {
                // if one of the strings is empty, reset to no qualifier.
                // Since the qualifier classes are immutable, and we don't want to
                // remove the qualifier from the configuration, we create a new default one.
                mSelectedConfiguration.setScreenWidthQualifier(new ScreenWidthQualifier());
            } else {
                ScreenWidthQualifier qualifier = ScreenWidthQualifier.getQualifier(size);

                if (qualifier != null) {
                    mSelectedConfiguration.setScreenWidthQualifier(qualifier);
                } else {
                    // Failure! Looks like the value is wrong, reset the qualifier
                    // Since the qualifier classes are immutable, and we don't want to
                    // remove the qualifier from the configuration, we create a new default one.
                    mSelectedConfiguration.setScreenWidthQualifier(
                            new ScreenWidthQualifier());
                }
            }

            // notify of change
            onChange(true /* keepSelection */);
        }

        @Override
        public void setQualifier(ResourceQualifier qualifier) {
            ScreenWidthQualifier q = (ScreenWidthQualifier)qualifier;

            mSize.setText(Integer.toString(q.getValue()));
        }
    }

    /**
     * Edit widget for {@link ScreenHeightQualifier}.
     */
    private class ScreenHeightEdit extends QualifierEditBase {

        private Text mSize;

        public ScreenHeightEdit(Composite parent) {
            super(parent, ScreenHeightQualifier.NAME);

            ModifyListener modifyListener = new ModifyListener() {
                public void modifyText(ModifyEvent e) {
                    onSizeChange();
                }
            };

            FocusAdapter focusListener = new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    onSizeChange();
                }
            };

            mSize = new Text(this, SWT.BORDER);
            mSize.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            mSize.addVerifyListener(new DimensionVerifier());
            mSize.addModifyListener(modifyListener);
            mSize.addFocusListener(focusListener);
        }

        private void onSizeChange() {
            // update the current config
            String size = mSize.getText();

            if (size.length() == 0) {
                // if one of the strings is empty, reset to no qualifier.
                // Since the qualifier classes are immutable, and we don't want to
                // remove the qualifier from the configuration, we create a new default one.
                mSelectedConfiguration.setScreenHeightQualifier(new ScreenHeightQualifier());
            } else {
                ScreenHeightQualifier qualifier = ScreenHeightQualifier.getQualifier(size);

                if (qualifier != null) {
                    mSelectedConfiguration.setScreenHeightQualifier(qualifier);
                } else {
                    // Failure! Looks like the value is wrong, reset the qualifier
                    // Since the qualifier classes are immutable, and we don't want to
                    // remove the qualifier from the configuration, we create a new default one.
                    mSelectedConfiguration.setScreenHeightQualifier(
                            new ScreenHeightQualifier());
                }
            }

            // notify of change
            onChange(true /* keepSelection */);
        }

        @Override
        public void setQualifier(ResourceQualifier qualifier) {
            ScreenHeightQualifier q = (ScreenHeightQualifier)qualifier;

            mSize.setText(Integer.toString(q.getValue()));
        }
    }


    /**
* Edit widget for {@link ScreenSizeQualifier}.
*/
private class ScreenSizeEdit extends QualifierEditBase {








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/configuration/FolderConfiguration.java b/ide_common/src/com/android/ide/common/resources/configuration/FolderConfiguration.java
//Synthetic comment -- index 09cf9e4..8b04bac 100644

//Synthetic comment -- @@ -17,7 +17,9 @@
package com.android.ide.common.resources.configuration;

import com.android.AndroidConstants;
import com.android.resources.Density;
import com.android.resources.ResourceFolderType;
import com.android.resources.ScreenOrientation;

import java.util.ArrayList;
import java.util.List;
//Synthetic comment -- @@ -41,24 +43,27 @@

private final ResourceQualifier[] mQualifiers = new ResourceQualifier[INDEX_COUNT];

    private final static int INDEX_COUNTRY_CODE          = 0;
    private final static int INDEX_NETWORK_CODE          = 1;
    private final static int INDEX_LANGUAGE              = 2;
    private final static int INDEX_REGION                = 3;
    private final static int INDEX_SMALLEST_SCREEN_WIDTH = 4;
    private final static int INDEX_SCREEN_WIDTH          = 5;
    private final static int INDEX_SCREEN_HEIGHT         = 6;
    private final static int INDEX_SCREEN_SIZE           = 7;
    private final static int INDEX_SCREEN_RATIO          = 8;
    private final static int INDEX_SCREEN_ORIENTATION    = 9;
    private final static int INDEX_DOCK_MODE             = 10;
    private final static int INDEX_NIGHT_MODE            = 11;
    private final static int INDEX_PIXEL_DENSITY         = 12;
    private final static int INDEX_TOUCH_TYPE            = 13;
    private final static int INDEX_KEYBOARD_STATE        = 14;
    private final static int INDEX_TEXT_INPUT_METHOD     = 15;
    private final static int INDEX_NAVIGATION_STATE      = 16;
    private final static int INDEX_NAVIGATION_METHOD     = 17;
    private final static int INDEX_SCREEN_DIMENSION      = 18;
    private final static int INDEX_VERSION               = 19;
    private final static int INDEX_COUNT                 = 20;

/**
* Creates a {@link FolderConfiguration} matching the folder segments.
//Synthetic comment -- @@ -205,38 +210,64 @@
public void addQualifier(ResourceQualifier qualifier) {
if (qualifier instanceof CountryCodeQualifier) {
mQualifiers[INDEX_COUNTRY_CODE] = qualifier;

} else if (qualifier instanceof NetworkCodeQualifier) {
mQualifiers[INDEX_NETWORK_CODE] = qualifier;

} else if (qualifier instanceof LanguageQualifier) {
mQualifiers[INDEX_LANGUAGE] = qualifier;

} else if (qualifier instanceof RegionQualifier) {
mQualifiers[INDEX_REGION] = qualifier;

        } else if (qualifier instanceof SmallestScreenWidthQualifier) {
            mQualifiers[INDEX_SMALLEST_SCREEN_WIDTH] = qualifier;

        } else if (qualifier instanceof ScreenWidthQualifier) {
            mQualifiers[INDEX_SCREEN_WIDTH] = qualifier;

        } else if (qualifier instanceof ScreenHeightQualifier) {
            mQualifiers[INDEX_SCREEN_HEIGHT] = qualifier;

} else if (qualifier instanceof ScreenSizeQualifier) {
mQualifiers[INDEX_SCREEN_SIZE] = qualifier;

} else if (qualifier instanceof ScreenRatioQualifier) {
mQualifiers[INDEX_SCREEN_RATIO] = qualifier;

} else if (qualifier instanceof ScreenOrientationQualifier) {
mQualifiers[INDEX_SCREEN_ORIENTATION] = qualifier;

} else if (qualifier instanceof DockModeQualifier) {
mQualifiers[INDEX_DOCK_MODE] = qualifier;

} else if (qualifier instanceof NightModeQualifier) {
mQualifiers[INDEX_NIGHT_MODE] = qualifier;

} else if (qualifier instanceof PixelDensityQualifier) {
mQualifiers[INDEX_PIXEL_DENSITY] = qualifier;

} else if (qualifier instanceof TouchScreenQualifier) {
mQualifiers[INDEX_TOUCH_TYPE] = qualifier;

} else if (qualifier instanceof KeyboardStateQualifier) {
mQualifiers[INDEX_KEYBOARD_STATE] = qualifier;

} else if (qualifier instanceof TextInputMethodQualifier) {
mQualifiers[INDEX_TEXT_INPUT_METHOD] = qualifier;

} else if (qualifier instanceof NavigationStateQualifier) {
mQualifiers[INDEX_NAVIGATION_STATE] = qualifier;

} else if (qualifier instanceof NavigationMethodQualifier) {
mQualifiers[INDEX_NAVIGATION_METHOD] = qualifier;

} else if (qualifier instanceof ScreenDimensionQualifier) {
mQualifiers[INDEX_SCREEN_DIMENSION] = qualifier;

} else if (qualifier instanceof VersionQualifier) {
mQualifiers[INDEX_VERSION] = qualifier;

}
}

//Synthetic comment -- @@ -295,6 +326,30 @@
return (RegionQualifier)mQualifiers[INDEX_REGION];
}

    public void setSmallestScreenWidthQualifier(SmallestScreenWidthQualifier qualifier) {
        mQualifiers[INDEX_SMALLEST_SCREEN_WIDTH] = qualifier;
    }

    public SmallestScreenWidthQualifier getSmallestScreenWidthQualifier() {
        return (SmallestScreenWidthQualifier) mQualifiers[INDEX_SMALLEST_SCREEN_WIDTH];
    }

    public void setScreenWidthQualifier(ScreenWidthQualifier qualifier) {
        mQualifiers[INDEX_SCREEN_WIDTH] = qualifier;
    }

    public ScreenWidthQualifier getScreenWidthQualifier() {
        return (ScreenWidthQualifier) mQualifiers[INDEX_SCREEN_WIDTH];
    }

    public void setScreenHeightQualifier(ScreenHeightQualifier qualifier) {
        mQualifiers[INDEX_SCREEN_HEIGHT] = qualifier;
    }

    public ScreenHeightQualifier getScreenHeightQualifier() {
        return (ScreenHeightQualifier) mQualifiers[INDEX_SCREEN_HEIGHT];
    }

public void setScreenSizeQualifier(ScreenSizeQualifier qualifier) {
mQualifiers[INDEX_SCREEN_SIZE] = qualifier;
}
//Synthetic comment -- @@ -400,6 +455,61 @@
}

/**
     * Updates the {@link SmallestScreenWidthQualifier}, {@link ScreenWidthQualifier}, and
     * {@link ScreenHeightQualifier} based on the (required) values of
     * {@link ScreenDimensionQualifier} {@link PixelDensityQualifier}, and
     * {@link ScreenOrientationQualifier}.
     *
     * Also the density cannot be {@link Density#NODPI} as it's not valid on a device.
     */
    public void updateScreenWidthAndHeight() {

        ResourceQualifier sizeQ = mQualifiers[INDEX_SCREEN_DIMENSION];
        ResourceQualifier densityQ = mQualifiers[INDEX_PIXEL_DENSITY];
        ResourceQualifier orientQ = mQualifiers[INDEX_SCREEN_ORIENTATION];

        if (sizeQ != null && densityQ != null && orientQ != null) {
            Density density = ((PixelDensityQualifier) densityQ).getValue();
            if (density == Density.NODPI) {
                return;
            }

            ScreenOrientation orientation = ((ScreenOrientationQualifier) orientQ).getValue();

            int size1 = ((ScreenDimensionQualifier) sizeQ).getValue1();
            int size2 = ((ScreenDimensionQualifier) sizeQ).getValue2();

            // make sure size1 is the biggest (should be the case, but make sure)
            if (size1 < size2) {
                int a = size1;
                size1 = size2;
                size2 = a;
            }

            // compute the dp. round them up since we want -w480dp to match a 480.5dp screen
            int dp1 = (int) Math.ceil(size1 * Density.DEFAULT_DENSITY / density.getDpiValue());
            int dp2 = (int) Math.ceil(size2 * Density.DEFAULT_DENSITY / density.getDpiValue());

            setSmallestScreenWidthQualifier(new SmallestScreenWidthQualifier(dp2));

            switch (orientation) {
                case PORTRAIT:
                    setScreenWidthQualifier(new ScreenWidthQualifier(dp2));
                    setScreenHeightQualifier(new ScreenHeightQualifier(dp1));
                    break;
                case LANDSCAPE:
                    setScreenWidthQualifier(new ScreenWidthQualifier(dp1));
                    setScreenHeightQualifier(new ScreenHeightQualifier(dp2));
                    break;
                case SQUARE:
                    setScreenWidthQualifier(new ScreenWidthQualifier(dp2));
                    setScreenHeightQualifier(new ScreenHeightQualifier(dp2));
                    break;
            }
        }
    }

    /**
* Returns whether an object is equals to the receiver.
*/
@Override
//Synthetic comment -- @@ -704,6 +814,7 @@
return false;
}
}

return true;
}

//Synthetic comment -- @@ -732,6 +843,9 @@
mQualifiers[INDEX_NETWORK_CODE] = new NetworkCodeQualifier();
mQualifiers[INDEX_LANGUAGE] = new LanguageQualifier();
mQualifiers[INDEX_REGION] = new RegionQualifier();
        mQualifiers[INDEX_SMALLEST_SCREEN_WIDTH] = new SmallestScreenWidthQualifier();
        mQualifiers[INDEX_SCREEN_WIDTH] = new ScreenWidthQualifier();
        mQualifiers[INDEX_SCREEN_HEIGHT] = new ScreenHeightQualifier();
mQualifiers[INDEX_SCREEN_SIZE] = new ScreenSizeQualifier();
mQualifiers[INDEX_SCREEN_RATIO] = new ScreenRatioQualifier();
mQualifiers[INDEX_SCREEN_ORIENTATION] = new ScreenOrientationQualifier();








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/configuration/ScreenDimensionQualifier.java b/ide_common/src/com/android/ide/common/resources/configuration/ScreenDimensionQualifier.java
//Synthetic comment -- index a58789a..9b42b88 100644

//Synthetic comment -- @@ -144,7 +144,7 @@

@Override
public String getShortDisplayValue() {
        if (isValid()) {
return String.format("%1$dx%2$d", mValue1, mValue2);
}

//Synthetic comment -- @@ -153,7 +153,7 @@

@Override
public String getLongDisplayValue() {
        if (isValid()) {
return String.format("Screen resolution %1$dx%2$d", mValue1, mValue2);
}









//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/configuration/ScreenHeightQualifier.java b/ide_common/src/com/android/ide/common/resources/configuration/ScreenHeightQualifier.java
new file mode 100644
//Synthetic comment -- index 0000000..2899631

//Synthetic comment -- @@ -0,0 +1,169 @@
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

package com.android.ide.common.resources.configuration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Resource Qualifier for Screen Pixel Density.
 */
public final class ScreenHeightQualifier extends ResourceQualifier {
    /** Default screen size value. This means the property is not set */
    final static int DEFAULT_SIZE = -1;

    private final static Pattern sParsePattern = Pattern.compile("^h(\\d+)dp$");//$NON-NLS-1$
    private final static String sPrintPattern = "h%1$ddp";

    public static final String NAME = "Screen Height";

    private int mValue = DEFAULT_SIZE;

    public ScreenHeightQualifier() {
        // pass
    }

    public ScreenHeightQualifier(int value) {
        mValue = value;
    }

    public int getValue() {
        return mValue;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getShortName() {
        return NAME;
    }

    @Override
    public boolean hasFakeValue() {
        return false;
    }

    @Override
    public boolean isValid() {
        return mValue != DEFAULT_SIZE;
    }

    @Override
    public boolean checkAndSet(String value, FolderConfiguration config) {
        Matcher m = sParsePattern.matcher(value);
        if (m.matches()) {
            String v = m.group(1);

            ScreenHeightQualifier qualifier = getQualifier(v);
            if (qualifier != null) {
                config.setScreenHeightQualifier(qualifier);
                return true;
            }
        }

        return false;
    }

    public static ScreenHeightQualifier getQualifier(String value) {
        try {
            int dp = Integer.parseInt(value);

            ScreenHeightQualifier qualifier = new ScreenHeightQualifier();
            qualifier.mValue = dp;
            return qualifier;

        } catch (NumberFormatException e) {
        }

        return null;
    }

    @Override
    public boolean isMatchFor(ResourceQualifier qualifier) {
        // this is the match only of the current dp value is lower or equal to the
        if (qualifier instanceof ScreenHeightQualifier) {
            return mValue <= ((ScreenHeightQualifier) qualifier).mValue;
        }

        return false;
    }

    @Override
    public boolean isBetterMatchThan(ResourceQualifier compareTo, ResourceQualifier reference) {
        if (compareTo == null) {
            return true;
        }

        ScreenHeightQualifier compareQ = (ScreenHeightQualifier)compareTo;
        ScreenHeightQualifier referenceQ = (ScreenHeightQualifier)reference;

        if (compareQ.mValue == referenceQ.mValue) {
            // what we have is already the best possible match (exact match)
            return false;
        } else if (mValue == referenceQ.mValue) {
            // got new exact value, this is the best!
            return true;
        } else {
            // get the qualifier that has the width that is the closest to the reference, but not
            // above. (which is guaranteed when this is called as isMatchFor is called first.
            return mValue > compareQ.mValue;
        }
    }

    @Override
    public String getFolderSegment() {
        return String.format(sPrintPattern, mValue);
    }

    @Override
    public String getShortDisplayValue() {
        if (isValid()) {
            return getFolderSegment();
        }

        return ""; //$NON-NLS-1$
    }

    @Override
    public String getLongDisplayValue() {
        if (isValid()) {
            return getFolderSegment();
        }

        return ""; //$NON-NLS-1$
    }

    @Override
    public int hashCode() {
        return mValue;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (getClass() != obj.getClass())
            return false;
        ScreenHeightQualifier other = (ScreenHeightQualifier) obj;
        if (mValue != other.mValue)
            return false;
        return true;
    }
}








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/configuration/ScreenWidthQualifier.java b/ide_common/src/com/android/ide/common/resources/configuration/ScreenWidthQualifier.java
new file mode 100644
//Synthetic comment -- index 0000000..8748864

//Synthetic comment -- @@ -0,0 +1,169 @@
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

package com.android.ide.common.resources.configuration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Resource Qualifier for Screen Pixel Density.
 */
public final class ScreenWidthQualifier extends ResourceQualifier {
    /** Default screen size value. This means the property is not set */
    final static int DEFAULT_SIZE = -1;

    private final static Pattern sParsePattern = Pattern.compile("^w(\\d+)dp$"); //$NON-NLS-1$
    private final static String sPrintPattern = "w%1$ddp"; //$NON-NLS-1$

    public static final String NAME = "Screen Width";

    private int mValue = DEFAULT_SIZE;

    public ScreenWidthQualifier() {
        // pass
    }

    public ScreenWidthQualifier(int value) {
        mValue = value;
    }

    public int getValue() {
        return mValue;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getShortName() {
        return NAME;
    }

    @Override
    public boolean hasFakeValue() {
        return false;
    }

    @Override
    public boolean isValid() {
        return mValue != DEFAULT_SIZE;
    }

    @Override
    public boolean checkAndSet(String value, FolderConfiguration config) {
        Matcher m = sParsePattern.matcher(value);
        if (m.matches()) {
            String v = m.group(1);

            ScreenWidthQualifier qualifier = getQualifier(v);
            if (qualifier != null) {
                config.setScreenWidthQualifier(qualifier);
                return true;
            }
        }

        return false;
    }

    public static ScreenWidthQualifier getQualifier(String value) {
        try {
            int dp = Integer.parseInt(value);

            ScreenWidthQualifier qualifier = new ScreenWidthQualifier();
            qualifier.mValue = dp;
            return qualifier;

        } catch (NumberFormatException e) {
        }

        return null;
    }

    @Override
    public boolean isMatchFor(ResourceQualifier qualifier) {
        // this is the match only of the current dp value is lower or equal to the
        if (qualifier instanceof ScreenWidthQualifier) {
            return mValue <= ((ScreenWidthQualifier) qualifier).mValue;
        }

        return false;
    }

    @Override
    public boolean isBetterMatchThan(ResourceQualifier compareTo, ResourceQualifier reference) {
        if (compareTo == null) {
            return true;
        }

        ScreenWidthQualifier compareQ = (ScreenWidthQualifier)compareTo;
        ScreenWidthQualifier referenceQ = (ScreenWidthQualifier)reference;

        if (compareQ.mValue == referenceQ.mValue) {
            // what we have is already the best possible match (exact match)
            return false;
        } else if (mValue == referenceQ.mValue) {
            // got new exact value, this is the best!
            return true;
        } else {
            // get the qualifier that has the width that is the closest to the reference, but not
            // above. (which is guaranteed when this is called as isMatchFor is called first.
            return mValue > compareQ.mValue;
        }
    }

    @Override
    public String getFolderSegment() {
        return String.format(sPrintPattern, mValue);
    }

    @Override
    public String getShortDisplayValue() {
        if (isValid()) {
            return getFolderSegment();
        }

        return ""; //$NON-NLS-1$
    }

    @Override
    public String getLongDisplayValue() {
        if (isValid()) {
            return getFolderSegment();
        }

        return ""; //$NON-NLS-1$
    }

    @Override
    public int hashCode() {
        return mValue;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (getClass() != obj.getClass())
            return false;
        ScreenWidthQualifier other = (ScreenWidthQualifier) obj;
        if (mValue != other.mValue)
            return false;
        return true;
    }
}








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/configuration/SmallestScreenWidthQualifier.java b/ide_common/src/com/android/ide/common/resources/configuration/SmallestScreenWidthQualifier.java
new file mode 100644
//Synthetic comment -- index 0000000..e151805

//Synthetic comment -- @@ -0,0 +1,169 @@
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

package com.android.ide.common.resources.configuration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Resource Qualifier for Screen Pixel Density.
 */
public final class SmallestScreenWidthQualifier extends ResourceQualifier {
    /** Default screen size value. This means the property is not set */
    final static int DEFAULT_SIZE = -1;

    private final static Pattern sParsePattern = Pattern.compile("^sw(\\d+)dp$"); //$NON-NLS-1$
    private final static String sPrintPattern = "sw%1$ddp"; //$NON-NLS-1$

    public static final String NAME = "Smallest Screen Width";

    private int mValue = DEFAULT_SIZE;

    public SmallestScreenWidthQualifier() {
        // pass
    }

    public SmallestScreenWidthQualifier(int value) {
        mValue = value;
    }

    public int getValue() {
        return mValue;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getShortName() {
        return NAME;
    }

    @Override
    public boolean hasFakeValue() {
        return false;
    }

    @Override
    public boolean isValid() {
        return mValue != DEFAULT_SIZE;
    }

    @Override
    public boolean checkAndSet(String value, FolderConfiguration config) {
        Matcher m = sParsePattern.matcher(value);
        if (m.matches()) {
            String v = m.group(1);

            SmallestScreenWidthQualifier qualifier = getQualifier(v);
            if (qualifier != null) {
                config.setSmallestScreenWidthQualifier(qualifier);
                return true;
            }
        }

        return false;
    }

    public static SmallestScreenWidthQualifier getQualifier(String value) {
        try {
            int dp = Integer.parseInt(value);

            SmallestScreenWidthQualifier qualifier = new SmallestScreenWidthQualifier();
            qualifier.mValue = dp;
            return qualifier;

        } catch (NumberFormatException e) {
        }

        return null;
    }

    @Override
    public boolean isMatchFor(ResourceQualifier qualifier) {
        // this is the match only of the current dp value is lower or equal to the
        if (qualifier instanceof SmallestScreenWidthQualifier) {
            return mValue <= ((SmallestScreenWidthQualifier) qualifier).mValue;
        }

        return false;
    }

    @Override
    public boolean isBetterMatchThan(ResourceQualifier compareTo, ResourceQualifier reference) {
        if (compareTo == null) {
            return true;
        }

        SmallestScreenWidthQualifier compareQ = (SmallestScreenWidthQualifier)compareTo;
        SmallestScreenWidthQualifier referenceQ = (SmallestScreenWidthQualifier)reference;

        if (compareQ.mValue == referenceQ.mValue) {
            // what we have is already the best possible match (exact match)
            return false;
        } else if (mValue == referenceQ.mValue) {
            // got new exact value, this is the best!
            return true;
        } else {
            // get the qualifier that has the width that is the closest to the reference, but not
            // above. (which is guaranteed when this is called as isMatchFor is called first.
            return mValue > compareQ.mValue;
        }
    }

    @Override
    public String getFolderSegment() {
        return String.format(sPrintPattern, mValue);
    }

    @Override
    public String getShortDisplayValue() {
        if (isValid()) {
            return getFolderSegment();
        }

        return ""; //$NON-NLS-1$
    }

    @Override
    public String getLongDisplayValue() {
        if (isValid()) {
            return getFolderSegment();
        }

        return ""; //$NON-NLS-1$
    }

    @Override
    public int hashCode() {
        return mValue;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (getClass() != obj.getClass())
            return false;
        SmallestScreenWidthQualifier other = (SmallestScreenWidthQualifier) obj;
        if (mValue != other.mValue)
            return false;
        return true;
    }
}








//Synthetic comment -- diff --git a/ide_common/tests/src/com/android/ide/common/resources/configuration/DockModeQualifierTest.java b/ide_common/tests/src/com/android/ide/common/resources/configuration/DockModeQualifierTest.java
//Synthetic comment -- index 195d474..c2b7452 100644

//Synthetic comment -- @@ -24,6 +24,7 @@

private DockModeQualifier mCarQualifier;
private DockModeQualifier mDeskQualifier;
    private DockModeQualifier mTVQualifier;
private DockModeQualifier mNoneQualifier;

@Override
//Synthetic comment -- @@ -31,6 +32,7 @@
super.setUp();
mCarQualifier = new DockModeQualifier(DockMode.CAR);
mDeskQualifier = new DockModeQualifier(DockMode.DESK);
        mTVQualifier = new DockModeQualifier(DockMode.TELEVISION);
mNoneQualifier = new DockModeQualifier(DockMode.NONE);
}

//Synthetic comment -- @@ -39,11 +41,13 @@
super.tearDown();
mCarQualifier = null;
mDeskQualifier = null;
        mTVQualifier = null;
mNoneQualifier = null;
}

public void testIsBetterMatchThan() {
assertTrue(mNoneQualifier.isBetterMatchThan(mCarQualifier, mDeskQualifier));
        assertTrue(mNoneQualifier.isBetterMatchThan(mCarQualifier, mDeskQualifier));
assertFalse(mNoneQualifier.isBetterMatchThan(mDeskQualifier, mDeskQualifier));
assertTrue(mNoneQualifier.isBetterMatchThan(mDeskQualifier, mCarQualifier));
assertFalse(mNoneQualifier.isBetterMatchThan(mCarQualifier, mCarQualifier));
//Synthetic comment -- @@ -53,17 +57,27 @@

assertTrue(mCarQualifier.isBetterMatchThan(mDeskQualifier, mCarQualifier));
assertFalse(mCarQualifier.isBetterMatchThan(mDeskQualifier, mDeskQualifier));

        assertTrue(mTVQualifier.isBetterMatchThan(mCarQualifier, mTVQualifier));
        assertFalse(mTVQualifier.isBetterMatchThan(mDeskQualifier, mDeskQualifier));

}

public void testIsMatchFor() {
assertTrue(mNoneQualifier.isMatchFor(mCarQualifier));
assertTrue(mNoneQualifier.isMatchFor(mDeskQualifier));
        assertTrue(mNoneQualifier.isMatchFor(mTVQualifier));
assertTrue(mCarQualifier.isMatchFor(mCarQualifier));
assertTrue(mDeskQualifier.isMatchFor(mDeskQualifier));
        assertTrue(mTVQualifier.isMatchFor(mTVQualifier));

assertFalse(mCarQualifier.isMatchFor(mNoneQualifier));
assertFalse(mCarQualifier.isMatchFor(mDeskQualifier));

assertFalse(mDeskQualifier.isMatchFor(mCarQualifier));
assertFalse(mDeskQualifier.isMatchFor(mNoneQualifier));

        assertFalse(mTVQualifier.isMatchFor(mCarQualifier));
        assertFalse(mTVQualifier.isMatchFor(mNoneQualifier));
}
}







