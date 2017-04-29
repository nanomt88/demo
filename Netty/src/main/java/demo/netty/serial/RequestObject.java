package demo.netty.serial;

import java.io.Serializable;

/**
 * Created by ZBOOK-17 on 2017/4/25.
 */
public class RequestObject implements Serializable{

    private static final long serialVersionUID = -1651304824249296621L;

    private String id ;
    private String name ;
    private String requestMessage ;
    private byte[] attachment;

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

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return "RequestObject{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", requestMessage='" + requestMessage + '\'' +
                '}';
    }
}
