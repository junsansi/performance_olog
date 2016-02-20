/**
 * RequestUtil.java
 * User: junsansi
 * Date: 2015-1-9
 */
package com.jss.module.performance.olog.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author junsansi Date: 2015-1-9
 */
public class IpAddrUtil {

	private static final Logger logger = LoggerFactory.getLogger(IpAddrUtil.class);
	
	public static String getRequestIpAddr(HttpServletRequest request) {

		String loginIp = request.getHeader("X-Forwarded-For");
		if (null == loginIp || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp))
			loginIp = request.getHeader("Proxy-Client-lastLoginIp");
		if (loginIp == null || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp)) 
			loginIp = request.getHeader("Proxy-Client-IP");
		if (loginIp == null || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp)) 
			loginIp = request.getHeader("WL-Proxy-Client-IP");
		if (loginIp == null || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp))
			loginIp = request.getHeader("WL-Proxy-Client-lastLoginIp");
		if (loginIp == null || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp))
			loginIp = request.getHeader("HTTP_CLIENT_lastLoginIp");
		if (loginIp == null || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp))
			loginIp = request.getHeader("HTTP_X_FORWARDED_FOR");
		if (loginIp == null || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp)) 
			loginIp = request.getRemoteAddr();
	    if ("127.0.0.1".equals(loginIp) || "0:0:0:0:0:0:0:1".equals(loginIp))
	        try {
	        	loginIp = InetAddress.getLocalHost().getHostAddress();
	        }
	        	catch (UnknownHostException unknownhostexception) {
	        }
		return loginIp;
	}

	public static String getLocalIp() {  
        String localip = "127.0.0.1";// 本地IP，如果没有配置外网IP则返回它  
        String netip = "";// 外网IP  
        try {  
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface  
                    .getNetworkInterfaces();  
            InetAddress ip = null;  
            boolean finded = false;// 是否找到外网IP  
            while (netInterfaces.hasMoreElements() && !finded) {  
                NetworkInterface ni = netInterfaces.nextElement();  
                Enumeration<InetAddress> address = ni.getInetAddresses();  
                while (address.hasMoreElements()) {  
                    ip = address.nextElement();  
                    if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {// 外网IP  
                        netip = ip.getHostAddress();  
                        finded = true;  
                        break;  
                    } else if (ip.isSiteLocalAddress()  && !ip.isLoopbackAddress()  && ip.getHostAddress().indexOf(":") == -1) {// 内网IP  
                        localip = ip.getHostAddress();
                        finded = true;
                        break;
                    } 
                }  
            }  
        } catch (SocketException e) {  
        	logger.warn("Get local IpAddr failed.", e);
        }  
        if (StringUtils.isNotBlank(netip)) {  
            return netip;  
        } else {  
            return localip;  
        }  
    }  
	
}
