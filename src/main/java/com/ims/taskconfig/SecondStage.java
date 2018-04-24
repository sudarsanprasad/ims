package com.ims.taskconfig;

import java.util.regex.*;
import java.io.*;

public class SecondStage {

	public static void main(String[] aArgs) throws IOException {

		FileWriter fValid = new FileWriter("/home/webonise/valid.txt");
		FileWriter fInvalid = new FileWriter("/home/webonise/invalid.txt");

		String strRe = new String(
				"^([a-z0-9._%+-])+@[a-z0-9.-]+.(?:[A-Z]{2}|com|org|net|edu|gov|mil|biz|vsnl|yahoo|gmail|info|mobi|name|aero|asia|jobs|museum)+$");
		Pattern p = Pattern.compile(strRe, Pattern.CASE_INSENSITIVE
				| Pattern.UNICODE_CASE | Pattern.MULTILINE);

		try {
			BufferedReader in = new BufferedReader(new FileReader("/home/webonise/users.csv"));

			String str2 = ";";
			String str1 = "\"";
			String str;

			String[] x = new String[2];
			while ((str = in.readLine()) != null) {

				String trial[];
				trial = str.split(str2);
				try {

					for (int i = 0; i < trial.length; i++) {

						x[i] = trial[i].replaceAll(str1, "");
						x[i] = x[i].replaceAll(" ", "");
					}

					Matcher m = p.matcher(x[1]);
					if (m.matches()) {
						fValid.write(str + "n");
					} else {
						fInvalid.write(str + "n");
					}

				} catch (Exception e) {
					e.printStackTrace();

				}
			}
			fValid.close();
			fInvalid.close();

			in.close();
		} catch (IOException e) {
			System.out.println("IO Exception : " + e);
		}

	}
}
