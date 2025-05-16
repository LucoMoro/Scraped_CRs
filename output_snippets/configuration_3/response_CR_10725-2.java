//<Beginning of snippet n. 0>
public String number;           // often filtered from 800-892-1212 to 8008921212
public String nameAndNumber;    // Fred Flintstone <670-782-1123>
public boolean bcc;

@Override
public String toString() {
    return nameAndNumber + " (" + number + ")";
}
//<End of snippet n. 0>


//<Beginning of snippet n. 1>
String bcc = getAnnotation(a, "bcc");
String number = getAnnotation(a, "number");

Recipient newRecipient = new Recipient();

newRecipient.name = name;
newRecipient.label = label;
newRecipient.bcc = bcc.equals("true");
newRecipient.number = TextUtils.isEmpty(number) ? TextUtils.substring(sp, start, end) : number;

if (TextUtils.isEmpty(newRecipient.name) && Mms.isEmailAddress(newRecipient.number)) {
    ContactInfoCache cache = ContactInfoCache.getInstance();
    Recipient existingRecipient = getRecipientAt(sp, start, i, mContext);

    boolean exists = false;
    for (Recipient recipient : rl) {
        if (recipient.name.equals(newRecipient.name) && recipient.number.equals(newRecipient.number)) {
            exists = true;
            break;
        }
    }
    
    if (!exists) {
        rl.add(newRecipient);
    }
}

i++;
//<End of snippet n. 1>