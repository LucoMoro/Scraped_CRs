/*Fixing issue 14017 - Android classpath container doesn't allow
changing Javadoc attachement

Change-Id:I6cdc767a295adea166311b90475f038a3f755aaehttp://code.google.com/p/android/issues/detail?id=14017*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/AndroidClasspathContainerInitializer.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/AndroidClasspathContainerInitializer.java
//Synthetic comment -- index a1b11ae..dc8b035 100644

//Synthetic comment -- @@ -67,6 +67,8 @@
*/
public class AndroidClasspathContainerInitializer extends ClasspathContainerInitializer {

    public static final String NULL_API_URL = "<null>"; //$NON-NLS-1$

public static final String SOURCES_ZIP = "/sources.zip"; //$NON-NLS-1$

public static final String COM_ANDROID_IDE_ECLIPSE_ADT_SOURCE =
//Synthetic comment -- @@ -524,8 +526,7 @@
}
}
IClasspathAttribute[] attributes = null;
        if (apiURL != null && !NULL_API_URL.equals(apiURL)) {
IClasspathAttribute cpAttribute = JavaCore.newClasspathAttribute(
IClasspathAttribute.JAVADOC_LOCATION_ATTRIBUTE_NAME, apiURL);
attributes = new IClasspathAttribute[] {
//Synthetic comment -- @@ -818,12 +819,22 @@
getAndroidSourceProperty(target), null);
}
IClasspathAttribute[] extraAttributtes = entry.getExtraAttributes();
                                if (extraAttributtes.length == 0) {
                                    ProjectHelper.saveStringProperty(root, PROPERTY_ANDROID_API,
                                            NULL_API_URL);
                                }
for (int j = 0; j < extraAttributtes.length; j++) {
IClasspathAttribute extraAttribute = extraAttributtes[j];
                                    String value = extraAttribute.getValue();
                                    if ((value == null || value.trim().length() == 0)
                                            && IClasspathAttribute.JAVADOC_LOCATION_ATTRIBUTE_NAME
                                                    .equals(extraAttribute.getName())) {
                                        value = NULL_API_URL;
                                    }
if (IClasspathAttribute.JAVADOC_LOCATION_ATTRIBUTE_NAME
.equals(extraAttribute.getName())) {
ProjectHelper.saveStringProperty(root,
                                                PROPERTY_ANDROID_API, value);

}
}







