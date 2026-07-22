package com.example.censor;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 脏话自动和谐应用入口。
 * 启动后打印本机局域网访问地址，方便同网段设备访问。
 */
@SpringBootApplication
public class CensorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CensorApplication.class, args);
    }

    /**
     * 启动完成后输出本机与局域网访问地址。
     */
    @Bean
    public ApplicationRunner printAccessUrls() {
        return args -> {
            System.out.println();
            System.out.println("========================================");
            System.out.println("  脏话自动和谐服务已启动");
            System.out.println("  本机访问: http://localhost:8080");
            try {
                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                while (interfaces.hasMoreElements()) {
                    NetworkInterface nif = interfaces.nextElement();
                    if (!nif.isUp() || nif.isLoopback() || nif.isVirtual()) {
                        continue;
                    }
                    Enumeration<InetAddress> addresses = nif.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress addr = addresses.nextElement();
                        if (addr.isLoopbackAddress() || addr.getHostAddress().contains(":")) {
                            continue; // 跳过 IPv6 与回环
                        }
                        System.out.println("  局域网访问: http://" + addr.getHostAddress() + ":8080");
                    }
                }
            } catch (Exception e) {
                System.out.println("  (未能自动探测局域网 IP，请手动查看本机 IP)");
            }
            System.out.println("========================================");
            System.out.println();
        };
    }
}
