/*Fix SymbolWriter to guarantee map order unit test output.

Change-Id:Iebf2588b6faf031d4ea166a9d9df053d0f70bdeb*/
//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/build/SymbolWriter.java b/sdklib/src/main/java/com/android/sdklib/internal/build/SymbolWriter.java
//Synthetic comment -- index 7411517..db5aad9 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
*/
//Synthetic comment -- @@ -80,7 +81,10 @@
writer.write(row);
writer.write(" {\n");

                for (Map.Entry<String, SymbolEntry> symbol : symbols.row(row).entrySet()) {
// get the matching SymbolEntry from the values Table.
SymbolEntry value = values.get(row, symbol.getKey());
if (value != null) {
//Synthetic comment -- @@ -102,4 +106,4 @@
Closeables.closeQuietly(writer);
}
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/sdklib/src/test/java/com/android/sdklib/internal/build/SymbolWriterTest.java b/sdklib/src/test/java/com/android/sdklib/internal/build/SymbolWriterTest.java
//Synthetic comment -- index 9edfb66..8120fc3 100644

//Synthetic comment -- @@ -108,8 +108,8 @@
"        public static final int lib1 = 0x7f030001;\n" +
"    }\n" +
"    public static final class drawable {\n" +
            "        public static final int ic_launcher = 0x7f020001;\n" +
"        public static final int foobar = 0x7f020000;\n" +
"    }\n" +
"}\n"
);
//Synthetic comment -- @@ -139,12 +139,12 @@
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
//Synthetic comment -- @@ -172,9 +172,9 @@
"\n" +
"public final class R {\n" +
"    public static final class styleable {\n" +
"        public static final int LimitedSizeLinearLayout_max_height = 1;\n" +
"        public static final int LimitedSizeLinearLayout_max_width = 0;\n" +
            "        public static final int[] LimitedSizeLinearLayout = { 0x7f010000, 0x7f010001 };\n" +
"    }\n" +
"    public static final class xml {\n" +
"        public static final int authenticator = 0x7f040000;\n" +







