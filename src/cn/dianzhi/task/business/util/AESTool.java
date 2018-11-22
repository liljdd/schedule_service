package cn.dianzhi.task.business.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;



public class AESTool {
    /** 
     * 加密 
     *  
     * @param content 需要加密的内容 
     * @param password  加密密码 
     * @return 
     */  
	public static byte[] encrypt(String content,String key){
		try {
			KeyGenerator kgen;
			kgen = KeyGenerator.getInstance("AES");
	        //需手动指定 SecureRandom 随机数生成规则
	        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
	        random.setSeed(key.getBytes());
	        kgen.init(128, random);
	        SecretKey secretKey = kgen.generateKey();
	        byte[] enCodeFormat = secretKey.getEncoded();
	        SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, "AES");
	        //创建密码器
	        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	        byte[] byteContent = content.getBytes("utf-8");
	        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
	        byte[] result =cipher.doFinal(byteContent);
	        return result;
		} catch (NoSuchAlgorithmException e) {  
	            e.printStackTrace();  
	    } catch (NoSuchPaddingException e) {  
	            e.printStackTrace();  
	    } catch (InvalidKeyException e) {  
	            e.printStackTrace();  
	    } catch (UnsupportedEncodingException e) {  
	            e.printStackTrace();  
	    } catch (IllegalBlockSizeException e) {  
	            e.printStackTrace();  
	    } catch (BadPaddingException e) {  
	            e.printStackTrace();  
	    }  
		return null;
}
    /**解密 
     * @param content  待解密内容 
     * @param password 解密密钥 
     * @return 
     */  
    private static byte[] decrypt(byte[] content, String key) {  
            try {  
            	KeyGenerator kgen;
    			kgen = KeyGenerator.getInstance("AES");
    	        //需手动指定 SecureRandom 随机数生成规则
    	        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
    	        random.setSeed(key.getBytes());
    	        kgen.init(128, random);
    	        SecretKey secretKey = kgen.generateKey();
    	        byte[] enCodeFormat = secretKey.getEncoded();
    	        SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, "AES");
    	        //创建密码器
    	        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    	        byte[] byteContent = content;
    	        cipher.init(Cipher.DECRYPT_MODE, keySpec);
    	        byte[] result =cipher.doFinal(byteContent);
    	        return result;
            } catch (NoSuchAlgorithmException e) {  
                    e.printStackTrace();  
            } catch (NoSuchPaddingException e) {  
                    e.printStackTrace();  
            } catch (InvalidKeyException e) {  
                    e.printStackTrace();  
            } catch (IllegalBlockSizeException e) {  
                    e.printStackTrace();  
            } catch (BadPaddingException e) {  
                    e.printStackTrace();  
            }  
            return null;  
    }  
    
    /**将二进制转换成16进制 
     * @param buf 
     * @return 
     */  
    private static String parseByte2HexStr(byte buf[]) {  
            StringBuffer sb = new StringBuffer();  
            for (int i = 0; i < buf.length; i++) {  
                    String hex = Integer.toHexString(buf[i] & 0xFF);  
                    if (hex.length() == 1) {  
                            hex = '0' + hex;  
                    }  
                    sb.append(hex.toUpperCase());  
            }  
            return sb.toString();  
    }  
    
    /**将16进制转换为二进制 
     * @param hexStr 
     * @return 
     */  
    private static byte[] parseHexStr2Byte(String hexStr) {  
            if (hexStr.length() < 1)  
                    return null;  
            byte[] result = new byte[hexStr.length()/2];  
            for (int i = 0;i< hexStr.length()/2; i++) {  
                    int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);  
                    int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);  
                    result[i] = (byte) (high * 16 + low);  
            }  
            return result;  
    }  
    
    public static String encodeAES(String content, String password){
    	 byte[] encryptResult = encrypt(content, password);  
         return parseByte2HexStr(encryptResult);  
    }
    public static String decodeAES(String encryptResultStr, String password){
    	 byte[] decryptFrom = parseHexStr2Byte(encryptResultStr);  
         byte[] decryptResult = decrypt(decryptFrom,password);  
         try {
			return  new String(decryptResult,"UTF-8");
			// TODO Auto-generated catch block
		}catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
         return "";
    }
    
}
