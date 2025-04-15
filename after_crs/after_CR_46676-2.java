/*Fix device writer to write out XML decimals properly

Change-Id:I483f1e3dd02a1ac61a06707bcf33ca1c9bb6f034*/




//Synthetic comment -- diff --git a/apps/DeviceConfig/src/com/example/android/deviceconfig/ConfigGenerator.java b/apps/DeviceConfig/src/com/example/android/deviceconfig/ConfigGenerator.java
//Synthetic comment -- index bc2ca7f..865f23c 100644

//Synthetic comment -- @@ -46,6 +46,7 @@
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
//Synthetic comment -- @@ -219,7 +220,7 @@
double xin = metrics.widthPixels / metrics.xdpi;
double yin = metrics.heightPixels / metrics.ydpi;
double diag = Math.sqrt(Math.pow(xin, 2) + Math.pow(yin, 2));
            diagonalLength.appendChild(doc.createTextNode(String.format(Locale.US, "%1$.2f", diag)));

Element pixelDensity = doc.createElement(PREFIX + NODE_PIXEL_DENSITY);
screen.appendChild(pixelDensity);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceWriter.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceWriter.java
//Synthetic comment -- index d80f91e..11b1c6d 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import java.awt.Point;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Locale;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
//Synthetic comment -- @@ -136,7 +137,7 @@

addElement(doc, screen, DeviceSchema.NODE_SCREEN_SIZE, s.getSize().getResourceValue());
addElement(doc, screen, DeviceSchema.NODE_DIAGONAL_LENGTH,
                String.format(Locale.US, "%.2f",s.getDiagonalLength()));
addElement(doc, screen, DeviceSchema.NODE_PIXEL_DENSITY,
s.getPixelDensity().getResourceValue());
addElement(doc, screen, DeviceSchema.NODE_SCREEN_RATIO, s.getRatio().getResourceValue());
//Synthetic comment -- @@ -148,8 +149,10 @@
Integer.toString(s.getXDimension()));
addElement(doc, dimensions, DeviceSchema.NODE_Y_DIMENSION,
Integer.toString(s.getYDimension()));
        addElement(doc, screen, DeviceSchema.NODE_XDPI, String.format(Locale.US,
                "%.2f", s.getXdpi()));
        addElement(doc, screen, DeviceSchema.NODE_YDPI, String.format(Locale.US,
                "%.2f", s.getYdpi()));

Element touch = doc.createElement(PREFIX + DeviceSchema.NODE_TOUCH);
screen.appendChild(touch);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/devices/DeviceWriterTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/devices/DeviceWriterTest.java
//Synthetic comment -- index 2b8474e..e8053be 100644

//Synthetic comment -- @@ -25,8 +25,10 @@
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@SuppressWarnings("javadoc")
public class DeviceWriterTest extends TestCase {

public void testWriteIsValid() throws Exception {
//Synthetic comment -- @@ -46,6 +48,25 @@
}
}

    public void testLocale() throws Exception {
        Locale prevLocale = Locale.getDefault();
        try {
            Locale.setDefault(Locale.FRANCE);
            InputStream devicesFile =
                DeviceSchemaTest.class.getResourceAsStream("devices.xml");
            List<Device> devices = DeviceParser.parse(devicesFile);
            assertEquals("Parsed devices contained an un expected number of devices",
                    2, devices.size());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DeviceWriter.writeToXml(baos, devices);
            String xml = baos.toString();
            assertTrue(xml.contains(".00"));
            assertFalse(xml.contains(",00"));
        } finally {
            Locale.setDefault(prevLocale);
        }
    }

public void testApiLowerBound() throws Exception {
Map<String, String> replacements = new HashMap<String, String>();
replacements.put("api-level", "1-");







