package test.sunkey.frameworks.xrift.c;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sunkey.frameworks.xrift.pool.AbstractFixedObjectPool;
import sunkey.frameworks.xrift.pool.AbstractObjectPool;
import sunkey.frameworks.xrift.pool.AutoReleaseFixedObjectPool;

public class TestPool {

  static int counter = 0;
  
  public static class S1Pool extends AbstractObjectPool<Object>{
    protected Object init() {
      return String.valueOf(counter ++);
    }
  }
  
  public static class S2Pool extends AbstractFixedObjectPool<Object>{
    protected Object init() {
      return String.valueOf(counter ++);
    }
  }
  
  public static class AC implements Closeable{
    private int ii;
    public AC(int i){
      ii=i;
    }
    public void close() throws IOException {
      System.err.println("auto closed " + ii);
    }
    @Override
    public String toString() {
      return "AC [ii=" + ii + "]";
    }
    
  }
  
  public static class S3Pool extends AutoReleaseFixedObjectPool<AC>{
    public S3Pool(){
      super(20000000L, 10, false);
      startWatchResources(50);
    }
    protected AC initResource() {
      return new AC(counter++);
    }
  }
  
  public static void main(String[] args) {
    //test1();
    //test2();
    test3();
  }
  
  public static void test3(){
   final S3Pool s3p = new S3Pool();
   List<AC> ls = new ArrayList<>();
   for(int i=0;i<100;i++){
     AC ac = s3p.getResource();
     System.err.println(ac);
     ls.add(ac);
    
   }
   for(AC ac:ls){
     s3p.returnResource(ac);
     
   }
    /*final List<AC> ls = new ArrayList<>();
    new Thread(new Runnable() {
      public void run() {
        for(int i=0;i<100;i++){
          try {
            Thread.sleep(500);
          } catch (InterruptedException e) {
          }
          s3p.returnResource(ls.get(i));
        }
      }
    }).start(); 
    for(int i=0;i<100;i++){
       AC o1 = s3p.getResource();
       ls.add(o1);
     }*/
  }
  
  
  public static void test2(){
   final S2Pool s2p = new S2Pool();
    for(int i=0;i<100;i++){
      Object o1 = s2p.getObject();
      s2p.returnObject(o1);
    }
   final List<Object> ls = new ArrayList<>();
   new Thread(new Runnable() {
     public void run() {
       for(int i=0;i<100;i++){
         try {
           Thread.sleep(500);
         } catch (InterruptedException e) {
         }
         s2p.returnObject(ls.get(i));
       }
     }
   }).start(); 
   for(int i=0;i<100;i++){
      Object o1 = s2p.getObject();
      ls.add(o1);
    }
    
  }
  
  public static void test1(){
    S1Pool s1p = new S1Pool();
    for(int i=0;i<100;i++){
      Object o1 = s1p.getObject();
      s1p.returnObject(o1);
    }
  }
  
}
