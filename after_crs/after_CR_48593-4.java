/*Use most specific attributes for distinguished name display

Bug: 7894348
Bug:http://code.google.com/p/android/issues/detail?id=41662Change-Id:I8048a53b8a9a17b384f12b6a4f40071bb3dd3e04*/




//Synthetic comment -- diff --git a/core/java/android/net/http/SslCertificate.java b/core/java/android/net/http/SslCertificate.java
//Synthetic comment -- index fe6d4eb..5b60c0d 100644

//Synthetic comment -- @@ -334,9 +334,11 @@

/**
* A distinguished name helper class: a 3-tuple of:
     * <ul>
     *   <li>the most specific common name (CN)</li>
     *   <li>the most specific organization (O)</li>
     *   <li>the most specific organizational unit (OU)</li>
     * <ul>
*/
public class DName {
/**
//Synthetic comment -- @@ -360,8 +362,15 @@
private String mUName;

/**
         * Creates a new {@code DName} from a string. The attributes
         * are assumed to come in most significant to least
         * significant order which is true of human readable values
         * returned by methods such as {@code X500Principal.getName()}.
         * Be aware that the underlying sources of distinguished names
         * such as instances of {@code X509Certificate} are encoded in
         * least significant to most significant order, so make sure
         * the value passed here has the expected ordering of
         * attributes.
*/
public DName(String dName) {
if (dName != null) {
//Synthetic comment -- @@ -374,18 +383,24 @@

for (int i = 0; i < oid.size(); i++) {
if (oid.elementAt(i).equals(X509Name.CN)) {
                            if (mCName == null) {
                                mCName = (String) val.elementAt(i);
                            }
continue;
}

if (oid.elementAt(i).equals(X509Name.O)) {
                            if (mOName == null) {
                                mOName = (String) val.elementAt(i);
                                continue;
                            }
}

if (oid.elementAt(i).equals(X509Name.OU)) {
                            if (mUName == null) {
                                mUName = (String) val.elementAt(i);
                                continue;
                            }
}
}
} catch (IllegalArgumentException ex) {
//Synthetic comment -- @@ -402,21 +417,21 @@
}

/**
         * @return The most specific Common-name (CN) component of this name
*/
public String getCName() {
return mCName != null ? mCName : "";
}

/**
         * @return The most specific Organization (O) component of this name
*/
public String getOName() {
return mOName != null ? mOName : "";
}

/**
         * @return The most specific Organizational Unit (OU) component of this name
*/
public String getUName() {
return mUName != null ? mUName : "";








//Synthetic comment -- diff --git a/core/tests/coretests/src/android/net/http/SslCertificateTest.java b/core/tests/coretests/src/android/net/http/SslCertificateTest.java
//Synthetic comment -- index 147816b..6a30c6c 100644

//Synthetic comment -- @@ -45,11 +45,70 @@

@LargeTest
public void testSslCertificateWithEmptyIssuer() throws Exception {
        X509Certificate x509Certificate = generateCertificate(Issue1597Certificate);
        assertEquals("", x509Certificate.getSubjectDN().getName());
SslCertificate sslCertificate = new SslCertificate(x509Certificate);
        assertEquals("", sslCertificate.getIssuedBy().getDName());
}

    /**
     * Problematic certificate from Issue 41662
     * http://code.google.com/p/android/issues/detail?id=41662
     */
    private static final String Issue41662Certificate =
        "-----BEGIN CERTIFICATE-----\n"+
        "MIIG6jCCBdKgAwIBAgIESPx/LDANBgkqhkiG9w0BAQUFADCBrjESMBAGCgmSJomT\n"+
        "8ixkARkWAnJzMRUwEwYKCZImiZPyLGQBGRYFcG9zdGExEjAQBgoJkiaJk/IsZAEZ\n"+
        "FgJjYTEWMBQGA1UEAxMNQ29uZmlndXJhdGlvbjERMA8GA1UEAxMIU2VydmljZXMx\n"+
        "HDAaBgNVBAMTE1B1YmxpYyBLZXkgU2VydmljZXMxDDAKBgNVBAMTA0FJQTEWMBQG\n"+
        "A1UEAxMNUG9zdGEgQ0EgUm9vdDAeFw0wODEwMjAxNDExMzBaFw0yODEwMTQyMjAw\n"+
        "MDBaMIGrMRIwEAYKCZImiZPyLGQBGRYCcnMxFTATBgoJkiaJk/IsZAEZFgVwb3N0\n"+
        "YTESMBAGCgmSJomT8ixkARkWAmNhMRYwFAYDVQQDEw1Db25maWd1cmF0aW9uMREw\n"+
        "DwYDVQQDEwhTZXJ2aWNlczEcMBoGA1UEAxMTUHVibGljIEtleSBTZXJ2aWNlczEM\n"+
        "MAoGA1UEAxMDQUlBMRMwEQYDVQQDEwpQb3N0YSBDQSAxMIIBIjANBgkqhkiG9w0B\n"+
        "AQEFAAOCAQ8AMIIBCgKCAQEAl5msW5MdLW/2aDlezrjU3jW58MKrcMPHs2szlGdL\n"+
        "nsAcSyYFF1JbyA8iuqLp7mhvcTz9m4jK82XBz/1mPq8wJMU9ekGnLhgbKLGKXRBA\n"+
        "sY9wzCvwpweQV6ui4vr2eOkS1j9Mk7ikatH8tNiIzkNrTj3npDpZv1w4G37iwtpb\n"+
        "yjg+lkNIDY2nWV9roBsAZM8Lvbyi4vxP41YEQZ3hxaGGG0/RKHbugvGatgckxfin\n"+
        "4gpFG2mDhS9uafGgqnLHLwpxgBbi3g6+2TsxOKatTxwxx9/4MND1GjhxKTjDNYPl\n"+
        "5JHUvr9fcvQMxP21/jbO4EsCWG+F38R90kT37hFL3l1qiQIDAQABo4IDDzCCAwsw\n"+
        "DwYDVR0TAQH/BAUwAwEB/zAOBgNVHQ8BAf8EBAMCAQYwgcwGA1UdIASBxDCBwTCB\n"+
        "vgYLKwYBBAH6OAoyAQEwga4wMAYIKwYBBQUHAgEWJGh0dHA6Ly93d3cuY2EucG9z\n"+
        "dGEucnMvZG9rdW1lbnRhY2lqYTB6BggrBgEFBQcCAjBuGmxPdm8gamUgZWxla3Ry\n"+
        "b25za2kgc2VydGlmaWthdCBpemRhdmFja29nIChwcm9kdWtjaW9ub2cpIENBIHNl\n"+
        "cnZlcmEgU2VydGlmaWthY2lvbm9nIHRlbGEgUG9zdGU6ICJQb3N0YSBDQSAxIi4w\n"+
        "ggG8BgNVHR8EggGzMIIBrzCByaCBxqCBw6SBwDCBvTESMBAGCgmSJomT8ixkARkW\n"+
        "AnJzMRUwEwYKCZImiZPyLGQBGRYFcG9zdGExEjAQBgoJkiaJk/IsZAEZFgJjYTEW\n"+
        "MBQGA1UEAxMNQ29uZmlndXJhdGlvbjERMA8GA1UEAxMIU2VydmljZXMxHDAaBgNV\n"+
        "BAMTE1B1YmxpYyBLZXkgU2VydmljZXMxDDAKBgNVBAMTA0FJQTEWMBQGA1UEAxMN\n"+
        "UG9zdGEgQ0EgUm9vdDENMAsGA1UEAxMEQ1JMMTCB4KCB3aCB2oaBo2xkYXA6Ly9s\n"+
        "ZGFwLmNhLnBvc3RhLnJzL2NuPVBvc3RhJTIwQ0ElMjBSb290LGNuPUFJQSxjbj1Q\n"+
        "dWJsaWMlMjBLZXklMjBTZXJ2aWNlcyxjbj1TZXJ2aWNlcyxjbj1Db25maWd1cmF0\n"+
        "aW9uLGRjPWNhLGRjPXBvc3RhLGRjPXJzP2NlcnRpZmljYXRlUmV2b2NhdGlvbkxp\n"+
        "c3QlM0JiaW5hcnmGMmh0dHA6Ly9zZXJ0aWZpa2F0aS5jYS5wb3N0YS5ycy9jcmwv\n"+
        "UG9zdGFDQVJvb3QuY3JsMB8GA1UdIwQYMBaAFPLLjeI17xBDxNp7yvrriQOhIq+4\n"+
        "MB0GA1UdDgQWBBQuZ6cm1uhncOeq+pAsMLzXYWUfhjAZBgkqhkiG9n0HQQAEDDAK\n"+
        "GwRWNy4xAwIAgTANBgkqhkiG9w0BAQUFAAOCAQEAjpmoaebsvfjgwgCYArou/s8k\n"+
        "Tr50TUdcJYxAYmCFQp531E1F+qUCWM/7bZApqByR3+EUz8goI5O2Cp/6ISxTR1HC\n"+
        "Dn71ESg7/c8Bs2Obx0LGYPnlRPvw7LH31dYXpj4EMNAamhOfBXgY2htXHCd7daIe\n"+
        "thvNkqWGDzmcoaGw/2BMNadlYkdXxudDBaiPDFm27yR7fPRibjxwkQVknzFezX/y\n"+
        "46j+20LoGJ/IpneT209XzytiaqtZBy3yqz2qImVDqvn5doHw63LOUqt8vfDS1sbd\n"+
        "zi3acAmPK1nERdCMJYJEEGNiGbkbw2cghwLw/4eYGXlj1VLXD3GU42uBr8QftA==\n"+
        "-----END CERTIFICATE-----\n";

    @LargeTest
    public void testSslCertificateWithMultipleCN() throws Exception {
        X509Certificate x509Certificate = generateCertificate(Issue41662Certificate);
        String dn = x509Certificate.getSubjectDN().getName();
        assertTrue(dn, dn.contains("Posta CA 1"));
        assertTrue(dn, dn.contains("Configuration"));
        SslCertificate sslCertificate = new SslCertificate(x509Certificate);
        assertEquals(dn, "Posta CA 1", sslCertificate.getIssuedTo().getCName());
    }

    private static X509Certificate generateCertificate(String pem) throws Exception {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        return (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(pem.getBytes()));
    }

}







