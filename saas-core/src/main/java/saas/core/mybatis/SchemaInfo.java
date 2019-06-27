package saas.core.mybatis;

import java.io.Serializable;

public class SchemaInfo implements Serializable {

	private static final long serialVersionUID = -2388485240779450008L;

	private String name;
	
	private int times = 1;
	
	public SchemaInfo() {
		super();
	}

	public SchemaInfo(String name) {
		super();
		this.name = name;
	}

	public SchemaInfo(String name, int times) {
		super();
		this.name = name;
		this.times = times;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}
	
	public int reduceTimes(){
		this.times--;
		
		return this.times;
	}
	
	
}