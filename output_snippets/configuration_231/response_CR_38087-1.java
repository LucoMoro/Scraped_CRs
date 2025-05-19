//<Beginning of snippet n. 0>
private InputStream getInputStream(String urlString, Monitor monitor) {
    InputStream xml = cache.openCachedUrl(urlString, monitor);
    if (xml != null) {
        try {
            xml.mark(500000);
            return xml; // Directly return the InputStream without wrapping
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error while getting InputStream.", e);
        } 
    }
    return null;
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
private Validator getValidator(int version) throws SAXException {
    try (InputStream xsdStream = SdkStatsConstants.getXsdStream(version)) {
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
    } catch (Exception e) {
        Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error getting validator for version " + version, e);
        throw new SAXException("Failed to get validator.", e);
    }
}
//<End of snippet n. 1>