//<Beginning of snippet n. 0>
@Override
public void ancestorMoved(AncestorEvent event) {
    viewer.setZoom(DEFAULT_ZOOM);
    viewer.repaint();
    viewer.removeAncestorListener(this);
}

@Override
public void ancestorAdded(AncestorEvent event) {
    File file = event.getFile(); // Assuming getFile() to retrieve the added file
    if (file != null && file.exists() && isValidImageFile(file)) {
        try {
            BufferedImage image = ImageIO.read(file);
            viewer.setImage(image); // Assuming setImage() method to display the image
            viewer.repaint();
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error loading image", e);
        }
    } else {
        Logger.getLogger(getClass().getName()).log(Level.WARNING, "Invalid image file: " + file);
    }
}

private boolean isValidImageFile(File file) {
    String[] validExtensions = { "jpg", "jpeg", "png", "gif" };
    String filename = file.getName().toLowerCase();
    for (String ext : validExtensions) {
        if (filename.endsWith("." + ext)) {
            return true;
        }
    }
    return false;
}
//<End of snippet n. 0>