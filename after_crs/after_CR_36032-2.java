/*Rework SDK Manager support for major.minor.micro revisions.

Change-Id:Ia80fd9a3791919e827ce0d183c0f297f0d27f2e6*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java
//Synthetic comment -- index 8284054..a786728 100644

//Synthetic comment -- @@ -463,7 +463,7 @@
int revision = 1;
LayoutlibVersion layoutlibVersion = null;
try {
                revision = Integer.parseInt(platformProp.get(PkgProps.PKG_MAJOR_REV));
} catch (NumberFormatException e) {
// do nothing, we'll keep the default value of 1.
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/DocPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/DocPackage.java
//Synthetic comment -- index 635dff9..9d6c02c 100755

//Synthetic comment -- @@ -170,12 +170,12 @@
if (mVersion.isPreview()) {
return String.format("Documentation for Android '%1$s' Preview SDK, revision %2$s%3$s",
mVersion.getCodename(),
                    getRevision().toShortString(),
isObsolete() ? " (Obsolete)" : "");
} else {
return String.format("Documentation for Android SDK, API %1$d, revision %2$s%3$s",
mVersion.getApiLevel(),
                    getRevision().toShortString(),
isObsolete() ? " (Obsolete)" : "");
}
}
//Synthetic comment -- @@ -194,8 +194,8 @@
}

if (s.indexOf("revision") == -1) {
            s += String.format("\nRevision %1$s%2$s",
                    getRevision().toShortString(),
isObsolete() ? " (Obsolete)" : "");
}

//Synthetic comment -- @@ -257,7 +257,7 @@
// Check if they're the same exact (api and codename)
if (replacementVersion.equals(mVersion)) {
// exact same version, so check the revision level
            if (replacementPackage.getRevision().compareTo(this.getRevision()) > 0) {
return UpdateInfo.UPDATE;
}
} else {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/ExtraPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/ExtraPackage.java
//Synthetic comment -- index cc27853..3b921ae 100755

//Synthetic comment -- @@ -490,9 +490,9 @@
*/
@Override
public String getShortDescription() {
        String s = String.format("%1$s, revision %2$s%3$s",
getDisplayName(),
                getRevision().toShortString(),
isObsolete() ? " (Obsolete)" : "");  //$NON-NLS-2$

return s;
//Synthetic comment -- @@ -506,9 +506,9 @@
*/
@Override
public String getLongDescription() {
        String s = String.format("%1$s, revision %2$s%3$s\nBy %4$s",
getDisplayName(),
                getRevision().toShortString(),
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
 *
 *  @see MajorRevision
*/
public class FullRevision implements Comparable<FullRevision> {

public static final int IMPLICIT_MINOR_REV = 0;
public static final int IMPLICIT_MICRO_REV = 0;
//Synthetic comment -- @@ -34,15 +36,15 @@
private final int mMicro;
private final int mPreview;

    public FullRevision(int major) {
this(major, 0, 0);
}

    public FullRevision(int major, int minor, int micro) {
this(major, minor, micro, NOT_A_PREVIEW);
}

    public FullRevision(int major, int minor, int micro, int preview) {
mMajor = major;
mMinor = minor;
mMicro = micro;
//Synthetic comment -- @@ -130,10 +132,10 @@
if (rhs == null) {
return false;
}
        if (!(rhs instanceof FullRevision)) {
return false;
}
        FullRevision other = (FullRevision) rhs;
if (mMajor != other.mMajor) {
return false;
}
//Synthetic comment -- @@ -150,7 +152,7 @@
}

/**
     * Trivial comparison of a version, e.g 17.1.2 < 18.0.0.
*
* Note that preview/release candidate are released before their final version,
* so "18.0.0 rc1" comes below "18.0.0". The best way to think of it as if the
//Synthetic comment -- @@ -159,7 +161,7 @@
* and more than "18.1.2.4"
*/
@Override
    public int compareTo(FullRevision rhs) {
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

import com.android.sdklib.internal.repository.XmlParserUtils;
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.packages.Package.UpdateInfo;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.PkgProps;
import com.android.sdklib.repository.SdkRepoConstants;
//Synthetic comment -- @@ -30,13 +30,13 @@
import java.util.Properties;

/**
 * Represents a package in an SDK repository that has a {@link FullRevision},
* which is a multi-part revision number (major.minor.micro) and an optional preview revision.
*/
public abstract class FullRevisionPackage extends Package
        implements IFullRevisionProvider {

    private final FullRevision mPreviewVersion;

/**
* Creates a new package from the attributes and elements of the given XML node.
//Synthetic comment -- @@ -48,25 +48,25 @@
*          parameters that vary according to the originating XML schema.
* @param licenses The licenses loaded from the XML originating document.
*/
    FullRevisionPackage(SdkSource source,
Node packageNode,
String nsUri,
Map<String,String> licenses) {
super(source, packageNode, nsUri, licenses);

        // The major revision is in Package.getRevision()
        int majorRevision = super.getRevision().getMajor();
int minorRevision = XmlParserUtils.getXmlInt(packageNode,
SdkRepoConstants.NODE_MINOR_REV,
                FullRevision.IMPLICIT_MINOR_REV);
int microRevision = XmlParserUtils.getXmlInt(packageNode,
SdkRepoConstants.NODE_MICRO_REV,
                FullRevision.IMPLICIT_MICRO_REV);
int preview = XmlParserUtils.getXmlInt(packageNode,
SdkRepoConstants.NODE_PREVIEW,
                FullRevision.NOT_A_PREVIEW);

        mPreviewVersion = new FullRevision(majorRevision, minorRevision, microRevision, preview);
}

/**
//Synthetic comment -- @@ -78,7 +78,7 @@
* <p/>
* By design, this creates a package with one and only one archive.
*/
    public FullRevisionPackage(
SdkSource source,
Properties props,
int revision,
//Synthetic comment -- @@ -91,26 +91,26 @@
super(source, props, revision, license, description, descUrl,
archiveOs, archiveArch, archiveOsPath);

        // The major revision is in Package.getRevision()
        int majorRevision = super.getRevision().getMajor();
int minorRevision = Integer.parseInt(
getProperty(props,
PkgProps.PKG_MINOR_REV,
                        Integer.toString(FullRevision.IMPLICIT_MINOR_REV)));
int microRevision = Integer.parseInt(
getProperty(props,
PkgProps.PKG_MICRO_REV,
                        Integer.toString(FullRevision.IMPLICIT_MINOR_REV)));
int preview = Integer.parseInt(
getProperty(props,
PkgProps.PKG_PREVIEW_REV,
                        Integer.toString(FullRevision.NOT_A_PREVIEW)));

        mPreviewVersion = new FullRevision(majorRevision, minorRevision, microRevision, preview);
}

    @Override
    public FullRevision getRevision() {
return mPreviewVersion;
}

//Synthetic comment -- @@ -118,11 +118,9 @@
public void saveProperties(Properties props) {
super.saveProperties(props);

        props.setProperty(PkgProps.PKG_MAJOR_REV,   Integer.toString(mPreviewVersion.getMajor()));
        props.setProperty(PkgProps.PKG_MINOR_REV,   Integer.toString(mPreviewVersion.getMinor()));
        props.setProperty(PkgProps.PKG_MICRO_REV,   Integer.toString(mPreviewVersion.getMicro()));
props.setProperty(PkgProps.PKG_PREVIEW_REV, Integer.toString(mPreviewVersion.getPreview()));
}

//Synthetic comment -- @@ -142,10 +140,10 @@
if (!super.equals(obj)) {
return false;
}
        if (!(obj instanceof FullRevisionPackage)) {
return false;
}
        FullRevisionPackage other = (FullRevisionPackage) obj;
if (mPreviewVersion == null) {
if (other.mPreviewVersion != null) {
return false;
//Synthetic comment -- @@ -155,4 +153,38 @@
}
return true;
}

    /**
     * Computes whether the given package is a suitable update for the current package.
     * <p/>
     * A specific case here is that a release package can update a preview, whereas
     * a preview can only update another preview.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    public UpdateInfo canBeUpdatedBy(Package replacementPackage) {
        if (replacementPackage == null) {
            return UpdateInfo.INCOMPATIBLE;
        }

        // check they are the same item, ignoring the preview bit.
        if (!sameItemAs(replacementPackage, true /*ignorePreviews*/)) {
            return UpdateInfo.INCOMPATIBLE;
        }

        // a preview cannot update a non-preview
        if (!getRevision().isPreview() && replacementPackage.getRevision().isPreview()) {
            return UpdateInfo.INCOMPATIBLE;
        }

        // check revision number
        if (replacementPackage.getRevision().compareTo(this.getRevision()) > 0) {
            return UpdateInfo.UPDATE;
        }

        // not an upgrade but not incompatible either.
        return UpdateInfo.NOT_UPDATE;
    }

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/IFullRevisionProvider.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/IFullRevisionProvider.java
new file mode 100755
//Synthetic comment -- index 0000000..497b2f8

//Synthetic comment -- @@ -0,0 +1,43 @@
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



/**
 * Interface for packages that provide a {@link FullRevision},
 * which is a multi-part revision number (major.minor.micro) and an optional preview revision.
 * <p/>
 * This interface is a tag. It indicates that {@link Package#getRevision()} returns a
 * {@link FullRevision} instead of a limited {@link MajorRevision}. <br/>
 * The preview version number is available via {@link Package#getRevision()}.
 */
public interface IFullRevisionProvider {

    /**
     * Returns whether the give package represents the same item as the current package.
     * <p/>
     * Two packages are considered the same if they represent the same thing, except for the
     * revision number.
     * @param pkg the package to compare
     * @param ignorePreviews true if 2 packages should be considered the same even if one
     *    is a preview and the other one is not.
     * @return true if the item are the same.
     */
    public abstract boolean sameItemAs(Package pkg, boolean ignorePreviews);

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/IPreviewVersionProvider.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/IPreviewVersionProvider.java
deleted file mode 100755
//Synthetic comment -- index 5a9f8a0..0000000

//Synthetic comment -- @@ -1,32 +0,0 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/MajorRevision.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/MajorRevision.java
new file mode 100755
//Synthetic comment -- index 0000000..2678b1f

//Synthetic comment -- @@ -0,0 +1,37 @@
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


/**
 * Package revision number composed of a <em>single</em> major revision.
 * <p/>
 * Contrary to a {@link FullRevision}, a {@link MajorRevision} does not
 * provide minor, micro and preview revision numbers -- these are all
 * set to zero.
 */
public class MajorRevision extends FullRevision {

    public MajorRevision(int major) {
        super(major, 0, 0);
    }

    @Override
    public String toString() {
        return super.toShortString();
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/Package.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/Package.java
//Synthetic comment -- index 6760747..e741923 100755

//Synthetic comment -- @@ -58,7 +58,7 @@
*/
public abstract class Package implements IDescription, Comparable<Package> {

    private final MajorRevision mRevision;
private final String mObsolete;
private final String mLicense;
private final String mDescription;
//Synthetic comment -- @@ -104,7 +104,8 @@
*/
Package(SdkSource source, Node packageNode, String nsUri, Map<String,String> licenses) {
mSource = source;
        mRevision    = new MajorRevision(
                       XmlParserUtils.getXmlInt   (packageNode, SdkRepoConstants.NODE_REVISION, 0));
mDescription = XmlParserUtils.getXmlString(packageNode, SdkRepoConstants.NODE_DESCRIPTION);
mDescUrl     = XmlParserUtils.getXmlString(packageNode, SdkRepoConstants.NODE_DESC_URL);
mReleaseNote = XmlParserUtils.getXmlString(packageNode, SdkRepoConstants.NODE_RELEASE_NOTE);
//Synthetic comment -- @@ -144,8 +145,8 @@
descUrl = "";
}

        mRevision = new MajorRevision(Integer.parseInt(
                       getProperty(props, PkgProps.PKG_MAJOR_REV, Integer.toString(revision))));
mLicense     = getProperty(props, PkgProps.PKG_LICENSE,      license);
mDescription = getProperty(props, PkgProps.PKG_DESC,         description);
mDescUrl     = getProperty(props, PkgProps.PKG_DESC_URL,     descUrl);
//Synthetic comment -- @@ -222,7 +223,7 @@
* These properties will later be give the constructor that takes a {@link Properties} object.
*/
public void saveProperties(Properties props) {
        props.setProperty(PkgProps.PKG_MAJOR_REV, Integer.toString(mRevision.getMajor()));
if (mLicense != null && mLicense.length() > 0) {
props.setProperty(PkgProps.PKG_LICENSE, mLicense);
}
//Synthetic comment -- @@ -328,7 +329,7 @@
* Returns the revision, an int > 0, for all packages (platform, add-on, tool, doc).
* Can be 0 if this is a local package of unknown revision.
*/
    public FullRevision getRevision() {
return mRevision;
}

//Synthetic comment -- @@ -484,8 +485,8 @@
sb.append("\n");
}

        sb.append(String.format("Revision %1$s%2$s",
                getRevision().toShortString(),
isObsolete() ? " (Obsolete)" : ""));

s = getDescUrl();
//Synthetic comment -- @@ -615,8 +616,8 @@
* <p/>
* Two packages are considered the same if they represent the same thing, except for the
* revision number.
     * @param pkg the package to compare.
     * @return true if the item as equivalent.
*/
public abstract boolean sameItemAs(Package pkg);

//Synthetic comment -- @@ -638,12 +639,12 @@
}

// check they are the same item.
        if (!sameItemAs(replacementPackage)) {
return UpdateInfo.INCOMPATIBLE;
}

// check revision number
        if (replacementPackage.getRevision().compareTo(this.getRevision()) > 0) {
return UpdateInfo.UPDATE;
}

//Synthetic comment -- @@ -652,7 +653,7 @@
}

/**
     * Returns an ordering <b>suitable for display</b> like this: <br/>
* - Tools <br/>
* - Platform-Tools <br/>
* - Docs. <br/>
//Synthetic comment -- @@ -668,6 +669,10 @@
* Important: this must NOT be used to compare if two packages are the same thing.
* This is achieved by {@link #sameItemAs(Package)} or {@link #canBeUpdatedBy(Package)}.
* <p/>
     * The order done here is suitable for display, and this may not be the appropriate
     * order when comparing whether packages are equal or of greater revision -- if you need
     * to compare revisions, then use {@link #getRevision()}{@code .compareTo(rev)} directly.
     * <p/>
* This {@link #compareTo(Package)} method is purely an implementation detail to
* perform the right ordering of the packages in the list of available or installed packages.
* <p/>
//Synthetic comment -- @@ -679,7 +684,8 @@
String s1 = this.comparisonKey();
String s2 = other.comparisonKey();

        int r = s1.compareTo(s2);
        return r;
}

/**
//Synthetic comment -- @@ -728,7 +734,8 @@


// We insert the package version here because it is more important
        // than the revision number.
        // In the list display, we want package version to be sorted
// top-down, so we'll use 10k-api as the sorting key. The day we
// reach 10k APIs, we'll need to revisit this.
sb.append("|v:");                                                       //$NON-NLS-1$
//Synthetic comment -- @@ -743,10 +750,18 @@

// Append revision number
sb.append("|r:");                                                       //$NON-NLS-1$
        FullRevision rev = getRevision();
        sb.append(rev.getMajor()).append('.')
          .append(rev.getMinor()).append('.')
          .append(rev.getMicro()).append('.');
        // Hack: When comparing packages for installation purposes, we want to treat
        // "final releases" packages as more important than rc/preview packages.
        // However like for the API level above, when sorting for list display purposes
        // we want the final releace package listed before its rc/preview packages.
        if (rev.isPreview()) {
            sb.append(rev.getPreview());
} else {
            sb.append('0'); // 0=Final (!preview), to make "18.0" < "18.1" (18 Final < 18 RC1)
}

sb.append('|');
//Synthetic comment -- @@ -759,7 +774,7 @@
int result = 1;
result = prime * result + Arrays.hashCode(mArchives);
result = prime * result + ((mObsolete == null) ? 0 : mObsolete.hashCode());
        result = prime * result + mRevision.hashCode();
result = prime * result + ((mSource == null) ? 0 : mSource.hashCode());
return result;
}
//Synthetic comment -- @@ -786,7 +801,7 @@
} else if (!mObsolete.equals(other.mObsolete)) {
return false;
}
        if (!mRevision.equals(other.mRevision)) {
return false;
}
if (mSource == null) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/PlatformPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/PlatformPackage.java
//Synthetic comment -- index 2f75610..6efb620 100755

//Synthetic comment -- @@ -222,13 +222,13 @@
if (mVersion.isPreview()) {
s = String.format("SDK Platform Android %1$s Preview, revision %2$s%3$s",
getVersionName(),
                    getRevision().toShortString(),
isObsolete() ? " (Obsolete)" : "");  //$NON-NLS-2$
} else {
s = String.format("SDK Platform Android %1$s, API %2$d, revision %3$s%4$s",
getVersionName(),
mVersion.getApiLevel(),
                getRevision().toShortString(),
isObsolete() ? " (Obsolete)" : "");      //$NON-NLS-2$
}

//Synthetic comment -- @@ -249,8 +249,8 @@
}

if (s.indexOf("revision") == -1) {
            s += String.format("\nRevision %1$s%2$s",
                    getRevision().toShortString(),
isObsolete() ? " (Obsolete)" : "");
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/PlatformToolPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/PlatformToolPackage.java
//Synthetic comment -- index 2566ce3..fdacfcc 100755

//Synthetic comment -- @@ -39,10 +39,12 @@
/**
* Represents a platform-tool XML node in an SDK repository.
*/
public class PlatformToolPackage extends FullRevisionPackage {

/** The value returned by {@link PlatformToolPackage#installId()}. */
public static final String INSTALL_ID = "platform-tools";                       //$NON-NLS-1$
    /** The value returned by {@link PlatformToolPackage#installId()}. */
    public static final String INSTALL_ID_PREVIEW = "platform-tools-preview";       //$NON-NLS-1$

/**
* Creates a new platform-tool package from the attributes and elements of the given XML node.
//Synthetic comment -- @@ -153,13 +155,18 @@

/**
* Returns a string identifier to install this package from the command line.
     * For platform-tools, we use "platform-tools" or "platform-tools-preview" since
     * this package type is unique.
* <p/>
* {@inheritDoc}
*/
@Override
public String installId() {
        if (getRevision().isPreview()) {
            return INSTALL_ID_PREVIEW;
        } else {
            return INSTALL_ID;
        }
}

/**
//Synthetic comment -- @@ -179,7 +186,7 @@
@Override
public String getShortDescription() {
return String.format("Android SDK Platform-tools, revision %1$s%2$s",
                getRevision().toShortString(),
isObsolete() ? " (Obsolete)" : "");
}

//Synthetic comment -- @@ -193,7 +200,7 @@

if (s.indexOf("revision") == -1) {
s += String.format("\nRevision %1$s%2$s",
                    getRevision().toShortString(),
isObsolete() ? " (Obsolete)" : "");
}

//Synthetic comment -- @@ -215,10 +222,28 @@
return new File(osSdkRoot, SdkConstants.FD_PLATFORM_TOOLS);
}

    /**
     * Check whether 2 platform-tool packages are the same <em>and</em> have the
     * same preview bit.
     */
@Override
public boolean sameItemAs(Package pkg) {
        return sameItemAs(pkg, false /*ignorePreviews*/);
    }

    @Override
    public boolean sameItemAs(Package pkg, boolean ignorePreviews) {
// only one platform-tool package so any platform-tool package is the same item.
        if (pkg instanceof PlatformToolPackage) {
            if (ignorePreviews) {
                return true;
            } else {
                // however previews can only match previews by default, unless we ignore that check.
                return ((PlatformToolPackage) pkg).getRevision().isPreview() ==
                    getRevision().isPreview();
            }
        }
        return false;
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/SamplePackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/SamplePackage.java
//Synthetic comment -- index e95c12e..59d6adc 100755

//Synthetic comment -- @@ -222,10 +222,10 @@
*/
@Override
public String getShortDescription() {
        String s = String.format("Samples for SDK API %1$s%2$s, revision %3$s%4$s",
mVersion.getApiString(),
mVersion.isPreview() ? " Preview" : "",
                getRevision().toShortString(),
isObsolete() ? " (Obsolete)" : "");
return s;
}
//Synthetic comment -- @@ -244,8 +244,8 @@
}

if (s.indexOf("revision") == -1) {
            s += String.format("\nRevision %1$s%2$s",
                    getRevision().toShortString(),
isObsolete() ? " (Obsolete)" : "");
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/SourcePackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/SourcePackage.java
//Synthetic comment -- index cf280f8..fc4d8af 100755

//Synthetic comment -- @@ -227,12 +227,12 @@
if (mVersion.isPreview()) {
return String.format("Sources for Android '%1$s' Preview SDK, revision %2$s%3$s",
mVersion.getCodename(),
                getRevision().toShortString(),
isObsolete() ? " (Obsolete)" : "");
} else {
return String.format("Sources for Android SDK, API %1$d, revision %2$s%3$s",
mVersion.getApiLevel(),
                getRevision().toShortString(),
isObsolete() ? " (Obsolete)" : "");
}
}
//Synthetic comment -- @@ -251,8 +251,8 @@
}

if (s.indexOf("revision") == -1) {
            s += String.format("\nRevision %1$s%2$s",
                    getRevision().toShortString(),
isObsolete() ? " (Obsolete)" : "");
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/SystemImagePackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/SystemImagePackage.java
//Synthetic comment -- index e1e441c..963b80e 100755

//Synthetic comment -- @@ -254,7 +254,7 @@
return String.format("%1$s System Image, Android API %2$s, revision %3$s%4$s",
getAbiDisplayName(),
mVersion.getApiString(),
                getRevision().toShortString(),
isObsolete() ? " (Obsolete)" : "");
}

//Synthetic comment -- @@ -272,8 +272,8 @@
}

if (s.indexOf("revision") == -1) {
            s += String.format("\nRevision %1$s%2$s",
                    getRevision().toShortString(),
isObsolete() ? " (Obsolete)" : "");
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/ToolPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/ToolPackage.java
//Synthetic comment -- index 763ed43..c34a3d0 100755

//Synthetic comment -- @@ -44,10 +44,12 @@
/**
* Represents a tool XML node in an SDK repository.
*/
public class ToolPackage extends FullRevisionPackage implements IMinPlatformToolsDependency {

/** The value returned by {@link ToolPackage#installId()}. */
public static final String INSTALL_ID = "tools";                             //$NON-NLS-1$
    /** The value returned by {@link ToolPackage#installId()}. */
    private static final String INSTALL_ID_PREVIEW = "tools-preview";            //$NON-NLS-1$

/**
* The minimal revision of the platform-tools package required by this package
//Synthetic comment -- @@ -163,13 +165,17 @@

/**
* Returns a string identifier to install this package from the command line.
     * For tools, we use "tools" or "tools-preview" since this package is unique.
* <p/>
* {@inheritDoc}
*/
@Override
public String installId() {
        if (getRevision().isPreview()) {
            return INSTALL_ID_PREVIEW;
        } else {
            return INSTALL_ID;
        }
}

/**
//Synthetic comment -- @@ -189,7 +195,7 @@
@Override
public String getShortDescription() {
return String.format("Android SDK Tools, revision %1$s%2$s",
                getRevision().toShortString(),
isObsolete() ? " (Obsolete)" : "");
}

//Synthetic comment -- @@ -203,7 +209,7 @@

if (s.indexOf("revision") == -1) {
s += String.format("\nRevision %1$s%2$s",
                    getRevision().toShortString(),
isObsolete() ? " (Obsolete)" : "");
}

//Synthetic comment -- @@ -225,10 +231,29 @@
return new File(osSdkRoot, SdkConstants.FD_TOOLS);
}

    /**
     * Check whether 2 tool packages are the same <em>and</em> have the
     * same preview bit.
     */
@Override
public boolean sameItemAs(Package pkg) {
        // Only one tool package so any tool package is the same item
        return sameItemAs(pkg, false /*ignorePreviews*/);
    }

    @Override
    public boolean sameItemAs(Package pkg, boolean ignorePreviews) {
// only one tool package so any tool package is the same item.
        if (pkg instanceof ToolPackage) {
            if (ignorePreviews) {
                return true;
            } else {
                // however previews can only match previews by default, unless we ignore that check.
                return ((ToolPackage) pkg).getRevision().isPreview() ==
                    getRevision().isPreview();
            }
        }
        return false;
}

@Override








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/PkgProps.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/PkgProps.java
//Synthetic comment -- index a570153..5b39e89 100755

//Synthetic comment -- @@ -29,8 +29,7 @@
public class PkgProps {

// Base Package
    public static final String PKG_MAJOR_REV            = "Pkg.Revision";           //$NON-NLS-1$
public static final String PKG_LICENSE              = "Pkg.License";            //$NON-NLS-1$
public static final String PKG_DESC                 = "Pkg.Desc";               //$NON-NLS-1$
public static final String PKG_DESC_URL             = "Pkg.DescUrl";            //$NON-NLS-1$
//Synthetic comment -- @@ -39,17 +38,17 @@
public static final String PKG_SOURCE_URL           = "Pkg.SourceUrl";          //$NON-NLS-1$
public static final String PKG_OBSOLETE             = "Pkg.Obsolete";           //$NON-NLS-1$

    // FullRevision

    public static final String PKG_MINOR_REV            = "Pkg.MinorRev";           //$NON-NLS-1$
    public static final String PKG_MICRO_REV            = "Pkg.MicroRev";           //$NON-NLS-1$
    public static final String PKG_PREVIEW_REV          = "Pkg.PreviewRev";         //$NON-NLS-1$

// AndroidVersion

public static final String VERSION_API_LEVEL        = "AndroidVersion.ApiLevel";//$NON-NLS-1$
public static final String VERSION_CODENAME         = "AndroidVersion.CodeName";//$NON-NLS-1$


// AddonPackage









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/FullRevisionPackageTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/FullRevisionPackageTest.java
new file mode 100755
//Synthetic comment -- index 0000000..63e1d05

//Synthetic comment -- @@ -0,0 +1,60 @@
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import junit.framework.TestCase;

public class FullRevisionPackageTest extends TestCase {

    public void testCompareTo() throws Exception {
        // Test order of full revision packages.
        //
        // Note that Package.compareTo() is designed to return the desired
        // ordering for a list display and as such a final/release package
        // needs to be listed before its rc/preview package.
        //
        // This differs from the order used by FullRevision.compareTo().

        ArrayList<Package> list = new ArrayList<Package>();

        list.add(new MockToolPackage(null, new FullRevision(1, 0, 0, 0), 8));
        list.add(new MockToolPackage(null, new FullRevision(1, 0, 0, 1), 8));
        list.add(new MockToolPackage(null, new FullRevision(1, 0, 1, 0), 8));
        list.add(new MockToolPackage(null, new FullRevision(1, 0, 1, 1), 8));
        list.add(new MockToolPackage(null, new FullRevision(1, 1, 0, 0), 8));
        list.add(new MockToolPackage(null, new FullRevision(1, 1, 0, 1), 8));
        list.add(new MockToolPackage(null, new FullRevision(2, 1, 1, 0), 8));
        list.add(new MockToolPackage(null, new FullRevision(2, 1, 1, 1), 8));

        Collections.sort(list);

        assertEquals(
                 "[Android SDK Tools, revision 1, " +
                  "Android SDK Tools, revision 1 rc1, " +
                  "Android SDK Tools, revision 1.0.1, " +
                  "Android SDK Tools, revision 1.0.1 rc1, " +
                  "Android SDK Tools, revision 1.1, " +
                  "Android SDK Tools, revision 1.1 rc1, " +
                  "Android SDK Tools, revision 2.1.1, " +
                  "Android SDK Tools, revision 2.1.1 rc1]",
                Arrays.toString(list.toArray()));
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/PreviewVersionTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/FullRevisionTest.java
similarity index 75%
rename from sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/PreviewVersionTest.java
rename to sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/FullRevisionTest.java
//Synthetic comment -- index daa4704..97e76bb 100755

//Synthetic comment -- @@ -18,7 +18,7 @@

import junit.framework.TestCase;

public class FullRevisionTest extends TestCase {

@Override
protected void setUp() throws Exception {
//Synthetic comment -- @@ -30,26 +30,26 @@
super.tearDown();
}

    public final void testFullRevision() {
        FullRevision p = new FullRevision(5);
assertEquals(5, p.getMajor());
        assertEquals(FullRevision.IMPLICIT_MINOR_REV, p.getMinor());
        assertEquals(FullRevision.IMPLICIT_MICRO_REV, p.getMicro());
        assertEquals(FullRevision.NOT_A_PREVIEW, p.getPreview());
assertFalse (p.isPreview());
assertEquals("5", p.toShortString());
assertEquals("5.0.0", p.toString());

        p = new FullRevision(5, 0, 0, 6);
assertEquals(5, p.getMajor());
        assertEquals(FullRevision.IMPLICIT_MINOR_REV, p.getMinor());
        assertEquals(FullRevision.IMPLICIT_MICRO_REV, p.getMicro());
assertEquals(6, p.getPreview());
assertTrue  (p.isPreview());
assertEquals("5 rc6", p.toShortString());
assertEquals("5.0.0 rc6", p.toString());

        p = new FullRevision(6, 7, 0);
assertEquals(6, p.getMajor());
assertEquals(7, p.getMinor());
assertEquals(0, p.getMicro());
//Synthetic comment -- @@ -58,7 +58,7 @@
assertEquals("6.7", p.toShortString());
assertEquals("6.7.0", p.toString());

        p = new FullRevision(10, 11, 12, FullRevision.NOT_A_PREVIEW);
assertEquals(10, p.getMajor());
assertEquals(11, p.getMinor());
assertEquals(12, p.getMicro());
//Synthetic comment -- @@ -67,7 +67,7 @@
assertEquals("10.11.12", p.toShortString());
assertEquals("10.11.12", p.toString());

        p = new FullRevision(10, 11, 12, 13);
assertEquals(10, p.getMajor());
assertEquals(11, p.getMinor());
assertEquals(12, p.getMicro());
//Synthetic comment -- @@ -78,13 +78,13 @@
}

public final void testCompareTo() {
        FullRevision s4 = new FullRevision(4);
        FullRevision i4 = new FullRevision(4);
        FullRevision g5 = new FullRevision(5, 1, 0, 6);
        FullRevision y5 = new FullRevision(5);
        FullRevision c5 = new FullRevision(5, 1, 0, 6);
        FullRevision o5 = new FullRevision(5, 0, 0, 7);
        FullRevision p5 = new FullRevision(5, 1, 0, 0);

assertEquals(s4, i4);                   // 4.0.0-0 == 4.0.0-0
assertEquals(g5, c5);                   // 5.1.0-6 == 5.1.0-6








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockEmptyPackage.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockEmptyPackage.java
//Synthetic comment -- index f1e4344..5c7bfe3 100755

//Synthetic comment -- @@ -134,7 +134,7 @@
public String getShortDescription() {
StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
sb.append(" '").append(mTestHandle).append('\'');
        if (getRevision().getMajor() > 0) {
sb.append(" rev=").append(getRevision());
}
return sb.toString();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockPlatformToolPackage.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockPlatformToolPackage.java
//Synthetic comment -- index 0b46876..7ed5114 100755

//Synthetic comment -- @@ -67,11 +67,11 @@
* <p/>
* By design, this creates a package with one and only one archive.
*/
    public MockPlatformToolPackage(SdkSource source, FullRevision revision) {
super(
source, // source,
                createProps(revision), // props,
                revision.getMajor(),
null, // license,
"desc", // description,
"url", // descUrl,
//Synthetic comment -- @@ -81,14 +81,14 @@
);
}

    private static Properties createProps(FullRevision revision) {
Properties props = new Properties();
props.setProperty(PkgProps.PKG_MINOR_REV,
                          Integer.toString(revision.getMinor()));
props.setProperty(PkgProps.PKG_MICRO_REV,
                          Integer.toString(revision.getMicro()));
props.setProperty(PkgProps.PKG_PREVIEW_REV,
                          Integer.toString(revision.getPreview()));
return props;
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockToolPackage.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockToolPackage.java
//Synthetic comment -- index e4bd9c6..cdccbb8 100755

//Synthetic comment -- @@ -69,12 +69,12 @@
*/
public MockToolPackage(
SdkSource source,
            FullRevision revision,
int minPlatformToolsRev) {
super(
source, // source,
                createProps(revision, minPlatformToolsRev), // props,
                revision.getMajor(),
null, // license,
"desc", // description,
"url", // descUrl,
//Synthetic comment -- @@ -84,17 +84,17 @@
);
}

    private static Properties createProps(FullRevision revision, int minPlatformToolsRev) {
Properties props = new Properties();
props.setProperty(PkgProps.MIN_PLATFORM_TOOLS_REV,
Integer.toString((minPlatformToolsRev)));
        if (revision != null) {
props.setProperty(PkgProps.PKG_MINOR_REV,
                              Integer.toString(revision.getMinor()));
props.setProperty(PkgProps.PKG_MICRO_REV,
                              Integer.toString(revision.getMicro()));
props.setProperty(PkgProps.PKG_PREVIEW_REV,
                              Integer.toString(revision.getPreview()));
}
return props;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/PackageTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/PackageTest.java
//Synthetic comment -- index ce41a4d..cfe70c1 100755

//Synthetic comment -- @@ -136,7 +136,7 @@
Properties props = new Properties();

// Package properties
        props.setProperty(PkgProps.PKG_MAJOR_REV, "42");
props.setProperty(PkgProps.PKG_LICENSE, "The License");
props.setProperty(PkgProps.PKG_DESC, "Some description.");
props.setProperty(PkgProps.PKG_DESC_URL, "http://description/url");
//Synthetic comment -- @@ -155,7 +155,7 @@
*/
protected void testCreatedPackage(Package p) {
// Package properties
        assertEquals("42", p.getRevision().toShortString());
assertEquals("The License", p.getLicense());
assertEquals("Some description.", p.getDescription());
assertEquals("http://description/url", p.getDescUrl());








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AboutDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AboutDialog.java
//Synthetic comment -- index 35e3420..6352bdb 100755

//Synthetic comment -- @@ -107,7 +107,7 @@
}
}

            String revision = p.getProperty(PkgProps.PKG_MAJOR_REV);
if (revision != null) {
return revision;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterChooserDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterChooserDialog.java
//Synthetic comment -- index 55e7db2..a41a952 100755

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.IAndroidVersionProvider;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.packages.FullRevision;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.ui.GridDialog;
//Synthetic comment -- @@ -423,8 +424,8 @@
if (aOld != null) {
Package pOld = aOld.getParentPackage();

            FullRevision rOld = pOld.getRevision();
            FullRevision rNew = pNew.getRevision();

boolean showRev = true;

//Synthetic comment -- @@ -435,17 +436,17 @@

if (!vOld.equals(vNew)) {
// Versions are different, so indicate more than just the revision.
                    addText(String.format("This update will replace API %1$s revision %2$s with API %3$s revision %4$s.\n\n",
                            vOld.getApiString(), rOld.toShortString(),
                            vNew.getApiString(), rNew.toShortString()));
showRev = false;
}
}

if (showRev) {
                addText(String.format("This update will replace revision %1$s with revision %2$s.\n\n",
                        rOld.toShortString(),
                        rNew.toShortString()));
}
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterLogic.java
//Synthetic comment -- index 97de564..d5ad456 100755

//Synthetic comment -- @@ -23,12 +23,14 @@
import com.android.sdklib.internal.repository.packages.AddonPackage;
import com.android.sdklib.internal.repository.packages.DocPackage;
import com.android.sdklib.internal.repository.packages.ExtraPackage;
import com.android.sdklib.internal.repository.packages.FullRevision;
import com.android.sdklib.internal.repository.packages.IAndroidVersionProvider;
import com.android.sdklib.internal.repository.packages.IExactApiLevelDependency;
import com.android.sdklib.internal.repository.packages.IMinApiLevelDependency;
import com.android.sdklib.internal.repository.packages.IMinPlatformToolsDependency;
import com.android.sdklib.internal.repository.packages.IMinToolsDependency;
import com.android.sdklib.internal.repository.packages.IPlatformDependency;
import com.android.sdklib.internal.repository.packages.MajorRevision;
import com.android.sdklib.internal.repository.packages.MinToolsPackage;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.packages.Package.UpdateInfo;
//Synthetic comment -- @@ -184,6 +186,14 @@
return archives;
}

    private double getRevisionRank(FullRevision rev) {
        int p = rev.isPreview() ? 999 : 999 - rev.getPreview();
        return  rev.getMajor() +
                rev.getMinor() / 1000.d +
                rev.getMicro() / 1000000.d +
                p              / 1000000000.d;
    }

/**
* Finds new packages that the user does not have in his/her local SDK
* and adds them to the list of archives to install.
//Synthetic comment -- @@ -212,15 +222,15 @@
ArchiveInfo[] localArchives = createLocalArchives(localPkgs);

// Find the highest platform installed
        double currentPlatformScore = 0;
        double currentSampleScore = 0;
        double currentAddonScore = 0;
        double currentDocScore = 0;
        HashMap<String, Double> currentExtraScore = new HashMap<String, Double>();
if (!includeAll) {
if (localPkgs != null) {
for (Package p : localPkgs) {
                    double rev = getRevisionRank(p.getRevision());
int api = 0;
boolean isPreview = false;
if (p instanceof IAndroidVersionProvider) {
//Synthetic comment -- @@ -229,10 +239,10 @@
isPreview = vers.isPreview();
}

                    // The score is 1000*api + (999 if preview) + rev
// This allows previews to rank above a non-preview and
// allows revisions to rank appropriately.
                    double score = api * 1000 + (isPreview ? 999 : 0) + rev;

if (p instanceof PlatformPackage) {
currentPlatformScore = Math.max(currentPlatformScore, score);
//Synthetic comment -- @@ -261,7 +271,7 @@
continue;
}

            double rev = getRevisionRank(p.getRevision());
int api = 0;
boolean isPreview = false;
if (p instanceof IAndroidVersionProvider) {
//Synthetic comment -- @@ -270,7 +280,7 @@
isPreview = vers.isPreview();
}

            double score = api * 1000 + (isPreview ? 999 : 0) + rev;

boolean shouldAdd = false;
if (p instanceof PlatformPackage) {
//Synthetic comment -- @@ -282,7 +292,7 @@
} else if (p instanceof ExtraPackage) {
String key = ((ExtraPackage) p).getPath();
shouldAdd = !currentExtraScore.containsKey(key) ||
                    score > currentExtraScore.get(key).doubleValue();
} else if (p instanceof DocPackage) {
// We don't want all the doc, only the most recent one
if (score > currentDocScore) {
//Synthetic comment -- @@ -673,20 +683,22 @@
SdkSource[] remoteSources,
ArchiveInfo[] localArchives) {
// This is the requirement to match.
        int revInt = pkg.getMinToolsRevision();         // FIXME support micro min-tools-rev

        if (revInt == MinToolsPackage.MIN_TOOLS_REV_NOT_SPECIFIED) {
// Well actually there's no requirement.
return null;
}

        MajorRevision rev = new MajorRevision(revInt);

// First look in locally installed packages.
for (ArchiveInfo ai : localArchives) {
Archive a = ai.getNewArchive();
if (a != null) {
Package p = a.getParentPackage();
if (p instanceof ToolPackage) {
                    if (((ToolPackage) p).getRevision().compareTo(rev) >= 0) {
// We found one already installed.
return null;
}
//Synthetic comment -- @@ -700,7 +712,7 @@
if (a != null) {
Package p = a.getParentPackage();
if (p instanceof ToolPackage) {
                    if (((ToolPackage) p).getRevision().compareTo(rev) >= 0) {
// The dependency is already scheduled for install, nothing else to do.
return ai;
}
//Synthetic comment -- @@ -713,7 +725,7 @@
for (Archive a : selectedArchives) {
Package p = a.getParentPackage();
if (p instanceof ToolPackage) {
                    if (((ToolPackage) p).getRevision().compareTo(rev) >= 0) {
// It's not already in the list of things to install, so add it now
return insertArchive(a,
outArchives,
//Synthetic comment -- @@ -731,7 +743,7 @@
fetchRemotePackages(remotePkgs, remoteSources);
for (Package p : remotePkgs) {
if (p instanceof ToolPackage) {
                if (((ToolPackage) p).getRevision().compareTo(rev) >= 0) {
// It's not already in the list of things to install, so add the
// first compatible archive we can find.
for (Archive a : p.getArchives()) {
//Synthetic comment -- @@ -752,7 +764,7 @@
// We end up here if nothing matches. We don't have a good platform to match.
// We need to indicate this extra depends on a missing platform archive
// so that it can be impossible to install later on.
        return new MissingArchiveInfo(MissingArchiveInfo.TITLE_TOOL, revInt);
}

/**
//Synthetic comment -- @@ -771,12 +783,13 @@
SdkSource[] remoteSources,
ArchiveInfo[] localArchives) {
// This is the requirement to match.
        int revInt = pkg.getMinPlatformToolsRevision(); // FIXME support micro min-plat-tools-rev
        FullRevision rev = new MajorRevision(revInt);
boolean findMax = false;
ArchiveInfo aiMax = null;
Archive aMax = null;

        if (revInt == IMinPlatformToolsDependency.MIN_PLATFORM_TOOLS_REV_INVALID) {
// The requirement is invalid, which is not supposed to happen since this
// property is mandatory. However in a typical upgrade scenario we can end
// up with the previous updater managing a new package and not dealing
//Synthetic comment -- @@ -792,11 +805,11 @@
if (a != null) {
Package p = a.getParentPackage();
if (p instanceof PlatformToolPackage) {
                    FullRevision r = ((PlatformToolPackage) p).getRevision();
                    if (findMax && r.compareTo(rev) > 0) {
rev = r;
aiMax = ai;
                    } else if (!findMax && r.compareTo(rev) >= 0) {
// We found one already installed.
return null;
}
//Synthetic comment -- @@ -810,11 +823,11 @@
if (a != null) {
Package p = a.getParentPackage();
if (p instanceof PlatformToolPackage) {
                    FullRevision r = ((PlatformToolPackage) p).getRevision();
                    if (findMax && r.compareTo(rev) > 0) {
rev = r;
aiMax = ai;
                    } else if (!findMax && r.compareTo(rev) >= 0) {
// The dependency is already scheduled for install, nothing else to do.
return ai;
}
//Synthetic comment -- @@ -827,12 +840,12 @@
for (Archive a : selectedArchives) {
Package p = a.getParentPackage();
if (p instanceof PlatformToolPackage) {
                    FullRevision r = ((PlatformToolPackage) p).getRevision();
                    if (findMax && r.compareTo(rev) > 0) {
rev = r;
aiMax = null;
aMax = a;
                    } else if (!findMax && r.compareTo(rev) >= 0) {
// It's not already in the list of things to install, so add it now
return insertArchive(a,
outArchives,
//Synthetic comment -- @@ -850,16 +863,16 @@
fetchRemotePackages(remotePkgs, remoteSources);
for (Package p : remotePkgs) {
if (p instanceof PlatformToolPackage) {
                FullRevision r = ((PlatformToolPackage) p).getRevision();
                if (r.compareTo(rev) >= 0) {
// Make sure there's at least one valid archive here
for (Archive a : p.getArchives()) {
if (a.isCompatible()) {
                            if (findMax && r.compareTo(rev) > 0) {
rev = r;
aiMax = null;
aMax = a;
                            } else if (!findMax && r.compareTo(rev) >= 0) {
// It's not already in the list of things to install, so add the
// first compatible archive we can find.
return insertArchive(a,
//Synthetic comment -- @@ -893,7 +906,7 @@
// We end up here if nothing matches. We don't have a good platform to match.
// We need to indicate this package depends on a missing platform archive
// so that it can be impossible to install later on.
        return new MissingArchiveInfo(MissingArchiveInfo.TITLE_PLATFORM_TOOL, revInt);
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackagesDiffLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackagesDiffLogic.java
//Synthetic comment -- index 687238b..8e1cbb3 100755

//Synthetic comment -- @@ -21,7 +21,9 @@
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.repository.packages.ExtraPackage;
import com.android.sdklib.internal.repository.packages.IAndroidVersionProvider;
import com.android.sdklib.internal.repository.packages.IFullRevisionProvider;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.packages.Package.UpdateInfo;
import com.android.sdklib.internal.repository.packages.PlatformPackage;
import com.android.sdklib.internal.repository.packages.PlatformToolPackage;
import com.android.sdklib.internal.repository.packages.SystemImagePackage;
//Synthetic comment -- @@ -80,11 +82,12 @@
/**
* Mark all new and update PkgItems as checked.
*
     * @param selectNew If true, select all new packages (except the rc/preview ones).
     * @param selectUpdates If true, select all update packages.
     * @param selectTop If true, select the top platform.
     *   If the top platform has nothing installed, select all items in it (except the rc/preview);
     *   If it is partially installed, at least select the platform and system images if none of
     *   the system images are installed.
* @param currentPlatform The {@link SdkConstants#currentPlatform()} value.
*/
public void checkNewUpdateItems(
//Synthetic comment -- @@ -97,7 +100,8 @@
SparseArray<List<PkgItem>> platformItems = new SparseArray<List<PkgItem>>();

// sort items in platforms... directly deal with new/update items
        List<PkgItem> allItems = getAllPkgItems(true /*byApi*/, true /*bySource*/);
        for (PkgItem item : allItems) {
if (!item.hasCompatibleArchive()) {
// Ignore items that have no archive compatible with the current platform.
continue;
//Synthetic comment -- @@ -129,8 +133,42 @@
items.add(item);
}

            if ((selectUpdates || selectNew) &&
                    item.getState() == PkgState.NEW &&
                    !item.getRevision().isPreview()) {
                boolean sameFound = false;
                Package newPkg = item.getMainPackage();
                if (newPkg instanceof IFullRevisionProvider) {
                    // We have a potential new non-preview package; but this kind of package
                    // supports having previews, which means we want to make sure we're not
                    // offering an older "new" non-preview if there's a newer preview installed.
                    //
                    // We should get into this odd situation only when updating an RC/preview
                    // by a final release pkg.

                    IFullRevisionProvider newPkg2 = (IFullRevisionProvider) newPkg;
                    for (PkgItem item2 : allItems) {
                        if (item2.getState() == PkgState.INSTALLED) {
                            Package installed = item2.getMainPackage();

                            if (installed.getRevision().isPreview() &&
                                    newPkg2.sameItemAs(installed, true /*ignorePreviews*/)) {
                                sameFound = true;

                                if (installed.canBeUpdatedBy(newPkg) == UpdateInfo.UPDATE) {
                                    item.setChecked(true);
                                    break;
                                }
                            }
                        }
                    }
                }

                if (selectNew && !sameFound) {
                    item.setChecked(true);
                }

            } else if (selectUpdates && item.hasUpdatePkg()) {
item.setChecked(true);
}
}
//Synthetic comment -- @@ -140,7 +178,8 @@
if (!installedPlatforms.contains(maxApi)) {
// If the top platform has nothing installed at all, select everything in it
for (PkgItem item : items) {
                    if ((item.getState() == PkgState.NEW && !item.getRevision().isPreview()) ||
                            item.hasUpdatePkg()) {
item.setChecked(true);
}
}
//Synthetic comment -- @@ -151,7 +190,8 @@
// First make sure the platform package itself is installed, or select it.
for (PkgItem item : items) {
Package p = item.getMainPackage();
                     if (p instanceof PlatformPackage &&
                             item.getState() == PkgState.NEW && !item.getRevision().isPreview()) {
item.setChecked(true);
break;
}
//Synthetic comment -- @@ -163,7 +203,7 @@
Package p = item.getMainPackage();
if (p instanceof PlatformPackage && item.getState() == PkgState.INSTALLED) {
if (item.hasUpdatePkg() && item.isChecked()) {
                            // If the installed platform is scheduled for update, look for the
// system image in the update package, not the current one.
p = item.getUpdatePkg();
if (p instanceof PlatformPackage) {
//Synthetic comment -- @@ -190,6 +230,7 @@
Package p = item.getMainPackage();
if (p instanceof PlatformPackage) {
if (item.getState() == PkgState.NEW &&
                                     !item.getRevision().isPreview() &&
((PlatformPackage) p).getIncludedAbi() != null) {
item.setChecked(true);
hasSysImg = true;
//Synthetic comment -- @@ -220,7 +261,9 @@
// On Windows, we'll also auto-select the USB driver
for (PkgItem item : getAllPkgItems(true /*byApi*/, true /*bySource*/)) {
Package p = item.getMainPackage();
                if (p instanceof ExtraPackage &&
                        item.getState() == PkgState.NEW &&
                        !item.getRevision().isPreview()) {
ExtraPackage ep = (ExtraPackage) p;
if (ep.getVendorId().equals("google") &&            //$NON-NLS-1$
ep.getPath().equals("usb_driver")) {        //$NON-NLS-1$
//Synthetic comment -- @@ -519,7 +562,7 @@

switch (currItem.getState()) {
case NEW:
                            if (newPkg.getRevision().compareTo(mainPkg.getRevision()) < 0) {
if (!op.isKeep(currItem)) {
// The new item has a lower revision than the current one,
// but the current one hasn't been marked as being kept so
//Synthetic comment -- @@ -528,7 +571,7 @@
addNewItem(op, newPkg, PkgState.NEW);
hasChanged = true;
}
                            } else if (newPkg.getRevision().compareTo(mainPkg.getRevision()) > 0) {
// We have a more recent new version, remove the current one
// and replace by a new one
currItemIt.remove();
//Synthetic comment -- @@ -538,7 +581,7 @@
break;
case INSTALLED:
// if newPkg.revision<=mainPkg.revision: it's already installed, ignore.
                            if (newPkg.getRevision().compareTo(mainPkg.getRevision()) > 0) {
// This is a new update for the main package.
if (currItem.mergeUpdate(newPkg)) {
op.keep(currItem.getUpdatePkg());
//Synthetic comment -- @@ -611,8 +654,11 @@
return ((IAndroidVersionProvider) pkg).getAndroidVersion();

} else if (pkg instanceof ToolPackage || pkg instanceof PlatformToolPackage) {
                if (pkg.getRevision().isPreview()) {
                    return PkgCategoryApi.KEY_TOOLS_PREVIEW;
                } else {
                    return PkgCategoryApi.KEY_TOOLS;
                }
} else {
return PkgCategoryApi.KEY_EXTRA;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackagesPage.java
//Synthetic comment -- index ea02e3a..51543e8 100755

//Synthetic comment -- @@ -1313,7 +1313,7 @@
} else if (mColumn == mColumnRevision) {
if (element instanceof PkgItem) {
PkgItem pkg = (PkgItem) element;
                    return pkg.getRevision().toShortString();
}

} else if (mColumn == mColumnStatus) {
//Synthetic comment -- @@ -1343,7 +1343,7 @@

} else if (element instanceof Package) {
// This is an update package.
                    return "New revision " + ((Package) element).getRevision().toShortString();
}
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PkgCategoryApi.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PkgCategoryApi.java
//Synthetic comment -- index 5bb4917..d1e3a28 100755

//Synthetic comment -- @@ -31,7 +31,9 @@
// them.
// (Note: don't use integer.max to avoid integers wrapping in comparisons. We can
// revisit the day we get 2^30 platforms.)
    public final static AndroidVersion KEY_TOOLS = new AndroidVersion(Integer.MAX_VALUE / 2, null);
    public final static AndroidVersion KEY_TOOLS_PREVIEW =
                                               new AndroidVersion(Integer.MAX_VALUE / 2 - 1, null);
public final static AndroidVersion KEY_EXTRA = new AndroidVersion(-1, null);

public PkgCategoryApi(AndroidVersion version, String platformName, Object iconRef) {
//Synthetic comment -- @@ -55,6 +57,8 @@
AndroidVersion key = (AndroidVersion) getKey();
if (key.equals(KEY_TOOLS)) {
return "TOOLS";             //$NON-NLS-1$ // for internal debug use only
        } else if (key.equals(KEY_TOOLS_PREVIEW)) {
                return "TOOLS-PREVIEW"; //$NON-NLS-1$ // for internal debug use only
} else if (key.equals(KEY_EXTRA)) {
return "EXTRAS";            //$NON-NLS-1$ // for internal debug use only
} else {
//Synthetic comment -- @@ -70,6 +74,8 @@

if (key.equals(KEY_TOOLS)) {
label = "Tools";
            } else if (key.equals(KEY_TOOLS_PREVIEW)) {
                label = "Tools (Beta Channel)";
} else if (key.equals(KEY_EXTRA)) {
label = "Extras";
} else {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PkgItem.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PkgItem.java
//Synthetic comment -- index ae20deb..89910cd 100755

//Synthetic comment -- @@ -17,8 +17,8 @@
package com.android.sdkuilib.internal.repository.sdkman2;

import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.FullRevision;
import com.android.sdklib.internal.repository.packages.IAndroidVersionProvider;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.packages.Package.UpdateInfo;
import com.android.sdklib.internal.repository.sources.SdkSource;
//Synthetic comment -- @@ -90,17 +90,10 @@
return mMainPkg.getListDescription();
}

    public FullRevision getRevision() {
return mMainPkg.getRevision();
}

public String getDescription() {
return mMainPkg.getDescription();
}
//Synthetic comment -- @@ -157,24 +150,24 @@

/**
* Checks whether the main packages are of the same type and are
     * not an update of each other and have the same revision number.
*/
public boolean isSameMainPackageAs(Package pkg) {
if (mMainPkg.canBeUpdatedBy(pkg) == UpdateInfo.NOT_UPDATE) {
// package revision numbers must match
            return mMainPkg.getRevision().equals(pkg.getRevision());
}
return false;
}

/**
* Checks whether the update packages are of the same type and are
     * not an update of each other and have the same revision numbers.
*/
public boolean isSameUpdatePackageAs(Package pkg) {
if (mUpdatePkg != null && mUpdatePkg.canBeUpdatedBy(pkg) == UpdateInfo.NOT_UPDATE) {
// package revision numbers must match
            return mUpdatePkg.getRevision().equals(pkg.getRevision());
}
return false;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/sdkman2/PackagesDiffLogicTest.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/sdkman2/PackagesDiffLogicTest.java
//Synthetic comment -- index a328ef1..26b39ed 100755

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.repository.packages.BrokenPackage;
import com.android.sdklib.internal.repository.packages.FullRevision;
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
SdkSource src1 = new SdkRepoSource("http://1.example.com/url1", "repo1");
SdkSource src2 = new SdkRepoSource("http://2.example.com/url2", "repo2");
MockPlatformPackage p1;
//Synthetic comment -- @@ -1427,6 +1425,327 @@
"-- <NEW, pkg:The addon B from vendor 1, Android API 1, revision 6>\n",
getTree(m, false /*displaySortByApi*/));
}

    public void testToolsMinorUpdate() {
        // Test: Check a minor revision updates an installed major revision.

        SdkSource src1 = new SdkRepoSource("http://1.example.com/url1", "repo1");

        m.updateStart();
        m.updateSourcePackages(true /*sortByApi*/, null /*locals*/, new Package[] {
                new MockToolPackage(3, 3),                                          // Tools 3.0.0
                new MockPlatformToolPackage(src1, 3),
        });
        m.updateSourcePackages(true /*sortByApi*/, src1, new Package[] {
                new MockToolPackage(src1, new FullRevision(3, 0, 1), 3),          // Tools 3.0.1
        });
        m.updateEnd(true /*sortByApi*/);

        assertEquals(
                "PkgCategoryApi <API=TOOLS, label=Tools, #items=2>\n" +
                "-- <INSTALLED, pkg:Android SDK Tools, revision 3, updated by:Android SDK Tools, revision 3.0.1>\n" +
                "-- <INSTALLED, pkg:Android SDK Platform-tools, revision 3>\n" +
                "PkgCategoryApi <API=EXTRAS, label=Extras, #items=0>\n",
                getTree(m, true /*displaySortByApi*/));
        assertEquals(
                "PkgCategorySource <source=Local Packages (no.source), #items=1>\n" +
                "-- <INSTALLED, pkg:Android SDK Tools, revision 3, updated by:Android SDK Tools, revision 3.0.1>\n" +
                "PkgCategorySource <source=repo1 (1.example.com), #items=1>\n" +
                "-- <INSTALLED, pkg:Android SDK Platform-tools, revision 3>\n",
                getTree(m, false /*displaySortByApi*/));
    }

    public void testToolsPreviews() {
        // Test: No local tools installed. The remote server has both tools and platforms
        // in release and RC versions.

        SdkSource src1 = new SdkRepoSource("http://1.example.com/url1", "repo1");

        m.updateStart();
        m.updateSourcePackages(true /*sortByApi*/, src1, new Package[] {
                new MockToolPackage(src1, new FullRevision(2, 0, 0), 3),          // Tools 2
                new MockToolPackage(src1, new FullRevision(4, 0, 0, 1), 3),       // Tools 4 rc1
                new MockPlatformToolPackage(src1, new FullRevision(3, 0, 0)),     // Plat-T 3
                new MockPlatformToolPackage(src1, new FullRevision(5, 0, 0, 1)),  // Plat-T 5 rc1
        });
        m.updateEnd(true /*sortByApi*/);

        assertEquals(
                "PkgCategoryApi <API=TOOLS, label=Tools, #items=2>\n" +
                "-- <NEW, pkg:Android SDK Tools, revision 2>\n" +
                "-- <NEW, pkg:Android SDK Platform-tools, revision 3>\n" +
                "PkgCategoryApi <API=TOOLS-PREVIEW, label=Tools (Beta Channel), #items=2>\n" +
                "-- <NEW, pkg:Android SDK Tools, revision 4 rc1>\n" +
                "-- <NEW, pkg:Android SDK Platform-tools, revision 5 rc1>\n" +
                "PkgCategoryApi <API=EXTRAS, label=Extras, #items=0>\n",
                getTree(m, true /*displaySortByApi*/));
        assertEquals(
                "PkgCategorySource <source=repo1 (1.example.com), #items=4>\n" +
                "-- <NEW, pkg:Android SDK Tools, revision 2>\n" +
                "-- <NEW, pkg:Android SDK Tools, revision 4 rc1>\n" +
                "-- <NEW, pkg:Android SDK Platform-tools, revision 3>\n" +
                "-- <NEW, pkg:Android SDK Platform-tools, revision 5 rc1>\n",
                getTree(m, false /*displaySortByApi*/));
    }

    public void testPreviewUpdateInstalledRelease() {
        // Test: Local release Tools 3.0.0 installed, server has both a release 3.0.1 available
        // and a Tools Preview 4.0.0 rc1 available.
        // => v3 is updated by 3.0.1
        // => v4.0.0rc1 does not update 3.0.0, instead it's a separate download.

        SdkSource src1 = new SdkRepoSource("http://1.example.com/url1", "repo1");

        m.updateStart();
        m.updateSourcePackages(true /*sortByApi*/, null /*locals*/, new Package[] {
                new MockToolPackage(3, 3),    // tool package has no source defined
                new MockPlatformToolPackage(src1, 3),
                new MockPlatformPackage(src1, 1, 2, 3),    // API 1
        });
        m.updateSourcePackages(true /*sortByApi*/, src1, new Package[] {
                new MockToolPackage(src1, 3, 3),                                  // Tools 3
                new MockToolPackage(src1, new FullRevision(3, 0, 1), 3),          // Tools 3.0.1
                new MockToolPackage(src1, new FullRevision(4, 0, 0, 1), 3),       // Tools 4 rc1
                new MockPlatformToolPackage(src1, new FullRevision(3, 0, 1)),     // PT    3.0.1
                new MockPlatformToolPackage(src1, new FullRevision(4, 0, 0, 1)),  // PT    4 rc1
        });
        m.updateEnd(true /*sortByApi*/);

        assertEquals(
                "PkgCategoryApi <API=TOOLS, label=Tools, #items=2>\n" +
                "-- <INSTALLED, pkg:Android SDK Tools, revision 3, updated by:Android SDK Tools, revision 3.0.1>\n" +
                "-- <INSTALLED, pkg:Android SDK Platform-tools, revision 3, updated by:Android SDK Platform-tools, revision 3.0.1>\n" +
                "PkgCategoryApi <API=TOOLS-PREVIEW, label=Tools (Beta Channel), #items=2>\n" +
                "-- <NEW, pkg:Android SDK Tools, revision 4 rc1>\n" +
                "-- <NEW, pkg:Android SDK Platform-tools, revision 4 rc1>\n" +
                "PkgCategoryApi <API=API 1, label=Android android-1 (API 1), #items=1>\n" +
                "-- <INSTALLED, pkg:SDK Platform Android android-1, API 1, revision 2>\n" +
                "PkgCategoryApi <API=EXTRAS, label=Extras, #items=0>\n",
                getTree(m, true /*displaySortByApi*/));
        assertEquals(
                "PkgCategorySource <source=Local Packages (no.source), #items=1>\n" +
                "-- <INSTALLED, pkg:Android SDK Tools, revision 3, updated by:Android SDK Tools, revision 3.0.1>\n" +
                "PkgCategorySource <source=repo1 (1.example.com), #items=4>\n" +
                "-- <NEW, pkg:Android SDK Tools, revision 4 rc1>\n" +
                "-- <INSTALLED, pkg:Android SDK Platform-tools, revision 3, updated by:Android SDK Platform-tools, revision 3.0.1>\n" +
                "-- <NEW, pkg:Android SDK Platform-tools, revision 4 rc1>\n" +
                "-- <INSTALLED, pkg:SDK Platform Android android-1, API 1, revision 2>\n",
                getTree(m, false /*displaySortByApi*/));

        // Now request to check new items and updates:
        // Tools 4 rc1 is greater than the installed Tools 3, but it's a preview so we will NOT
        //   auto-select it by default even though we requested to select "NEW" packages. We
        //   want the user to manually opt-in into the rc/preview package.
        // However Tools 3 has a 3.0.1 update that we'll auto-select.
        m.checkNewUpdateItems(true, true, false, SdkConstants.PLATFORM_LINUX);

        assertEquals(
                "PkgCategoryApi <API=TOOLS, label=Tools, #items=2>\n" +
                "-- < * INSTALLED, pkg:Android SDK Tools, revision 3, updated by:Android SDK Tools, revision 3.0.1>\n" +
                "-- < * INSTALLED, pkg:Android SDK Platform-tools, revision 3, updated by:Android SDK Platform-tools, revision 3.0.1>\n" +
                "PkgCategoryApi <API=TOOLS-PREVIEW, label=Tools (Beta Channel), #items=2>\n" +
                "-- <NEW, pkg:Android SDK Tools, revision 4 rc1>\n" +
                "-- <NEW, pkg:Android SDK Platform-tools, revision 4 rc1>\n" +
                "PkgCategoryApi <API=API 1, label=Android android-1 (API 1), #items=1>\n" +
                "-- <INSTALLED, pkg:SDK Platform Android android-1, API 1, revision 2>\n" +
                "PkgCategoryApi <API=EXTRAS, label=Extras, #items=0>\n",
                getTree(m, true /*displaySortByApi*/));
        assertEquals(
                "PkgCategorySource <source=Local Packages (no.source), #items=1>\n" +
                "-- < * INSTALLED, pkg:Android SDK Tools, revision 3, updated by:Android SDK Tools, revision 3.0.1>\n" +
                "PkgCategorySource <source=repo1 (1.example.com), #items=4>\n" +
                "-- <NEW, pkg:Android SDK Tools, revision 4 rc1>\n" +
                "-- < * INSTALLED, pkg:Android SDK Platform-tools, revision 3, updated by:Android SDK Platform-tools, revision 3.0.1>\n" +
                "-- <NEW, pkg:Android SDK Platform-tools, revision 4 rc1>\n" +
                "-- <INSTALLED, pkg:SDK Platform Android android-1, API 1, revision 2>\n",
                getTree(m, false /*displaySortByApi*/));

    }

    public void testPreviewUpdateInstalledPreview() {
        // Test: Local preview Tools 3.0.1rc1 installed, server has both a release 3.0.0 available
        // and a Tools Preview 3.0.1 rc2 available.
        // => Installed 3.0.1rc1 can be updated by 3.0.1rc2
        // => There's a separate "new" download for 3.0.0, not installed and NOT updating 3.0.1rc1.

        SdkSource src1 = new SdkRepoSource("http://1.example.com/url1", "repo1");

        m.updateStart();
        m.updateSourcePackages(true /*sortByApi*/, null /*locals*/, new Package[] {
                new MockToolPackage(src1, new FullRevision(3, 0, 1, 1), 4),       //  T 3.0.1rc1
                new MockPlatformToolPackage(src1, new FullRevision(4, 0, 1, 1)),  // PT 4.0.1rc1
                new MockPlatformPackage(src1, 1, 2, 3),    // API 1
        });
        m.updateSourcePackages(true /*sortByApi*/, src1, new Package[] {
                new MockToolPackage(src1, new FullRevision(3, 0, 0), 4),          //  T 3.0.0
                new MockToolPackage(src1, new FullRevision(3, 0, 1, 2), 4),       //  T 3.0.1rc2
                new MockPlatformToolPackage(src1, new FullRevision(4, 0, 0)),     // PT 4.0.0
                new MockPlatformToolPackage(src1, new FullRevision(4, 0, 1, 2)),  // PT 4.0.1 rc2
        });
        m.updateEnd(true /*sortByApi*/);

        assertEquals(
                "PkgCategoryApi <API=TOOLS, label=Tools, #items=2>\n" +
                "-- <NEW, pkg:Android SDK Tools, revision 3>\n" +
                "-- <NEW, pkg:Android SDK Platform-tools, revision 4>\n" +
                "PkgCategoryApi <API=TOOLS-PREVIEW, label=Tools (Beta Channel), #items=2>\n" +
                "-- <INSTALLED, pkg:Android SDK Tools, revision 3.0.1 rc1, updated by:Android SDK Tools, revision 3.0.1 rc2>\n" +
                "-- <INSTALLED, pkg:Android SDK Platform-tools, revision 4.0.1 rc1, updated by:Android SDK Platform-tools, revision 4.0.1 rc2>\n" +
                "PkgCategoryApi <API=API 1, label=Android android-1 (API 1), #items=1>\n" +
                "-- <INSTALLED, pkg:SDK Platform Android android-1, API 1, revision 2>\n" +
                "PkgCategoryApi <API=EXTRAS, label=Extras, #items=0>\n",
                getTree(m, true /*displaySortByApi*/));
        assertEquals(
                "PkgCategorySource <source=repo1 (1.example.com), #items=5>\n" +
                "-- <NEW, pkg:Android SDK Tools, revision 3>\n" +
                "-- <INSTALLED, pkg:Android SDK Tools, revision 3.0.1 rc1, updated by:Android SDK Tools, revision 3.0.1 rc2>\n" +
                "-- <NEW, pkg:Android SDK Platform-tools, revision 4>\n" +
                "-- <INSTALLED, pkg:Android SDK Platform-tools, revision 4.0.1 rc1, updated by:Android SDK Platform-tools, revision 4.0.1 rc2>\n" +
                "-- <INSTALLED, pkg:SDK Platform Android android-1, API 1, revision 2>\n",
                getTree(m, false /*displaySortByApi*/));

        // Auto select new and update items. In this case:
        // - the previews have updates available.
        // - we're not selecting the non-installed "3.0" version that is older than the
        //   currently installed "3.0.1rc1" version since that would be a downgrade.
        m.checkNewUpdateItems(true, true, false, SdkConstants.PLATFORM_LINUX);

        assertEquals(
                "PkgCategoryApi <API=TOOLS, label=Tools, #items=2>\n" +
                "-- <NEW, pkg:Android SDK Tools, revision 3>\n" +
                "-- <NEW, pkg:Android SDK Platform-tools, revision 4>\n" +
                "PkgCategoryApi <API=TOOLS-PREVIEW, label=Tools (Beta Channel), #items=2>\n" +
                "-- < * INSTALLED, pkg:Android SDK Tools, revision 3.0.1 rc1, updated by:Android SDK Tools, revision 3.0.1 rc2>\n" +
                "-- < * INSTALLED, pkg:Android SDK Platform-tools, revision 4.0.1 rc1, updated by:Android SDK Platform-tools, revision 4.0.1 rc2>\n" +
                "PkgCategoryApi <API=API 1, label=Android android-1 (API 1), #items=1>\n" +
                "-- <INSTALLED, pkg:SDK Platform Android android-1, API 1, revision 2>\n" +
                "PkgCategoryApi <API=EXTRAS, label=Extras, #items=0>\n",
                getTree(m, true /*displaySortByApi*/));
        assertEquals(
                "PkgCategorySource <source=repo1 (1.example.com), #items=5>\n" +
                "-- <NEW, pkg:Android SDK Tools, revision 3>\n" +
                "-- < * INSTALLED, pkg:Android SDK Tools, revision 3.0.1 rc1, updated by:Android SDK Tools, revision 3.0.1 rc2>\n" +
                "-- <NEW, pkg:Android SDK Platform-tools, revision 4>\n" +
                "-- < * INSTALLED, pkg:Android SDK Platform-tools, revision 4.0.1 rc1, updated by:Android SDK Platform-tools, revision 4.0.1 rc2>\n" +
                "-- <INSTALLED, pkg:SDK Platform Android android-1, API 1, revision 2>\n",
                getTree(m, false /*displaySortByApi*/));

        // -----

        // Now simulate that the server has a final package (3.0.1) to replace the
        // installed 3.0.1rc1 package. It's not installed yet, just available.
        // - A new 3.0.1 will be available.
        // - The server no longer lists the RC since there's a final package, yet it is
        //   still locally installed.
        // - The 3.0.1 rc1 is not listed as having an update, since we treat the previews
        //   separately. TODO: consider having the 3.0.1 show up as both a new item /and/
        //   as an update to the 3.0.1rc1. That may have some other side effects.

        m.uncheckAllItems();
        m.updateStart();
        m.updateSourcePackages(true /*sortByApi*/, null /*locals*/, new Package[] {
                new MockToolPackage(src1, new FullRevision(3, 0, 1, 1), 4),       //  T 3.0.1rc1
                new MockPlatformToolPackage(src1, new FullRevision(4, 0, 1, 1)),  // PT 4.0.1rc1
                new MockPlatformPackage(src1, 1, 2, 3),    // API 1
        });
        m.updateSourcePackages(true /*sortByApi*/, src1, new Package[] {
                new MockToolPackage(src1, new FullRevision(3, 0, 1), 4),          //  T 3.0.1
                new MockPlatformToolPackage(src1, new FullRevision(4, 0, 1)),     // PT 4.0.1
        });
        m.updateEnd(true /*sortByApi*/);

        assertEquals(
                "PkgCategoryApi <API=TOOLS, label=Tools, #items=2>\n" +
                "-- <NEW, pkg:Android SDK Tools, revision 3.0.1>\n" +
                "-- <NEW, pkg:Android SDK Platform-tools, revision 4.0.1>\n" +
                "PkgCategoryApi <API=TOOLS-PREVIEW, label=Tools (Beta Channel), #items=2>\n" +
                "-- <INSTALLED, pkg:Android SDK Tools, revision 3.0.1 rc1>\n" +
                "-- <INSTALLED, pkg:Android SDK Platform-tools, revision 4.0.1 rc1>\n" +
                "PkgCategoryApi <API=API 1, label=Android android-1 (API 1), #items=1>\n" +
                "-- <INSTALLED, pkg:SDK Platform Android android-1, API 1, revision 2>\n" +
                "PkgCategoryApi <API=EXTRAS, label=Extras, #items=0>\n",
                getTree(m, true /*displaySortByApi*/));
        assertEquals(
                "PkgCategorySource <source=repo1 (1.example.com), #items=5>\n" +
                "-- <NEW, pkg:Android SDK Tools, revision 3.0.1>\n" +
                "-- <INSTALLED, pkg:Android SDK Tools, revision 3.0.1 rc1>\n" +
                "-- <NEW, pkg:Android SDK Platform-tools, revision 4.0.1>\n" +
                "-- <INSTALLED, pkg:Android SDK Platform-tools, revision 4.0.1 rc1>\n" +
                "-- <INSTALLED, pkg:SDK Platform Android android-1, API 1, revision 2>\n",
                getTree(m, false /*displaySortByApi*/));

        // Auto select new and update items. In this case the new items are considered
        // updates and yet new at the same time.
        // Test by selecting new items only:
        m.checkNewUpdateItems(true, false, false, SdkConstants.PLATFORM_LINUX);

        assertEquals(
                "PkgCategoryApi <API=TOOLS, label=Tools, #items=2>\n" +
                "-- < * NEW, pkg:Android SDK Tools, revision 3.0.1>\n" +
                "-- < * NEW, pkg:Android SDK Platform-tools, revision 4.0.1>\n" +
                "PkgCategoryApi <API=TOOLS-PREVIEW, label=Tools (Beta Channel), #items=2>\n" +
                "-- <INSTALLED, pkg:Android SDK Tools, revision 3.0.1 rc1>\n" +
                "-- <INSTALLED, pkg:Android SDK Platform-tools, revision 4.0.1 rc1>\n" +
                "PkgCategoryApi <API=API 1, label=Android android-1 (API 1), #items=1>\n" +
                "-- <INSTALLED, pkg:SDK Platform Android android-1, API 1, revision 2>\n" +
                "PkgCategoryApi <API=EXTRAS, label=Extras, #items=0>\n",
                getTree(m, true /*displaySortByApi*/));

        // Test by selecting update items only:
        m.uncheckAllItems();
        m.checkNewUpdateItems(false, true, false, SdkConstants.PLATFORM_LINUX);

        assertEquals(
                "PkgCategoryApi <API=TOOLS, label=Tools, #items=2>\n" +
                "-- < * NEW, pkg:Android SDK Tools, revision 3.0.1>\n" +
                "-- < * NEW, pkg:Android SDK Platform-tools, revision 4.0.1>\n" +
                "PkgCategoryApi <API=TOOLS-PREVIEW, label=Tools (Beta Channel), #items=2>\n" +
                "-- <INSTALLED, pkg:Android SDK Tools, revision 3.0.1 rc1>\n" +
                "-- <INSTALLED, pkg:Android SDK Platform-tools, revision 4.0.1 rc1>\n" +
                "PkgCategoryApi <API=API 1, label=Android android-1 (API 1), #items=1>\n" +
                "-- <INSTALLED, pkg:SDK Platform Android android-1, API 1, revision 2>\n" +
                "PkgCategoryApi <API=EXTRAS, label=Extras, #items=0>\n",
                getTree(m, true /*displaySortByApi*/));


        // -----

        // Now simulate that the user has installed the final package (3.0.1) to replace the
        // installed 3.0.1rc1 package.
        // - The 3.0.1 is installed.
        // - The 3.0.1 rc1 isn't listed anymore by the server.

        m.uncheckAllItems();
        m.updateStart();
        m.updateSourcePackages(true /*sortByApi*/, null /*locals*/, new Package[] {
                new MockToolPackage(src1, new FullRevision(3, 0, 1), 4),          //  T 3.0.1
                new MockPlatformToolPackage(src1, new FullRevision(4, 0, 1)),     // PT 4.0.1
                new MockPlatformPackage(src1, 1, 2, 3),    // API 1
        });
        m.updateSourcePackages(true /*sortByApi*/, src1, new Package[] {
                new MockToolPackage(src1, new FullRevision(3, 0, 1), 4),          //  T 3.0.1
                new MockPlatformToolPackage(src1, new FullRevision(4, 0, 1)),     // PT 4.0.1
        });
        m.updateEnd(true /*sortByApi*/);

        assertEquals(
                "PkgCategoryApi <API=TOOLS, label=Tools, #items=2>\n" +
                "-- <INSTALLED, pkg:Android SDK Tools, revision 3.0.1>\n" +
                "-- <INSTALLED, pkg:Android SDK Platform-tools, revision 4.0.1>\n" +
                "PkgCategoryApi <API=API 1, label=Android android-1 (API 1), #items=1>\n" +
                "-- <INSTALLED, pkg:SDK Platform Android android-1, API 1, revision 2>\n" +
                "PkgCategoryApi <API=EXTRAS, label=Extras, #items=0>\n",
                getTree(m, true /*displaySortByApi*/));
        assertEquals(
                "PkgCategorySource <source=repo1 (1.example.com), #items=3>\n" +
                "-- <INSTALLED, pkg:Android SDK Tools, revision 3.0.1>\n" +
                "-- <INSTALLED, pkg:Android SDK Platform-tools, revision 4.0.1>\n" +
                "-- <INSTALLED, pkg:SDK Platform Android android-1, API 1, revision 2>\n",
                getTree(m, false /*displaySortByApi*/));
    }



// ----

/**







