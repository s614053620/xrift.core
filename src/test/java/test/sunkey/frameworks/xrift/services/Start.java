package test.sunkey.frameworks.xrift.services;

import sunkey.frameworks.xrift.context.SimpleInvokeContext;
import sunkey.frameworks.xrift.meta.support.InvocableService;

public class Start {

  public static void main(String[] args) {
    SimpleInvokeContext ctx = new SimpleInvokeContext();
    InvocableService<Iface> serv = ctx.registerService("cn.px.om.thrift.service.weixin.WeixinMpTService", Iface.class);
    Iface s = serv.getService();
    
    
    
    WeixinMpBindingT res = s.getWeixinMpBindingByHotelId(300000091L);
    System.out.println(res);
    s.updateAccessToken(300000091L, "NEW_ACCESS_TOKEN");
  }
  
}
