package sunkey.frameworks.xrift.strategy;

import sunkey.frameworks.xrift.filter.TypedFilter;
import sunkey.frameworks.xrift.invoke.ProtocolInvoke;
import sunkey.frameworks.xrift.invoke.support.InvokePostProcessor;

public interface FailOver extends TypedFilter<ProtocolInvoke>, InvokePostProcessor {

}
