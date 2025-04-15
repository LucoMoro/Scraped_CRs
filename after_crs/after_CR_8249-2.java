/*Remove old (unused) download manager APIs*/




//Synthetic comment -- diff --git a/core/java/android/provider/Downloads.java b/core/java/android/provider/Downloads.java
//Synthetic comment -- index a59776a..9593595 100644

//Synthetic comment -- @@ -65,7 +65,6 @@
*/
public static final String ACTION_DOWNLOAD_COMPLETED =
"android.intent.action.DOWNLOAD_COMPLETED";

/**
* Broadcast Action: this is sent by the download manager to the app
//Synthetic comment -- @@ -79,7 +78,6 @@
*/
public static final String ACTION_NOTIFICATION_CLICKED =
"android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED";

/**
* The name of the column containing the URI of the data being downloaded.
//Synthetic comment -- @@ -87,7 +85,6 @@
* <P>Owner can Init/Read</P>
*/
public static final String COLUMN_URI = "uri";

/**
* The name of the column containing application-specific data.
//Synthetic comment -- @@ -95,7 +92,6 @@
* <P>Owner can Init/Read/Write</P>
*/
public static final String COLUMN_APP_DATA = "entity";

/**
* The name of the column containing the flags that indicates whether
//Synthetic comment -- @@ -109,7 +105,6 @@
* <P>Owner can Init</P>
*/
public static final String COLUMN_NO_INTEGRITY = "no_integrity";

/**
* The name of the column containing the filename that the initiating
//Synthetic comment -- @@ -119,7 +114,6 @@
* <P>Owner can Init</P>
*/
public static final String COLUMN_FILE_NAME_HINT = "hint";

/**
* The name of the column containing the filename where the downloaded data
//Synthetic comment -- @@ -135,7 +129,6 @@
* <P>Owner can Init/Read</P>
*/
public static final String COLUMN_MIME_TYPE = "mimetype";

/**
* The name of the column containing the flag that controls the destination
//Synthetic comment -- @@ -144,7 +137,6 @@
* <P>Owner can Init</P>
*/
public static final String COLUMN_DESTINATION = "destination";

/**
* The name of the column containing the flags that controls whether the
//Synthetic comment -- @@ -154,7 +146,6 @@
* <P>Owner can Init/Read/Write</P>
*/
public static final String COLUMN_VISIBILITY = "visibility";

/**
* The name of the column containing the current control state  of the download.
//Synthetic comment -- @@ -164,7 +155,6 @@
* <P>Owner can Read</P>
*/
public static final String COLUMN_CONTROL = "control";

/**
* The name of the column containing the current status of the download.
//Synthetic comment -- @@ -174,7 +164,6 @@
* <P>Owner can Read</P>
*/
public static final String COLUMN_STATUS = "status";

/**
* The name of the column containing the date at which some interesting
//Synthetic comment -- @@ -184,7 +173,6 @@
* <P>Owner can Read</P>
*/
public static final String COLUMN_LAST_MODIFICATION = "lastmod";

/**
* The name of the column containing the package name of the application
//Synthetic comment -- @@ -194,7 +182,6 @@
* <P>Owner can Init/Read</P>
*/
public static final String COLUMN_NOTIFICATION_PACKAGE = "notificationpackage";

/**
* The name of the column containing the component name of the class that
//Synthetic comment -- @@ -205,7 +192,6 @@
* <P>Owner can Init/Read</P>
*/
public static final String COLUMN_NOTIFICATION_CLASS = "notificationclass";

/**
* If extras are specified when requesting a download they will be provided in the intent that
//Synthetic comment -- @@ -214,7 +200,6 @@
* <P>Owner can Init</P>
*/
public static final String COLUMN_NOTIFICATION_EXTRAS = "notificationextras";

/**
* The name of the column contain the values of the cookie to be used for
//Synthetic comment -- @@ -224,7 +209,6 @@
* <P>Owner can Init</P>
*/
public static final String COLUMN_COOKIE_DATA = "cookiedata";

/**
* The name of the column containing the user agent that the initiating
//Synthetic comment -- @@ -233,7 +217,6 @@
* <P>Owner can Init</P>
*/
public static final String COLUMN_USER_AGENT = "useragent";

/**
* The name of the column containing the referer (sic) that the initiating
//Synthetic comment -- @@ -242,7 +225,6 @@
* <P>Owner can Init</P>
*/
public static final String COLUMN_REFERER = "referer";

/**
* The name of the column containing the total size of the file being
//Synthetic comment -- @@ -251,7 +233,6 @@
* <P>Owner can Read</P>
*/
public static final String COLUMN_TOTAL_BYTES = "total_bytes";

/**
* The name of the column containing the size of the part of the file that
//Synthetic comment -- @@ -260,7 +241,6 @@
* <P>Owner can Read</P>
*/
public static final String COLUMN_CURRENT_BYTES = "current_bytes";

/**
* The name of the column where the initiating application can provide the
//Synthetic comment -- @@ -273,7 +253,6 @@
* <P>Owner can Init</P>
*/
public static final String COLUMN_OTHER_UID = "otheruid";

/**
* The name of the column where the initiating application can provided the
//Synthetic comment -- @@ -283,7 +262,6 @@
* <P>Owner can Init/Read/Write</P>
*/
public static final String COLUMN_TITLE = "title";

/**
* The name of the column where the initiating application can provide the
//Synthetic comment -- @@ -293,7 +271,6 @@
* <P>Owner can Init/Read/Write</P>
*/
public static final String COLUMN_DESCRIPTION = "description";

/*
* Lists the destinations that an application can specify for a download.







