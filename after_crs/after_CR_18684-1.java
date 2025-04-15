/*Fixes Issue 7907 in the public bugs database (http://code.google.com/p/android/issues/detail?id=7907).

The Javadoc comment for class `android.content.UriMatcher` had four issues:
1. The example calls to `addURI` should not be using a leading forward slash in
   the path parameter (reported by Ester Ytterbrink).
2. The sample code to construct a `UriMatcher` was incorrect because the
   `UriMatcher` constructor takes a parameter (reported by Ross Light).
3. The code example for using `match` was incorrect because it showed two
   parameters being passed, when `match` only takes one (reported by
   Ross Light).
4. The sample `getType` implementations were incorrect because `getType` takes
   a `Uri` object, not an array of `String`s.

Change-Id:I560bff6f021c13cabf736f40ff0f47a205074291*/




//Synthetic comment -- diff --git a/core/java/android/content/UriMatcher.java b/core/java/android/content/UriMatcher.java
//Synthetic comment -- index 72ec469..841c8f4 100644

//Synthetic comment -- @@ -25,8 +25,8 @@
/**
Utility class to aid in matching URIs in content providers.

<p>To use this class, build up a tree of <code>UriMatcher</code> objects.
For example:
<pre>
private static final int PEOPLE = 1;
private static final int PEOPLE_ID = 2;
//Synthetic comment -- @@ -48,36 +48,35 @@
private static final int CALLS_ID = 12;
private static final int CALLS_FILTER = 15;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

static
{
        sURIMatcher.addURI("contacts", "people", PEOPLE);
        sURIMatcher.addURI("contacts", "people/#", PEOPLE_ID);
        sURIMatcher.addURI("contacts", "people/#/phones", PEOPLE_PHONES);
        sURIMatcher.addURI("contacts", "people/#/phones/#", PEOPLE_PHONES_ID);
        sURIMatcher.addURI("contacts", "people/#/contact_methods", PEOPLE_CONTACTMETHODS);
        sURIMatcher.addURI("contacts", "people/#/contact_methods/#", PEOPLE_CONTACTMETHODS_ID);
        sURIMatcher.addURI("contacts", "deleted_people", DELETED_PEOPLE);
        sURIMatcher.addURI("contacts", "phones", PHONES);
        sURIMatcher.addURI("contacts", "phones/filter/*", PHONES_FILTER);
        sURIMatcher.addURI("contacts", "phones/#", PHONES_ID);
        sURIMatcher.addURI("contacts", "contact_methods", CONTACTMETHODS);
        sURIMatcher.addURI("contacts", "contact_methods/#", CONTACTMETHODS_ID);
        sURIMatcher.addURI("call_log", "calls", CALLS);
        sURIMatcher.addURI("call_log", "calls/filter/*", CALLS_FILTER);
        sURIMatcher.addURI("call_log", "calls/#", CALLS_ID);
}
</pre>
<p>Then when you need to match against a URI, call {@link #match}, providing
the URL that you have been given.  You can use the result to build a query,
return a type, insert or delete a row, or whatever you need, without duplicating
all of the if-else logic that you would otherwise need.  For example:
<pre>
    public String getType(Uri url)
{
        int match = sURIMatcher.match(url);
switch (match)
{
case PEOPLE:
//Synthetic comment -- @@ -93,19 +92,20 @@
}
}
</pre>
instead of:
<pre>
    public String getType(Uri url)
{
        List<String> pathSegments = url.getPathSegments();
        if (pathSegments.size() >= 2) {
            if ("people".equals(pathSegments.get(1))) {
                if (pathSegments.size() == 2) {
return "vnd.android.cursor.dir/person";
                } else if (pathSegments.size() == 3) {
return "vnd.android.cursor.item/person";
... snip ...
return "vnd.android.cursor.dir/snail-mail";
                } else if (pathSegments.size() == 3) {
return "vnd.android.cursor.item/snail-mail";
}
}







