package sunkey.frameworks.xrift.invoke.support;

import java.util.Arrays;

import org.apache.thrift.protocol.TProtocol;

import sunkey.frameworks.xrift.meta.ServiceName;

public class InvokeImpl implements ModifiableProtocolInvoke {

  private TProtocol protocol;
  private ServiceName serviceName;
  private String serviceVersion;
  private Object[] invokeParams;
  private Class<?> returnType;
  private Object invokeTarget;
  private long invokeId;
  public TProtocol getProtocol() {
    return protocol;
  }
  public void setProtocol(TProtocol protocol) {
    this.protocol = protocol;
  }
  public ServiceName getServiceName() {
    return serviceName;
  }
  public void setServiceName(ServiceName serviceName) {
    this.serviceName = serviceName;
  }
  public String getServiceVersion() {
    return serviceVersion;
  }
  public void setServiceVersion(String serviceVersion) {
    this.serviceVersion = serviceVersion;
  }
  public Object[] getInvokeParams() {
    return invokeParams;
  }
  public void setInvokeParams(Object[] invokeParams) {
    this.invokeParams = invokeParams;
  }
  public Class<?> getReturnType() {
    return returnType;
  }
  public void setReturnType(Class<?> returnType) {
    this.returnType = returnType;
  }
  public Object getInvokeTarget() {
    return invokeTarget;
  }
  public void setInvokeTarget(Object invokeTarget) {
    this.invokeTarget = invokeTarget;
  }
  public long getInvokeId() {
    return invokeId;
  }
  public void setInvokeId(long invokeId) {
    this.invokeId = invokeId;
  }
  @Override
  public String toString() {
    return "InvokeImpl [protocol=" + protocol + ", serviceName=" + serviceName + ", serviceVersion="
        + serviceVersion + ", invokeParams=" + Arrays.toString(invokeParams) + ", returnType="
        + returnType + ", invokeTarget=" + invokeTarget + ", invokeId=" + invokeId + "]";
  }
  
}
