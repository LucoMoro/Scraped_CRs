//<Beginning of snippet n. 0>
try (InputStream xml = cache.openCachedUrl(urlString, monitor)) {
    if (xml != null) {
        xml.mark(500000);
        xml = new NonClosingInputStream(xml).setCloseBehavior(CloseBehavior.RESET);
    }
    return xml;
} catch (Exception e) {
    // Handle exception (logging without disclosing sensitive information)
    throw new RuntimeException("Error processing XML", e);
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
private Validator getValidator(int version) throws SAXException {
    InputStream xsdStream = null;
    try {
        xsdStream = SdkStatsConstants.getXsdStream(version);
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

        if (factory == null) {
            return null;
        }

        Schema schema = factory.newSchema(new StreamSource(xsdStream));
        Validator validator = schema == null ? null : schema.newValidator();
        return validator;
    } finally {
        if (xsdStream != null) {
            try {
                xsdStream.close();
            } catch (IOException e) {
                // Handle exception during InputStream closure
            }
        }
    }
}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
try (InputStream xml = cache.openCachedUrl(urlString, monitor)) {
    if (xml != null) {
        xml.mark(500000);
        xml = new NonClosingInputStream(xml).setCloseBehavior(CloseBehavior.RESET);
    }
    return xml;
} catch (Exception e) {
    // Handle exception (logging without disclosing sensitive information)
    throw new RuntimeException("Error processing XML", e);
}
//<End of snippet n. 2>