package com.claritrics.service.exceptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Class to normalize the errors and maintain customized error descriptions
 * 
 * @author Kishore
 * 
 */
final public class ErrorCodes {

	/**
	 * Method to get documented error message given the code
	 * 
	 * @param code
	 * @return error message
	 */
	public static String getErrorMessageByCode(String code) {
		String message = "";
		try {

			String errorCodesFile = null;
			try {

				final File jarDir = new File(ErrorCodes.class
						.getProtectionDomain().getCodeSource().getLocation()
						.toURI().getPath()).getParentFile();

				if (jarDir != null && jarDir.isDirectory()) {
					errorCodesFile = jarDir.getAbsolutePath() + File.separator
							+ "errorcodes.txt";
				} else {
					errorCodesFile = System.getProperty("user.dir")
							+ File.separator + "errorcodes.txt";
				}
			} catch (Exception e) {
				errorCodesFile = System.getProperty("user.dir")
						+ File.separator + "errorcodes.txt";
			}

			// System.out.println("errorCodesFile : " + errorCodesFile);
			// Construct BufferedReader from InputStreamReader
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(errorCodesFile)));

			String line = null;
			while ((line = br.readLine()) != null) {
				// System.out.println("line : " + line);
				line = line.trim();
				if (line.startsWith("#")) {
					continue;
				}
				if (line.startsWith(code + "=")) {
					message = line.substring(line.indexOf("=") + 1);
					break;
				}
			}
			br.close();
			if (message.isEmpty()) {
				message = code;
			}
			// System.out.println("message : " + message);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return message;
	}

}
