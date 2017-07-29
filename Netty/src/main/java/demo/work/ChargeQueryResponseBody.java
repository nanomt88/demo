package demo.work;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


@XmlAccessorType(XmlAccessType.FIELD)
public class ChargeQueryResponseBody {


    @XmlElement(name = "retcode")
    protected String returnCode;


    @XmlElement(name = "errmsg")
    protected String errorMsg;

    @XmlElement(name = "sacode")
    String sacode;

    @XmlElement(name = "cstcode")
    String cstcode;

    @XmlElement(name = "cstname")
    String cstname;

    public String getSacode() {
        return sacode;
    }

    public void setSacode(String sacode) {
        this.sacode = sacode;
    }

    public String getCstcode() {
        return cstcode;
    }

    public void setCstcode(String cstcode) {
        this.cstcode = cstcode;
    }

    public String getCstname() {
        return cstname;
    }

    public void setCstname(String cstname) {
        this.cstname = cstname;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
