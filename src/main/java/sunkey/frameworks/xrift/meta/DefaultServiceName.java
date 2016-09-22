package sunkey.frameworks.xrift.meta;

public class DefaultServiceName implements ServiceName {

  private String serviceClass;
  private String methodName;
  private String version;
  private String sign;
  
  public String getServiceClass() {
    return serviceClass;
  }
  public void setServiceClass(String serviceClass) {
    this.serviceClass = serviceClass;
  }
  public String getMethodName() {
    return methodName;
  }
  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }
  public String getVersion() {
    return version;
  }
  public void setVersion(String version) {
    this.version = version;
  }
  public String getSign() {
    return sign;
  }
  public void setSign(String sign) {
    this.sign = sign;
  }
  @Override
  public String toString() {
    return "DefaultServiceName [serviceClass=" + serviceClass + ", methodName=" + methodName
        + ", version=" + version + ", sign=" + sign + "]";
  }
  
}
