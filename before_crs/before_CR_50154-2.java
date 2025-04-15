/*SDK Manager: rework license display in install chooser dialog.

Change-Id:I4e4fa033e8543523cf0892079b06a273594e0d09*/
//Synthetic comment -- diff --git a/manifmerger/src/main/java/com/android/manifmerger/MergerXmlUtils.java b/manifmerger/src/main/java/com/android/manifmerger/MergerXmlUtils.java
//Synthetic comment -- index 9e15353..97291b2 100755

//Synthetic comment -- @@ -504,8 +504,10 @@

if (name1 && name2) {
return 0;
                } else if (name1 || name2) {
return -1;  // name is always first
} else {
return s1.compareTo(s2);
}








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/Package.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/Package.java
//Synthetic comment -- index 34f5e8a..777f219 100755

//Synthetic comment -- @@ -60,7 +60,7 @@
public abstract class Package implements IDescription, Comparable<Package> {

private final String mObsolete;
    private final String mLicense;
private final String mDescription;
private final String mDescUrl;
private final String mReleaseNote;
//Synthetic comment -- @@ -74,6 +74,52 @@
SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_DARWIN ||
SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_LINUX;


/**
* Enum for the result of {@link Package#canBeUpdatedBy(Package)}. This used so that we can
//Synthetic comment -- @@ -115,8 +161,8 @@
mObsolete    =
PackageParserUtils.getOptionalXmlString(packageNode, SdkRepoConstants.NODE_OBSOLETE);

        mLicense  = parseLicense(packageNode, licenses);
        mArchives = parseArchives(
PackageParserUtils.findChildElement(packageNode, SdkRepoConstants.NODE_ARCHIVES));
}

//Synthetic comment -- @@ -147,7 +193,8 @@
descUrl = "";
}

        mLicense     = getProperty(props, PkgProps.PKG_LICENSE,      license);
mDescription = getProperty(props, PkgProps.PKG_DESC,         description);
mDescUrl     = getProperty(props, PkgProps.PKG_DESC_URL,     descUrl);
mReleaseNote = getProperty(props, PkgProps.PKG_RELEASE_NOTE, "");       //$NON-NLS-1$
//Synthetic comment -- @@ -252,8 +299,15 @@
* These properties will later be give the constructor that takes a {@link Properties} object.
*/
public void saveProperties(Properties props) {
        if (mLicense != null && mLicense.length() > 0) {
            props.setProperty(PkgProps.PKG_LICENSE, mLicense);
}
if (mDescription != null && mDescription.length() > 0) {
props.setProperty(PkgProps.PKG_DESC, mDescription);
//Synthetic comment -- @@ -281,14 +335,14 @@
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
//Synthetic comment -- @@ -361,7 +415,7 @@
* Returns the optional description for all packages (platform, add-on, tool, doc) or
* for a lib. It is null if the element has not been specified in the repository XML.
*/
    public String getLicense() {
return mLicense;
}









//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/repository/PkgProps.java b/sdklib/src/main/java/com/android/sdklib/repository/PkgProps.java
//Synthetic comment -- index 68d7119..8f12432 100755

//Synthetic comment -- @@ -31,6 +31,7 @@
// Base Package
public static final String PKG_REVISION             = "Pkg.Revision";           //$NON-NLS-1$
public static final String PKG_LICENSE              = "Pkg.License";            //$NON-NLS-1$
public static final String PKG_DESC                 = "Pkg.Desc";               //$NON-NLS-1$
public static final String PKG_DESC_URL             = "Pkg.DescUrl";            //$NON-NLS-1$
public static final String PKG_RELEASE_NOTE         = "Pkg.RelNote";            //$NON-NLS-1$








//Synthetic comment -- diff --git a/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/PackageTest.java b/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/PackageTest.java
//Synthetic comment -- index f4d7b56..590f4d3 100755

//Synthetic comment -- @@ -156,7 +156,7 @@
protected void testCreatedPackage(Package p) {
// Package properties
assertEquals("42", p.getRevision().toShortString());
        assertEquals("The License", p.getLicense());
assertEquals("Some description.", p.getDescription());
assertEquals("http://description/url", p.getDescUrl());
assertEquals("Release Note", p.getReleaseNote());







