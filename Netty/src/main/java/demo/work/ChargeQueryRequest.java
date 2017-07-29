package demo.work;



import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "body")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChargeQueryRequest extends Request {


	/**
	 * 渠道标识
	 */
	@XmlElement(name = "chncode")
	protected String chncode = "chncode";

	/**
	 * 受理机构代码
	 */
	@XmlElement(name = "instcode")
	protected String instcode = "instcode";

	/**
	 * 商户号
	 */
	@XmlElement(name = "mercid")
	protected String mercid = "mercid";
	/**
	 * 终端号
	 */
	@XmlElement(name = "termid")
	protected String termid = "termid";
	/**
	 * 线路号码
	 */
	@XmlElement(name = "telecode")
	protected String telecode= "telecode";
	/**
	 * 输入条件
	 */
	@XmlElement(name = "chntype")
	protected String chntype= "chntype";
	/**
	 * 二级商户号
	 */
	@XmlElement(name = "secmercd")
	private String secmercd;
	/**
	 * 二级商户名称
	 */
	@XmlElement(name = "secmername")
	private String secmername;
	/**
	 * 二级商户终端号
	 */
	@XmlElement(name = "sectermid")
	private String sectermid;
	/**
	 * 原入账交易sid号
	 */
	@XmlElement(name = "srcsid")
	private String srcsid;

	/**
	 * IP地址
	 */
	@XmlElement(name = "ipaddr")
	private String ipaddr;
	/**
	 * 支付卡号
	 */
	@XmlElement(name = "pan")
	private String pan;
	/**
	 * 金额，12位长，分为单位，左补零。转账金额
	 */
	@XmlElement(name = "amount")
	private String amount;
	/**
	 * 交易代码
	 */
	@XmlElement(name = "busid")
	protected String busid="1ID";
	/**
	 * 商户号
	 */
	@XmlElement(name = "service")
	protected String service="saleqry";

	public String getSrcsid() {
		return srcsid;
	}

	public void setSrcsid(String srcsid) {
		this.srcsid = srcsid;
	}

	public String getIpaddr() {
		return ipaddr;
	}

	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getService() {
		return service;
	}

	public String getBusid() {
		return busid;
	}

	public void setBusid(String busid) {
		this.busid = busid;
	}


	public String getChncode() {
		return chncode;
	}

	public void setChncode(String chncode) {
		this.chncode = chncode;
	}

	public String getInstcode() {
		return instcode;
	}

	public void setInstcode(String instcode) {
		this.instcode = instcode;
	}

	public String getMercid() {
		return mercid;
	}

	public void setMercid(String mercid) {
		this.mercid = mercid;
	}

	public String getTermid() {
		return termid;
	}

	public void setTermid(String termid) {
		this.termid = termid;
	}

	public String getTelecode() {
		return telecode;
	}

	public void setTelecode(String telecode) {
		this.telecode = telecode;
	}

	public String getChntype() {
		return chntype;
	}

	public void setChntype(String chntype) {
		this.chntype = chntype;
	}

	public String getSecmercd() {
		return secmercd;
	}

	public void setSecmercd(String secmercd) {
		this.secmercd = secmercd;
	}

	public String getSecmername() {
		return secmername;
	}

	public void setSecmername(String secmername) {
		this.secmername = secmername;
	}

	public String getSectermid() {
		return sectermid;
	}

	public void setSectermid(String sectermid) {
		this.sectermid = sectermid;
	}

}
