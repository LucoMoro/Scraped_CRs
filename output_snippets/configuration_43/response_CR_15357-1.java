//<Beginning of snippet n. 0>
try {
    if (inUrl == null || inUrl.isEmpty()) {
        throw new IllegalArgumentException("URL cannot be null or empty.");
    }

    URL url = new URL(inUrl);
    
    String protocol = url.getProtocol();
    if (protocol == null || (!protocol.equals("http") && !protocol.equals("https"))) {
        throw new MalformedURLException("Invalid protocol: " + protocol);
    }

    webAddress = new WebAddress(url.toString());
} catch (IllegalArgumentException e) {
    System.err.println("Error: " + e.getMessage());
} catch (MalformedURLException e) {
    System.err.println("Error: " + e.getMessage());
} catch (Exception e) {
    System.err.println("An unexpected error occurred: " + e.getMessage());
}
//<End of snippet n. 0>