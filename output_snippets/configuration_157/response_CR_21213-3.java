//<Beginning of snippet n. 0>
if (name != null) {
    String newValue;
    if (combinePackage) {
        newValue = RefactoringUtil.getNewValue(getAppPackage(), name.trim(), newName.trim());
    } else {
        newValue = newName.trim();
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
    if (oldName == null || oldName.trim().length() == 0) {
        return null;
    }
    if (javaPackage == null || javaPackage.trim().length() == 0) {
        return null;
    }
    if (newName == null || newName.trim().length() == 0) {
        return null;
    }

    javaPackage = javaPackage.trim();
    newName = newName.trim();

    if (!newName.startsWith(javaPackage + ".")) { //$NON-NLS-1$
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

public static void updateManifest(String newValue) {
    try {
        File manifestFile = new File("AndroidManifest.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(manifestFile);
        doc.getDocumentElement().normalize();

        NodeList nodeList = doc.getElementsByTagName("activity");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String activityName = element.getAttribute("android:name");
                if (activityName.equals(newValue)) {
                    element.setAttribute("android:name", newValue); // Update the activity name
                }
            }
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(manifestFile);
        transformer.transform(source, result);
    } catch (Exception e) {
        Logger logger = Logger.getLogger("ManifestUpdater");
        logger.log(Level.SEVERE, "Error updating manifest: ", e);
    }
}
//<End of snippet n. 1>