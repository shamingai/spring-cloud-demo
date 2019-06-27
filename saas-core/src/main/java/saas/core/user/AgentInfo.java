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
@JsonPropertyOrder({ "agentId", "agentOs", "agentMid", "agentVersion", "agentIp", "timestamp"})
public class AgentInfo implements Serializable {

	private static final long serialVersionUID = -4308395060771135648L;
		
	private String agentId; 	  //客户端ID，saas app可不传此值，定制的app必须填写。
	
	private String agentOs;  	  //客户端操作系统 ，IOS，Android，Winphone, Windows NT 6.1
	
	private String agentMid;  	  //客户端设备id，I-044B1AC84CB044909C94874ADB34E994
	
	private String agentVersion;  //客户端版本
	
	private String agentIp;  	  //客户端IP
	
	private Long timestamp = System.currentTimeMillis();
	
}