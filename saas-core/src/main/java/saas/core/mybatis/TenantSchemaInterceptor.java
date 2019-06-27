
package saas.core.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import saas.core.user.SessionLocal;
import saas.util.ReflectHelper;

import java.sql.Connection;
import java.util.Properties;

@Slf4j
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class , Integer.class}) })
public class TenantSchemaInterceptor implements Interceptor {

	public static final String  MYCAT_SCHEMA_ANNOTATION_PREFIX = "/*!mycat:schema=";
	public static final String  MYCAT_SCHEMA_ANNOTATION = MYCAT_SCHEMA_ANNOTATION_PREFIX + "%s*/%s";

	@Override
	public Object intercept(Invocation invocation) throws Throwable {

		String tenantCode = SessionLocal.getEnterpriseCode();
		
		//如果指定了 SchemaName，优先级高于  SessionLocal 中 tenantCode
		String currentSchemaName = SchemaLocal.getCurrentSchema();
		log.info("currentSchemaName = "+currentSchemaName);
		
		if(StringUtils.isNotBlank(currentSchemaName)){
			log.info("currentSchemaName is not blank");
			tenantCode = currentSchemaName;
		}

		if (StringUtils.isBlank(tenantCode)) {
			log.debug("tenantCode 为空，不需要改写sql语句");
			return invocation.proceed();
		}

		log.info("tenantCode = "+tenantCode);
		
		if (invocation.getTarget() instanceof RoutingStatementHandler) {
			
			RoutingStatementHandler statementHandler = (RoutingStatementHandler) invocation.getTarget();

			if(statementHandler == null){
				log.debug("statementHandler == null");
				return invocation.proceed();
			}
			
			BoundSql boundSql = statementHandler.getBoundSql();
			
			if(boundSql == null){
				log.debug("boundSql == null");
				return invocation.proceed();
			}

			String sql = boundSql.getSql();
			
			if(StringUtils.isBlank(sql)){
				log.debug("sql is null return");
				return invocation.proceed();
			}
			
			
			if(StringUtils.trim(sql).startsWith(MYCAT_SCHEMA_ANNOTATION_PREFIX)){
				log.debug("sql include /*!mycat:schema= do nothing ");
				return invocation.proceed();
			}

			ReflectHelper.setFieldValue(boundSql, "sql", String.format(MYCAT_SCHEMA_ANNOTATION, tenantCode, sql));

		}
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {

		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {

	}

}
