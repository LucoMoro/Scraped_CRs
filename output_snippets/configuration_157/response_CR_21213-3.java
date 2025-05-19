//<Beginning of snippet n. 0>

if (name != null) {
    String newValue;
    if (combinePackage) {
        newValue = RefactoringUtil.getNewValue(getAppPackage(), name, newName);
    } else {
        newValue = validateAndTrimName(newName);
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
 * @throws IllegalArgumentException if any input is invalid
 */
public static String getNewValue(String javaPackage, String oldName, String newName) {
    validateInputs(javaPackage, oldName, newName);
    String trimmedNewName = newName.trim();
    if (!trimmedNewName.startsWith(javaPackage + ".")) {
        return trimmedNewName;
    } else if (trimmedNewName.length() > (javaPackage.length() + 1)) {
        String value = trimmedNewName.substring(javaPackage.length() + 1);
        return value;
    }
    boolean startWithDot = (oldName.charAt(0) == '.');
    boolean hasDot = (oldName.indexOf('.') != -1);
    if (startWithDot || !hasDot) {
        return startWithDot ? "." + trimmedNewName : trimmedNewName.substring(trimmedNewName.lastIndexOf(".") + 1);
    } else {
        return trimmedNewName;
    }
}

private static void validateInputs(String javaPackage, String oldName, String newName) {
    if (oldName == null || oldName.trim().isEmpty()) {
        throw new IllegalArgumentException("Invalid oldName: " + oldName);
    }
    if (javaPackage == null || javaPackage.trim().isEmpty()) {
        throw new IllegalArgumentException("Invalid javaPackage: " + javaPackage);
    }
    if (newName == null || newName.trim().isEmpty()) {
        throw new IllegalArgumentException("Invalid newName: " + newName);
    }
}

private static String validateAndTrimName(String name) {
    if (name == null) {
        throw new IllegalArgumentException("Invalid name: null");
    }
    return name.trim();
}

/**
 * Releases SSE read model; saves SSE model if exists edit model
 * Called in dispose method of refactoring change classes
 */

//<End of snippet n. 1>