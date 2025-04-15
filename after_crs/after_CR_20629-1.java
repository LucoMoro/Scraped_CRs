/*Add Korean Phone number formatting feature that inserts '-' in Korean phone numbers for easier reading.

Change-Id:I82aad1e5f6a917bbe0a434e4e12b845dc2f7ef2a*/




//Synthetic comment -- diff --git a/telephony/java/android/telephony/KoreanPhoneNumberFormatter.java b/telephony/java/android/telephony/KoreanPhoneNumberFormatter.java
new file mode 100644
//Synthetic comment -- index 0000000..0a63daf

//Synthetic comment -- @@ -0,0 +1,153 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.telephony;

import android.text.Editable;
// import android.util.Log;

/*
 * Korean Phone number formatting rule is a bit complicated.
 * 02-111-2222, 031-123-4567, 016-123-1234, 016-1234-5678, 010-1234-5678, ...
 */
class KoreanPhoneNumberFormatter {

	public static void insertDash(Editable edit, int where)
	{
		if (edit.length()>where) edit.insert(where, "-");
	}	

	public static boolean startsWith(Editable edit, String str)
	{
		int strlength = str.length();
		if (edit.length()>strlength)
		{
			if (edit.subSequence(0, strlength).toString().equals(str))
				return true;
			else
				return false;
		}
		return false;
	}	

	public static void format(Editable text) {
		// Here, "root" means the position of "'":
		// 0'3, 0'90, and +81'-90
		// (dash will be deleted soon, so it is actually +81'90).
		int rootIndex = 1;
		int length = text.length();
		if (length > 3
				&& text.subSequence(0, 3).toString().equals("+82")) {
			rootIndex = 3;
		} else if (length < 1 || text.charAt(0) != '0') {
			return;
		}

		// Strip the dashes first, as we're going to add them back
		int i = 0;
		while (i < text.length()) {
			if (text.charAt(i) == '-') {
				text.delete(i, i + 1);
			} else {
				i++;
			}
		}

		length = text.length();

		if (startsWith(text, "001") // KT - International Dialing
				|| startsWith(text, "002") // LG U+ - International Dialing
				|| startsWith(text, "005") // SK Broadband - International Dialing
				|| startsWith(text, "006") // SK Telelink - International Dialing
				|| startsWith(text, "008") // Onse Telecom - International Dialing
		)
		{
			insertDash(text, 3);
		} 
		else if (startsWith(text, "003") // Example) 00345 KT - International Dialing
				|| startsWith(text, "007") // Example) 00700 SK - International Dialing
		)
		{
			insertDash(text, 5);
		}
		else if (startsWith(text, "020") // Reserved
				|| startsWith(text, "030") // UMS
				|| startsWith(text, "040") // Reserved
				|| startsWith(text, "050") // 
				|| startsWith(text, "060")
				|| startsWith(text, "070") // VoIP
				|| startsWith(text, "080") 
				|| startsWith(text, "090") // Reserved
		)
		{
			if (length<11)
			{
				insertDash(text, 3);
				insertDash(text, 7);
			} else
			{
				insertDash(text, 3);
				insertDash(text, 8);
			}
		}
		else  if (startsWith(text, "02")) // Seoul
		{
			if (length<10)
			{
				insertDash(text, 2);
				insertDash(text, 6);
			} else
			{
				insertDash(text, 2);
				insertDash(text, 7);

			}
		}
		else if (startsWith(text, "03") // Kyoungkido, Inchon, Kangwondo
				|| startsWith(text, "04") // Chungchungnamdo, Daejeon, Chungchungbukdo
				|| startsWith(text, "05") // Pusan, Ulsan, Taego, Kyoungsangnamdo, Kyoungsangbukdo
				|| startsWith(text, "06") // Jeonla-namdo, Kwhangju, Jeonla-bukdo, Jeju
				|| startsWith(text, "07") // 070 VoIP, 07x reserved.
				|| startsWith(text, "08") // Long distance call
		)
		{
			if (length<11)
			{
				insertDash(text, 3);
				insertDash(text, 7);
			} else
			{
				insertDash(text, 3);
				insertDash(text, 8);
			}
		}
		else if (startsWith(text, "01")) // Mobile, Pager
		{
			// mobile
			if (length<11)
			{
				insertDash(text, 3);
				insertDash(text, 7);
			} else
			{
				insertDash(text, 3);
				insertDash(text, 8);
			}	
		}
	}

}









//Synthetic comment -- diff --git a/telephony/java/android/telephony/PhoneNumberUtils.java b/telephony/java/android/telephony/PhoneNumberUtils.java
//Synthetic comment -- index 8e4f6fc..be8d815 100644

//Synthetic comment -- @@ -1059,6 +1059,8 @@
public static final int FORMAT_NANP = 1;
/** Japanese formatting */
public static final int FORMAT_JAPAN = 2;
    /** Korean formatting */
    public static final int FORMAT_KOREA = 3;

/** List of country codes for countries that use the NANP */
private static final String[] NANP_COUNTRIES = new String[] {
//Synthetic comment -- @@ -1149,6 +1151,9 @@
} else if (text.length() >= 3 && text.charAt(1) == '8'
&& text.charAt(2) == '1') {
formatType = FORMAT_JAPAN;
            } else if (text.length() >= 3 && text.charAt(1) == '8'
					   && text.charAt(2) == '2') {
                formatType = FORMAT_KOREA;
} else {
formatType = FORMAT_UNKNOWN;
}
//Synthetic comment -- @@ -1161,6 +1166,9 @@
case FORMAT_JAPAN:
formatJapaneseNumber(text);
return;
            case FORMAT_KOREA:
                formatKoreanNumber(text);
                return;
case FORMAT_UNKNOWN:
removeDashes(text);
return;
//Synthetic comment -- @@ -1303,6 +1311,28 @@
JapanesePhoneNumberFormatter.format(text);
}

	/**
     * Formats a phone number in-place using the Korean formatting rules.
     * Numbers will be formatted as:
     *
     * <p><code>
     * 02-xxx-xxxx
     * 02-xxxx-xxxx
     * 031-xxx-xxxx
     * 016-xxx-xxxx
     * 016-xxx-xxxx
     * 016-xxxx-xxxx
     * +82-2-xxxx-xxxx
     * </code></p>
     *
     * @param text the number to be formatted, will be modified with
     * the formatting
     */
    public static void formatKoreanNumber(Editable text) {
        KoreanPhoneNumberFormatter.format(text);
    }
	
	
/**
* Removes all dashes from the number.
*
//Synthetic comment -- @@ -1646,6 +1676,9 @@
if ("jp".compareToIgnoreCase(country) == 0) {
return FORMAT_JAPAN;
}
        if ("kr".compareToIgnoreCase(country) == 0) {
            return FORMAT_KOREA;
        }
return FORMAT_UNKNOWN;
}








