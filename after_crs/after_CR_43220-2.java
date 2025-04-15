/*Validate user edits in XML files

Around ADT 15 or so we introduced a bunch of optimizations to run AAPT
much less frequently, since with large projects it can take a long
time, and end up blocking the UI if you try to save twice.

Unfortunately, one side effect of this change is that if you edit only
the *value* of an attribute, we will not re-run aapt, which means
that if you set the value to a bogus value, you will get no error
message until the next time AAPT runs (usually when you try to run).

This changeset fixes this. We already have the attribute metadata
which aapt uses, so now, when an XML file is changed and saved, we
process it with an XML pull parser, and validate all the Android
namespace attributes.  If any are found to not be correct, then we
request a full AAPT process, which will then display errors as
appropriate.

Change-Id:I374c19648e29c27c6d82616b3ee602cb2343cd3a*/




//Synthetic comment -- diff --git a/common/src/com/android/SdkConstants.java b/common/src/com/android/SdkConstants.java
//Synthetic comment -- index 1aa3853..cf64967 100644

//Synthetic comment -- @@ -816,6 +816,9 @@
public static final String UNIT_DIP = "dip";                       //$NON-NLS-1$
public static final String UNIT_SP = "sp";                         //$NON-NLS-1$
public static final String UNIT_PX = "px";                         //$NON-NLS-1$
    public static final String UNIT_IN = "in";                         //$NON-NLS-1$
    public static final String UNIT_MM = "mm";                         //$NON-NLS-1$
    public static final String UNIT_PT = "pt";                         //$NON-NLS-1$

// Filenames and folder names
public static final String ANDROID_MANIFEST_XML = "AndroidManifest.xml"; //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttributeInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttributeInfo.java
//Synthetic comment -- index 05f0adf..be928b0 100755

//Synthetic comment -- @@ -16,10 +16,31 @@

package com.android.ide.common.resources.platform;

import static com.android.SdkConstants.ANDROID_PREFIX;
import static com.android.SdkConstants.ANDROID_THEME_PREFIX;
import static com.android.SdkConstants.ID_PREFIX;
import static com.android.SdkConstants.NEW_ID_PREFIX;
import static com.android.SdkConstants.VALUE_FALSE;
import static com.android.SdkConstants.VALUE_TRUE;
import static com.android.ide.common.api.IAttributeInfo.Format.BOOLEAN;
import static com.android.ide.common.api.IAttributeInfo.Format.COLOR;
import static com.android.ide.common.api.IAttributeInfo.Format.DIMENSION;
import static com.android.ide.common.api.IAttributeInfo.Format.ENUM;
import static com.android.ide.common.api.IAttributeInfo.Format.FLAG;
import static com.android.ide.common.api.IAttributeInfo.Format.FLOAT;
import static com.android.ide.common.api.IAttributeInfo.Format.FRACTION;
import static com.android.ide.common.api.IAttributeInfo.Format.INTEGER;
import static com.android.ide.common.api.IAttributeInfo.Format.STRING;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.api.IAttributeInfo;
import com.android.ide.common.resources.ResourceRepository;
import com.android.resources.ResourceType;
import com.google.common.base.Splitter;

import java.util.EnumSet;
import java.util.regex.Pattern;


/**
//Synthetic comment -- @@ -160,4 +181,161 @@
public @NonNull String getDefinedBy() {
return mDefinedBy;
}

    private final static Pattern INTEGER_PATTERN = Pattern.compile("-?[0-9]+"); //$NON-NLS-1$
    private final static Pattern FLOAT_PATTERN =
            Pattern.compile("-?[0-9]?(\\.[0-9]+)?"); //$NON-NLS-1$
    private final static Pattern DIMENSION_PATTERN =
            Pattern.compile("-?[0-9]+(\\.[0-9]+)?(dp|dip|sp|px|pt|in|mm)"); //$NON-NLS-1$

    /**
     * Checks the given value and returns true only if it is a valid XML value
     * for this attribute.
     *
     * @param value the XML value to check
     * @param projectResources project resources to validate resource URLs with,
     *            if any
     * @param frameworkResources framework resources to validate resource URLs
     *            with, if any
     * @return true if the value is valid, false otherwise
     */
    public boolean isValid(
            @NonNull String value,
            @Nullable ResourceRepository projectResources,
            @Nullable ResourceRepository frameworkResources) {

        if (mFormats.contains(STRING) || mFormats.isEmpty()) {
            // Anything is allowed
            return true;
        }

        // All other formats require a nonempty string
        if (value.isEmpty()) {
            return false;
        }
        char first = value.charAt(0);

        // There are many attributes which are incorrectly marked in the attrs.xml
        // file, such as "duration", "minHeight", etc. These are marked as only
        // accepting "integer", but also appear to accept "reference". Therefore,
        // in these cases, be more lenient. (This happens for theme references too,
        // such as ?android:attr/listPreferredItemHeight)
        if ((first == '@' || first == '?') /* && mFormats.contains(REFERENCE)*/) {
            if (value.equals("@null")) {
                return true;
            }

            if (value.startsWith(NEW_ID_PREFIX) || value.startsWith(ID_PREFIX)) {
                // These are handled in the IdGeneratingResourceFile; we shouldn't
                // complain about not finding ids in the repository yet since they may
                // not yet have been defined (@+id's can be defined in the same layout,
                // later on.)
                return true;
            }

            if (value.startsWith(ANDROID_PREFIX) || value.startsWith(ANDROID_THEME_PREFIX)) {
                if (frameworkResources != null) {
                    return frameworkResources.hasResourceItem(value);
                }
            } else if (projectResources != null) {
                return projectResources.hasResourceItem(value);
            }

            // Validate resource string
            String url = value;
            int typeEnd = url.indexOf('/', 1);
            if (typeEnd != -1) {
                int typeBegin = url.startsWith("@+") ? 2 : 1; //$NON-NLS-1$
                int colon = url.lastIndexOf(':', typeEnd);
                if (colon != -1) {
                    typeBegin = colon + 1;
                }
                String typeName = url.substring(typeBegin, typeEnd);
                ResourceType type = ResourceType.getEnum(typeName);
                if (type != null) {
                    // TODO: Validate that the name portion conforms to the rules
                    // (is an identifier but not a keyword, etc.)
                    // Also validate that the prefix before the colon is either
                    // not there or is "android"

                    //int nameBegin = typeEnd + 1;
                    //String name = url.substring(nameBegin);
                    return true;
                }
            }
        }

        if (mFormats.contains(ENUM) && mEnumValues != null) {
            for (String e : mEnumValues) {
                if (value.equals(e)) {
                    return true;
                }
            }
        }

        if (mFormats.contains(FLAG) && mFlagValues != null) {
            for (String v : Splitter.on('|').split(value)) {
                for (String e : mFlagValues) {
                    if (v.equals(e)) {
                        return true;
                    }
                }
            }
        }

        if (mFormats.contains(DIMENSION)) {
            if (DIMENSION_PATTERN.matcher(value).matches()) {
                return true;
            }
        }

        if (mFormats.contains(BOOLEAN)) {
            if (value.equals(VALUE_TRUE) || value.equals(VALUE_FALSE)) {
                return true;
            }
        }

        if (mFormats.contains(FLOAT)) {
            if (Character.isDigit(first) || first == '-' || first == '.') {
                if (FLOAT_PATTERN.matcher(value).matches()) {
                    return true;
                }
                // AAPT accepts more general floats, such as ".1",
                try {
                    Float.parseFloat(value);
                    return true;
                } catch (NumberFormatException nufe) {
                    // Not a float
                }
            }
        }

        if (mFormats.contains(INTEGER)) {
            if (Character.isDigit(first) || first == '-') {
                if (INTEGER_PATTERN.matcher(value).matches()) {
                    return true;
                }
            }
        }

        if (mFormats.contains(COLOR)) {
            if (first == '#' && value.length() <= 9) { // Only allowed 32 bit ARGB
                try {
                    // Use Long.parseLong rather than Integer.parseInt to not overflow on
                    // 32 big hex values like "ff191919"
                    Long.parseLong(value.substring(1), 16);
                    return true;
                } catch (NumberFormatException nufe) {
                    // Not a valid color number
                }
            }
        }

        if (mFormats.contains(FRACTION)) {
            // should end with % or %p
            return true;
        }

        return false;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttrsXmlParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttrsXmlParser.java
//Synthetic comment -- index 3c1fa97..1330c50 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.ide.common.resources.platform.ViewClassInfo.LayoutParamsInfo;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.utils.ILogger;
import com.google.common.collect.Maps;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
//Synthetic comment -- @@ -54,7 +55,7 @@

// all attributes that have the same name are supposed to have the same
// parameters so we'll keep a cache of them to avoid processing them twice.
    private Map<String, AttributeInfo> mAttributeMap;

/** Map of all attribute names for a given element */
private final Map<String, DeclareStyleableInfo> mStyleMap =
//Synthetic comment -- @@ -75,7 +76,6 @@
*/
private final ILogger mLog;

/**
* Creates a new {@link AttrsXmlParser}, set to load things from the given
* XML file. Nothing has been parsed yet. Callers should call {@link #preload()}
//Synthetic comment -- @@ -84,9 +84,19 @@
* @param osAttrsXmlPath The path of the <code>attrs.xml</code> file to parse.
*              Must not be null. Should point to an existing valid XML document.
* @param log A logger object. Must not be null.
     * @param expectedAttributeCount expected number of attributes in the file
*/
    public AttrsXmlParser(String osAttrsXmlPath, ILogger log, int expectedAttributeCount) {
        this(osAttrsXmlPath, null /* inheritableAttributes */, log, expectedAttributeCount);
    }

    /**
     * Returns the parsed map of attribute infos
     *
     * @return a map from string name to {@link AttributeInfo}
     */
    public Map<String, AttributeInfo> getAttributeMap() {
        return mAttributeMap;
}

/**
//Synthetic comment -- @@ -103,24 +113,26 @@
*              If not null, the parser must have had its {@link #preload()} method
*              invoked prior to being used here.
* @param log A logger object. Must not be null.
     * @param expectedAttributeCount expected number of attributes in the file
*/
public AttrsXmlParser(
String osAttrsXmlPath,
AttrsXmlParser inheritableAttributes,
            ILogger log,
            int expectedAttributeCount) {
mOsAttrsXmlPath = osAttrsXmlPath;
mLog = log;

assert osAttrsXmlPath != null;
assert log != null;

        mAttributeMap = Maps.newHashMapWithExpectedSize(expectedAttributeCount);
if (inheritableAttributes == null) {
mEnumFlagValues = new HashMap<String, Map<String,Integer>>();
} else {
            mAttributeMap.putAll(inheritableAttributes.mAttributeMap);
mEnumFlagValues = new HashMap<String, Map<String,Integer>>(
                                                         inheritableAttributes.mEnumFlagValues);
}

// Pre-compute the set of format names such that we don't have to compute the uppercase








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index 51ae496..1507a8d 100644

//Synthetic comment -- @@ -325,7 +325,7 @@

if (ResourceManager.isAutoBuilding()) {
IdeScanningContext context = new IdeScanningContext(projectResources,
                                project, true);

resManager.processDelta(delta, context);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java
//Synthetic comment -- index 2ea70fc..b39c4cb 100644

//Synthetic comment -- @@ -19,6 +19,9 @@
import static com.android.SdkConstants.ATTR_LAYOUT_RESOURCE_PREFIX;
import static com.android.SdkConstants.PREFIX_RESOURCE_REF;
import static com.android.SdkConstants.UNIT_DP;
import static com.android.SdkConstants.UNIT_IN;
import static com.android.SdkConstants.UNIT_MM;
import static com.android.SdkConstants.UNIT_PT;
import static com.android.SdkConstants.UNIT_PX;
import static com.android.SdkConstants.UNIT_SP;
import static com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor.ATTRIBUTE_ICON_FILENAME;
//Synthetic comment -- @@ -1149,13 +1152,13 @@
"<b>Scale-independent Pixels</b> - this is like the dp unit, but it is also scaled by "
+ "the user's font size preference.",

        UNIT_PT,
"<b>Points</b> - 1/72 of an inch based on the physical size of the screen.",

        UNIT_MM,
"<b>Millimeters</b> - based on the physical size of the screen.",

        UNIT_IN,
"<b>Inches</b> - based on the physical size of the screen.",

UNIT_PX,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java
//Synthetic comment -- index de09d00..9553bc8 100644

//Synthetic comment -- @@ -22,6 +22,13 @@
import static com.android.SdkConstants.ATTR_LAYOUT_WIDTH;
import static com.android.SdkConstants.ATTR_PADDING;
import static com.android.SdkConstants.AUTO_URI;
import static com.android.SdkConstants.UNIT_DIP;
import static com.android.SdkConstants.UNIT_DP;
import static com.android.SdkConstants.UNIT_IN;
import static com.android.SdkConstants.UNIT_MM;
import static com.android.SdkConstants.UNIT_PT;
import static com.android.SdkConstants.UNIT_PX;
import static com.android.SdkConstants.UNIT_SP;
import static com.android.SdkConstants.VALUE_FILL_PARENT;
import static com.android.SdkConstants.VALUE_MATCH_PARENT;
import static com.android.SdkConstants.VIEW_FRAGMENT;
//Synthetic comment -- @@ -78,7 +85,7 @@
* Number of pixels to pad views with in exploded-rendering mode.
*/
private static final String DEFAULT_PADDING_VALUE =
        ExplodedRenderingHelper.PADDING_VALUE + UNIT_PX;

/**
* Number of pixels to pad exploded individual views with. (This is HALF the width of the
//Synthetic comment -- @@ -542,13 +549,13 @@
private static final int COMPLEX_UNIT_MM = 5;

private final static DimensionEntry[] sDimensions = new DimensionEntry[] {
        new DimensionEntry(UNIT_PX, COMPLEX_UNIT_PX),
        new DimensionEntry(UNIT_DIP, COMPLEX_UNIT_DIP),
        new DimensionEntry(UNIT_DP, COMPLEX_UNIT_DIP),
        new DimensionEntry(UNIT_SP, COMPLEX_UNIT_SP),
        new DimensionEntry(UNIT_PT, COMPLEX_UNIT_PT),
        new DimensionEntry(UNIT_IN, COMPLEX_UNIT_IN),
        new DimensionEntry(UNIT_MM, COMPLEX_UNIT_MM),
};

/**
//Synthetic comment -- @@ -564,7 +571,7 @@
padding += sIntOut[0];
}

        return padding + UNIT_PX;
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/CustomViewDescriptorService.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/CustomViewDescriptorService.java
//Synthetic comment -- index ca229a4..6df6929 100644

//Synthetic comment -- @@ -265,7 +265,7 @@
if (parser == null) {
parser = new AttrsXmlParser(
file.getFile().getOsLocation(),
                    AdtPlugin.getDefault(), 20);
parser.preload();
mParserCache.put(file, parser);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/ConvertToDpFix.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/ConvertToDpFix.java
//Synthetic comment -- index 0ca6aa2..44b676f 100644

//Synthetic comment -- @@ -15,6 +15,7 @@
*/
package com.android.ide.eclipse.adt.internal.lint;

import static com.android.SdkConstants.UNIT_PX;
import static com.android.SdkConstants.VALUE_N_DP;

import com.android.ide.eclipse.adt.AdtPlugin;
//Synthetic comment -- @@ -63,7 +64,7 @@
for (int i = 0, n = attributes.getLength(); i < n; i++) {
Attr attribute = (Attr) attributes.item(i);
String value = attribute.getValue();
                if (value.endsWith(UNIT_PX)) {
Matcher matcher = pattern.matcher(value);
if (matcher.matches()) {
String numberString = matcher.group(1);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/IdeScanningContext.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/IdeScanningContext.java
//Synthetic comment -- index 5a4fc75..d613249 100644

//Synthetic comment -- @@ -15,15 +15,20 @@
*/
package com.android.ide.eclipse.adt.internal.resources.manager;

import static com.android.SdkConstants.ANDROID_URI;
import static com.android.ide.eclipse.adt.AdtConstants.MARKER_AAPT_COMPILE;
import static org.eclipse.core.resources.IResource.DEPTH_ONE;
import static org.eclipse.core.resources.IResource.DEPTH_ZERO;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.common.resources.ScanningContext;
import com.android.ide.common.resources.platform.AttributeInfo;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.build.AaptParser;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.utils.Pair;

import org.eclipse.core.resources.IFolder;
//Synthetic comment -- @@ -36,6 +41,7 @@
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
//Synthetic comment -- @@ -50,20 +56,36 @@
private IResource mCurrentFile;
private List<Pair<IResource, String>> mErrors;
private Set<IProject> mFullAaptProjects;
    private boolean mValidate;
    private Map<String, AttributeInfo> mAttributeMap;
    private ResourceRepository mFrameworkResources;

/**
* Constructs a new {@link IdeScanningContext}
*
* @param repository the associated {@link ResourceRepository}
* @param project the associated project
     * @param validate if true, check that the attributes and resources are
     *            valid and if not request a full AAPT check
*/
    public IdeScanningContext(@NonNull ResourceRepository repository, @NonNull IProject project,
            boolean validate) {
super(repository);
mProject = project;
        mValidate = validate;

        Sdk sdk = Sdk.getCurrent();
        if (sdk != null) {
            AndroidTargetData targetData = sdk.getTargetData(project);
            if (targetData != null) {
                mAttributeMap = targetData.getAttributeMap();
                mFrameworkResources = targetData.getFrameworkResources();
            }
        }
}

@Override
    public void addError(@NonNull String error) {
super.addError(error);

if (mErrors == null) {
//Synthetic comment -- @@ -77,7 +99,7 @@
*
* @param resource the resource about to be scanned
*/
    public void startScanning(@NonNull IResource resource) {
assert mCurrentFile == null : mCurrentFile;
mCurrentFile = resource;
mScannedResources.add(resource);
//Synthetic comment -- @@ -88,7 +110,7 @@
*
* @param resource the resource that was scanned
*/
    public void finishScanning(@NonNull IResource resource) {
assert mCurrentFile != null;
mCurrentFile = null;
}
//Synthetic comment -- @@ -193,4 +215,20 @@
public Collection<IProject> getAaptRequestedProjects() {
return mFullAaptProjects;
}

    @Override
    public boolean checkValue(@Nullable String uri, @NonNull String name, @NonNull String value) {
        if (!mValidate) {
            return true;
        }

        if (!needsFullAapt() && mAttributeMap != null && ANDROID_URI.equals(uri)) {
            AttributeInfo info = mAttributeMap.get(name);
            if (info != null && !info.isValid(value, mRepository, mFrameworkResources)) {
                return false;
            }
        }

        return super.checkValue(uri, name, value);
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceManager.java
//Synthetic comment -- index b346dcd..e1a12d7 100644

//Synthetic comment -- @@ -423,7 +423,7 @@
if (delta.getResource() instanceof IProject) {
IProject project = (IProject) delta.getResource();
IdeScanningContext context =
                            new IdeScanningContext(getProjectResources(project), project, true);

processDelta(delta, context);

//Synthetic comment -- @@ -522,7 +522,7 @@
mMap.put(project, projectResources);
}
}
            IdeScanningContext context = new IdeScanningContext(projectResources, project, true);

if (resourceFolder != null && resourceFolder.exists()) {
try {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java
//Synthetic comment -- index 59a1236..85ae9fd 100644

//Synthetic comment -- @@ -16,9 +16,12 @@

package com.android.ide.eclipse.adt.internal.sdk;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.common.resources.platform.AttributeInfo;
import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.animator.AnimDescriptors;
//Synthetic comment -- @@ -87,6 +90,7 @@

private ResourceRepository mFrameworkResources;
private LayoutLibrary mLayoutLibrary;
    private Map<String, AttributeInfo> mAttributeMap;

private boolean mLayoutBridgeInit = false;

//Synthetic comment -- @@ -95,6 +99,27 @@
}

/**
     * Sets the associated map from string attribute name to
     * {@link AttributeInfo}
     *
     * @param attributeMap the map
     */
    public void setAttributeMap(@NonNull Map<String, AttributeInfo> attributeMap) {
        mAttributeMap = attributeMap;
    }

    /**
     * Returns the associated map from string attribute name to
     * {@link AttributeInfo}
     *
     * @return the map
     */
    @Nullable
    public Map<String, AttributeInfo> getAttributeMap() {
        return mAttributeMap;
    }

    /**
* Creates an AndroidTargetData object.
*/
void setExtraData(








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetParser.java
//Synthetic comment -- index 24eadfa..9a1fd3d 100644

//Synthetic comment -- @@ -129,15 +129,17 @@
progress.subTask("Attributes definitions");
AttrsXmlParser attrsXmlParser = new AttrsXmlParser(
mAndroidTarget.getPath(IAndroidTarget.ATTRIBUTES),
                    AdtPlugin.getDefault(),
                    1000);
attrsXmlParser.preload();

progress.worked(1);

progress.subTask("Manifest definitions");
AttrsXmlParser attrsManifestXmlParser = new AttrsXmlParser(
mAndroidTarget.getPath(IAndroidTarget.MANIFEST_ATTRIBUTES),
attrsXmlParser,
                    AdtPlugin.getDefault(), 1100);
attrsManifestXmlParser.preload();
progress.worked(1);

//Synthetic comment -- @@ -290,6 +292,8 @@
frameworkResources,
layoutBridge);

            targetData.setAttributeMap(attrsXmlParser.getAttributeMap());

Sdk.getCurrent().setTargetData(mAndroidTarget, targetData);

return Status.OK_STATUS;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index b1b057b..1a299d9 100644

//Synthetic comment -- @@ -16,9 +16,9 @@

package com.android.ide.eclipse.adt.internal.sdk;

import static com.android.SdkConstants.DOT_XML;
import static com.android.SdkConstants.EXT_JAR;
import static com.android.SdkConstants.FD_RES;

import com.android.SdkConstants;
import com.android.annotations.NonNull;
//Synthetic comment -- @@ -215,6 +215,7 @@
* Returns the lock object used to synchronize all operations dealing with SDK, targets and
* projects.
*/
    @NonNull
public static final Object getLock() {
return LOCK;
}
//Synthetic comment -- @@ -224,6 +225,7 @@
* <p/>If the SDK failed to load, it displays an error to the user.
* @param sdkLocation the OS path to the SDK.
*/
    @Nullable
public static Sdk loadSdk(String sdkLocation) {
synchronized (LOCK) {
if (sCurrentSdk != null) {
//Synthetic comment -- @@ -298,6 +300,7 @@
/**
* Returns the current {@link Sdk} object.
*/
    @Nullable
public static Sdk getCurrent() {
synchronized (LOCK) {
return sCurrentSdk;
//Synthetic comment -- @@ -336,6 +339,7 @@
*
* @return A file:// URL on the local documentation folder if it exists or null.
*/
    @Nullable
public String getDocumentationBaseUrl() {
return mDocBaseUrl;
}
//Synthetic comment -- @@ -367,7 +371,8 @@
* @param hash the {@link IAndroidTarget} hash string.
* @return The matching {@link IAndroidTarget} or null.
*/
    @Nullable
    public IAndroidTarget getTargetFromHashString(@NonNull String hash) {
return mManager.getTargetFromHashString(hash);
}

//Synthetic comment -- @@ -377,9 +382,9 @@
* @param project the project to initialize
* @param target the project's target.
* @throws IOException if creating the file failed in any way.
     * @throws StreamException if processing the project property file fails
*/
    public void initProject(@Nullable IProject project, @Nullable IAndroidTarget target)
throws IOException, StreamException {
if (project == null || target == null) {
return;
//Synthetic comment -- @@ -423,6 +428,7 @@
* @param project the request project
* @return the ProjectState for the project.
*/
    @Nullable
@SuppressWarnings("deprecation")
public static ProjectState getProjectState(IProject project) {
if (project == null) {
//Synthetic comment -- @@ -492,6 +498,7 @@
/**
* Returns the {@link IAndroidTarget} object associated with the given {@link IProject}.
*/
    @Nullable
public IAndroidTarget getTarget(IProject project) {
if (project == null) {
return null;
//Synthetic comment -- @@ -512,6 +519,7 @@
* @param state the state representing the project to load.
* @return the target that was loaded.
*/
    @Nullable
public IAndroidTarget loadTarget(ProjectState state) {
IAndroidTarget target = null;
if (state != null) {
//Synthetic comment -- @@ -537,6 +545,7 @@
* If the target is already loaded, nothing happens.
* @return The load status if the target data is already loaded.
*/
    @NonNull
public LoadStatus checkAndLoadTargetData(final IAndroidTarget target, IJavaProject project) {
boolean loadData = false;

//Synthetic comment -- @@ -633,6 +642,7 @@
/**
* Return the {@link AndroidTargetData} for a given {@link IAndroidTarget}.
*/
    @Nullable
public AndroidTargetData getTargetData(IAndroidTarget target) {
synchronized (LOCK) {
return mTargetDataMap.get(target);
//Synthetic comment -- @@ -642,6 +652,7 @@
/**
* Return the {@link AndroidTargetData} for a given {@link IProject}.
*/
    @Nullable
public AndroidTargetData getTargetData(IProject project) {
synchronized (LOCK) {
IAndroidTarget target = getTarget(project);
//Synthetic comment -- @@ -665,11 +676,13 @@
* Returns the {@link AvdManager}. If the AvdManager failed to parse the AVD folder, this could
* be <code>null</code>.
*/
    @Nullable
public AvdManager getAvdManager() {
return mAvdManager;
}

    @Nullable
    public static AndroidVersion getDeviceVersion(@NonNull IDevice device) {
try {
Map<String, String> props = device.getProperties();
String apiLevel = props.get(IDevice.PROP_BUILD_API_LEVEL);
//Synthetic comment -- @@ -684,11 +697,13 @@
}
}

    @NonNull
public DeviceManager getDeviceManager() {
return mDeviceManager;
}

/** Returns the devices provided by the SDK, including user created devices */
    @NonNull
public List<Device> getDevices() {
return mDeviceManager.getDevices(getSdkLocation());
}
//Synthetic comment -- @@ -699,10 +714,11 @@
* @param project the library project.
* @return a possibly empty list of ProjectState.
*/
    @NonNull
public static Set<ProjectState> getMainProjectsFor(IProject project) {
synchronized (LOCK) {
// first get the project directly depending on this.
            Set<ProjectState> list = new HashSet<ProjectState>();

// loop on all project and see if ProjectState.getLibrary returns a non null
// project.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttributeInfoTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttributeInfoTest.java
new file mode 100644
//Synthetic comment -- index 0000000..7ae96a8

//Synthetic comment -- @@ -0,0 +1,371 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
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
package com.android.ide.common.resources.platform;

import static com.android.SdkConstants.ANDROID_URI;
import static com.android.SdkConstants.DOT_XML;

import com.android.annotations.NonNull;
import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.common.resources.ResourceItem;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
import com.android.resources.ResourceType;
import com.android.utils.StdLogger;
import com.google.common.base.Charsets;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.io.Files;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

@SuppressWarnings("javadoc")
public class AttributeInfoTest extends TestCase {
    public void testSimple() throws Exception {
        AttributeInfo info = new AttributeInfo("test", EnumSet.noneOf(Format.class));
        assertTrue(info.isValid("", null, null));
        assertTrue(info.isValid("a b c", null, null));
        assertTrue(info.isValid("@android foo bar", null, null));
    }

    public void testIsValidString() throws Exception {
        AttributeInfo info = new AttributeInfo("test", Format.STRING_SET);
        assertTrue(info.isValid("", null, null));
        assertTrue(info.isValid("a b c", null, null));
        assertTrue(info.isValid("@android foo bar", null, null));
    }

    public void testIsValidBoolean() throws Exception {
        AttributeInfo info = new AttributeInfo("test", Format.BOOLEAN_SET);
        assertTrue(info.isValid("true", null, null));
        assertTrue(info.isValid("false", null, null));
        assertFalse(info.isValid("", null, null));
        assertFalse(info.isValid("TRUE", null, null));
    }

    public void testIsValidInteger() throws Exception {
        AttributeInfo info = new AttributeInfo("test", Format.INTEGER_SET);
        assertTrue(info.isValid("0", null, null));
        assertTrue(info.isValid("1", null, null));
        assertTrue(info.isValid("10", null, null));
        assertTrue(info.isValid("-10", null, null));
        assertTrue(info.isValid(Integer.toString(Integer.MAX_VALUE), null, null));

        assertFalse(info.isValid("", null, null));
        assertFalse(info.isValid("a", null, null));
        assertFalse(info.isValid("a1", null, null));
        assertFalse(info.isValid("1a", null, null));
        assertFalse(info.isValid("1.0", null, null));
    }

    public void testIsValidFloat() throws Exception {
        AttributeInfo info = new AttributeInfo("test", Format.FLOAT_SET);
        assertTrue(info.isValid("0", null, null));
        assertTrue(info.isValid("1", null, null));
        assertTrue(info.isValid("10", null, null));
        assertTrue(info.isValid("-10", null, null));
        assertTrue(info.isValid("-10.1234", null, null));
        assertTrue(info.isValid(".1", null, null));
        assertTrue(info.isValid("-.1", null, null));
        assertTrue(info.isValid("1.5e22", null, null));
        assertTrue(info.isValid(Integer.toString(Integer.MAX_VALUE), null, null));

        assertFalse(info.isValid("", null, null));
        assertFalse(info.isValid(".", null, null));
        assertFalse(info.isValid("-.", null, null));
        assertFalse(info.isValid("a", null, null));
        assertFalse(info.isValid("a1", null, null));
        assertFalse(info.isValid("1a", null, null));
    }

    public void testIsValidDimension() throws Exception {
        AttributeInfo info = new AttributeInfo("test", Format.DIMENSION_SET);
        assertTrue(info.isValid("0dp", null, null));
        assertTrue(info.isValid("1dp", null, null));
        assertTrue(info.isValid("10dip", null, null));
        assertTrue(info.isValid("-10px", null, null));
        assertTrue(info.isValid("-10.1234mm", null, null));
        assertTrue(info.isValid("14sp", null, null));
        assertTrue(info.isValid("72pt", null, null));

        assertFalse(info.isValid("", null, null));
        assertFalse(info.isValid("5", null, null));
        assertFalse(info.isValid("50ps", null, null));
        // Since we allow resources even when not specified in format, don't assert
        // this:
        //assertFalse(info.isValid("@dimen/foo"));
    }

    public void testIsValidColor() throws Exception {
        AttributeInfo info = new AttributeInfo("test", Format.COLOR_SET);
        assertTrue(info.isValid("#fff", null, null));
        assertTrue(info.isValid("#ffffff", null, null));
        assertTrue(info.isValid("#12345678", null, null));
        assertTrue(info.isValid("#abcdef00", null, null));

        assertFalse(info.isValid("", null, null));
        assertFalse(info.isValid("#fffffffff", null, null));
        assertFalse(info.isValid("red", null, null));
        assertFalse(info.isValid("rgb(1,2,3)", null, null));
    }

    public void testIsValidFraction() throws Exception {
        AttributeInfo info = new AttributeInfo("test", EnumSet.<Format>of(Format.FRACTION));
        assertTrue(info.isValid("5%", null, null));
        assertTrue(info.isValid("25%p", null, null));

        // We don't validate fractions accurately yet
        //assertFalse(info.isValid(""));
        //assertFalse(info.isValid("50%%"));
        //assertFalse(info.isValid("50"));
        //assertFalse(info.isValid("-2%"));
    }

    public void testIsValidReference() throws Exception {
        AttributeInfo info = new AttributeInfo("test", Format.REFERENCE_SET);
        assertTrue(info.isValid("@android:string/foo", null, null));
        assertTrue(info.isValid("@string/foo", null, null));
        assertTrue(info.isValid("@dimen/foo", null, null));
        assertTrue(info.isValid("@color/foo", null, null));
        assertTrue(info.isValid("@animator/foo", null, null));
        assertTrue(info.isValid("@anim/foo", null, null));
        assertTrue(info.isValid("?android:attr/textAppearanceMedium", null, null));

        assertFalse(info.isValid("", null, null));
        assertFalse(info.isValid("foo", null, null));
        assertFalse(info.isValid("3.4", null, null));
    }

    public void testIsValidEnum() throws Exception {
        AttributeInfo info = new AttributeInfo("test", Format.ENUM_SET);
        info.setEnumValues(new String[] { "wrap_content", "match_parent" });
        assertTrue(info.isValid("wrap_content", null, null));
        assertTrue(info.isValid("match_parent", null, null));
        assertFalse(info.isValid("", null, null));
        assertFalse(info.isValid("other", null, null));
        assertFalse(info.isValid("50", null, null));
    }

    public void testIsValidFlag() throws Exception {
        AttributeInfo info = new AttributeInfo("test", Format.FLAG_SET);
        info.setFlagValues(new String[] { "left", "top", "right", "bottom" });
        assertTrue(info.isValid("left", null, null));
        assertTrue(info.isValid("top", null, null));
        assertTrue(info.isValid("left|top", null, null));

        assertFalse(info.isValid("", null, null));
        assertFalse(info.isValid("other", null, null));
        assertFalse(info.isValid("50", null, null));
    }

    public void testCombined1() throws Exception {
        AttributeInfo info = new AttributeInfo("test", EnumSet.<Format>of(Format.INTEGER,
                Format.REFERENCE));
        assertTrue(info.isValid("1", null, null));
        assertTrue(info.isValid("@dimen/foo", null, null));
        assertFalse(info.isValid("foo", null, null));
    }

    public void testCombined2() throws Exception {
        AttributeInfo info = new AttributeInfo("test", EnumSet.<Format>of(Format.COLOR,
                Format.REFERENCE));
        assertTrue(info.isValid("#ff00ff00", null, null));
        assertTrue(info.isValid("@color/foo", null, null));
        assertFalse(info.isValid("foo", null, null));
    }

    public void testCombined3() throws Exception {
        AttributeInfo info = new AttributeInfo("test", EnumSet.<Format>of(Format.STRING,
                Format.REFERENCE));
        assertTrue(info.isValid("test", null, null));
        assertTrue(info.isValid("@color/foo", null, null));
    }

    public void testCombined4() throws Exception {
        AttributeInfo info = new AttributeInfo("test", EnumSet.<Format>of(Format.ENUM,
                Format.DIMENSION));
        info.setEnumValues(new String[] { "wrap_content", "match_parent" });
        assertTrue(info.isValid("wrap_content", null, null));
        assertTrue(info.isValid("match_parent", null, null));
        assertTrue(info.isValid("50dp", null, null));
        assertFalse(info.isValid("50", null, null));
        assertFalse(info.isValid("test", null, null));
    }

    public void testResourcesExist() throws Exception {
        AttributeInfo info = new AttributeInfo("test", Format.REFERENCE_SET);
        TestResourceRepository projectResources = new TestResourceRepository(false);
        projectResources.addResource(ResourceType.STRING, "mystring");
        projectResources.addResource(ResourceType.DIMEN, "mydimen");
        TestResourceRepository frameworkResources = new TestResourceRepository(true);
        frameworkResources.addResource(ResourceType.LAYOUT, "mylayout");

        assertTrue(info.isValid("@string/mystring", null, null));
        assertTrue(info.isValid("@dimen/mydimen", null, null));
        assertTrue(info.isValid("@android:layout/mylayout", null, null));
        assertTrue(info.isValid("?android:attr/listPreferredItemHeigh", null, null));

        assertTrue(info.isValid("@string/mystring", projectResources, frameworkResources));
        assertTrue(info.isValid("@dimen/mydimen", projectResources, frameworkResources));
        assertTrue(info.isValid("@android:layout/mylayout", projectResources, frameworkResources));

        assertFalse(info.isValid("@android:string/mystring", projectResources,
                frameworkResources));
        assertFalse(info.isValid("@android:dimen/mydimen", projectResources, frameworkResources));
        assertFalse(info.isValid("@layout/mylayout", projectResources, frameworkResources));
        assertFalse(info.isValid("@layout/foo", projectResources, frameworkResources));
        assertFalse(info.isValid("@anim/foo", projectResources, frameworkResources));
        assertFalse(info.isValid("@android:anim/foo", projectResources, frameworkResources));
    }

    private class TestResourceRepository extends ResourceRepository {
        private Multimap<ResourceType, String> mResources = ArrayListMultimap.create();

        protected TestResourceRepository(boolean isFrameworkRepository) {
            super(isFrameworkRepository);
        }

        void addResource(ResourceType type, String name) {
            mResources.put(type, name);
        }

        @Override
        @NonNull
        protected ResourceItem createResourceItem(@NonNull String name) {
            fail("Not used in test");
            return null;
        }

        @Override
        public boolean hasResourceItem(@NonNull ResourceType type, @NonNull String name) {
            Collection<String> names = mResources.get(type);
            if (names != null) {
                return names.contains(name);
            }

            return false;
        }
    };


    public void testIsValid() throws Exception {
        // This test loads the full attrs.xml file and then processes a bunch of platform
        // resource file and makes sure that they're all considered valid. This helps
        // make sure that isValid() closely matches what aapt accepts.
        String sdkPath = System.getenv("ADT_SDK_SOURCE_PATH");
        assertNotNull("This test requires ADT_SDK_SOURCE_PATH to be set to point to the" +
                "SDK git repository", sdkPath);
        File sdk = new File(sdkPath);
        assertNotNull("$ADT_SDK_SOURCE_PATH (" + sdk.getPath() + ") is not a directory",
                sdk.isDirectory());
        File git = sdk.getParentFile();
        File attrsPath = new File(git, "frameworks" + File.separator + "base"
                + File.separator + "core" + File.separator + "res" + File.separator + "res"
                + File.separator + "values" + File.separator + "attrs.xml");
        assertTrue(attrsPath.getPath(), attrsPath.exists());
        AttrsXmlParser parser = new AttrsXmlParser(attrsPath.getPath(),
                new StdLogger(StdLogger.Level.VERBOSE), 1100);
        parser.preload();
        Map<String, AttributeInfo> attributeMap = parser.getAttributeMap();
        assertNotNull(attributeMap);
        assertNotNull(attributeMap.get("layout_width"));
        Set<String> seen = Sets.newHashSet();

        checkDir(new File(git, "packages" + File.separator + "apps"), false, attributeMap, seen);
    }

    private void checkDir(File dir, boolean isResourceDir,
            Map<String, AttributeInfo> map, Set<String> seen) throws IOException {
        assertTrue(dir.isDirectory());
        File[] list = dir.listFiles();
        if (list != null) {
            for (File file : list) {
                if (isResourceDir && file.isFile() && file.getPath().endsWith(DOT_XML)) {
                    checkXmlFile(file, map, seen);
                } else if (file.isDirectory()) {
                    checkDir(file, isResourceDir || file.getName().equals("res"), map, seen);
                }
            }
        }
    }

    private void checkXmlFile(File file, Map<String, AttributeInfo> map,
            Set<String> seen) throws IOException {
        String xml = Files.toString(file, Charsets.UTF_8);
        if (xml != null) {
            //Document doc = DomUtilities.parseStructuredDocument(xml);
            Document doc = DomUtilities.parseDocument(xml, false);
            if (doc != null && doc.getDocumentElement() != null) {
                checkElement(file, doc.getDocumentElement(), map, seen);
            }
        }
    }

    private void checkElement(File file, Element element, Map<String, AttributeInfo> map,
            Set<String> seen) {
        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0, n = attributes.getLength(); i < n; i++) {
            Attr attribute = (Attr) attributes.item(i);

            String uri = attribute.getNamespaceURI();
            String name = attribute.getLocalName();
            String value = attribute.getValue();
            if (ANDROID_URI.equals(uri)) {
                AttributeInfo info = map.get(name);
                if (info == null) {
                    System.out.println("Warning: Unknown attribute '" + name + "' in " + file);
                            return;
                }
                if (!info.isValid(value, null, null)) {
                    if (name.equals("duration") || name.equals("exitFadeDuration")) {
                        // Already known
                        return;
                    }
                    String message = "In file " +
                            file.getPath() + ":\nCould not validate value \"" + value
                            + "\" for attribute '"
                            + name + "' where the attribute info has formats " + info.getFormats()
                            + "\n";
                    System.out.println("\n" + message);
                    fail(message);
                }
                if ((value.startsWith("@") || value.startsWith("?")) &&
                        !info.getFormats().contains(Format.REFERENCE)) {
                    // Print out errors in attrs.xml

                    if (!seen.contains(name)) {
                        seen.add(name);
                        System.out.println("\"" + name + "\" with formats " + info.getFormats()
                                + " was passed a reference (" + value + ") in file " + file);
                    }
                }
            }
        }
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttrsXmlParserManifestTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttrsXmlParserManifestTest.java
//Synthetic comment -- index cddd63e..82d6aca 100755

//Synthetic comment -- @@ -35,8 +35,8 @@

@Override
public void setUp() throws Exception {
        mFilePath = AdtTestData.getInstance().getTestFilePath(MOCK_DATA_PATH);
        mParser = new AttrsXmlParser(mFilePath, new TestLogger(), 100);
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttrsXmlParserTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttrsXmlParserTest.java
//Synthetic comment -- index e57e5cd..883577b 100644

//Synthetic comment -- @@ -36,7 +36,7 @@
@Override
public void setUp() throws Exception {
mFilePath = AdtTestData.getInstance().getTestFilePath(MOCK_DATA_PATH);
        mParser = new AttrsXmlParser(mFilePath, new TestLogger(), 100);
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/manager/IdeScanningContextTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/manager/IdeScanningContextTest.java
new file mode 100644
//Synthetic comment -- index 0000000..3104c85

//Synthetic comment -- @@ -0,0 +1,22 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
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
package com.android.ide.eclipse.adt.internal.resources.manager;

import junit.framework.TestCase;

@SuppressWarnings("javadoc")
public class IdeScanningContextTest extends TestCase {
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/sdk/LayoutParamsParserTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/sdk/LayoutParamsParserTest.java
//Synthetic comment -- index 9d87e2f..c89dd06 100644

//Synthetic comment -- @@ -62,7 +62,7 @@
super(new MockFrameworkClassLoader(),
new AttrsXmlParser(
AdtTestData.getInstance().getTestFilePath(MOCK_DATA_PATH),
                          new TestLogger(), 100).preload());

mTopViewClass = new ClassWrapper(mock_android.view.View.class);
mTopGroupClass = new ClassWrapper(mock_android.view.ViewGroup.class);








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/IdResourceParser.java b/sdk_common/src/com/android/ide/common/resources/IdResourceParser.java
//Synthetic comment -- index 1de664e..66a72ce 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.common.resources;

import com.android.annotations.NonNull;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.resources.ValueResourceParser.IValueResourceRepository;
import com.android.resources.ResourceType;
//Synthetic comment -- @@ -48,7 +49,9 @@
*            as a place to stash errors encountered
* @param isFramework true if scanning a framework resource
*/
    public IdResourceParser(
            @NonNull IValueResourceRepository repository,
            @NonNull ScanningContext context,
boolean isFramework) {
mRepository = repository;
mContext = context;
//Synthetic comment -- @@ -107,7 +110,6 @@
private boolean parse(ResourceType type, String path, KXmlParser parser)
throws XmlPullParserException, IOException {
boolean valid = true;
boolean checkForErrors = !mIsFramework && !mContext.needsFullAapt();

while (true) {
//Synthetic comment -- @@ -118,33 +120,22 @@
String value = parser.getAttributeValue(i);
assert value != null : attribute;

                    if (checkForErrors) {
                        String uri = parser.getAttributeNamespace(i);
                        if (!mContext.checkValue(uri, attribute, value)) {
                            mContext.requestFullAapt();
                            checkForErrors = false;
                            valid = false;
}
}

                    if (value.startsWith("@+")) {       //$NON-NLS-1$
                        // Strip out the @+id/ or @+android:id/ section
                        String id = value.substring(value.indexOf('/') + 1);
                        ResourceValue newId = new ResourceValue(ResourceType.ID, id,
                                mIsFramework);
                        mRepository.addResourceValue(newId);
                    }
}
} else if (event == XmlPullParser.END_DOCUMENT) {
break;








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/ResourceRepository.java b/sdk_common/src/com/android/ide/common/resources/ResourceRepository.java
//Synthetic comment -- index ac0614d..4f50f63 100755

//Synthetic comment -- @@ -171,7 +171,7 @@
* @return true if the resource is known
*/
public boolean hasResourceItem(@NonNull String url) {
        assert url.startsWith("@") || url.startsWith("?") : url;

int typeEnd = url.indexOf('/', 1);
if (typeEnd != -1) {








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/ScanningContext.java b/sdk_common/src/com/android/ide/common/resources/ScanningContext.java
//Synthetic comment -- index e4ed275..43561e8 100644

//Synthetic comment -- @@ -15,6 +15,9 @@
*/
package com.android.ide.common.resources;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

//Synthetic comment -- @@ -24,7 +27,7 @@
* so on.
*/
public class ScanningContext {
    protected final ResourceRepository mRepository;
private boolean mNeedsFullAapt;
private List<String> mErrors = null;

//Synthetic comment -- @@ -33,7 +36,7 @@
*
* @param repository the associated resource repository
*/
    public ScanningContext(@NonNull ResourceRepository repository) {
super();
mRepository = repository;
}
//Synthetic comment -- @@ -43,6 +46,7 @@
*
* @return a list of errors encountered during scanning (or null)
*/
    @Nullable
public List<String> getErrors() {
return mErrors;
}
//Synthetic comment -- @@ -55,7 +59,7 @@
* @param error the error message, including file name and line number at
*            the beginning
*/
    public void addError(@NonNull String error) {
if (mErrors == null) {
mErrors = new ArrayList<String>();
}
//Synthetic comment -- @@ -67,6 +71,7 @@
*
* @return the associated repository, never null
*/
    @NonNull
public ResourceRepository getRepository() {
return mRepository;
}
//Synthetic comment -- @@ -89,4 +94,17 @@
public boolean needsFullAapt() {
return mNeedsFullAapt;
}

    /**
     * Asks the context to check whether the given attribute name and value is valid
     * in this context.
     *
     * @param uri the XML namespace URI
     * @param name the attribute local name
     * @param value the attribute value
     * @return true if the attribute is valid
     */
    public boolean checkValue(@Nullable String uri, @NonNull String name, @NonNull String value) {
        return true;
    }
}








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/SingleResourceFile.java b/sdk_common/src/com/android/ide/common/resources/SingleResourceFile.java
//Synthetic comment -- index 6b663e9..eb643f6 100644

//Synthetic comment -- @@ -88,6 +88,10 @@
protected void update(ScanningContext context) {
// when this happens, nothing needs to be done since the file only generates
// a single resources that doesn't actually change (its content is the file path)

        // However, we should check for newly introduced errors
        // Parse the file and look for @+id/ entries
        validateAttributes(context);
}

@Override
//Synthetic comment -- @@ -139,4 +143,26 @@

return name;
}

    /**
     * Validates the associated resource file to make sure the attribute references are valid
     *
     * @return true if parsing succeeds and false if it fails
     */
    private boolean validateAttributes(ScanningContext context) {
        // We only need to check if it's a non-framework file
        if (!isFramework()) {
            ValidatingResourceParser parser = new ValidatingResourceParser(context, false);
            try {
                IAbstractFile file = getFile();
                return parser.parse(file.getOsLocation(), file.getContents());
            } catch (Exception e) {
                context.needsFullAapt();
            }

            return false;
        }

        return true;
    }
}








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/ValidatingResourceParser.java b/sdk_common/src/com/android/ide/common/resources/ValidatingResourceParser.java
new file mode 100644
//Synthetic comment -- index 0000000..c1e45a8

//Synthetic comment -- @@ -0,0 +1,136 @@
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

package com.android.ide.common.resources;

import com.android.annotations.NonNull;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Parser for scanning an XML resource file and validating all framework
 * attribute references in it. If an error is found, the associated context
 * is marked as needing a full AAPT run.
 */
public class ValidatingResourceParser {
    private final boolean mIsFramework;
    private ScanningContext mContext;

    /**
     * Creates a new {@link ValidatingResourceParser}
     *
     * @param context a context object with state for the current update, such
     *            as a place to stash errors encountered
     * @param isFramework true if scanning a framework resource
     */
    public ValidatingResourceParser(
            @NonNull ScanningContext context,
            boolean isFramework) {
        mContext = context;
        mIsFramework = isFramework;
    }

    /**
     * Parse the given input and return false if it contains errors, <b>or</b> if
     * the context is already tagged as needing a full aapt run.
     *
     * @param path the full OS path to the file being parsed
     * @param input the input stream of the XML to be parsed
     * @return true if parsing succeeds and false if it fails
     * @throws IOException if reading the contents fails
     */
    public boolean parse(final String path, InputStream input)
            throws IOException {
        // No need to validate framework files
        if (mIsFramework) {
            return true;
        }
        if (mContext.needsFullAapt()) {
            return false;
        }

        KXmlParser parser = new KXmlParser();
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);

            if (input instanceof FileInputStream) {
                input = new BufferedInputStream(input);
            }
            parser.setInput(input, "UTF-8"); //$NON-NLS-1$

            return parse(path, parser);
        } catch (XmlPullParserException e) {
            String message = e.getMessage();

            // Strip off position description
            int index = message.indexOf("(position:"); //$NON-NLS-1$ (Hardcoded in KXml)
            if (index != -1) {
                message = message.substring(0, index);
            }

            String error = String.format("%1$s:%2$d: Error: %3$s", //$NON-NLS-1$
                    path, parser.getLineNumber(), message);
            mContext.addError(error);
            return false;
        } catch (RuntimeException e) {
            // Some exceptions are thrown by the KXmlParser that are not XmlPullParserExceptions,
            // such as this one:
            //    java.lang.RuntimeException: Undefined Prefix: w in org.kxml2.io.KXmlParser@...
            //        at org.kxml2.io.KXmlParser.adjustNsp(Unknown Source)
            //        at org.kxml2.io.KXmlParser.parseStartTag(Unknown Source)
            String message = e.getMessage();
            String error = String.format("%1$s:%2$d: Error: %3$s", //$NON-NLS-1$
                    path, parser.getLineNumber(), message);
            mContext.addError(error);
            return false;
        }
    }

    private boolean parse(String path, KXmlParser parser)
            throws XmlPullParserException, IOException {
        boolean checkForErrors = !mIsFramework && !mContext.needsFullAapt();

        while (true) {
            int event = parser.next();
            if (event == XmlPullParser.START_TAG) {
                for (int i = 0, n = parser.getAttributeCount(); i < n; i++) {
                    String attribute = parser.getAttributeName(i);
                    String value = parser.getAttributeValue(i);
                    assert value != null : attribute;

                    if (checkForErrors) {
                        String uri = parser.getAttributeNamespace(i);
                        if (!mContext.checkValue(uri, attribute, value)) {
                            mContext.requestFullAapt();
                            return false;
                        }
                    }
                }
            } else if (event == XmlPullParser.END_DOCUMENT) {
                break;
            }
        }

        return true;
    }
}







