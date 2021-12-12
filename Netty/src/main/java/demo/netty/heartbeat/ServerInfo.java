package demo.netty.heartbeat;

import java.io.Serializable;
import java.util.Map;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/4/29 下午5:34
 * @Description: 服务器信息
 */

public class ServerInfo implements Serializable {

    private static final long serialVersionUID = 1299865210437907509L;

    private String ip;
    private Map<String, Object> cpuInfo;
    private Map<String, Object> memoryInfo;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Map<String, Object> getCpuInfo() {
        return cpuInfo;
    }

    public void setCpuInfo(Map<String, Object> cpuInfo) {
        this.cpuInfo = cpuInfo;
    }

    public Map<String, Object> getMemoryInfo() {
        return memoryInfo;
    }

    public void setMemoryInfo(Map<String, Object> memoryInfo) {
        this.memoryInfo = memoryInfo;
    }
}
