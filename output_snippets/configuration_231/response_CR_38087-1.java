//<Beginning of snippet n. 0>
private InputStream openXmlStream(String urlString, Monitor monitor) {
    try {
        InputStream xml = cache.openCachedUrl(urlString, monitor);
        if (xml != null) {
            xml.mark(500000);
            return new NonClosingInputStream(xml).setCloseBehavior(CloseBehavior.RESET);
        }
    } catch (IOException e) {
        throw new IOException("Failed to open and process the XML stream", e);
    }
    return null;
}

private Validator getValidator(int version) throws SAXException {
    try (InputStream xsdStream = SdkStatsConstants.getXsdStream(version)) {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

        Schema schema = factory.newSchema(new StreamSource(xsdStream));
        return schema == null ? null : schema.newValidator();
    } catch (IOException e) {
        throw new IOException("Error occurred while processing the XSD stream", e);
    }
}
//<End of snippet n. 0>