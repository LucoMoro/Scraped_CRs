/*Introduce new names for the download manager API fields.

The new names will allow for better consistency and better API
documentation if/when they're made public in the SDK.

This will be followed by a later change that removes the old names*/
//Synthetic comment -- diff --git a/core/java/android/provider/Downloads.java b/core/java/android/provider/Downloads.java
//Synthetic comment -- index a5a30b9..a59776a 100644

//Synthetic comment -- @@ -63,8 +63,9 @@
* that had initiated a download when that download completes. The
* download's content: uri is specified in the intent's data.
*/
    public static final String DOWNLOAD_COMPLETED_ACTION =
"android.intent.action.DOWNLOAD_COMPLETED";

/**
* Broadcast Action: this is sent by the download manager to the app
//Synthetic comment -- @@ -76,22 +77,25 @@
* Note: this is not currently sent for downloads that have completed
* successfully.
*/
    public static final String NOTIFICATION_CLICKED_ACTION =
"android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED";

/**
* The name of the column containing the URI of the data being downloaded.
* <P>Type: TEXT</P>
* <P>Owner can Init/Read</P>
*/
    public static final String URI = "uri";

/**
* The name of the column containing application-specific data.
* <P>Type: TEXT</P>
* <P>Owner can Init/Read/Write</P>
*/
    public static final String APP_DATA = "entity";

/**
* The name of the column containing the flags that indicates whether
//Synthetic comment -- @@ -104,7 +108,8 @@
* <P>Type: BOOLEAN</P>
* <P>Owner can Init</P>
*/
    public static final String NO_INTEGRITY = "no_integrity";

/**
* The name of the column containing the filename that the initiating
//Synthetic comment -- @@ -113,7 +118,8 @@
* <P>Type: TEXT</P>
* <P>Owner can Init</P>
*/
    public static final String FILENAME_HINT = "hint";

/**
* The name of the column containing the filename where the downloaded data
//Synthetic comment -- @@ -128,7 +134,8 @@
* <P>Type: TEXT</P>
* <P>Owner can Init/Read</P>
*/
    public static final String MIMETYPE = "mimetype";

/**
* The name of the column containing the flag that controls the destination
//Synthetic comment -- @@ -136,7 +143,8 @@
* <P>Type: INTEGER</P>
* <P>Owner can Init</P>
*/
    public static final String DESTINATION = "destination";

/**
* The name of the column containing the flags that controls whether the
//Synthetic comment -- @@ -145,7 +153,8 @@
* <P>Type: INTEGER</P>
* <P>Owner can Init/Read/Write</P>
*/
    public static final String VISIBILITY = "visibility";

/**
* The name of the column containing the current control state  of the download.
//Synthetic comment -- @@ -154,7 +163,8 @@
* <P>Type: INTEGER</P>
* <P>Owner can Read</P>
*/
    public static final String CONTROL = "control";

/**
* The name of the column containing the current status of the download.
//Synthetic comment -- @@ -163,7 +173,8 @@
* <P>Type: INTEGER</P>
* <P>Owner can Read</P>
*/
    public static final String STATUS = "status";

/**
* The name of the column containing the date at which some interesting
//Synthetic comment -- @@ -172,7 +183,8 @@
* <P>Type: BIGINT</P>
* <P>Owner can Read</P>
*/
    public static final String LAST_MODIFICATION = "lastmod";

/**
* The name of the column containing the package name of the application
//Synthetic comment -- @@ -181,7 +193,8 @@
* <P>Type: TEXT</P>
* <P>Owner can Init/Read</P>
*/
    public static final String NOTIFICATION_PACKAGE = "notificationpackage";

/**
* The name of the column containing the component name of the class that
//Synthetic comment -- @@ -191,7 +204,8 @@
* <P>Type: TEXT</P>
* <P>Owner can Init/Read</P>
*/
    public static final String NOTIFICATION_CLASS = "notificationclass";

/**
* If extras are specified when requesting a download they will be provided in the intent that
//Synthetic comment -- @@ -199,7 +213,8 @@
* <P>Type: TEXT</P>
* <P>Owner can Init</P>
*/
    public static final String NOTIFICATION_EXTRAS = "notificationextras";

/**
* The name of the column contain the values of the cookie to be used for
//Synthetic comment -- @@ -208,7 +223,8 @@
* <P>Type: TEXT</P>
* <P>Owner can Init</P>
*/
    public static final String COOKIE_DATA = "cookiedata";

/**
* The name of the column containing the user agent that the initiating
//Synthetic comment -- @@ -216,7 +232,8 @@
* <P>Type: TEXT</P>
* <P>Owner can Init</P>
*/
    public static final String USER_AGENT = "useragent";

/**
* The name of the column containing the referer (sic) that the initiating
//Synthetic comment -- @@ -224,7 +241,8 @@
* <P>Type: TEXT</P>
* <P>Owner can Init</P>
*/
    public static final String REFERER = "referer";

/**
* The name of the column containing the total size of the file being
//Synthetic comment -- @@ -232,7 +250,8 @@
* <P>Type: INTEGER</P>
* <P>Owner can Read</P>
*/
    public static final String TOTAL_BYTES = "total_bytes";

/**
* The name of the column containing the size of the part of the file that
//Synthetic comment -- @@ -240,7 +259,8 @@
* <P>Type: INTEGER</P>
* <P>Owner can Read</P>
*/
    public static final String CURRENT_BYTES = "current_bytes";

/**
* The name of the column where the initiating application can provide the
//Synthetic comment -- @@ -252,7 +272,8 @@
* <P>Type: INTEGER</P>
* <P>Owner can Init</P>
*/
    public static final String OTHER_UID = "otheruid";

/**
* The name of the column where the initiating application can provided the
//Synthetic comment -- @@ -261,7 +282,8 @@
* <P>Type: TEXT</P>
* <P>Owner can Init/Read/Write</P>
*/
    public static final String TITLE = "title";

/**
* The name of the column where the initiating application can provide the
//Synthetic comment -- @@ -270,7 +292,8 @@
* <P>Type: TEXT</P>
* <P>Owner can Init/Read/Write</P>
*/
    public static final String DESCRIPTION = "description";

/*
* Lists the destinations that an application can specify for a download.







