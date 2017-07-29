package demo.work;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement(name = "root")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChargeQueryResponse extends Response {

	@XmlElement(name = "body")
	private ChargeQueryResponseBody body;

	public ChargeQueryResponseBody getBody() {
		return body;
	}

	public void setBody(ChargeQueryResponseBody body) {
		this.body = body;
	}
}
