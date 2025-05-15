//<Beginning of snippet n. 0>
@Override
public void ancestorMoved(AncestorEvent event) {
    // Set exactly size.
    viewer.setZoom(DEFAULT_ZOOM);
    loadImageForViewer(event.getAncestor());
    viewer.removeAncestorListener(this);
}

@Override
public void ancestorAdded(AncestorEvent event) {
    try {
        loadImageForViewer(event.getAncestor());
    } catch (IOException e) {
        // Log or display error information
        System.err.println("Error loading image: " + e.getMessage());
    }
}

private void loadImageForViewer(Object ancestor) throws IOException {
    // Logic to load the image based on ancestor
    if (ancestor instanceof File) {
        File imageFile = (File) ancestor;
        BufferedImage image = ImageIO.read(imageFile);
        viewer.setImage(image);
    } else {
        throw new IOException("Invalid ancestor type for image loading.");
    }
}
//<End of snippet n. 0>