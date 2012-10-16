package com.campaignslibrary.ws.services;

import android.util.Log;

public class HmacSHAClass {
	
	private static final String encryptionType = "HmacSHA1";
	private static final String encryptionKey = "checkin123";

	 static String hash_hmac(String value) {		

		String strEncrypted = "";
				
		try {
			javax.crypto.Mac mac = javax.crypto.Mac.getInstance(encryptionType);
			javax.crypto.spec.SecretKeySpec secret = new javax.crypto.spec.SecretKeySpec(
					encryptionKey.getBytes(), encryptionType);
			mac.init(secret);
			byte[] digest = mac.doFinal(value.getBytes());
			StringBuilder sb = new StringBuilder(digest.length * 2);
			String s;
			for (byte b : digest) {
				int iVal = (int) (b);
				if (iVal < 0)
					iVal = 256 + iVal;
				s = Integer.toHexString(iVal);
				if (s.length() == 1)
					sb.append('0');
				sb.append(s);
			}
			strEncrypted = sb.toString();
			Log.i("STR", sb.toString());			
		} catch (Exception e) {
			android.util.Log.v("TAG", "Exception [" + e.getMessage() + "]", e);
		}
		return strEncrypted;
	}
}
