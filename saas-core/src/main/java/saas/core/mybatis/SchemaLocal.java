package saas.core.mybatis;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import saas.core.constants.Constants;

import java.io.Serializable;

@Slf4j
public class SchemaLocal implements Serializable {

	private static final long serialVersionUID = 355097567658924301L;
	
	private static ThreadLocal<SchemaInfo> schemaThreadLocal = new TransmittableThreadLocal<SchemaInfo>();

    public static void save(SchemaInfo schemaName) {
        schemaThreadLocal.set(schemaName);
    }
        
    public static void saveSaasAdmin() {
        schemaThreadLocal.set(new SchemaInfo(Constants.DB_SAAS_ADMIN));
    }
    
    public static void saveSaasAdmin(int times) {
        schemaThreadLocal.set(new SchemaInfo(Constants.DB_SAAS_ADMIN, times));
    }
    
    public static void saveSaasSalesman() {
        schemaThreadLocal.set(new SchemaInfo(Constants.DB_SAAS_SALESMAN));
    }
    
    public static void saveSaasSalesman(int times) {
        schemaThreadLocal.set(new SchemaInfo(Constants.DB_SAAS_SALESMAN, times));
    }
    
    public static String getCurrentSchema(){
    	SchemaInfo schemaInfo = null;
    	
    	String schemaName = null;
    	
    	try{
    		schemaInfo = schemaThreadLocal.get();
    		
    		if(schemaInfo == null){
    			return null;
    		}
    		
    		schemaName = schemaInfo.getName();
	    	
	    	log.info("schemaName="+schemaName);
	    	
	    	if(StringUtils.isBlank(schemaName)){
	    		return null;
	    	}
    	}finally{
    		//默认只使用一次，使用过就清空    		
    		if(schemaInfo != null && schemaInfo.reduceTimes() <= 0){
    			save(null);
    		}
    	}
    	
    	log.info("finally schemaName="+schemaName);
    	
    	return schemaName;
    }
    
    
}
