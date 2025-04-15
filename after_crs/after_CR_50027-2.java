/*Add a regression test for an icu4c bug.

The bug was already fixed as part of the icu4c 4.9 upgrade, so there's no
corresponding fix.

Bug:http://code.google.com/p/android/issues/detail?id=42769Change-Id:I2ceed6e9c1bc230683e6c8b9f38aaadee371eeef*/




//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/nio/charset/CharsetTest.java b/luni/src/test/java/libcore/java/nio/charset/CharsetTest.java
//Synthetic comment -- index af9bc59..24daa52 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;
import java.util.ArrayList;

public class CharsetTest extends junit.framework.TestCase {
public void test_guaranteedCharsetsAvailable() throws Exception {
//Synthetic comment -- @@ -33,6 +34,28 @@
assertNotNull(Charset.forName("UTF-8"));
}

    // http://code.google.com/p/android/issues/detail?id=42769
    public void test_42769() throws Exception {
        ArrayList<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < 10; ++i) {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    for (int i = 0; i < 50; ++i) {
                        Charset.availableCharsets();
                    }
                }
            });
            threads.add(t);
        }

        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
    }

public void test_allAvailableCharsets() throws Exception {
// Check that we can instantiate every Charset, CharsetDecoder, and CharsetEncoder.
for (String charsetName : Charset.availableCharsets().keySet()) {







