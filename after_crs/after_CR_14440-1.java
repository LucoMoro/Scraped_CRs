/*Optimized Maps usage by adding explicit types and improved performance

Optimized the use of Maps for improved runtime performance, the
site object can be accessed directly.

Change-Id:Icfcd020541e806427c4ac7ba0817d30b2c7eb9a5*/




//Synthetic comment -- diff --git a/src/com/android/browser/WebsiteSettingsActivity.java b/src/com/android/browser/WebsiteSettingsActivity.java
//Synthetic comment -- index 047867a..a5270a4 100644

//Synthetic comment -- @@ -62,7 +62,7 @@
private static String sMBStored = null;
private SiteAdapter mAdapter = null;

    static class Site {
private String mOrigin;
private String mTitle;
private Bitmap mIcon;
//Synthetic comment -- @@ -186,7 +186,7 @@
* Adds the specified feature to the site corresponding to supplied
* origin in the map. Creates the site if it does not already exist.
*/
        private void addFeatureToSite(Map<String, Site> sites, String origin, int feature) {
Site site = null;
if (sites.containsKey(origin)) {
site = (Site) sites.get(origin);
//Synthetic comment -- @@ -209,7 +209,7 @@

WebStorage.getInstance().getOrigins(new ValueCallback<Map>() {
public void onReceiveValue(Map origins) {
                    Map<String, Site> sites = new HashMap<String, Site>();
if (origins != null) {
Iterator<String> iter = origins.keySet().iterator();
while (iter.hasNext()) {
//Synthetic comment -- @@ -221,7 +221,7 @@
});
}

        public void askForGeolocation(final Map<String, Site> sites) {
GeolocationPermissions.getInstance().getOrigins(new ValueCallback<Set<String> >() {
public void onReceiveValue(Set<String> origins) {
if (origins != null) {
//Synthetic comment -- @@ -236,19 +236,19 @@
});
}

        public void populateIcons(Map<String, Site> sites) {
// Create a map from host to origin. This is used to add metadata
// (title, icon) for this origin from the bookmarks DB.
            HashMap<String, Set<Site>> hosts = new HashMap<String, Set<Site>>();
            Set<Map.Entry<String, Site>> elements = sites.entrySet();
            Iterator<Map.Entry<String, Site>> originIter = elements.iterator();
while (originIter.hasNext()) {
                Map.Entry<String, Site> entry = originIter.next();
                Site site = entry.getValue();
                String host = Uri.parse(entry.getKey()).getHost();
                Set<Site> hostSites = null;
if (hosts.containsKey(host)) {
                    hostSites = (Set<Site>)hosts.get(host);
} else {
hostSites = new HashSet<Site>();
hosts.put(host, hostSites);
//Synthetic comment -- @@ -262,46 +262,47 @@
new String[] { Browser.BookmarkColumns.URL, Browser.BookmarkColumns.TITLE,
Browser.BookmarkColumns.FAVICON }, "bookmark = 1", null, null);

            if (c != null) {
                if(c.moveToFirst()) {
                    int urlIndex = c.getColumnIndex(Browser.BookmarkColumns.URL);
                    int titleIndex = c.getColumnIndex(Browser.BookmarkColumns.TITLE);
                    int faviconIndex = c.getColumnIndex(Browser.BookmarkColumns.FAVICON);
                    do {
                        String url = c.getString(urlIndex);
                        String host = Uri.parse(url).getHost();
                        if (hosts.containsKey(host)) {
                            String title = c.getString(titleIndex);
                            Bitmap bmp = null;
                            byte[] data = c.getBlob(faviconIndex);
                            if (data != null) {
                                bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                            }
                            Set matchingSites = (Set)hosts.get(host);
                            Iterator<Site> sitesIter = matchingSites.iterator();
                            while (sitesIter.hasNext()) {
                                Site site = sitesIter.next();
                                site.setTitle(title);
                                if (bmp != null) {
                                    site.setIcon(bmp);
                                }
}
}
                    } while (c.moveToNext());
                }
                c.close();
}
}


        public void populateOrigins(Map<String, Site> sites) {
clear();

// We can now simply populate our array with Site instances
            Set<Map.Entry<String, Site>> elements = sites.entrySet();
            Iterator<Map.Entry<String, Site>> entryIterator = elements.iterator();
            while (entryIterator.hasNext()) {
                Map.Entry<String, Site> entry = entryIterator.next();
                Site site = entry.getValue();
add(site);
}








