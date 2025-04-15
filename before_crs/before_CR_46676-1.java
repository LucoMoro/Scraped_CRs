/*Fix device writer to write out XML decimals properly

Change-Id:I483f1e3dd02a1ac61a06707bcf33ca1c9bb6f034*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceWriter.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceWriter.java
//Synthetic comment -- index d80f91e..11b1c6d 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import java.awt.Point;
import java.io.OutputStream;
import java.util.Collection;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
//Synthetic comment -- @@ -136,7 +137,7 @@

addElement(doc, screen, DeviceSchema.NODE_SCREEN_SIZE, s.getSize().getResourceValue());
addElement(doc, screen, DeviceSchema.NODE_DIAGONAL_LENGTH,
                String.format("%.2f",s.getDiagonalLength()));
addElement(doc, screen, DeviceSchema.NODE_PIXEL_DENSITY,
s.getPixelDensity().getResourceValue());
addElement(doc, screen, DeviceSchema.NODE_SCREEN_RATIO, s.getRatio().getResourceValue());
//Synthetic comment -- @@ -148,8 +149,10 @@
Integer.toString(s.getXDimension()));
addElement(doc, dimensions, DeviceSchema.NODE_Y_DIMENSION,
Integer.toString(s.getYDimension()));
        addElement(doc, screen, DeviceSchema.NODE_XDPI, String.format("%.2f", s.getXdpi()));
        addElement(doc, screen, DeviceSchema.NODE_YDPI, String.format("%.2f", s.getYdpi()));

Element touch = doc.createElement(PREFIX + DeviceSchema.NODE_TOUCH);
screen.appendChild(touch);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/devices/DeviceWriterTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/devices/DeviceWriterTest.java
//Synthetic comment -- index 2b8474e..e8053be 100644

//Synthetic comment -- @@ -25,8 +25,10 @@
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceWriterTest extends TestCase {

public void testWriteIsValid() throws Exception {
//Synthetic comment -- @@ -46,6 +48,25 @@
}
}

public void testApiLowerBound() throws Exception {
Map<String, String> replacements = new HashMap<String, String>();
replacements.put("api-level", "1-");







