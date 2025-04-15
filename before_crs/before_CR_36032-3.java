/*Rework SDK Manager support for major.minor.micro revisions.

2 things in this CL:

- There's a bit of refactoring:
  - PreviewVersion becomes FullVersion (which is
    a full major.minor.micro.preview)
  - And I introduce a MajorRevision (which is just
    a major number)
  - Package.getRevision() returns one of these
    revision objects instead of an integer which
    leads to a multide of small boring changes.
- Changed the PackageDiffLogic and its test to
  adequately use the new revision; "tools preview"
  packages are placed in their own category and
  releases can update previews but not the reverse.

Change-Id:Ia80fd9a3791919e827ce0d183c0f297f0d27f2e6*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java
//Synthetic comment -- index 8284054..a786728 100644

//Synthetic comment -- @@ -463,7 +463,7 @@
int revision = 1;
LayoutlibVersion layoutlibVersion = null;
try {
                revision = Integer.parseInt(platformProp.get(PkgProps.PKG_REVISION));
} catch (NumberFormatException e) {
// do nothing, we'll keep the default value of 1.
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/DocPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/DocPackage.java
//Synthetic comment -- index 635dff9..9d6c02c 100755

//Synthetic comment -- @@ -170,12 +170,12 @@
if (mVersion.isPreview()) {
return String.format("Documentation for Android '%1$s' Preview SDK, revision %2$s%3$s",
mVersion.getCodename(),
                    getRevision(),
isObsolete() ? " (Obsolete)" : "");
} else {
return String.format("Documentation for Android SDK, API %1$d, revision %2$s%3$s",
mVersion.getApiLevel(),
                    getRevision(),
isObsolete() ? " (Obsolete)" : "");
}
}
//Synthetic comment -- @@ -194,8 +194,8 @@
}

if (s.indexOf("revision") == -1) {
            s += String.format("\nRevision %1$d%2$s",
                    getRevision(),
isObsolete() ? " (Obsolete)" : "");
}

//Synthetic comment -- @@ -257,7 +257,7 @@
// Check if they're the same exact (api and codename)
if (replacementVersion.equals(mVersion)) {
// exact same version, so check the revision level
            if (replacementPackage.getRevision() > this.getRevision()) {
return UpdateInfo.UPDATE;
}
} else {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/ExtraPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/ExtraPackage.java
//Synthetic comment -- index cc27853..3b921ae 100755

//Synthetic comment -- @@ -490,9 +490,9 @@
*/
@Override
public String getShortDescription() {
        String s = String.format("%1$s, revision %2$d%3$s",
getDisplayName(),
                getRevision(),
isObsolete() ? " (Obsolete)" : "");  //$NON-NLS-2$

return s;
//Synthetic comment -- @@ -506,9 +506,9 @@
*/
@Override
public String getLongDescription() {
        String s = String.format("%1$s, revision %2$d%3$s\nBy %4$s",
getDisplayName(),
                getRevision(),
isObsolete() ? " (Obsolete)" : "",  //$NON-NLS-2$
getVendorDisplay());









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/PreviewVersion.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/FullRevision.java
similarity index 90%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/PreviewVersion.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/FullRevision.java
//Synthetic comment -- index a75eb4b..f902001 100755

//Synthetic comment -- @@ -22,8 +22,10 @@
* (major.minor.micro) and an optional preview revision
* (the lack of a preview number indicates it's not a preview
*  but a final package.)
*/
public class PreviewVersion implements Comparable<PreviewVersion> {

public static final int IMPLICIT_MINOR_REV = 0;
public static final int IMPLICIT_MICRO_REV = 0;
//Synthetic comment -- @@ -34,15 +36,15 @@
private final int mMicro;
private final int mPreview;

    public PreviewVersion(int major) {
this(major, 0, 0);
}

    public PreviewVersion(int major, int minor, int micro) {
this(major, minor, micro, NOT_A_PREVIEW);
}

    public PreviewVersion(int major, int minor, int micro, int preview) {
mMajor = major;
mMinor = minor;
mMicro = micro;
//Synthetic comment -- @@ -130,10 +132,10 @@
if (rhs == null) {
return false;
}
        if (!(rhs instanceof PreviewVersion)) {
return false;
}
        PreviewVersion other = (PreviewVersion) rhs;
if (mMajor != other.mMajor) {
return false;
}
//Synthetic comment -- @@ -150,7 +152,7 @@
}

/**
     * Trivial comparision of a version, e.g 17.1.2 < 18.0.0.
*
* Note that preview/release candidate are released before their final version,
* so "18.0.0 rc1" comes below "18.0.0". The best way to think of it as if the
//Synthetic comment -- @@ -159,7 +161,7 @@
* and more than "18.1.2.4"
*/
@Override
    public int compareTo(PreviewVersion rhs) {
int delta = mMajor - rhs.mMajor;
if (delta != 0) {
return delta;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/PreviewVersionPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/FullRevisionPackage.java
similarity index 62%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/PreviewVersionPackage.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/FullRevisionPackage.java
//Synthetic comment -- index eac548e..9024af5 100755

//Synthetic comment -- @@ -16,10 +16,10 @@

package com.android.sdklib.internal.repository.packages;

import com.android.annotations.NonNull;
import com.android.sdklib.internal.repository.XmlParserUtils;
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.PkgProps;
import com.android.sdklib.repository.SdkRepoConstants;
//Synthetic comment -- @@ -30,13 +30,13 @@
import java.util.Properties;

/**
 * Represents a package in an SDK repository that has a {@link PreviewVersion},
* which is a multi-part revision number (major.minor.micro) and an optional preview revision.
*/
public abstract class PreviewVersionPackage extends Package
        implements IPreviewVersionProvider {

    private final PreviewVersion mPreviewVersion;

/**
* Creates a new package from the attributes and elements of the given XML node.
//Synthetic comment -- @@ -48,25 +48,25 @@
*          parameters that vary according to the originating XML schema.
* @param licenses The licenses loaded from the XML originating document.
*/
    PreviewVersionPackage(SdkSource source,
Node packageNode,
String nsUri,
Map<String,String> licenses) {
super(source, packageNode, nsUri, licenses);

        // The major revision is getRevision(), already handled by Package.

int minorRevision = XmlParserUtils.getXmlInt(packageNode,
SdkRepoConstants.NODE_MINOR_REV,
                PreviewVersion.IMPLICIT_MINOR_REV);
int microRevision = XmlParserUtils.getXmlInt(packageNode,
SdkRepoConstants.NODE_MICRO_REV,
                PreviewVersion.IMPLICIT_MICRO_REV);
int preview = XmlParserUtils.getXmlInt(packageNode,
SdkRepoConstants.NODE_PREVIEW,
                PreviewVersion.NOT_A_PREVIEW);

        mPreviewVersion = new PreviewVersion(getRevision(), minorRevision, microRevision, preview);
}

/**
//Synthetic comment -- @@ -78,7 +78,7 @@
* <p/>
* By design, this creates a package with one and only one archive.
*/
    public PreviewVersionPackage(
SdkSource source,
Properties props,
int revision,
//Synthetic comment -- @@ -91,26 +91,26 @@
super(source, props, revision, license, description, descUrl,
archiveOs, archiveArch, archiveOsPath);

        // The major revision is getRevision(), already handled by Package.

int minorRevision = Integer.parseInt(
getProperty(props,
PkgProps.PKG_MINOR_REV,
                        Integer.toString(PreviewVersion.IMPLICIT_MINOR_REV)));
int microRevision = Integer.parseInt(
getProperty(props,
PkgProps.PKG_MICRO_REV,
                        Integer.toString(PreviewVersion.IMPLICIT_MINOR_REV)));
int preview = Integer.parseInt(
getProperty(props,
PkgProps.PKG_PREVIEW_REV,
                        Integer.toString(PreviewVersion.NOT_A_PREVIEW)));

        mPreviewVersion = new PreviewVersion(getRevision(), minorRevision, microRevision, preview);
}

    @Override @NonNull
    public PreviewVersion getPreviewVersion() {
return mPreviewVersion;
}

//Synthetic comment -- @@ -118,11 +118,9 @@
public void saveProperties(Properties props) {
super.saveProperties(props);

        // The major revision is getRevision(), already handled by Package.
        assert mPreviewVersion.getMajor() == getRevision();

        props.setProperty(PkgProps.PKG_MINOR_REV, Integer.toString(mPreviewVersion.getMinor()));
        props.setProperty(PkgProps.PKG_MICRO_REV, Integer.toString(mPreviewVersion.getMicro()));
props.setProperty(PkgProps.PKG_PREVIEW_REV, Integer.toString(mPreviewVersion.getPreview()));
}

//Synthetic comment -- @@ -142,10 +140,10 @@
if (!super.equals(obj)) {
return false;
}
        if (!(obj instanceof PreviewVersionPackage)) {
return false;
}
        PreviewVersionPackage other = (PreviewVersionPackage) obj;
if (mPreviewVersion == null) {
if (other.mPreviewVersion != null) {
return false;
//Synthetic comment -- @@ -155,4 +153,38 @@
}
return true;
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/IFullRevisionProvider.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/IFullRevisionProvider.java
new file mode 100755
//Synthetic comment -- index 0000000..497b2f8

//Synthetic comment -- @@ -0,0 +1,43 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/IPreviewVersionProvider.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/IPreviewVersionProvider.java
deleted file mode 100755
//Synthetic comment -- index 5a9f8a0..0000000

//Synthetic comment -- @@ -1,32 +0,0 @@
/*
 * Copyright (C) 2009 The Android Open Source Project
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

import com.android.annotations.NonNull;


/**
 * Interface for packages that provide a {@link PreviewVersion},
 * which is a multi-part revision number (major.minor.micro) and an optional preview revision.
 */
public interface IPreviewVersionProvider {

    /**
     * Returns a {@link PreviewVersion} for this package. Never null.
     */
    public abstract @NonNull PreviewVersion getPreviewVersion();
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/MajorRevision.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/MajorRevision.java
new file mode 100755
//Synthetic comment -- index 0000000..2678b1f

//Synthetic comment -- @@ -0,0 +1,37 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/Package.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/Package.java
//Synthetic comment -- index 6760747..9c2d30b 100755

//Synthetic comment -- @@ -58,7 +58,7 @@
*/
public abstract class Package implements IDescription, Comparable<Package> {

    private final int mRevision;
private final String mObsolete;
private final String mLicense;
private final String mDescription;
//Synthetic comment -- @@ -104,7 +104,8 @@
*/
Package(SdkSource source, Node packageNode, String nsUri, Map<String,String> licenses) {
mSource = source;
        mRevision    = XmlParserUtils.getXmlInt   (packageNode, SdkRepoConstants.NODE_REVISION, 0);
mDescription = XmlParserUtils.getXmlString(packageNode, SdkRepoConstants.NODE_DESCRIPTION);
mDescUrl     = XmlParserUtils.getXmlString(packageNode, SdkRepoConstants.NODE_DESC_URL);
mReleaseNote = XmlParserUtils.getXmlString(packageNode, SdkRepoConstants.NODE_RELEASE_NOTE);
//Synthetic comment -- @@ -144,8 +145,8 @@
descUrl = "";
}

        mRevision = Integer.parseInt(
                       getProperty(props, PkgProps.PKG_REVISION, Integer.toString(revision)));
mLicense     = getProperty(props, PkgProps.PKG_LICENSE,      license);
mDescription = getProperty(props, PkgProps.PKG_DESC,         description);
mDescUrl     = getProperty(props, PkgProps.PKG_DESC_URL,     descUrl);
//Synthetic comment -- @@ -222,7 +223,7 @@
* These properties will later be give the constructor that takes a {@link Properties} object.
*/
public void saveProperties(Properties props) {
        props.setProperty(PkgProps.PKG_REVISION, Integer.toString(mRevision));
if (mLicense != null && mLicense.length() > 0) {
props.setProperty(PkgProps.PKG_LICENSE, mLicense);
}
//Synthetic comment -- @@ -328,7 +329,7 @@
* Returns the revision, an int > 0, for all packages (platform, add-on, tool, doc).
* Can be 0 if this is a local package of unknown revision.
*/
    public int getRevision() {
return mRevision;
}

//Synthetic comment -- @@ -484,8 +485,8 @@
sb.append("\n");
}

        sb.append(String.format("Revision %1$d%2$s",
                getRevision(),
isObsolete() ? " (Obsolete)" : ""));

s = getDescUrl();
//Synthetic comment -- @@ -615,8 +616,8 @@
* <p/>
* Two packages are considered the same if they represent the same thing, except for the
* revision number.
     * @param pkg the package to compare
     * @return true if the item
*/
public abstract boolean sameItemAs(Package pkg);

//Synthetic comment -- @@ -638,12 +639,12 @@
}

// check they are the same item.
        if (sameItemAs(replacementPackage) == false) {
return UpdateInfo.INCOMPATIBLE;
}

// check revision number
        if (replacementPackage.getRevision() > this.getRevision()) {
return UpdateInfo.UPDATE;
}

//Synthetic comment -- @@ -652,7 +653,7 @@
}

/**
     * Returns an ordering like this: <br/>
* - Tools <br/>
* - Platform-Tools <br/>
* - Docs. <br/>
//Synthetic comment -- @@ -668,6 +669,10 @@
* Important: this must NOT be used to compare if two packages are the same thing.
* This is achieved by {@link #sameItemAs(Package)} or {@link #canBeUpdatedBy(Package)}.
* <p/>
* This {@link #compareTo(Package)} method is purely an implementation detail to
* perform the right ordering of the packages in the list of available or installed packages.
* <p/>
//Synthetic comment -- @@ -679,7 +684,8 @@
String s1 = this.comparisonKey();
String s2 = other.comparisonKey();

        return s1.compareTo(s2);
}

/**
//Synthetic comment -- @@ -728,7 +734,8 @@


// We insert the package version here because it is more important
        // than the revision number. We want package version to be sorted
// top-down, so we'll use 10k-api as the sorting key. The day we
// reach 10k APIs, we'll need to revisit this.
sb.append("|v:");                                                       //$NON-NLS-1$
//Synthetic comment -- @@ -743,10 +750,18 @@

// Append revision number
sb.append("|r:");                                                       //$NON-NLS-1$
        if (this instanceof IPreviewVersionProvider) {
            sb.append(String.format("%1$s", ((IPreviewVersionProvider) this).getPreviewVersion()));
} else {
            sb.append(String.format("%1$04d", getRevision()));                  //$NON-NLS-1$
}

sb.append('|');
//Synthetic comment -- @@ -759,7 +774,7 @@
int result = 1;
result = prime * result + Arrays.hashCode(mArchives);
result = prime * result + ((mObsolete == null) ? 0 : mObsolete.hashCode());
        result = prime * result + mRevision;
result = prime * result + ((mSource == null) ? 0 : mSource.hashCode());
return result;
}
//Synthetic comment -- @@ -786,7 +801,7 @@
} else if (!mObsolete.equals(other.mObsolete)) {
return false;
}
        if (mRevision != other.mRevision) {
return false;
}
if (mSource == null) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/PlatformPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/PlatformPackage.java
//Synthetic comment -- index 2f75610..6efb620 100755

//Synthetic comment -- @@ -222,13 +222,13 @@
if (mVersion.isPreview()) {
s = String.format("SDK Platform Android %1$s Preview, revision %2$s%3$s",
getVersionName(),
                    getRevision(),
isObsolete() ? " (Obsolete)" : "");  //$NON-NLS-2$
} else {
s = String.format("SDK Platform Android %1$s, API %2$d, revision %3$s%4$s",
getVersionName(),
mVersion.getApiLevel(),
                getRevision(),
isObsolete() ? " (Obsolete)" : "");      //$NON-NLS-2$
}

//Synthetic comment -- @@ -249,8 +249,8 @@
}

if (s.indexOf("revision") == -1) {
            s += String.format("\nRevision %1$d%2$s",
                    getRevision(),
isObsolete() ? " (Obsolete)" : "");
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/PlatformToolPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/PlatformToolPackage.java
//Synthetic comment -- index 2566ce3..fdacfcc 100755

//Synthetic comment -- @@ -39,10 +39,12 @@
/**
* Represents a platform-tool XML node in an SDK repository.
*/
public class PlatformToolPackage extends PreviewVersionPackage {

/** The value returned by {@link PlatformToolPackage#installId()}. */
public static final String INSTALL_ID = "platform-tools";                       //$NON-NLS-1$

/**
* Creates a new platform-tool package from the attributes and elements of the given XML node.
//Synthetic comment -- @@ -153,13 +155,18 @@

/**
* Returns a string identifier to install this package from the command line.
     * For platform-tools, we use "platform-tools" since this package type is unique.
* <p/>
* {@inheritDoc}
*/
@Override
public String installId() {
        return INSTALL_ID;
}

/**
//Synthetic comment -- @@ -179,7 +186,7 @@
@Override
public String getShortDescription() {
return String.format("Android SDK Platform-tools, revision %1$s%2$s",
                getPreviewVersion().toShortString(),
isObsolete() ? " (Obsolete)" : "");
}

//Synthetic comment -- @@ -193,7 +200,7 @@

if (s.indexOf("revision") == -1) {
s += String.format("\nRevision %1$s%2$s",
                    getPreviewVersion().toShortString(),
isObsolete() ? " (Obsolete)" : "");
}

//Synthetic comment -- @@ -215,10 +222,28 @@
return new File(osSdkRoot, SdkConstants.FD_PLATFORM_TOOLS);
}

@Override
public boolean sameItemAs(Package pkg) {
// only one platform-tool package so any platform-tool package is the same item.
        return pkg instanceof PlatformToolPackage;
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/SamplePackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/SamplePackage.java
//Synthetic comment -- index e95c12e..59d6adc 100755

//Synthetic comment -- @@ -222,10 +222,10 @@
*/
@Override
public String getShortDescription() {
        String s = String.format("Samples for SDK API %1$s%2$s, revision %3$d%4$s",
mVersion.getApiString(),
mVersion.isPreview() ? " Preview" : "",
                getRevision(),
isObsolete() ? " (Obsolete)" : "");
return s;
}
//Synthetic comment -- @@ -244,8 +244,8 @@
}

if (s.indexOf("revision") == -1) {
            s += String.format("\nRevision %1$d%2$s",
                    getRevision(),
isObsolete() ? " (Obsolete)" : "");
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/SourcePackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/SourcePackage.java
//Synthetic comment -- index cf280f8..fc4d8af 100755

//Synthetic comment -- @@ -227,12 +227,12 @@
if (mVersion.isPreview()) {
return String.format("Sources for Android '%1$s' Preview SDK, revision %2$s%3$s",
mVersion.getCodename(),
                getRevision(),
isObsolete() ? " (Obsolete)" : "");
} else {
return String.format("Sources for Android SDK, API %1$d, revision %2$s%3$s",
mVersion.getApiLevel(),
                getRevision(),
isObsolete() ? " (Obsolete)" : "");
}
}
//Synthetic comment -- @@ -251,8 +251,8 @@
}

if (s.indexOf("revision") == -1) {
            s += String.format("\nRevision %1$d%2$s",
                    getRevision(),
isObsolete() ? " (Obsolete)" : "");
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/SystemImagePackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/SystemImagePackage.java
//Synthetic comment -- index e1e441c..963b80e 100755

//Synthetic comment -- @@ -254,7 +254,7 @@
return String.format("%1$s System Image, Android API %2$s, revision %3$s%4$s",
getAbiDisplayName(),
mVersion.getApiString(),
                getRevision(),
isObsolete() ? " (Obsolete)" : "");
}

//Synthetic comment -- @@ -272,8 +272,8 @@
}

if (s.indexOf("revision") == -1) {
            s += String.format("\nRevision %1$d%2$s",
                    getRevision(),
isObsolete() ? " (Obsolete)" : "");
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/ToolPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/ToolPackage.java
//Synthetic comment -- index 763ed43..c34a3d0 100755

//Synthetic comment -- @@ -44,10 +44,12 @@
/**
* Represents a tool XML node in an SDK repository.
*/
public class ToolPackage extends PreviewVersionPackage implements IMinPlatformToolsDependency {

/** The value returned by {@link ToolPackage#installId()}. */
public static final String INSTALL_ID = "tools";                             //$NON-NLS-1$

/**
* The minimal revision of the platform-tools package required by this package
//Synthetic comment -- @@ -163,13 +165,17 @@

/**
* Returns a string identifier to install this package from the command line.
     * For tools, we use "tools" since this package is unique.
* <p/>
* {@inheritDoc}
*/
@Override
public String installId() {
        return INSTALL_ID;
}

/**
//Synthetic comment -- @@ -189,7 +195,7 @@
@Override
public String getShortDescription() {
return String.format("Android SDK Tools, revision %1$s%2$s",
                getPreviewVersion().toShortString(),
isObsolete() ? " (Obsolete)" : "");
}

//Synthetic comment -- @@ -203,7 +209,7 @@

if (s.indexOf("revision") == -1) {
s += String.format("\nRevision %1$s%2$s",
                    getPreviewVersion().toShortString(),
isObsolete() ? " (Obsolete)" : "");
}

//Synthetic comment -- @@ -225,10 +231,29 @@
return new File(osSdkRoot, SdkConstants.FD_TOOLS);
}

@Override
public boolean sameItemAs(Package pkg) {
// only one tool package so any tool package is the same item.
        return pkg instanceof ToolPackage;
}

@Override








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/PkgProps.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/PkgProps.java
//Synthetic comment -- index a570153..5b39e89 100755

//Synthetic comment -- @@ -29,8 +29,7 @@
public class PkgProps {

// Base Package

    public static final String PKG_REVISION             = "Pkg.Revision";           //$NON-NLS-1$
public static final String PKG_LICENSE              = "Pkg.License";            //$NON-NLS-1$
public static final String PKG_DESC                 = "Pkg.Desc";               //$NON-NLS-1$
public static final String PKG_DESC_URL             = "Pkg.DescUrl";            //$NON-NLS-1$
//Synthetic comment -- @@ -39,17 +38,17 @@
public static final String PKG_SOURCE_URL           = "Pkg.SourceUrl";          //$NON-NLS-1$
public static final String PKG_OBSOLETE             = "Pkg.Obsolete";           //$NON-NLS-1$

// AndroidVersion

public static final String VERSION_API_LEVEL        = "AndroidVersion.ApiLevel";//$NON-NLS-1$
public static final String VERSION_CODENAME         = "AndroidVersion.CodeName";//$NON-NLS-1$

    // PreviewVersion

    public static final String PKG_MINOR_REV            = "PreviewVersion.MinorRev";//$NON-NLS-1$
    public static final String PKG_MICRO_REV            = "PreviewVersion.MicroRev";//$NON-NLS-1$
    public static final String PKG_PREVIEW_REV          = "PreviewVersion.Preview"; //$NON-NLS-1$


// AddonPackage









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/FullRevisionPackageTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/FullRevisionPackageTest.java
new file mode 100755
//Synthetic comment -- index 0000000..63e1d05

//Synthetic comment -- @@ -0,0 +1,60 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/PreviewVersionTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/FullRevisionTest.java
similarity index 75%
rename from sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/PreviewVersionTest.java
rename to sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/FullRevisionTest.java
//Synthetic comment -- index daa4704..97e76bb 100755

//Synthetic comment -- @@ -18,7 +18,7 @@

import junit.framework.TestCase;

public class PreviewVersionTest extends TestCase {

@Override
protected void setUp() throws Exception {
//Synthetic comment -- @@ -30,26 +30,26 @@
super.tearDown();
}

    public final void testPreviewVersion() {
        PreviewVersion p = new PreviewVersion(5);
assertEquals(5, p.getMajor());
        assertEquals(PreviewVersion.IMPLICIT_MINOR_REV, p.getMinor());
        assertEquals(PreviewVersion.IMPLICIT_MICRO_REV, p.getMicro());
        assertEquals(PreviewVersion.NOT_A_PREVIEW, p.getPreview());
assertFalse (p.isPreview());
assertEquals("5", p.toShortString());
assertEquals("5.0.0", p.toString());

        p = new PreviewVersion(5, 0, 0, 6);
assertEquals(5, p.getMajor());
        assertEquals(PreviewVersion.IMPLICIT_MINOR_REV, p.getMinor());
        assertEquals(PreviewVersion.IMPLICIT_MICRO_REV, p.getMicro());
assertEquals(6, p.getPreview());
assertTrue  (p.isPreview());
assertEquals("5 rc6", p.toShortString());
assertEquals("5.0.0 rc6", p.toString());

        p = new PreviewVersion(6, 7, 0);
assertEquals(6, p.getMajor());
assertEquals(7, p.getMinor());
assertEquals(0, p.getMicro());
//Synthetic comment -- @@ -58,7 +58,7 @@
assertEquals("6.7", p.toShortString());
assertEquals("6.7.0", p.toString());

        p = new PreviewVersion(10, 11, 12, PreviewVersion.NOT_A_PREVIEW);
assertEquals(10, p.getMajor());
assertEquals(11, p.getMinor());
assertEquals(12, p.getMicro());
//Synthetic comment -- @@ -67,7 +67,7 @@
assertEquals("10.11.12", p.toShortString());
assertEquals("10.11.12", p.toString());

        p = new PreviewVersion(10, 11, 12, 13);
assertEquals(10, p.getMajor());
assertEquals(11, p.getMinor());
assertEquals(12, p.getMicro());
//Synthetic comment -- @@ -78,13 +78,13 @@
}

public final void testCompareTo() {
        PreviewVersion s4 = new PreviewVersion(4);
        PreviewVersion i4 = new PreviewVersion(4);
        PreviewVersion g5 = new PreviewVersion(5, 1, 0, 6);
        PreviewVersion y5 = new PreviewVersion(5);
        PreviewVersion c5 = new PreviewVersion(5, 1, 0, 6);
        PreviewVersion o5 = new PreviewVersion(5, 0, 0, 7);
        PreviewVersion p5 = new PreviewVersion(5, 1, 0, 0);

assertEquals(s4, i4);                   // 4.0.0-0 == 4.0.0-0
assertEquals(g5, c5);                   // 5.1.0-6 == 5.1.0-6








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockEmptyPackage.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockEmptyPackage.java
//Synthetic comment -- index f1e4344..5c7bfe3 100755

//Synthetic comment -- @@ -134,7 +134,7 @@
public String getShortDescription() {
StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
sb.append(" '").append(mTestHandle).append('\'');
        if (getRevision() > 0) {
sb.append(" rev=").append(getRevision());
}
return sb.toString();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockPlatformToolPackage.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockPlatformToolPackage.java
//Synthetic comment -- index 0b46876..7ed5114 100755

//Synthetic comment -- @@ -67,11 +67,11 @@
* <p/>
* By design, this creates a package with one and only one archive.
*/
    public MockPlatformToolPackage(SdkSource source, PreviewVersion previewVersion) {
super(
source, // source,
                createProps(previewVersion), // props,
                previewVersion.getMajor(),
null, // license,
"desc", // description,
"url", // descUrl,
//Synthetic comment -- @@ -81,14 +81,14 @@
);
}

    private static Properties createProps(PreviewVersion previewVersion) {
Properties props = new Properties();
props.setProperty(PkgProps.PKG_MINOR_REV,
                          Integer.toString(previewVersion.getMinor()));
props.setProperty(PkgProps.PKG_MICRO_REV,
                          Integer.toString(previewVersion.getMicro()));
props.setProperty(PkgProps.PKG_PREVIEW_REV,
                          Integer.toString(previewVersion.getPreview()));
return props;
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockToolPackage.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockToolPackage.java
//Synthetic comment -- index e4bd9c6..cdccbb8 100755

//Synthetic comment -- @@ -69,12 +69,12 @@
*/
public MockToolPackage(
SdkSource source,
            PreviewVersion previewVersion,
int minPlatformToolsRev) {
super(
source, // source,
                createProps(previewVersion, minPlatformToolsRev), // props,
                previewVersion.getMajor(),
null, // license,
"desc", // description,
"url", // descUrl,
//Synthetic comment -- @@ -84,17 +84,17 @@
);
}

    private static Properties createProps(PreviewVersion previewVersion, int minPlatformToolsRev) {
Properties props = new Properties();
props.setProperty(PkgProps.MIN_PLATFORM_TOOLS_REV,
Integer.toString((minPlatformToolsRev)));
        if (previewVersion != null) {
props.setProperty(PkgProps.PKG_MINOR_REV,
                              Integer.toString(previewVersion.getMinor()));
props.setProperty(PkgProps.PKG_MICRO_REV,
                              Integer.toString(previewVersion.getMicro()));
props.setProperty(PkgProps.PKG_PREVIEW_REV,
                              Integer.toString(previewVersion.getPreview()));
}
return props;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/PackageTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/PackageTest.java
//Synthetic comment -- index ce41a4d..cfe70c1 100755

//Synthetic comment -- @@ -136,7 +136,7 @@
Properties props = new Properties();

// Package properties
        props.setProperty(PkgProps.PKG_REVISION, "42");
props.setProperty(PkgProps.PKG_LICENSE, "The License");
props.setProperty(PkgProps.PKG_DESC, "Some description.");
props.setProperty(PkgProps.PKG_DESC_URL, "http://description/url");
//Synthetic comment -- @@ -155,7 +155,7 @@
*/
protected void testCreatedPackage(Package p) {
// Package properties
        assertEquals(42, p.getRevision());
assertEquals("The License", p.getLicense());
assertEquals("Some description.", p.getDescription());
assertEquals("http://description/url", p.getDescUrl());








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AboutDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AboutDialog.java
//Synthetic comment -- index 35e3420..6352bdb 100755

//Synthetic comment -- @@ -107,7 +107,7 @@
}
}

            String revision = p.getProperty(PkgProps.PKG_REVISION);
if (revision != null) {
return revision;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterChooserDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterChooserDialog.java
//Synthetic comment -- index 55e7db2..a41a952 100755

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.IAndroidVersionProvider;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.ui.GridDialog;
//Synthetic comment -- @@ -423,8 +424,8 @@
if (aOld != null) {
Package pOld = aOld.getParentPackage();

            int rOld = pOld.getRevision();
            int rNew = pNew.getRevision();

boolean showRev = true;

//Synthetic comment -- @@ -435,17 +436,17 @@

if (!vOld.equals(vNew)) {
// Versions are different, so indicate more than just the revision.
                    addText(String.format("This update will replace API %1$s revision %2$d with API %3$s revision %4$d.\n\n",
                            vOld.getApiString(), rOld,
                            vNew.getApiString(), rNew));
showRev = false;
}
}

if (showRev) {
                addText(String.format("This update will replace revision %1$d with revision %2$d.\n\n",
                        rOld,
                        rNew));
}
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterLogic.java
//Synthetic comment -- index 97de564..d5ad456 100755

//Synthetic comment -- @@ -23,12 +23,14 @@
import com.android.sdklib.internal.repository.packages.AddonPackage;
import com.android.sdklib.internal.repository.packages.DocPackage;
import com.android.sdklib.internal.repository.packages.ExtraPackage;
import com.android.sdklib.internal.repository.packages.IExactApiLevelDependency;
import com.android.sdklib.internal.repository.packages.IMinApiLevelDependency;
import com.android.sdklib.internal.repository.packages.IMinPlatformToolsDependency;
import com.android.sdklib.internal.repository.packages.IMinToolsDependency;
import com.android.sdklib.internal.repository.packages.IPlatformDependency;
import com.android.sdklib.internal.repository.packages.IAndroidVersionProvider;
import com.android.sdklib.internal.repository.packages.MinToolsPackage;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.packages.Package.UpdateInfo;
//Synthetic comment -- @@ -184,6 +186,14 @@
return archives;
}

/**
* Finds new packages that the user does not have in his/her local SDK
* and adds them to the list of archives to install.
//Synthetic comment -- @@ -212,15 +222,15 @@
ArchiveInfo[] localArchives = createLocalArchives(localPkgs);

// Find the highest platform installed
        float currentPlatformScore = 0;
        float currentSampleScore = 0;
        float currentAddonScore = 0;
        float currentDocScore = 0;
        HashMap<String, Float> currentExtraScore = new HashMap<String, Float>();
if (!includeAll) {
if (localPkgs != null) {
for (Package p : localPkgs) {
                    int rev = p.getRevision();
int api = 0;
boolean isPreview = false;
if (p instanceof IAndroidVersionProvider) {
//Synthetic comment -- @@ -229,10 +239,10 @@
isPreview = vers.isPreview();
}

                    // The score is 10*api + (1 if preview) + rev/100
// This allows previews to rank above a non-preview and
// allows revisions to rank appropriately.
                    float score = api * 10 + (isPreview ? 1 : 0) + rev/100.f;

if (p instanceof PlatformPackage) {
currentPlatformScore = Math.max(currentPlatformScore, score);
//Synthetic comment -- @@ -261,7 +271,7 @@
continue;
}

            int rev = p.getRevision();
int api = 0;
boolean isPreview = false;
if (p instanceof IAndroidVersionProvider) {
//Synthetic comment -- @@ -270,7 +280,7 @@
isPreview = vers.isPreview();
}

            float score = api * 10 + (isPreview ? 1 : 0) + rev/100.f;

boolean shouldAdd = false;
if (p instanceof PlatformPackage) {
//Synthetic comment -- @@ -282,7 +292,7 @@
} else if (p instanceof ExtraPackage) {
String key = ((ExtraPackage) p).getPath();
shouldAdd = !currentExtraScore.containsKey(key) ||
                    score > currentExtraScore.get(key).floatValue();
} else if (p instanceof DocPackage) {
// We don't want all the doc, only the most recent one
if (score > currentDocScore) {
//Synthetic comment -- @@ -673,20 +683,22 @@
SdkSource[] remoteSources,
ArchiveInfo[] localArchives) {
// This is the requirement to match.
        int rev = pkg.getMinToolsRevision();

        if (rev == MinToolsPackage.MIN_TOOLS_REV_NOT_SPECIFIED) {
// Well actually there's no requirement.
return null;
}

// First look in locally installed packages.
for (ArchiveInfo ai : localArchives) {
Archive a = ai.getNewArchive();
if (a != null) {
Package p = a.getParentPackage();
if (p instanceof ToolPackage) {
                    if (((ToolPackage) p).getRevision() >= rev) {
// We found one already installed.
return null;
}
//Synthetic comment -- @@ -700,7 +712,7 @@
if (a != null) {
Package p = a.getParentPackage();
if (p instanceof ToolPackage) {
                    if (((ToolPackage) p).getRevision() >= rev) {
// The dependency is already scheduled for install, nothing else to do.
return ai;
}
//Synthetic comment -- @@ -713,7 +725,7 @@
for (Archive a : selectedArchives) {
Package p = a.getParentPackage();
if (p instanceof ToolPackage) {
                    if (((ToolPackage) p).getRevision() >= rev) {
// It's not already in the list of things to install, so add it now
return insertArchive(a,
outArchives,
//Synthetic comment -- @@ -731,7 +743,7 @@
fetchRemotePackages(remotePkgs, remoteSources);
for (Package p : remotePkgs) {
if (p instanceof ToolPackage) {
                if (((ToolPackage) p).getRevision() >= rev) {
// It's not already in the list of things to install, so add the
// first compatible archive we can find.
for (Archive a : p.getArchives()) {
//Synthetic comment -- @@ -752,7 +764,7 @@
// We end up here if nothing matches. We don't have a good platform to match.
// We need to indicate this extra depends on a missing platform archive
// so that it can be impossible to install later on.
        return new MissingArchiveInfo(MissingArchiveInfo.TITLE_TOOL, rev);
}

/**
//Synthetic comment -- @@ -771,12 +783,13 @@
SdkSource[] remoteSources,
ArchiveInfo[] localArchives) {
// This is the requirement to match.
        int rev = pkg.getMinPlatformToolsRevision();
boolean findMax = false;
ArchiveInfo aiMax = null;
Archive aMax = null;

        if (rev == IMinPlatformToolsDependency.MIN_PLATFORM_TOOLS_REV_INVALID) {
// The requirement is invalid, which is not supposed to happen since this
// property is mandatory. However in a typical upgrade scenario we can end
// up with the previous updater managing a new package and not dealing
//Synthetic comment -- @@ -792,11 +805,11 @@
if (a != null) {
Package p = a.getParentPackage();
if (p instanceof PlatformToolPackage) {
                    int r = ((PlatformToolPackage) p).getRevision();
                    if (findMax && r > rev) {
rev = r;
aiMax = ai;
                    } else if (!findMax && r >= rev) {
// We found one already installed.
return null;
}
//Synthetic comment -- @@ -810,11 +823,11 @@
if (a != null) {
Package p = a.getParentPackage();
if (p instanceof PlatformToolPackage) {
                    int r = ((PlatformToolPackage) p).getRevision();
                    if (findMax && r > rev) {
rev = r;
aiMax = ai;
                    } else if (!findMax && r >= rev) {
// The dependency is already scheduled for install, nothing else to do.
return ai;
}
//Synthetic comment -- @@ -827,12 +840,12 @@
for (Archive a : selectedArchives) {
Package p = a.getParentPackage();
if (p instanceof PlatformToolPackage) {
                    int r = ((PlatformToolPackage) p).getRevision();
                    if (findMax && r > rev) {
rev = r;
aiMax = null;
aMax = a;
                    } else if (!findMax && r >= rev) {
// It's not already in the list of things to install, so add it now
return insertArchive(a,
outArchives,
//Synthetic comment -- @@ -850,16 +863,16 @@
fetchRemotePackages(remotePkgs, remoteSources);
for (Package p : remotePkgs) {
if (p instanceof PlatformToolPackage) {
                int r = ((PlatformToolPackage) p).getRevision();
                if (r >= rev) {
// Make sure there's at least one valid archive here
for (Archive a : p.getArchives()) {
if (a.isCompatible()) {
                            if (findMax && r > rev) {
rev = r;
aiMax = null;
aMax = a;
                            } else if (!findMax && r >= rev) {
// It's not already in the list of things to install, so add the
// first compatible archive we can find.
return insertArchive(a,
//Synthetic comment -- @@ -893,7 +906,7 @@
// We end up here if nothing matches. We don't have a good platform to match.
// We need to indicate this package depends on a missing platform archive
// so that it can be impossible to install later on.
        return new MissingArchiveInfo(MissingArchiveInfo.TITLE_PLATFORM_TOOL, rev);
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackagesDiffLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackagesDiffLogic.java
//Synthetic comment -- index 687238b..8e1cbb3 100755

//Synthetic comment -- @@ -21,7 +21,9 @@
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.repository.packages.ExtraPackage;
import com.android.sdklib.internal.repository.packages.IAndroidVersionProvider;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.packages.PlatformPackage;
import com.android.sdklib.internal.repository.packages.PlatformToolPackage;
import com.android.sdklib.internal.repository.packages.SystemImagePackage;
//Synthetic comment -- @@ -80,11 +82,12 @@
/**
* Mark all new and update PkgItems as checked.
*
     * @param selectNew If true, select all new packages
     * @param selectUpdates If true, select all update packages
     * @param selectTop If true, select the top platform. If the top platform has nothing installed,
     *   select all items in it; if it is partially installed, at least select the platform and
     *   system images if none of the system images are installed.
* @param currentPlatform The {@link SdkConstants#currentPlatform()} value.
*/
public void checkNewUpdateItems(
//Synthetic comment -- @@ -97,7 +100,8 @@
SparseArray<List<PkgItem>> platformItems = new SparseArray<List<PkgItem>>();

// sort items in platforms... directly deal with new/update items
        for (PkgItem item : getAllPkgItems(true /*byApi*/, true /*bySource*/)) {
if (!item.hasCompatibleArchive()) {
// Ignore items that have no archive compatible with the current platform.
continue;
//Synthetic comment -- @@ -129,8 +133,42 @@
items.add(item);
}

            if ((selectNew && item.getState() == PkgState.NEW) ||
                    (selectUpdates && item.hasUpdatePkg())) {
item.setChecked(true);
}
}
//Synthetic comment -- @@ -140,7 +178,8 @@
if (!installedPlatforms.contains(maxApi)) {
// If the top platform has nothing installed at all, select everything in it
for (PkgItem item : items) {
                    if (item.getState() == PkgState.NEW || item.hasUpdatePkg()) {
item.setChecked(true);
}
}
//Synthetic comment -- @@ -151,7 +190,8 @@
// First make sure the platform package itself is installed, or select it.
for (PkgItem item : items) {
Package p = item.getMainPackage();
                     if (p instanceof PlatformPackage && item.getState() == PkgState.NEW) {
item.setChecked(true);
break;
}
//Synthetic comment -- @@ -163,7 +203,7 @@
Package p = item.getMainPackage();
if (p instanceof PlatformPackage && item.getState() == PkgState.INSTALLED) {
if (item.hasUpdatePkg() && item.isChecked()) {
                            // If the installed platform is schedule for update, look for the
// system image in the update package, not the current one.
p = item.getUpdatePkg();
if (p instanceof PlatformPackage) {
//Synthetic comment -- @@ -190,6 +230,7 @@
Package p = item.getMainPackage();
if (p instanceof PlatformPackage) {
if (item.getState() == PkgState.NEW &&
((PlatformPackage) p).getIncludedAbi() != null) {
item.setChecked(true);
hasSysImg = true;
//Synthetic comment -- @@ -220,7 +261,9 @@
// On Windows, we'll also auto-select the USB driver
for (PkgItem item : getAllPkgItems(true /*byApi*/, true /*bySource*/)) {
Package p = item.getMainPackage();
                if (p instanceof ExtraPackage && item.getState() == PkgState.NEW) {
ExtraPackage ep = (ExtraPackage) p;
if (ep.getVendorId().equals("google") &&            //$NON-NLS-1$
ep.getPath().equals("usb_driver")) {        //$NON-NLS-1$
//Synthetic comment -- @@ -519,7 +562,7 @@

switch (currItem.getState()) {
case NEW:
                            if (newPkg.getRevision() < mainPkg.getRevision()) {
if (!op.isKeep(currItem)) {
// The new item has a lower revision than the current one,
// but the current one hasn't been marked as being kept so
//Synthetic comment -- @@ -528,7 +571,7 @@
addNewItem(op, newPkg, PkgState.NEW);
hasChanged = true;
}
                            } else if (newPkg.getRevision() > mainPkg.getRevision()) {
// We have a more recent new version, remove the current one
// and replace by a new one
currItemIt.remove();
//Synthetic comment -- @@ -538,7 +581,7 @@
break;
case INSTALLED:
// if newPkg.revision<=mainPkg.revision: it's already installed, ignore.
                            if (newPkg.getRevision() > mainPkg.getRevision()) {
// This is a new update for the main package.
if (currItem.mergeUpdate(newPkg)) {
op.keep(currItem.getUpdatePkg());
//Synthetic comment -- @@ -611,8 +654,11 @@
return ((IAndroidVersionProvider) pkg).getAndroidVersion();

} else if (pkg instanceof ToolPackage || pkg instanceof PlatformToolPackage) {
                return PkgCategoryApi.KEY_TOOLS;

} else {
return PkgCategoryApi.KEY_EXTRA;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackagesPage.java
//Synthetic comment -- index ea02e3a..51543e8 100755

//Synthetic comment -- @@ -1313,7 +1313,7 @@
} else if (mColumn == mColumnRevision) {
if (element instanceof PkgItem) {
PkgItem pkg = (PkgItem) element;
                    return pkg.getRevisionStr();
}

} else if (mColumn == mColumnStatus) {
//Synthetic comment -- @@ -1343,7 +1343,7 @@

} else if (element instanceof Package) {
// This is an update package.
                    return "New revision " + Integer.toString(((Package) element).getRevision());
}
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PkgCategoryApi.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PkgCategoryApi.java
//Synthetic comment -- index 5bb4917..d1e3a28 100755

//Synthetic comment -- @@ -31,7 +31,9 @@
// them.
// (Note: don't use integer.max to avoid integers wrapping in comparisons. We can
// revisit the day we get 2^30 platforms.)
    public final static AndroidVersion KEY_TOOLS = new AndroidVersion(Integer.MAX_VALUE / 2, null);;
public final static AndroidVersion KEY_EXTRA = new AndroidVersion(-1, null);

public PkgCategoryApi(AndroidVersion version, String platformName, Object iconRef) {
//Synthetic comment -- @@ -55,6 +57,8 @@
AndroidVersion key = (AndroidVersion) getKey();
if (key.equals(KEY_TOOLS)) {
return "TOOLS";             //$NON-NLS-1$ // for internal debug use only
} else if (key.equals(KEY_EXTRA)) {
return "EXTRAS";            //$NON-NLS-1$ // for internal debug use only
} else {
//Synthetic comment -- @@ -70,6 +74,8 @@

if (key.equals(KEY_TOOLS)) {
label = "Tools";
} else if (key.equals(KEY_EXTRA)) {
label = "Extras";
} else {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PkgItem.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PkgItem.java
//Synthetic comment -- index ae20deb..89910cd 100755

//Synthetic comment -- @@ -17,8 +17,8 @@
package com.android.sdkuilib.internal.repository.sdkman2;

import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.IAndroidVersionProvider;
import com.android.sdklib.internal.repository.packages.IPreviewVersionProvider;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.packages.Package.UpdateInfo;
import com.android.sdklib.internal.repository.sources.SdkSource;
//Synthetic comment -- @@ -90,17 +90,10 @@
return mMainPkg.getListDescription();
}

    public int getRevision() {
return mMainPkg.getRevision();
}

    public String getRevisionStr() {
        if (mMainPkg instanceof IPreviewVersionProvider) {
            return ((IPreviewVersionProvider) mMainPkg).getPreviewVersion().toShortString();
        }
        return Integer.toString(mMainPkg.getRevision());
    }

public String getDescription() {
return mMainPkg.getDescription();
}
//Synthetic comment -- @@ -157,24 +150,24 @@

/**
* Checks whether the main packages are of the same type and are
     * not an update of each other.
*/
public boolean isSameMainPackageAs(Package pkg) {
if (mMainPkg.canBeUpdatedBy(pkg) == UpdateInfo.NOT_UPDATE) {
// package revision numbers must match
            return mMainPkg.getRevision() == pkg.getRevision();
}
return false;
}

/**
* Checks whether the update packages are of the same type and are
     * not an update of each other.
*/
public boolean isSameUpdatePackageAs(Package pkg) {
if (mUpdatePkg != null && mUpdatePkg.canBeUpdatedBy(pkg) == UpdateInfo.NOT_UPDATE) {
// package revision numbers must match
            return mUpdatePkg.getRevision() == pkg.getRevision();
}
return false;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/sdkman2/PackagesDiffLogicTest.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/sdkman2/PackagesDiffLogicTest.java
//Synthetic comment -- index a328ef1..26b39ed 100755

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.repository.packages.BrokenPackage;
import com.android.sdklib.internal.repository.packages.MockAddonPackage;
import com.android.sdklib.internal.repository.packages.MockBrokenPackage;
import com.android.sdklib.internal.repository.packages.MockEmptyPackage;
//Synthetic comment -- @@ -1290,7 +1291,6 @@
}

public void testBrokenAddon() {

SdkSource src1 = new SdkRepoSource("http://1.example.com/url1", "repo1");
SdkSource src2 = new SdkRepoSource("http://2.example.com/url2", "repo2");

//Synthetic comment -- @@ -1383,8 +1383,6 @@
}

public void testToolsUpdate() {
        //

SdkSource src1 = new SdkRepoSource("http://1.example.com/url1", "repo1");
SdkSource src2 = new SdkRepoSource("http://2.example.com/url2", "repo2");
MockPlatformPackage p1;
//Synthetic comment -- @@ -1427,6 +1425,327 @@
"-- <NEW, pkg:The addon B from vendor 1, Android API 1, revision 6>\n",
getTree(m, false /*displaySortByApi*/));
}
// ----

/**







