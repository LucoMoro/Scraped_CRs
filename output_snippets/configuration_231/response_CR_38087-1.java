//<Beginning of snippet n. 1>
private Validator getValidator(int version) throws SAXException {
    InputStream xsdStream = SdkStatsConstants.getXsdStream(version);
    if (xsdStream == null) {
        throw new IllegalArgumentException("XSD stream is null.");
    }

    SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

    Schema schema = factory.newSchema(new StreamSource(xsdStream));
    Validator validator = schema == null ? null : schema.newValidator();

    try (InputStream secureXsdStream = xsdStream) {
        return validator;
    } catch (IOException e) {
        throw new SAXException("Error processing XSD stream", e);
    }
}
//<End of snippet n. 1>