//<Beginning of snippet n. 0>
if (data != null && !data.isEmpty() && start >= 0 && index >= 0) {
    ViewNode.Property property = new ViewNode.Property();
    property.name = data.substring(start, index).trim();

    int index2 = data.indexOf(',', index + 1);
    if (index2 != -1 && index2 > index + 1) {
        try {
            int length = Integer.parseInt(data.substring(index + 1, index2).trim());
            start = index2 + 1 + length;
            // Further categorization logic to be applied here
        } catch (NumberFormatException e) {
            // Handle the error accordingly, possibly log it
        }
    }
}
//<End of snippet n. 0>