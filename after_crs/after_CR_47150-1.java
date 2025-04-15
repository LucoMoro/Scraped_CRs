/*Added missing USB device descriptor fields needed for intent filters

The UsbDevice object is missing the ManufacturerName, ProductName, and
SerialNumber fields.  These are needed by intent filters to further
qualify a USB device that is plugged in while in host mode.  These
fields have been added in the jni UsbHostManager implementation and
propagated through UsbHostManager and UsbDevice implementations.
The UsbSettingsManager implementation has been modified to allow
manufacturer-name, product-name, and serial-number tags in intents.

File changes:
       modified:   api/current.txt
       modified:   core/java/android/hardware/usb/UsbDevice.java
       modified:   services/java/com/android/server/usb/UsbHostManager.java
       modified:   services/java/com/android/server/usb/UsbSettingsManager.java
       modified:   services/jni/com_android_server_UsbHostManager.cpp

Change-Id:I5711097c01a0af7b7c7fe0f257ebd83c3cdf500bSigned-off-by: Robin Cutshaw <robin.cutshaw@gmail.com>*/




//Synthetic comment -- diff --git a/services/java/com/android/server/usb/UsbSettingsManager.java b/services/java/com/android/server/usb/UsbSettingsManager.java
//Synthetic comment -- index df03418..b2c2a3e 100644

//Synthetic comment -- @@ -151,25 +151,40 @@
int count = parser.getAttributeCount();
for (int i = 0; i < count; i++) {
String name = parser.getAttributeName(i);
                String value = parser.getAttributeValue(i);
// Attribute values are ints or strings
                if ("manufacturer-name".equals(name)) {
                    manufacturerName = value;
} else if ("product-name".equals(name)) {
                    productName = value;
} else if ("serial-number".equals(name)) {
                    serialNumber = value;
                } else {
                    int intValue = -1;
                    int radix = 10;
                    if (value != null && value.length() > 2 && value.charAt(0) == '0' &&
                        (value.charAt(1) == 'x' || value.charAt(1) == 'X')) {
                        // allow hex values starting with 0x or 0X
                        radix = 16;
                        value = value.substring(2);
                    }
                    try {
                        intValue = Integer.parseInt(value, radix);
                    } catch (NumberFormatException e) {
                        Slog.e(TAG, "invalid number for field " + name, e);
                        continue;
                    }
                    if ("vendor-id".equals(name)) {
                        vendorId = intValue;
                    } else if ("product-id".equals(name)) {
                        productId = intValue;
                    } else if ("class".equals(name)) {
                        deviceClass = intValue;
                    } else if ("subclass".equals(name)) {
                        deviceSubclass = intValue;
                    } else if ("protocol".equals(name)) {
                        deviceProtocol = intValue;
                    }
}
}
return new DeviceFilter(vendorId, productId,
//Synthetic comment -- @@ -215,11 +230,15 @@
public boolean matches(UsbDevice device) {
if (mVendorId != -1 && device.getVendorId() != mVendorId) return false;
if (mProductId != -1 && device.getProductId() != mProductId) return false;
            if (mManufacturerName != null && device.getManufacturerName() == null) return false;
            if (mProductName != null && device.getProductName() == null) return false;
            if (mSerialNumber != null && device.getSerialNumber() == null) return false;
            if (mManufacturerName != null && device.getManufacturerName() != null &&
                !mManufacturerName.equals(device.getManufacturerName())) return false;
            if (mProductName != null && device.getProductName() != null &&
                !mProductName.equals(device.getProductName())) return false;
            if (mSerialNumber != null && device.getSerialNumber() != null &&
                !mSerialNumber.equals(device.getSerialNumber())) return false;

// check device class/subclass/protocol
if (matches(device.getDeviceClass(), device.getDeviceSubclass(),
//Synthetic comment -- @@ -239,10 +258,15 @@
public boolean matches(DeviceFilter f) {
if (mVendorId != -1 && f.mVendorId != mVendorId) return false;
if (mProductId != -1 && f.mProductId != mProductId) return false;
            if (f.mManufacturerName != null && mManufacturerName == null) return false;
            if (f.mProductName != null && mProductName == null) return false;
            if (f.mSerialNumber != null && mSerialNumber == null) return false;
            if (mManufacturerName != null && f.mManufacturerName != null &&
                !mManufacturerName.equals(f.mManufacturerName)) return false;
            if (mProductName != null && f.mProductName != null &&
                !mProductName.equals(f.mProductName)) return false;
            if (mSerialNumber != null && f.mSerialNumber != null &&
                !mSerialNumber.equals(f.mSerialNumber)) return false;

// check device class/subclass/protocol
return matches(f.mClass, f.mSubclass, f.mProtocol);
//Synthetic comment -- @@ -252,32 +276,72 @@
public boolean equals(Object obj) {
// can't compare if we have wildcard strings
if (mVendorId == -1 || mProductId == -1 ||
                    mClass == -1 || mSubclass == -1 || mProtocol == -1) {
return false;
}
if (obj instanceof DeviceFilter) {
DeviceFilter filter = (DeviceFilter)obj;

                if (filter.mVendorId != mVendorId ||
                        filter.mProductId != mProductId ||
                        filter.mClass != mClass ||
                        filter.mSubclass != mSubclass ||
                        filter.mProtocol != mProtocol) {
                    return(false);
                }
                if ((filter.mManufacturerName != null &&
                        mManufacturerName == null) ||
                    (filter.mManufacturerName == null &&
                        mManufacturerName != null) ||
                    (filter.mProductName != null &&
                        mProductName == null)  ||
                    (filter.mProductName == null &&
                        mProductName != null) ||
                    (filter.mSerialNumber != null &&
                        mSerialNumber == null)  ||
                    (filter.mSerialNumber == null &&
                        mSerialNumber != null)) {
                    return(false);
                }
                if  ((filter.mManufacturerName != null &&
                        mManufacturerName != null &&
                        !mManufacturerName.equals(filter.mManufacturerName)) ||
                     (filter.mProductName != null &&
                        mProductName != null &&
                        !mProductName.equals(filter.mProductName)) ||
                     (filter.mSerialNumber != null &&
                        mSerialNumber != null &&
                        !mSerialNumber.equals(filter.mSerialNumber))) {
                    return(false);
                }
                return(true);
}
if (obj instanceof UsbDevice) {
UsbDevice device = (UsbDevice)obj;
                if (device.getVendorId() != mVendorId ||
                        device.getProductId() != mProductId ||
                        device.getDeviceClass() != mClass ||
                        device.getDeviceSubclass() != mSubclass ||
                        device.getDeviceProtocol() != mProtocol) {
                    return(false);
                }
                if ((mManufacturerName != null && device.getManufacturerName() == null) ||
                        (mManufacturerName == null && device.getManufacturerName() != null) ||
                        (mProductName != null && device.getProductName() == null) ||
                        (mProductName == null && device.getProductName() != null) ||
                        (mSerialNumber != null && device.getSerialNumber() == null) ||
                        (mSerialNumber == null && device.getSerialNumber() != null)) {
                    return(false);
                }
                if ((device.getManufacturerName() != null &&
                        !mManufacturerName.equals(device.getManufacturerName())) ||
                        (device.getProductName() != null &&
                            !mProductName.equals(device.getProductName())) ||
                        (device.getSerialNumber() != null &&
                            !mSerialNumber.equals(device.getSerialNumber()))) {
                    return(false);
                }
                return true;
}
return false;
}







