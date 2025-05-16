//<Beginning of snippet n. 0>
@Override
public void ancestorMoved(AncestorEvent event) {
    double currentZoom = viewer.getZoom(); 
    viewer.setZoom(currentZoom);
    viewer.removeAncestorListener(this);
}

@Override
public void ancestorAdded(AncestorEvent event) {
    File file = ...; // logic to retrieve the file from event or context
    try {
        BufferedImage image = ImageIO.read(file);
        viewer.setImage(image);
        viewer.setZoom(viewer.getDefaultZoom());
    } catch (IOException e) {
        JOptionPane.showMessageDialog(viewer, "Failed to load image: " + e.getMessage());
    }
}
//<End of snippet n. 0>