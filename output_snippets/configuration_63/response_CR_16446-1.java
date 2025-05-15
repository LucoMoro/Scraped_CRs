//<Beginning of snippet n. 0>
ViewNode.Property property = new ViewNode.Property();
if (data != null && data.contains(",") && data.length() > 0) {
    property.name = data.substring(start, index);
    int index2 = data.indexOf(',', index + 1);
    if (index2 != -1) {
        try {
            int length = Integer.parseInt(data.substring(index + 1, index2));
            start = index2 + 1 + length;
        } catch (NumberFormatException e) {
            // Handle the exception (log it or rethrow it as needed)
        }
    }
}
//<End of snippet n. 0>