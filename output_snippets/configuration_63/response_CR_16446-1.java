//<Beginning of snippet n. 0>

ViewNode.Property property = new ViewNode.Property();
property.name = data.substring(start, index);

int index2 = data.indexOf(',', index + 1);
if (index2 == -1) throw new IllegalArgumentException("Invalid index for property length");
int length = Integer.parseInt(data.substring(index + 1, index2));
if (length < 0) throw new IllegalArgumentException("Property length cannot be negative");
start = index2 + 1 + length;

if (start < 0 || start >= data.length()) throw new IndexOutOfBoundsException("Start index out of bounds");

property.type = determinePropertyType(property.name);
property.category = determineCategory(property.name);

validateProperty(property);
if (property.type == null || property.category == null) {
    throw new IllegalArgumentException("Property type or category cannot be null");
}

//<End of snippet n. 0>