//<Beginning of snippet n. 0>
@Override
public void ancestorMoved(AncestorEvent event) {
    if (event.getSource() instanceof FileDropTarget) {
        File file = ((FileDropTarget) event.getSource()).getDroppedFile();
        if (file != null && isValidImageFile(file)) {
            resetViewerState();
            loadImage(file);
            viewer.setZoom(DEFAULT_ZOOM);
        } else {
            showError("Invalid image file or loading failed.");
        }
    }
    viewer.removeAncestorListener(this);
}

private void resetViewerState() {
    viewer.clearContent();
    viewer.resetAttributes();
}

private boolean isValidImageFile(File file) {
    // Assuming a method to validate image files
    return file.exists() && file.isFile() && file.getName().endsWith(".png" || ".jpg" || ".jpeg");
}

private void loadImage(File file) {
    // Load the image and handle any errors
    try {
        BufferedImage image = ImageIO.read(file);
        if (image != null) {
            viewer.setImage(image);
        } else {
            throw new IOException("Failed to load image.");
        }
    } catch (IOException e) {
        showError("Error loading image: " + e.getMessage());
    }
}

private void showError(String message) {
    JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
}
@Override
public void ancestorAdded(AncestorEvent event) {
}
//<End of snippet n. 0>