package newcrm.business.businessbase.promotions;

import java.util.Objects;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import newcrm.business.dbbusiness.PromotionDB;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.REGULATOR;


public class PromotionResult {

	@JSONField(name="name")
	private String name;
	
	//campaign id. dpb is campaign type id
	@JSONField(name="id")
	private String id;
	
	@JSONField(name="participantId")
	private String participantId;
	
	@JSONField(name="brand")
	private BRAND brand;
	
	@JSONField(name="regulator")
	private REGULATOR regulator;
	
	@JSONField(name="env")
	private ENV env;
	
	
	@Override
	public int hashCode() {
		return Objects.hash(brand, env, participantId, regulator);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PromotionResult other = (PromotionResult) obj;
		return brand == other.brand && env == other.env && Objects.equals(participantId, other.participantId)
				&& regulator == other.regulator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	public BRAND getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = BRAND.getBRAND(brand);
	}

	public REGULATOR getRegulator() {
		return regulator;
	}

	public void setRegulator(String regulator) {
		this.regulator = REGULATOR.getREGULATOR(regulator);
	}

	public ENV getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = ENV.getENV(env);
	}

	
	
	
	
	@Override
	public String toString() {
		return "{\"name\":\""+this.name+"\",\"id\":\""+this.id+"\",\"participantId\":\""+this.participantId
				+"\",\"brand\":\""+this.brand+"\",\"regulator\":\""+this.regulator+"\",\"env\":\""+this.env+"\"}";
	}
	
    public PromotionResult() {
    	super();
    }
	
	public PromotionResult(String name, String id, String participantId, BRAND brand, REGULATOR regulator, ENV env) {
		super();
		this.name = name;
		this.id = id;
		this.participantId = participantId;
		this.brand = brand;
		this.regulator = regulator;
		this.env = env;
	}

	public void print() {
		String info = "Name: " + name + " ID: " + id + " Participantd ID:  " + participantId + " Brand: " + brand + " regulator: " +regulator +" env: " + env;
		System.out.println(info);
	}
	
	public JSONArray findVoucherTable() {
		if(this.brand ==null || this.env == null || this.regulator == null) {
			return null;
		}
		PromotionDB db = new PromotionDB(env, brand, regulator);
		
		return db.getVouchers(this.participantId);
	}
	
	public static void main(String args[]) {
		String js = "{\"name\":\"Refer A Friend\",\"id\":9,\"participantId\":\"123\",\"brand\":\"vfx\",\"regulator\":\"VFSC2\",\"env\":\"alpha\"}";
		
		JSONObject obj = JSONObject.parseObject(js);
		
		System.out.println(obj);
		PromotionResult result = obj.toJavaObject(PromotionResult.class);
		result.setName("New RAF");
		
		result.print();
	}
}
