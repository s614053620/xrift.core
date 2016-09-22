package sunkey.frameworks.xrift.pool.support;

import sunkey.frameworks.xrift.pool.AutoReleaseFixedObjectPool;
import sunkey.frameworks.xrift.pool.ObjectPool;
import sunkey.frameworks.xrift.protocol.XProtocol;
import sunkey.frameworks.xrift.protocol.provider.ProtocolProvider;

public class GenericProtocolPool implements ObjectPool<XProtocol> {

  private final ProtocolProvider provider;
  private final InnerPool targetPool;

  public GenericProtocolPool(Args arg) {
    targetPool = new InnerPool();
    this.provider = arg.getProvider();
  }

  @Override
  public XProtocol getObject() {
    return targetPool.getResource();
  }

  @Override
  public void returnObject(XProtocol t) {
    targetPool.returnResource(t);
  }
  
  public static class Args {
    private ProtocolProvider provider;
    private long avaliableTime = AutoReleaseFixedObjectPool.DEFAULT_AVALIABLE_TIME;
    private int capacity = AutoReleaseFixedObjectPool.DEFAULT_CAPACITY;
    private boolean fair = AutoReleaseFixedObjectPool.DEFAULT_FAIR;

    public ProtocolProvider getProvider() {
      return provider;
    }

    public void setProvider(ProtocolProvider provider) {
      this.provider = provider;
    }

    public long getAvaliableTime() {
      return avaliableTime;
    }

    public void setAvaliableTime(long avaliableTime) {
      this.avaliableTime = avaliableTime;
    }

    public int getCapacity() {
      return capacity;
    }

    public void setCapacity(int capacity) {
      this.capacity = capacity;
    }

    public boolean isFair() {
      return fair;
    }

    public void setFair(boolean fair) {
      this.fair = fair;
    }
  }

  private class InnerPool extends AutoReleaseFixedObjectPool<XProtocol>{
    @Override
    protected XProtocol initResource() {
      return XProtocol.create(provider);
    }
  }

}
