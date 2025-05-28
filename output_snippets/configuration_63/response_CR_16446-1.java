//<Beginning of snippet n. 0>
ViewNode.Property property = new ViewNode.Property();
if (data != null && start >= 0 && index >= 0 && index < data.length()) {
    property.name = data.substring(start, index);
    
    int index2 = data.indexOf(',', index + 1);
    if (index2 != -1 && index2 < data.length()) {
        String lengthStr = data.substring(index + 1, index2);
        if (lengthStr != null && lengthStr.matches("\\d+")) {
            int length = Integer.parseInt(lengthStr);
            if (length >= 0 && start + length <= data.length()) {
                start = index2 + 1 + length;
            } else {
                throw new IllegalArgumentException("Calculated length exceeds data bounds.");
            }
        } else {
            throw new IllegalArgumentException("Length string is malformed or not a digit.");
        }
    } else {
        throw new IllegalArgumentException("Expected delimiter ',' not found.");
    }
} else {
    throw new IllegalArgumentException("Invalid input parameters.");
}
//<End of snippet n. 0>