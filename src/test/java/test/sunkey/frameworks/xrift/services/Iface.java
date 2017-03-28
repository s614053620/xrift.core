package test.sunkey.frameworks.xrift.services;
public interface Iface { 

    public WeixinMpBindingT getWeixinMpBindingByHotelId(long hotelId);

    public int save(WeixinMpBindingT bean);

    public int updateAccessToken(long hotelId, String accessToken);

    public String getUrl(long hotelId, byte type, String tableNum);

    public int saveUrl(long hotelId, byte type, String tableNum, String url);

    public int clearQrCodes(long hotelId);

  }