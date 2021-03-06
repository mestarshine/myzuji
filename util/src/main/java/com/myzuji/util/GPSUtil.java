package com.myzuji.util;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

/**
 * 经纬度计算工具类
 */
public class GPSUtil {

    /**
     * 长半径a=6378137  赤道半径(单位m)
     */
    private static final double EQUATORIAL_RADIUS = 6378137;


    public static void main(String[] args) throws Exception {

        double meter1 = GetDistance(111.401208, 41.23753, 111.361826, 41.248429);
        System.out.println("计算结果：" + meter1 + "米");

        for (int i = 0; i < 1000; i++) {
            double lon1 = 111.401208 + i;
            double lon2 = 111.3672456137661 + i;
            double meter2 = GetDistance(lon1, 41.23753, lon2, 41.21683429415043);
            System.out.println("计算结果I：" + meter2 + "米");

        }


        Map<String, Double> objMapList = getPeopleNearby(111.401208, 41.23753, 3939.6726485588797);
        System.out.println("计算结果：" + JSON.toJSONString(objMapList));

    }

    /**
     * 转化为弧度(rad)
     */
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     *  
     * 弧度换成度  
     *
     * @param x 弧度 
     * @return 度 
     */
    private static double deg(double x) {
        return x * 180 / Math.PI;
    }

    /**
     * 基于googleMap中的算法得到两经纬度之间的距离,计算精度与谷歌地图的距离精度差不多，相差范围在0.2米以下（单位m）
     *
     * @param lon1 第一点的经度
     * @param lat1 第一点的纬度
     * @param lon2 第二点的经度
     * @param lat2 第二点的纬度
     * @return 返回的距离，单位m
     */
    public static double GetDistance(double lon1, double lat1, double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(
            Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EQUATORIAL_RADIUS;
        return s;
    }

    /**
     * 范围 range 内的经纬度
     *
     * @param longitude 第一点的经度
     * @param latitude  第一点的纬度
     * @param range     间距范围单位米
     * @return
     */
    public static Map<String, Double> getPeopleNearby(double longitude, double latitude, double range) {
        //地球半径 千米
        double r = EQUATORIAL_RADIUS / 1000;
        //0.5千米距离
        range = range / 1000;

        double longitudeDeg = Math.asin(Math.sin(range / (2 * r)) / Math.cos(rad(longitude)));
        //角度转为弧度
        longitudeDeg = deg(longitudeDeg);

        double latitudeDeg = Math.asin(Math.sin(range / (2 * r)) / Math.cos(rad(latitude)));
        //角度转为弧度
        latitudeDeg = deg(latitudeDeg);


        double minlong = longitude - longitudeDeg;
        double maxlong = longitude + longitudeDeg;

        double minlat = latitude - latitudeDeg;
        double maxlat = latitude + latitudeDeg;

        HashMap<String, Double> map = new HashMap<>();
        //经度最小
        map.put("minlong", minlong);
        //经度最大
        map.put("maxlong", maxlong);
        //纬度最小
        map.put("minlat", minlat);
        //纬度最大
        map.put("maxlat", maxlat);
        return map;
    }

}
