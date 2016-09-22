package sunkey.frameworks.xrift.utils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.thrift.protocol.TField;
import org.apache.thrift.protocol.TList;
import org.apache.thrift.protocol.TMap;
import org.apache.thrift.protocol.TMessage;
import org.apache.thrift.protocol.TMessageType;
import org.apache.thrift.protocol.TSet;
import org.apache.thrift.protocol.TStruct;
import org.apache.thrift.protocol.TType;
import org.springframework.core.ResolvableType;

public class THelper {

  private static final Map<Byte, String> typeNames;
  private static final Map<Byte, String> messageTypeNames;

  public static String getTypeName(byte tType) {
    return typeNames.get(tType);
  }

  public static String getMessageTypeName(byte tMessageType) {
    return messageTypeNames.get(tMessageType);
  }

  static {
    typeNames = loadTypeNames();
    messageTypeNames = loadMessageTypeNames();
  }

  private static Map<Byte, String> loadMessageTypeNames() {
    Map<Byte, String> typeNames = new HashMap<>();
    try {
      for (Field f : TMessageType.class.getFields()) {
        typeNames.put((Byte) f.get(null), f.getName());
      }
    } catch (Exception e) {}
    return typeNames;
  }

  private static Map<Byte, String> loadTypeNames() {
    Map<Byte, String> typeNames = new HashMap<>();
    try {
      for (Field f : TType.class.getFields()) {
        typeNames.put((Byte) f.get(null), f.getName());
      }
    } catch (Exception e) {}
    return typeNames;
  }

  // STATIC TO_STRING METHODS

  public static String toString(TMessage msg) {
    return "TMessage[name=" + msg.name + ",type=" + THelper.getMessageTypeName(msg.type) + ",seq="
        + msg.seqid + "]";
  }

  public static String toString(TStruct msg) {
    return "TStruct[name=" + msg.name + "]";
  }

  public static String toString(TField msg) {
    return "TField[name=" + msg.name + ",type=" + THelper.getTypeName(msg.type) + ",id=" + msg.id
        + "]";
  }

  public static String toString(TMap msg) {
    return "TMap[size=" + msg.size + ",keyType=" + getTypeName(msg.keyType) + ",valueType="
        + getTypeName(msg.valueType) + "]";
  }

  public static String toString(TSet msg) {
    return "TSet[size=" + msg.size + ",elemType=" + getTypeName(msg.elemType) + "]";
  }

  public static String toString(TList msg) {
    return "TList[size=" + msg.size + ",elemType=" + getTypeName(msg.elemType) + "]";
  }

  // GENERIC TYPE METHODS
  
  public static Class<?>[] getGenericType(Class<?> type, int length, Class<?> def_){
    ResolvableType[] ges = ResolvableType.forClass(type).getGenerics();
    Class<?>[] res_ = new Class<?>[length];
    for(int i = 0; i < length; i ++){
      if(ges.length > i){
        res_[i] = ges[i].resolve(def_);
      }else{
        res_[i] = def_;
      }
    }
    return res_;
  }
  
  public static Class<?> getCollectionGenericType(Collection<?> coll){
    Class<?> res_ = getGenericType(coll.getClass(), 1, null)[0];
    if(res_ == null && coll != null && !coll.isEmpty()){
      Object o1 = coll.iterator().next();
      if(o1 != null){
        res_ = o1.getClass();
      }
    }
    return ifNull(res_, Object.class);
  }
  
  public static Class<?>[] getMapGenericTypes(Map<?, ?> map){
    Class<?>[] types = getGenericType(map.getClass(), 2, null);
    if(map == null || map.isEmpty()){
      return ifNull(types, Object.class);
    }
    Map.Entry<?, ?> e = map.entrySet().iterator().next();
    if(types[0] == null && e != null && e.getKey() != null){
      types[0] = e.getKey().getClass();
    }
    if(types[1] == null && e != null && e.getValue() != null){
      types[1] = e.getValue().getClass();
    }
    return ifNull(types, Object.class);
  }
  
  // OBJECT UTILS
  
  public static <T> T[] ifNull(T[] tarr, T def){
    if(tarr != null)
      for(int i = 0; i < tarr.length; i ++)
        tarr[i] = ifNull(tarr[i], def);
    return tarr;
  }
  
  public static <T> T ifNull(T t, T def){
    if(t == null)
      return def;
    return t;
  }
  
  // THRIFTS
  
  public static byte forThriftType(Class<?> type){
    if(type == Integer.class || type == Integer.TYPE)
      return TType.I32;
    if(type == Short.class || type == Short.TYPE)
      return TType.I16;
    if(type == Boolean.class || type == Boolean.TYPE)
      return TType.BOOL;
    if(type == Byte.class || type == Byte.TYPE)
      return TType.BYTE;
    if(type == Double.class || type == Double.TYPE)
      return TType.DOUBLE;
    if(type.isEnum() || Enum.class.isAssignableFrom(type))
      return TType.ENUM;
    if(type == Long.class || type == Short.TYPE)
      return TType.I64;
    if(List.class.isAssignableFrom(type))
      return TType.LIST;
    if(Map.class.isAssignableFrom(type))
      return TType.MAP;
    if(Set.class.isAssignableFrom(type))
      return TType.SET;
    if(type == String.class)
      return TType.STRING;
    if(type == Void.class || type == Void.TYPE)
      return TType.VOID;
    return TType.STRUCT;
  }
  
}
