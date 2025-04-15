/*Fix unit test: sort XML node attribute map.

The XML attributes is a map, which order is not
guaranteed. Always sort the attributes when
doing xml-to-string dump for unit test purposes.

Change-Id:I7d5198dcce6749931147ad168061755ba80abe57*/
//Synthetic comment -- diff --git a/manifmerger/src/main/java/com/android/manifmerger/MergerXmlUtils.java b/manifmerger/src/main/java/com/android/manifmerger/MergerXmlUtils.java
//Synthetic comment -- index bb60464..9e15353 100755

//Synthetic comment -- @@ -390,15 +390,11 @@
case Node.ELEMENT_NODE:
String attr = "";
if (keyAttr != null) {
                        NamedNodeMap attrs = node.getAttributes();
                        if (attrs != null) {
                            for (int i = 0; i < attrs.getLength(); i++) {
                                Node a = attrs.item(i);
                                if (a != null && keyAttr.equals(a.getLocalName())) {
                                    attr = String.format(" %1$s=%2$s",
                                            a.getNodeName(), a.getNodeValue());
                                    break;
                                }
}
}
}
//Synthetic comment -- @@ -442,8 +438,7 @@
}

if (deep) {
                    List<Attr> attrs = sortedAttributeList(node.getAttributes());
                    for (Attr attr : attrs) {
sb.append(String.format("%1$s    @%2$s = %3$s\n",
offset, attr.getNodeName(), attr.getNodeValue()));
}
//Synthetic comment -- @@ -501,16 +496,19 @@
return new Comparator<Attr>() {
@Override
public int compare(Attr a1, Attr a2) {
                String s1 = a1 == null ? "" : a1.getNodeName();           //$NON-NLS-1$
                String s2 = a2 == null ? "" : a2.getNodeValue();          //$NON-NLS-1$

                int prio1 = s1.equals("name") ? 0 : 1;                    //$NON-NLS-1$
                int prio2 = s2.equals("name") ? 0 : 1;                    //$NON-NLS-1$
                if (prio1 == 0 || prio2 == 0) {
                    return prio1 - prio2;
}

                return s1.compareTo(s2);
}
};
}
//Synthetic comment -- @@ -719,7 +717,7 @@
}

/**
     * Flatten several attributes to a string using their alphabethical order.
* This is an implementation detail for {@link #printElement(Node, Map, String)}.
*/
@NonNull
//Synthetic comment -- @@ -768,7 +766,7 @@
* This is a <em>not</em> designed to be a full contextual diff.
* It just stops at the first difference found, printing up to 3 lines of diff
* and backtracking to add prior contextual information to understand the
     * structure of the element where the first diff line occured (by printing
* each parent found till the root one as well as printing the attribute
* named by {@code keyAttr}).
*







