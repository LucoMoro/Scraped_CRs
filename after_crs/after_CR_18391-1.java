/*Code added to support speed dial

Change-Id:Iad11014b20fbca74355e92b039a53f5645486207*/




//Synthetic comment -- diff --git a/core/java/android/provider/ContactsContract.java b/core/java/android/provider/ContactsContract.java
//Synthetic comment -- index 40a408a..e649895 100644

//Synthetic comment -- @@ -2805,6 +2805,53 @@
public static final String DATA_ID = "data_id";
}

    //Code added for speed dial support starts
    /**
     * SpeedDial table columns
     */
    protected interface SpeedDialColumns {
        /**
        * The user assigned speed dial id
        * <P>Type: INT</P>
        */
        public static final String KEY_ID = "key_id";

         /**
         * The user assigned speed dial id
         * <P>Type: INTEGER</P>
         */
        public static final String PHONE_ID = "phone_id";

         /**
         * The type of phone number, for example Home or Work.
         * <P>Type: INTEGER</P>
         */
        public static final String PHONE_TYPE = "phone_type";

        /**
         * The phone number as the user entered it.
         * <P>Type: TEXT</P>
         */
        public static final String PHONE_NUMBER = "number";


    }

    /**
     * Speed dial table constants
     */
    public static final class SpeedDial implements SpeedDialColumns, ContactNameColumns{
    /**
     * This utility class cannot be instantiated
     */
        private SpeedDial() {
        }

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI,"speed_dial");
    }
    //Code added for speed dial support ends


/**
* @see PhoneLookup
*/
//Synthetic comment -- @@ -5710,6 +5757,68 @@
*/
public static final String FILTER_TEXT_EXTRA_KEY =
"com.android.contacts.extra.FILTER_TEXT";

            //Code added for speed dial support starts
            /**
             * Activity Action: Display a Speed dial grid
             * Output: Nothing.
             */
            public static final String SPEED_DIAL_GRID_ACTION =
                    "com.android.contacts.action.SPEED_DIAL_GRID";

            /**
             * Activity Action: Display a Speed dial grid
             * Output: Nothing.
             */
            public static final String SPEED_DIAL_LIST_ACTION =
                    "com.android.contacts.action.SPEED_DIAL_LIST";

            /**
             * Activity Action: Display a contact list to choose for speed dial assigining
             * Output: Nothing.
             */
            public static final String SPEED_DIAL_CONTACT_LIST_ACTION =
                    "com.android.contacts.action.SPEED_DIAL_CONTACT_LIST";

            /**
             * Activity Action: Display a list of phone numbers of a contact
             * to choose for speed dial assigining
             * Output: Nothing.
             */
            public static final String SPEED_DIAL_VIEW_CONTACT_ACTION =
                    "com.android.contacts.action.SPEED_DIAL_VIEW_CONTACT";

            /**
             * Activity Action: Display a list of phone numbers of a contact
             * to choose for speed dial assigining
             * Output: Nothing.
             */
            public static final String EDIT_SPEED_DIAL_VIEW_CONTACT_ACTION =
                    "com.android.contacts.action.EDIT_SPEED_DIAL_VIEW_CONTACT";

            /**
             * Used as an int extra field in {@link #SPEED_DIAL_GRID_ACTION,@link #SPEED_DIAL_LIST_ACTION}
             * intents to supply the text on Speed dial key.
             */
            public static final String SPEED_DIAL_KEY =
                    "com.android.contacts.extra.SPEED_DIAL_KEY";

             /**
             * Used as an int extra field in {@link #SPEED_DIAL_GRID_ACTION}
             * intents to supply the text on phone id to which Speed dial is assigned
             */
            public static final String SPEED_DIAL_PHONE_ID =
                    "com.android.contacts.extra.SPEED_DIAL_PHONE_ID";

             /**
             * Used as an int extra field in {@link #SPEED_DIAL_CONTACT_LIST_ACTION,
             * @link #SPEED_DIAL_CONTACT_LIST_ACTION, EDIT_SPEED_DIAL_VIEW_CONTACT_ACTION}
             * intents to supply the text on phone id to which Speed dial is assigned
             */
            public static final String SPEED_DIAL_CONTACT_ID =
                    "com.android.contacts.extra.CONTACT_ID";
            //Code added for speed dial support ends

}

/**







