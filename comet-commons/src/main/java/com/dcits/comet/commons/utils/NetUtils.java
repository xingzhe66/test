package com.dcits.comet.commons.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @ClassName NetUtils
 * @Author leijian
 * @Date 2019/3/28 9:12
 * @Description TODO
 * @Version 1.0
 **/
public class NetUtils {

    public static InetAddress localAddress;
    
    static {
        try {
            localAddress = getLocalInetAddress();
        } catch (SocketException e) {
            throw new RuntimeException("fail to get local ip.");
        }
    }

    /**  
    * @Author leijian
    * @Description //TODO   
    * @Date 2019/3/28 9:18
    * @Param []  
    * @return java.net.InetAddress  
    **/ 
    public static InetAddress getLocalInetAddress() throws SocketException {
        // enumerates all network interfaces
        Enumeration<NetworkInterface> enu = NetworkInterface.getNetworkInterfaces();

        while (enu.hasMoreElements()) {
            NetworkInterface ni = enu.nextElement();
            if (ni.isLoopback()) {
                continue;
            }

            Enumeration<InetAddress> addressEnumeration = ni.getInetAddresses();
            while (addressEnumeration.hasMoreElements()) {
                InetAddress address = addressEnumeration.nextElement();

                // ignores all invalidated addresses
                if (address.isLinkLocalAddress() || address.isLoopbackAddress() || address.isAnyLocalAddress()) {
                    continue;
                }

                return address;
            }
        }

        throw new RuntimeException("No validated local address!");
    }

    /**  
    * @Author leijian  
    * @Description //TODO   
    * @Date 2019/3/28 9:13  
    * @Param []  
    * @return java.lang.String  
    **/ 
    public static String getLocalAddress() {
        return localAddress.getHostAddress();
    }
}
