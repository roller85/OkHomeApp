package id.co.okhome.okhomeapp.lib;

import com.lambdaworks.crypto.SCrypt;

/**
 * Created by josongmin on 2016-08-28.
 */

public class SCryptPassword {
    public final static String makePassword(final String password){
        String salt = "OKHOME";
        try{
            //Password: 패스워드
//			Salt: 암호학 솔트
//			N: CPU 비용
//			r: 메모리 비용
//			p: 병렬화(parallelization)
//			DLen: 원하는 다이제스트 길이
            byte[] key = SCrypt.scrypt(password.getBytes(), salt.getBytes(), 16384, 8, 1, 128);
            return getHexString(key);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static String getHexString(byte[] b) throws Exception{
        String result = "";
        for (int i=0; i < b.length; i++)
        {
            result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }
}
