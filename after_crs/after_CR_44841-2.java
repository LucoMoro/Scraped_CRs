/*AlgNameMapper: make test case insensitive

Change-Id:I17263ef991e192226f8ca91e7d07d49b7ae3247e*/




//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/security/tests/AlgNameMapperTest.java b/luni/src/test/java/org/apache/harmony/security/tests/AlgNameMapperTest.java
//Synthetic comment -- index aa69384..943eb43 100644

//Synthetic comment -- @@ -53,15 +53,15 @@

for (int i = 0; i < HARDCODED_ALIASES.length; i++) {
try {
                assertEquals(HARDCODED_ALIASES[i][1].toUpperCase(Locale.US),
                        AlgNameMapper.map2AlgName(HARDCODED_ALIASES[i][0]).toUpperCase(Locale.US));

assertEquals(HARDCODED_ALIASES[i][0],
AlgNameMapper.map2OID(HARDCODED_ALIASES[i][1]));

                assertEquals(HARDCODED_ALIASES[i][1].toUpperCase(Locale.US),
AlgNameMapper.getStandardName(HARDCODED_ALIASES[i][1]
                                .toUpperCase(Locale.US)).toUpperCase(Locale.US));

assertTrue(AlgNameMapper.isOID(HARDCODED_ALIASES[i][0]));
} catch (Throwable e) {
//Synthetic comment -- @@ -78,7 +78,7 @@

private final String[][] NON_HARDCODED_ALIASES = {
{"2.16.840.1.101.3.4.2.3", "SHA512"}, // This isn't currently hardcoded in AlgNameMapper
            {"1.2.840.10045.3.1.7", "prime256v1"}, // No provider provides EC curves
};

public void testNon_Hardcoded_Aliases_Exist() throws Exception {
//Synthetic comment -- @@ -89,7 +89,8 @@
try {
String algName = AlgNameMapper.map2AlgName(NON_HARDCODED_ALIASES[i][0]);
assertNotNull(algName);
                assertEquals(NON_HARDCODED_ALIASES[i][1].toUpperCase(Locale.US),
                        algName.toUpperCase(Locale.US));

String oid = AlgNameMapper.map2OID(algName);
assertNotNull(oid);







