package com.vcom.publiclibrary.model;

import java.io.Serializable;
import java.util.Date;

public class LocationModel implements Serializable {
    private String clientId;
    private Double latitude;    //获取纬度
    private Double longitude;     //获取经度
    private Float accuracy;//获取精度信息
    private Double altitude;    //获取海拔高度信息
    private Float speed;   //速度 单位：米/秒
    private Float bearing;    //方向角信息
    private String province;  //省信息
    private String city;     //城市信息
    private String district;   //城区信息
    private String cityCode;  //城市编码
    private String adCode;    //地区编码
    private String address;   //地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
    private String country;  //国家信息
    private String road;
    private String poiName;
    private String street;   //街道信息
    private String streetNum;  //街道门牌号信息
    private String aoiName;     //获取当前定位点的AOI信息
    private String buildingId;//获取当前室内定位的建筑物Id
    private String poiid;
    private String floor;        //获取当前室内定位的楼层
    private Integer gpsAccuracyStatus;
    private String errorCode;
    private String errorInfo;
    private String locationDetail;
    private String description;
    private Integer locationType;     //获取当前定位结果来源，如网络定位结果，详见定位类型表
    private Long time;  //获取定位时间
    private Date dateTime;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Float accuracy) {
        this.accuracy = accuracy;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public Float getBearing() {
        return bearing;
    }

    public void setBearing(Float bearing) {
        this.bearing = bearing;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getPoiName() {
        return poiName;
    }

    public void setPoiName(String poiName) {
        this.poiName = poiName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNum() {
        return streetNum;
    }

    public void setStreetNum(String streetNum) {
        this.streetNum = streetNum;
    }

    public String getAoiName() {
        return aoiName;
    }

    public void setAoiName(String aoiName) {
        this.aoiName = aoiName;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getPoiid() {
        return poiid;
    }

    public void setPoiid(String poiid) {
        this.poiid = poiid;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public Integer getGpsAccuracyStatus() {
        return gpsAccuracyStatus;
    }

    public void setGpsAccuracyStatus(Integer gpsAccuracyStatus) {
        this.gpsAccuracyStatus = gpsAccuracyStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getLocationDetail() {
        return locationDetail;
    }

    public void setLocationDetail(String locationDetail) {
        this.locationDetail = locationDetail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLocationType() {
        return locationType;
    }

    public void setLocationType(Integer locationType) {
        this.locationType = locationType;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Date getDateTime() {
        Date date = new Date(this.time);
        return date;
    }
}
