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

// Filenames and folder names
public static final String ANDROID_MANIFEST_XML = "AndroidManifest.xml"; //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttributeInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttributeInfo.java
//Synthetic comment -- index 05f0adf..be928b0 100755

//Synthetic comment -- @@ -16,10 +16,31 @@

package com.android.ide.common.resources.platform;

import com.android.annotations.NonNull;
import com.android.ide.common.api.IAttributeInfo;

import java.util.EnumSet;


/**
//Synthetic comment -- @@ -160,4 +181,161 @@
public @NonNull String getDefinedBy() {
return mDefinedBy;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttrsXmlParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttrsXmlParser.java
//Synthetic comment -- index 3c1fa97..1330c50 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.ide.common.resources.platform.ViewClassInfo.LayoutParamsInfo;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.utils.ILogger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
//Synthetic comment -- @@ -54,7 +55,7 @@

// all attributes that have the same name are supposed to have the same
// parameters so we'll keep a cache of them to avoid processing them twice.
    private HashMap<String, AttributeInfo> mAttributeMap;

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
*/
    public AttrsXmlParser(String osAttrsXmlPath, ILogger log) {
        this(osAttrsXmlPath, null /* inheritableAttributes */, log);
}

/**
//Synthetic comment -- @@ -103,24 +113,26 @@
*              If not null, the parser must have had its {@link #preload()} method
*              invoked prior to being used here.
* @param log A logger object. Must not be null.
*/
public AttrsXmlParser(
String osAttrsXmlPath,
AttrsXmlParser inheritableAttributes,
            ILogger log) {
mOsAttrsXmlPath = osAttrsXmlPath;
mLog = log;

assert osAttrsXmlPath != null;
assert log != null;

if (inheritableAttributes == null) {
            mAttributeMap = new HashMap<String, AttributeInfo>();
mEnumFlagValues = new HashMap<String, Map<String,Integer>>();
} else {
            mAttributeMap = new HashMap<String, AttributeInfo>(inheritableAttributes.mAttributeMap);
mEnumFlagValues = new HashMap<String, Map<String,Integer>>(
                                                             inheritableAttributes.mEnumFlagValues);
}

// Pre-compute the set of format names such that we don't have to compute the uppercase








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index 51ae496..1507a8d 100644

//Synthetic comment -- @@ -325,7 +325,7 @@

if (ResourceManager.isAutoBuilding()) {
IdeScanningContext context = new IdeScanningContext(projectResources,
                                project);

resManager.processDelta(delta, context);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java
//Synthetic comment -- index 2ea70fc..b39c4cb 100644

//Synthetic comment -- @@ -19,6 +19,9 @@
import static com.android.SdkConstants.ATTR_LAYOUT_RESOURCE_PREFIX;
import static com.android.SdkConstants.PREFIX_RESOURCE_REF;
import static com.android.SdkConstants.UNIT_DP;
import static com.android.SdkConstants.UNIT_PX;
import static com.android.SdkConstants.UNIT_SP;
import static com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor.ATTRIBUTE_ICON_FILENAME;
//Synthetic comment -- @@ -1149,13 +1152,13 @@
"<b>Scale-independent Pixels</b> - this is like the dp unit, but it is also scaled by "
+ "the user's font size preference.",

        "pt", //$NON-NLS-1$
"<b>Points</b> - 1/72 of an inch based on the physical size of the screen.",

        "mm", //$NON-NLS-1$
"<b>Millimeters</b> - based on the physical size of the screen.",

        "in", //$NON-NLS-1$
"<b>Inches</b> - based on the physical size of the screen.",

UNIT_PX,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java
//Synthetic comment -- index de09d00..9553bc8 100644

//Synthetic comment -- @@ -22,6 +22,13 @@
import static com.android.SdkConstants.ATTR_LAYOUT_WIDTH;
import static com.android.SdkConstants.ATTR_PADDING;
import static com.android.SdkConstants.AUTO_URI;
import static com.android.SdkConstants.VALUE_FILL_PARENT;
import static com.android.SdkConstants.VALUE_MATCH_PARENT;
import static com.android.SdkConstants.VIEW_FRAGMENT;
//Synthetic comment -- @@ -78,7 +85,7 @@
* Number of pixels to pad views with in exploded-rendering mode.
*/
private static final String DEFAULT_PADDING_VALUE =
        ExplodedRenderingHelper.PADDING_VALUE + "px"; //$NON-NLS-1$

/**
* Number of pixels to pad exploded individual views with. (This is HALF the width of the
//Synthetic comment -- @@ -542,13 +549,13 @@
private static final int COMPLEX_UNIT_MM = 5;

private final static DimensionEntry[] sDimensions = new DimensionEntry[] {
        new DimensionEntry("px", COMPLEX_UNIT_PX),
        new DimensionEntry("dip", COMPLEX_UNIT_DIP),
        new DimensionEntry("dp", COMPLEX_UNIT_DIP),
        new DimensionEntry("sp", COMPLEX_UNIT_SP),
        new DimensionEntry("pt", COMPLEX_UNIT_PT),
        new DimensionEntry("in", COMPLEX_UNIT_IN),
        new DimensionEntry("mm", COMPLEX_UNIT_MM),
};

/**
//Synthetic comment -- @@ -564,7 +571,7 @@
padding += sIntOut[0];
}

        return padding + "px"; //$NON-NLS-1$
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/CustomViewDescriptorService.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/CustomViewDescriptorService.java
//Synthetic comment -- index ca229a4..6df6929 100644

//Synthetic comment -- @@ -265,7 +265,7 @@
if (parser == null) {
parser = new AttrsXmlParser(
file.getFile().getOsLocation(),
                    AdtPlugin.getDefault());
parser.preload();
mParserCache.put(file, parser);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/ConvertToDpFix.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/ConvertToDpFix.java
//Synthetic comment -- index 0ca6aa2..44b676f 100644

//Synthetic comment -- @@ -15,6 +15,7 @@
*/
package com.android.ide.eclipse.adt.internal.lint;

import static com.android.SdkConstants.VALUE_N_DP;

import com.android.ide.eclipse.adt.AdtPlugin;
//Synthetic comment -- @@ -63,7 +64,7 @@
for (int i = 0, n = attributes.getLength(); i < n; i++) {
Attr attribute = (Attr) attributes.item(i);
String value = attribute.getValue();
                if (value.endsWith("px")) {
Matcher matcher = pattern.matcher(value);
if (matcher.matches()) {
String numberString = matcher.group(1);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/IdeScanningContext.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/IdeScanningContext.java
//Synthetic comment -- index 5a4fc75..d613249 100644

//Synthetic comment -- @@ -15,15 +15,20 @@
*/
package com.android.ide.eclipse.adt.internal.resources.manager;

import static com.android.ide.eclipse.adt.AdtConstants.MARKER_AAPT_COMPILE;

import static org.eclipse.core.resources.IResource.DEPTH_ONE;
import static org.eclipse.core.resources.IResource.DEPTH_ZERO;

import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.common.resources.ScanningContext;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.build.AaptParser;
import com.android.utils.Pair;

import org.eclipse.core.resources.IFolder;
//Synthetic comment -- @@ -36,6 +41,7 @@
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
//Synthetic comment -- @@ -50,20 +56,36 @@
private IResource mCurrentFile;
private List<Pair<IResource, String>> mErrors;
private Set<IProject> mFullAaptProjects;

/**
* Constructs a new {@link IdeScanningContext}
*
* @param repository the associated {@link ResourceRepository}
* @param project the associated project
*/
    public IdeScanningContext(ResourceRepository repository, IProject project) {
super(repository);
mProject = project;
}

@Override
    public void addError(String error) {
super.addError(error);

if (mErrors == null) {
//Synthetic comment -- @@ -77,7 +99,7 @@
*
* @param resource the resource about to be scanned
*/
    public void startScanning(IResource resource) {
assert mCurrentFile == null : mCurrentFile;
mCurrentFile = resource;
mScannedResources.add(resource);
//Synthetic comment -- @@ -88,7 +110,7 @@
*
* @param resource the resource that was scanned
*/
    public void finishScanning(IResource resource) {
assert mCurrentFile != null;
mCurrentFile = null;
}
//Synthetic comment -- @@ -193,4 +215,20 @@
public Collection<IProject> getAaptRequestedProjects() {
return mFullAaptProjects;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceManager.java
//Synthetic comment -- index b346dcd..e1a12d7 100644

//Synthetic comment -- @@ -423,7 +423,7 @@
if (delta.getResource() instanceof IProject) {
IProject project = (IProject) delta.getResource();
IdeScanningContext context =
                            new IdeScanningContext(getProjectResources(project), project);

processDelta(delta, context);

//Synthetic comment -- @@ -522,7 +522,7 @@
mMap.put(project, projectResources);
}
}
            IdeScanningContext context = new IdeScanningContext(projectResources, project);

if (resourceFolder != null && resourceFolder.exists()) {
try {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java
//Synthetic comment -- index 59a1236..85ae9fd 100644

//Synthetic comment -- @@ -16,9 +16,12 @@

package com.android.ide.eclipse.adt.internal.sdk;

import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.animator.AnimDescriptors;
//Synthetic comment -- @@ -87,6 +90,7 @@

private ResourceRepository mFrameworkResources;
private LayoutLibrary mLayoutLibrary;

private boolean mLayoutBridgeInit = false;

//Synthetic comment -- @@ -95,6 +99,27 @@
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
                    AdtPlugin.getDefault());
attrsXmlParser.preload();
progress.worked(1);

progress.subTask("Manifest definitions");
AttrsXmlParser attrsManifestXmlParser = new AttrsXmlParser(
mAndroidTarget.getPath(IAndroidTarget.MANIFEST_ATTRIBUTES),
attrsXmlParser,
                    AdtPlugin.getDefault());
attrsManifestXmlParser.preload();
progress.worked(1);

//Synthetic comment -- @@ -290,6 +292,8 @@
frameworkResources,
layoutBridge);

Sdk.getCurrent().setTargetData(mAndroidTarget, targetData);

return Status.OK_STATUS;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index b1b057b..1a299d9 100644

//Synthetic comment -- @@ -16,9 +16,9 @@

package com.android.ide.eclipse.adt.internal.sdk;

import static com.android.SdkConstants.FD_RES;
import static com.android.SdkConstants.DOT_XML;
import static com.android.SdkConstants.EXT_JAR;

import com.android.SdkConstants;
import com.android.annotations.NonNull;
//Synthetic comment -- @@ -215,6 +215,7 @@
* Returns the lock object used to synchronize all operations dealing with SDK, targets and
* projects.
*/
public static final Object getLock() {
return LOCK;
}
//Synthetic comment -- @@ -224,6 +225,7 @@
* <p/>If the SDK failed to load, it displays an error to the user.
* @param sdkLocation the OS path to the SDK.
*/
public static Sdk loadSdk(String sdkLocation) {
synchronized (LOCK) {
if (sCurrentSdk != null) {
//Synthetic comment -- @@ -298,6 +300,7 @@
/**
* Returns the current {@link Sdk} object.
*/
public static Sdk getCurrent() {
synchronized (LOCK) {
return sCurrentSdk;
//Synthetic comment -- @@ -336,6 +339,7 @@
*
* @return A file:// URL on the local documentation folder if it exists or null.
*/
public String getDocumentationBaseUrl() {
return mDocBaseUrl;
}
//Synthetic comment -- @@ -367,7 +371,8 @@
* @param hash the {@link IAndroidTarget} hash string.
* @return The matching {@link IAndroidTarget} or null.
*/
    public IAndroidTarget getTargetFromHashString(String hash) {
return mManager.getTargetFromHashString(hash);
}

//Synthetic comment -- @@ -377,9 +382,9 @@
* @param project the project to initialize
* @param target the project's target.
* @throws IOException if creating the file failed in any way.
     * @throws StreamException
*/
    public void initProject(IProject project, IAndroidTarget target)
throws IOException, StreamException {
if (project == null || target == null) {
return;
//Synthetic comment -- @@ -423,6 +428,7 @@
* @param project the request project
* @return the ProjectState for the project.
*/
@SuppressWarnings("deprecation")
public static ProjectState getProjectState(IProject project) {
if (project == null) {
//Synthetic comment -- @@ -492,6 +498,7 @@
/**
* Returns the {@link IAndroidTarget} object associated with the given {@link IProject}.
*/
public IAndroidTarget getTarget(IProject project) {
if (project == null) {
return null;
//Synthetic comment -- @@ -512,6 +519,7 @@
* @param state the state representing the project to load.
* @return the target that was loaded.
*/
public IAndroidTarget loadTarget(ProjectState state) {
IAndroidTarget target = null;
if (state != null) {
//Synthetic comment -- @@ -537,6 +545,7 @@
* If the target is already loaded, nothing happens.
* @return The load status if the target data is already loaded.
*/
public LoadStatus checkAndLoadTargetData(final IAndroidTarget target, IJavaProject project) {
boolean loadData = false;

//Synthetic comment -- @@ -633,6 +642,7 @@
/**
* Return the {@link AndroidTargetData} for a given {@link IAndroidTarget}.
*/
public AndroidTargetData getTargetData(IAndroidTarget target) {
synchronized (LOCK) {
return mTargetDataMap.get(target);
//Synthetic comment -- @@ -642,6 +652,7 @@
/**
* Return the {@link AndroidTargetData} for a given {@link IProject}.
*/
public AndroidTargetData getTargetData(IProject project) {
synchronized (LOCK) {
IAndroidTarget target = getTarget(project);
//Synthetic comment -- @@ -665,11 +676,13 @@
* Returns the {@link AvdManager}. If the AvdManager failed to parse the AVD folder, this could
* be <code>null</code>.
*/
public AvdManager getAvdManager() {
return mAvdManager;
}

    public static AndroidVersion getDeviceVersion(IDevice device) {
try {
Map<String, String> props = device.getProperties();
String apiLevel = props.get(IDevice.PROP_BUILD_API_LEVEL);
//Synthetic comment -- @@ -684,11 +697,13 @@
}
}

public DeviceManager getDeviceManager() {
return mDeviceManager;
}

/** Returns the devices provided by the SDK, including user created devices */
public List<Device> getDevices() {
return mDeviceManager.getDevices(getSdkLocation());
}
//Synthetic comment -- @@ -699,10 +714,11 @@
* @param project the library project.
* @return a possibly empty list of ProjectState.
*/
public static Set<ProjectState> getMainProjectsFor(IProject project) {
synchronized (LOCK) {
// first get the project directly depending on this.
            HashSet<ProjectState> list = new HashSet<ProjectState>();

// loop on all project and see if ProjectState.getLibrary returns a non null
// project.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttributeInfoTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttributeInfoTest.java
new file mode 100644
//Synthetic comment -- index 0000000..7ae96a8

//Synthetic comment -- @@ -0,0 +1,371 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttrsXmlParserManifestTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttrsXmlParserManifestTest.java
//Synthetic comment -- index cddd63e..82d6aca 100755

//Synthetic comment -- @@ -35,8 +35,8 @@

@Override
public void setUp() throws Exception {
        mFilePath = AdtTestData.getInstance().getTestFilePath(MOCK_DATA_PATH); //$NON-NLS-1$
        mParser = new AttrsXmlParser(mFilePath, new TestLogger());
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttrsXmlParserTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttrsXmlParserTest.java
//Synthetic comment -- index e57e5cd..883577b 100644

//Synthetic comment -- @@ -36,7 +36,7 @@
@Override
public void setUp() throws Exception {
mFilePath = AdtTestData.getInstance().getTestFilePath(MOCK_DATA_PATH);
        mParser = new AttrsXmlParser(mFilePath, new TestLogger());
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/manager/IdeScanningContextTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/manager/IdeScanningContextTest.java
new file mode 100644
//Synthetic comment -- index 0000000..3104c85

//Synthetic comment -- @@ -0,0 +1,22 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/sdk/LayoutParamsParserTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/sdk/LayoutParamsParserTest.java
//Synthetic comment -- index 9d87e2f..c89dd06 100644

//Synthetic comment -- @@ -62,7 +62,7 @@
super(new MockFrameworkClassLoader(),
new AttrsXmlParser(
AdtTestData.getInstance().getTestFilePath(MOCK_DATA_PATH),
                          new TestLogger()).preload());

mTopViewClass = new ClassWrapper(mock_android.view.View.class);
mTopGroupClass = new ClassWrapper(mock_android.view.ViewGroup.class);








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/IdResourceParser.java b/sdk_common/src/com/android/ide/common/resources/IdResourceParser.java
//Synthetic comment -- index 1de664e..66a72ce 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.common.resources;

import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.resources.ValueResourceParser.IValueResourceRepository;
import com.android.resources.ResourceType;
//Synthetic comment -- @@ -48,7 +49,9 @@
*            as a place to stash errors encountered
* @param isFramework true if scanning a framework resource
*/
    public IdResourceParser(IValueResourceRepository repository, ScanningContext context,
boolean isFramework) {
mRepository = repository;
mContext = context;
//Synthetic comment -- @@ -107,7 +110,6 @@
private boolean parse(ResourceType type, String path, KXmlParser parser)
throws XmlPullParserException, IOException {
boolean valid = true;
        ResourceRepository resources = mContext.getRepository();
boolean checkForErrors = !mIsFramework && !mContext.needsFullAapt();

while (true) {
//Synthetic comment -- @@ -118,33 +120,22 @@
String value = parser.getAttributeValue(i);
assert value != null : attribute;

                    if (value.startsWith("@")) {       //$NON-NLS-1$
                        // Gather IDs
                        if (value.startsWith("@+")) {  //$NON-NLS-1$
                            // Strip out the @+id/ or @+android:id/ section
                            String id = value.substring(value.indexOf('/') + 1);
                            ResourceValue newId = new ResourceValue(ResourceType.ID, id,
                                    mIsFramework);
                            mRepository.addResourceValue(newId);
                        } else if (checkForErrors){
                            // Validate resource references (unless we're scanning a framework
                            // resource or if we've already scheduled a full aapt run)
                            boolean exists = resources.hasResourceItem(value);
                            if (!exists && !mRepository.hasResourceValue(ResourceType.ID,
                                    value.substring(value.indexOf('/') + 1))) {
                                String error = String.format(
                                    // Don't localize because the exact pattern matches AAPT's
                                    // output which has hardcoded regexp matching in
                                    // AaptParser.
                                    "%1$s:%2$d: Error: No resource found that matches " + //$NON-NLS-1$
                                    "the given name (at '%3$s' with value '%4$s')",       //$NON-NLS-1$
                                            path, parser.getLineNumber(),
                                            attribute, value);
                                mContext.addError(error);
                                valid = false;
                            }
}
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
        assert url.startsWith("@") : url;

int typeEnd = url.indexOf('/', 1);
if (typeEnd != -1) {








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/ScanningContext.java b/sdk_common/src/com/android/ide/common/resources/ScanningContext.java
//Synthetic comment -- index e4ed275..43561e8 100644

//Synthetic comment -- @@ -15,6 +15,9 @@
*/
package com.android.ide.common.resources;

import java.util.ArrayList;
import java.util.List;

//Synthetic comment -- @@ -24,7 +27,7 @@
* so on.
*/
public class ScanningContext {
    private final ResourceRepository mRepository;
private boolean mNeedsFullAapt;
private List<String> mErrors = null;

//Synthetic comment -- @@ -33,7 +36,7 @@
*
* @param repository the associated resource repository
*/
    public ScanningContext(ResourceRepository repository) {
super();
mRepository = repository;
}
//Synthetic comment -- @@ -43,6 +46,7 @@
*
* @return a list of errors encountered during scanning (or null)
*/
public List<String> getErrors() {
return mErrors;
}
//Synthetic comment -- @@ -55,7 +59,7 @@
* @param error the error message, including file name and line number at
*            the beginning
*/
    public void addError(String error) {
if (mErrors == null) {
mErrors = new ArrayList<String>();
}
//Synthetic comment -- @@ -67,6 +71,7 @@
*
* @return the associated repository, never null
*/
public ResourceRepository getRepository() {
return mRepository;
}
//Synthetic comment -- @@ -89,4 +94,17 @@
public boolean needsFullAapt() {
return mNeedsFullAapt;
}
}








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/SingleResourceFile.java b/sdk_common/src/com/android/ide/common/resources/SingleResourceFile.java
//Synthetic comment -- index 6b663e9..eb643f6 100644

//Synthetic comment -- @@ -88,6 +88,10 @@
protected void update(ScanningContext context) {
// when this happens, nothing needs to be done since the file only generates
// a single resources that doesn't actually change (its content is the file path)
}

@Override
//Synthetic comment -- @@ -139,4 +143,26 @@

return name;
}
}








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/ValidatingResourceParser.java b/sdk_common/src/com/android/ide/common/resources/ValidatingResourceParser.java
new file mode 100644
//Synthetic comment -- index 0000000..c1e45a8

//Synthetic comment -- @@ -0,0 +1,136 @@







