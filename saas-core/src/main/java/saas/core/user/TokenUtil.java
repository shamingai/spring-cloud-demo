package saas.core.user;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import saas.core.constants.TokenConstants;
import saas.util.JacksonUtil;
import saas.util.codec.Cryptogram;

/**
 * Description: 编码相关的封装类
 */
@Slf4j
public class TokenUtil {	

    
    public static String getTokenKey(TokenInfo tokenInfo){
    	String tokenType = tokenInfo.getType();
    	
    	if(TokenConstants.WEB.TOKEN_TYPE.equalsIgnoreCase(tokenType)){
    		//return TokenConstants.WEB.TOKEN_REDIS_KEY;
    		return String.format(TokenConstants.WEB.TOKEN_REDIS_KEY, tokenInfo.getEnterpriseCode(), tokenType, tokenInfo.getUid()); 
    	}    	
    	//return TokenConstants.APP2b.TOKEN_REDIS_KEY;
    	return String.format(TokenConstants.APP2b.TOKEN_REDIS_KEY, tokenInfo.getEnterpriseCode(), tokenType, tokenInfo.getUid()); 
    }
	

	public static String encodeToken(TokenInfo tokenInfo) {

		if (tokenInfo == null) {
			return null;
		}
		String encryptToken = null;

		try {

			String jsonToken = JacksonUtil.toJson(tokenInfo);

			encryptToken = Cryptogram.DESEncrypt(jsonToken, TokenConstants.TOKEN_KEY, TokenConstants.COMMON_IV);

		} catch (Exception e) {
			log.error("decodeToken Exception", e);
		}

		return encryptToken;
	}

	public static TokenInfo decodeToken(String token) {

		if (StringUtils.isBlank(token)) {
			return null;
		}

		String jsonToken = null;
		TokenInfo tokenInfo = null;

		try {

			jsonToken = Cryptogram.DESDecrypt(token, TokenConstants.TOKEN_KEY, TokenConstants.COMMON_IV);

			tokenInfo = JacksonUtil.parseJsonWithFormat(jsonToken, TokenInfo.class);

		} catch (Exception e) {
			log.error("decodeToken Exception", e);
		}
		return tokenInfo;
	}
	
	public static void main(String[] args) {
		TokenInfo token = decodeToken("8kdSlgc31B0p6FuguGEQA0dtJyketzrpRCypiC32xZ6PkRFFA5KBMNqDPbPG1DdQac9jqJOwXOyctY0nlUsREQOKRYMo5i7xyCA31QFKhpGvTpSKEyugexfaF8WBgCSUV3VeDBo21ud1RkevS69fVWREylnHEfZje368z3Z6E1UybzauVivDW1e+mbQ0ronMk997tK1NT1iiI7E5XPhLs23D93zH8ZVKwVz6ac4Urif7b87SU9E0r2APH59haTHI+GQ0gCK3mtGQiRawSs0jsM8NGA07vJA+rNbvDr4dB9Ib2tCf1/Ru4e2CRWFLy9TG+aNP0Qm44r8bkGanCb+qawI/pimkVV+x");  
		
		log.info("token::"+token);
		
		
	}

}
