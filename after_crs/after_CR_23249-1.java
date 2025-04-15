/*The Referred-By header is not well parsed int the NIST SIP stack

The contracted form "b" of the "Referred-By" SIP header is not parsed
by the NIST SIP stack (see RFC 3892)

Change-Id:I5e33d8bca71f4b01ed9aa7bad93cb20a665ab143*/




//Synthetic comment -- diff --git a/java/gov/nist/javax/sip/parser/Lexer.java b/java/gov/nist/javax/sip/parser/Lexer.java
//Synthetic comment -- index 628d858..f8a60bd 100644

//Synthetic comment -- @@ -220,11 +220,7 @@
TokenTypes.ALLOW_EVENTS); // JvB: added
addKeyword(TokenNames.V.toUpperCase(), TokenTypes.VIA);
addKeyword(TokenNames.R.toUpperCase(), TokenTypes.REFER_TO);
                    addKeyword(TokenNames.O.toUpperCase(), TokenTypes.EVENT); // Bug fix by Mario Mantak
addKeyword(TokenNames.X.toUpperCase(), TokenTypes.SESSIONEXPIRES_TO); // Bug fix by Jozef Saniga

// JvB: added to support RFC3903
//Synthetic comment -- @@ -238,8 +234,9 @@
TokenTypes.SESSIONEXPIRES_TO);
addKeyword(MinSEHeader.NAME.toUpperCase(),
TokenTypes.MINSE_TO);
                    addKeyword(ReferredByHeader.NAME.toUpperCase(), TokenTypes.REFERREDBY_TO);
                    addKeyword(TokenNames.B.toUpperCase(), TokenTypes.REFERREDBY_TO); // Bug fix OrangeLabs, AUFFRET Jean-Marc


// pmusgrave RFC3891
addKeyword(ReplacesHeader.NAME.toUpperCase(),








//Synthetic comment -- diff --git a/java/gov/nist/javax/sip/parser/ParserFactory.java b/java/gov/nist/javax/sip/parser/ParserFactory.java
//Synthetic comment -- index e38cbf9..9f2925d 100644

//Synthetic comment -- @@ -260,7 +260,7 @@

// Per RFC 3892 (pmusgrave)
parserTable.put(ReferredBy.NAME.toLowerCase(), ReferredByParser.class);
        parserTable.put("b", ReferredByParser.class); // Bug fix OrangeLabs, AUFFRET Jean-Marc

// Per RFC4028 Session Timers (pmusgrave)
parserTable.put(SessionExpires.NAME.toLowerCase(), SessionExpiresParser.class);








//Synthetic comment -- diff --git a/java/gov/nist/javax/sip/parser/TokenNames.java b/java/gov/nist/javax/sip/parser/TokenNames.java
//Synthetic comment -- index 938f231..f13923e 100644

//Synthetic comment -- @@ -89,6 +89,7 @@
public static final String R = "R";
public static final String O = "O";
public static final String X = "X"; //Jozef Saniga added
    public static final String B = "B"; // Bug fix OrangeLabs, AUFFRET Jean-Marc
}
/*
* $Log: TokenNames.java,v $







