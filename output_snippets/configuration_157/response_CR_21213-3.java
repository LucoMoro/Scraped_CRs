//<Beginning of snippet n. 0>
if (name != null && !name.trim().isEmpty()) {
    String newValue;
    if (combinePackage) {
        newValue = RefactoringUtil.getNewValue(getAppPackage(), name.trim(), newName.trim());
    } else {
        newValue = newName.trim();
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
    if (oldName == null || oldName.trim().isEmpty()) {
        throw new IllegalArgumentException("oldName must not be null or empty");
    }
    if (javaPackage == null || javaPackage.trim().isEmpty()) {
        throw new IllegalArgumentException("javaPackage must not be null or empty");
    }
    if (newName == null || newName.trim().isEmpty()) {
        throw new IllegalArgumentException("newName must not be null or empty");
    }
    newName = newName.trim();
    if (!newName.startsWith(javaPackage + ".")) {
        return newName;
    } else if (newName.length() > (javaPackage.length() + 1)) {
        String value = newName.substring(javaPackage.length() + 1).trim();
        return value.isEmpty() ? newName : value;
    }
    boolean startWithDot = (oldName.charAt(0) == '.');
    boolean hasDot = (oldName.indexOf('.') != -1);
    if (startWithDot || !hasDot) {
        if (startWithDot) {
            return "." + newName;
        } else {
            int lastPeriod = newName.lastIndexOf(".");
            if (lastPeriod != -1 && lastPeriod + 1 < newName.length()) {
                return newName.substring(lastPeriod + 1).trim();
            }
        }
    }
    return newName;
}
//<End of snippet n. 1>