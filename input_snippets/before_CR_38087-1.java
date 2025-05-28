
//<Beginning of snippet n. 0>


InputStream xml = cache.openCachedUrl(urlString, monitor);
if (xml != null) {
xml.mark(500000);
                xml = new NonClosingInputStream(xml).setCloseBehavior(CloseBehavior.RESET);
}
return xml;
} catch (Exception e) {

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


InputStream xml = cache.openCachedUrl(urlString, monitor);
if (xml != null) {
xml.mark(500000);
                xml = new NonClosingInputStream(xml).setCloseBehavior(CloseBehavior.RESET);
}
return xml;
} catch (Exception e) {
*/
private Validator getValidator(int version) throws SAXException {
InputStream xsdStream = SdkStatsConstants.getXsdStream(version);
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        if (factory == null) {
            return null;
}

        // This may throw a SAX Exception if the schema itself is not a valid XSD
        Schema schema = factory.newSchema(new StreamSource(xsdStream));

        Validator validator = schema == null ? null : schema.newValidator();

        return validator;
}

/**

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


InputStream xml = cache.openCachedUrl(urlString, monitor);
if (xml != null) {
xml.mark(500000);
                xml = new NonClosingInputStream(xml).setCloseBehavior(CloseBehavior.RESET);
}
return xml;
} catch (Exception e) {

//<End of snippet n. 2>








