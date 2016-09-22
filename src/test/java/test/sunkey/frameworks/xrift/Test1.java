package test.sunkey.frameworks.xrift;

import java.util.ArrayList;
import java.util.Arrays;

import sunkey.frameworks.xrift.context.SimpleInvokeContext;
import sunkey.frameworks.xrift.meta.support.InvocableService;

public class Test1 {

  public static void main(String[] args) {
    SimpleInvokeContext ctx = new SimpleInvokeContext();
    InvocableService<DemoI> serv = ctx.registerService("sunkey.learn.thrift.DemoService", DemoI.class);
    Request r = new Request();
    r.setData("TEST_REQ_XRIFT");
    Response res = serv.getServiceObject().demo(r);
    System.err.println("RESULT:" + res);
   
    serv.getServiceObject().ttt(new ArrayList<>(Arrays.asList("123","1234")));
    
    System.err.println(serv.getServiceObject().mm(123));;
  }
  
}
