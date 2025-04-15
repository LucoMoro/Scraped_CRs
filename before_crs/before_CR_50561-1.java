/*SDK Manager: rework license display in install chooser dialog. DO NOT MERGE.

(Merge of master tools/base.git @ 8e18bfcfa0948de9d05de16e7f57d36119b40f78)

Change-Id:I5213faeb0acbf3f6f10a66f439af5288f37f2f68*/
//Synthetic comment -- diff --git a/manifmerger/src/main/java/com/android/manifmerger/MergerXmlUtils.java b/manifmerger/src/main/java/com/android/manifmerger/MergerXmlUtils.java
//Synthetic comment -- index bb60464..3e54cac 100755

//Synthetic comment -- @@ -504,10 +504,14 @@
String s1 = a1 == null ? "" : a1.getNodeName();           //$NON-NLS-1$
String s2 = a2 == null ? "" : a2.getNodeValue();          //$NON-NLS-1$

                int prio1 = s1.equals("name") ? 0 : 1;                    //$NON-NLS-1$
                int prio2 = s2.equals("name") ? 0 : 1;                    //$NON-NLS-1$
                if (prio1 == 0 || prio2 == 0) {
                    return prio1 - prio2;
}

return s1.compareTo(s2);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/Package.java b/sdkmanager/libs/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/Package.java
//Synthetic comment -- index feab109..8410ff7 100755

//Synthetic comment -- @@ -28,6 +28,7 @@
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.sources.SdkAddonSource;
import com.android.sdklib.internal.repository.sources.SdkRepoSource;
import com.android.sdklib.internal.repository.sources.SdkSource;
//Synthetic comment -- @@ -60,7 +61,7 @@
public abstract class Package implements IDescription, Comparable<Package> {

private final String mObsolete;
    private final String mLicense;
private final String mDescription;
private final String mDescUrl;
private final String mReleaseNote;
//Synthetic comment -- @@ -74,6 +75,92 @@
SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_DARWIN ||
SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_LINUX;


/**
* Enum for the result of {@link Package#canBeUpdatedBy(Package)}. This used so that we can
//Synthetic comment -- @@ -89,7 +176,7 @@
*  TODO: this name is confusing. We need to dig deeper. */
NOT_UPDATE,
/** Means that the 2 packages are the same thing, and one is the upgrade of the other */
        UPDATE;
}

/**
//Synthetic comment -- @@ -115,8 +202,8 @@
mObsolete    =
PackageParserUtils.getOptionalXmlString(packageNode, SdkRepoConstants.NODE_OBSOLETE);

        mLicense  = parseLicense(packageNode, licenses);
        mArchives = parseArchives(
PackageParserUtils.findChildElement(packageNode, SdkRepoConstants.NODE_ARCHIVES));
}

//Synthetic comment -- @@ -147,7 +234,8 @@
descUrl = "";
}

        mLicense     = getProperty(props, PkgProps.PKG_LICENSE,      license);
mDescription = getProperty(props, PkgProps.PKG_DESC,         description);
mDescUrl     = getProperty(props, PkgProps.PKG_DESC_URL,     descUrl);
mReleaseNote = getProperty(props, PkgProps.PKG_RELEASE_NOTE, "");       //$NON-NLS-1$
//Synthetic comment -- @@ -252,8 +340,15 @@
* These properties will later be give the constructor that takes a {@link Properties} object.
*/
public void saveProperties(Properties props) {
        if (mLicense != null && mLicense.length() > 0) {
            props.setProperty(PkgProps.PKG_LICENSE, mLicense);
}
if (mDescription != null && mDescription.length() > 0) {
props.setProperty(PkgProps.PKG_DESC, mDescription);
//Synthetic comment -- @@ -281,14 +376,14 @@
* definition if there's one. Returns null if there's no uses-license element or no
* license of this name defined.
*/
    private String parseLicense(Node packageNode, Map<String, String> licenses) {
Node usesLicense =
PackageParserUtils.findChildElement(packageNode, SdkRepoConstants.NODE_USES_LICENSE);
if (usesLicense != null) {
Node ref = usesLicense.getAttributes().getNamedItem(SdkRepoConstants.ATTR_REF);
if (ref != null) {
String licenseRef = ref.getNodeValue();
                return licenses.get(licenseRef);
}
}
return null;
//Synthetic comment -- @@ -361,7 +456,7 @@
* Returns the optional description for all packages (platform, add-on, tool, doc) or
* for a lib. It is null if the element has not been specified in the repository XML.
*/
    public String getLicense() {
return mLicense;
}

//Synthetic comment -- @@ -480,7 +575,7 @@
* Should not be empty. Must never be null.
* <p/>
* Note that this is the "base" name for the package
     * with no specific revision nor API mentionned.
* In contrast, {@link #getShortDescription()} should be used if you want more details
* such as the package revision number or the API, if applicable.
*/








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/main/java/com/android/sdklib/repository/PkgProps.java b/sdkmanager/libs/sdklib/src/main/java/com/android/sdklib/repository/PkgProps.java
//Synthetic comment -- index 68d7119..8f12432 100755

//Synthetic comment -- @@ -31,6 +31,7 @@
// Base Package
public static final String PKG_REVISION             = "Pkg.Revision";           //$NON-NLS-1$
public static final String PKG_LICENSE              = "Pkg.License";            //$NON-NLS-1$
public static final String PKG_DESC                 = "Pkg.Desc";               //$NON-NLS-1$
public static final String PKG_DESC_URL             = "Pkg.DescUrl";            //$NON-NLS-1$
public static final String PKG_RELEASE_NOTE         = "Pkg.RelNote";            //$NON-NLS-1$








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/PackageTest.java b/sdkmanager/libs/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/PackageTest.java
//Synthetic comment -- index f4d7b56..590f4d3 100755

//Synthetic comment -- @@ -156,7 +156,7 @@
protected void testCreatedPackage(Package p) {
// Package properties
assertEquals("42", p.getRevision().toShortString());
        assertEquals("The License", p.getLicense());
assertEquals("Some description.", p.getDescription());
assertEquals("http://description/url", p.getDescUrl());
assertEquals("Release Note", p.getReleaseNote());







