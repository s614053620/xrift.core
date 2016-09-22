package sunkey.frameworks.xrift.filter;

import sunkey.frameworks.xrift.invoke.ProtocolInvoke;

public interface ProtocolInvokeFilter extends TypedFilter<ProtocolInvoke> {

  ProtocolInvoke doFilter(ProtocolInvoke invoke) throws Exception;
  
}
