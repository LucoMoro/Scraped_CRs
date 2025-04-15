/*Fixing issue 14017

Android classpath container doesn't allow changing Javadoc attachment

Change-Id:I6cdc767a295adea166311b90475f038a3f755aaehttp://code.google.com/p/android/issues/detail?id=14017*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/AndroidClasspathContainerInitializer.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/AndroidClasspathContainerInitializer.java
//Synthetic comment -- index 0f6b508..c5f1fff 100644

//Synthetic comment -- @@ -67,6 +67,8 @@
*/
public class AndroidClasspathContainerInitializer extends ClasspathContainerInitializer {

public static final String SOURCES_ZIP = "/sources.zip"; //$NON-NLS-1$

public static final String COM_ANDROID_IDE_ECLIPSE_ADT_SOURCE =
//Synthetic comment -- @@ -530,8 +532,7 @@
}
}
IClasspathAttribute[] attributes = null;
        if (apiURL != null) {

IClasspathAttribute cpAttribute = JavaCore.newClasspathAttribute(
IClasspathAttribute.JAVADOC_LOCATION_ATTRIBUTE_NAME, apiURL);
attributes = new IClasspathAttribute[] {
//Synthetic comment -- @@ -824,12 +825,22 @@
getAndroidSourceProperty(target), null);
}
IClasspathAttribute[] extraAttributtes = entry.getExtraAttributes();
for (int j = 0; j < extraAttributtes.length; j++) {
IClasspathAttribute extraAttribute = extraAttributtes[j];
if (IClasspathAttribute.JAVADOC_LOCATION_ATTRIBUTE_NAME
.equals(extraAttribute.getName())) {
ProjectHelper.saveStringProperty(root,
                                                PROPERTY_ANDROID_API, extraAttribute.getValue());

}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sourcelookup/AdtSourceLookupDirector.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sourcelookup/AdtSourceLookupDirector.java
//Synthetic comment -- index 612ef76..323b65d 100755

//Synthetic comment -- @@ -32,6 +32,7 @@
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.launching.JavaSourceLookupDirector;

import java.io.File;

//Synthetic comment -- @@ -41,8 +42,8 @@
public void initializeDefaults(ILaunchConfiguration configuration) throws CoreException {
dispose();
setLaunchConfiguration(configuration);
        String projectName = configuration.getAttribute("org.eclipse.jdt.launching.PROJECT_ATTR", //$NON-NLS-1$
                ""); //$NON-NLS-1$
if (projectName != null && projectName.length() > 0) {
IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
if (project != null && project.isOpen()) {







