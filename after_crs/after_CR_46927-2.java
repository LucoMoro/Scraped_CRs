/*Note the icu4c 49 upgrade.

(Yes, their version numbering changed. For CLDR too.)

(cherry-pick of dcb358b1be404d56462165e52ee944df9de03839.)

Bug: 7625281
Change-Id:Icfbbcd5382b315e47c2d06555a1475fadd97f62d*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Locale.java b/luni/src/main/java/java/util/Locale.java
//Synthetic comment -- index 0fbe2f5..51636a7 100644

//Synthetic comment -- @@ -66,11 +66,12 @@
* <p>Here are the versions of ICU (and the corresponding CLDR and Unicode versions) used in
* various Android releases:
* <table BORDER="1" WIDTH="100%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
 * <tr><td>cupcake/donut/eclair</td> <td>ICU 3.8</td> <td><a href="http://cldr.unicode.org/index/downloads/cldr-1-5">CLDR 1.5</a></td>  <td><a href="http://www.unicode.org/versions/Unicode5.0.0/">Unicode 5.0</a></td></tr>
 * <tr><td>froyo</td>                <td>ICU 4.2</td> <td><a href="http://cldr.unicode.org/index/downloads/cldr-1-7">CLDR 1.7</a></td>  <td><a href="http://www.unicode.org/versions/Unicode5.1.0/">Unicode 5.1</a></td></tr>
 * <tr><td>gingerbread/honeycomb</td><td>ICU 4.4</td> <td><a href="http://cldr.unicode.org/index/downloads/cldr-1-8">CLDR 1.8</a></td>  <td><a href="http://www.unicode.org/versions/Unicode5.2.0/">Unicode 5.2</a></td></tr>
 * <tr><td>ice cream sandwich</td>   <td>ICU 4.6</td> <td><a href="http://cldr.unicode.org/index/downloads/cldr-1-9">CLDR 1.9</a></td>  <td><a href="http://www.unicode.org/versions/Unicode6.0.0/">Unicode 6.0</a></td></tr>
 * <tr><td>jelly bean</td>           <td>ICU 4.8</td> <td><a href="http://cldr.unicode.org/index/downloads/cldr-2-0">CLDR 2.0</a></td>  <td><a href="http://www.unicode.org/versions/Unicode6.0.0/">Unicode 6.0</a></td></tr>
 * <tr><td>later</td>                <td>ICU 49</td>  <td><a href="http://cldr.unicode.org/index/downloads/cldr-21">CLDR 21.0</a></td> <td><a href="http://www.unicode.org/versions/Unicode6.1.0/">Unicode 6.1</a></td></tr>
* </table>
*
* <a name="default_locale"><h3>Be wary of the default locale</h3></a>







