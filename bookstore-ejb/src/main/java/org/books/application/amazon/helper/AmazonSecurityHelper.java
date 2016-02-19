/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application.amazon.helper;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class AmazonSecurityHelper {
	public static final String ASSOCIATE_TAG = "test0e5d-20";
	public static final String ACCESS_KEY = "AKIAIYFLREOYORYNAQTQ";
	private static final String SECRET_KEY = "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";
	private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	private static final String MAC_ALGORITHM = "HmacSHA256";

        private String timestamp;
        private String signature;
        
	public AmazonSecurityHelper(String requestType) throws NoSuchAlgorithmException, InvalidKeyException {

		DateFormat dateFormat = new SimpleDateFormat(TIMESTAMP_FORMAT);
		timestamp = dateFormat.format(Calendar.getInstance().getTime());

		Mac mac = Mac.getInstance(MAC_ALGORITHM);
		SecretKey key = new SecretKeySpec(SECRET_KEY.getBytes(), MAC_ALGORITHM);
		mac.init(key);
		byte[] data = mac.doFinal((requestType + timestamp).getBytes());
		signature = DatatypeConverter.printBase64Binary(data);
	}

    public String getTimestamp() {
        return timestamp;
    }

    public String getSignature() {
        return signature;
    }
        
        
    
}
