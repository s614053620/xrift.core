package sunkey.frameworks.xrift.pool;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Sunkey(sunzhiwei)
 * @email 614053620@qq.com;s614053620@gmail.com
 * @github s614053620
 * @date 2016年9月21日 下午7:24:11
 *
 */
public abstract class AutoReleaseFixedObjectPool<T extends Closeable>
    extends AbstractFixedObjectPool<AutoReleaseFixedObjectPool.ObjectEntity<T>>
    implements
      AutoCloseable {

  public static final long DEFAULT_AVALIABLE_TIME = 10 * 60 * 1000L;// 10min
  public static final long DEFAULT_WATCHING_INTERVAL = 1 * 60 * 1000L;// 1min

  private static final Logger loger = LoggerFactory.getLogger(AutoReleaseFixedObjectPool.class);
  
  protected volatile boolean closing = false;
  protected Map<T, ObjectEntity<T>> mgrBeans = new HashMap<>();

  private final long avaliableTime;
  protected WatchingService watchingService;
  private long lastInterval = 0;

  public AutoReleaseFixedObjectPool() {
    this(DEFAULT_AVALIABLE_TIME);
  }

  public AutoReleaseFixedObjectPool(long avaliableTime) {
    this(avaliableTime, DEFAULT_CAPACITY);
  }

  public AutoReleaseFixedObjectPool(long avaliableTime, int capacity) {
    this(avaliableTime, capacity, DEFAULT_FAIR);
  }

  public AutoReleaseFixedObjectPool(long avaliableTime, int capacity, boolean fair) {
    super(capacity, fair);
    this.avaliableTime = avaliableTime;
    loger.info("auto release pool started. with avaliableTime : {} ms", avaliableTime);
  }

  public synchronized final void startWatchResources(long interval){
    if(watchingService == null){
      watchingService = new WatchingService(queue, interval);
      watchingService.start();
      loger.info("start watching resources to auto release. interval={}", interval);
    }else{
      if(lastInterval != interval){
        watchingService.stopWatching();
        watchingService = new WatchingService(queue, interval);
        watchingService.start();
        loger.info("restart watching resources to auto release. interval={}", interval);
      }else{
        loger.info("try to restart watching service, but interval not changed, ignore.");
      }
    }
    lastInterval = interval;
  }

  @Override
  public ObjectEntity<T> getObject() {
    if (!closing){
      return super.getObject();
    }
    throw new IllegalStateException("closing");
  }

  public T getResource() {
    ObjectEntity<T> entity = getObject();
    mgrBeans.put(entity.object, entity);
    return entity.object;
  }

  public void returnResource(T resource) {
    returnObject(mgrBeans.get(resource));
  }

  @Override
  protected ObjectEntity<T> init() {
    return new ObjectEntity<>(initResource(), avaliableTime);
  }

  protected abstract T initResource();

  @Override
  public void close() throws Exception {
    loger.info("closing pool.");
    if (closing) {
      throw new IllegalStateException("closing!");
    }
    closing = true;
    watchingService.stopWatching();
    // close all resources.
    tryCloseAll();
    loger.info("pool closed.");
  }

  protected void tryCloseAll() throws Exception {
    for (ObjectEntity<T> c = queue.poll(); c != null; c = queue.poll()) {
      c.object.close();
    }
  }

  public static class ObjectEntity<T extends Closeable> {
    final T object;
    final long avaliableTime;
    private long saveTime;

    ObjectEntity(T object, long avaliableTime) {
      this.object = object;
      this.avaliableTime = avaliableTime;
      this.saveTime = System.currentTimeMillis();
    }

    boolean timeout() {
      return System.currentTimeMillis() - avaliableTime > saveTime;
    }
  }

  protected class WatchingService extends Thread {
    private final long interval;
    private boolean running = true;
    private boolean fast = false;
    private final Queue<ObjectEntity<T>> queue;

    public WatchingService(Queue<ObjectEntity<T>> queue, long interval) {
      this.interval = interval;
      this.queue = queue;
    }

    public void useFastMode() {
      fast = true;
    }

    @Override
    public void run() {
      if (fast) {
        while (running) {
          while (running) {
            ObjectEntity<T> entity = queue.peek();
            if (entity != null && entity.timeout()){ 
              queue.poll();
              // release the lock.
              returnObject(null);
              loger.debug("resource auto closed.");
            }
          }
          sleep0(interval);
        }
      } else {
        while (running) {
          ObjectEntity<T> entity = queue.peek();
          if (entity != null && entity.timeout()){ 
            queue.poll();
            // release the lock.
            returnObject(null);
            loger.debug("resource auto closed.");
          }
          sleep0(interval);
        }
      }
    }

    public void stopWatching() {
      running = false;
    }

    private void sleep0(long time) {
      if (time > 1) {
        try {
          Thread.sleep(time);
        } catch (InterruptedException e) {
          throw new IllegalStateException(e);
        }
      }
    }
  }

}
