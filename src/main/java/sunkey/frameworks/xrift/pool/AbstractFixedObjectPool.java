package sunkey.frameworks.xrift.pool;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * 
 * @author Sunkey(sunzhiwei)
 * @email 614053620@qq.com;s614053620@gmail.com
 * @github s614053620
 * @date 2016年9月21日 下午7:24:26
 *
 */
public abstract class AbstractFixedObjectPool<T> extends AbstractObjectPool<T> {

  public static final int DEFAULT_CAPACITY = 16;
  public static final boolean DEFAULT_FAIR = true;

  private static final Logger loger = LoggerFactory.getLogger(AbstractFixedObjectPool.class);
  
  private final int capacity;

  private Semaphore lock;

  public AbstractFixedObjectPool() {
    this(DEFAULT_CAPACITY);
  }

  public AbstractFixedObjectPool(int capacity) {
    this(capacity, DEFAULT_FAIR);
  }

  public AbstractFixedObjectPool(int capacity, boolean fair) {
    this.capacity = capacity;
    lock = new Semaphore(capacity, fair);
    loger.info("fixed pool started. with capacity:{}, fair:{}", capacity, fair);
  }

  public int getCapacity() {
    return capacity;
  }

  public int size() {
    return queue.size();
  }

  /**
   * 阻塞调用
   */
  @Override
  public T getObject() {
    try {
      lock.acquire();
    } catch (InterruptedException e) {
      loger.error("interrupted.");
      return null;
    }
    return super.getObject();
  }

  /**
   * 在timeout内等待（阻塞），超过等待时间返回
   * 
   * @param timeout
   * @param unit
   * @return
   */
  public T getObject(long timeout, TimeUnit unit) {
    try {
      boolean res = lock.tryAcquire(timeout, unit);
      if (res) return super.getObject();
    } catch (InterruptedException e) {
      loger.error("interrupted.");
    }
    return null;
  }

  /**
   * 如果无资源可以获取，立即返回空
   * @return
   */
  public T tryGetObject() {
    boolean res = lock.tryAcquire();
    if (res) return super.getObject();
    return null;
  }
  /**
   * 回收资源
   */
  @Override
  public void returnObject(T t) {
    super.returnObject(t);
    lock.release();
  }

  protected abstract T init();

}
