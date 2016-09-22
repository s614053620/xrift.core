package sunkey.frameworks.xrift.invoke;

public interface InvokeResult {

  long getRequestId();
  
  Throwable getException();
  
  Object getResult();
  
  Invoke getInvoke();
  
}
