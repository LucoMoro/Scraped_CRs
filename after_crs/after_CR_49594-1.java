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
import java.io.IOException;
import java.security.ProviderException;
import libcore.io.Streams;

/**
 * Supplies random bits from /dev/urandom.
*/
public class RandomBitsSupplier implements SHA1_Data {
    private static FileInputStream devURandom;
static {
try {
            devURandom = new FileInputStream(new File("/dev/urandom"));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
}
}

    static boolean isServiceAvailable() {
        return (devURandom != null);
    }

    public static byte[] getRandomBits(int byteCount) {
        if (byteCount <= 0) {
            throw new IllegalArgumentException("Too few bytes requested: " + byteCount);
}
        try {
            byte[] result = new byte[byteCount];
            Streams.readFully(devURandom, result, 0, byteCount);
            return result;
        } catch (Exception ex) {
            throw new ProviderException("Couldn't read " + byteCount + " random bytes", ex);
        }
}
}







