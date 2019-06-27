package saas.util.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Data
@Setter
@Getter
public class IDVO implements Serializable{

	private static final long serialVersionUID = -7203470675978370273L;
	
	public IDVO() {
		super();
	}
	
	public IDVO(String id) {
		super();
		this.id = id;
	}

    private String id;

}
