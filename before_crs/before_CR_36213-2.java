/*SDK Manager change format of revision property.

Instead of storing the full revision as 4 separate
properties, this stores it using the legacy
"Pkg.Revision" property with the "1.2.3 rc4"
format. It reduces the number of properties to
maintain, it's more human-readable and it's
easier when we want to have min-tools-rev be
a full revision in the next CL (otherwise it
means 4 more properties, etc.)

Change-Id:I94ebc4786e83c5b565a6d3a33d11efdfda6f72c5*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/VersionCheck.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/VersionCheck.java
//Synthetic comment -- index 0dde05f..594912b 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.ide.eclipse.adt.AdtPlugin.CheckSdkErrorHandler.Solution;
import com.android.ide.eclipse.adt.Messages;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.repository.PkgProps;

import org.osgi.framework.Constants;
//Synthetic comment -- @@ -48,7 +49,7 @@
/**
* The minimum version of the SDK Tools that this version of ADT requires.
*/
    private final static int MIN_TOOLS_REV = 17;

/**
* Pattern to get the minimum plugin version supported by the SDK. This is read from
//Synthetic comment -- @@ -57,7 +58,7 @@
private final static Pattern sPluginVersionPattern = Pattern.compile(
"^plugin.version=(\\d+)\\.(\\d+)\\.(\\d+).*$"); //$NON-NLS-1$
private final static Pattern sSourcePropPattern = Pattern.compile(
            "^" + PkgProps.PKG_MAJOR_REV + "=(\\d+).*$"); //$NON-NLS-1$

/**
* Checks the plugin and the SDK have compatible versions.
//Synthetic comment -- @@ -136,7 +137,7 @@

// now check whether the tools are new enough.
String osTools = osSdkPath + SdkConstants.OS_SDK_TOOLS_FOLDER;
        int toolsRevision = Integer.MAX_VALUE;
try {
reader = new FileReader(osTools + SdkConstants.FN_SOURCE_PROP);
BufferedReader bReader = new BufferedReader(reader);
//Synthetic comment -- @@ -144,7 +145,9 @@
while ((line = bReader.readLine()) != null) {
Matcher m = sSourcePropPattern.matcher(line);
if (m.matches()) {
                    toolsRevision = Integer.parseInt(m.group(1));
break;
}
}
//Synthetic comment -- @@ -163,7 +166,7 @@
}
}

        if (toolsRevision < MIN_TOOLS_REV) {
// this is a warning only as we need to parse the SDK to allow updating
// of the tools!
return errorHandler.handleWarning(








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java
//Synthetic comment -- index a786728..8284054 100644

//Synthetic comment -- @@ -463,7 +463,7 @@
int revision = 1;
LayoutlibVersion layoutlibVersion = null;
try {
                revision = Integer.parseInt(platformProp.get(PkgProps.PKG_MAJOR_REV));
} catch (NumberFormatException e) {
// do nothing, we'll keep the default value of 1.
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/FullRevision.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/FullRevision.java
//Synthetic comment -- index a90fa0a..40235c2 100755

//Synthetic comment -- @@ -16,6 +16,11 @@

package com.android.sdklib.internal.repository.packages;


/**
* Package multi-part revision number composed of a tuple
//Synthetic comment -- @@ -32,6 +37,10 @@
public static final int IMPLICIT_MICRO_REV = 0;
public static final int NOT_A_PREVIEW      = 0;

private final int mMajor;
private final int mMinor;
private final int mMicro;
//Synthetic comment -- @@ -73,6 +82,52 @@
}

/**
* Returns the version in a fixed format major.minor.micro
* with an optional "rc preview#". For example it would
* return "18.0.0", "18.1.0" or "18.1.2 rc5".








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/FullRevisionPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/FullRevisionPackage.java
//Synthetic comment -- index 10c4ce0..6028873 100755

//Synthetic comment -- @@ -102,12 +102,19 @@
super(source, props, revision, license, description, descUrl,
archiveOs, archiveArch, archiveOsPath);

        int major = getPropertyInt(props, PkgProps.PKG_MAJOR_REV,revision);
        int minor = getPropertyInt(props, PkgProps.PKG_MINOR_REV, FullRevision.IMPLICIT_MINOR_REV);
        int micro = getPropertyInt(props, PkgProps.PKG_MICRO_REV, FullRevision.IMPLICIT_MINOR_REV);
        int preview = getPropertyInt(props, PkgProps.PKG_PREVIEW_REV, FullRevision.NOT_A_PREVIEW);

        mPreviewVersion = new FullRevision(major, minor, micro, preview);
}

@Override
//Synthetic comment -- @@ -118,11 +125,7 @@
@Override
public void saveProperties(Properties props) {
super.saveProperties(props);

        props.setProperty(PkgProps.PKG_MAJOR_REV,   Integer.toString(mPreviewVersion.getMajor()));
        props.setProperty(PkgProps.PKG_MINOR_REV,   Integer.toString(mPreviewVersion.getMinor()));
        props.setProperty(PkgProps.PKG_MICRO_REV,   Integer.toString(mPreviewVersion.getMicro()));
        props.setProperty(PkgProps.PKG_PREVIEW_REV, Integer.toString(mPreviewVersion.getPreview()));
}

@Override








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/MajorRevision.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/MajorRevision.java
//Synthetic comment -- index 2678b1f..9ca9e22 100755

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.sdklib.internal.repository.packages;


/**
* Package revision number composed of a <em>single</em> major revision.
//Synthetic comment -- @@ -34,4 +36,21 @@
public String toString() {
return super.toShortString();
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/MajorRevisionPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/MajorRevisionPackage.java
//Synthetic comment -- index 7e61e5f..1348bb9 100755

//Synthetic comment -- @@ -77,7 +77,19 @@
super(source, props, revision, license, description, descUrl,
archiveOs, archiveArch, archiveOsPath);

        mRevision = new MajorRevision(getPropertyInt(props, PkgProps.PKG_MAJOR_REV, revision));
}

/**
//Synthetic comment -- @@ -93,7 +105,7 @@
@Override
public void saveProperties(Properties props) {
super.saveProperties(props);
        props.setProperty(PkgProps.PKG_MAJOR_REV, Integer.toString(mRevision.getMajor()));
}

@Override








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/PkgProps.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/PkgProps.java
//Synthetic comment -- index 4406b42..571a4a6 100755

//Synthetic comment -- @@ -29,6 +29,7 @@
public class PkgProps {

// Base Package
public static final String PKG_LICENSE              = "Pkg.License";            //$NON-NLS-1$
public static final String PKG_DESC                 = "Pkg.Desc";               //$NON-NLS-1$
public static final String PKG_DESC_URL             = "Pkg.DescUrl";            //$NON-NLS-1$
//Synthetic comment -- @@ -37,13 +38,6 @@
public static final String PKG_SOURCE_URL           = "Pkg.SourceUrl";          //$NON-NLS-1$
public static final String PKG_OBSOLETE             = "Pkg.Obsolete";           //$NON-NLS-1$

    // FullRevision
    // Note that MajorRev keeps the legacy "Pkg.Revision" property name for legacy compatibility.
    public static final String PKG_MAJOR_REV            = "Pkg.Revision";           //$NON-NLS-1$
    public static final String PKG_MINOR_REV            = "Pkg.MinorRev";           //$NON-NLS-1$
    public static final String PKG_MICRO_REV            = "Pkg.MicroRev";           //$NON-NLS-1$
    public static final String PKG_PREVIEW_REV          = "Pkg.PreviewRev";         //$NON-NLS-1$

// AndroidVersion

public static final String VERSION_API_LEVEL        = "AndroidVersion.ApiLevel";//$NON-NLS-1$








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/FullRevisionPackageTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/FullRevisionPackageTest.java
//Synthetic comment -- index d816256..9bf2703 100755

//Synthetic comment -- @@ -34,14 +34,7 @@
public static Properties createProps(FullRevision revision) {
Properties props = new Properties();
if (revision != null) {
            props.setProperty(PkgProps.PKG_MAJOR_REV,
                              Integer.toString(revision.getMajor()));
            props.setProperty(PkgProps.PKG_MINOR_REV,
                              Integer.toString(revision.getMinor()));
            props.setProperty(PkgProps.PKG_MICRO_REV,
                              Integer.toString(revision.getMicro()));
            props.setProperty(PkgProps.PKG_PREVIEW_REV,
                              Integer.toString(revision.getPreview()));
}
return props;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/FullRevisionTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/FullRevisionTest.java
//Synthetic comment -- index 97e76bb..d072d05 100755

//Synthetic comment -- @@ -20,16 +20,6 @@

public class FullRevisionTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

public final void testFullRevision() {
FullRevision p = new FullRevision(5);
assertEquals(5, p.getMajor());
//Synthetic comment -- @@ -38,7 +28,9 @@
assertEquals(FullRevision.NOT_A_PREVIEW, p.getPreview());
assertFalse (p.isPreview());
assertEquals("5", p.toShortString());
assertEquals("5.0.0", p.toString());

p = new FullRevision(5, 0, 0, 6);
assertEquals(5, p.getMajor());
//Synthetic comment -- @@ -47,7 +39,9 @@
assertEquals(6, p.getPreview());
assertTrue  (p.isPreview());
assertEquals("5 rc6", p.toShortString());
assertEquals("5.0.0 rc6", p.toString());

p = new FullRevision(6, 7, 0);
assertEquals(6, p.getMajor());
//Synthetic comment -- @@ -56,7 +50,9 @@
assertEquals(0, p.getPreview());
assertFalse (p.isPreview());
assertEquals("6.7", p.toShortString());
assertEquals("6.7.0", p.toString());

p = new FullRevision(10, 11, 12, FullRevision.NOT_A_PREVIEW);
assertEquals(10, p.getMajor());
//Synthetic comment -- @@ -66,6 +62,7 @@
assertFalse (p.isPreview());
assertEquals("10.11.12", p.toShortString());
assertEquals("10.11.12", p.toString());

p = new FullRevision(10, 11, 12, 13);
assertEquals(10, p.getMajor());
//Synthetic comment -- @@ -75,6 +72,48 @@
assertTrue  (p.isPreview());
assertEquals("10.11.12 rc13", p.toShortString());
assertEquals("10.11.12 rc13", p.toString());
}

public final void testCompareTo() {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MajorRevisionTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MajorRevisionTest.java
new file mode 100755
//Synthetic comment -- index 0000000..b77caad

//Synthetic comment -- @@ -0,0 +1,75 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/PackageTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/PackageTest.java
//Synthetic comment -- index 6023b5a..f4d7b56 100755

//Synthetic comment -- @@ -136,7 +136,7 @@
Properties props = new Properties();

// Package properties
        props.setProperty(PkgProps.PKG_MAJOR_REV, "42");
props.setProperty(PkgProps.PKG_LICENSE, "The License");
props.setProperty(PkgProps.PKG_DESC, "Some description.");
props.setProperty(PkgProps.PKG_DESC_URL, "http://description/url");








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AboutDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AboutDialog.java
//Synthetic comment -- index 6352bdb..35e3420 100755

//Synthetic comment -- @@ -107,7 +107,7 @@
}
}

            String revision = p.getProperty(PkgProps.PKG_MAJOR_REV);
if (revision != null) {
return revision;
}







