//<Beginning of snippet n. 0>
ViewNode.Property property = new ViewNode.Property();

try {
    if (start < 0 || index < 0 || index >= data.length()) {
        throw new IllegalArgumentException("Invalid 'start' or 'index' values.");
    }
    
    property.name = data.substring(start, index);
    
    int index2 = data.indexOf(',', index + 1);
    if (index2 == -1 || index2 <= index + 1 || index2 >= data.length()) {
        throw new IllegalArgumentException("Invalid data format: expected a length after comma.");
    }
    
    int length = Integer.parseInt(data.substring(index + 1, index2));
    if (length < 0) {
        throw new IllegalArgumentException("Length derived from input cannot be negative.");
    }

    if (start + length > data.length()) {
        throw new IllegalArgumentException("Calculated length exceeds remaining substring length.");
    }
    
    start = index2 + 1 + length;
} catch (StringIndexOutOfBoundsException e) {
    System.err.println("Error: String index out of bounds at position: " + e.getMessage());
    property.name = "default";  
} catch (NumberFormatException e) {
    System.err.println("Error parsing number: " + e.getMessage());
    property.name = "default";  
} catch (IllegalArgumentException e) {
    System.err.println("Error: " + e.getMessage());
    property.name = "default";  
}
//<End of snippet n. 0>