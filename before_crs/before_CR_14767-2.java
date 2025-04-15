/*Fix missing @override.

Change-Id:Ic2bc0ae1c822f184bde63b29fdc15d897661c623*/
//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/MultiApkExportTask.java b/anttasks/src/com/android/ant/MultiApkExportTask.java
//Synthetic comment -- index 850004e..d6df8ec 100644

//Synthetic comment -- @@ -108,6 +108,7 @@
glVersion = data.glVersion;
}

public String toString() {
StringBuilder sb = new StringBuilder(outputName);
sb.append(" / ").append(relativePath);
//Synthetic comment -- @@ -151,7 +152,7 @@
/**
* Writes the apk description in the given writer. a single line is used to write
* everything.
         * @param writer
* @throws IOException
*
* @see {@link #read(String)}
//Synthetic comment -- @@ -164,7 +165,7 @@

/**
* reads the apk description from a log line.
         * @param data
*
* @see #write(FileWriter)
*/
//Synthetic comment -- @@ -538,10 +539,13 @@
/**
* Checks a manifest of the project for inclusion in the list of exported APK.
* If the manifest is correct, a list of apk to export is created and returned.
* @param androidManifest the manifest to check
* @param appPackage the package name of the application being exported, as read from
* export.properties.
     * @return
*/
private ArrayList<ApkData> checkManifest(FileWrapper androidManifest, String appPackage) {
try {
//Synthetic comment -- @@ -611,8 +615,9 @@

/**
* Finds ABIs in a project folder. This is based on the presence of libs/<abi>/ folder.
     * @param projectPath
     * @return
*/
private List<String> findAbis(String projectPath) {
ArrayList<String> abiList = new ArrayList<String>();
//Synthetic comment -- @@ -669,7 +674,7 @@
* Returns the {@link File} for the build log.
* @param appPackage
* @param versionCode
     * @return
*/
private File getBuildLog(String appPackage, int versionCode) {
return new File(appPackage + "." + versionCode + ".log");
//Synthetic comment -- @@ -678,7 +683,8 @@
/**
* Loads and returns a list of {@link ApkData} from a build log.
* @param log
     * @return
*/
private ApkData[] getProjects(File log) {
ArrayList<ApkData> datalist = new ArrayList<ApkData>();







