/*Fix test_KeyStore_setEntry issue

Also include KeyStore type information in failure output.

Change-Id:I60f30e2264fa1516f489c79a69a6a213af961419*/




//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/security/KeyStoreTest.java b/luni/src/test/java/libcore/java/security/KeyStoreTest.java
//Synthetic comment -- index f57b73f..15314c9 100644

//Synthetic comment -- @@ -207,7 +207,7 @@
if (isReadOnly(ks)) {
try {
setPrivateKey(ks);
                fail(ks.toString());
} catch (UnsupportedOperationException e) {
}
return;
//Synthetic comment -- @@ -377,7 +377,7 @@
String type = KeyStore.getDefaultType();
try {
KeyStore.getInstance(null);
            fail(type);
} catch (NullPointerException expected) {
}

//Synthetic comment -- @@ -386,12 +386,12 @@
String providerName = StandardNames.SECURITY_PROVIDER_NAME;
try {
KeyStore.getInstance(null, (String)null);
            fail(type);
} catch (IllegalArgumentException expected) {
}
try {
KeyStore.getInstance(null, providerName);
            fail(type);
} catch (Exception e) {
if (e.getClass() != NullPointerException.class
&& e.getClass() != KeyStoreException.class) {
//Synthetic comment -- @@ -400,7 +400,7 @@
}
try {
KeyStore.getInstance(type, (String)null);
            fail(type);
} catch (IllegalArgumentException expected) {
}
assertNotNull(KeyStore.getInstance(type, providerName));
//Synthetic comment -- @@ -408,17 +408,17 @@
Provider provider = Security.getProvider(providerName);
try {
KeyStore.getInstance(null, (Provider)null);
            fail(type);
} catch (IllegalArgumentException expected) {
}
try {
KeyStore.getInstance(null, provider);
            fail(type);
} catch (NullPointerException expected) {
}
try {
KeyStore.getInstance(type, (Provider)null);
            fail(type);
} catch (IllegalArgumentException expected) {
}
assertNotNull(KeyStore.getInstance(type, provider));
//Synthetic comment -- @@ -457,7 +457,7 @@
for (KeyStore keyStore : keyStores()) {
try {
keyStore.getKey(null, null);
                fail(keyStore.getType());
} catch (KeyStoreException expected) {
}
}
//Synthetic comment -- @@ -468,7 +468,7 @@
// test odd inputs
try {
keyStore.getKey(null, null);
                fail(keyStore.getType());
} catch (Exception e) {
if (e.getClass() != NullPointerException.class
&& e.getClass() != IllegalArgumentException.class) {
//Synthetic comment -- @@ -477,7 +477,7 @@
}
try {
keyStore.getKey(null, PASSWORD_KEY);
                fail(keyStore.getType());
} catch (Exception e) {
if (e.getClass() != NullPointerException.class
&& e.getClass() != IllegalArgumentException.class
//Synthetic comment -- @@ -520,7 +520,7 @@
} else {
try {
keyStore.getKey(ALIAS_PRIVATE, null);
                        fail(keyStore.getType());
} catch (Exception e) {
if (e.getClass() != UnrecoverableKeyException.class
&& e.getClass() != IllegalArgumentException.class) {
//Synthetic comment -- @@ -534,7 +534,7 @@
} else if (isSecretKeyEnabled(keyStore)) {
try {
keyStore.getKey(ALIAS_SECRET, null);
                    fail(keyStore.getType());
} catch (Exception e) {
if (e.getClass() != UnrecoverableKeyException.class
&& e.getClass() != IllegalArgumentException.class) {
//Synthetic comment -- @@ -551,7 +551,7 @@
} else {
try {
keyStore.getKey(ALIAS_PRIVATE, PASSWORD_BAD);
                    fail(keyStore.getType());
} catch (UnrecoverableKeyException expected) {
}
}
//Synthetic comment -- @@ -560,7 +560,7 @@
} else if (isSecretKeyEnabled(keyStore)) {
try {
keyStore.getKey(ALIAS_SECRET, PASSWORD_BAD);
                    fail(keyStore.getType());
} catch (UnrecoverableKeyException expected) {
}
}
//Synthetic comment -- @@ -571,7 +571,7 @@
for (KeyStore keyStore : keyStores()) {
try {
keyStore.getCertificateChain(null);
                fail(keyStore.getType());
} catch (KeyStoreException expected) {
}
}
//Synthetic comment -- @@ -581,7 +581,7 @@
// test odd inputs
try {
keyStore.getCertificateChain(null);
                fail(keyStore.getType());
} catch (Exception e) {
if (e.getClass() != NullPointerException.class
&& e.getClass() != IllegalArgumentException.class) {
//Synthetic comment -- @@ -610,7 +610,7 @@
for (KeyStore keyStore : keyStores()) {
try {
keyStore.getCertificate(null);
                fail(keyStore.getType());
} catch (KeyStoreException expected) {
}
}
//Synthetic comment -- @@ -620,7 +620,7 @@
// test odd inputs
try {
keyStore.getCertificate(null);
                fail(keyStore.getType());
} catch (Exception e) {
if (e.getClass() != NullPointerException.class
&& e.getClass() != IllegalArgumentException.class) {
//Synthetic comment -- @@ -651,7 +651,7 @@
for (KeyStore keyStore : keyStores()) {
try {
keyStore.getCreationDate(null);
                fail(keyStore.getType());
} catch (KeyStoreException expected) {
}
}
//Synthetic comment -- @@ -664,7 +664,7 @@
// test odd inputs
try {
keyStore.getCreationDate(null);
                fail(keyStore.getType());
} catch (NullPointerException expected) {
}
assertNull(keyStore.getCreationDate(""));
//Synthetic comment -- @@ -696,7 +696,7 @@
for (KeyStore keyStore : keyStores()) {
try {
keyStore.setKeyEntry(null, null, null, null);
                fail(keyStore.getType());
} catch (KeyStoreException expected) {
}
}
//Synthetic comment -- @@ -706,7 +706,7 @@
if (isReadOnly(keyStore)) {
try {
keyStore.setKeyEntry(null, null, null, null);
                    fail(keyStore.getType());
} catch (UnsupportedOperationException expected) {
}
continue;
//Synthetic comment -- @@ -715,7 +715,7 @@
// test odd inputs
try {
keyStore.setKeyEntry(null, null, null, null);
                fail(keyStore.getType());
} catch (Exception e) {
if (e.getClass() != NullPointerException.class
&& e.getClass() != KeyStoreException.class) {
//Synthetic comment -- @@ -724,7 +724,7 @@
}
try {
keyStore.setKeyEntry(null, null, PASSWORD_KEY, null);
                fail(keyStore.getType());
} catch (Exception e) {
if (e.getClass() != NullPointerException.class
&& e.getClass() != KeyStoreException.class) {
//Synthetic comment -- @@ -736,7 +736,7 @@
getPrivateKey().getPrivateKey(),
PASSWORD_KEY,
null);
                fail(keyStore.getType());
} catch (IllegalArgumentException expected) {
}
}
//Synthetic comment -- @@ -749,7 +749,7 @@
if (isReadOnly(keyStore)) {
try {
keyStore.setKeyEntry(ALIAS_SECRET, getSecretKey(), PASSWORD_KEY, null);
                    fail(keyStore.getType());
} catch (UnsupportedOperationException expected) {
}
continue;
//Synthetic comment -- @@ -764,7 +764,7 @@
} else {
try {
keyStore.setKeyEntry(ALIAS_SECRET, getSecretKey(), PASSWORD_KEY, null);
                    fail(keyStore.getType());
} catch (Exception e) {
if (e.getClass() != KeyStoreException.class
&& e.getClass() != NullPointerException.class) {
//Synthetic comment -- @@ -820,7 +820,7 @@
getPrivateKey().getPrivateKey(),
null,
getPrivateKey().getCertificateChain());
                    fail(keyStore.getType());
} catch (UnsupportedOperationException expected) {
}
continue;
//Synthetic comment -- @@ -839,7 +839,7 @@
getPrivateKey().getPrivateKey(),
null,
getPrivateKey().getCertificateChain());
                    fail(keyStore.getType());
} catch (Exception e) {
if (e.getClass() != UnrecoverableKeyException.class
&& e.getClass() != IllegalArgumentException.class
//Synthetic comment -- @@ -855,7 +855,7 @@
} else {
try {
keyStore.setKeyEntry(ALIAS_SECRET, getSecretKey(), null, null);
                        fail(keyStore.getType());
} catch (Exception e) {
if (e.getClass() != UnrecoverableKeyException.class
&& e.getClass() != IllegalArgumentException.class
//Synthetic comment -- @@ -872,7 +872,7 @@
for (KeyStore keyStore : keyStores()) {
try {
keyStore.setKeyEntry(null, null, null);
                fail(keyStore.getType());
} catch (KeyStoreException expected) {
}
}
//Synthetic comment -- @@ -883,7 +883,7 @@
if (isReadOnly(keyStore)) {
try {
keyStore.setKeyEntry(null, null, null);
                    fail(keyStore.getType());
} catch (UnsupportedOperationException expected) {
}
continue;
//Synthetic comment -- @@ -892,7 +892,7 @@
// test odd inputs
try {
keyStore.setKeyEntry(null, null, null);
                fail(keyStore.getType());
} catch (Exception e) {
if (e.getClass() != NullPointerException.class
&& e.getClass() != IllegalArgumentException.class
//Synthetic comment -- @@ -920,7 +920,7 @@
if (isReadOnly(keyStore)) {
try {
setPrivateKeyBytes(keyStore);
                    fail(keyStore.getType());
} catch (UnsupportedOperationException expected) {
}
continue;
//Synthetic comment -- @@ -935,7 +935,7 @@
} else {
try {
keyStore.setKeyEntry(ALIAS_SECRET, getSecretKey().getEncoded(), null);
                    fail(keyStore.getType());
} catch (KeyStoreException expected) {
}
}
//Synthetic comment -- @@ -994,7 +994,7 @@
for (KeyStore keyStore : keyStores()) {
try {
keyStore.setCertificateEntry(null, null);
                fail(keyStore.getType());
} catch (KeyStoreException expected) {
}
}
//Synthetic comment -- @@ -1005,7 +1005,7 @@
// test odd inputs
try {
keyStore.setCertificateEntry(null, null);
                fail(keyStore.getType());
} catch (Exception e) {
if (e.getClass() != NullPointerException.class
&& e.getClass() != KeyStoreException.class) {
//Synthetic comment -- @@ -1017,7 +1017,7 @@
try {
assertNull(keyStore.getCertificate(ALIAS_CERTIFICATE));
keyStore.setCertificateEntry(ALIAS_CERTIFICATE, null);
                    fail(keyStore.getType());
} catch (UnsupportedOperationException expected) {
}
continue;
//Synthetic comment -- @@ -1042,7 +1042,7 @@
} else {
try {
keyStore.setCertificateEntry(ALIAS_CERTIFICATE, null);
                    fail(keyStore.getType());
} catch (KeyStoreException expected) {
}
}
//Synthetic comment -- @@ -1060,7 +1060,7 @@
if (isReadOnly(keyStore)) {
try {
setCertificate(keyStore);
                    fail(keyStore.getType());
} catch (UnsupportedOperationException expected) {
}
continue;
//Synthetic comment -- @@ -1104,7 +1104,7 @@
for (KeyStore keyStore : keyStores()) {
try {
keyStore.deleteEntry(null);
                fail(keyStore.getType());
} catch (KeyStoreException expected) {
}
}
//Synthetic comment -- @@ -1115,7 +1115,7 @@
if (isReadOnly(keyStore)) {
try {
keyStore.deleteEntry(null);
                    fail(keyStore.getType());
} catch (UnsupportedOperationException expected) {
}
continue;
//Synthetic comment -- @@ -1124,7 +1124,7 @@
// test odd inputs
try {
keyStore.deleteEntry(null);
                fail(keyStore.getType());
} catch (Exception e) {
if (e.getClass() != NullPointerException.class
&& e.getClass() != KeyStoreException.class) {
//Synthetic comment -- @@ -1201,7 +1201,7 @@
for (KeyStore keyStore : keyStores()) {
try {
keyStore.aliases();
                fail(keyStore.getType());
} catch (KeyStoreException expected) {
}
}
//Synthetic comment -- @@ -1245,7 +1245,7 @@
for (KeyStore keyStore : keyStores()) {
try {
keyStore.containsAlias(null);
                fail(keyStore.getType());
} catch (KeyStoreException expected) {
}
}
//Synthetic comment -- @@ -1255,7 +1255,7 @@

try {
keyStore.containsAlias(null);
                fail(keyStore.getType());
} catch (NullPointerException expected) {
}

//Synthetic comment -- @@ -1288,7 +1288,7 @@
for (KeyStore keyStore : keyStores()) {
try {
keyStore.aliases();
                fail(keyStore.getType());
} catch (KeyStoreException expected) {
}
}
//Synthetic comment -- @@ -1330,7 +1330,7 @@
for (KeyStore keyStore : keyStores()) {
try {
keyStore.isKeyEntry(null);
                fail(keyStore.getType());
} catch (KeyStoreException expected) {
}
}
//Synthetic comment -- @@ -1340,7 +1340,7 @@

try {
keyStore.isKeyEntry(null);
                fail(keyStore.getType());
} catch (NullPointerException expected) {
}

//Synthetic comment -- @@ -1371,7 +1371,7 @@
for (KeyStore keyStore : keyStores()) {
try {
keyStore.isCertificateEntry(null);
                fail(keyStore.getType());
} catch (KeyStoreException expected) {
}
}
//Synthetic comment -- @@ -1382,7 +1382,7 @@
if (isCertificateEnabled(keyStore)) {
try {
keyStore.isCertificateEntry(null);
                    fail(keyStore.getType());
} catch (NullPointerException expected) {
}
} else {
//Synthetic comment -- @@ -1415,7 +1415,7 @@
for (KeyStore keyStore : keyStores()) {
try {
keyStore.getCertificateAlias(null);
                fail(keyStore.getType());
} catch (KeyStoreException expected) {
}
}
//Synthetic comment -- @@ -1479,7 +1479,7 @@
for (KeyStore keyStore : keyStores()) {
try {
keyStore.store(null, null);
                fail(keyStore.getType());
} catch (KeyStoreException expected) {
}
}
//Synthetic comment -- @@ -1490,7 +1490,7 @@
if (isReadOnly(keyStore)) {
try {
keyStore.store(out, null);
                    fail(keyStore.getType());
} catch (UnsupportedOperationException expected) {
}
continue;
//Synthetic comment -- @@ -1504,7 +1504,7 @@

try {
keyStore.store(out, null);
                fail(keyStore.getType());
} catch (Exception e) {
if (e.getClass() != IllegalArgumentException.class
&& e.getClass() != NullPointerException.class) {
//Synthetic comment -- @@ -1520,7 +1520,7 @@
if (isReadOnly(keyStore)) {
try {
keyStore.store(out, null);
                    fail(keyStore.getType());
} catch (UnsupportedOperationException e) {
}
} else if (isNullPasswordAllowed(keyStore)) {
//Synthetic comment -- @@ -1529,7 +1529,7 @@
} else {
try {
keyStore.store(out, null);
                    fail(keyStore.getType());
} catch (Exception e) {
if (e.getClass() != IllegalArgumentException.class
&& e.getClass() != NullPointerException.class) {
//Synthetic comment -- @@ -1545,7 +1545,7 @@
if (isReadOnly(keyStore)) {
try {
keyStore.store(out, PASSWORD_STORE);
                    fail(keyStore.getType());
} catch (UnsupportedOperationException e) {
}
continue;
//Synthetic comment -- @@ -1560,7 +1560,7 @@
if (isReadOnly(keyStore)) {
try {
keyStore.store(out, PASSWORD_STORE);
                    fail(keyStore.getType());
} catch (UnsupportedOperationException e) {
}
continue;
//Synthetic comment -- @@ -1574,7 +1574,7 @@
for (KeyStore keyStore : keyStores()) {
try {
keyStore.store(null);
                fail(keyStore.getType());
} catch (KeyStoreException expected) {
}
}
//Synthetic comment -- @@ -1583,7 +1583,7 @@
keyStore.load(null, null);
try {
keyStore.store(null);
                fail(keyStore.getType());
} catch (UnsupportedOperationException expected) {
assertFalse(isLoadStoreParameterSupported(keyStore));
} catch (IllegalArgumentException expected) {
//Synthetic comment -- @@ -1632,7 +1632,7 @@
return null;
}
});
                fail(keyStore.getType());
} catch (UnsupportedOperationException expected) {
}
}
//Synthetic comment -- @@ -1642,7 +1642,7 @@
for (KeyStore keyStore : keyStores()) {
try {
keyStore.getEntry(null, null);
                fail(keyStore.getType());
} catch (NullPointerException expected) {
}
}
//Synthetic comment -- @@ -1653,12 +1653,12 @@
// test odd inputs
try {
keyStore.getEntry(null, null);
                fail(keyStore.getType());
} catch (NullPointerException expected) {
}
try {
keyStore.getEntry(null, PARAM_KEY);
                fail(keyStore.getType());
} catch (NullPointerException expected) {
}
assertNull(keyStore.getEntry("", null));
//Synthetic comment -- @@ -1709,7 +1709,7 @@
} else {
try {
keyStore.getEntry(ALIAS_PRIVATE, null);
                    fail(keyStore.getType());
} catch (Exception e) {
if (e.getClass() != UnrecoverableKeyException.class
&& e.getClass() != IllegalArgumentException.class) {
//Synthetic comment -- @@ -1722,7 +1722,7 @@
} else if (isSecretKeyEnabled(keyStore)) {
try {
keyStore.getEntry(ALIAS_SECRET, null);
                    fail(keyStore.getType());
} catch (Exception e) {
if (e.getClass() != UnrecoverableKeyException.class
&& e.getClass() != IllegalArgumentException.class) {
//Synthetic comment -- @@ -1739,7 +1739,7 @@
} else {
try {
keyStore.getEntry(ALIAS_PRIVATE, PARAM_BAD);
                    fail(keyStore.getType());
} catch (UnrecoverableKeyException expected) {
}
}
//Synthetic comment -- @@ -1748,7 +1748,7 @@
} else if (isSecretKeyEnabled(keyStore)) {
try {
keyStore.getEntry(ALIAS_SECRET, PARAM_BAD);
                    fail(keyStore.getType());
} catch (UnrecoverableKeyException expected) {
}
}
//Synthetic comment -- @@ -1763,7 +1763,7 @@
keyStore.load(null, null);
try {
keyStore.setEntry(null, null, null);
                fail(keyStore.getType());
} catch (NullPointerException expected) {
}
}
//Synthetic comment -- @@ -1784,7 +1784,7 @@
// test odd inputs
try {
keyStore.setEntry(null, null, null);
                fail(keyStore.getType());
} catch (Exception e) {
if (e.getClass() != NullPointerException.class
&& e.getClass() != KeyStoreException.class) {
//Synthetic comment -- @@ -1793,7 +1793,7 @@
}
try {
keyStore.setEntry(null, null, PARAM_KEY);
                fail(keyStore.getType());
} catch (Exception e) {
if (e.getClass() != NullPointerException.class
&& e.getClass() != KeyStoreException.class) {
//Synthetic comment -- @@ -1802,7 +1802,7 @@
}
try {
keyStore.setEntry("", null, PARAM_KEY);
                fail(keyStore.getType());
} catch (NullPointerException expected) {
}
}
//Synthetic comment -- @@ -1815,7 +1815,7 @@
if (isReadOnly(keyStore)) {
try {
keyStore.setEntry(ALIAS_PRIVATE, getPrivateKey(), PARAM_KEY);
                    fail(keyStore.getType());
} catch (UnsupportedOperationException expected) {
}
continue;
//Synthetic comment -- @@ -1830,7 +1830,7 @@
} else {
try {
keyStore.setKeyEntry(ALIAS_SECRET, getSecretKey(), PASSWORD_KEY, null);
                    fail(keyStore.getType());
} catch (KeyStoreException expected) {
}
}
//Synthetic comment -- @@ -1845,7 +1845,7 @@
keyStore.setEntry(ALIAS_CERTIFICATE,
new TrustedCertificateEntry(getPrivateKey().getCertificate()),
null);
                    fail(keyStore.getType());
} catch (KeyStoreException expected) {
}
}
//Synthetic comment -- @@ -1859,7 +1859,7 @@
} else {
try {
keyStore.setKeyEntry(ALIAS_UNICODE_SECRET, getSecretKey(), PASSWORD_KEY, null);
                    fail(keyStore.getType());
} catch (KeyStoreException expected) {
}
}
//Synthetic comment -- @@ -1944,20 +1944,33 @@
keyStore.load(null, null);

// test with null/non-null passwords
            if (isReadOnly(keyStore)) {
                try {
                    keyStore.setEntry(ALIAS_PRIVATE, getPrivateKey(), null);
                    fail(keyStore.getType());
                } catch (UnsupportedOperationException expected) {
}
try {
keyStore.setEntry(ALIAS_SECRET, new SecretKeyEntry(getSecretKey()), null);
                    fail(keyStore.getType());
                } catch (UnsupportedOperationException expected) {
                }
                try {
                    keyStore.setEntry(ALIAS_CERTIFICATE,
                                      new TrustedCertificateEntry(getPrivateKey().getCertificate()),
                                      null);
                    fail(keyStore.getType());
                } catch (UnsupportedOperationException expected) {
                }
                continue;
            }
            if (isNullPasswordAllowed(keyStore) || isKeyPasswordIgnored(keyStore)) {
                keyStore.setEntry(ALIAS_PRIVATE, getPrivateKey(), null);
                assertPrivateKey(keyStore.getKey(ALIAS_PRIVATE, null));
            } else {
                try {
                    keyStore.setEntry(ALIAS_PRIVATE, getPrivateKey(), null);
                    fail(keyStore.getType());
} catch (Exception e) {
if (e.getClass() != UnrecoverableKeyException.class
&& e.getClass() != IllegalArgumentException.class
//Synthetic comment -- @@ -1966,15 +1979,22 @@
}
}
}
            if (isSecretKeyEnabled(keyStore)) {
                if (isNullPasswordAllowed(keyStore) || isKeyPasswordIgnored(keyStore)) {
                    keyStore.setEntry(ALIAS_SECRET, new SecretKeyEntry(getSecretKey()), null);
                    assertSecretKey(keyStore.getKey(ALIAS_SECRET, null));
                } else {                    
                    try {
                        keyStore.setEntry(ALIAS_SECRET, new SecretKeyEntry(getSecretKey()), null);
                        fail(keyStore.getType());
                    } catch (Exception e) {
                        if (e.getClass() != UnrecoverableKeyException.class
                            && e.getClass() != IllegalArgumentException.class
                            && e.getClass() != KeyStoreException.class) {
                            throw e;
                        }
                    }
}
}
if (isCertificateEnabled(keyStore)) {
if (isNullPasswordAllowed(keyStore) || isKeyPasswordIgnored(keyStore)) {
//Synthetic comment -- @@ -1988,7 +2008,7 @@
new TrustedCertificateEntry(
getPrivateKey().getCertificate()),
PARAM_KEY);
                        fail(keyStore.getType());
} catch (KeyStoreException expected) {
}
}
//Synthetic comment -- @@ -2000,7 +2020,7 @@
for (KeyStore keyStore : keyStores()) {
try {
keyStore.entryInstanceOf(null, null);
                fail(keyStore.getType());
} catch (NullPointerException expected) {
}
}
//Synthetic comment -- @@ -2010,17 +2030,17 @@

try {
keyStore.entryInstanceOf(null, null);
                fail(keyStore.getType());
} catch (NullPointerException expected) {
}
try {
keyStore.entryInstanceOf(null, Entry.class);
                fail(keyStore.getType());
} catch (NullPointerException expected) {
}
try {
keyStore.entryInstanceOf("", null);
                fail(keyStore.getType());
} catch (NullPointerException expected) {
}

//Synthetic comment -- @@ -2095,7 +2115,7 @@
keyStore.load(null, null);
try {
Builder.newInstance(keyStore, null);
                fail(keyStore.getType());
} catch (NullPointerException expected) {
}
}
//Synthetic comment -- @@ -2105,7 +2125,7 @@
Builder.newInstance(keyStore.getType(),
keyStore.getProvider(),
null);
                fail(keyStore.getType());
} catch (NullPointerException expected) {
}
}
//Synthetic comment -- @@ -2116,7 +2136,7 @@
null,
null,
null);
                fail(keyStore.getType());
} catch (NullPointerException expected) {
}
try {
//Synthetic comment -- @@ -2124,7 +2144,7 @@
keyStore.getProvider(),
null,
null);
                fail(keyStore.getType());
} catch (NullPointerException expected) {
}
}
//Synthetic comment -- @@ -2134,13 +2154,13 @@
Builder builder = Builder.newInstance(keyStore, PARAM_STORE);
try {
builder.getProtectionParameter(null);
                fail(keyStore.getType());
} catch (NullPointerException expected) {
}
assertEquals(keyStore, builder.getKeyStore());
try {
builder.getProtectionParameter(null);
                fail(keyStore.getType());
} catch (NullPointerException expected) {
}
assertEquals(PARAM_STORE, builder.getProtectionParameter(""));
//Synthetic comment -- @@ -2156,7 +2176,7 @@
if (isReadOnly(keyStore)) {
try {
keyStore.store(os, PASSWORD_STORE);
                        fail(keyStore.getType());
} catch (UnsupportedOperationException expected) {
}
continue;







