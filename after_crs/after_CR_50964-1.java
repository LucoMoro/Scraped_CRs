/*Update SymbolLoader/Writer with the one from builder.

Change-Id:I89bcbdb4b9daf436f548e78d268b3eabffb709db*/




//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/build/BuildConfigGenerator.java b/sdklib/src/main/java/com/android/sdklib/internal/build/BuildConfigGenerator.java
//Synthetic comment -- index 22d0d9e..0183b7f 100644

//Synthetic comment -- @@ -30,7 +30,10 @@
/**
* Class able to generate a BuildConfig class in Android project.
* The BuildConfig class contains constants related to the build target.
 * 
 * @deprecated Use Android-Builder instead
*/
@Deprecated
public class BuildConfigGenerator {

public static final String BUILD_CONFIG_NAME = "BuildConfig.java";








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/build/DebugKeyProvider.java b/sdklib/src/main/java/com/android/sdklib/internal/build/DebugKeyProvider.java
//Synthetic comment -- index 4f4af36..8d4ba37 100644

//Synthetic comment -- @@ -34,7 +34,10 @@
/**
* A provider of a dummy key to sign Android application for debugging purpose.
* <p/>This provider uses a custom keystore to create and store a key with a known password.
 * 
 * @deprecated Use Android-Builder instead
*/
@Deprecated
public class DebugKeyProvider {

public interface IKeyGenOutput {








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/build/KeystoreHelper.java b/sdklib/src/main/java/com/android/sdklib/internal/build/KeystoreHelper.java
//Synthetic comment -- index ba4ce8c..1f3f6e3 100644

//Synthetic comment -- @@ -33,7 +33,10 @@

/**
* A Helper to create new keystore/key.
 * 
 * @deprecated Use Android-Builder instead
*/
@Deprecated
public final class KeystoreHelper {

/**








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/build/SignedJarBuilder.java b/sdklib/src/main/java/com/android/sdklib/internal/build/SignedJarBuilder.java
//Synthetic comment -- index 5044b45..506eb49 100644

//Synthetic comment -- @@ -52,7 +52,10 @@

/**
* A Jar file builder with signature support.
 * 
 * @deprecated Use Android-Builder instead
*/
@Deprecated
public class SignedJarBuilder {
private static final String DIGEST_ALGORITHM = "SHA1";
private static final String DIGEST_ATTR = "SHA1-Digest";








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/build/SymbolLoader.java b/sdklib/src/main/java/com/android/sdklib/internal/build/SymbolLoader.java
//Synthetic comment -- index 3be729e..cbb4192 100644

//Synthetic comment -- @@ -26,7 +26,12 @@
import java.util.List;

/**
 * A class to load the text symbol file generated by aapt with the
 * --output-text-symbols option.
 * 
 * @deprecated Use Android-Builder instead
*/
@Deprecated
public class SymbolLoader {

private final File mSymbolFile;
//Synthetic comment -- @@ -65,10 +70,13 @@

mSymbols = HashBasedTable.create();

        int lineIndex = 1;
        String line = null;
try {
            final int count = lines.size();
            for (; lineIndex <= count ; lineIndex++) {
                line = lines.get(lineIndex-1);

// format is "<type> <class> <name> <value>"
// don't want to split on space as value could contain spaces.
int pos = line.indexOf(' ');
//Synthetic comment -- @@ -82,9 +90,9 @@
mSymbols.put(className, name, new SymbolEntry(name, type, value));
}
} catch (Exception e) {
            String s = String.format("File format error reading %s\tline %d: '%s'",
                    mSymbolFile.getAbsolutePath(), lineIndex, line);
            throw new IOException(s, e);
}
}









//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/build/SymbolWriter.java b/sdklib/src/main/java/com/android/sdklib/internal/build/SymbolWriter.java
//Synthetic comment -- index db5aad9..6146b02 100644

//Synthetic comment -- @@ -20,6 +20,8 @@
import com.android.sdklib.internal.build.SymbolLoader.SymbolEntry;
import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.google.common.io.Closeables;
import com.google.common.io.Files;
//Synthetic comment -- @@ -27,27 +29,46 @@
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A class to write R.java classes based on data read from text symbol files generated by
 * aapt with the --output-text-symbols option.
 * 
 * @deprecated Use Android-Builder instead
*/
@Deprecated
public class SymbolWriter {

private final String mOutFolder;
private final String mPackageName;
    private final List<SymbolLoader> mSymbols = Lists.newArrayList();
private final SymbolLoader mValues;

    public SymbolWriter(String outFolder, String packageName, SymbolLoader values) {
mOutFolder = outFolder;
mPackageName = packageName;
mValues = values;
}

    public void addSymbolsToWrite(SymbolLoader symbols) {
        mSymbols.add(symbols);
    }

    private Table<String, String, SymbolEntry> getAllSymbols() {
        Table<String, String, SymbolEntry> symbols = HashBasedTable.create();

        for (SymbolLoader symbolLoader : mSymbols) {
            symbols.putAll(symbolLoader.getSymbols());
        }

        return symbols;
    }

public void write() throws IOException {
Splitter splitter = Splitter.on('.');
Iterable<String> folders = splitter.split(mPackageName);
//Synthetic comment -- @@ -73,20 +94,26 @@
writer.write(mPackageName);
writer.write(";\n\npublic final class R {\n");

            Table<String, String, SymbolEntry> symbols = getAllSymbols();
Table<String, String, SymbolEntry> values = mValues.getSymbols();

            Set<String> rowSet = symbols.rowKeySet();
            List<String> rowList = Lists.newArrayList(rowSet);
            Collections.sort(rowList);

            for (String row : rowList) {
writer.write("\tpublic static final class ");
writer.write(row);
writer.write(" {\n");

Map<String, SymbolEntry> rowMap = symbols.row(row);
                Set<String> symbolSet = rowMap.keySet();
                ArrayList<String> symbolList = Lists.newArrayList(symbolSet);
                Collections.sort(symbolList);

                for (String symbolName : symbolList) {
// get the matching SymbolEntry from the values Table.
                    SymbolEntry value = values.get(row, symbolName);
if (value != null) {
writer.write("\t\tpublic static final ");
writer.write(value.getType());








//Synthetic comment -- diff --git a/sdklib/src/test/java/com/android/sdklib/internal/build/SymbolLoaderTest.java b/sdklib/src/test/java/com/android/sdklib/internal/build/SymbolLoaderTest.java
//Synthetic comment -- index a972dfa..e594372 100644

//Synthetic comment -- @@ -15,16 +15,14 @@
*/
package com.android.sdklib.internal.build;

import com.google.common.base.Charsets;
import com.google.common.collect.Table;
import com.google.common.io.Files;
import junit.framework.TestCase;

import java.io.File;

@SuppressWarnings({"javadoc", "deprecation"})
public class SymbolLoaderTest extends TestCase {
public void test() throws Exception {
String r = "" +
//Synthetic comment -- @@ -34,7 +32,7 @@
Files.write(r, file, Charsets.UTF_8);
SymbolLoader loader = new SymbolLoader(file);
loader.load();
        Table<String, String, SymbolLoader.SymbolEntry> symbols = loader.getSymbols();
assertNotNull(symbols);
assertEquals(1, symbols.size());
assertNotNull(symbols.get("xml", "authenticator"));
//Synthetic comment -- @@ -52,7 +50,7 @@
Files.write(r, file, Charsets.UTF_8);
SymbolLoader loader = new SymbolLoader(file);
loader.load();
        Table<String, String, SymbolLoader.SymbolEntry> symbols = loader.getSymbols();
assertNotNull(symbols);
assertEquals(4, symbols.size());
}








//Synthetic comment -- diff --git a/sdklib/src/test/java/com/android/sdklib/internal/build/SymbolWriterTest.java b/sdklib/src/test/java/com/android/sdklib/internal/build/SymbolWriterTest.java
//Synthetic comment -- index 8120fc3..dd30562 100644

//Synthetic comment -- @@ -15,32 +15,63 @@
*/
package com.android.sdklib.internal.build;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.google.common.io.Files;
import junit.framework.TestCase;

import java.io.File;
import java.util.List;

@SuppressWarnings({"javadoc", "deprecation"})
public class SymbolWriterTest extends TestCase {
    private void check(String packageName, String rJava, String rValues, String... rTexts)
            throws Exception {
        if (rValues == null) {
            if (rTexts.length == 1) {
                rValues = rTexts[0];
            } else {
                throw new IllegalArgumentException(
                        "Can't have a null rValues with rTexts.length!=1");
            }
        }

        // Load the symbol values
        // 1. write rText in a temp file
File file = File.createTempFile(getClass().getSimpleName(), "txt");
file.deleteOnExit();
        Files.write(rValues, file, Charsets.UTF_8);
        // 2. load symbol from temp file.
        SymbolLoader symbolValues = new SymbolLoader(file);
        symbolValues.load();
        Table<String, String, SymbolLoader.SymbolEntry> values = symbolValues.getSymbols();
        assertNotNull(values);


        // Load the symbols to write
        List<SymbolLoader> symbolList = Lists.newArrayListWithCapacity(rTexts.length);
        for (String rText : rTexts) {
            // 1. write rText in a temp file
            file = File.createTempFile(getClass().getSimpleName(), "txt");
            file.deleteOnExit();
            Files.write(rText, file, Charsets.UTF_8);
            // 2. load symbol from temp file.
            SymbolLoader loader = new SymbolLoader(file);
            loader.load();
            Table<String, String, SymbolLoader.SymbolEntry> symbols = loader.getSymbols();
            assertNotNull(symbols);
            symbolList.add(loader);
        }

// Write symbols
File outFolder = Files.createTempDir();
outFolder.mkdirs();

        SymbolWriter writer = new SymbolWriter(outFolder.getPath(), packageName, symbolValues);
        for (SymbolLoader symbolLoader : symbolList) {
            writer.addSymbolsToWrite(symbolLoader);
        }
writer.write();

String contents = Files.toString(new File(outFolder,
//Synthetic comment -- @@ -56,9 +87,6 @@
// Package
"test.pkg",

// R.java
"/* AUTO-GENERATED FILE.  DO NOT MODIFY.\n" +
" *\n" +
//Synthetic comment -- @@ -72,7 +100,13 @@
"    public static final class xml {\n" +
"        public static final int authenticator = 0x7f040000;\n" +
"    }\n" +
            "}\n",

            // R values
            null,

            // R.txt
            "int xml authenticator 0x7f040000\n"
);
}

//Synthetic comment -- @@ -81,14 +115,6 @@
// Package
"test.pkg",

// R.java
"/* AUTO-GENERATED FILE.  DO NOT MODIFY.\n" +
" *\n" +
//Synthetic comment -- @@ -99,19 +125,35 @@
"package test.pkg;\n" +
"\n" +
"public final class R {\n" +
            "    public static final class drawable {\n" +
            "        public static final int foobar = 0x7f020000;\n" +
            "        public static final int ic_launcher = 0x7f020001;\n" +
"    }\n" +
"    public static final class string {\n" +
"        public static final int app_name = 0x7f030000;\n" +
"        public static final int lib1 = 0x7f030001;\n" +
"    }\n" +
            "    public static final class style {\n" +
            "        public static final int AppBaseTheme = 0x7f040000;\n" +
            "        public static final int AppTheme = 0x7f040001;\n" +
"    }\n" +
            "}\n",

            // R values
            "int drawable foobar 0x7f020000\n" +
            "int drawable ic_launcher 0x7f020001\n" +
            "int string app_name 0x7f030000\n" +
            "int string lib1 0x7f030001\n" +
            "int style AppBaseTheme 0x7f040000\n" +
            "int style AppTheme 0x7f040001\n",

            // R.txt
            "int drawable foobar 0x7fffffff\n" +
            "int drawable ic_launcher 0x7fffffff\n" +
            "int string app_name 0x7fffffff\n" +
            "int string lib1 0x7fffffff\n" +
            "int style AppBaseTheme 0x7fffffff\n" +
            "int style AppTheme 0x7fffffff\n"
);
}

//Synthetic comment -- @@ -120,14 +162,6 @@
// Package
"test.pkg",

// R.java
"/* AUTO-GENERATED FILE.  DO NOT MODIFY.\n" +
" *\n" +
//Synthetic comment -- @@ -146,7 +180,18 @@
"        public static final int TiledView_tilingProperty = 0;\n" +
"        public static final int TiledView_tilingResource = 1;\n" +
"    }\n" +
            "}\n",

            // R values
            null,

            // R.txt
            "int[] styleable TiledView { 0x7f010000, 0x7f010001, 0x7f010002, 0x7f010003, 0x7f010004 }\n" +
            "int styleable TiledView_tileName 2\n" +
            "int styleable TiledView_tilingEnum 4\n" +
            "int styleable TiledView_tilingMode 3\n" +
            "int styleable TiledView_tilingProperty 0\n" +
            "int styleable TiledView_tilingResource 1\n"
);
}

//Synthetic comment -- @@ -155,12 +200,6 @@
// Package
"test.pkg",

// R.java
"/* AUTO-GENERATED FILE.  DO NOT MODIFY.\n" +
" *\n" +
//Synthetic comment -- @@ -179,7 +218,67 @@
"    public static final class xml {\n" +
"        public static final int authenticator = 0x7f040000;\n" +
"    }\n" +
            "}\n",

            // R values
            null,

            // R.txt
            "int[] styleable LimitedSizeLinearLayout { 0x7f010000, 0x7f010001 }\n" +
            "int styleable LimitedSizeLinearLayout_max_height 1\n" +
            "int styleable LimitedSizeLinearLayout_max_width 0\n" +
            "int xml authenticator 0x7f040000\n"
        );
    }

    public void testMerge() throws Exception {
        check(
            // Package
            "test.pkg",

            // R.java
            "/* AUTO-GENERATED FILE.  DO NOT MODIFY.\n" +
            " *\n" +
            " * This class was automatically generated by the\n" +
            " * aapt tool from the resource data it found.  It\n" +
            " * should not be modified by hand.\n" +
            " */\n" +
            "package test.pkg;\n" +
            "\n" +
            "public final class R {\n" +
            "    public static final class drawable {\n" +
            "        public static final int foobar = 0x7f020000;\n" +
            "        public static final int ic_launcher = 0x7f020001;\n" +
            "    }\n" +
            "    public static final class string {\n" +
            "        public static final int app_name = 0x7f030000;\n" +
            "        public static final int lib1 = 0x7f030001;\n" +
            "    }\n" +
            "    public static final class style {\n" +
            "        public static final int AppBaseTheme = 0x7f040000;\n" +
            "        public static final int AppTheme = 0x7f040001;\n" +
            "    }\n" +
            "}\n",

            // R values
            "int drawable foobar 0x7f020000\n" +
            "int drawable ic_launcher 0x7f020001\n" +
            "int string app_name 0x7f030000\n" +
            "int string lib1 0x7f030001\n" +
            "int style AppBaseTheme 0x7f040000\n" +
            "int style AppTheme 0x7f040001\n",

            // R.txt 1
            "int drawable foobar 0x7fffffff\n" +
            "int drawable ic_launcher 0x7fffffff\n" +
            "int string app_name 0x7fffffff\n" +
            "int string lib1 0x7fffffff\n" +

            // R.txt 2
            "int string app_name 0x80000000\n" +
            "int string lib1 0x80000000\n" +
            "int style AppBaseTheme 0x80000000\n" +
            "int style AppTheme 0x80000000\n"
);
}
}







