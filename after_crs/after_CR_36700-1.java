/*Fix resource validator for style resources

This fixes issue 31414:
Extract style from XML layout doesn't allow . (dot) in the name

The root issue is that in value files, you can use "."  as part of the
resource name; it gets remapped to _ in the R file.

Change-Id:I59bf9d7ceb36be832dde64f557d023dd6d1d29e0*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidator.java
//Synthetic comment -- index d6d77d3..188bbc7 100644

//Synthetic comment -- @@ -82,6 +82,10 @@
newText = newText.substring(0, newText.lastIndexOf('.'));
}

            if (!mIsFileType) {
                newText = newText.replace('.', '_');
            }

if (newText.indexOf('.') != -1 && !newText.endsWith(DOT_XML)) {
if (mIsImageType) {
return "The filename must end with .xml or .png";








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidatorTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidatorTest.java
//Synthetic comment -- index a5d34ec..97408c3 100644

//Synthetic comment -- @@ -32,16 +32,20 @@
assertTrue(validator.isValid("foo") == null);
assertTrue(validator.isValid("foo.xml") == null);
assertTrue(validator.isValid("Foo123_$") == null);
        assertTrue(validator.isValid("foo.xm") == null); // For non-file types, . => _

// Invalid
assertTrue(validator.isValid("") != null);
assertTrue(validator.isValid(" ") != null);
assertTrue(validator.isValid("foo bar") != null);
assertTrue(validator.isValid("1foo") != null);
assertTrue(validator.isValid("foo%bar") != null);
assertTrue(ResourceNameValidator.create(true, Collections.singleton("foo"),
ResourceType.STRING).isValid("foo") != null);
        assertTrue(ResourceNameValidator.create(true,
                ResourceFolderType.DRAWABLE).isValid("foo.xm") != null);
        assertTrue(ResourceNameValidator.create(false,
                ResourceFolderType.DRAWABLE).isValid("foo.xm") != null);

// Only lowercase chars allowed in file-based resource names
assertTrue(ResourceNameValidator.create(true, ResourceFolderType.LAYOUT)







