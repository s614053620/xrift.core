package sunkey.frameworks.xrift.utils;

import java.net.InetSocketAddress;

import org.springframework.util.StringUtils;

import sunkey.frameworks.xrift.common.Address;

public class INetUtils {
  
  public static Address parseAddress(String exp, int defPort){
    if(StringUtils.isEmpty(exp)){
      throw new NullPointerException();
    }
    Address adr = new Address();
    int index = exp.indexOf(':');
    if(index < 0){
      adr.setHost(exp);
      adr.setPort(defPort);
    }else{
      String addr = exp.substring(0, index);
      String port = exp.substring(index + 1);
      adr.setHost(addr);
      try{
        adr.setPort(Integer.valueOf(port));
      }catch (NumberFormatException e) {
        adr.setPort(defPort);
      }
    }
    return adr;
  }
  
  public static Address parseAddress(String exp){
    Address adr = parseAddress(exp, -1);
    if(adr.getPort() == -1)
      throw new IllegalArgumentException(exp);
    return adr;
  }
  
  public static InetSocketAddress parseSocketAddress(String exp, int defPort){
    Address adr = parseAddress(exp, defPort);
    return new InetSocketAddress(adr.getHost(), adr.getPort());
  }
  
  public static InetSocketAddress parseSocketAddress(String exp){
    Address adr = parseAddress(exp);
    return new InetSocketAddress(adr.getHost(), adr.getPort());
  }
  
}
