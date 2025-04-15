/*Fix for startup NPE.

Change-Id:I6928939d235faf544a85bb84bba2fa135ddc8e45*/




//Synthetic comment -- diff --git a/services/java/com/android/server/UsbObserver.java b/services/java/com/android/server/UsbObserver.java
//Synthetic comment -- index d08fe9b..c36e6ca 100644

//Synthetic comment -- @@ -128,6 +128,9 @@

try {
File[] files = new File(USB_COMPOSITE_CLASS_PATH).listFiles();
            if (files == null) {
                return; // nothing to do
            }
for (int i = 0; i < files.length; i++) {
File file = new File(files[i], "enable");
FileReader reader = new FileReader(file);







