package demo.work;



import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Request{

    /**
     * 交易请求流水号，最长32位，必须唯一（作为交易查询依据）生成规则，
     */
    @XmlElement(name = "sid")
    protected String sid;

    /**
     * 固定值：commit
     */
    @XmlElement(name = "action")
    protected String action="commit";

    /**
     * 商户号
     */
    @XmlElement(name = "receiver")
    protected String receiver = "receiver";
    /**
     * 商户号
     */
    @XmlElement(name = "sender")
    protected String sender = "sender";

    /**
     * MAC
     */
    @XmlElement(name = "mac")
    private String mac;



    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getAction() {
        return action;
    }



}
