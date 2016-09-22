package sunkey.frameworks.xrift.invoke.support;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import sunkey.frameworks.xrift.common.Ordered;
import sunkey.frameworks.xrift.invoke.InvokeResult;
import sunkey.frameworks.xrift.invoke.ProtocolInvoke;

public class InvokeProcessorChain implements InvokePostProcessor {

  private final List<InvokePostProcessor> processors;

  public InvokeProcessorChain() {
    this(new LinkedList<InvokePostProcessor>());
  }

  public InvokeProcessorChain(List<InvokePostProcessor> processors) {
    this.processors = processors;
  }

  public void registerProcessor(InvokePostProcessor processor) {
    processors.add(processor);
    Collections.sort(processors, F_C);
  }

  @Override
  public void preProcessInvoke(ProtocolInvoke invoke, Object invokeContext) {
    for (InvokePostProcessor proc : processors) {
      proc.preProcessInvoke(invoke, invokeContext);
    }
  }

  @Override
  public void postProcessInvoke(ProtocolInvoke invoke, Object invokeContext, InvokeResult result) {
    for (InvokePostProcessor proc : processors) {
      proc.postProcessInvoke(invoke, invokeContext, result);
    }
  }

  private static final Comparator<InvokePostProcessor> F_C = new Comparator<InvokePostProcessor>() {
    public int compare(InvokePostProcessor o1, InvokePostProcessor o2) {
      int o1v = Ordered.DEFAULT_PRIORITY;
      int o2v = Ordered.DEFAULT_PRIORITY;
      if (o1 instanceof Ordered) {
        o1v = ((Ordered) o1).getOrder();
      }
      if (o2 instanceof Ordered) {
        o2v = ((Ordered) o2).getOrder();
      }
      return o1v - o2v;
    }
  };

}
