//<Beginning of snippet n. 0>
ViewNode.Property property = new ViewNode.Property();
if (data != null && start >= 0 && index >= 0 && index < data.length()) {
    property.name = data.substring(start, index);
    property.category = categorizeProperty(property.name); // Implement categorization logic

    int index2 = data.indexOf(',', index + 1);
    if (index2 != -1 && index2 < data.length()) {
        String lengthStr = data.substring(index + 1, index2);
        if (lengthStr != null && !lengthStr.isEmpty() && lengthStr.matches("\\d+")) {
            int length = Integer.parseInt(lengthStr);
            if (length >= 0 && start + length <= data.length()) {
                start = index2 + 1 + length;
            } else {
                throw new InvalidDataException("Calculated length exceeds data bounds."); // Custom exception
            }
        } else {
            throw new InvalidDataException("Length string is malformed or not a digit."); // Custom exception
        }
    } else {
        throw new InvalidDataException("Expected delimiter ',' not found."); // Custom exception
    }
} else {
    throw new InvalidDataException("Invalid input parameters."); // Custom exception
}

private String categorizeProperty(String propertyName) {
    // Logic to categorize property based on its name or other criteria
    return "default"; // Replace with actual categorization logic
}

class InvalidDataException extends IllegalArgumentException {
    public InvalidDataException(String message) {
        super(message);
    }
}
//<End of snippet n. 0>