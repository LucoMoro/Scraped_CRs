/*SDK Ping: properly parse new "21 rc7" format as "21.0.0.7"

Change-Id:I6c57e64dbf2586b597826c6ab7cd5eca1fc18580*/




//Synthetic comment -- diff --git a/sdkstats/src/com/android/sdkstats/SdkStatsService.java b/sdkstats/src/com/android/sdkstats/SdkStatsService.java
//Synthetic comment -- index c490fed..ab97dae 100644

//Synthetic comment -- @@ -505,23 +505,35 @@
*/
protected String normalizeVersion(String version) {

        Pattern regex = Pattern.compile(
                //1=major     2=minor       3=micro       4=build |  5=rc
                "^(\\d+)(?:\\.(\\d+))?(?:\\.(\\d+))?(?:\\.(\\d+)| +rc(\\d+))?"); //$NON-NLS-1$

        Matcher m = regex.matcher(version);
        if (m != null && m.lookingAt()) {
            StringBuffer normal = new StringBuffer();
            for (int i = 1; i <= 4; i++) {
                int v = 0;
                try {
                    // If build is null but we have an rc, take that number instead as the 4th part.
                    if (i == 4 &&
                            i < m.groupCount() &&
                            m.group(i) == null &&
                            m.group(i+1) != null) {
                        i++;
                    }
                    v = Integer.parseInt(m.group(i));
                } catch (Exception ignore) {
                }
                if (i > 1) {
                    normal.append('.');
                }
                normal.append(v);
}
            return normal.toString();
}

        throw new IllegalArgumentException("Bad version: " + version);          //$NON-NLS-1$
}

/**








//Synthetic comment -- diff --git a/sdkstats/tests/com/android/sdkstats/SdkStatsServiceTest.java b/sdkstats/tests/com/android/sdkstats/SdkStatsServiceTest.java
//Synthetic comment -- index b1db42b..9982bb7 100755

//Synthetic comment -- @@ -306,6 +306,139 @@
assertEquals("one3456789ten3456789twenty6789th", m.getOsName());
}

    public void testSdkStatsService_parseVersion() {
        // Tests that the version parses supports the new "major.minor.micro rcPreview" format
        // as well as "x.y.z.t" formats as well as Eclipse's "x.y.z.v2012somedate" formats.

        MockSdkStatsService m;
        m = new MockSdkStatsService("Windows", "6.2", "x86_64", "1.7.8_09");

        m.ping("monitor", "21");
        assertEquals(
                "http://tools.google.com/service/update?" +
                "as=androidsdk_monitor&" +
                "id=42&" +
                "version=21.0.0.0&" +
                "os=win-6.2&" +
                "osa=x86_64&" +
                "vma=1.7-x86_64",
                m.getPingUrlResult().toString());

        m.ping("monitor", "21.1");
        assertEquals(
                "http://tools.google.com/service/update?" +
                "as=androidsdk_monitor&" +
                "id=42&" +
                "version=21.1.0.0&" +
                "os=win-6.2&" +
                "osa=x86_64&" +
                "vma=1.7-x86_64",
                m.getPingUrlResult().toString());

        m.ping("monitor", "21.2.03");
        assertEquals(
                "http://tools.google.com/service/update?" +
                "as=androidsdk_monitor&" +
                "id=42&" +
                "version=21.2.3.0&" +
                "os=win-6.2&" +
                "osa=x86_64&" +
                "vma=1.7-x86_64",
                m.getPingUrlResult().toString());

        m.ping("monitor", "21.2.3.4");
        assertEquals(
                "http://tools.google.com/service/update?" +
                "as=androidsdk_monitor&" +
                "id=42&" +
                "version=21.2.3.4&" +
                "os=win-6.2&" +
                "osa=x86_64&" +
                "vma=1.7-x86_64",
                m.getPingUrlResult().toString());

        // More than 4 parts or extra stuff that is not an "rc" preview are ignored.
        m.ping("monitor", "21.2.3.4.5.6.7.8");
        assertEquals(
                "http://tools.google.com/service/update?" +
                "as=androidsdk_monitor&" +
                "id=42&" +
                "version=21.2.3.4&" +
                "os=win-6.2&" +
                "osa=x86_64&" +
                "vma=1.7-x86_64",
                m.getPingUrlResult().toString());

        m.ping("monitor", "21.2.3.4.v20120101 the rest is ignored");
        assertEquals(
                "http://tools.google.com/service/update?" +
                "as=androidsdk_monitor&" +
                "id=42&" +
                "version=21.2.3.4&" +
                "os=win-6.2&" +
                "osa=x86_64&" +
                "vma=1.7-x86_64",
                m.getPingUrlResult().toString());

        // If the "rc" preview integer is present, it's equivalent to a 4th number.
        m.ping("monitor", "21 rc4");
        assertEquals(
                "http://tools.google.com/service/update?" +
                "as=androidsdk_monitor&" +
                "id=42&" +
                "version=21.0.0.4&" +
                "os=win-6.2&" +
                "osa=x86_64&" +
                "vma=1.7-x86_64",
                m.getPingUrlResult().toString());

        m.ping("monitor", "21.01 rc5");
        assertEquals(
                "http://tools.google.com/service/update?" +
                "as=androidsdk_monitor&" +
                "id=42&" +
                "version=21.1.0.5&" +
                "os=win-6.2&" +
                "osa=x86_64&" +
                "vma=1.7-x86_64",
                m.getPingUrlResult().toString());

        m.ping("monitor", "21.02.03 rc6");
        assertEquals(
                "http://tools.google.com/service/update?" +
                "as=androidsdk_monitor&" +
                "id=42&" +
                "version=21.2.3.6&" +
                "os=win-6.2&" +
                "osa=x86_64&" +
                "vma=1.7-x86_64",
                m.getPingUrlResult().toString());

        // If there's a 4-part version number, the rc preview number isn't used.
        m.ping("monitor", "21.2.3.4 rc7");
        assertEquals(
                "http://tools.google.com/service/update?" +
                "as=androidsdk_monitor&" +
                "id=42&" +
                "version=21.2.3.4&" +
                "os=win-6.2&" +
                "osa=x86_64&" +
                "vma=1.7-x86_64",
                m.getPingUrlResult().toString());

        // For Eclipse plugins, the 4th part might be a date. It is ignored.
        m.ping("eclipse", "21.2.3.v20120102235958");
        assertEquals(
                "http://tools.google.com/service/update?" +
                "as=androidsdk_eclipse&" +
                "id=42&" +
                "version=21.2.3.0&" +
                "os=win-6.2&" +
                "osa=x86_64&" +
                "vma=1.7-x86_64",
                m.getPingUrlResult().toString());
    }

public void testSdkStatsService_glPing() {
MockSdkStatsService m;
m = new MockSdkStatsService("Windows", "6.2", "x86_64", "1.7.8_09");







