package twc.Automation.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.simple.JSONArray;
public class StringUtils {
	/**
	 * Returns toString of a list separated by new line instead of commas
	 *
	 * @param arr
	 * @param postNewLine
	 *            true = new line at end, false = new line at beginning
	 * @return
	 */
	public static String toStringNewLines(List<String> arr, boolean postNewLine) {
		StringBuilder output = new StringBuilder();
		arr.forEach((str) -> {
			output.append((postNewLine ? "" : "\n") + str + (postNewLine ? "\n" : ""));
		});
		return output.toString();
	}

	/**
	 * Method to extract string based on regular expression
	 *
	 * @param sourceText
	 *            Given text to search in
	 * @param pattern
	 *            pattern to match in given text
	 * @return null if no match, else matching string to given regex
	 */
	public static String extractText(String sourceText, String pattern) {
		String result = null;

		final Pattern p = Pattern.compile(pattern);
		final Matcher m = p.matcher(sourceText);
		if(m.find()) {
			result = m.group(1);
		}
		return result;
	}

	/**
	 * Converts any array into readable format
	 *
	 * @param paramArray
	 *            Current test method
	 * @return String representation of the current test, including parameters
	 */
	public static String getParameterString(final Object[] paramArray) {
		final StringBuilder parameters = new StringBuilder();
		if((paramArray != null) && (paramArray.length > 0)) {
			String deepSearch = Arrays.deepToString(paramArray);
			parameters.append(deepSearch.substring(1, deepSearch.length() - 1).replace("[", "(").replace("]", ")"));
			if(parameters.length() == 0 || parameters.charAt(0) != '(') {
				return "(" + parameters + ")";
			}
			// List<Object> list = Arrays.asList(paramArray);
			// parameters.append("(");
			// for (int i = 0; i < list.size(); i++) {
			// System.out.println(list.get(i).toString());
			// if (i == (list.size() - 1)) {
			// parameters.append(list.get(i).toString() + ")");
			// } else {
			// parameters.append(list.get(i).toString() + ",");
			// }
			// }
		}
		return parameters.toString();
	}

	/**
	 * Parses a string into a list
	 *
	 * @param str
	 *            String thats comma separated
	 * @return
	 */
	public static List<String> getAsList(String str) {
		if((str != null) && !str.isEmpty()) {
			str.replace(" ", ""); // remove spaces
			String[] splitStr = str.split(",");
			return Arrays.asList(splitStr);
		}
		return new ArrayList<String>();
	}
	public static String jSONArrayToString(JSONArray jsa) {
		String s = "";
		for (int i = 0; i<jsa.size(); i++) {
			 s = s.concat(jsa.get(i).toString())+",";
		}
		s=s.substring(0, s.length()-1);
		return s;
	}
}
