/*Introduce new names for the download manager API fields.

The new names will allow for better consistency and better API
documentation if/when they're made public in the SDK.*/




//Synthetic comment -- diff --git a/core/java/android/provider/Downloads.java b/core/java/android/provider/Downloads.java
//Synthetic comment -- index a5a30b9..a59776a 100644

//Synthetic comment -- @@ -63,8 +63,9 @@
* that had initiated a download when that download completes. The
* download's content: uri is specified in the intent's data.
*/
    public static final String ACTION_DOWNLOAD_COMPLETED =
"android.intent.action.DOWNLOAD_COMPLETED";
    public static final String DOWNLOAD_COMPLETED_ACTION = ACTION_DOWNLOAD_COMPLETED;

/**
* Broadcast Action: this is sent by the download manager to the app
//Synthetic comment -- @@ -76,22 +77,25 @@
* Note: this is not currently sent for downloads that have completed
* successfully.
*/
    public static final String ACTION_NOTIFICATION_CLICKED =
"android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED";
    public static final String NOTIFICATION_CLICKED_ACTION = ACTION_NOTIFICATION_CLICKED;

/**
* The name of the column containing the URI of the data being downloaded.
* <P>Type: TEXT</P>
* <P>Owner can Init/Read</P>
*/
    public static final String COLUMN_URI = "uri";
    public static final String URI = COLUMN_URI;

/**
* The name of the column containing application-specific data.
* <P>Type: TEXT</P>
* <P>Owner can Init/Read/Write</P>
*/
    public static final String COLUMN_APP_DATA = "entity";
    public static final String APP_DATA = COLUMN_APP_DATA;

/**
* The name of the column containing the flags that indicates whether
//Synthetic comment -- @@ -104,7 +108,8 @@
* <P>Type: BOOLEAN</P>
* <P>Owner can Init</P>
*/
    public static final String COLUMN_NO_INTEGRITY = "no_integrity";
    public static final String NO_INTEGRITY = COLUMN_NO_INTEGRITY;

/**
* The name of the column containing the filename that the initiating
//Synthetic comment -- @@ -113,7 +118,8 @@
* <P>Type: TEXT</P>
* <P>Owner can Init</P>
*/
    public static final String COLUMN_FILE_NAME_HINT = "hint";
    public static final String FILENAME_HINT = COLUMN_FILE_NAME_HINT;

/**
* The name of the column containing the filename where the downloaded data
//Synthetic comment -- @@ -128,7 +134,8 @@
* <P>Type: TEXT</P>
* <P>Owner can Init/Read</P>
*/
    public static final String COLUMN_MIME_TYPE = "mimetype";
    public static final String MIMETYPE = COLUMN_MIME_TYPE;

/**
* The name of the column containing the flag that controls the destination
//Synthetic comment -- @@ -136,7 +143,8 @@
* <P>Type: INTEGER</P>
* <P>Owner can Init</P>
*/
    public static final String COLUMN_DESTINATION = "destination";
    public static final String DESTINATION = COLUMN_DESTINATION;

/**
* The name of the column containing the flags that controls whether the
//Synthetic comment -- @@ -145,7 +153,8 @@
* <P>Type: INTEGER</P>
* <P>Owner can Init/Read/Write</P>
*/
    public static final String COLUMN_VISIBILITY = "visibility";
    public static final String VISIBILITY = COLUMN_VISIBILITY;

/**
* The name of the column containing the current control state  of the download.
//Synthetic comment -- @@ -154,7 +163,8 @@
* <P>Type: INTEGER</P>
* <P>Owner can Read</P>
*/
    public static final String COLUMN_CONTROL = "control";
    public static final String CONTROL = COLUMN_CONTROL;

/**
* The name of the column containing the current status of the download.
//Synthetic comment -- @@ -163,7 +173,8 @@
* <P>Type: INTEGER</P>
* <P>Owner can Read</P>
*/
    public static final String COLUMN_STATUS = "status";
    public static final String STATUS = COLUMN_STATUS;

/**
* The name of the column containing the date at which some interesting
//Synthetic comment -- @@ -172,7 +183,8 @@
* <P>Type: BIGINT</P>
* <P>Owner can Read</P>
*/
    public static final String COLUMN_LAST_MODIFICATION = "lastmod";
    public static final String LAST_MODIFICATION = COLUMN_LAST_MODIFICATION;

/**
* The name of the column containing the package name of the application
//Synthetic comment -- @@ -181,7 +193,8 @@
* <P>Type: TEXT</P>
* <P>Owner can Init/Read</P>
*/
    public static final String COLUMN_NOTIFICATION_PACKAGE = "notificationpackage";
    public static final String NOTIFICATION_PACKAGE = COLUMN_NOTIFICATION_PACKAGE;

/**
* The name of the column containing the component name of the class that
//Synthetic comment -- @@ -191,7 +204,8 @@
* <P>Type: TEXT</P>
* <P>Owner can Init/Read</P>
*/
    public static final String COLUMN_NOTIFICATION_CLASS = "notificationclass";
    public static final String NOTIFICATION_CLASS = COLUMN_NOTIFICATION_CLASS;

/**
* If extras are specified when requesting a download they will be provided in the intent that
//Synthetic comment -- @@ -199,7 +213,8 @@
* <P>Type: TEXT</P>
* <P>Owner can Init</P>
*/
    public static final String COLUMN_NOTIFICATION_EXTRAS = "notificationextras";
    public static final String NOTIFICATION_EXTRAS = COLUMN_NOTIFICATION_EXTRAS;

/**
* The name of the column contain the values of the cookie to be used for
//Synthetic comment -- @@ -208,7 +223,8 @@
* <P>Type: TEXT</P>
* <P>Owner can Init</P>
*/
    public static final String COLUMN_COOKIE_DATA = "cookiedata";
    public static final String COOKIE_DATA = COLUMN_COOKIE_DATA;

/**
* The name of the column containing the user agent that the initiating
//Synthetic comment -- @@ -216,7 +232,8 @@
* <P>Type: TEXT</P>
* <P>Owner can Init</P>
*/
    public static final String COLUMN_USER_AGENT = "useragent";
    public static final String USER_AGENT = COLUMN_USER_AGENT;

/**
* The name of the column containing the referer (sic) that the initiating
//Synthetic comment -- @@ -224,7 +241,8 @@
* <P>Type: TEXT</P>
* <P>Owner can Init</P>
*/
    public static final String COLUMN_REFERER = "referer";
    public static final String REFERER = COLUMN_REFERER;

/**
* The name of the column containing the total size of the file being
//Synthetic comment -- @@ -232,7 +250,8 @@
* <P>Type: INTEGER</P>
* <P>Owner can Read</P>
*/
    public static final String COLUMN_TOTAL_BYTES = "total_bytes";
    public static final String TOTAL_BYTES = COLUMN_TOTAL_BYTES;

/**
* The name of the column containing the size of the part of the file that
//Synthetic comment -- @@ -240,7 +259,8 @@
* <P>Type: INTEGER</P>
* <P>Owner can Read</P>
*/
    public static final String COLUMN_CURRENT_BYTES = "current_bytes";
    public static final String CURRENT_BYTES = COLUMN_CURRENT_BYTES;

/**
* The name of the column where the initiating application can provide the
//Synthetic comment -- @@ -252,7 +272,8 @@
* <P>Type: INTEGER</P>
* <P>Owner can Init</P>
*/
    public static final String COLUMN_OTHER_UID = "otheruid";
    public static final String OTHER_UID = COLUMN_OTHER_UID;

/**
* The name of the column where the initiating application can provided the
//Synthetic comment -- @@ -261,7 +282,8 @@
* <P>Type: TEXT</P>
* <P>Owner can Init/Read/Write</P>
*/
    public static final String COLUMN_TITLE = "title";
    public static final String TITLE = COLUMN_TITLE;

/**
* The name of the column where the initiating application can provide the
//Synthetic comment -- @@ -270,7 +292,8 @@
* <P>Type: TEXT</P>
* <P>Owner can Init/Read/Write</P>
*/
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String DESCRIPTION = COLUMN_DESCRIPTION;

/*
* Lists the destinations that an application can specify for a download.







