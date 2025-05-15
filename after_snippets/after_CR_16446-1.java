
//<Beginning of snippet n. 0>


ViewNode.Property property = new ViewNode.Property();
property.name = data.substring(start, index);

            int colonIndex = property.name.indexOf(':');
            if (colonIndex != -1) {
                property.name = property.name.substring(colonIndex + 1);
            }

int index2 = data.indexOf(',', index + 1);
int length = Integer.parseInt(data.substring(index + 1, index2));
start = index2 + 1 + length;

//<End of snippet n. 0>








