package com.example.censor;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URI;
import java.util.Enumeration;

/**
 * 脏话自动和谐应用入口。
 * 启动后打印本机局域网访问地址，方便同网段设备访问。
 */
@SpringBootApplication
// 【重要】删除 @EnableScheduling，不再使用定时心跳检测
public class CensorApplication {

    // 统一管理服务端口，和application.properties保持一致
    private static final int SERVER_PORT = 8090;

    public static void main(String[] args) {
        try {
            // 关闭SpringBoot默认无头模式，确保桌面弹窗、浏览器打开功能可用
            SpringApplication app = new SpringApplication(CensorApplication.class);
            app.setHeadless(false);
            app.run(args);
        } catch (Exception e) {
            // 启动失败：弹出图形化错误提示
            String errorMsg = "程序启动失败\n错误原因：" + getRootCause(e).getMessage();
            JOptionPane.showMessageDialog(null, errorMsg, "启动错误", JOptionPane.ERROR_MESSAGE);
            // 点击确认后直接结束进程，无后台残留
            System.exit(1);
        }
    }

    /**
     * 启动完成后输出本机与局域网访问地址，并自动打开浏览器
     */
    @Bean
    public ApplicationRunner printAccessUrls() {
        return args -> {
            System.out.println();
            System.out.println("========================================");
            System.out.println("  和谐助手服务已启动");
            System.out.println("  本机访问: http://localhost:" + SERVER_PORT);
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
                        System.out.println("  局域网访问: http://" + addr.getHostAddress() + ":" + SERVER_PORT);
                    }
                }
            } catch (Exception e) {
                System.out.println("  (未能自动探测局域网 IP，请手动查看本机 IP)");
            }
            System.out.println("========================================");
            System.out.println();

            // 服务完全就绪后，自动打开浏览器【带上区分标记】
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(new URI("http://localhost:" + SERVER_PORT + "?autoLaunch=1"));
                }
            } catch (Exception ignored) {
                // 浏览器打开失败不影响服务运行，静默忽略
            }
        };
    }

    /**
     * 提取最底层异常原因，避免嵌套异常信息晦涩
     */
    private static Throwable getRootCause(Throwable e) {
        Throwable cause = e;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause;
    }
}