package sunkey.frameworks.xrift.pool.support;

import sunkey.frameworks.xrift.common.Ordered;
import sunkey.frameworks.xrift.filter.TypedFilter;
import sunkey.frameworks.xrift.invoke.InvokeResult;
import sunkey.frameworks.xrift.invoke.ProtocolInvoke;
import sunkey.frameworks.xrift.invoke.support.InvokePostProcessor;
import sunkey.frameworks.xrift.invoke.support.ModifiableProtocolInvoke;
import sunkey.frameworks.xrift.pool.support.GenericProtocolPool;
import sunkey.frameworks.xrift.protocol.XProtocol;

public class PooledProtocolFilter
    implements
      TypedFilter<ProtocolInvoke>,
      Ordered,
      InvokePostProcessor {

  private final GenericProtocolPool pool;
  private final int priority;

  public PooledProtocolFilter(GenericProtocolPool pool) {
    this(pool, MAX_PRIORITY);
  }

  public PooledProtocolFilter(GenericProtocolPool pool, int priority) {
    this.pool = pool;
    this.priority = priority;
  }

  @Override
  public ProtocolInvoke doFilter(ProtocolInvoke t) throws Exception {
    if (t instanceof ModifiableProtocolInvoke) {
      ModifiableProtocolInvoke m_ = ((ModifiableProtocolInvoke) t);
      if (m_.getProtocol() instanceof XProtocol) {
        // ignore
      } else {
        m_.setProtocol(pool.getObject());
      }
    }
    return t;
  }

  @Override
  public Class<ProtocolInvoke> getFilterType() {
    return ProtocolInvoke.class;
  }

  @Override
  public int getOrder() {
    return priority;
  }

  @Override
  public void preProcessInvoke(ProtocolInvoke invoke, Object invokeContext) {
    // don't do anything.
  }

  @Override
  public void postProcessInvoke(ProtocolInvoke invoke, Object invokeContext, InvokeResult result) {
    if (invoke.getProtocol() instanceof XProtocol) {
      pool.returnObject((XProtocol) invoke.getProtocol());
    }
  }

}
