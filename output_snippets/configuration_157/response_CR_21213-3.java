//<Beginning of snippet n. 0>
if (name != null) {
    String newValue;
    if (combinePackage) {
        newValue = RefactoringUtil.getNewValue(getAppPackage(), name, newName);
        if (newValue == null) {
            throw new IllegalArgumentException("Invalid newName or package structure.");
        }
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
    if (oldName == null || oldName.length() == 0 || !isValidName(oldName)) {
        throw new IllegalArgumentException("Invalid oldName.");
    }
    if (javaPackage == null || javaPackage.length() == 0 || !isValidPackage(javaPackage)) {
        throw new IllegalArgumentException("Invalid javaPackage.");
    }
    if (newName == null || newName.length() == 0 || !isValidName(newName)) {
        throw new IllegalArgumentException("Invalid newName.");
    }
    
    if (!newName.startsWith(javaPackage + ".")) {
        return newName;
    } else if (newName.length() > (javaPackage.length() + 1)) {
        return newName.substring(javaPackage.length() + 1);
    }
    boolean startWithDot = (oldName.charAt(0) == '.');
    boolean hasDot = (oldName.indexOf('.') != -1);
    if (startWithDot || !hasDot) {
        return startWithDot ? "." + newName : newName.substring(newName.lastIndexOf(".") + 1);
    } else {
        return newName;
    }
}

private static boolean isValidName(String name) {
    return name.matches("^[\\p{L}_][\\p{L}\\p{N}_]*$");
}

private static boolean isValidPackage(String javaPackage) {
    return javaPackage.matches("^[a-zA-Z_]+(\\.[a-zA-Z_][\\w]*)*$");
}
//<End of snippet n. 1>