/*Remove all never used imports

Change-Id:I2e45f5ba1d56b9b17314a8b0f4c0fc1a5d8fc4d9Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/mms/MmsApp.java b/src/com/android/mms/MmsApp.java
//Synthetic comment -- index ddb43b0..148fa4c 100644

//Synthetic comment -- @@ -17,32 +17,25 @@

package com.android.mms;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.location.Country;
import android.location.CountryDetector;
import android.location.CountryListener;
import android.preference.PreferenceManager;
import android.provider.SearchRecentSuggestions;
import android.telephony.TelephonyManager;

import com.android.mms.data.Contact;
import com.android.mms.data.Conversation;
import com.android.mms.drm.DrmUtils;
import com.android.mms.layout.LayoutManager;
import com.android.mms.transaction.MessagingNotification;
import com.android.mms.util.DownloadManager;
import com.android.mms.util.DraftCache;
import com.android.mms.util.RateController;
import com.android.mms.util.SmileyParser;

public class MmsApp extends Application {
public static final String LOG_TAG = "Mms";








//Synthetic comment -- diff --git a/src/com/android/mms/SuggestionsProvider.java b/src/com/android/mms/SuggestionsProvider.java
//Synthetic comment -- index 375851b..358ac06 100644

//Synthetic comment -- @@ -17,7 +17,6 @@
package com.android.mms;

import java.util.ArrayList;

import android.app.SearchManager;
import android.content.ContentResolver;








//Synthetic comment -- diff --git a/src/com/android/mms/data/Contact.java b/src/com/android/mms/data/Contact.java
//Synthetic comment -- index 01d13e5..a58ea9a 100644

//Synthetic comment -- @@ -13,6 +13,7 @@
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
//Synthetic comment -- @@ -20,22 +21,21 @@
import android.net.Uri;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.Presence;
import android.provider.ContactsContract.Profile;
import android.provider.Telephony.Mms;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;

import com.android.mms.LogTag;
import com.android.mms.MmsApp;
import com.android.mms.R;
import com.android.mms.ui.MessageUtils;

public class Contact {
public static final int CONTACT_METHOD_TYPE_UNKNOWN = 0;








//Synthetic comment -- diff --git a/src/com/android/mms/data/ContactList.java b/src/com/android/mms/data/ContactList.java
//Synthetic comment -- index 90a793f..01c2598 100644

//Synthetic comment -- @@ -8,7 +8,6 @@
import android.text.TextUtils;
import android.util.Log;

import com.android.mms.LogTag;
import com.android.mms.ui.MessageUtils;









//Synthetic comment -- diff --git a/src/com/android/mms/data/WorkingMessage.java b/src/com/android/mms/data/WorkingMessage.java
//Synthetic comment -- index c44b4ca..af01f04 100755

//Synthetic comment -- @@ -31,9 +31,8 @@
import android.os.Bundle;
import android.provider.Telephony.Mms;
import android.provider.Telephony.MmsSms;
import android.provider.Telephony.MmsSms.PendingMessages;
import android.provider.Telephony.Sms;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
//Synthetic comment -- @@ -56,7 +55,6 @@
import com.android.mms.transaction.MessageSender;
import com.android.mms.transaction.MmsMessageSender;
import com.android.mms.transaction.SmsMessageSender;
import com.android.mms.ui.ComposeMessageActivity;
import com.android.mms.ui.MessageUtils;
import com.android.mms.ui.SlideshowEditor;








//Synthetic comment -- diff --git a/src/com/android/mms/layout/LayoutManager.java b/src/com/android/mms/layout/LayoutManager.java
//Synthetic comment -- index ac6fabf..cb41fc5 100644

//Synthetic comment -- @@ -20,8 +20,6 @@
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

/**
* MMS presentation layout management.








//Synthetic comment -- diff --git a/src/com/android/mms/model/ImageModel.java b/src/com/android/mms/model/ImageModel.java
//Synthetic comment -- index 0e6fa48..c9ee9a0 100644

//Synthetic comment -- @@ -17,31 +17,6 @@

package com.android.mms.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
//Synthetic comment -- @@ -50,6 +25,29 @@
import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.events.Event;
import org.w3c.dom.smil.ElementTime;

import android.content.Context;
import android.drm.mobile1.DrmException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.android.mms.ContentRestrictionException;
import com.android.mms.ExceedMessageSizeException;
import com.android.mms.LogTag;
import com.android.mms.MmsConfig;
import com.android.mms.dom.smil.SmilMediaElementImpl;
import com.android.mms.drm.DrmWrapper;
import com.android.mms.ui.MessageUtils;
import com.android.mms.ui.UriImage;
import com.google.android.mms.MmsException;
import com.google.android.mms.pdu.PduPart;
import com.google.android.mms.pdu.PduPersister;


public class ImageModel extends RegionMediaModel {
@SuppressWarnings("hiding")








//Synthetic comment -- diff --git a/src/com/android/mms/model/MediaModelFactory.java b/src/com/android/mms/model/MediaModelFactory.java
//Synthetic comment -- index bd0ff63..94682f9 100644

//Synthetic comment -- @@ -17,16 +17,7 @@

package com.android.mms.model;

import java.io.IOException;

import org.w3c.dom.smil.SMILMediaElement;
import org.w3c.dom.smil.SMILRegionElement;
//Synthetic comment -- @@ -35,9 +26,17 @@
import org.w3c.dom.smil.TimeList;

import android.content.Context;
import android.drm.mobile1.DrmException;
import android.util.Log;

import com.android.mms.LogTag;
import com.android.mms.MmsConfig;
import com.android.mms.drm.DrmWrapper;
import com.google.android.mms.ContentType;
import com.google.android.mms.MmsException;
import com.google.android.mms.pdu.CharacterSets;
import com.google.android.mms.pdu.PduBody;
import com.google.android.mms.pdu.PduPart;

public class MediaModelFactory {
private static final String TAG = "Mms:media";








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/HttpUtils.java b/src/com/android/mms/transaction/HttpUtils.java
//Synthetic comment -- index 8726238..8d17e24 100644

//Synthetic comment -- @@ -17,6 +17,13 @@

package com.android.mms.transaction;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
//Synthetic comment -- @@ -25,29 +32,18 @@
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.mms.LogTag;
import com.android.mms.MmsConfig;

public class HttpUtils {
private static final String TAG = LogTag.TRANSACTION;








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/MessagingNotification.java b/src/com/android/mms/transaction/MessagingNotification.java
//Synthetic comment -- index 0ed2466..08cb305 100644

//Synthetic comment -- @@ -20,37 +20,28 @@
import static com.google.android.mms.pdu.PduHeaders.MESSAGE_TYPE_NOTIFICATION_IND;
import static com.google.android.mms.pdu.PduHeaders.MESSAGE_TYPE_RETRIEVE_CONF;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Telephony.Mms;
import android.provider.Telephony.Sms;
import android.text.Spannable;
//Synthetic comment -- @@ -60,11 +51,18 @@
import android.util.Log;
import android.widget.Toast;

import com.android.mms.LogTag;
import com.android.mms.R;
import com.android.mms.data.Contact;
import com.android.mms.data.Conversation;
import com.android.mms.ui.ComposeMessageActivity;
import com.android.mms.ui.ConversationList;
import com.android.mms.ui.MessagingPreferenceActivity;
import com.android.mms.util.AddressUtils;
import com.android.mms.util.DownloadManager;
import com.google.android.mms.pdu.EncodedStringValue;
import com.google.android.mms.pdu.PduHeaders;
import com.google.android.mms.pdu.PduPersister;

/**
* This class is used to update the notification indicator. It will check whether








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/NotificationTransaction.java b/src/com/android/mms/transaction/NotificationTransaction.java
//Synthetic comment -- index 20bf087..b913474 100644

//Synthetic comment -- @@ -25,6 +25,17 @@
import static com.google.android.mms.pdu.PduHeaders.STATUS_RETRIEVED;
import static com.google.android.mms.pdu.PduHeaders.STATUS_UNRECOGNIZED;

import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
import android.provider.Telephony.Mms;
import android.provider.Telephony.Mms.Inbox;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.mms.MmsApp;
import com.android.mms.MmsConfig;
import com.android.mms.util.DownloadManager;
//Synthetic comment -- @@ -37,18 +48,6 @@
import com.google.android.mms.pdu.PduHeaders;
import com.google.android.mms.pdu.PduParser;
import com.google.android.mms.pdu.PduPersister;

/**
* The NotificationTransaction is responsible for handling multimedia








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/PrivilegedSmsReceiver.java b/src/com/android/mms/transaction/PrivilegedSmsReceiver.java
//Synthetic comment -- index c2d0dca..f1e21a8 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.mms.transaction;

import android.content.Context;
import android.content.Intent;









//Synthetic comment -- diff --git a/src/com/android/mms/transaction/RetrieveTransaction.java b/src/com/android/mms/transaction/RetrieveTransaction.java
//Synthetic comment -- index a35c775..d40539b 100644

//Synthetic comment -- @@ -17,6 +17,18 @@

package com.android.mms.transaction;

import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
import android.provider.Telephony.Mms;
import android.provider.Telephony.Mms.Inbox;
import android.text.TextUtils;
import android.util.Log;

import com.android.mms.MmsConfig;
import com.android.mms.ui.MessageUtils;
import com.android.mms.util.DownloadManager;
//Synthetic comment -- @@ -29,19 +41,6 @@
import com.google.android.mms.pdu.PduParser;
import com.google.android.mms.pdu.PduPersister;
import com.google.android.mms.pdu.RetrieveConf;

/**
* The RetrieveTransaction is responsible for retrieving multimedia








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/RetryScheduler.java b/src/com/android/mms/transaction/RetryScheduler.java
//Synthetic comment -- index 5c5cc5c..4788a58 100644

//Synthetic comment -- @@ -17,13 +17,6 @@

package com.android.mms.transaction;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
//Synthetic comment -- @@ -32,16 +25,21 @@
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Telephony.Mms;
import android.provider.Telephony.MmsSms;
import android.provider.Telephony.MmsSms.PendingMessages;
import android.util.Log;

import com.android.mms.LogTag;
import com.android.mms.R;
import com.android.mms.util.DownloadManager;
import com.google.android.mms.pdu.PduHeaders;
import com.google.android.mms.pdu.PduPersister;

public class RetryScheduler implements Observer {
private static final String TAG = "RetryScheduler";
private static final boolean DEBUG = false;








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/SendTransaction.java b/src/com/android/mms/transaction/SendTransaction.java
//Synthetic comment -- index 3d6207c..3a4b97e 100644

//Synthetic comment -- @@ -17,31 +17,29 @@

package com.android.mms.transaction;

import java.util.Arrays;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
import android.provider.Telephony.Mms;
import android.provider.Telephony.Mms.Sent;
import android.text.TextUtils;
import android.util.Log;

import com.android.mms.LogTag;
import com.android.mms.ui.MessageUtils;
import com.android.mms.util.RateController;
import com.android.mms.util.SendingProgressTokenManager;
import com.google.android.mms.pdu.EncodedStringValue;
import com.google.android.mms.pdu.PduComposer;
import com.google.android.mms.pdu.PduHeaders;
import com.google.android.mms.pdu.PduParser;
import com.google.android.mms.pdu.PduPersister;
import com.google.android.mms.pdu.SendConf;
import com.google.android.mms.pdu.SendReq;

/**
* The SendTransaction is responsible for sending multimedia messages








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/SmsReceiver.java b/src/com/android/mms/transaction/SmsReceiver.java
//Synthetic comment -- index 8c70dff..22eb4e6 100644

//Synthetic comment -- @@ -19,11 +19,10 @@

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.provider.Telephony.Sms.Intents;

/**
* Handle incoming SMSes.  Just dispatches the work off to a Service.








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/Transaction.java b/src/com/android/mms/transaction/Transaction.java
//Synthetic comment -- index f3225e3..adc1722 100644

//Synthetic comment -- @@ -17,18 +17,17 @@

package com.android.mms.transaction;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;

import com.android.mms.util.SendingProgressTokenManager;
import com.google.android.mms.MmsException;

/**
* Transaction is an abstract class for notification transaction, send transaction
* and other transactions described in MMS spec.








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/TransactionService.java b/src/com/android/mms/transaction/TransactionService.java
//Synthetic comment -- index 9470c53..a0ea9ae 100644

//Synthetic comment -- @@ -17,20 +17,8 @@

package com.android.mms.transaction;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Service;
import android.content.BroadcastReceiver;
//Synthetic comment -- @@ -48,12 +36,22 @@
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Telephony.Mms;
import android.provider.Telephony.MmsSms;
import android.provider.Telephony.MmsSms.PendingMessages;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.telephony.Phone;
import com.android.mms.LogTag;
import com.android.mms.R;
import com.android.mms.util.RateController;
import com.google.android.mms.pdu.GenericPdu;
import com.google.android.mms.pdu.NotificationInd;
import com.google.android.mms.pdu.PduHeaders;
import com.google.android.mms.pdu.PduParser;
import com.google.android.mms.pdu.PduPersister;

/**
* The TransactionService of the MMS Client is responsible for handling requests








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/TransactionSettings.java b/src/com/android/mms/transaction/TransactionSettings.java
//Synthetic comment -- index ba37d19..8f91fd1 100644

//Synthetic comment -- @@ -17,19 +17,17 @@

package com.android.mms.transaction;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.net.NetworkUtils;
import android.provider.Telephony;
import android.text.TextUtils;
import android.util.Log;

import com.android.internal.telephony.Phone;
import com.android.mms.LogTag;

/**
* Container of transaction settings. Instances of this class are contained
* within Transaction instances to allow overriding of the default APN








//Synthetic comment -- diff --git a/src/com/android/mms/ui/AttachmentEditor.java b/src/com/android/mms/ui/AttachmentEditor.java
//Synthetic comment -- index b2f3ece..64a9ae4 100644

//Synthetic comment -- @@ -17,23 +17,22 @@

package com.android.mms.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.android.mms.R;
import com.android.mms.data.WorkingMessage;
import com.android.mms.model.SlideModel;
import com.android.mms.model.SlideshowModel;

/**
* This is an embedded editor/view to add photos and sound/video clips
* into a multimedia message.








//Synthetic comment -- diff --git a/src/com/android/mms/ui/BasicSlideEditorView.java b/src/com/android/mms/ui/BasicSlideEditorView.java
//Synthetic comment -- index c5676c7..2ec2d13 100644

//Synthetic comment -- @@ -17,12 +17,11 @@

package com.android.mms.ui;

import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
//Synthetic comment -- @@ -34,8 +33,7 @@
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.mms.R;

/**
* This is a basic view to show and edit a slide.








//Synthetic comment -- diff --git a/src/com/android/mms/ui/ClassZeroActivity.java b/src/com/android/mms/ui/ClassZeroActivity.java
//Synthetic comment -- index 3cf0737..1e3dd83 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
//Synthetic comment -- @@ -38,11 +39,8 @@
import android.view.Window;

import com.android.mms.R;
import com.android.mms.transaction.MessagingNotification;

/**
* Display a class-zero SMS message to the user. Wait for the user to dismiss
* it.








//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index c482ef2..6c40348 100644

//Synthetic comment -- @@ -53,9 +53,9 @@
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
//Synthetic comment -- @@ -75,21 +75,22 @@
import android.os.SystemProperties;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Intents;
import android.provider.DrmStore;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;
import android.provider.Settings;
import android.provider.Telephony.Mms;
import android.provider.Telephony.Sms;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
//Synthetic comment -- @@ -99,26 +100,24 @@
import android.text.util.Linkify;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnKeyListener;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

//Synthetic comment -- @@ -134,14 +133,6 @@
import com.android.mms.data.Conversation;
import com.android.mms.data.WorkingMessage;
import com.android.mms.data.WorkingMessage.MessageStatusListener;
import com.android.mms.model.SlideModel;
import com.android.mms.model.SlideshowModel;
import com.android.mms.transaction.MessagingNotification;
//Synthetic comment -- @@ -149,8 +140,14 @@
import com.android.mms.ui.RecipientsEditor.RecipientContextMenuInfo;
import com.android.mms.util.SendingProgressTokenManager;
import com.android.mms.util.SmileyParser;
import com.google.android.mms.ContentType;
import com.google.android.mms.MmsException;
import com.google.android.mms.pdu.EncodedStringValue;
import com.google.android.mms.pdu.PduBody;
import com.google.android.mms.pdu.PduPart;
import com.google.android.mms.pdu.PduPersister;
import com.google.android.mms.pdu.SendReq;
import com.google.android.mms.util.PduCache;

/**
* This is the main UI for:








//Synthetic comment -- diff --git a/src/com/android/mms/ui/ConversationList.java b/src/com/android/mms/ui/ConversationList.java
//Synthetic comment -- index 80fc421..a1e2877 100644

//Synthetic comment -- @@ -21,6 +21,50 @@
import java.util.Collection;
import java.util.HashSet;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SqliteWrapper;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.Telephony.Mms;
import android.provider.Telephony.Threads;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.mms.LogTag;
import com.android.mms.R;
import com.android.mms.data.Contact;
//Synthetic comment -- @@ -31,52 +75,6 @@
import com.android.mms.util.DraftCache;
import com.android.mms.util.Recycler;
import com.google.android.mms.pdu.PduHeaders;

/**
* This activity provides a list view of existing conversations.








//Synthetic comment -- diff --git a/src/com/android/mms/ui/ConversationListAdapter.java b/src/com/android/mms/ui/ConversationListAdapter.java
//Synthetic comment -- index a6492f8..40d0eaf 100644

//Synthetic comment -- @@ -17,10 +17,6 @@

package com.android.mms.ui;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
//Synthetic comment -- @@ -30,6 +26,9 @@
import android.widget.AbsListView;
import android.widget.CursorAdapter;

import com.android.mms.R;
import com.android.mms.data.Conversation;

/**
* The back-end data adapter for ConversationList.
*/








//Synthetic comment -- diff --git a/src/com/android/mms/ui/EditSlideDurationActivity.java b/src/com/android/mms/ui/EditSlideDurationActivity.java
//Synthetic comment -- index ee317ff..222875c 100644

//Synthetic comment -- @@ -20,15 +20,11 @@
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;








//Synthetic comment -- diff --git a/src/com/android/mms/ui/MiniPreferenceActivity.java b/src/com/android/mms/ui/MiniPreferenceActivity.java
//Synthetic comment -- index a78c5e4..e3e071c 100644

//Synthetic comment -- @@ -5,15 +5,12 @@

package com.android.mms.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.android.mms.R;

/**
* This activity is used by 3rd party apps to allow the user to turn on/off notifications in








//Synthetic comment -- diff --git a/src/com/android/mms/ui/NumberPickerDialog.java b/src/com/android/mms/ui/NumberPickerDialog.java
//Synthetic comment -- index ed2012d..22118d2 100644

//Synthetic comment -- @@ -21,7 +21,6 @@
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;








//Synthetic comment -- diff --git a/src/com/android/mms/ui/SlideEditorActivity.java b/src/com/android/mms/ui/SlideEditorActivity.java
//Synthetic comment -- index 78692c8..555e401 100644

//Synthetic comment -- @@ -17,28 +17,6 @@

package com.android.mms.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
//Synthetic comment -- @@ -52,7 +30,8 @@
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
//Synthetic comment -- @@ -61,11 +40,28 @@
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.mms.ExceedMessageSizeException;
import com.android.mms.MmsConfig;
import com.android.mms.R;
import com.android.mms.ResolutionException;
import com.android.mms.TempFileProvider;
import com.android.mms.UnsupportContentTypeException;
import com.android.mms.model.IModelChangedObserver;
import com.android.mms.model.LayoutModel;
import com.android.mms.model.Model;
import com.android.mms.model.SlideModel;
import com.android.mms.model.SlideshowModel;
import com.android.mms.ui.BasicSlideEditorView.OnTextChangedListener;
import com.android.mms.ui.MessageUtils.ResizeImageResultCallback;
import com.google.android.mms.ContentType;
import com.google.android.mms.MmsException;
import com.google.android.mms.pdu.PduBody;
import com.google.android.mms.pdu.PduPart;
import com.google.android.mms.pdu.PduPersister;

/**
* This activity allows user to edit the contents of a slide.








//Synthetic comment -- diff --git a/src/com/android/mms/ui/SlideView.java b/src/com/android/mms/ui/SlideView.java
//Synthetic comment -- index 5b36b3c..04e24b7 100644

//Synthetic comment -- @@ -17,8 +17,10 @@

package com.android.mms.ui;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import android.content.Context;
import android.graphics.Bitmap;
//Synthetic comment -- @@ -40,10 +42,8 @@
import android.widget.TextView;
import android.widget.VideoView;

import com.android.mms.R;
import com.android.mms.layout.LayoutManager;

/**
* A basic view to show the contents of a slide.








//Synthetic comment -- diff --git a/src/com/android/mms/ui/VideoAttachmentView.java b/src/com/android/mms/ui/VideoAttachmentView.java
//Synthetic comment -- index e3a0498..49a4e17 100644

//Synthetic comment -- @@ -17,20 +17,19 @@

package com.android.mms.ui;

import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.mms.R;

/**
* This class provides an embedded editor/viewer of video attachment.








//Synthetic comment -- diff --git a/src/com/android/mms/util/AddressUtils.java b/src/com/android/mms/util/AddressUtils.java
//Synthetic comment -- index cfb6ddf..4b8823d 100644

//Synthetic comment -- @@ -16,19 +16,18 @@
*/
package com.android.mms.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
import android.provider.Telephony.Mms;
import android.provider.Telephony.Mms.Addr;
import android.text.TextUtils;

import com.android.mms.R;
import com.google.android.mms.pdu.EncodedStringValue;
import com.google.android.mms.pdu.PduHeaders;
import com.google.android.mms.pdu.PduPersister;

public class AddressUtils {
private static final String TAG = "AddressUtils";







