/*CTS would report 'too many files open' because it did not close .xml output files

Change-Id:If4986dfe8c9996f297b012cc455c3782a5b57f51*/




//Synthetic comment -- diff --git a/tools/host/src/com/android/cts/ConsoleUi.java b/tools/host/src/com/android/cts/ConsoleUi.java
//Synthetic comment -- index 27173a4..679ecc0 100644

//Synthetic comment -- @@ -970,7 +970,7 @@
* @param resultType The result type.
*/
private void createPlanFromSession(final String name, TestSession ts, final String resultType)
            throws FileNotFoundException, IOException, ParserConfigurationException,
TransformerFactoryConfigurationError, TransformerException {

HashMap<String, ArrayList<String>> selectedResult =








//Synthetic comment -- diff --git a/tools/host/src/com/android/cts/TestSessionBuilder.java b/tools/host/src/com/android/cts/TestSessionBuilder.java
//Synthetic comment -- index c592476..b4e69f1 100644

//Synthetic comment -- @@ -544,7 +544,7 @@
*/
public void serialize(String planName,
ArrayList<String> packageNames, HashMap<String, ArrayList<String>> selectedResult)
            throws ParserConfigurationException, FileNotFoundException, IOException,
TransformerFactoryConfigurationError, TransformerException {
File plan = new File(HostConfig.getInstance().getPlanRepository()
.getPlanPath(planName));








//Synthetic comment -- diff --git a/tools/host/src/com/android/cts/XMLResourceHandler.java b/tools/host/src/com/android/cts/XMLResourceHandler.java
//Synthetic comment -- index f77b27b..f44a574 100644

//Synthetic comment -- @@ -18,6 +18,7 @@
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
//Synthetic comment -- @@ -125,11 +126,16 @@
* @param doc DOM Document
*/
protected static void writeToFile(File file, Document doc) throws FileNotFoundException,
            IOException, TransformerFactoryConfigurationError, TransformerException {
Transformer t = TransformerFactory.newInstance().newTransformer();
// enable indent in result file
t.setOutputProperty("indent", "yes");
        FileOutputStream fos = new FileOutputStream(file);
        try {
            StreamResult sr = new StreamResult(fos);
            t.transform(new DOMSource(doc), sr);
        } finally {
            fos.close();
        }
}
}







