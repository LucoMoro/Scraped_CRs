//<Beginning of snippet n. 0>

InputStream xml = null;
try (InputStream cachedXml = cache.openCachedUrl(urlString, monitor)) {
    if (cachedXml != null) {
        cachedXml.mark(500000);
        xml = new NonClosingInputStream(cachedXml).setCloseBehavior(CloseBehavior.RESET);
    }
    return xml;
} catch (IOException e) {
    // Handle IO specific exceptions
    e.printStackTrace();
    return null; // or throw a custom exception as needed
} catch (Exception e) {
    // Handle other exceptions
    e.printStackTrace();
    return null; // or throw a custom exception as needed
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

private Validator getValidator(int version) throws SAXException {
    try (InputStream xsdStream = SdkStatsConstants.getXsdStream(version)) {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        if (factory == null) {
            return null;
        }

        // This may throw a SAX Exception if the schema itself is not a valid XSD
        Schema schema = factory.newSchema(new StreamSource(xsdStream));

        Validator validator = schema == null ? null : schema.newValidator();

        return validator;
    } catch (IOException e) {
        // Handle IO specific exceptions
        e.printStackTrace();
        throw new SAXException("Error reading the schema", e);
    } catch (SAXException e) {
        // Handle SAX exceptions specifically
        throw e;
    } catch (Exception e) {
        // Handle other exceptions
        e.printStackTrace();
        throw new SAXException("An unexpected error occurred while getting the validator", e);
    }
}

//<End of snippet n. 1>

//<Beginning of snippet n. 2>

InputStream xml = null;
try (InputStream cachedXml = cache.openCachedUrl(urlString, monitor)) {
    if (cachedXml != null) {
        cachedXml.mark(500000);
        xml = new NonClosingInputStream(cachedXml).setCloseBehavior(CloseBehavior.RESET);
    }
    return xml;
} catch (IOException e) {
    // Handle IO specific exceptions
    e.printStackTrace();
    return null; // or throw a custom exception as needed
} catch (Exception e) {
    // Handle other exceptions
    e.printStackTrace();
    return null; // or throw a custom exception as needed
}

//<End of snippet n. 2>