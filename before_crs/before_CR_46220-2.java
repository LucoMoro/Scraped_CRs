/*Check for unsupported Java packages

If you include a library in your class path which was not built as an
Android project it's possible for it to include references to classes
in the java.* and javax.* namespace which are not valid in Android.

This changeset adds a new lint detector for this. It augments the API
database used by the API Checker to record the set of platform
packages, and to be able to quickly query whether an internal class
reference is in one of the valid packages.

The new lint check runs only on libraries (since tools will typically
set up the right classpath to give immediate errors if you try to
access invalid packages from your own code).

This was requested in issue 39109.

Change-Id:Id1ce7982e683bae9a484e7b75d7e77a256ca4414*/
//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiClass.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiClass.java
//Synthetic comment -- index b534b2f..365de80 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import static com.android.SdkConstants.CONSTRUCTOR_NAME;

import com.android.utils.Pair;
import com.google.common.collect.Lists;

//Synthetic comment -- @@ -229,6 +230,16 @@

}

@Override
public String toString() {
return mName;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiDetector.java
//Synthetic comment -- index ba93d01..fd7bba9 100644

//Synthetic comment -- @@ -312,7 +312,6 @@
className.substring(className.lastIndexOf('/') + 1), null,
SearchHints.create(NEAREST).matchJavaSymbol());
}

}
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiLookup.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiLookup.java
//Synthetic comment -- index 9d661a8..17578b8 100644

//Synthetic comment -- @@ -24,7 +24,9 @@
import com.android.tools.lint.client.api.LintClient;
import com.android.tools.lint.detector.api.LintUtils;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.common.primitives.UnsignedBytes;

//Synthetic comment -- @@ -74,12 +76,12 @@
/** Relative path to the api-versions.xml database file within the Lint installation */
private static final String XML_FILE_PATH = "platform-tools/api/api-versions.xml"; //$NON-NLS-1$
private static final String FILE_HEADER = "API database used by Android lint\000";
    private static final int BINARY_FORMAT_VERSION = 4;
private static final boolean DEBUG_FORCE_REGENERATE_BINARY = false;
private static final boolean DEBUG_SEARCH = false;
private static final boolean WRITE_STATS = false;
/** Default size to reserve for each API entry when creating byte buffer to build up data */
    private static final int BYTES_PER_ENTRY = 40;

private final LintClient mClient;
private final File mXmlFile;
//Synthetic comment -- @@ -89,6 +91,7 @@
private int[] mIndices;
private int mClassCount;
private int mMethodCount;

private static WeakReference<ApiLookup> sInstance =
new WeakReference<ApiLookup>(null);
//Synthetic comment -- @@ -237,16 +240,20 @@
*     version, it can ignore it (and regenerate the cache from XML).
* 3. The number of classes [1 int]
* 4. The number of members (across all classes) [1 int].
     * 5. Class offset table (one integer per class, pointing to the byte offset in the
*      file (relative to the beginning of the file) where each class begins.
*      The classes are always sorted alphabetically by fully qualified name.
     * 6. Member offset table (one integer per member, pointing to the byte offset in the
*      file (relative to the beginning of the file) where each member entry begins.
*      The members are always sorted alphabetically.
     * 7. Class entry table. Each class entry consists of the fully qualified class name,
*       in JVM format (using / instead of . in package names and $ for inner classes),
*       followed by the byte 0 as a terminator, followed by the API version as a byte.
     * 8. Member entry table. Each member entry consists of the class number (as a short),
*      followed by the JVM method/field signature, encoded as UTF-8, followed by a 0 byte
*      signature terminator, followed by the API level as a byte.
* <p>
//Synthetic comment -- @@ -290,6 +297,16 @@
mClassCount = buffer.getInt();
mMethodCount = buffer.getInt();

// Read in the class table indices;
int count = mClassCount + mMethodCount;
int[] offsets = new int[count];
//Synthetic comment -- @@ -345,10 +362,17 @@
Map<ApiClass, List<String>> memberMap =
Maps.newHashMapWithExpectedSize(classMap.size());
int memberCount = 0;
for (Map.Entry<String, ApiClass> entry : classMap.entrySet()) {
String className = entry.getKey();
ApiClass apiClass = entry.getValue();

Set<String> allMethods = apiClass.getAllMethods(info);
Set<String> allFields = apiClass.getAllFields(info);

//Synthetic comment -- @@ -399,6 +423,10 @@
}
Collections.sort(classes);

int entryCount = classMap.size() + memberCount;
int capacity = entryCount * BYTES_PER_ENTRY;
ByteBuffer buffer = ByteBuffer.allocate(capacity);
//Synthetic comment -- @@ -413,14 +441,26 @@
//      version, it can ignore it (and regenerate the cache from XML).
buffer.put((byte) BINARY_FORMAT_VERSION);



//  3. The number of classes [1 int]
buffer.putInt(classes.size());
//  4. The number of members (across all classes) [1 int].
buffer.putInt(memberCount);

        //  5. Class offset table (one integer per class, pointing to the byte offset in the
//       file (relative to the beginning of the file) where each class begins.
//       The classes are always sorted alphabetically by fully qualified name.
int classOffsetTable = buffer.position();
//Synthetic comment -- @@ -431,7 +471,7 @@
buffer.putInt(0);
}

        //  6. Member offset table (one integer per member, pointing to the byte offset in the
//       file (relative to the beginning of the file) where each member entry begins.
//       The members are always sorted alphabetically.
int methodOffsetTable = buffer.position();
//Synthetic comment -- @@ -442,7 +482,7 @@
int nextEntry = buffer.position();
int nextOffset = classOffsetTable;

        // 7. Class entry table. Each class entry consists of the fully qualified class name,
//      in JVM format (using / instead of . in package names and $ for inner classes),
//      followed by the byte 0 as a terminator, followed by the API version as a byte.
for (String clz : classes) {
//Synthetic comment -- @@ -462,7 +502,7 @@
nextEntry = buffer.position();
}

        //  8. Member entry table. Each member entry consists of the class number (as a short),
//       followed by the JVM method/field signature, encoded as UTF-8, followed by a 0 byte
//       signature terminator, followed by the API level as a byte.
assert nextOffset == methodOffsetTable;
//Synthetic comment -- @@ -715,6 +755,70 @@
return -1;
}

/** Returns the class number of the given class, or -1 if it is unknown */
private int findClass(@NonNull String owner) {
assert owner.indexOf('.') == -1 : "Should use / instead of . in owner: " + owner;
//Synthetic comment -- @@ -723,17 +827,17 @@
//   member indices from classCount to mIndices.length.
int low = 0;
int high = mClassCount - 1;
while (low <= high) {
int middle = (low + high) >>> 1;
int offset = mIndices[middle];

if (DEBUG_SEARCH) {
                System.out.println("Comparing string " + owner +" with entry at " + offset
+ ": " + dumpEntry(offset));
}

            // Compare the api info at the given index.
            int classNameLength = owner.length();
int compare = compare(mData, offset, (byte) 0, owner, classNameLength);
if (compare == 0) {
return middle;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index 52c89d4..7e7916e 100644

//Synthetic comment -- @@ -55,7 +55,7 @@
private static final List<Issue> sIssues;

static {
        final int initialCapacity = 131;
List<Issue> issues = new ArrayList<Issue>(initialCapacity);

issues.add(AccessibilityDetector.ISSUE);
//Synthetic comment -- @@ -64,6 +64,7 @@
issues.add(FieldGetterDetector.ISSUE);
issues.add(SdCardDetector.ISSUE);
issues.add(ApiDetector.UNSUPPORTED);
issues.add(DuplicateIdDetector.CROSS_LAYOUT);
issues.add(DuplicateIdDetector.WITHIN_LAYOUT);
issues.add(DuplicateResourceDetector.ISSUE);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/InvalidPackageDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/InvalidPackageDetector.java
new file mode 100644
//Synthetic comment -- index 0000000..460995d

//Synthetic comment -- @@ -0,0 +1,279 @@








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/ApiLookupTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/ApiLookupTest.java
//Synthetic comment -- index 2e55533..252ec5e 100644

//Synthetic comment -- @@ -87,6 +87,16 @@
"(Landroid/preference/PreferenceFragment;Landroid/preference/Preference;)"));
}

@Override
protected Detector getDetector() {
fail("This is not used in the ApiDatabase test");
//Synthetic comment -- @@ -129,6 +139,9 @@
raf.close();
lookup = ApiLookup.get(new LookupTestClient());
String message = mLogBuffer.toString();
assertTrue(message.contains("Please delete the file and restart the IDE/lint:"));
assertTrue(message.contains(mCacheDir.getPath()));
ApiLookup.dispose();








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/InvalidPackageDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/InvalidPackageDetectorTest.java
new file mode 100644
//Synthetic comment -- index 0000000..b634255

//Synthetic comment -- @@ -0,0 +1,56 @@







