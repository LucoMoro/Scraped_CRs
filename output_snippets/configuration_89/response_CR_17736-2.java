//<Beginning of snippet n. 0>

String data = sb.toString();
if (data != null && data.length() > 0) {
    clipboard.setContents(
        new Object[] { data },
        new Transfer[] { TextTransfer.getInstance() });
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

return new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID, String.format(
    Messages.DexWrapper_s_does_not_exists, osFilepath));
}
URL url = f.toURI().toURL();

URLClassLoader loader = new URLClassLoader(new URL[] { url },
    DexWrapper.class.getClassLoader());

//<End of snippet n. 1>

//<Beginning of snippet n. 2>

if (resource.getType() == IResource.FILE) {
    local = true;
    try {
        oslibraryList.add(
            new File(resource.getLocation().toOSString()).toURI().toURL());
    } catch (MalformedURLException mue) {
        // Improved error handling
        Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Malformed URL Exception: ", mue);
    }
    File f = new File(osFullPath);
    if (f.exists()) {
        try {
            oslibraryList.add(f.toURI().toURL());
        } catch (MalformedURLException mue) {
            // Improved error handling
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Malformed URL Exception: ", mue);
        }
    }
//<End of snippet n. 2>