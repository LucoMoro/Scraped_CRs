//<Beginning of snippet n. 0>

ViewNode.Property property = new ViewNode.Property();
if (data != null && start < data.length() && data.indexOf(',', start) != -1) {
    property.name = data.substring(start, index);
    
    int index2 = data.indexOf(',', index + 1);
    if (index2 != -1 && index + 1 < data.length()) {
        try {
            int length = Integer.parseInt(data.substring(index + 1, index2).trim());
            start = index2 + 1 + length;
        } catch (NumberFormatException e) {
            // Handle invalid number format appropriately
        }
    }
}

//<End of snippet n. 0>