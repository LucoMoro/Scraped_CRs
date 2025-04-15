/*Unit test for symbol loader test

Change-Id:I5b85b01fb62d659ed75ee998da780feaea75c51c*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/SymbolLoader.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/SymbolLoader.java
//Synthetic comment -- index 775c558..9c20081 100644

//Synthetic comment -- @@ -30,7 +30,7 @@
public class SymbolLoader {

private final File mSymbolFile;
    private Table<String, String, SymbolEntry> mSymbols;

public static class SymbolEntry {
private final String mName;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/SymbolWriter.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/SymbolWriter.java
//Synthetic comment -- index 63346c2..7411517 100644

//Synthetic comment -- @@ -46,6 +46,7 @@
mValues = values;
}

    @SuppressWarnings("resource") // Eclipse does handle Closeables.closeQuietly; see E#381445
public void write() throws IOException {
Splitter splitter = Splitter.on('.');
Iterable<String> folders = splitter.split(mPackageName);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/build/SymbolLoaderTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/build/SymbolLoaderTest.java
new file mode 100644
//Synthetic comment -- index 0000000..a972dfa

//Synthetic comment -- @@ -0,0 +1,59 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
                "int xml authenticator 0x7f040000\n";
        File file = File.createTempFile(getClass().getSimpleName(), "txt");
        file.deleteOnExit();
        Files.write(r, file, Charsets.UTF_8);
        SymbolLoader loader = new SymbolLoader(file);
        loader.load();
        Table<String, String, SymbolEntry> symbols = loader.getSymbols();
        assertNotNull(symbols);
        assertEquals(1, symbols.size());
        assertNotNull(symbols.get("xml", "authenticator"));
        assertEquals("0x7f040000", symbols.get("xml", "authenticator").getValue());
    }

    public void testStyleables() throws Exception {
        String r = "" +
            "int[] styleable LimitedSizeLinearLayout { 0x7f010000, 0x7f010001 }\n" +
            "int styleable LimitedSizeLinearLayout_max_height 1\n" +
            "int styleable LimitedSizeLinearLayout_max_width 0\n" +
            "int xml authenticator 0x7f040000\n";
        File file = File.createTempFile(getClass().getSimpleName(), "txt");
        file.deleteOnExit();
        Files.write(r, file, Charsets.UTF_8);
        SymbolLoader loader = new SymbolLoader(file);
        loader.load();
        Table<String, String, SymbolEntry> symbols = loader.getSymbols();
        assertNotNull(symbols);
        assertEquals(4, symbols.size());
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/build/SymbolWriterTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/build/SymbolWriterTest.java
new file mode 100644
//Synthetic comment -- index 0000000..9edfb66

//Synthetic comment -- @@ -0,0 +1,185 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
                packageName.replace('.',  File.separatorChar) + File.separator + "R.java"),
                Charsets.UTF_8);

        // Ensure we wrote what was expected
        assertEquals(rJava, contents.replaceAll("\t", "    "));
    }

    public void test1() throws Exception {
        check(
            // Package
            "test.pkg",

            // R.txt
            "int xml authenticator 0x7f040000\n",

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
            "    public static final class xml {\n" +
            "        public static final int authenticator = 0x7f040000;\n" +
            "    }\n" +
            "}\n"
        );
    }

    public void test2() throws Exception {
        check(
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
            " * This class was automatically generated by the\n" +
            " * aapt tool from the resource data it found.  It\n" +
            " * should not be modified by hand.\n" +
            " */\n" +
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
            "        public static final int ic_launcher = 0x7f020001;\n" +
            "        public static final int foobar = 0x7f020000;\n" +
            "    }\n" +
            "}\n"
        );
    }

    public void testStyleables1() throws Exception {
        check(
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
            " * This class was automatically generated by the\n" +
            " * aapt tool from the resource data it found.  It\n" +
            " * should not be modified by hand.\n" +
            " */\n" +
            "package test.pkg;\n" +
            "\n" +
            "public final class R {\n" +
            "    public static final class styleable {\n" +
            "        public static final int TiledView_tilingProperty = 0;\n" +
            "        public static final int TiledView_tilingMode = 3;\n" +
            "        public static final int TiledView_tilingResource = 1;\n" +
            "        public static final int TiledView_tileName = 2;\n" +
            "        public static final int TiledView_tilingEnum = 4;\n" +
            "        public static final int[] TiledView = { 0x7f010000, 0x7f010001, 0x7f010002, 0x7f010003, 0x7f010004 };\n" +
            "    }\n" +
            "}\n"
        );
    }

    public void testStyleables2() throws Exception {
        check(
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
            " * This class was automatically generated by the\n" +
            " * aapt tool from the resource data it found.  It\n" +
            " * should not be modified by hand.\n" +
            " */\n" +
            "package test.pkg;\n" +
            "\n" +
            "public final class R {\n" +
            "    public static final class styleable {\n" +
            "        public static final int LimitedSizeLinearLayout_max_height = 1;\n" +
            "        public static final int LimitedSizeLinearLayout_max_width = 0;\n" +
            "        public static final int[] LimitedSizeLinearLayout = { 0x7f010000, 0x7f010001 };\n" +
            "    }\n" +
            "    public static final class xml {\n" +
            "        public static final int authenticator = 0x7f040000;\n" +
            "    }\n" +
            "}\n"
        );
    }
}







