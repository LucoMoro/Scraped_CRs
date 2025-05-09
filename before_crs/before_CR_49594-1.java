/*Clean up RandomBitsSupplier.

The reason the array had later items commented out is that the loop
was wrong: it would always choose the _last_ valid device, not the
_first_ as intended.

There were two choices here:
1. fix the loop and have the same list of three devices that OpenSSL uses.
2. remove the loop and just say "you must have /dev/urandom".

This patch goes with option 2.

Change-Id:Ia99be6ba929c7b6c35bed77c19d47de80719973c*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/provider/crypto/RandomBitsSupplier.java b/luni/src/main/java/org/apache/harmony/security/provider/crypto/RandomBitsSupplier.java
//Synthetic comment -- index 3862132..ad3a280 100644

//Synthetic comment -- @@ -18,144 +18,39 @@

package org.apache.harmony.security.provider.crypto;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.ProviderException;

/**
 *  The static class providing access on Linux platform
 *  to system means for generating true random bits. <BR>
 *
 *  The source for true random bits is one of Linux's devices "/dev/urandom" or
 *  "/dev/random" depends on which one is available; if both the first is used. <BR>
 *
 *  If no device available the service is not available,
 *  that is, provider shouldn't register the algorithm. <BR>
*/
public class RandomBitsSupplier implements SHA1_Data {


    /**
     * InputStream to read from device
     *
     * Using a BufferedInputStream leads to problems
     * on Android in rare cases, since the
     * BufferedInputStream's available() issues an
     * ioctl(), and the pseudo device doesn't seem
     * to like that. Since we're reading bigger
     * chunks and not single bytes, the FileInputStream
     * shouldn't be slower, so we use that. Same might
     * apply to other Linux platforms.
     *
     * TODO: the above doesn't sound true.
     */
    private static FileInputStream fis = null;

    /**
     * File to connect to device
     */
    private static File randomFile = null;

    /**
     * value of field is "true" only if a device is available
     */
    private static boolean serviceAvailable = false;

    /**
     *  names of random devices on Linux platform
     */
    private static final String DEVICE_NAMES[] = { "/dev/urandom" /*, "/dev/random" */ };

static {
        for (String deviceName : DEVICE_NAMES) {
            try {
                File file = new File(deviceName);
                if (file.canRead()) {
                    fis = new FileInputStream(file);
                    randomFile = file;
                    serviceAvailable = true;
                }
            } catch (FileNotFoundException e) {
            }
        }
    }


    /**
     * The method is called by provider to determine if a device is available.
     */
    static boolean isServiceAvailable() {
        return serviceAvailable;
    }


    /**
     * On platforms with "random" devices available,
     * the method reads random bytes from the device.  <BR>
     *
     * In case of any runtime failure ProviderException gets thrown.
     */
    private static synchronized byte[] getUnixDeviceRandom(int numBytes) {

        byte[] bytes = new byte[numBytes];

        int total = 0;
        int bytesRead;
        int offset = 0;
try {
            for ( ; ; ) {

                bytesRead = fis.read(bytes, offset, numBytes-total);


                // the below case should not occur because /dev/random or /dev/urandom is a special file
                // hence, if it is happened there is some internal problem
                if ( bytesRead == -1 ) {
                    throw new ProviderException("bytesRead == -1");
                }

                total  += bytesRead;
                offset += bytesRead;

                if ( total >= numBytes ) {
                    break;
                }
            }
        } catch (IOException e) {

            // actually there should be no IOException because device is a special file;
            // hence, there is either some internal problem or, for instance,
            // device was removed in runtime, or something else
            throw new ProviderException("ATTENTION: IOException in RandomBitsSupplier.getLinuxRandomBits(): " + e);
}
        return bytes;
}

    /**
     * The method returns byte array of requested length provided service is available.
     * ProviderException gets thrown otherwise.
     *
     * @param
     *       numBytes - length of bytes requested
     * @return
     *       byte array
     * @throws
     *       InvalidArgumentException - if numBytes <= 0
     */
    public static byte[] getRandomBits(int numBytes) {
        if (numBytes <= 0) {
            throw new IllegalArgumentException(Integer.toString(numBytes));
        }

        // We have been unable to get a random device or fall back to the
        // native security module code - throw an exception.
        if ( !serviceAvailable ) {
            throw new ProviderException("ATTENTION: service is not available : no random devices");
}

        return getUnixDeviceRandom(numBytes);
}
}







