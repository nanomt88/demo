package netty.serial;

import java.io.Serializable;

/**
 * Created by ZBOOK-17 on 2017/4/25.
 */
public class ResponseObject implements Serializable{

    private static final long serialVersionUID = -5796830850162930480L;

    private String id;
    private String name;
    private String responseMessage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    @Override
    public String toString() {
        return "ResponseObject{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", responseMessage='" + responseMessage + '\'' +
                '}';
    }
}
