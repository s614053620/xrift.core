package test.sunkey.frameworks.xrift.services;

public class WeixinMpBindingT {

  public int id; // required
  public long hotelid; // required
  public String wxid; // required
  public String accessToken; // required
  public String refreshAccessToken; // required
  public String userName; // required
  public String nickName; // required
  public String qrcodeUrl; // required
  public String headImg; // required
  public long createtime; // required
  public long modifytime; // required
  public byte state; // required
  public String ext1; // required
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public long getHotelid() {
    return hotelid;
  }
  public void setHotelid(long hotelid) {
    this.hotelid = hotelid;
  }
  public String getWxid() {
    return wxid;
  }
  public void setWxid(String wxid) {
    this.wxid = wxid;
  }
  public String getAccessToken() {
    return accessToken;
  }
  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }
  public String getRefreshAccessToken() {
    return refreshAccessToken;
  }
  public void setRefreshAccessToken(String refreshAccessToken) {
    this.refreshAccessToken = refreshAccessToken;
  }
  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public String getNickName() {
    return nickName;
  }
  public void setNickName(String nickName) {
    this.nickName = nickName;
  }
  public String getQrcodeUrl() {
    return qrcodeUrl;
  }
  public void setQrcodeUrl(String qrcodeUrl) {
    this.qrcodeUrl = qrcodeUrl;
  }
  public String getHeadImg() {
    return headImg;
  }
  public void setHeadImg(String headImg) {
    this.headImg = headImg;
  }
  public long getCreatetime() {
    return createtime;
  }
  public void setCreatetime(long createtime) {
    this.createtime = createtime;
  }
  public long getModifytime() {
    return modifytime;
  }
  public void setModifytime(long modifytime) {
    this.modifytime = modifytime;
  }
  public byte getState() {
    return state;
  }
  public void setState(byte state) {
    this.state = state;
  }
  public String getExt1() {
    return ext1;
  }
  public void setExt1(String ext1) {
    this.ext1 = ext1;
  }
  @Override
  public String toString() {
    return "WeixinMpBindingT [id=" + id + ", hotelid=" + hotelid + ", wxid=" + wxid
        + ", accessToken=" + accessToken + ", refreshAccessToken=" + refreshAccessToken
        + ", userName=" + userName + ", nickName=" + nickName + ", qrcodeUrl=" + qrcodeUrl
        + ", headImg=" + headImg + ", createtime=" + createtime + ", modifytime=" + modifytime
        + ", state=" + state + ", ext1=" + ext1 + "]";
  }
  
  
  
}
