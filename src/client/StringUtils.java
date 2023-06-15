package client;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.util.ExceptionUtils;

import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Utility class to peform common String manipulation algorithms.
 */
public class StringUtils {
	private final static Log log = LogFactory.getLog(StringUtils.class);

	public final static String PATERN_START = "${";

	public final static String PATERN_END = "}";

	public static final String LINE_DELIMETER = "|||";

	public static final String WORD_DELIMETER = ":::";

	public final static String HIDDEN_START = "[HIDDEN]";

	public final static String HIDDEN_END = "[/HIDDEN]";

	/**
	 * Replaces all instances of oldString with newString in line.
	 *
	 * @param line
	 *            the String to search to perform replacements on
	 * @param oldString
	 *            the String that should be replaced by newString
	 * @param newString
	 *            the String that will replace all instances of oldString
	 *
	 * @return a String will all instances of oldString replaced by newString
	 */
	public static final String replace(String line, String oldString,
			String newString) {
		if (line == null) {
			return null;
		}
        if (newString == null)
            return line;
		int i = 0;
		if ((i = line.indexOf(oldString, i)) >= 0) {
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = line.indexOf(oldString, i)) > 0) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			return buf.toString();
		}
		return line;
	}

	/**
	 * Replaces all instances of oldString with newString in line with the added
	 * feature that matches of newString in oldString ignore case.
	 *
	 * @param line
	 *            the String to search to perform replacements on
	 * @param oldString
	 *            the String that should be replaced by newString
	 * @param newString
	 *            the String that will replace all instances of oldString
	 *
	 * @return a String will all instances of oldString replaced by newString
	 */
	public static final String replaceIgnoreCase(String line, String oldString,
			String newString) {
		if (line == null) {
			return null;
		}
		String lcLine = line.toLowerCase();
		String lcOldString = oldString.toLowerCase();
		int i = 0;
		if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = lcLine.indexOf(lcOldString, i)) > 0) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			return buf.toString();
		}
		return line;
	}

	/**
	 * Replaces all instances of oldString with newString in line. The count
	 * Integer is updated with number of replaces.
	 *
	 * @param line
	 *            the String to search to perform replacements on
	 * @param oldString
	 *            the String that should be replaced by newString
	 * @param newString
	 *            the String that will replace all instances of oldString
	 *
	 * @return a String will all instances of oldString replaced by newString
	 */
	public static String replace(String line, String oldString,
			String newString, int[] count) {
		if (line == null) {
			return null;
		}
		int i = 0;
		if ((i = line.indexOf(oldString, i)) >= 0) {
			int counter = 0;
			counter++;
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = line.indexOf(oldString, i)) > 0) {
				counter++;
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			count[0] = counter;
			return buf.toString();
		}
		return line;
	}

	/**
	 * This method takes a string which may contain HTML tags (ie, &lt;b&gt;,
	 * &lt;table&gt;, etc) and converts the '&lt'' and '&gt;' characters to
	 * their HTML escape sequences.
	 *
	 * @param input
	 *            the text to be converted.
	 * @return the input string with the characters '&lt;' and '&gt;' replaced
	 *         with their HTML escape sequences.
	 */
	public static String escapeHTMLTags(String input) {
		// Check if the string is null or zero length -- if so, return
		// what was sent in.
		if (input == null || input.length() == 0) {
			return input;
		}
		// Use a StringBuffer in lieu of String concatenation -- it is
		// much more efficient this way.
		StringBuffer buf = new StringBuffer(input.length());
		char ch = ' ';
		for (int i = 0; i < input.length(); i++) {
			ch = input.charAt(i);
			if (ch == '<') {
				buf.append("&lt;");
			} else if (ch == '>') {
				buf.append("&gt;");
			} else {
				buf.append(ch);
			}
		}
		return buf.toString();
	}

	/**
	 * Used by the hash method.
	 */
	private static MessageDigest digest = null;

	/**
	 * Hashes a String using the Md5 algorithm and returns the result as a
	 * String of hexadecimal numbers. This method is synchronized to avoid
	 * excessive MessageDigest object creation. If calling this method becomes a
	 * bottleneck in your code, you may wish to maintain a pool of MessageDigest
	 * objects instead of using this method.
	 * <p>
	 * A hash is a one-way function -- that is, given an input, an output is
	 * easily computed. However, given the output, the input is almost
	 * impossible to compute. This is useful for passwords since we can store
	 * the hash and a hacker will then have a very hard time determining the
	 * original password.
	 * <p>
	 * In Jive, every time a user logs in, we simply take their plain text
	 * password, compute the hash, and compare the generated hash to the stored
	 * hash. Since it is almost impossible that two passwords will generate the
	 * same hash, we know if the user gave us the correct password or not. The
	 * only negative to this system is that password recovery is basically
	 * impossible. Therefore, a reset password method is used instead.
	 *
	 * @param data
	 *            the String to compute the hash of.
	 * @return a hashed version of the passed-in String
	 */
	public synchronized static String hash(String data) {
		if (digest == null) {
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException nsae) {
				log.error("Failed to load the MD5 MessageDigest. "
						+ "Jive will be unable to function normally.", nsae);
			}
		}
		// Now, compute hash.
		digest.update(data.getBytes());
		return toHex(digest.digest());
	}

	public static final String md5sum(String data) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.reset();
			digest.update(data.getBytes());

			String result = new String(Hex.encodeHex(digest.digest()));
			return result;

		} catch (NoSuchAlgorithmException nsae) {
			log.error("Failed to load the MD5 MessageDigest. "
					+ "Jive will be unable to function normally.", nsae);
		}
		return null;
	}

	/**
	 * Turns an array of bytes into a String representing each byte as an
	 * unsigned hex number.
	 * <p>
	 * Method by Santeri Paavolainen, Helsinki Finland 1996 <br>
	 * (c) Santeri Paavolainen, Helsinki Finland 1996 <br>
	 * Distributed under LGPL.
	 *
	 * @param hash
	 *            an rray of bytes to convert to a hex-string
	 * @return generated hex string
	 */
	public static final String toHex(byte hash[]) {
		StringBuffer buf = new StringBuffer(hash.length * 2);
		int i;

		for (i = 0; i < hash.length; i++) {
			if (((int) hash[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString((int) hash[i] & 0xff, 16));
		}
		return buf.toString();
	}

	private static final String AB = "0123456789abcdefghijklmnopqrstuvwxyz";
	private static SecureRandom rnd = new SecureRandom();

	public static String randomString(int length) {
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();
	}

	/**
	 * Escapes all necessary characters in the String so that it can be used in
	 * an XML doc.
	 *
	 * @param string
	 *            the string to escape.
	 * @return the string with appropriate characters escaped.
	 */
	public static String escapeForXML(String string) {
		// Check if the string is null or zero length -- if so, return
		// what was sent in.
		if (string == null || string.length() == 0) {
			return string;
		}
		char[] sArray = string.toCharArray();
		StringBuffer buf = new StringBuffer(sArray.length);
		char ch;
		for (int i = 0; i < sArray.length; i++) {
			ch = sArray[i];
			if (ch == '<') {
				buf.append("&lt;");
			} else if (ch == '&') {
				buf.append("&amp;");
			} else {
				buf.append(ch);
			}
		}
		return buf.toString();
	}

	public static String replaceParams(String parameterizedString,
			Object params[]) {
		StringBuffer buf = new StringBuffer(parameterizedString.length());
		int lastIndex = 0;
		int i = 0;
		for (int s = params.length; i < s; i++) {
			int index = parameterizedString.indexOf("@", lastIndex);
			if (index == -1)
				break;
			buf.append(parameterizedString.substring(lastIndex, index));
			buf.append(params[i].toString());
			lastIndex = index + 4;
		}

		buf.append(parameterizedString.substring(lastIndex));
		return buf.toString();
	}

	/**
	 * Insert the method's description here. Creation date: (2001-9-5 14:02:27)
	 *
	 * @return java.lang.String
	 * @param s
	 *            java.lang.String
	 */
	public static String converto2gb(String s) {
		String temp = null;
		try {
			temp = new String(s.getBytes("iso8859-1"), "gbk");
		} catch (java.io.UnsupportedEncodingException e) {
			log.error("Exception here", e);
		}
		return temp;
	}

	/**
	 * Insert the method's description here. Creation date: (2001-9-5 14:02:27)
	 *
	 * @return java.lang.String
	 * @param s
	 *            java.lang.String
	 */
	public static String converto2iso(String s) {
		String temp = null;
		try {
			temp = new String(s.getBytes("gbk"), "iso8859-1");
		} catch (java.io.UnsupportedEncodingException e) {
			log.error("Exception here", e);
		}
		return temp;
	}

	public static String convert(String s, String enconding) {
		String temp = null;
		try {
			temp = new String(s.getBytes(), enconding);
		} catch (java.io.UnsupportedEncodingException e) {
			log.error("Exception here", e);
		}
		return temp;
	}

	/**
	 *
	 * @return java.lang.String
	 * @param num
	 *            double
	 */
	public static String numToPercent(double num) {
		return formatNumer(num, "0.00");
	}

	/**
	 *
	 * @return java.lang.String
	 * @param num
	 *            double
	 */
	public static String formatNumer(double num, String pattern) {
		DecimalFormat nf = new DecimalFormat(pattern);
		return nf.format(num);
	}

	public static String formatScore(float score) {
		return formatNumer(score, "#.#");
	}

	public static String quote(String x) {
		if (x == null)
			return null;
		int c;
		for (int oldC = -1; (c = x.substring(oldC + 1).indexOf(38)) != -1; oldC = c) {
			c += oldC + 1;
			x = new String((new StringBuffer(x)).replace(c, c + 1, "&amp;"));
		}

		while ((c = x.indexOf(34)) != -1)
			x = new String((new StringBuffer(x)).replace(c, c + 1, "&quot;"));
		while ((c = x.indexOf(60)) != -1)
			x = new String((new StringBuffer(x)).replace(c, c + 1, "&lt;"));
		while ((c = x.indexOf(62)) != -1)
			x = new String((new StringBuffer(x)).replace(c, c + 1, "&gt;"));
		return x;
	}

	public static String generateConditions(String key, String condition,
			Collection values) {
		String s = "";

		for (Iterator iter = values.iterator(); iter.hasNext();) {

			s += " ( " + key + " = " + iter.next() + " ) " + condition;

		}
		if (s.endsWith(condition)) {
			s = s.substring(0, s.length() - condition.length());

		}
		return s;

	}

	public static String generateConditions(String key, String condition,
			long[] values) {
		String s = "";

		for (int i = 0; i < values.length; i++) {

			s += " ( " + key + " = " + values[i] + " ) " + condition;

		}
		if (s.endsWith(condition)) {
			s = s.substring(0, s.length() - condition.length());

		}
		return s;

	}

	public static String text2html(String input) {
		String a =replace(input, "\r\n", "<br>");
		a = replace(a,"\r","<br>");
		a = replace(a,"\n","<br>");
		return a;
	}


	public static String toString(long[] ids) {
		String s = "";
		for (int i = 0; i < ids.length; i++) {
			s += ids[i] + ",";
		}
		if (s.endsWith(","))
			s = s.substring(0, s.length() - 1);
		return s;
	}

	public static String toString(String[] strs, String deli) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strs.length; i++) {
			sb.append(strs[i]);
			if (i != strs.length - 1)
				sb.append(deli);
		}
		return sb.toString();
	}

	public static Long[] toArray(String s) {
		s = s.trim();
		if (s.endsWith(","))
			s = s.substring(0, s.length() - 1);
		StringTokenizer st = new StringTokenizer(s, ",");
		Long[] result = new Long[st.countTokens()];

		int i = 0;
		while (st.hasMoreTokens()) {
			String ll = st.nextToken();
			result[i++] = Long.valueOf(ll);

		}
		return result;
	}

	public static String[] split(String s, String delimiter) {
		if (s == null) {
			return new String[0];
		}
		if (delimiter == null) {
			return new String[] { s };
		}
		List<String> list = new ArrayList<String>();
		int start = 0;
		int idx = s.indexOf(delimiter);
		while (idx != -1) {
			list.add(s.substring(start, idx));
			start = idx + delimiter.length();
			idx = s.indexOf(delimiter, start);
		}
		if (start < s.length())
			list.add(s.substring(start));
		return list.toArray(new String[list.size()]);
	}

	public static String[] removeBlanks(String[] array) {
		List<String> list = new ArrayList<String>();
		for (String s : array) {
			if (!isBlank(s))
				list.add(s);
		}
		return list.toArray(new String[list.size()]);
	}

	public static String[] splitByToken(String s, String delimiter) {
		if (s == null) {
			return new String[0];
		}
		String[] result;
		if (delimiter == null || delimiter.length() == 0) {
			result = new String[1];
			result[0] = s;
			return result;
		}
		StringTokenizer st = new StringTokenizer(s, delimiter);
		Collection rr = new ArrayList();
		while (st.hasMoreTokens()) {
			String temp = st.nextToken();
			if (temp != null && temp.length() > 0)
				rr.add(temp);
		}
		result = new String[rr.size()];
		int i = 0;
		for (Iterator iter = rr.iterator(); iter.hasNext();) {
			result[i++] = iter.next().toString();
		}
		return result;
	}





	public static Collection PatternKeys(String str, String patternBegin,
			String patternEnd) {

		Collection list = new ArrayList();
		if (str == null)
			return list;
		int idx = str.indexOf(patternBegin);
		while (idx >= 0) {
			int lastIdx = str.indexOf(patternEnd, idx);
			if (lastIdx > idx) {
				String key = str
						.substring(idx + patternBegin.length(), lastIdx);
				if (!list.contains(key))
					list.add(key);
			}
		}
		return list;
	}

	public static String replaceFieldStr(String str, String paternBegin,
			String paternEnd, Object data) {
		int idx = str.indexOf(paternBegin);
		while (idx >= 0) {
			int lastIdx = str.indexOf(paternEnd, idx);
			if (lastIdx > idx) {
				String filed = str.substring(idx + paternBegin.length(),
						lastIdx);
				try {
					Object o = "";
					if (data instanceof String) {
						o = data;
					} else {
						o = ReflectUtils.getFieldValue(data, filed);
					}

					if (o == null)
						o = "";
					str = str.substring(0, idx) + o.toString()
							+ str.substring(lastIdx + paternEnd.length());
				} catch (Exception e) {
					log.warn("Error when replacing filed value", e);
					str = str.substring(0, idx) + filed
							+ str.substring(lastIdx + paternEnd.length());
				}
				idx = str.indexOf(paternBegin);
			} else {
				idx = -1;
			}
		}
		return str;
	}

	public static String replaceFieldStr(String str, String paternBegin,
			String paternEnd, Object data, Hashtable ht) {
		int idx = str.indexOf(paternBegin);
		while (idx >= 0) {
			int lastIdx = str.indexOf(paternEnd, idx);
			if (lastIdx > idx) {
				String field = str.substring(idx + paternBegin.length(),
						lastIdx);
				Object value = null;
				if (ht.containsKey(field)) {
					value = ht.get(field);
				} else {
					try {
						value = ReflectUtils.getFieldValue(data, field);
						if (value == null)
							value = "";
					} catch (Exception e) {
						value = field;
					}
				}
				str = str.substring(0, idx) + value.toString()
						+ str.substring(lastIdx + paternEnd.length());
				idx = str.indexOf(paternBegin);
			} else {
				idx = -1;
			}
		}
		return str;
	}

	public static boolean isTrue(String value) {
		if (!StringUtils.isValid(value))
			return false;
		value = value.trim();
        return value.equalsIgnoreCase("true")
                || value.equalsIgnoreCase("1")
                || value.equalsIgnoreCase("yes")
                || value.equalsIgnoreCase("on");

    }

	public static boolean isFalse(String value) {
		if (!StringUtils.isValid(value))
			return false;
		value = value.trim();
        return value.equalsIgnoreCase("false")
                || value.equalsIgnoreCase("0")
                || value.equalsIgnoreCase("no")
                || value.equalsIgnoreCase("off");
    }

	public static boolean isNotFalse(String value) {
		if (value == null)
			return true;
		value = value.trim();
        return !(value.equalsIgnoreCase("false") || value.equalsIgnoreCase("0")
                || value.equalsIgnoreCase("no"));
    }

	public static boolean isValid(String... strings) {
		for (int i = 0; i < strings.length; i++) {
			if (strings[i] == null || strings[i].trim().length() == 0)
				return false;
		}
		return true;
	}

	public static boolean isValid(String s1) {
		return (s1 != null && s1.trim().length() > 0);
	}

	public static boolean isValid(String s1, String s2) {
		return (isValid(s1) && isValid(s2));
	}

	public static boolean isValid(String s1, String s2, String s3) {
		return (isValid(s1) && isValid(s2) && isValid(s3));
	}

	public static String fomatStr(String str, String type) {
		if (type.equalsIgnoreCase("month")) {
			if (str.length() == 1)
				return "0" + str;
			else
				return str;
		}
		return "";
	}

	public static String nullToBlank(Object s) {
		if (s == null)
			return "";
		return s.toString().trim();
	}

	public static String toUpperCaseFirst(String s) {
		if (s == null || s.length() == 0)
			return "";
		if (s.length() == 1)
			return s.toUpperCase();
		else
			return s.substring(0, 1).toUpperCase()
					+ s.substring(1).toLowerCase();
	}

	public static String reOrderPersonTypeStr(String type) {
		String all = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String result = "";
		for (int i = 0; i < all.length(); i++) {
			char c = all.charAt(i);
			if (type.indexOf(c) > -1) {
				result += c;
			}
		}
		return result;
	}

	public static String getPaternValue(String text, String paternName) {
		if (text == null || paternName == null) {
			return null;
		}
		int idxBegin = text.toUpperCase().indexOf(
				PATERN_START + paternName.toUpperCase());
		int idxEnd = 0;
		if (idxBegin >= 0)
			idxEnd = text.indexOf(PATERN_END, idxBegin);

		if (idxBegin < 0 || idxEnd <= idxBegin) {
			return null;
		}
		String result = text.substring(
				idxBegin + (PATERN_START + paternName).length() + 1, idxEnd);
		return result;
	}

	public static String getPaternValue(String text, String paternName,
			String defaultValue) {
		String value = getPaternValue(text, paternName);
		return value == null ? defaultValue : value;
	}

	public static String getMailSuffix(String address) {
		if (isValid(address)) {
			int idx = address.indexOf("@");
			if (idx >= 0) {
				return address.substring(idx);
			}
		}
		return null;
	}

	public static String contactPath(String a, String b) {
		if (a == null)
			a = "";
		if (a.endsWith("/"))
			a = a.substring(0, a.length() - 1);
		if (b == null)
			b = "";
		if (b != null && b.startsWith("/"))
			b = b.substring(1);

		return a + "/" + b;
	}

	public static boolean isPassword(String password) {



		boolean isDiff = false;
		char firstC = password.charAt(0);
		for (int i = 1; i < password.length(); i++) {
			char c = password.charAt(i);
			if (c != firstC) {
				isDiff = true;
				break;
			}
		}
		return isDiff;
	}

	public static String toInputValueString(String s) {
		s = s.replace("<", "&lt;");
		s = s.replace(">", "&gt;");
		s = s.replace("\r\n", "");
		s = s.replace("\n", "");
		s = s.replace("\"", "&quot;");
		// s = s.replace("'", "\\'");
		return s;
	}


	public static String shorter(String s) {
		if (s == null || s.length() < 23)
			return s;
		else
			return s.substring(0, 20) + "...";
	}

	public static String shorter(String s, int toLength) {
		if (s == null || s.length() < toLength + 3)
			return s;
		else
			return s.substring(0, toLength) + "...";
	}

	public static String[] combineDupString(String[] array) {
		String[] result = null;
		Collection list = new ArrayList();
		for (int i = 0; i < array.length; i++) {
			if (!list.contains(array[i])) {
				list.add(array[i]);
			}
		}
		result = new String[list.size()];
		int i = 0;
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			String s = (String) iterator.next();
			result[i] = s;
			i++;
		}
		return result;
	}

	public static boolean isValidMobile(String m) {
		return isValid(m) && m.length() >= 7;
	}

	public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
		String regExp = "^((16[0-9])|(19[0-9])|(13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147))\\d{8}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(str);
		return m.matches();
	}

	static final String EX = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'()";

	public static String encodeURIComponent(String input) {
		input = nullToBlank(input);
		int l = input.length();
		StringBuilder o = new StringBuilder(l * 3);
		for (int i = 0; i < l; i++) {
			String e = input.substring(i, i + 1);
			if (EX.indexOf(e) == -1) {
				try {
					byte[] b = e.getBytes("utf-8");
					o.append(getHex(b));
				} catch (Exception e2) {
					log.error("Error", e2);
				}
				continue;
			}
			o.append(e);
		}
		return o.toString();
	}

	public static String getHex(byte buf[]) {
		StringBuilder o = new StringBuilder(buf.length * 3);
		for (int i = 0; i < buf.length; i++) {
			int n = (int) buf[i] & 0xff;
			o.append("%");
			if (n < 0x10)
				o.append("0");
			o.append(Long.toString(n, 16).toUpperCase());
		}
		return o.toString();
	}

	public static String filterBlank(String s) {
		if (s == null) {
			return "";
		}
		return s.replace("\u3000", "").replace(" ", "");
	}

	/**
	 * 将15位的身份证号码转换成18位,具体算法参考
	 * http://www.cnblogs.com/naotang/archive/2009/03/06/1404870.html
	 *
	 * @param cardNumber
	 * @return
	 */
	public static String convertTo18CardNum(String cardNumber) {
		cardNumber = nullToBlank(cardNumber).trim();
		if (cardNumber.length() == 15) {
			// 310102850814364
			try {
				cardNumber = cardNumber.substring(0, 6) + "19"
						+ cardNumber.substring(6);
				cardNumber = cardNumber + getCardNumberLastChar(cardNumber);
			} catch (Exception e) {
			}
		}
		return cardNumber;
	}

	/**
	 * 1.将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7 9 10 5 8 4 2 1 6 3 7 9 10 5 8
	 * 4 2 2.将这17位数字和系数相乘的结果相加。 3.用加出来和除以11，看余数是多少？ 4余数只可能有0 1 2 3 4 5 6 7 8 9
	 * 10这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3 2。
	 * 5.通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。如果余数是10，身份证的最后一位号码就是2。
	 *
	 * @return
	 */
	private static String getCardNumberLastChar(String c17) throws Exception {
		try {
			int re = 0;
			int[] xishu = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
			for (int i = 0; i < xishu.length; i++) {
				re += xishu[i] * Integer.parseInt(c17.charAt(i) + "");
			}
			re = re % 11;
			String[] lastChar = { "1", "0", "X", "9", "8", "7", "6", "5", "4",
					"3", "2" };
			return lastChar[re];
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 将18位的身份证号码转换成15位
	 *
	 * @param cardNumber
	 * @return
	 */
	public static String convertTo15CardNum(String cardNumber) {
		cardNumber = nullToBlank(cardNumber).trim();
		if (cardNumber.length() == 18) {
			cardNumber = cardNumber.substring(0, 6)
					+ cardNumber.substring(8, 17);
		}
		return cardNumber;
	}

	/**
	 * 从15或者18位身份证中获得人员的性别
	 *
	 * @param cardNumber
	 * @return
	 */
	public static int getGenderFromCarnNum(String cardNumber) {
		int gender = 0;
		try {
			cardNumber = nullToBlank(cardNumber).trim();
			if (cardNumber.length() == 18 || cardNumber.length() == 15) {
				cardNumber = convertTo15CardNum(cardNumber);
				return Integer
						.parseInt(cardNumber.charAt(cardNumber.length() - 1)
								+ "") % 2 == 0 ? 2 : 1;
			}
		} catch (Exception e) {
		}
		return gender;
	}

	/**
	 * @param key
	 * @return
	 */
	public static Collection getUsernames(String key) {
		Collection uns = new ArrayList();
		if (key == null)
			return uns;
		key = key.trim();
		String[] usernames = key.split(";");

		for (int i = 0; i < usernames.length; i++) {
			uns.add(usernames[i].toUpperCase().trim());
		}
		return uns;
	}

	public static String USERNAME_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01234567890-_.@";

	/**
	 *
	 * @param username
	 * @return
	 */
	public static boolean isUsernameValid(String username) {
		if (StringUtils.isValid(username)) {
			for (int i = 0; i < username.length(); i++) {
				if (USERNAME_CHARS.indexOf(username.charAt(i)) == -1)
					return false;
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * 从键值字符串中获取对应于键(key)的值,不存在返回空字符串 键值字符串形式为 key1=value1;key2=value2
	 *
	 * @param keyValueString
	 * @param keyName
	 * @return
	 */
	public static String getValue(String keyValueString, String keyName) {
		String[] keyValues = nullToBlank(keyValueString).split(";");
		for (int i = 0; i < keyValues.length; i++) {
			String line = keyValues[i];
			if (line.indexOf("=") > 0) {
				String key = line.substring(0, line.indexOf("=")).trim();
				String value = line.substring(line.indexOf("=") + 1).trim();
				if (key.equals(keyName)) {
					return value;
				}
			}
		}
		return "";
	}

	public static String bytesToString(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(bytes[i]);
			if (i < bytes.length - 1)
				sb.append(",");
		}
		return sb.toString();
	}

	public static String arrayToString(String[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(bytes[i]);
			if (i < bytes.length - 1)
				sb.append(",");
		}
		return sb.toString();
	}

	public static String maskHiddenText(String text) {
		if (text == null) {
			return null;
		}
		int idxBegin = text.toUpperCase().indexOf(HIDDEN_START);
		while (idxBegin >= 0) {
			int idxEnd = 0;
			if (idxBegin >= 0)
				idxEnd = text.toUpperCase().indexOf(HIDDEN_END, idxBegin);
			String hiddenPart = text.substring(idxBegin,
					idxEnd + HIDDEN_END.length());
			text = text.replaceAll("(?i)"
					+ hiddenPart.replace("[", "\\[").replace("]", "\\]"),
					"******");
			idxBegin = text.toUpperCase().indexOf(HIDDEN_START);
		}
		return text;
	}

	public static String showHiddenText(String text) {
		if (text == null) {
			return null;
		}
		text = text.replaceAll("(?i)"
				+ HIDDEN_START.replace("[", "\\[").replace("]", "\\]"), "");
		text = text
				.replaceAll(
						"(?i)"
								+ HIDDEN_END.replace("[", "\\[").replace("]",
										"\\]"), "");
		return text;
	}

	public static String hideText(String text) {
		if (text == null) {
			return null;
		}
		return HIDDEN_START + text + HIDDEN_END;
	}

	public static boolean isBlank(String s) {
		if (s == null)
			return true;
		if (s.length() == 0)
			return true;

        return s.matches("(\\s)*");
    }

    public static boolean isNotBlank(String s) {
	    return  !isBlank(s);
    }

	/**
	 * 以中英文的分号、逗号和回车做为分割符号，返回trim后的结果。
	 *
	 * @param s
	 * @return
	 */
	public static String[] fuzzySplit(String s) {
		if (!StringUtils.isValid(s))
			return new String[0];

		s = s.replace("；", ";");
		s = s.replace("，", ";");
		s = s.replace(",", ";");
		s = s.replace(System.getProperty("line.separator"), ";");
		s = s.replace("\n", ";");

		String[] ss = split(s, ";");
		String[] result = new String[ss.length];

		for (int i = 0; i < result.length; i++) {
			result[i] = ss[i].trim();
		}

		return result;
	}

	public static String getLineSeparator() {
		return System.getProperty("line.separator");
	}


	public static int getSmsCount(String content) {
		String cnt = nullToBlank(content);
		if (cnt.length() <= 70) {
			return 1;
		}
		return (int) Math.round((cnt.length() * 1.0 / 67.0) + 0.5);
	}

	public static Collection split(String[] strings, int limit) {
		if (limit <= 0) {
			return null;
		}
		Collection result = new ArrayList();
		String[] temp = null;
		int j = 0;
		for (int i = 0; i < strings.length; i++) {
			if (i % limit == 0) {
				temp = new String[strings.length - i > limit ? limit
						: strings.length - i];
				j = 0;
			}
			temp[j++] = strings[i];

			if (i % limit == limit - 1 || i == strings.length - 1) {
				result.add(temp);
				temp = null;
			}

		}
		return result;
	}

	public static String getBetween(String str, String begin, String end) {
		String r = "";
		int mkStart = str.indexOf(begin);
		if (mkStart == -1)
			return null;
		int mkEnd = str.indexOf(end, mkStart + begin.length());
		if (mkEnd > mkStart)
			r = str.substring(mkStart + begin.length(), mkEnd);
		return r.trim();
	}

	public static String getXmlTagVal(String str, String xmlTag) {
		String begin = "<" + xmlTag + ">";
		String end = "</" + xmlTag + ">";
		return getBetween(str, begin, end);
	}

	public static String reorderAnswer(String ori) {
		if (ori == null)
			return "";
		final String temp = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuffer sf = new StringBuffer("__________________________");
		for (int i = 0; i < ori.length(); i++) {
			char c = ori.charAt(i);
			int loc = temp.indexOf(c);
			sf.setCharAt(loc, c);
		}
		String result = sf.toString();
		result = result.replace("_", "");
		return result;

	}

	public static String filter(String str, String regex) {
		if (str == null)
			return "";

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

    public static String filter(String str) {
       String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）\\v——+|{}]";
	   return filter(str, regEx);
    }

	public static int getWordCount(String s) {

		s = s.replaceAll("[^\\x00-\\xff]", "**");
		int length = s.length();
		return length;
	}

	/*
	 * William 2016-0918 get sub string by max length
	 */
	public static String getShort(String str, Integer maxLength,
			boolean showTitle) {
       if(StringUtils.isBlank(str))
           return null;

		String shortStr = (str.length() > maxLength) ? str.substring(0,
				maxLength) + "..." : str;

		if (showTitle && str.length() > maxLength)
			shortStr = "<span class=\"title\" title=\"" + str + "\">"
					+ shortStr + "</span>";

		return shortStr;

	}

	public static String getBirthFromCardNumber(String cardNumber) {
		if (isBlank(cardNumber))
			return null;

		String year = "";
		String month = "";
		String date = "";

		if (cardNumber.length() == 18) {
			year = cardNumber.substring(6, 10);
			month = cardNumber.substring(10, 12);
			date = cardNumber.substring(12, 14);
		} else if (cardNumber.length() == 15) {
			year = "19" + cardNumber.substring(6, 8);
			month = cardNumber.substring(8, 10);
			date = cardNumber.substring(10, 12);
		}

		try {
			int iYear = Integer.parseInt(year);
			int iMonth = Integer.parseInt(month);
			int iDate = Integer.parseInt(date);
			if (year.length() == 4 && month.length() == 2 && date.length() == 2
					&& iYear > 1900 && iYear < 2100 && iMonth >= 1
					&& iMonth <= 12 && iDate >= 1 && iDate <= 31)
				return year + "-" + month + "-" + date;
		} catch (Exception e) {
		}

		return null;
	}




    /***
     * 下划线命名转为驼峰命名
     *
     * @param para
     *        下划线命名的字符串
     */

    public static String underlineToHump(String para){
        StringBuilder result=new StringBuilder();
        String a[]=para.split("_");
        for(String s:a){
            if(result.length()==0){
                result.append(s.toLowerCase());
            }else{
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }



    /***
     * 驼峰命名转为下划线命名
     *
     * @param para
     *        驼峰命名的字符串
     */

    public static String humpToUnderline(String para, boolean isUpperCase){
        StringBuilder sb=new StringBuilder(para);
        int temp=0;//定位
        for(int i=0;i<para.length();i++){
            if(Character.isUpperCase(para.charAt(i))){
                sb.insert(i+temp, "_");
                temp+=1;
            }
        }
        return isUpperCase ? sb.toString().toUpperCase() : sb.toString().toLowerCase();
    }

	public static String shortUUID() {
		UUID uuid = UUID.randomUUID();
		long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
		return Long.toString(l, Character.MAX_RADIX);
	}


    public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };


    public static String generateShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();

    }


    /**
     * 生成16位不重复的随机数，含数字+大小写
     * @return
     */
    public static String getGUID() {
        StringBuilder uid = new StringBuilder();
        //产生16位的强随机数
        int capCount = 0;
        int lowerCount =0;
        int digitCount =0;
        Random rd = new SecureRandom();
        for (int i = 0; i < 6; i++) {
            //产生0-2的3位随机数
            int type = rd.nextInt(3);
            switch (type){
                case 0:
                    //0-9的随机数
                    int anInt = rd.nextInt(10);
                    while (anInt == 0 || anInt == 1) {
                        anInt = rd.nextInt(10);
                        if (anInt != 0 && anInt != 1) {
                            break;
                        }
                    }
                    uid.append(anInt);
                    break;
                case 1:
                    //ASCII在65-90之间为大写,获取大写随机
                    int capital = rd.nextInt(25) + 65;
                    while (capital == 0 || capital == 73 || capital == 79) {// 73 I , 79 O
                        capital=  rd.nextInt(25) + 65;
                        if (capital != 0 && capital != 73 && capital!=79) {
                            break;
                        }
                    }

                    uid.append((char) capital);
                    break;
                case 2:
                    //ASCII在97-122之间为小写，获取小写随机
                    int lowerCase = rd.nextInt(25) + 97;
                    while (lowerCase == 0 || lowerCase == 108 || lowerCase == 105 || lowerCase == 111) {// 108 l ,
                        // 111 o, 105 i
                        lowerCase=  rd.nextInt(25) + 97;
                        if (lowerCase != 0 && lowerCase != 108 && lowerCase != 105 && lowerCase != 111) {
                            break;
                        }
                    }
                    uid.append((char) lowerCase);
                    break;
                default:
                    break;
            }
        }
        return uid.toString();
    }

    /**
     * @description 解析日志url
     * @param url 需要解析的单条日志内容
     */
    public static Map<String, String> getUrlParams(String url){
        HashMap<String,String> strUrlParas = new HashMap<>();
        strUrlParas.clear();
//		传递的URL参数
        String strUrl = "";
        String strUrlParams = "";


//		解析访问地址
        if(url.contains("?")){
            String[] strUrlPatten = url.split("\\?");
            strUrl = strUrlPatten[0];
            strUrlParams = strUrlPatten[1];

        }else{
            strUrl = url;
            strUrlParams = url;
        }

        strUrlParas.put("URL", strUrl);
//		解析参数
        String[] params = null;

        if(strUrlParams.contains("&")){
            params = strUrlParams.split("&");
        }else{
            params = new String[] {strUrlParams};
        }

//		保存参数到参数容器
        for(String p:params){
            if(p.contains("=")) {
                String[] param = p.split("=");
                if(param.length==1){
                    strUrlParas.put(param[0],"");
                }else{

                    String key = param[0];
                    String value = param[1];

                    strUrlParas.put(key, value);
                }
            }else {
                strUrlParas.put("errorParam",p);
            }
        }
        return strUrlParas;
    }

    public static boolean isChinaMobile(String mobile) {
        if (mobile == null)
            return false;
        String PATTERN = "^((13[0-9])|(14[0,1,4-9])|(15[0-3,5-9])|(16[2,5,6,7])|(17[0-8])|(18[0-9])|(19[0-3,5-9]))\\\\d{8}$";
        // Set the email pattern string
        Pattern p = Pattern.compile(PATTERN);

        // Match the given string with the pattern
        Matcher m = p.matcher(mobile);

        // check whether match is found
        return m.matches();

    }


}