//<Beginning of snippet n. 0>

public String number;           // often filtered from 800-892-1212 to 8008921212
public String nameAndNumber;    // Fred Flintstone <670-782-1123>
public boolean bcc;
private List<Recipient> recipients = new ArrayList<>(); // List to store recipients

@Override
public String toString() {
    return nameAndNumber + " " + number;
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

String bcc = getAnnotation(a, "bcc");
String number = getAnnotation(a, "number");
String name = getAnnotation(a, "name");
Recipient r = new Recipient();

r.name = name;
r.label = label;
r.bcc = bcc.equals("true");
r.number = TextUtils.isEmpty(number) ? TextUtils.substring(sp, start, end) : number;

// Check for duplicates before adding the recipient
boolean isDuplicate = recipients.stream().anyMatch(existingRecipient ->
    existingRecipient.name.equals(r.name) && existingRecipient.number.equals(r.number));

if (!isDuplicate) {
    if (TextUtils.isEmpty(r.name) && Mms.isEmailAddress(r.number)) {
        ContactInfoCache cache = ContactInfoCache.getInstance();
        r = getRecipientAt(sp, start, i, mContext);
        recipients.add(r);
        // Logging instance of addition
        Log.d("RecipientAddition", "Added recipient: " + r.toString());
    }
}

i++;

//<End of snippet n. 1>