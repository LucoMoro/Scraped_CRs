//<Beginning of snippet n. 0>
if (name != null) {
    String newValue;
    if (combinePackage) {
        newValue = RefactoringUtil.getNewValue(getAppPackage(), name, newName);
    } else {
        newValue = newName;
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
private static boolean sRefactorAppPackage = false;

/**
 * Returns the new class name combined with a package name
 * the oldName and newName are class names as found in the manifest
 * (for instance with a leading dot or with a single element,
 * that needs to be recombined with a package name)
 *
 * @param javaPackage the package name
 * @param oldName the old name
 * @param newName the new name
 *
 * @return the new name
 */
public static String getNewValue(String javaPackage, String oldName, String newName) {
    if (oldName == null || oldName.isEmpty()) {
        throw new IllegalArgumentException("Old name cannot be null or empty");
    }
    if (javaPackage == null || javaPackage.isEmpty()) {
        throw new IllegalArgumentException("Java package cannot be null or empty");
    }
    if (newName == null || newName.isEmpty()) {
        throw new IllegalArgumentException("New name cannot be null or empty");
    }
    if (newName.equals(oldName)) {
        return newName;
    }
    if (!newName.startsWith(javaPackage + ".")) {
        return newName;
    } else if (newName.length() > (javaPackage.length() + 1)) {
        String value = newName.substring(javaPackage.length() + 1);
        return value;
    }
    boolean startWithDot = (oldName.charAt(0) == '.');
    boolean hasDot = (oldName.indexOf('.') != -1);
    if (startWithDot || !hasDot) {
        if (startWithDot) {
            return "." + newName;
        } else {
            int lastPeriod = newName.lastIndexOf(".");
            return newName.substring(lastPeriod + 1);
        }
    } else {
        return newName;
    }
}
/**
* Releases SSE read model; saves SSE model if exists edit model
* Called in dispose method of refactoring change classes
*/
//<End of snippet n. 1>