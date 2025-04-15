/*droiddoc: fix compiler warnings

Change-Id:I2483188966d453884a41445f2fe8a8f70164ac90*/
//Synthetic comment -- diff --git a/tools/droiddoc/src/Converter.java b/tools/droiddoc/src/Converter.java
//Synthetic comment -- index ee911f4..ffbbe20 100644

//Synthetic comment -- @@ -689,8 +689,10 @@
}

// annotation values
    private static HashMap<AnnotationValue,AnnotationValueInfo> mAnnotationValues = new HashMap();
    private static HashSet<AnnotationValue> mAnnotationValuesNeedingInit = new HashSet();

private static AnnotationValueInfo obtainAnnotationValue(AnnotationValue o, MethodInfo element)
{
//Synthetic comment -- @@ -743,7 +745,7 @@
int depth = 0;
while (mAnnotationValuesNeedingInit.size() > 0) {
HashSet<AnnotationValue> set = mAnnotationValuesNeedingInit;
            mAnnotationValuesNeedingInit = new HashSet();
for (AnnotationValue o: set) {
AnnotationValueInfo v = mAnnotationValues.get(o);
initAnnotationValue(o, v);








//Synthetic comment -- diff --git a/tools/droiddoc/src/DroidDoc.java b/tools/droiddoc/src/DroidDoc.java
//Synthetic comment -- index 4e9d6b1..6976e47 100644

//Synthetic comment -- @@ -178,7 +178,7 @@
stubsDir = a[1];
}
else if (a[0].equals("-stubpackages")) {
                stubPackages = new HashSet();
for (String pkg: a[1].split(":")) {
stubPackages.add(pkg);
}
//Synthetic comment -- @@ -706,7 +706,7 @@
sorted.put(name, pkg);
}

        ArrayList<PackageInfo> result = new ArrayList();

for (String s: sorted.keySet()) {
PackageInfo pkg = sorted.get(s);








//Synthetic comment -- diff --git a/tools/droiddoc/src/NavTree.java b/tools/droiddoc/src/NavTree.java
//Synthetic comment -- index 0469fdc..1dea4b7 100644

//Synthetic comment -- @@ -21,7 +21,7 @@
public class NavTree {

public static void writeNavTree(String dir) {
        ArrayList<Node> children = new ArrayList();
for (PackageInfo pkg: DroidDoc.choosePackages()) {
children.add(makePackageNode(pkg));
}
//Synthetic comment -- @@ -44,7 +44,7 @@
}

private static Node makePackageNode(PackageInfo pkg) {
        ArrayList<Node> children = new ArrayList();

children.add(new Node("Description", pkg.fullDescriptionHtmlPage(), null, null));

//Synthetic comment -- @@ -58,7 +58,7 @@
}

private static void addClassNodes(ArrayList<Node> parent, String label, ClassInfo[] classes) {
        ArrayList<Node> children = new ArrayList();

for (ClassInfo cl: classes) {
if (cl.checkLevel()) {








//Synthetic comment -- diff --git a/tools/droiddoc/src/Stubs.java b/tools/droiddoc/src/Stubs.java
//Synthetic comment -- index e1ec76a..b9e9371 100644

//Synthetic comment -- @@ -30,7 +30,7 @@
public static void writeStubs(String stubsDir, Boolean writeXML, String xmlFile,
HashSet<String> stubPackages) {
// figure out which classes we need
        notStrippable = new HashSet();
ClassInfo[] all = Converter.allClasses();
File  xml = new File(xmlFile);
xml.getParentFile().mkdirs();
//Synthetic comment -- @@ -359,7 +359,7 @@
stream.print("strictfp ");
}

        HashSet<String> classDeclTypeVars = new HashSet();
String leafName = cl.asTypeInfo().fullName(classDeclTypeVars);
int bracket = leafName.indexOf('<');
if (bracket < 0) bracket = leafName.length() - 1;
//Synthetic comment -- @@ -547,7 +547,7 @@
stream.print("strictfp ");
}

        stream.print(method.typeArgumentsName(new HashSet()) + " ");

if (!isConstructor) {
stream.print(method.returnType().fullName(method.typeVariables()) + " ");








//Synthetic comment -- diff --git a/tools/droiddoc/src/TypeInfo.java b/tools/droiddoc/src/TypeInfo.java
//Synthetic comment -- index 45e9db9..8113796 100644

//Synthetic comment -- @@ -62,7 +62,7 @@
if (mFullName != null) {
return mFullName;
} else {
            return fullName(new HashSet());
}
}

//Synthetic comment -- @@ -218,7 +218,7 @@

static HashSet<String> typeVariables(TypeInfo[] params)
{
        return typeVariables(params, new HashSet());
}

static HashSet<String> typeVariables(TypeInfo[] params, HashSet<String> result)







