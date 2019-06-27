package saas.core.user;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@EqualsAndHashCode
@Data
@Setter
@Getter
@JsonPropertyOrder({ "uid", "enterpriseCode", "enterpriseAccount", "enterpriseName", "userId", "userAccount", "userName", "type", "registrationId", "agentId", "timestamp"})
public class TokenInfo implements Serializable {

	private static final long serialVersionUID = -4308395060771135642L;
	
	private String uid;
	private String enterpriseCode;
	private String enterpriseAccount;
	private String enterpriseName;
	private String userId;
	private String userAccount;
	private String userName;
	
	private String type = "web";			//web;app2b;app2c	
	private String registrationId;			//对应极光推送的 registration_id, 由客户端初始化JPush 成功后生成，登录时传入	
	
	private String agentId;					//对应header中的 fyi-agent-id 值
	
	private Long timestamp = System.currentTimeMillis();
	
	
}