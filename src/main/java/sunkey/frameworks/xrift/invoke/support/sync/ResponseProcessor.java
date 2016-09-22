package sunkey.frameworks.xrift.invoke.support.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TField;
import org.apache.thrift.protocol.TList;
import org.apache.thrift.protocol.TMap;
import org.apache.thrift.protocol.TMessage;
import org.apache.thrift.protocol.TMessageType;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TSet;
import org.apache.thrift.protocol.TStruct;
import org.apache.thrift.protocol.TType;
import org.slf4j.Logger;

import sunkey.frameworks.xrift.utils.JavaBean;
import sunkey.frameworks.xrift.utils.JavaBean.Setter;
import sunkey.frameworks.xrift.utils.THelper;

public class ResponseProcessor {

  private static final Logger loger = org.slf4j.LoggerFactory.getLogger(ResponseProcessor.class);

  private final TProtocol prot;

  public ResponseProcessor(TProtocol prot) {
    this.prot = prot;
  }

  public Object processResult(Class<?> returnType) throws TException {
    Object result_ = null;
    TMessage msg = prot.readMessageBegin();
    String msgType = THelper.getMessageTypeName(msg.type);
    loger.trace("RECV TMessage[name={},type={},seq={}]", msg.name, msgType, msg.seqid);
    switch (msg.type) {
      case TMessageType.REPLY:
        result_ = processReply(returnType);
    }
    loger.trace("readMessageEnd");
    prot.readMessageEnd();
    return result_;
  }

  private boolean shouldStop(TField tf) {
    return tf.type == TType.STOP;
  }

  private Object processReply(Class<?> returnType) throws TException {
    prot.readStructBegin();
    loger.trace("readStructBegin[PARAM]");
    Object result_ = null;
    while (true) {
      TField tf = prot.readFieldBegin();
      loger.trace("readFieldBegin:{}", THelper.toString(tf));
      if (shouldStop(tf)) {
        break;
      }
      result_ = readResult(returnType, tf);
      loger.trace("readFieldEnd:{}", THelper.toString(tf));
      prot.readFieldEnd();
    }
    loger.trace("readStructEnd[PARAM]");
    prot.readStructEnd();
    return result_;
  }

  private Object readResult(Class<?> type, TField reType) throws TException {
    if (type == Void.class || type == Void.TYPE) {
      return null;
    }
    return readField(type, reType.type);
  }

  private Object readField(Class<?> type, byte tType) throws TException {
    switch (tType) {
      case TType.STRUCT:
        return readStruct(type);
      case TType.I64:
        return readPrimitive(Long.TYPE, type);
      case TType.I32:
        return readPrimitive(Integer.TYPE, type);
      case TType.I16:
        return readPrimitive(Short.TYPE, type);
      case TType.STRING:
        return readString(type);
      case TType.DOUBLE:
        return readPrimitive(Double.TYPE, type);
      case TType.BOOL:
        return readPrimitive(Boolean.TYPE, type);
      case TType.BYTE:
        return readPrimitive(Byte.TYPE, type);
      case TType.VOID:
        return null;
      case TType.ENUM:
        return readEnum(type);
      case TType.LIST:
        return readList(type);
      case TType.MAP:
        return readMap(type);
      case TType.SET:
        return readSet(type);
      case TType.STOP:
        return null;
      default:
        return null;
    }
  }

  private Object readPrimitive(Class<?> primType, Class<?> type) throws TException {
    Object result_ = null;
    if (primType == Integer.TYPE)
      result_ = prot.readI32();
    else if (primType == Short.TYPE)
      result_ = prot.readI16();
    else if (primType == Long.TYPE)
      result_ = prot.readI64();
    else if (primType == Byte.TYPE)
      result_ = prot.readByte();
    else if (primType == Boolean.TYPE)
      result_ = prot.readBool();
    else if (primType == Double.TYPE) result_ = prot.readDouble();
    loger.trace("readPrimitive:{}", result_);
    return convert(result_, type);
  }

  private Object readStruct(Class<?> type) throws TException {
    TStruct struct = prot.readStructBegin();
    loger.trace("readStructBegin:{}", THelper.toString(struct));
    JavaBean jb = JavaBean.forType(type);
    Object target = newInstance(type);
    while (true) {
      TField tf_ = prot.readFieldBegin();
      loger.trace("readFieldBegin:{}", THelper.toString(tf_));
      if (shouldStop(tf_)) {
        break;
      }
      Setter setter = jb.setters().get(tf_.id - 1);
      Object field_ = readField(setter.getType(), tf_.type);
      setter.set(target, field_);
      loger.trace("readFieldEnd:{}", THelper.toString(tf_));
      prot.readFieldEnd();
    }
    loger.trace("readStructEnd:{}", THelper.toString(struct));
    prot.readStructEnd();
    return target;
  }

  private Object readMap(Class<?> type) throws TException {
    Map<Object, Object> map = new HashMap<>();
    TMap tmap = prot.readMapBegin();
    loger.trace("readMapBegin:{}", THelper.toString(tmap));
    for (int i = 0; i < tmap.size; i++) {
      Object key = readField(null/* 未知类型 */, tmap.keyType);
      Object val = readField(null/* 未知类型 */, tmap.keyType);
      map.put(key, val);
    }
    loger.trace("readMapEnd:{}", THelper.toString(tmap));
    prot.readMapEnd();
    return convert(map, type);
  }

  private Object readSet(Class<?> type) throws TException {
    Set<Object> set = new HashSet<>();
    TSet tset = prot.readSetBegin();
    loger.trace("readSetBegin:{}", THelper.toString(tset));
    for (int i = 0; i < tset.size; i++) {
      Object val = readField(null/* 未知类型 */, tset.elemType);
      set.add(val);
    }
    loger.trace("readSetEnd:{}", THelper.toString(tset));
    prot.readSetEnd();
    return convert(set, type);
  }

  private Object readList(Class<?> type) throws TException {
    List<Object> list = new ArrayList<>();
    TList tlist = prot.readListBegin();
    loger.trace("readListBegin:{}", THelper.toString(tlist));
    for (int i = 0; i < tlist.size; i++) {
      Object val = readField(null/* 未知类型 */, tlist.elemType);
      list.add(val);
    }
    loger.trace("readListEnd:{}", THelper.toString(tlist));
    prot.readListEnd();
    return convert(list, type);
  }

  private Object readString(Class<?> type) throws TException {
    String str = prot.readString();
    loger.trace("readString:{}", str);
    return convert(str, type);
  }

  private Object readEnum(Class<?> type) {
    throw new IllegalStateException();
  }

  protected Object newInstance(Class<?> type) {
    try {
      return type.newInstance();
    } catch (Exception e) {
      loger.error(e.getMessage(), e);
    }
    return null;
  }

  protected Object convert(Object obj, Class<?> needType) {
    if (needType == null) {

    }
    return obj;
  }

}
