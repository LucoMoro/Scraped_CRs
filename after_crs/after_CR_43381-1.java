/*Remove null warnings

Eclipse's null analysis thinks that a couple of references (required
to be non null) might be null. We can tell it we know better with an
assertion.

That's because its analysis doesn't go deep enough to for example
understand this:

     Object x = null;
     boolean y = true;
     if (y) {
        x = "not null";
     }

     if (y) {
         // Here we know x is not null, but Eclipse does not
     }

Change-Id:If63db13feabf05832c5d9e488ace5c72224da554*/




//Synthetic comment -- diff --git a/manifmerger/src/com/android/manifmerger/ManifestMerger.java b/manifmerger/src/com/android/manifmerger/ManifestMerger.java
//Synthetic comment -- index d6063ea..e4c3d6f 100755

//Synthetic comment -- @@ -374,6 +374,7 @@
if (pkg == null || pkg.length() == 0) {
// We can't adjust FQCNs if we don't know the root package name.
// It's not a proper manifest if this is missing anyway.
            assert manifest != null;
mLog.error(Severity.WARNING,
xmlFileAndLine(manifest),
"Missing 'package' attribute in manifest.");
//Synthetic comment -- @@ -444,6 +445,7 @@
// The main doc does not have to have an application node.
String mainValue = mainApp == null ? "" : getAttributeValue(mainApp, attrName);
if (!libValue.equals(mainValue)) {
                assert mainApp != null;
mLog.conflict(Severity.WARNING,
xmlFileAndLine(mainApp),
xmlFileAndLine(libApp),







