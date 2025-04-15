/*Remove references to SWT from classes that are moving to ide-common.

- Move the SWT based getIcon from the resource qualifier and use a static
  helper instead.

- Make a new enum ResourceDeltaKind to replace the IResourceDelta
  integer constants, and use the same helper class to convert from
  one to the other.

- Make ResourceRepository exclusively use the IAbstractFile/Folder

- Use the ILogger in place of AdtPlugin (which implements ILogger)

Change-Id:I0011c01ac2064b07d28c980112c47cfa433ee372*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceDeltaKind.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceDeltaKind.java
new file mode 100644
//Synthetic comment -- index 0000000..80a0ec7

//Synthetic comment -- @@ -0,0 +1,5 @@
package com.android.ide.eclipse.adt.internal.resources;

public enum ResourceDeltaKind {
    CHANGED, ADDED, REMOVED;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceHelper.java
//Synthetic comment -- index 568174c..4d482f5 100644

//Synthetic comment -- @@ -16,25 +16,112 @@

package com.android.ide.eclipse.adt.internal.resources;

import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.resources.configurations.CountryCodeQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.DockModeQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.KeyboardStateQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.LanguageQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.NavigationMethodQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.NavigationStateQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.NetworkCodeQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.NightModeQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.PixelDensityQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.RegionQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.ResourceQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.ScreenDimensionQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.ScreenOrientationQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.ScreenRatioQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.ScreenSizeQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.TextInputMethodQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.TouchScreenQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.VersionQualifier;

import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.swt.graphics.Image;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class to deal with SWT specifics for the resources.
 */
public class ResourceHelper {

    private final static Map<Class<?>, Image> ICON_MAP = new HashMap<Class<?>, Image>(20);

/**
     * Returns the icon for the qualifier.
*/
    public static Image getIcon(Class<? extends ResourceQualifier> theClass) {
        Image image = ICON_MAP.get(theClass);
        if (image == null) {
            image = computeImage(theClass);
            ICON_MAP.put(theClass, image);
}

        return image;
}

    private static Image computeImage(Class<? extends ResourceQualifier> theClass) {
        if (theClass == CountryCodeQualifier.class) {
            return IconFactory.getInstance().getIcon("mcc"); //$NON-NLS-1$
        } else if (theClass == NetworkCodeQualifier.class) {
            return IconFactory.getInstance().getIcon("mnc"); //$NON-NLS-1$
        } else if (theClass == LanguageQualifier.class) {
            return IconFactory.getInstance().getIcon("language"); //$NON-NLS-1$
        } else if (theClass == RegionQualifier.class) {
            return IconFactory.getInstance().getIcon("region"); //$NON-NLS-1$
        } else if (theClass == ScreenSizeQualifier.class) {
            return IconFactory.getInstance().getIcon("size"); //$NON-NLS-1$
        } else if (theClass == ScreenRatioQualifier.class) {
            return IconFactory.getInstance().getIcon("ratio"); //$NON-NLS-1$
        } else if (theClass == ScreenOrientationQualifier.class) {
            return IconFactory.getInstance().getIcon("orientation"); //$NON-NLS-1$
        } else if (theClass == DockModeQualifier.class) {
            return IconFactory.getInstance().getIcon("dockmode"); //$NON-NLS-1$
        } else if (theClass == NightModeQualifier.class) {
            return IconFactory.getInstance().getIcon("nightmode"); //$NON-NLS-1$
        } else if (theClass == PixelDensityQualifier.class) {
            return IconFactory.getInstance().getIcon("dpi"); //$NON-NLS-1$
        } else if (theClass == TouchScreenQualifier.class) {
            return IconFactory.getInstance().getIcon("touch"); //$NON-NLS-1$
        } else if (theClass == KeyboardStateQualifier.class) {
            return IconFactory.getInstance().getIcon("keyboard"); //$NON-NLS-1$
        } else if (theClass == TextInputMethodQualifier.class) {
            return IconFactory.getInstance().getIcon("text_input"); //$NON-NLS-1$
        } else if (theClass == NavigationStateQualifier.class) {
            return IconFactory.getInstance().getIcon("navpad"); //$NON-NLS-1$
        } else if (theClass == NavigationMethodQualifier.class) {
            return IconFactory.getInstance().getIcon("navpad"); //$NON-NLS-1$
        } else if (theClass == ScreenDimensionQualifier.class) {
            return IconFactory.getInstance().getIcon("dimension"); //$NON-NLS-1$
        } else if (theClass == VersionQualifier.class) {
            return IconFactory.getInstance().getIcon("version"); //$NON-NLS-1$
        }

        // this can only happen if we forget to add a class above.
        return null;
    }

    /**
     * Returns a {@link ResourceDeltaKind} from an {@link IResourceDelta} value.
     * @param kind a {@link IResourceDelta} integer constant.
     * @return a matching {@link ResourceDeltaKind} or null.
     *
     * @see IResourceDelta#ADDED
     * @see IResourceDelta#REMOVED
     * @see IResourceDelta#CHANGED
     */
    public static ResourceDeltaKind getResourceDeltaKind(int kind) {
        switch (kind) {
            case IResourceDelta.ADDED:
                return ResourceDeltaKind.ADDED;
            case IResourceDelta.REMOVED:
                return ResourceDeltaKind.REMOVED;
            case IResourceDelta.CHANGED:
                return ResourceDeltaKind.CHANGED;
        }

        return null;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/CountryCodeQualifier.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/CountryCodeQualifier.java
//Synthetic comment -- index fcad2dc..42dc6e1 100644

//Synthetic comment -- @@ -16,10 +16,6 @@

package com.android.ide.eclipse.adt.internal.resources.configurations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -98,11 +94,6 @@
}

@Override
public boolean isValid() {
return mCode != DEFAULT_CODE;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/DockModeQualifier.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/DockModeQualifier.java
//Synthetic comment -- index 37af3b4..32dab22 100644

//Synthetic comment -- @@ -16,12 +16,9 @@

package com.android.ide.eclipse.adt.internal.resources.configurations;

import com.android.resources.DockMode;
import com.android.resources.ResourceEnum;

/**
* Resource Qualifier for Navigation Method.
*/
//Synthetic comment -- @@ -58,12 +55,6 @@
return "Dock Mode";
}

@Override
public boolean checkAndSet(String value, FolderConfiguration config) {
DockMode mode = DockMode.getEnum(value);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/KeyboardStateQualifier.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/KeyboardStateQualifier.java
//Synthetic comment -- index 3cf6091..3b331f7 100644

//Synthetic comment -- @@ -16,12 +16,9 @@

package com.android.ide.eclipse.adt.internal.resources.configurations;

import com.android.resources.KeyboardState;
import com.android.resources.ResourceEnum;

/**
* Resource Qualifier for keyboard state.
*/
//Synthetic comment -- @@ -59,11 +56,6 @@
}

@Override
public boolean checkAndSet(String value, FolderConfiguration config) {
KeyboardState orientation = KeyboardState.getEnum(value);
if (orientation != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/LanguageQualifier.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/LanguageQualifier.java
//Synthetic comment -- index 2686eac..73809a1 100644

//Synthetic comment -- @@ -16,10 +16,6 @@

package com.android.ide.eclipse.adt.internal.resources.configurations;

import java.util.regex.Pattern;

/**
//Synthetic comment -- @@ -90,11 +86,6 @@
}

@Override
public boolean isValid() {
return mValue != null;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/NavigationMethodQualifier.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/NavigationMethodQualifier.java
//Synthetic comment -- index 5faa293..32020d9 100644

//Synthetic comment -- @@ -16,12 +16,9 @@

package com.android.ide.eclipse.adt.internal.resources.configurations;

import com.android.resources.Navigation;
import com.android.resources.ResourceEnum;

/**
* Resource Qualifier for Navigation Method.
*/
//Synthetic comment -- @@ -58,12 +55,6 @@
return NAME;
}

@Override
public boolean checkAndSet(String value, FolderConfiguration config) {
Navigation method = Navigation.getEnum(value);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/NavigationStateQualifier.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/NavigationStateQualifier.java
//Synthetic comment -- index 8cea2d3..d1f6513 100644

//Synthetic comment -- @@ -16,12 +16,9 @@

package com.android.ide.eclipse.adt.internal.resources.configurations;

import com.android.resources.NavigationState;
import com.android.resources.ResourceEnum;

/**
* Resource Qualifier for navigation state.
*/
//Synthetic comment -- @@ -59,11 +56,6 @@
}

@Override
public boolean checkAndSet(String value, FolderConfiguration config) {
NavigationState state = NavigationState.getEnum(value);
if (state != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/NetworkCodeQualifier.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/NetworkCodeQualifier.java
//Synthetic comment -- index 4ef0c75..63dddea 100644

//Synthetic comment -- @@ -16,10 +16,6 @@

package com.android.ide.eclipse.adt.internal.resources.configurations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -98,11 +94,6 @@
}

@Override
public boolean isValid() {
return mCode != DEFAULT_CODE;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/NightModeQualifier.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/NightModeQualifier.java
//Synthetic comment -- index 15aea6b..058bea7 100644

//Synthetic comment -- @@ -16,12 +16,9 @@

package com.android.ide.eclipse.adt.internal.resources.configurations;

import com.android.resources.NightMode;
import com.android.resources.ResourceEnum;

/**
* Resource Qualifier for Navigation Method.
*/
//Synthetic comment -- @@ -59,11 +56,6 @@
}

@Override
public boolean checkAndSet(String value, FolderConfiguration config) {
NightMode mode = NightMode.getEnum(value);
if (mode != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/PixelDensityQualifier.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/PixelDensityQualifier.java
//Synthetic comment -- index 32fc0c5..e730a39 100644

//Synthetic comment -- @@ -16,12 +16,9 @@

package com.android.ide.eclipse.adt.internal.resources.configurations;

import com.android.resources.Density;
import com.android.resources.ResourceEnum;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -63,11 +60,6 @@
}

@Override
public boolean checkAndSet(String value, FolderConfiguration config) {
Density density = Density.getEnum(value);
if (density == null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/RegionQualifier.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/RegionQualifier.java
//Synthetic comment -- index dfe02cf..2a9bd36 100644

//Synthetic comment -- @@ -16,10 +16,6 @@

package com.android.ide.eclipse.adt.internal.resources.configurations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -94,11 +90,6 @@
}

@Override
public boolean isValid() {
return mValue != null;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/ResourceQualifier.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/ResourceQualifier.java
//Synthetic comment -- index b4d9a34..14d6bd0 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.ide.eclipse.adt.internal.resources.configurations;


/**
* Base class for resource qualifiers.
//Synthetic comment -- @@ -36,11 +35,6 @@
public abstract String getShortName();

/**
* Returns whether the qualifier has a valid filter value.
*/
public abstract boolean isValid();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/ScreenDimensionQualifier.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/ScreenDimensionQualifier.java
//Synthetic comment -- index c9ff7c2..61fa94e 100644

//Synthetic comment -- @@ -16,10 +16,6 @@

package com.android.ide.eclipse.adt.internal.resources.configurations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -75,11 +71,6 @@
}

@Override
public boolean isValid() {
return mValue1 != DEFAULT_SIZE && mValue2 != DEFAULT_SIZE;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/ScreenOrientationQualifier.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/ScreenOrientationQualifier.java
//Synthetic comment -- index d7d6bd3..0ca17a6 100644

//Synthetic comment -- @@ -16,12 +16,9 @@

package com.android.ide.eclipse.adt.internal.resources.configurations;

import com.android.resources.ResourceEnum;
import com.android.resources.ScreenOrientation;

/**
* Resource Qualifier for Screen Orientation.
*/
//Synthetic comment -- @@ -58,11 +55,6 @@
}

@Override
public boolean checkAndSet(String value, FolderConfiguration config) {
ScreenOrientation orientation = ScreenOrientation.getEnum(value);
if (orientation != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/ScreenRatioQualifier.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/ScreenRatioQualifier.java
//Synthetic comment -- index 4444273..1d710ba 100644

//Synthetic comment -- @@ -16,12 +16,9 @@

package com.android.ide.eclipse.adt.internal.resources.configurations;

import com.android.resources.ResourceEnum;
import com.android.resources.ScreenRatio;

public class ScreenRatioQualifier extends EnumBasedResourceQualifier {

public static final String NAME = "Screen Ratio";
//Synthetic comment -- @@ -55,11 +52,6 @@
}

@Override
public boolean checkAndSet(String value, FolderConfiguration config) {
ScreenRatio size = ScreenRatio.getEnum(value);
if (size != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/ScreenSizeQualifier.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/ScreenSizeQualifier.java
//Synthetic comment -- index 023a861..9c0a507 100644

//Synthetic comment -- @@ -16,12 +16,9 @@

package com.android.ide.eclipse.adt.internal.resources.configurations;

import com.android.resources.ResourceEnum;
import com.android.resources.ScreenSize;

/**
* Resource Qualifier for Screen Size. Size can be "small", "normal", "large" and "x-large"
*/
//Synthetic comment -- @@ -59,11 +56,6 @@
}

@Override
public boolean checkAndSet(String value, FolderConfiguration config) {
ScreenSize size = ScreenSize.getEnum(value);
if (size != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/TextInputMethodQualifier.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/TextInputMethodQualifier.java
//Synthetic comment -- index b5ce166..a2d7fc7 100644

//Synthetic comment -- @@ -16,12 +16,9 @@

package com.android.ide.eclipse.adt.internal.resources.configurations;

import com.android.resources.Keyboard;
import com.android.resources.ResourceEnum;

/**
* Resource Qualifier for Text Input Method.
*/
//Synthetic comment -- @@ -60,11 +57,6 @@
}

@Override
public boolean checkAndSet(String value, FolderConfiguration config) {
Keyboard method = Keyboard.getEnum(value);
if (method != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/TouchScreenQualifier.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/TouchScreenQualifier.java
//Synthetic comment -- index f3b8eb0..3396b9c 100644

//Synthetic comment -- @@ -16,12 +16,9 @@

package com.android.ide.eclipse.adt.internal.resources.configurations;

import com.android.resources.ResourceEnum;
import com.android.resources.TouchScreen;


/**
* Resource Qualifier for Touch Screen type.
//Synthetic comment -- @@ -60,11 +57,6 @@
}

@Override
public boolean checkAndSet(String value, FolderConfiguration config) {
TouchScreen type = TouchScreen.getEnum(value);
if (type != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/VersionQualifier.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/VersionQualifier.java
//Synthetic comment -- index 199e804..509a218 100644

//Synthetic comment -- @@ -16,10 +16,6 @@

package com.android.ide.eclipse.adt.internal.resources.configurations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -99,11 +95,6 @@
}

@Override
public boolean isValid() {
return mVersion != DEFAULT_VERSION;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/FrameworkResources.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/FrameworkResources.java
//Synthetic comment -- index fc29dd9..4a16aa4 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import static com.android.AndroidConstants.FD_RES_VALUES;

import com.android.ide.common.log.ILogger;
import com.android.io.IAbstractFile;
import com.android.io.IAbstractFolder;
import com.android.resources.ResourceType;
//Synthetic comment -- @@ -103,7 +103,7 @@
*
* @param osFrameworkResourcePath The root folder of the resources
*/
    void loadPublicResources(IAbstractFolder resFolder, ILogger logger) {
IAbstractFolder valueFolder = resFolder.getFolder(FD_RES_VALUES);
if (valueFolder.exists() == false) {
return;
//Synthetic comment -- @@ -176,7 +176,9 @@
}
}
} catch (Exception e) {
                if (logger != null) {
                    logger.error(e, "Can't read and parse public attribute list");
                }
} finally {
if (reader != null) {
try {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java
//Synthetic comment -- index 4dd01bb..6b8106c 100644

//Synthetic comment -- @@ -20,9 +20,11 @@
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.io.IFolderWrapper;
import com.android.resources.ResourceType;
import com.android.util.Pair;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;

import java.util.ArrayList;
//Synthetic comment -- @@ -154,6 +156,17 @@
}

/**
     * Returns the {@link ResourceFolder} associated with a {@link IFolder}.
     * @param folder The {@link IFolder} object.
     * @return the {@link ResourceFolder} or null if it was not found.
     *
     * @see ResourceRepository#getResourceFolder(com.android.io.IAbstractFolder)
     */
    public ResourceFolder getResourceFolder(IFolder folder) {
        return getResourceFolder(new IFolderWrapper(folder));
    }

    /**
* Resolves a compiled resource id into the resource name and type
* @param id the resource integer id.
* @return a {@link Pair} of 2 strings { name, type } or null if the id could not be resolved








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFolder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFolder.java
//Synthetic comment -- index 09c98df..7fafe1a 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.ide.eclipse.adt.internal.resources.ResourceDeltaKind;
import com.android.ide.eclipse.adt.internal.resources.configurations.Configurable;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.io.IAbstractFile;
//Synthetic comment -- @@ -26,8 +27,6 @@
import com.android.resources.ResourceFolderType;
import com.android.resources.ResourceType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
//Synthetic comment -- @@ -66,12 +65,12 @@
* @param kind the file change kind.
* @return the {@link ResourceFile} that was created.
*/
    public ResourceFile processFile(IAbstractFile file, ResourceDeltaKind kind) {
// look for this file if it's already been created
ResourceFile resFile = getFile(file);

if (resFile == null) {
            if (kind != ResourceDeltaKind.REMOVED) {
// create a ResourceFile for it.

// check if that's a single or multi resource type folder. For now we define this by
//Synthetic comment -- @@ -94,7 +93,7 @@
addFile(resFile);
}
} else {
            if (kind == ResourceDeltaKind.REMOVED) {
removeFile(resFile);
} else {
resFile.update();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceItem.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceItem.java
//Synthetic comment -- index b085832..e11fc96 100644

//Synthetic comment -- @@ -217,6 +217,19 @@
return count;
}

    /**
     * Returns a formatted string usable in an XML to use for the {@link ResourceItem}.
     * @param system Whether this is a system resource or a project resource.
     * @return a string in the format @[type]/[name]
     */
    public String getXmlString(ResourceType type, boolean system) {
        if (type == ResourceType.ID && isDeclaredInline()) {
            return (system ? "@android:" : "@+") + type.getName() + "/" + mName; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }

        return (system ? "@android:" : "@") + type.getName() + "/" + mName; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

@Override
public String toString() {
return "ResourceItem [mName=" + mName + ", mFiles=" + mFiles + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceManager.java
//Synthetic comment -- index dc14e18..df7caf6 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.resources.ResourceHelper;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IFileListener;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IFolderListener;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IProjectListener;
//Synthetic comment -- @@ -327,7 +328,8 @@
// folder will have taken care of things.
if (folder != null) {
ResourceFile resFile = folder.processFile(
                                    new IFileWrapper(file),
                                    ResourceHelper.getResourceDeltaKind(kind));
notifyListenerOnFileChange(project, resFile, kind);
}
}
//Synthetic comment -- @@ -412,7 +414,7 @@

try {
loadResources(resources, frameworkRes);
                resources.loadPublicResources(frameworkRes, AdtPlugin.getDefault());
return resources;
} catch (IOException e) {
// since we test that folders are folders, and files are files, this shouldn't
//Synthetic comment -- @@ -459,7 +461,8 @@

for (IAbstractResource childRes : children) {
if (childRes instanceof IAbstractFile) {
                            resFolder.processFile((IAbstractFile) childRes,
                                    ResourceHelper.getResourceDeltaKind(IResourceDelta.ADDED));
}
}
}
//Synthetic comment -- @@ -512,7 +515,8 @@
IFile file = (IFile)fileRes;

resFolder.processFile(new IFileWrapper(file),
                                                ResourceHelper.getResourceDeltaKind(
                                                        IResourceDelta.ADDED));
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceRepository.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceRepository.java
//Synthetic comment -- index b10bf03..bb4e95c 100644

//Synthetic comment -- @@ -22,14 +22,11 @@
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.internal.resources.configurations.LanguageQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.RegionQualifier;
import com.android.io.IAbstractFolder;
import com.android.resources.FolderTypeRelationship;
import com.android.resources.ResourceFolderType;
import com.android.resources.ResourceType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
//Synthetic comment -- @@ -47,7 +44,7 @@
* A repository is both a file representation of a resource folder and a representation
* of the generated resources, organized by type.
*
 * {@link #getResourceFolder(IAbstractFolder)} and {@link #getSourceFiles(ResourceType, String, FolderConfiguration)}
* give access to the folders and files of the resource folder.
*
* {@link #getResources(ResourceType)} gives access to the resources directly.
//Synthetic comment -- @@ -136,7 +133,6 @@
int count = list.size();
for (int i = 0 ; i < count ; i++) {
ResourceFolder resFolder = list.get(i);
IAbstractFolder folder = resFolder.getFolder();
if (removedFolder.equals(folder)) {
// we found the matching ResourceFolder. we need to remove it.
//Synthetic comment -- @@ -297,16 +293,15 @@
}

/**
     * Returns the {@link ResourceFolder} associated with a {@link IAbstractFolder}.
     * @param folder The {@link IAbstractFolder} object.
* @return the {@link ResourceFolder} or null if it was not found.
*/
    public ResourceFolder getResourceFolder(IAbstractFolder folder) {
for (List<ResourceFolder> list : mFolderMap.values()) {
for (ResourceFolder resFolder : list) {
                IAbstractFolder wrapper = resFolder.getFolder();
                if (wrapper.equals(folder)) {
return resFolder;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ConfigurationSelector.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ConfigurationSelector.java
//Synthetic comment -- index f9748f6..0235e3f 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.ide.eclipse.adt.internal.ui;

import com.android.AndroidConstants;
import com.android.ide.eclipse.adt.internal.resources.ResourceHelper;
import com.android.ide.eclipse.adt.internal.resources.configurations.CountryCodeQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.DockModeQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
//Synthetic comment -- @@ -657,7 +658,7 @@
public Image getColumnImage(Object element, int columnIndex) {
// only one column, so we can ignore columnIndex
if (element instanceof ResourceQualifier) {
                return ResourceHelper.getIcon(((ResourceQualifier)element).getClass());
}

return null;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ReferenceChooserDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ReferenceChooserDialog.java
//Synthetic comment -- index c6bbd0d..f070e40 100644

//Synthetic comment -- @@ -19,7 +19,6 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringRefactoring;
import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringWizard;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceItem;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceRepository;
import com.android.resources.ResourceType;
//Synthetic comment -- @@ -115,8 +114,7 @@
ResourceType resourceType = (ResourceType)treeSelection.getFirstSegment();
ResourceItem resourceItem = (ResourceItem)treeSelection.getLastSegment();

                mCurrentResource = resourceItem.getXmlString(resourceType, false /* system */);
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java
//Synthetic comment -- index 83d0f96..b2c3ced 100644

//Synthetic comment -- @@ -30,7 +30,6 @@
import com.android.ide.eclipse.adt.internal.editors.xml.Hyperlinks;
import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringRefactoring;
import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringWizard;
import com.android.ide.eclipse.adt.internal.resources.ResourceNameValidator;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceItem;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceRepository;
//Synthetic comment -- @@ -158,8 +157,7 @@
if (elements.length == 1 && elements[0] instanceof ResourceItem) {
ResourceItem item = (ResourceItem)elements[0];

            mCurrentResource = item.getXmlString(mResourceType, mSystemButton.getSelection());

if (mInputValidator != null && mInputValidator.isValid(mCurrentResource) != null) {
mCurrentResource = null;
//Synthetic comment -- @@ -257,8 +255,7 @@
Object[] elements = getSelectedElements();
if (elements.length == 1 && elements[0] instanceof ResourceItem) {
ResourceItem item = (ResourceItem)elements[0];
                String current = item.getXmlString(mResourceType, mSystemButton.getSelection());
String error = mInputValidator.isValid(current);
IStatus status;
if (error != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/ResourceHelperTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/ResourceHelperTest.java
new file mode 100644
//Synthetic comment -- index 0000000..48b5ca6

//Synthetic comment -- @@ -0,0 +1,114 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.adt.internal.resources;

import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.internal.resources.configurations.ResourceQualifier;

import org.eclipse.core.resources.IResourceDelta;

import junit.framework.TestCase;

/**
 * Test ResourceHelper
 */
public class ResourceHelperTest extends TestCase {

    /**
     * temp fake qualifier class.
     */
    private static class FakeQualifierClass extends ResourceQualifier {

        @Override
        public boolean checkAndSet(String value, FolderConfiguration config) {
            return false;
        }

        @Override
        public boolean equals(Object object) {
            return false;
        }

        @Override
        public String getFolderSegment() {
            return null;
        }

        @Override
        public String getLongDisplayValue() {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public String getShortDisplayValue() {
            return null;
        }

        @Override
        public String getShortName() {
            return null;
        }

        @Override
        public boolean hasFakeValue() {
            return false;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean isValid() {
            return false;
        }

    }

    public void testgetIcon() throws Exception {
        // check that the method returns null for an unknown qualifier class
        assertNull(ResourceHelper.getIcon(FakeQualifierClass.class));

        // find all the qualifiers through FolderConfiguration.createdefault()
        FolderConfiguration config = new FolderConfiguration();
        config.createDefault();
        final int count = FolderConfiguration.getQualifierCount();
        for (int i = 0 ; i < count ; i++) {
            ResourceQualifier qual = config.getQualifier(i);
            assertNotNull(qual);
            assertNotNull(qual.getClass().getCanonicalName(),
                    ResourceHelper.getIcon(qual.getClass()));
        }
    }

    public void testGetResourceDeltaKind() {
        assertEquals(ResourceDeltaKind.ADDED,
                ResourceHelper.getResourceDeltaKind(IResourceDelta.ADDED));
        assertEquals(ResourceDeltaKind.REMOVED,
                ResourceHelper.getResourceDeltaKind(IResourceDelta.REMOVED));
        assertEquals(ResourceDeltaKind.CHANGED,
                ResourceHelper.getResourceDeltaKind(IResourceDelta.CHANGED));

        assertNull(ResourceHelper.getResourceDeltaKind(IResourceDelta.ADDED_PHANTOM));
    }
}







