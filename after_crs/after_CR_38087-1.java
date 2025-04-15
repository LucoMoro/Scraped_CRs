/*Workaround for Eclipse resource detection.

Plus close a potentially unclosed resource.

Change-Id:I4eb69c28dc120ab3eb5fe5724149b749ed9fe335*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AddonsListFetcher.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AddonsListFetcher.java
//Synthetic comment -- index 55021a3..328204c 100755

//Synthetic comment -- @@ -267,7 +267,8 @@
InputStream xml = cache.openCachedUrl(urlString, monitor);
if (xml != null) {
xml.mark(500000);
                xml = new NonClosingInputStream(xml);
                ((NonClosingInputStream) xml).setCloseBehavior(CloseBehavior.RESET);
}
return xml;
} catch (Exception e) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkStats.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkStats.java
//Synthetic comment -- index 60bb89d..83c9c3a 100755

//Synthetic comment -- @@ -252,7 +252,8 @@
InputStream xml = cache.openCachedUrl(urlString, monitor);
if (xml != null) {
xml.mark(500000);
                xml = new NonClosingInputStream(xml);
                ((NonClosingInputStream) xml).setCloseBehavior(CloseBehavior.RESET);
}
return xml;
} catch (Exception e) {
//Synthetic comment -- @@ -446,18 +447,26 @@
*/
private Validator getValidator(int version) throws SAXException {
InputStream xsdStream = SdkStatsConstants.getXsdStream(version);
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            if (factory == null) {
                return null;
            }

            // This may throw a SAX Exception if the schema itself is not a valid XSD
            Schema schema = factory.newSchema(new StreamSource(xsdStream));

            Validator validator = schema == null ? null : schema.newValidator();

            return validator;
        } finally {
            if (xsdStream != null) {
                try {
                    xsdStream.close();
                } catch (IOException ignore) {}
            }
}
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkSource.java
//Synthetic comment -- index a0d515a..ee3b0af 100755

//Synthetic comment -- @@ -631,7 +631,8 @@
InputStream xml = cache.openCachedUrl(urlString, monitor);
if (xml != null) {
xml.mark(500000);
                xml = new NonClosingInputStream(xml);
                ((NonClosingInputStream) xml).setCloseBehavior(CloseBehavior.RESET);
}
return xml;
} catch (Exception e) {







