/*SDK Ping: properly parse new "21 rc7" format as "21.0.0.7"

Change-Id:I6c57e64dbf2586b597826c6ab7cd5eca1fc18580*/
//Synthetic comment -- diff --git a/sdkstats/src/com/android/sdkstats/SdkStatsService.java b/sdkstats/src/com/android/sdkstats/SdkStatsService.java
//Synthetic comment -- index c490fed..ab97dae 100644

//Synthetic comment -- @@ -505,23 +505,35 @@
*/
protected String normalizeVersion(String version) {

        // Version must be between 1 and 4 dotted numbers
        String[] numbers = version.split("\\.");                                //$NON-NLS-1$
        if (numbers.length > 4) {
            throw new IllegalArgumentException("Bad version: " + version);      //$NON-NLS-1$
        }
        for (String part: numbers) {
            if (!part.matches("\\d+")) {                                        //$NON-NLS-1$
                throw new IllegalArgumentException("Bad version: " + version);  //$NON-NLS-1$
}
}

        // Always output 4 numbers, even if fewer were supplied (pad with .0)
        StringBuffer normal = new StringBuffer(numbers[0]);
        for (int i = 1; i < 4; i++) {
            normal.append('.').append(i < numbers.length ? numbers[i] : "0");   //$NON-NLS-1$
        }
        return normal.toString();
}

/**








//Synthetic comment -- diff --git a/sdkstats/tests/com/android/sdkstats/SdkStatsServiceTest.java b/sdkstats/tests/com/android/sdkstats/SdkStatsServiceTest.java
//Synthetic comment -- index b1db42b..9982bb7 100755

//Synthetic comment -- @@ -306,6 +306,139 @@
assertEquals("one3456789ten3456789twenty6789th", m.getOsName());
}

public void testSdkStatsService_glPing() {
MockSdkStatsService m;
m = new MockSdkStatsService("Windows", "6.2", "x86_64", "1.7.8_09");







