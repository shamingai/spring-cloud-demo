package saas.util.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
@ApiModel(description = "列表样式通用实体")
public class DataRowsVO<T> implements Serializable{

	private static final long serialVersionUID = 8089970500081432164L;

	public DataRowsVO(){
		super();
	}
			
	public DataRowsVO(Long total, List<T> rows) {
		super();
		this.total = total;
		this.rows = rows;
	}

	@ApiModelProperty(value = "总条数", position = 1)
    private Long total;

    @ApiModelProperty(value = "内容", position = 2)
    private List<T> rows;
    
 

}
