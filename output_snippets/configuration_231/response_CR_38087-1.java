//<Beginning of snippet n. 0>
try (InputStream xml = cache.openCachedUrl(urlString, monitor)) {
    if (xml != null) {
        xml.mark(500000);
        xml = new NonClosingInputStream(xml).setCloseBehavior(CloseBehavior.RESET);
    }
    return xml;
} catch (Exception e) {
    Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error opening cached URL", e);
    throw new RuntimeException("Error opening cached URL", e);
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
private Validator getValidator(int version) throws SAXException {
    InputStream xsdStream = SdkStatsConstants.getXsdStream(version);
    if (xsdStream == null) {
        throw new IllegalArgumentException("XSD stream cannot be null");
    }

    SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

    try {
        Schema schema = factory.newSchema(new StreamSource(xsdStream));
        return schema != null ? schema.newValidator() : null;
    } finally {
        xsdStream.close();
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
    Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error opening cached URL", e);
    throw new RuntimeException("Error opening cached URL", e);
}
//<End of snippet n. 2>