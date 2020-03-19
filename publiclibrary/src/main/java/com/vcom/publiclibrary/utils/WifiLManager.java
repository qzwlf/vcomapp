package com.vcom.publiclibrary.utils;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;

import com.vcom.publiclibrary.model.ClientScanResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 作者：chenZY
 * 时间：2018/4/3 15:06
 * 描述：https://www.jianshu.com/u/9df45b87cfdf
 * https://github.com/leavesC
 */
public class WifiLManager {

    /**
     * 开启Wifi
     *
     * @param context 上下文
     * @return 是否成功
     */
    public static boolean openWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifiManager != null && (wifiManager.isWifiEnabled() || wifiManager.setWifiEnabled(true));
    }

    /**
     * Wifi是否已开启
     *
     * @param context 上下文
     * @return 开关
     */
    public static boolean isWifiEnabled(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifiManager != null && wifiManager.isWifiEnabled();
    }

    /**
     * 开启Wifi扫描
     *
     * @param context 上下文
     */
    public static void startWifiScan(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            return;
        }
        ApManager.closeAp(context);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        wifiManager.startScan();
    }

    /**
     * 开启Wifi扫描
     *
     * @param context 上下文
     */
    public static List<ScanResult> getScanResults(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            return null;
        }
        return wifiManager.getScanResults();
    }

    /**
     * 关闭Wifi
     *
     * @param context 上下文
     */
    public static void closeWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null && wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }

    /**
     * 连接指定Wifi
     *
     * @param context  上下文
     * @param ssid     SSID
     * @param password 密码
     * @return 是否连接成功
     */
    public static boolean connectWifi(Context context, String ssid, String password) {
        String connectedSsid = getConnectedSSID(context);
        if (!TextUtils.isEmpty(connectedSsid) && connectedSsid.equals(ssid)) {
            return true;
        }
        ApManager.closeAp(context);
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            return false;
        }
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiConfiguration wifiConfiguration = createWifiConfiguration(ssid, password);
        int networkId = wifiManager.addNetwork(wifiConfiguration);
        return wifiManager.enableNetwork(networkId, true);
    }

    /**
     * 断开Wifi连接
     *
     * @param context 上下文
     */
    public static void disconnectWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null && wifiManager.isWifiEnabled()) {
            wifiManager.disconnect();
        }
    }

    /**
     * 获取当前连接的Wifi的SSID
     *
     * @param context 上下文
     * @return SSID
     */
    public static String getConnectedSSID(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager == null ? null : wifiManager.getConnectionInfo();
        return wifiInfo != null ? wifiInfo.getSSID().replaceAll("\"", "") : "";
    }

    /**
     * 获取连接的Wifi热点的IP地址
     *
     * @param context 上下文
     * @return IP地址
     */
    public static String getHotspotIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiinfo = wifiManager == null ? null : wifiManager.getConnectionInfo();
        if (wifiinfo != null) {
            DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
            if (dhcpInfo != null) {
                int address = dhcpInfo.gateway;
                return ((address & 0xFF)
                        + "." + ((address >> 8) & 0xFF)
                        + "." + ((address >> 16) & 0xFF)
                        + "." + ((address >> 24) & 0xFF));
            }
        }
        return "";
    }

    private static ArrayList<String> getConnectedHotIP() {
        ArrayList<String> connectedIP = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(
                    "/proc/net/arp"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitted = line.split(" +");
                if (splitted != null && splitted.length >= 4) {
                    String ip = splitted[0];
                    connectedIP.add(ip);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connectedIP;
    }

    public static List<ClientScanResult> getAllClientIp() {
        ArrayList<String> connectedIP = new ArrayList<String>();
        List<ClientScanResult> list = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(
                    "/proc/net/arp"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitted = line.split(" +");
                if (splitted != null && splitted.length >= 4) {
                    if (!splitted[0].equals("IP")) {
                        ClientScanResult clientScanResult = new ClientScanResult();
                        clientScanResult.setIpAddr(splitted[0]);
                        clientScanResult.setHWAddr(splitted[3]);
                        clientScanResult.setDevice(splitted[5]);
                        clientScanResult.setReachable(splitted[2].equals("0x0"));
                        list.add(clientScanResult);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void clearClient() {
        File file = new File("/proc/net/arp");
        file.delete();
    }

    //输出链接到当前设备的IP地址
    public static String getEnableClientIp() {
        String result = "";
        List<ClientScanResult> list = getAllClientIp();
//        for (ClientScanResult client : list) {
//            if (client.isReachable())
//                result = client.getIpAddr();
//        }
        if (list != null && list.size() > 0) {
            int idx = list.size() - 1;
            result = list.get(idx).getIpAddr();
        }
        return result;
    }

    //输出链接到当前设备的IP地址
    public static String getClientIp(String mac) {
        String result = "";
        List<ClientScanResult> list = getAllClientIp();
        if (list != null && list.size() > 0) {
            result = list.get(0).getIpAddr().replace(":", "");
            for (ClientScanResult cli : list) {
                if (cli.getHWAddr().replace(":", "").equals(mac))
                    result = cli.getIpAddr().replace(":", "");
            }
        }
        return result;
    }

    public static Map<String, String> getClient() {
        Map<String, String> map = new HashMap<>();
        List<ClientScanResult> list = getAllClientIp();
        if (list != null && list.size() > 0) {
            for (ClientScanResult cli : list) {
                map.put(cli.getHWAddr().replace(":", ""), cli.getIpAddr().replace(":", ""));
            }
        }
        return map;
    }

    /**
     * 获取连接Wifi后设备本身的IP地址
     *
     * @param context 上下文
     * @return IP地址
     */
    public static String getLocalIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiinfo = wifiManager == null ? null : wifiManager.getConnectionInfo();
        if (wifiinfo != null) {
            int ipAddress = wifiinfo.getIpAddress();
            return ((ipAddress & 0xFF)
                    + "." + ((ipAddress >> 8) & 0xFF)
                    + "." + ((ipAddress >> 16) & 0xFF)
                    + "." + ((ipAddress >> 24) & 0xFF));
        }
        return "";
    }

    /**
     * 判断本地是否有保存指定Wifi的配置信息（之前是否曾成功连接过该Wifi）
     *
     * @param context 上下文
     * @param ssid    SSID
     * @return Wifi的配置信息
     */
    private static WifiConfiguration isWifiExist(Context context, String ssid) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> wifiConfigurationList = wifiManager == null ? null : wifiManager.getConfiguredNetworks();
        if (wifiConfigurationList != null && wifiConfigurationList.size() > 0) {
            for (WifiConfiguration wifiConfiguration : wifiConfigurationList) {
                if (wifiConfiguration.SSID.equals("\"" + ssid + "\"")) {
                    return wifiConfiguration;
                }
            }
        }
        return null;
    }

    /**
     * 清除指定Wifi的配置信息
     *
     * @param ssid SSID
     */
    public static void cleanWifiInfo(Context context, String ssid) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration wifiConfiguration = isWifiExist(context, ssid);
        if (wifiManager != null && wifiConfiguration != null) {
            wifiManager.removeNetwork(wifiConfiguration.networkId);
        }
    }

    public static void cleanAllWifiInfo(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> wifiConfigurationList = wifiManager == null ? null : wifiManager.getConfiguredNetworks();
        if (wifiConfigurationList != null) {
            for (WifiConfiguration c : wifiConfigurationList) {
                wifiManager.removeNetwork(c.networkId);
            }
        }

    }

    /**
     * 创建Wifi网络配置
     *
     * @param ssid     SSID
     * @param password 密码
     * @return Wifi网络配置
     */
    private static WifiConfiguration createWifiConfiguration(String ssid, String password) {
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.SSID = "\"" + ssid + "\"";
        wifiConfiguration.preSharedKey = "\"" + password + "\"";
        wifiConfiguration.hiddenSSID = true;
        wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        return wifiConfiguration;
    }

    private static String getMacDefault(Context context) {
        String mac = "";
        if (context == null) {
            return mac;
        }
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = null;
        try {
            info = wifi.getConnectionInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (info == null) {
            return null;
        }
        mac = info.getMacAddress();
        if (!TextUtils.isEmpty(mac)) {
            mac = mac.toUpperCase(Locale.ENGLISH);
        }
        return mac;
    }

    private static String getMacAddress() {
        String macSerial = null;
        String str = "";

        try {
            Process pp = Runtime.getRuntime().exec("cat/sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            while (null != str) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();//去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }

        return macSerial;
    }

    /**
     * Android 7.0之后获取Mac地址
     * 遍历循环所有的网络接口，找到接口是 wlan0
     * 必须的权限 <uses-permission android:name="android.permission.INTERNET"></uses-permission>
     *
     * @return
     */
    private static String getMacFromHardware() {
        try {
            List<NetworkInterface> all =
                    Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equals("wlan0"))
                    continue;
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) return "";
                StringBuilder res1 = new StringBuilder();
                for (Byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }
                if (!TextUtils.isEmpty(res1)) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getMac(Context context) {
        String mac = "";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mac = getMacDefault(context);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mac = getMacAddress();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mac = getMacFromHardware();
        }
        return mac;
    }

}
