/*Fix minor errors when used with Eclipse4.

Change-Id:Ia31c45715530f58ac293f61185b5a45f71fab4f5*/




//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/DependencyGraph.java b/anttasks/src/com/android/ant/DependencyGraph.java
//Synthetic comment -- index ef059b9..4a02aa8 100644

//Synthetic comment -- @@ -431,25 +431,28 @@
* @return null if the file could not be read
*/
private static String readFile(String filepath) {
        FileInputStream fStream = null;
        BufferedReader reader = null;
try {
            fStream = new FileInputStream(filepath);
            reader = new BufferedReader(new InputStreamReader(fStream));

            String line;
            StringBuilder total = new StringBuilder(reader.readLine());
            while ((line = reader.readLine()) != null) {
                total.append('\n');
                total.append(line);
}
            return total.toString();
} catch (IOException e) {
// we'll just return null
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
}
return null;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectPropertiesWorkingCopy.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectPropertiesWorkingCopy.java
//Synthetic comment -- index e88463c..a2aefd0 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdklib.internal.project;

import com.android.annotations.NonNull;
import com.android.io.IAbstractFile;
import com.android.io.IAbstractFolder;
import com.android.io.StreamException;
//Synthetic comment -- @@ -240,6 +241,7 @@
super(projectFolder, map, type);
}

    @NonNull
public ProjectProperties makeReadOnlyCopy() {
// copy the current properties in a new map
Map<String, String> propList = new HashMap<String, String>(mProperties);







