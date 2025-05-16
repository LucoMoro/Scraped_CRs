//<Beginning of snippet n. 0>

private static final Pattern sSkippingPattern =
        Pattern.compile("\\(skipping .+ .+ '(.*)'\\)"); //$NON-NLS-1$

private static final List<String> benignFileTypes = Arrays.asList(".git", "layout.xml~");

m = sSkippingPattern.matcher(p);
if (m.matches()) {
    String location = m.group(1);
    String fileName = location.trim();

    if (benignFileTypes.stream().anyMatch(fileName::endsWith)) {
        System.out.println("Skipping benign file: " + fileName);
    } else {
        try {
            checkAndMark(location, null, p.trim(), osRoot, project);
        } catch (Exception e) {
            System.err.println("Error processing file: " + fileName + ". Exception: " + e.getMessage());
        }
    }
}

//<End of snippet n. 0>