/*Added DeviceWriter and equals for Devices

Change-Id:I6c9a162bc414d3d8252dbbb5a37bb4d2ed1d6058*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java
//Synthetic comment -- index 24b9260..9e65765 100644

//Synthetic comment -- @@ -274,6 +274,11 @@
public final static String NS_RESOURCES =
"http://schemas.android.com/apk/res/android";                   //$NON-NLS-1$

    /** Namespace for the device schema, i.e. "http://schemas.android.com/sdk/devices/1" */
    public static final String NS_DEVICES_XSD =
        "http://schemas.android.com/sdk/devices/1";                     //$NON-NLS-1$


/** The name of the uses-library that provides "android.test.runner" */
public final static String ANDROID_TEST_RUNNER_LIB =
"android.test.runner";                                          //$NON-NLS-1$








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Camera.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Camera.java
//Synthetic comment -- index 39cfd29..ea29171 100644

//Synthetic comment -- @@ -46,4 +46,27 @@
c.mFlash = mFlash;
return c;
}
\ No newline at end of file

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Camera)) {
            return false;
        }
        Camera c = (Camera) o;
        return mLocation == c.mLocation
                && mAutofocus == c.hasAutofocus()
                && mFlash == c.hasFlash();
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + mLocation.hashCode();
        hash = 31 * hash + (mAutofocus ? 1 : 0);
        hash = 31 * hash + (mFlash ? 1 : 0);
        return hash;
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Device.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Device.java
//Synthetic comment -- index ba2187f..3c868e8 100644

//Synthetic comment -- @@ -221,4 +221,33 @@
mMeta = b.mMeta;
mDefaultState = b.mDefaultState;
}

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Device)) {
            return false;
        }
        Device d = (Device) o;
        return mName.equals(d.getName())
                && mManufacturer.equals(d.getManufacturer())
                && mSoftware.equals(d.getAllSoftware())
                && mState.equals(d.getAllStates())
                && mMeta.equals(d.getMeta())
                && mDefaultState.equals(d.getDefaultState());
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + mName.hashCode();
        hash = 31 * hash + mManufacturer.hashCode();
        hash = 31 * hash + mSoftware.hashCode();
        hash = 31 * hash + mState.hashCode();
        hash = 31 * hash + mMeta.hashCode();
        hash = 31 * hash + mDefaultState.hashCode();
        return hash;
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java
//Synthetic comment -- index 7825215..1195548 100644

//Synthetic comment -- @@ -53,9 +53,6 @@
private final static String sDeviceProfilesProp = "DeviceProfiles";
private final static Pattern sPathPropertyPattern = Pattern.compile("^" + PkgProps.EXTRA_PATH
+ "=" + sDeviceProfilesProp + "$");
private ISdkLog mLog;
private List<Device> mVendorDevices;
private List<Device> mUserDevices;
//Synthetic comment -- @@ -145,8 +142,10 @@
props.put("hw.audioInput", getBooleanVal(hw.hasMic()));
props.put("hw.sdCard", getBooleanVal(hw.getRemovableStorage().size() > 0));
props.put("hw.sdCard", getBooleanVal(hw.getRemovableStorage().size() > 0));
        props.put("hw.lcd.density",
                Integer.toString(hw.getScreen().getPixelDensity().getDpiValue()));
        props.put("hw.sensors.proximity",
                getBooleanVal(sensors.contains(Sensor.PROXIMITY_SENSOR)));
return props;
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceWriter.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceWriter.java
new file mode 100644
//Synthetic comment -- index 0000000..3ecc55e

//Synthetic comment -- @@ -0,0 +1,290 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.sdklib.devices;

import com.android.dvlib.DeviceSchema;
import com.android.resources.UiMode;
import com.android.sdklib.SdkConstants;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.Point;
import java.io.OutputStream;
import java.util.Collection;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class DeviceWriter {

    public static final String LOCAL_NS = "d";
    public static final String PREFIX = LOCAL_NS + ":";

    /**
     * Writes the XML definition of the given {@link Collection} of {@link Device}s according to
     * {@value #NS_DEVICES_XSD} to the {@link OutputStream}. Note that it is up to the caller to
     * close the {@link OutputStream}.
     * @param out The {@link OutputStream} to write the resulting XML to.
     * @param devices The {@link Device}s from which to generate the XML.
     * @throws ParserConfigurationException
     * @throws TransformerFactoryConfigurationError
     * @throws TransformerException
     */
    public static void writeToXml(OutputStream out, Collection<Device> devices) throws
            ParserConfigurationException,
            TransformerFactoryConfigurationError,
            TransformerException {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = doc.createElement(PREFIX + DeviceSchema.NODE_DEVICES);
        root.setAttribute(XMLConstants.XMLNS_ATTRIBUTE + ":xsi",
                XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI);
        root.setAttribute(XMLConstants.XMLNS_ATTRIBUTE + ":" + LOCAL_NS, SdkConstants.NS_DEVICES_XSD);
        doc.appendChild(root);

        for (Device device : devices) {
            Element deviceNode = doc.createElement(PREFIX + DeviceSchema.NODE_DEVICE);
            root.appendChild(deviceNode);

            Element name = doc.createElement(PREFIX + DeviceSchema.NODE_NAME);
            name.appendChild(doc.createTextNode(device.getName()));
            deviceNode.appendChild(name);

            Element manufacturer = doc.createElement(PREFIX + DeviceSchema.NODE_MANUFACTURER);
            manufacturer.appendChild(doc.createTextNode(device.getManufacturer()));
            deviceNode.appendChild(manufacturer);

            deviceNode.appendChild(generateMetaNode(device.getMeta(), doc));
            deviceNode.appendChild(generateHardwareNode(device.getDefaultHardware(), doc));
            for (Software sw : device.getAllSoftware()) {
                deviceNode.appendChild(generateSoftwareNode(sw, doc));
            }
            for (State s : device.getAllStates()) {
                deviceNode.appendChild(generateStateNode(s, doc, device.getDefaultHardware()));
            }
        }

        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(out);
        tf.transform(source, result);
    }

    /* This returns the XML Element for the given instance of Meta */
    private static Node generateMetaNode(Meta meta, Document doc) {
        Element m = doc.createElement(PREFIX + DeviceSchema.NODE_META);
        if (meta.hasIconSixtyFour() || meta.hasIconSixteen()) {
            Element icons = doc.createElement(PREFIX + DeviceSchema.NODE_ICONS);
            m.appendChild(icons);
            if (meta.hasIconSixtyFour()) {
                addElement(doc, icons, DeviceSchema.NODE_SIXTY_FOUR,
                        meta.getIconSixtyFour().getPath());
            }
            if (meta.hasIconSixteen()) {
                addElement(doc, icons, DeviceSchema.NODE_SIXTEEN, meta.getIconSixteen().getPath());
            }
        }

        if (meta.hasFrame()) {
            Element frame = doc.createElement(PREFIX + DeviceSchema.NODE_FRAME);
            addElement(doc, frame, DeviceSchema.NODE_PATH, meta.getFrame().getPath());
            Point offset = meta.getFrameOffsetPortrait();
            addElement(doc, frame, DeviceSchema.NODE_PORTRAIT_X_OFFSET, Integer.toString(offset.x));
            addElement(doc, frame, DeviceSchema.NODE_PORTRAIT_Y_OFFSET, Integer.toString(offset.y));
            offset = meta.getFrameOffsetLandscape();
            addElement(doc, frame, DeviceSchema.NODE_LANDSCAPE_X_OFFSET,
                    Integer.toString(offset.x));
            addElement(doc, frame, DeviceSchema.NODE_LANDSCAPE_Y_OFFSET,
                    Integer.toString(offset.y));
        }

        return m;
    }

    /* This returns the XML Element for the given instance of Hardware */
    private static Element generateHardwareNode(Hardware hw, Document doc) {
        Screen s = hw.getScreen();
        Element hardware = doc.createElement(PREFIX + DeviceSchema.NODE_HARDWARE);
        Element screen = doc.createElement(PREFIX + DeviceSchema.NODE_SCREEN);
        hardware.appendChild(screen);

        addElement(doc, screen, DeviceSchema.NODE_SCREEN_SIZE, s.getSize().getResourceValue());
        addElement(doc, screen, DeviceSchema.NODE_DIAGONAL_LENGTH,
                String.format("%.2f",s.getDiagonalLength()));
        addElement(doc, screen, DeviceSchema.NODE_PIXEL_DENSITY,
                s.getPixelDensity().getResourceValue());
        addElement(doc, screen, DeviceSchema.NODE_SCREEN_RATIO, s.getRatio().getResourceValue());

        Element dimensions = doc.createElement(PREFIX + DeviceSchema.NODE_DIMENSIONS);
        screen.appendChild(dimensions);

        addElement(doc, dimensions, DeviceSchema.NODE_X_DIMENSION,
                Integer.toString(s.getXDimension()));
        addElement(doc, dimensions, DeviceSchema.NODE_Y_DIMENSION,
                Integer.toString(s.getYDimension()));
        addElement(doc, screen, DeviceSchema.NODE_XDPI, String.format("%.2f", s.getXdpi()));
        addElement(doc, screen, DeviceSchema.NODE_YDPI, String.format("%.2f", s.getYdpi()));

        Element touch = doc.createElement(PREFIX + DeviceSchema.NODE_TOUCH);
        screen.appendChild(touch);

        addElement(doc, touch, DeviceSchema.NODE_MULTITOUCH, s.getMultitouch().toString());
        addElement(doc, touch, DeviceSchema.NODE_MECHANISM, s.getMechanism().getResourceValue());
        addElement(doc, touch, DeviceSchema.NODE_SCREEN_TYPE, s.getScreenType().toString());

        addElement(doc, hardware, DeviceSchema.NODE_NETWORKING, hw.getNetworking());
        addElement(doc, hardware, DeviceSchema.NODE_SENSORS, hw.getSensors());
        addElement(doc, hardware, DeviceSchema.NODE_MIC, Boolean.toString(hw.hasMic()));

        for(Camera c : hw.getCameras()) {
            Element camera  = doc.createElement(PREFIX + DeviceSchema.NODE_CAMERA);
            hardware.appendChild(camera);
            addElement(doc, camera, DeviceSchema.NODE_LOCATION, c.getLocation().toString());
            addElement(doc, camera, DeviceSchema.NODE_AUTOFOCUS,
                    Boolean.toString(c.hasAutofocus()));
            addElement(doc, camera, DeviceSchema.NODE_FLASH, Boolean.toString(c.hasFlash()));
        }

        addElement(doc, hardware, DeviceSchema.NODE_KEYBOARD, hw.getKeyboard().getResourceValue());
        addElement(doc, hardware, DeviceSchema.NODE_NAV, hw.getNav().getResourceValue());

        Storage.Unit unit = hw.getRam().getApproriateUnits();
        Element ram = addElement(doc, hardware, DeviceSchema.NODE_RAM,
                Long.toString(hw.getRam().getSizeAsUnit(unit)));
        ram.setAttribute(DeviceSchema.ATTR_UNIT, unit.toString());

        addElement(doc, hardware, DeviceSchema.NODE_BUTTONS, hw.getButtonType().toString());
        addStorageElement(doc, hardware, DeviceSchema.NODE_INTERNAL_STORAGE,
                hw.getInternalStorage());
        addStorageElement(doc, hardware, DeviceSchema.NODE_REMOVABLE_STORAGE,
                hw.getRemovableStorage());
        addElement(doc, hardware, DeviceSchema.NODE_CPU, hw.getCpu());
        addElement(doc, hardware, DeviceSchema.NODE_GPU, hw.getGpu());
        addElement(doc, hardware, DeviceSchema.NODE_ABI, hw.getSupportedAbis());

        StringBuilder sb = new StringBuilder();
        for (UiMode u : hw.getSupportedUiModes()) {
            sb.append("\n" + u.getResourceValue());
        }
        addElement(doc, hardware, DeviceSchema.NODE_DOCK, sb.toString());

        addElement(doc, hardware, DeviceSchema.NODE_POWER_TYPE, hw.getChargeType().toString());

        return hardware;
    }

    /* This returns the XML Element for the given instance of Software */
    private static Element generateSoftwareNode(Software sw, Document doc) {
        Element software = doc.createElement(PREFIX + DeviceSchema.NODE_SOFTWARE);

        String apiVersion = "";
        if (sw.getMinSdkLevel() != 0) {
            apiVersion += Integer.toString(sw.getMinSdkLevel());
        }
        apiVersion += "-";
        if (sw.getMaxSdkLevel() != Integer.MAX_VALUE) {
            apiVersion += Integer.toString(sw.getMaxSdkLevel());
        }
        addElement(doc, software, DeviceSchema.NODE_API_LEVEL, apiVersion);
        addElement(doc, software, DeviceSchema.NODE_LIVE_WALLPAPER_SUPPORT,
                Boolean.toString(sw.hasLiveWallpaperSupport()));
        addElement(doc, software, DeviceSchema.NODE_BLUETOOTH_PROFILES, sw.getBluetoothProfiles());
        addElement(doc, software, DeviceSchema.NODE_GL_VERSION, sw.getGlVersion());
        addElement(doc, software, DeviceSchema.NODE_GL_EXTENSIONS, sw.getGlExtensions());

        return software;
    }

    /* This returns the XML Element for the given instance of State */
    private static Element generateStateNode(State s, Document doc, Hardware defaultHardware) {
        Element state = doc.createElement(PREFIX + DeviceSchema.NODE_STATE);
        state.setAttribute(DeviceSchema.ATTR_NAME, s.getName());
        if (s.isDefaultState()) {
            state.setAttribute(DeviceSchema.ATTR_DEFAULT, Boolean.toString(s.isDefaultState()));
        }
        addElement(doc, state, DeviceSchema.NODE_DESCRIPTION, s.getDescription());
        addElement(doc, state, DeviceSchema.NODE_SCREEN_ORIENTATION,
                s.getOrientation().getResourceValue());
        addElement(doc, state, DeviceSchema.NODE_KEYBOARD_STATE,
                s.getKeyState().getResourceValue());
        addElement(doc, state, DeviceSchema.NODE_NAV_STATE, s.getNavState().getResourceValue());

        // Only if the hardware is different do we want to append hardware values
        if (!s.getHardware().equals(defaultHardware)){
            // TODO: Only append nodes which are different from the default hardware
            Element hardware = generateHardwareNode(s.getHardware(), doc);
            NodeList children = hardware.getChildNodes();
            for (int i = 0 ; i < children.getLength(); i++) {
                Node child = children.item(i);
                state.appendChild(child);
            }
        }
        return state;
    }

    private static Element addElement(Document doc, Element parent, String tag, String content) {
        Element child = doc.createElement(PREFIX + tag);
        child.appendChild(doc.createTextNode(content));
        parent.appendChild(child);
        return child;
    }

    private static Element addElement(Document doc, Element parent, String tag,
            Collection<? extends Object> content) {
        StringBuilder sb = new StringBuilder();
        for (Object o : content) {
            sb.append("\n" + o.toString());
        }
        return addElement(doc, parent,  tag, sb.toString());
    }

    /* This adds generates the XML for a Collection<Storage> and appends it to the parent. Note
     * that it picks the proper unit for the unit attribute and sets it on the node.
     */
    private static Element addStorageElement(Document doc, Element parent, String tag,
            Collection<Storage> content){
        Storage.Unit unit = Storage.Unit.TiB;

        // Get the lowest common unit (so if one piece of storage is 128KiB and another is 1MiB,
        // use KiB for units)
        for(Storage storage : content) {
            if(storage.getApproriateUnits().getNumberOfBytes() < unit.getNumberOfBytes()) {
                unit = storage.getApproriateUnits();
            }
        }

        StringBuilder sb = new StringBuilder();
        for(Storage storage : content) {
            sb.append("\n" + storage.getSizeAsUnit(unit));
        }
        Element storage = addElement(doc, parent, tag, sb.toString());
        storage.setAttribute(DeviceSchema.ATTR_UNIT, unit.toString());
        return storage;
    }

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Hardware.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Hardware.java
//Synthetic comment -- index b74aa5b..272b9bb 100644

//Synthetic comment -- @@ -115,7 +115,7 @@
public PowerType getChargeType() {
return mPluggedIn;
}

public Screen getScreen() {
return mScreen;
}
//Synthetic comment -- @@ -123,12 +123,12 @@
/**
* Returns a copy of the object that shares no state with it,
* but is initialized to equivalent values.
     *
* @return A copy of the object.
*/
public Hardware deepCopy() {
Hardware hw = new Hardware();
        hw.mScreen = mScreen.deepCopy();
hw.mNetworking = new HashSet<Network>(mNetworking);
hw.mSensors = new HashSet<Sensor>(mSensors);
// Get the constant boolean value
//Synthetic comment -- @@ -156,4 +156,54 @@
hw.mPluggedIn = mPluggedIn;
return hw;
}

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Hardware)) {
            return false;
        }
        Hardware hw = (Hardware) o;
        return mScreen.equals(hw.getScreen())
                && mNetworking.equals(hw.getNetworking())
                && mSensors.equals(hw.getSensors())
                && mMic == hw.hasMic()
                && mCameras.equals(hw.getCameras())
                && mKeyboard == hw.getKeyboard()
                && mNav == hw.getNav()
                && mRam.equals(hw.getRam())
                && mButtons == hw.getButtonType()
                && mInternalStorage.equals(hw.getInternalStorage())
                && mRemovableStorage.equals(hw.getRemovableStorage())
                && mCpu.equals(hw.getCpu())
                && mGpu.equals(hw.getGpu())
                && mAbis.equals(hw.getSupportedAbis())
                && mUiModes.equals(hw.getSupportedUiModes())
                && mPluggedIn == hw.getChargeType();

    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + mScreen.hashCode();
        hash = 31 * hash + mNetworking.hashCode();
        hash = 31 * hash + mSensors.hashCode();
        hash = 31 * hash + (mMic ? 1 : 0);
        hash = 31 * hash + mCameras.hashCode();
        hash = 31 * hash + mKeyboard.hashCode();
        hash = 31 * hash + mNav.hashCode();
        hash = 31 * hash + mRam.hashCode();
        hash = 31 * hash + mButtons.hashCode();
        hash = 31 * hash + mInternalStorage.hashCode();
        hash = 31 * hash + mRemovableStorage.hashCode();
        hash = 31 * hash + mCpu.hashCode();
        hash = 31 * hash + mGpu.hashCode();
        hash = 31 * hash + mAbis.hashCode();
        hash = 31 * hash + mUiModes.hashCode();
        hash = 31 * hash + mPluggedIn.hashCode();
        return hash;
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Meta.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Meta.java
//Synthetic comment -- index a773005..6e8ae73 100644

//Synthetic comment -- @@ -69,4 +69,74 @@
public Point getFrameOffsetPortrait() {
return mFrameOffsetPortrait;
}

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Meta)) {
            return false;
        }
        Meta m = (Meta) o;

        // Note that any of the fields of either object can be null
        if (mIconSixtyFour != null && !mIconSixtyFour.equals(m.getIconSixtyFour())){
            return false;
        } else if (m.getIconSixtyFour() != null && !m.getIconSixtyFour().equals(mIconSixtyFour)) {
            return false;
        }

        if (mIconSixteen != null && !mIconSixteen.equals(m.getIconSixteen())){
            return false;
        } else if (m.getIconSixteen() != null && !m.getIconSixteen().equals(mIconSixteen)) {
            return false;
        }

        if (mFrame != null && !mFrame.equals(m.getFrame())) {
            return false;
        } else if (m.getFrame() != null && !m.getFrame().equals(mFrame)) {
            return false;
        }

        if (mFrameOffsetLandscape != null
                && !mFrameOffsetLandscape.equals(m.getFrameOffsetLandscape())){
            return false;
        } else if (m.getFrameOffsetLandscape() != null
                && !m.getFrameOffsetLandscape().equals(mFrameOffsetLandscape)){
            return false;
        }


        if (mFrameOffsetPortrait != null
                && !mFrameOffsetPortrait.equals(m.getFrameOffsetPortrait())){
            return false;
        } else if (m.getFrameOffsetPortrait() != null
                && !m.getFrameOffsetPortrait().equals(mFrameOffsetPortrait)){
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if(mIconSixteen != null){
            hash = 31 * hash + mIconSixteen.hashCode();
        }
        if(mIconSixtyFour != null){
            hash = 31 * hash + mIconSixtyFour.hashCode();
        }
        if(mFrame != null){
            hash = 31 * hash + mFrame.hashCode();
        }
        if(mFrameOffsetLandscape != null){
            hash = 31 * hash + mFrameOffsetLandscape.hashCode();
        }
        if(mFrameOffsetPortrait != null){
            hash = 31 * hash + mFrameOffsetPortrait.hashCode();
        }
        return hash;
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Screen.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Screen.java
//Synthetic comment -- index 035fc46..5943988 100644

//Synthetic comment -- @@ -34,7 +34,7 @@
Multitouch mMultitouch;
TouchScreen mMechanism;
ScreenType mScreenType;

public ScreenSize getSize() {
return mScreenSize;
}
//Synthetic comment -- @@ -50,11 +50,11 @@
public int getXDimension() {
return mXDimension;
}

public int getYDimension() {
return mYDimension;
}

public double getXdpi() {
return mXdpi;
}
//Synthetic comment -- @@ -70,4 +70,68 @@
public ScreenType getScreenType() {
return mScreenType;
}
\ No newline at end of file

    /**
     * Returns a copy of the object that shares no state with it,
     * but is initialized to equivalent values.
     *
     * @return A copy of the object.
     */
    public Screen deepCopy() {
        Screen s = new Screen();
        s.mScreenSize = mScreenSize;
        s.mDiagonalLength = mDiagonalLength;
        s.mPixelDensity = mPixelDensity;
        s.mScreenRatio = mScreenRatio;
        s.mXDimension = mXDimension;
        s.mYDimension = mYDimension;
        s.mXdpi = mXdpi;
        s.mYdpi = mYdpi;
        s.mMultitouch = mMultitouch;
        s.mMechanism = mMechanism;
        s.mScreenType = mScreenType;
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Screen)) {
            return false;
        }
        Screen s = (Screen) o;
        return s.mScreenSize == mScreenSize
                && s.mDiagonalLength == mDiagonalLength
                && s.mPixelDensity == mPixelDensity
                && s.mScreenRatio == mScreenRatio
                && s.mXDimension == mXDimension
                && s.mYDimension == mYDimension
                && s.mXdpi == mXdpi
                && s.mYdpi == mYdpi
                && s.mMultitouch == mMultitouch
                && s.mMechanism == mMechanism
                && s.mScreenType == mScreenType;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + mScreenSize.hashCode();
        long f = Double.doubleToLongBits(mDiagonalLength);
        hash = 31 * hash + (int) (f ^ (f >>> 32));
        hash = 31 * hash + mPixelDensity.hashCode();
        hash = 31 * hash + mScreenRatio.hashCode();
        hash = 31 * hash + mXDimension;
        hash = 31 * hash + mYDimension;
        f = Double.doubleToLongBits(mXdpi);
        hash = 31 * hash + (int) (f ^ (f >>> 32));
        f = Double.doubleToLongBits(mYdpi);
        hash = 31 * hash + (int) (f ^ (f >>> 32));
        hash = 31 * hash + mMultitouch.hashCode();
        hash = 31 * hash + mMechanism.hashCode();
        hash = 31 * hash + mScreenType.hashCode();
        return hash;
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Software.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Software.java
//Synthetic comment -- index eb83e4d..7d12298 100644

//Synthetic comment -- @@ -49,4 +49,34 @@
public Set<String> getGlExtensions() {
return mGlExtensions;
}

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Software)) {
            return false;
        }

        Software sw = (Software) o;
        return mMinSdkLevel == sw.getMinSdkLevel()
                && mMaxSdkLevel == sw.getMaxSdkLevel()
                && mLiveWallpaperSupport == sw.hasLiveWallpaperSupport()
                && mBluetoothProfiles.equals(sw.getBluetoothProfiles())
                && mGlVersion.equals(sw.getGlVersion())
                && mGlExtensions.equals(sw.getGlExtensions());
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + mMinSdkLevel;
        hash = 31 * hash + mMaxSdkLevel;
        hash = 31 * hash + (mLiveWallpaperSupport ? 1 : 0);
        hash = 31 * hash + mBluetoothProfiles.hashCode();
        hash = 31 * hash + mGlVersion.hashCode();
        hash = 31 * hash + mGlExtensions.hashCode();
        return hash;
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/State.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/State.java
//Synthetic comment -- index b93a94d..bbd7b9c 100644

//Synthetic comment -- @@ -56,4 +56,35 @@
public Hardware getHardware() {
return mHardwareOverride;
}

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof State)) {
            return false;
        }
        State s = (State) o;
        return mDefaultState == s.isDefaultState()
                && mName.equals(s.getName())
                && mDescription.equals(s.getDescription())
                && mOrientation.equals(s.getOrientation())
                && mKeyState.equals(s.getKeyState())
                && mNavState.equals(s.getNavState())
                && mHardwareOverride.equals(s.getHardware());
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + (mDefaultState ? 1 : 0);
        hash = 31 * hash + mName.hashCode();
        hash = 31 * hash + mDescription.hashCode();
        hash = 31 * hash + mOrientation.hashCode();
        hash = 31 * hash + mKeyState.hashCode();
        hash = 31 * hash + mNavState.hashCode();
        hash = 31 * hash + mHardwareOverride.hashCode();
        return hash;
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Storage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Storage.java
//Synthetic comment -- index 9778154..b30fe6e 100644

//Synthetic comment -- @@ -16,8 +16,6 @@

package com.android.sdklib.devices;

public class Storage {
private long mNoBytes;









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/devices/DeviceWriterTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/devices/DeviceWriterTest.java
new file mode 100644
//Synthetic comment -- index 0000000..2b8474e

//Synthetic comment -- @@ -0,0 +1,127 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.sdklib.devices;

import com.android.dvlib.DeviceSchemaTest;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceWriterTest extends TestCase {

    public void testWriteIsValid() throws Exception {
        InputStream devicesFile =
            DeviceSchemaTest.class.getResourceAsStream("devices.xml");
        List<Device> devices = DeviceParser.parse(devicesFile);
        assertEquals("Parsed devices contained an un expected number of devices",
                2, devices.size());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DeviceWriter.writeToXml(baos, devices);
        List<Device> writtenDevices = DeviceParser.parse(
                new ByteArrayInputStream(baos.toString().getBytes()));
        assertEquals("Writing and reparsing returns a different number of devices",
                devices.size(), writtenDevices.size());
        for (int i = 0; i < devices.size(); i++){
            assertEquals(devices.get(i), writtenDevices.get(i));
        }
    }

    public void testApiLowerBound() throws Exception {
        Map<String, String> replacements = new HashMap<String, String>();
        replacements.put("api-level", "1-");
        InputStream stream = DeviceSchemaTest.getReplacedStream(replacements);
        List<Device> devices = DeviceParser.parse(stream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DeviceWriter.writeToXml(baos, devices);
        List<Device> writtenDevices = DeviceParser.parse(
                new ByteArrayInputStream(baos.toString().getBytes()));
        assertEquals("Writing and reparsing returns a different number of devices",
                devices.size(), writtenDevices.size());
        for (int i = 0; i < devices.size(); i++){
            assertEquals(devices.get(i), writtenDevices.get(i));
        }
    }

    public void testApiUpperBound() throws Exception {
        Map<String, String> replacements = new HashMap<String, String>();
        replacements.put("api-level", "-10");
        InputStream stream = DeviceSchemaTest.getReplacedStream(replacements);
        List<Device> devices = DeviceParser.parse(stream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DeviceWriter.writeToXml(baos, devices);
        List<Device> writtenDevices = DeviceParser.parse(
                new ByteArrayInputStream(baos.toString().getBytes()));
        assertEquals("Writing and reparsing returns a different number of devices",
                devices.size(), writtenDevices.size());
        for (int i = 0; i < devices.size(); i++){
            assertEquals(devices.get(i), writtenDevices.get(i));
        }
    }

    public void testApiNeitherBound() throws Exception {
        Map<String, String> replacements = new HashMap<String, String>();
        replacements.put("api-level", "-");
        InputStream stream = DeviceSchemaTest.getReplacedStream(replacements);
        List<Device> devices = DeviceParser.parse(stream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DeviceWriter.writeToXml(baos, devices);
        List<Device> writtenDevices = DeviceParser.parse(
                new ByteArrayInputStream(baos.toString().getBytes()));
        assertEquals("Writing and reparsing returns a different number of devices",
                devices.size(), writtenDevices.size());
        for (int i = 0; i < devices.size(); i++){
            assertEquals(devices.get(i), writtenDevices.get(i));
        }
    }

    public void testApiBothBound() throws Exception {
        Map<String, String> replacements = new HashMap<String, String>();
        replacements.put("api-level", "9-10");
        InputStream stream = DeviceSchemaTest.getReplacedStream(replacements);
        List<Device> devices = DeviceParser.parse(stream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DeviceWriter.writeToXml(baos, devices);
        List<Device> writtenDevices = DeviceParser.parse(
                new ByteArrayInputStream(baos.toString().getBytes()));
        assertEquals("Writing and reparsing returns a different number of devices",
                devices.size(), writtenDevices.size());
        for (int i = 0; i < devices.size(); i++){
            assertEquals(devices.get(i), writtenDevices.get(i));
        }
    }
    public void testApiSingle() throws Exception {
        Map<String, String> replacements = new HashMap<String, String>();
        replacements.put("api-level", "10");
        InputStream stream = DeviceSchemaTest.getReplacedStream(replacements);
        List<Device> devices = DeviceParser.parse(stream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DeviceWriter.writeToXml(baos, devices);
        List<Device> writtenDevices = DeviceParser.parse(
                new ByteArrayInputStream(baos.toString().getBytes()));
        assertEquals("Writing and reparsing returns a different number of devices",
                devices.size(), writtenDevices.size());
        for (int i = 0; i < devices.size(); i++){
            assertEquals(devices.get(i), writtenDevices.get(i));
        }
    }
}







