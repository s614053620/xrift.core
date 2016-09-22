package sunkey.frameworks.xrift.invoke;

public class ProtocolResult implements InvokeResult {

  private final long invokeId;
  private Throwable exception;
  private Object result;
  private ProtocolInvoke invoke;
  
  public ProtocolResult(long invokeId, Throwable exception){
    this.exception = exception;
    this.invokeId = invokeId;
  }
  
  public ProtocolResult(long invokeId, Object result){
    this.result = result;
    this.invokeId = invokeId;
  }
  
  public long getRequestId() {
    return invokeId;
  }

  public Throwable getException() {
    return exception;
  }

  public Object getResult() {
    return result;
  }

  public Invoke getInvoke() {
    return invoke;
  }

  public void setInvoke(ProtocolInvoke invoke) {
    this.invoke = invoke;
  }

}
