package sunkey.frameworks.xrift.meta;

import org.apache.thrift.protocol.TProtocol;

public class DefaultServiceMeta implements ProtocolServiceMeta {

  private ServiceName serviceName;
  private String serverHost;
  private int serverPort;
  private String version;
  private TProtocol protocol;
  
  public ServiceName getServiceName() {
    return serviceName;
  }
  public void setServiceName(ServiceName serviceName) {
    this.serviceName = serviceName;
  }
  public String getServerHost() {
    return serverHost;
  }
  public void setServerHost(String serverHost) {
    this.serverHost = serverHost;
  }
  public int getServerPort() {
    return serverPort;
  }
  public void setServerPort(int serverPort) {
    this.serverPort = serverPort;
  }
  public String getServiceVersion() {
    return version;
  }
  public void setServiceVersion(String version) {
    this.version = version;
  }
  public TProtocol getProtocol() {
    return protocol;
  }
  public void setProtocol(TProtocol protocol) {
    this.protocol = protocol;
  }
  @Override
  public String toString() {
    return "DefaultServiceMeta [serviceName=" + serviceName + ", serverHost=" + serverHost
        + ", serverPort=" + serverPort + ", version=" + version + "]";
  }
 
}
