/*Lazy parsing of zip files for Java resources.

Central directories of zip files should be read lazily.
They are needed only to service calls to
BaseDexClassLoader#findResouce(s). Most android processes
don't use these methods since they use android resources.

bug:6797061

(cherry-picked from 8dd90ae77922eb05d46c797937620a4c69f758f7)

Change-Id:I1a5b5d03572601707e1fb1fd4424c1ae2fd2217d*/




//Synthetic comment -- diff --git a/dalvik/src/main/java/dalvik/system/DexPathList.java b/dalvik/src/main/java/dalvik/system/DexPathList.java
//Synthetic comment -- index 048cc83..3b4c100 100644

//Synthetic comment -- @@ -189,7 +189,7 @@
* up front.
*/
for (File file : files) {
            File zip = null;
DexFile dex = null;
String name = file.getName();

//Synthetic comment -- @@ -202,17 +202,7 @@
}
} else if (name.endsWith(APK_SUFFIX) || name.endsWith(JAR_SUFFIX)
|| name.endsWith(ZIP_SUFFIX)) {
                zip = file;

try {
dex = loadDexFile(file, optimizedDirectory);
//Synthetic comment -- @@ -372,23 +362,53 @@
* Element of the dex/resource file path
*/
/*package*/ static class Element {
        private final File file;
        private final File zip;
        private final DexFile dexFile;

        private ZipFile zipFile;
        private boolean init;

        public Element(File file, File zip, DexFile dexFile) {
this.file = file;
            this.zip = zip;
this.dexFile = dexFile;
}

        public synchronized void maybeInit() {
            if (init) {
                return;
            }

            init = true;

            if (zip == null) {
/*
* Either this element has no zip/jar file (first
* clause), or the zip/jar file doesn't have an entry
* for the given name (second clause).
*/
                return;
            }

            try {
                zipFile = new ZipFile(zip);
            } catch (IOException ioe) {
                /*
                 * Note: ZipException (a subclass of IOException)
                 * might get thrown by the ZipFile constructor
                 * (e.g. if the file isn't actually a zip/jar
                 * file).
                 */
                System.logE("Unable to open zip file: " + file, ioe);
                zipFile = null;
            }
        }

        public URL findResource(String name) {
            maybeInit();

            if (zipFile == null || zipFile.getEntry(name) == null) {
return null;
}








