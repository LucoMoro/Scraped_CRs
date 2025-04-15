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
import com.android.sdklib.internal.repository.packages.FullRevision;
import com.android.sdklib.repository.PkgProps;

import org.osgi.framework.Constants;
//Synthetic comment -- @@ -48,7 +49,7 @@
/**
* The minimum version of the SDK Tools that this version of ADT requires.
*/
    private final static FullRevision MIN_TOOLS_REV = new FullRevision(17);

/**
* Pattern to get the minimum plugin version supported by the SDK. This is read from
//Synthetic comment -- @@ -57,7 +58,7 @@
private final static Pattern sPluginVersionPattern = Pattern.compile(
"^plugin.version=(\\d+)\\.(\\d+)\\.(\\d+).*$"); //$NON-NLS-1$
private final static Pattern sSourcePropPattern = Pattern.compile(
            "^" + PkgProps.PKG_REVISION + "=(.*)$"); //$NON-NLS-1$

/**
* Checks the plugin and the SDK have compatible versions.
//Synthetic comment -- @@ -136,7 +137,7 @@

// now check whether the tools are new enough.
String osTools = osSdkPath + SdkConstants.OS_SDK_TOOLS_FOLDER;
        FullRevision toolsRevision = new FullRevision(Integer.MAX_VALUE);
try {
reader = new FileReader(osTools + SdkConstants.FN_SOURCE_PROP);
BufferedReader bReader = new BufferedReader(reader);
//Synthetic comment -- @@ -144,7 +145,9 @@
while ((line = bReader.readLine()) != null) {
Matcher m = sSourcePropPattern.matcher(line);
if (m.matches()) {
                    try {
                        toolsRevision = FullRevision.parseRevision(m.group(1));
                    } catch (NumberFormatException ignore) {}
break;
}
}
//Synthetic comment -- @@ -163,7 +166,7 @@
}
}

        if (toolsRevision.compareTo(MIN_TOOLS_REV) < 0) {
// this is a warning only as we need to parse the SDK to allow updating
// of the tools!
return errorHandler.handleWarning(








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java
//Synthetic comment -- index a786728..8284054 100644

//Synthetic comment -- @@ -463,7 +463,7 @@
int revision = 1;
LayoutlibVersion layoutlibVersion = null;
try {
                revision = Integer.parseInt(platformProp.get(PkgProps.PKG_REVISION));
} catch (NumberFormatException e) {
// do nothing, we'll keep the default value of 1.
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/FullRevision.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/FullRevision.java
//Synthetic comment -- index a90fa0a..85b05f3 100755

//Synthetic comment -- @@ -16,6 +16,11 @@

package com.android.sdklib.internal.repository.packages;

import com.android.annotations.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
* Package multi-part revision number composed of a tuple
//Synthetic comment -- @@ -72,6 +77,58 @@
return mPreview;
}

    private static Pattern FULL_REVISION_PATTERN =
        //                   1=major       2=minor       3=micro              4=preview
        Pattern.compile("\\s*([0-9]+)(?:\\.([0-9]+)(?:\\.([0-9]+))?)?\\s*(?:rc([0-9]+))?\\s*");

    /**
     * Parses a string of format "major.minor.micro rcPreview" and returns
     * a new {@link FullRevision} for it. All the fields except major are
     * optional.
     * <p/>
     * The parsing is equivalent to the pseudo-BNF/regexp:
     * <pre>
     *   Major/Minor/Micro/Preview := [0-9]+
     *   Revision := Major ('.' Minor ('.' Micro)? )? \s* ('rc'Preview)?
     * </pre>
     *
     * @param revision A non-null revision to parse.
     * @return A new non-null {@link FullRevision}.
     * @throws NumberFormatException if the parsing failed.
     */
    public static @NonNull FullRevision parseRevision(@NonNull String revision)
            throws NumberFormatException {

        if (revision == null) {
            throw new NumberFormatException("revision is <null>"); //$NON-NLS-1$
        }

        Throwable cause = null;
        try {
            Matcher m = FULL_REVISION_PATTERN.matcher(revision);
            if (m != null && m.matches()) {
                int major = Integer.parseInt(m.group(1));
                String s = m.group(2);
                int minor = s == null ? IMPLICIT_MINOR_REV : Integer.parseInt(s);
                s = m.group(3);
                int micro = s == null ? IMPLICIT_MICRO_REV : Integer.parseInt(s);
                s = m.group(4);
                int preview = s == null ? NOT_A_PREVIEW : Integer.parseInt(s);

                return new FullRevision(major, minor, micro, preview);
            }
        } catch (Throwable t) {
            cause = t;
        }

        NumberFormatException n = new NumberFormatException(
                "Invalid full revision: " + revision); //$NON-NLS-1$
        if (cause != null) {
            n.initCause(cause);
        }
        throw n;
    }

/**
* Returns the version in a fixed format major.minor.micro
* with an optional "rc preview#". For example it would








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/FullRevisionPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/FullRevisionPackage.java
//Synthetic comment -- index 10c4ce0..6028873 100755

//Synthetic comment -- @@ -102,12 +102,19 @@
super(source, props, revision, license, description, descUrl,
archiveOs, archiveArch, archiveOsPath);

        String revStr = getProperty(props, PkgProps.PKG_REVISION, null);

        FullRevision rev = null;
        if (revStr != null) {
            try {
                rev = FullRevision.parseRevision(revStr);
            } catch (NumberFormatException ignore) {}
        }
        if (rev == null) {
            rev = new FullRevision(revision);
        }

        mPreviewVersion = rev;
}

@Override
//Synthetic comment -- @@ -118,11 +125,7 @@
@Override
public void saveProperties(Properties props) {
super.saveProperties(props);
        props.setProperty(PkgProps.PKG_REVISION, mPreviewVersion.toString());
}

@Override








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/MajorRevision.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/MajorRevision.java
//Synthetic comment -- index 2678b1f..9ca9e22 100755

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.sdklib.internal.repository.packages;

import com.android.annotations.NonNull;


/**
* Package revision number composed of a <em>single</em> major revision.
//Synthetic comment -- @@ -34,4 +36,21 @@
public String toString() {
return super.toShortString();
}

    /**
     * Parses a single-integer string and returns a new {@link MajorRevision} for it.
     *
     * @param revision A non-null revision to parse.
     * @return A new non-null {@link MajorRevision}.
     * @throws NumberFormatException if the parsing failed.
     */
    public static @NonNull MajorRevision parseRevision(@NonNull String revision)
            throws NumberFormatException {

        if (revision == null) {
            throw new NumberFormatException("revision is <null>"); //$NON-NLS-1$
        }

        return new MajorRevision(Integer.parseInt(revision.trim()));
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/MajorRevisionPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/MajorRevisionPackage.java
//Synthetic comment -- index 7e61e5f..1348bb9 100755

//Synthetic comment -- @@ -77,7 +77,19 @@
super(source, props, revision, license, description, descUrl,
archiveOs, archiveArch, archiveOsPath);

        String revStr = getProperty(props, PkgProps.PKG_REVISION, null);

        MajorRevision rev = null;
        if (revStr != null) {
            try {
                rev = MajorRevision.parseRevision(revStr);
            } catch (NumberFormatException ignore) {}
        }
        if (rev == null) {
            rev = new MajorRevision(revision);
        }

        mRevision = rev;
}

/**
//Synthetic comment -- @@ -93,7 +105,7 @@
@Override
public void saveProperties(Properties props) {
super.saveProperties(props);
        props.setProperty(PkgProps.PKG_REVISION, mRevision.toString());
}

@Override








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/PkgProps.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/PkgProps.java
//Synthetic comment -- index 4406b42..571a4a6 100755

//Synthetic comment -- @@ -29,6 +29,7 @@
public class PkgProps {

// Base Package
    public static final String PKG_REVISION             = "Pkg.Revision";           //$NON-NLS-1$
public static final String PKG_LICENSE              = "Pkg.License";            //$NON-NLS-1$
public static final String PKG_DESC                 = "Pkg.Desc";               //$NON-NLS-1$
public static final String PKG_DESC_URL             = "Pkg.DescUrl";            //$NON-NLS-1$
//Synthetic comment -- @@ -37,13 +38,6 @@
public static final String PKG_SOURCE_URL           = "Pkg.SourceUrl";          //$NON-NLS-1$
public static final String PKG_OBSOLETE             = "Pkg.Obsolete";           //$NON-NLS-1$

// AndroidVersion

public static final String VERSION_API_LEVEL        = "AndroidVersion.ApiLevel";//$NON-NLS-1$








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/FullRevisionPackageTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/FullRevisionPackageTest.java
//Synthetic comment -- index d816256..9bf2703 100755

//Synthetic comment -- @@ -34,14 +34,7 @@
public static Properties createProps(FullRevision revision) {
Properties props = new Properties();
if (revision != null) {
            props.setProperty(PkgProps.PKG_REVISION, revision.toString());
}
return props;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/FullRevisionTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/FullRevisionTest.java
//Synthetic comment -- index 97e76bb..73fc038 100755

//Synthetic comment -- @@ -20,16 +20,6 @@

public class FullRevisionTest extends TestCase {

public final void testFullRevision() {
FullRevision p = new FullRevision(5);
assertEquals(5, p.getMajor());
//Synthetic comment -- @@ -38,7 +28,9 @@
assertEquals(FullRevision.NOT_A_PREVIEW, p.getPreview());
assertFalse (p.isPreview());
assertEquals("5", p.toShortString());
        assertEquals(p, FullRevision.parseRevision("5"));
assertEquals("5.0.0", p.toString());
        assertEquals(p, FullRevision.parseRevision("5.0.0"));

p = new FullRevision(5, 0, 0, 6);
assertEquals(5, p.getMajor());
//Synthetic comment -- @@ -47,7 +39,9 @@
assertEquals(6, p.getPreview());
assertTrue  (p.isPreview());
assertEquals("5 rc6", p.toShortString());
        assertEquals(p, FullRevision.parseRevision("5 rc6"));
assertEquals("5.0.0 rc6", p.toString());
        assertEquals(p, FullRevision.parseRevision("5.0.0 rc6"));

p = new FullRevision(6, 7, 0);
assertEquals(6, p.getMajor());
//Synthetic comment -- @@ -56,7 +50,9 @@
assertEquals(0, p.getPreview());
assertFalse (p.isPreview());
assertEquals("6.7", p.toShortString());
        assertEquals(p, FullRevision.parseRevision("6.7"));
assertEquals("6.7.0", p.toString());
        assertEquals(p, FullRevision.parseRevision("6.7.0"));

p = new FullRevision(10, 11, 12, FullRevision.NOT_A_PREVIEW);
assertEquals(10, p.getMajor());
//Synthetic comment -- @@ -66,6 +62,7 @@
assertFalse (p.isPreview());
assertEquals("10.11.12", p.toShortString());
assertEquals("10.11.12", p.toString());
        assertEquals(p, FullRevision.parseRevision("10.11.12"));

p = new FullRevision(10, 11, 12, 13);
assertEquals(10, p.getMajor());
//Synthetic comment -- @@ -75,6 +72,48 @@
assertTrue  (p.isPreview());
assertEquals("10.11.12 rc13", p.toShortString());
assertEquals("10.11.12 rc13", p.toString());
        assertEquals(p, FullRevision.parseRevision("10.11.12 rc13"));
        assertEquals(p, FullRevision.parseRevision("   10.11.12 rc13"));
        assertEquals(p, FullRevision.parseRevision("10.11.12 rc13   "));
        assertEquals(p, FullRevision.parseRevision("   10.11.12   rc13   "));
    }

    public final void testParseError() {
        String errorMsg = null;
        try {
            FullRevision.parseRevision("not a number");
            fail("FullRevision.parseRevision should thrown NumberFormatException");
        } catch (NumberFormatException e) {
            errorMsg = e.getMessage();
        }
        assertEquals("Invalid full revision: not a number", errorMsg);
        
        errorMsg = null;
        try {
            FullRevision.parseRevision("5 .6 .7");
            fail("FullRevision.parseRevision should thrown NumberFormatException");
        } catch (NumberFormatException e) {
            errorMsg = e.getMessage();
        }
        assertEquals("Invalid full revision: 5 .6 .7", errorMsg);

        errorMsg = null;
        try {
            FullRevision.parseRevision("5.0.0 preview 1");
            fail("FullRevision.parseRevision should thrown NumberFormatException");
        } catch (NumberFormatException e) {
            errorMsg = e.getMessage();
        }
        assertEquals("Invalid full revision: 5.0.0 preview 1", errorMsg);

        errorMsg = null;
        try {
            FullRevision.parseRevision("  5.1.2 rc 42  ");
            fail("FullRevision.parseRevision should thrown NumberFormatException");
        } catch (NumberFormatException e) {
            errorMsg = e.getMessage();
        }
        assertEquals("Invalid full revision:   5.1.2 rc 42  ", errorMsg);
}

public final void testCompareTo() {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MajorRevisionTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MajorRevisionTest.java
new file mode 100755
//Synthetic comment -- index 0000000..b77caad

//Synthetic comment -- @@ -0,0 +1,75 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
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

package com.android.sdklib.internal.repository.packages;

import junit.framework.TestCase;

public class MajorRevisionTest extends TestCase {

    public final void testMajorRevision() {
        MajorRevision p = new MajorRevision(5);
        assertEquals(5, p.getMajor());
        assertEquals(FullRevision.IMPLICIT_MINOR_REV, p.getMinor());
        assertEquals(FullRevision.IMPLICIT_MICRO_REV, p.getMicro());
        assertEquals(FullRevision.NOT_A_PREVIEW, p.getPreview());
        assertFalse (p.isPreview());
        assertEquals("5", p.toShortString());
        assertEquals(p, MajorRevision.parseRevision("5"));
        assertEquals("5", p.toString());

        assertEquals(new FullRevision(5, 0, 0, 0), p);
    }

    public final void testParseError() {
        String errorMsg = null;
        try {
            MajorRevision.parseRevision("5.0.0");
            fail("MajorRevision.parseRevision should thrown NumberFormatException");
        } catch (NumberFormatException e) {
            errorMsg = e.getMessage();
        }
        assertEquals("For input string: \"5.0.0\"", errorMsg);
    }

    public final void testCompareTo() {
        MajorRevision s4 = new MajorRevision(4);
        MajorRevision i4 = new MajorRevision(4);
        FullRevision  g5 = new FullRevision (5, 1, 0, 6);
        MajorRevision y5 = new MajorRevision(5);
        FullRevision  c5 = new FullRevision (5, 1, 0, 6);
        FullRevision  o5 = new FullRevision (5, 0, 0, 7);
        FullRevision  p5 = new FullRevision (5, 1, 0, 0);

        assertEquals(s4, i4);                   // 4.0.0-0 == 4.0.0-0
        assertEquals(g5, c5);                   // 5.1.0-6 == 5.1.0-6

        assertFalse(y5.equals(p5));             // 5.0.0-0 != 5.1.0-0
        assertFalse(g5.equals(p5));             // 5.1.0-6 != 5.1.0-0
        assertTrue (s4.compareTo(i4) == 0);     // 4.0.0-0 == 4.0.0-0
        assertTrue (s4.compareTo(y5)  < 0);     // 4.0.0-0  < 5.0.0-0
        assertTrue (y5.compareTo(y5) == 0);     // 5.0.0-0 == 5.0.0-0
        assertTrue (y5.compareTo(p5)  < 0);     // 5.0.0-0  < 5.1.0-0
        assertTrue (o5.compareTo(y5)  < 0);     // 5.0.0-7  < 5.0.0-0
        assertTrue (p5.compareTo(p5) == 0);     // 5.1.0-0 == 5.1.0-0
        assertTrue (c5.compareTo(p5)  < 0);     // 5.1.0-6  < 5.1.0-0
        assertTrue (p5.compareTo(c5)  > 0);     // 5.1.0-0  > 5.1.0-6
        assertTrue (p5.compareTo(o5)  > 0);     // 5.1.0-0  > 5.0.0-7
        assertTrue (c5.compareTo(o5)  > 0);     // 5.1.0-6  > 5.0.0-7
        assertTrue (o5.compareTo(o5) == 0);     // 5.0.0-7  > 5.0.0-7
    }

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/PackageTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/PackageTest.java
//Synthetic comment -- index 6023b5a..f4d7b56 100755

//Synthetic comment -- @@ -136,7 +136,7 @@
Properties props = new Properties();

// Package properties
        props.setProperty(PkgProps.PKG_REVISION, "42");
props.setProperty(PkgProps.PKG_LICENSE, "The License");
props.setProperty(PkgProps.PKG_DESC, "Some description.");
props.setProperty(PkgProps.PKG_DESC_URL, "http://description/url");








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AboutDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AboutDialog.java
//Synthetic comment -- index 6352bdb..35e3420 100755

//Synthetic comment -- @@ -107,7 +107,7 @@
}
}

            String revision = p.getProperty(PkgProps.PKG_REVISION);
if (revision != null) {
return revision;
}







