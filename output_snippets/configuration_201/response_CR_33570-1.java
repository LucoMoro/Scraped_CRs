//<Beginning of snippet n. 0>
@Override
public void ancestorMoved(AncestorEvent event) {
    viewer.setZoom(DEFAULT_ZOOM);
    viewer.removeAncestorListener(this);
}

@Override
public void ancestorAdded(AncestorEvent event) {
    viewer.resetState();
    File file = getFileFromEvent(event);
    if (file != null && file.exists() && isValidFileType(file)) {
        try {
            boolean success = loadImage(file);
            if (success) {
                viewer.repaint();
            } else {
                showError("Image failed to load. Please check the file.");
            }
        } catch (IOException e) {
            showError("Error loading image: " + e.getMessage());
        }
    } else {
        showError("File does not exist, is invalid, or unsupported format.");
    }
}

private boolean isValidFileType(File file) {
    String[] supportedExtensions = {"jpg", "jpeg", "png", "gif"};
    String fileName = file.getName().toLowerCase();
    for (String ext : supportedExtensions) {
        if (fileName.endsWith("." + ext)) {
            return true;
        }
    }
    return false;
}

private void showError(String message) {
    JOptionPane.showMessageDialog(null, message, "Image Load Error", JOptionPane.ERROR_MESSAGE);
}
//<End of snippet n. 0>