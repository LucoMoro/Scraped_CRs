//<Beginning of snippet n. 0>
public String number;           // often filtered from 800-892-1212 to 8008921212
public String nameAndNumber;    // Fred Flintstone <670-782-1123>
public boolean bcc;

@Override
public String toString() {
    return String.format("%s <%s>", name, number);
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
String bcc = getAnnotation(a, "bcc");
String number = getAnnotation(a, "number");

// Normalize and trim input
String[] tokens = nameAndNumber.split(",");
Set<Recipient> recipientSet = new HashSet<>();

for (String token : tokens) {
    String[] parts = token.trim().split("<");
    String name = parts[0].trim();
    String num = parts.length > 1 ? parts[1].replace(">", "").trim() : "";

    Recipient r = new Recipient();
    r.name = name;
    r.label = label;
    r.bcc = bcc.equals("true");
    r.number = TextUtils.isEmpty(number) ? num : number;

    if (!TextUtils.isEmpty(r.name) && !recipientSet.contains(r)) {
        if (Mms.isEmailAddress(r.number) || !TextUtils.isEmpty(r.number)) {
            recipientSet.add(r);
        }
    }
}

rl.addAll(recipientSet); // Add all unique recipients at once
//<End of snippet n. 1>