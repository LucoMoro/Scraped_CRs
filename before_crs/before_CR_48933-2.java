/*Move DebugKeyProvider into the Builder library.

Change-Id:I815dc7caff9056af4a7462bf976ddfd7824ef772*/
//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/internal/packaging/Packager.java b/builder/src/main/java/com/android/builder/internal/packaging/Packager.java
//Synthetic comment -- index 5860f20..06ddb1e 100644

//Synthetic comment -- @@ -19,13 +19,13 @@
import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.builder.internal.packaging.JavaResourceProcessor.IArchiveBuilder;
import com.android.builder.internal.signing.SignedJarBuilder;
import com.android.builder.internal.signing.SignedJarBuilder.IZipEntryFilter;
import com.android.builder.internal.signing.SigningInfo;
import com.android.builder.packaging.DuplicateFileException;
import com.android.builder.packaging.PackagerException;
import com.android.builder.packaging.SealedPackageException;
import com.android.sdklib.internal.build.DebugKeyProvider;
import com.android.utils.ILogger;

import java.io.File;








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/internal/signing/DebugKeyProvider.java b/builder/src/main/java/com/android/builder/internal/signing/DebugKeyProvider.java
new file mode 100644
//Synthetic comment -- index 0000000..8b232dc

//Synthetic comment -- @@ -0,0 +1,173 @@








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/internal/signing/KeystoreHelper.java b/builder/src/main/java/com/android/builder/internal/signing/KeystoreHelper.java
//Synthetic comment -- index ad687b0..3c9b040 100644

//Synthetic comment -- @@ -112,18 +112,14 @@
@Override
public void out(@Nullable String line) {
if (line != null) {
                                if (logger != null) {
                                    logger.info(line);
                                }
}
}

@Override
public void err(@Nullable String line) {
if (line != null) {
                                if (logger != null) {
                                    logger.error(null /*throwable*/, line);
                                }
}
}
});








//Synthetic comment -- diff --git a/builder/src/test/java/com/android/builder/internal/signing/DebugKeyProviderTest.java b/builder/src/test/java/com/android/builder/internal/signing/DebugKeyProviderTest.java
new file mode 100755
//Synthetic comment -- index 0000000..86aa40f

//Synthetic comment -- @@ -0,0 +1,142 @@








//Synthetic comment -- diff --git a/builder/src/test/java/com/android/builder/resources/NoteUtilsTest.java b/builder/src/test/java/com/android/builder/resources/NodeUtilsTest.java
similarity index 98%
rename from builder/src/test/java/com/android/builder/resources/NoteUtilsTest.java
rename to builder/src/test/java/com/android/builder/resources/NodeUtilsTest.java
//Synthetic comment -- index 1bcd083..70d25e7 100644

//Synthetic comment -- @@ -24,7 +24,7 @@
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class NoteUtilsTest extends TestCase {

public void testBasicAttributes() throws Exception {
Document document = createDocument();







