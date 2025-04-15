/*Update SymbolLoader/Writer with the one from builder.

Change-Id:I89bcbdb4b9daf436f548e78d268b3eabffb709db*/
//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/build/BuildConfigGenerator.java b/sdklib/src/main/java/com/android/sdklib/internal/build/BuildConfigGenerator.java
//Synthetic comment -- index 22d0d9e..0183b7f 100644

//Synthetic comment -- @@ -30,7 +30,10 @@
/**
* Class able to generate a BuildConfig class in Android project.
* The BuildConfig class contains constants related to the build target.
*/
public class BuildConfigGenerator {

public static final String BUILD_CONFIG_NAME = "BuildConfig.java";








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/build/DebugKeyProvider.java b/sdklib/src/main/java/com/android/sdklib/internal/build/DebugKeyProvider.java
//Synthetic comment -- index 4f4af36..8d4ba37 100644

//Synthetic comment -- @@ -34,7 +34,10 @@
/**
* A provider of a dummy key to sign Android application for debugging purpose.
* <p/>This provider uses a custom keystore to create and store a key with a known password.
*/
public class DebugKeyProvider {

public interface IKeyGenOutput {








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/build/KeystoreHelper.java b/sdklib/src/main/java/com/android/sdklib/internal/build/KeystoreHelper.java
//Synthetic comment -- index ba4ce8c..1f3f6e3 100644

//Synthetic comment -- @@ -33,7 +33,10 @@

/**
* A Helper to create new keystore/key.
*/
public final class KeystoreHelper {

/**








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/build/SignedJarBuilder.java b/sdklib/src/main/java/com/android/sdklib/internal/build/SignedJarBuilder.java
//Synthetic comment -- index 5044b45..506eb49 100644

//Synthetic comment -- @@ -52,7 +52,10 @@

/**
* A Jar file builder with signature support.
*/
public class SignedJarBuilder {
private static final String DIGEST_ALGORITHM = "SHA1";
private static final String DIGEST_ATTR = "SHA1-Digest";








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/build/SymbolLoader.java b/sdklib/src/main/java/com/android/sdklib/internal/build/SymbolLoader.java
//Synthetic comment -- index 3be729e..cbb4192 100644

//Synthetic comment -- @@ -26,7 +26,12 @@
import java.util.List;

/**
*/
public class SymbolLoader {

private final File mSymbolFile;
//Synthetic comment -- @@ -65,10 +70,13 @@

mSymbols = HashBasedTable.create();

        String currentLine = "";
try {
            for (String line : lines) {
                currentLine = line;
// format is "<type> <class> <name> <value>"
// don't want to split on space as value could contain spaces.
int pos = line.indexOf(' ');
//Synthetic comment -- @@ -82,9 +90,9 @@
mSymbols.put(className, name, new SymbolEntry(name, type, value));
}
} catch (Exception e) {
            // Catch both ArrayIndexOutOfBoundsException and StringIndexOutOfBoundsException
            throw new IOException("File format error reading " + mSymbolFile.getAbsolutePath()
                    + ": " + currentLine, e);
}
}









//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/build/SymbolWriter.java b/sdklib/src/main/java/com/android/sdklib/internal/build/SymbolWriter.java
//Synthetic comment -- index db5aad9..6146b02 100644

//Synthetic comment -- @@ -20,6 +20,8 @@
import com.android.sdklib.internal.build.SymbolLoader.SymbolEntry;
import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Table;
import com.google.common.io.Closeables;
import com.google.common.io.Files;
//Synthetic comment -- @@ -27,27 +29,46 @@
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
*/
public class SymbolWriter {

private final String mOutFolder;
private final String mPackageName;
    private final SymbolLoader mSymbols;
private final SymbolLoader mValues;

    public SymbolWriter(String outFolder, String packageName, SymbolLoader symbols,
            SymbolLoader values) {
mOutFolder = outFolder;
mPackageName = packageName;
        mSymbols = symbols;
mValues = values;
}

    @SuppressWarnings("resource") // Eclipse does handle Closeables.closeQuietly; see E#381445
public void write() throws IOException {
Splitter splitter = Splitter.on('.');
Iterable<String> folders = splitter.split(mPackageName);
//Synthetic comment -- @@ -73,20 +94,26 @@
writer.write(mPackageName);
writer.write(";\n\npublic final class R {\n");

            Table<String, String, SymbolEntry> symbols = mSymbols.getSymbols();
Table<String, String, SymbolEntry> values = mValues.getSymbols();

            for (String row : symbols.rowKeySet()) {
writer.write("\tpublic static final class ");
writer.write(row);
writer.write(" {\n");

                // Wrap the row in a TreeMap so that the field order be stable for unit tests.
Map<String, SymbolEntry> rowMap = symbols.row(row);
                rowMap = new TreeMap<String, SymbolEntry>(rowMap);
                for (Map.Entry<String, SymbolEntry> symbol : rowMap.entrySet()) {
// get the matching SymbolEntry from the values Table.
                    SymbolEntry value = values.get(row, symbol.getKey());
if (value != null) {
writer.write("\t\tpublic static final ");
writer.write(value.getType());








//Synthetic comment -- diff --git a/sdklib/src/test/java/com/android/sdklib/internal/build/SymbolLoaderTest.java b/sdklib/src/test/java/com/android/sdklib/internal/build/SymbolLoaderTest.java
//Synthetic comment -- index a972dfa..e594372 100644

//Synthetic comment -- @@ -15,16 +15,14 @@
*/
package com.android.sdklib.internal.build;

import com.android.sdklib.internal.build.SymbolLoader.SymbolEntry;
import com.google.common.base.Charsets;
import com.google.common.collect.Table;
import com.google.common.io.Files;

import java.io.File;

import junit.framework.TestCase;

@SuppressWarnings("javadoc")
public class SymbolLoaderTest extends TestCase {
public void test() throws Exception {
String r = "" +
//Synthetic comment -- @@ -34,7 +32,7 @@
Files.write(r, file, Charsets.UTF_8);
SymbolLoader loader = new SymbolLoader(file);
loader.load();
        Table<String, String, SymbolEntry> symbols = loader.getSymbols();
assertNotNull(symbols);
assertEquals(1, symbols.size());
assertNotNull(symbols.get("xml", "authenticator"));
//Synthetic comment -- @@ -52,7 +50,7 @@
Files.write(r, file, Charsets.UTF_8);
SymbolLoader loader = new SymbolLoader(file);
loader.load();
        Table<String, String, SymbolEntry> symbols = loader.getSymbols();
assertNotNull(symbols);
assertEquals(4, symbols.size());
}








//Synthetic comment -- diff --git a/sdklib/src/test/java/com/android/sdklib/internal/build/SymbolWriterTest.java b/sdklib/src/test/java/com/android/sdklib/internal/build/SymbolWriterTest.java
//Synthetic comment -- index 8120fc3..dd30562 100644

//Synthetic comment -- @@ -15,32 +15,63 @@
*/
package com.android.sdklib.internal.build;

import com.android.sdklib.internal.build.SymbolLoader.SymbolEntry;
import com.google.common.base.Charsets;
import com.google.common.collect.Table;
import com.google.common.io.Files;

import java.io.File;

import junit.framework.TestCase;

@SuppressWarnings("javadoc")
public class SymbolWriterTest extends TestCase {
    private void check(String packageName, String rText, String rJava) throws Exception {
        // Load symbols
File file = File.createTempFile(getClass().getSimpleName(), "txt");
file.deleteOnExit();
        Files.write(rText, file, Charsets.UTF_8);
        SymbolLoader loader = new SymbolLoader(file);
        loader.load();
        Table<String, String, SymbolEntry> symbols = loader.getSymbols();
        assertNotNull(symbols);

// Write symbols
File outFolder = Files.createTempDir();
outFolder.mkdirs();

        SymbolWriter writer = new SymbolWriter(outFolder.getPath(), packageName, loader, loader);
writer.write();

String contents = Files.toString(new File(outFolder,
//Synthetic comment -- @@ -56,9 +87,6 @@
// Package
"test.pkg",

            // R.txt
            "int xml authenticator 0x7f040000\n",

// R.java
"/* AUTO-GENERATED FILE.  DO NOT MODIFY.\n" +
" *\n" +
//Synthetic comment -- @@ -72,7 +100,13 @@
"    public static final class xml {\n" +
"        public static final int authenticator = 0x7f040000;\n" +
"    }\n" +
            "}\n"
);
}

//Synthetic comment -- @@ -81,14 +115,6 @@
// Package
"test.pkg",

            // R.txt
            "int drawable foobar 0x7f020000\n" +
            "int drawable ic_launcher 0x7f020001\n" +
            "int string app_name 0x7f030000\n" +
            "int string lib1 0x7f030001\n" +
            "int style AppBaseTheme 0x7f040000\n" +
            "int style AppTheme 0x7f040001\n",

// R.java
"/* AUTO-GENERATED FILE.  DO NOT MODIFY.\n" +
" *\n" +
//Synthetic comment -- @@ -99,19 +125,35 @@
"package test.pkg;\n" +
"\n" +
"public final class R {\n" +
            "    public static final class style {\n" +
            "        public static final int AppBaseTheme = 0x7f040000;\n" +
            "        public static final int AppTheme = 0x7f040001;\n" +
"    }\n" +
"    public static final class string {\n" +
"        public static final int app_name = 0x7f030000;\n" +
"        public static final int lib1 = 0x7f030001;\n" +
"    }\n" +
            "    public static final class drawable {\n" +
            "        public static final int foobar = 0x7f020000;\n" +
            "        public static final int ic_launcher = 0x7f020001;\n" +
"    }\n" +
            "}\n"
);
}

//Synthetic comment -- @@ -120,14 +162,6 @@
// Package
"test.pkg",

            // R.txt
            "int[] styleable TiledView { 0x7f010000, 0x7f010001, 0x7f010002, 0x7f010003, 0x7f010004 }\n" +
            "int styleable TiledView_tileName 2\n" +
            "int styleable TiledView_tilingEnum 4\n" +
            "int styleable TiledView_tilingMode 3\n" +
            "int styleable TiledView_tilingProperty 0\n" +
            "int styleable TiledView_tilingResource 1\n",

// R.java
"/* AUTO-GENERATED FILE.  DO NOT MODIFY.\n" +
" *\n" +
//Synthetic comment -- @@ -146,7 +180,18 @@
"        public static final int TiledView_tilingProperty = 0;\n" +
"        public static final int TiledView_tilingResource = 1;\n" +
"    }\n" +
            "}\n"
);
}

//Synthetic comment -- @@ -155,12 +200,6 @@
// Package
"test.pkg",

            // R.txt
            "int[] styleable LimitedSizeLinearLayout { 0x7f010000, 0x7f010001 }\n" +
            "int styleable LimitedSizeLinearLayout_max_height 1\n" +
            "int styleable LimitedSizeLinearLayout_max_width 0\n" +
            "int xml authenticator 0x7f040000\n",

// R.java
"/* AUTO-GENERATED FILE.  DO NOT MODIFY.\n" +
" *\n" +
//Synthetic comment -- @@ -179,7 +218,67 @@
"    public static final class xml {\n" +
"        public static final int authenticator = 0x7f040000;\n" +
"    }\n" +
            "}\n"
);
}
}







