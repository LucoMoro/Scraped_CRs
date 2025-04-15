/*Unit test for symbol loader test

Change-Id:I5b85b01fb62d659ed75ee998da780feaea75c51c*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/SymbolLoader.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/SymbolLoader.java
//Synthetic comment -- index 775c558..9c20081 100644

//Synthetic comment -- @@ -30,7 +30,7 @@
public class SymbolLoader {

private final File mSymbolFile;
    private HashBasedTable<String, String, SymbolEntry> mSymbols;

public static class SymbolEntry {
private final String mName;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/SymbolWriter.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/SymbolWriter.java
//Synthetic comment -- index 63346c2..7411517 100644

//Synthetic comment -- @@ -46,6 +46,7 @@
mValues = values;
}

public void write() throws IOException {
Splitter splitter = Splitter.on('.');
Iterable<String> folders = splitter.split(mPackageName);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/build/SymbolLoaderTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/build/SymbolLoaderTest.java
new file mode 100644
//Synthetic comment -- index 0000000..a972dfa

//Synthetic comment -- @@ -0,0 +1,59 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/build/SymbolWriterTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/build/SymbolWriterTest.java
new file mode 100644
//Synthetic comment -- index 0000000..9edfb66

//Synthetic comment -- @@ -0,0 +1,185 @@







