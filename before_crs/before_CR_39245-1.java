/*Fix a possible NPE when reading bad prop files.

Change-Id:I8f650a139c121bd68e3c246ff1a7db1a02378191*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectProperties.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectProperties.java
//Synthetic comment -- index e028aea..27879a7 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import static com.android.sdklib.SdkConstants.FN_PROJECT_PROGUARD_FILE;

import com.android.annotations.NonNull;
import com.android.io.FolderWrapper;
import com.android.io.IAbstractFile;
import com.android.io.IAbstractFolder;
//Synthetic comment -- @@ -423,10 +424,12 @@
* <p/>If the file is not present, null is returned with no error messages sent to the log.
*
* @param propFile the property file to parse
     * @param log the ISdkLog object receiving warning/error from the parsing. Cannot be null.
* @return the map of (key,value) pairs, or null if the parsing failed.
*/
    public static Map<String, String> parsePropertyFile(IAbstractFile propFile, ISdkLog log) {
BufferedReader reader = null;
try {
reader = new BufferedReader(new InputStreamReader(propFile.getContents(),
//Synthetic comment -- @@ -435,15 +438,18 @@
String line = null;
Map<String, String> map = new HashMap<String, String>();
while ((line = reader.readLine()) != null) {
if (line.length() > 0 && line.charAt(0) != '#') {

Matcher m = PATTERN_PROP.matcher(line);
if (m.matches()) {
map.put(m.group(1), unescape(m.group(2)));
} else {
                        log.warning("Error parsing '%1$s': \"%2$s\" is not a valid syntax",
                                propFile.getOsLocation(),
                                line);
return null;
}
}
//Synthetic comment -- @@ -455,13 +461,17 @@
// calling the method.
// Return null below.
} catch (IOException e) {
            log.warning("Error parsing '%1$s': %2$s.",
                    propFile.getOsLocation(),
                    e.getMessage());
} catch (StreamException e) {
            log.warning("Error parsing '%1$s': %2$s.",
                    propFile.getOsLocation(),
                    e.getMessage());
} finally {
if (reader != null) {
try {







