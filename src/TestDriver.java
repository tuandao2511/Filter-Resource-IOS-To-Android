import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestDriver {
	public static final String ANDROID_FILE_PATH = "/Users/mac/Documents/its-global/Language strings/strings.xml";
	public static final String IOS_FILE_PATH = "/Users/mac/Documents/its-global/Language strings/base.strings";
	public static final String IOS_FILE_PATH_JA = "/Users/mac/Documents/its-global/Language strings/ja.strings";
	public static final String IOS_FILE_PATH_ZH = "/Users/mac/Documents/its-global/Language strings/zh-Hans.strings";
	public static final String OUTPUT_FILE_PATH = "/Users/mac/Documents/its-global/Language strings/resource-update.txt";
	public static final String ANDROID_ENGLISH = "/Users/mac/Documents/its-global/Language strings/strings-update.xml";
	public static final String ANDROID_JAPAN = "/Users/mac/Documents/its-global/Language strings/string-ja.xml";
	public static final String ANDROID_ZH = "/Users/mac/Documents/its-global/Language strings/string-zh.xml";
	public static final String OUTPUT_DIFF_JA_KEY = "/Users/mac/Documents/its-global/Language strings/output-diff-key-ja.txt";
	public static final String OUTPUT_DIFF_JA_VALUE = "/Users/mac/Documents/its-global/Language strings/output-diff-value-ja.txt";
	public static final String OUTPUT_DIFF_ZH = "/Users/mac/Documents/its-global/Language strings/output-diff-zh.txt";
	public static final String OUTPUT_FILE_DIFF_JA = "/Users/mac/Documents/its-global/Language strings/output-diff-jar.txt";

	public static void main(String[] args) {
		try {
//			anhdt: th so sanh 2 resource android ios
//			FileWriter f0 = new FileWriter(OUTPUT_FILE_DIFF_JA);
			BufferedReader brIos = new BufferedReader(new FileReader(IOS_FILE_PATH_ZH));
			for(String iosLine; (iosLine = brIos.readLine())!=null;) {
				boolean check = false;
				BufferedReader brAndroid = new BufferedReader(new FileReader(ANDROID_ZH));
				for(String androidLine; (androidLine = brAndroid.readLine())!=null;) {
					if (compareResource(iosLine, androidLine)) {
						check = true;
						break;
					}
				}
				if (!check) {
//					extractResouce(iosLine, f0);
					extractResouce(iosLine);

				}
			}
			//anhdt: th android language
//			perform(ANDROID_ENGLISH, ANDROID_JAPAN);
			//anhdt: read output file
//			BufferedReader brOutput = new BufferedReader(new FileReader(OUTPUT_FILE_PATH));
//			int count1 = 0;
//			for(String a; (a = brOutput.readLine())!=null;) {
//				count1++;
//			}
//			System.out.println("count1: " + count1);
//			f0.close();
		} catch(IOException io) {
			
		}
	
		System.out.println("count " + count);
//		boolean a = compareResource("\"TABmasterMiniLink_Format_DisplayTime2\" = \"0 : %d\";",
//				"    <string name=\"E_00800_00003\">The XC-Gate authentication is not completed.\\nAuthenticate in the PC client.</string>\n" + 
//				"");
//		System.out.println(a);
	}
	
	public static void perform(String path1, String path2) throws IOException {
		FileWriter fKey = new FileWriter(OUTPUT_DIFF_JA_KEY);
		FileWriter fValue = new FileWriter(OUTPUT_DIFF_JA_VALUE);
		BufferedReader brIos = new BufferedReader(new FileReader(path1));
		for(String iosLine; (iosLine = brIos.readLine())!=null;) {
			boolean check = false;
			BufferedReader brAndroid = new BufferedReader(new FileReader(path2));
			for(String androidLine; (androidLine = brAndroid.readLine())!=null;) {
				if (compareAndroidResource(iosLine, androidLine)) {
					check = true;
					break;
				}
			}
			if (!check) {
//				extractResouce(iosLine, f0);
				extractResouceLanguage(iosLine, fKey, fValue);
			}
		}
		fKey.close();
		fValue.close();
	}
	
	public static void printResource(String line, String regex, boolean isAndroid) {
		Matcher matcher = Pattern.compile(regex).matcher(line);
		while(matcher.find()) {
			String key = matcher.group(1);
			String value = matcher.group(2);
			if (isAndroid) {
//				System.out.printf("key=%s value=%s", key, value);
				System.out.printf("<string name=%s>%s</string>", key, value);
			} else {
				System.out.printf("\"%s\"=\"%s\"", key, value);
			}
			System.out.println();
		}
	}
	
	
	public static boolean compareAndroidResource(String eText, String jText) {
		Matcher eMatcher = Pattern.compile(RegularExpressions.ANDROID_RESOURCE_REGEX).matcher(eText);
		boolean matchAndroid = false;
		boolean matchIos = false;

		String aKey = "";
		String aValue = "";
		String iKey = "";
		String iValue = "";
		
		while(eMatcher.find()) {
			aKey = eMatcher.group(1).toLowerCase();
			aValue = eMatcher.group(2);
			matchAndroid =  true;
		}
		Matcher jMatcher = Pattern.compile(RegularExpressions.ANDROID_RESOURCE_REGEX).matcher(jText);

		while(jMatcher.find()) {
			iKey = jMatcher.group(1).toLowerCase();
			iValue = jMatcher.group(2);
			matchIos = true;
		}
//		System.out.println("matcherAndroid " + matchAndroid);
//		System.out.println("matcherIos " + matchIos);
//		System.out.println("aKey " + aKey);
//		System.out.println("iKey " + iKey);
//
//		System.out.println("aKey.equals(iKey) " + aKey.equals(iKey));
		return matchAndroid && matchIos && aKey.equals(iKey);
	}
	
	
	public static boolean compareResource(String iosLine, String androidLine) {
		Matcher matcherAndroid = Pattern.compile(RegularExpressions.ANDROID_RESOURCE_REGEX).matcher(androidLine);
		boolean matchAndroid = false;
		boolean matchIos = false;

		String aKey = "";
		String aValue = "";
		String iKey = "";
		String iValue = "";
		
		while(matcherAndroid.find()) {
			aKey = matcherAndroid.group(1).toLowerCase();
			aValue = matcherAndroid.group(2);
			matchAndroid =  true;
		}
		Matcher matcherIos = Pattern.compile(RegularExpressions.IOS_RESOURCE_REGEX).matcher(iosLine);

		while(matcherIos.find()) {
			iKey = matcherIos.group(1).toLowerCase();
			iValue = matcherIos.group(2);
			matchIos = (iKey.contains(".") || iKey.contains("-")) ? false : true;
		}
//		System.out.println("matcherAndroid " + matchAndroid);
//		System.out.println("matcherIos " + matchIos);
//		System.out.println("aKey " + aKey);
//		System.out.println("iKey " + iKey);
//
//		System.out.println("aKey.equals(iKey) " + aKey.equals(iKey));
		return matchAndroid && matchIos && aKey.equals(iKey);
	}
	static int count = 0;
	public static void extractResouce(String iosLine) throws IOException {
		Matcher matcherIos = Pattern.compile(RegularExpressions.IOS_RESOURCE_REGEX).matcher(iosLine);
		while(matcherIos.find()) {
			String iKey = matcherIos.group(1);
			String iValue = matcherIos.group(2);
			if (iValue.contains("%@")) {
				iValue =  iValue.replace("@", "d");
			}
			if (iValue.contains("'")) {
				iValue = iValue.replace("'", "\\'");
			}
//			<string name="settings">Settings</string>
			String result = String.format("<string name=\"%s\">%s</string>", iKey, iValue);
			System.out.printf("%s\n", result);

//			System.out.printf("result %s", result);
//			System.out.println();
//			System.out.printf("<string name=%s>%s</string>", iKey, iValue);
//			System.out.println();
//			String newLine = System.getProperty("line.separator");
//			writer.write(result+ newLine);
			count++;
		}
	}
	
	public static void extractResouceLanguage(String text, FileWriter keyWriter, FileWriter valueWriter) throws IOException {
		Matcher matcher = Pattern.compile(RegularExpressions.ANDROID_RESOURCE_REGEX).matcher(text);
		while(matcher.find()) {
			String iKey = matcher.group(1);
			String iValue = matcher.group(2);
//			<string name="settings">Settings</string>
			String result = String.format("<string name=\"%s\">%s</string>", iKey, iValue);
			System.out.printf("%s\n", result);

//			System.out.printf("result %s", result);
//			System.out.println();
//			System.out.printf("<string name=%s>%s</string>", iKey, iValue);
//			System.out.println();
			String newLine = System.getProperty("line.separator");
//			writer.write(result+ newLine);
			keyWriter.write(iKey + newLine);
			valueWriter.write(iValue + newLine);
			count++;
		}
	}
}
