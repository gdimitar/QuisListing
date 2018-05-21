package com.quislisting.util;

import com.quislisting.endpoints.rest.BaseConfigurations;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class ConnectionChecker {

    public static boolean isOnline() {
        try {
            final int timeoutMs = BaseConfigurations.InternetCheckerConfig.TIMEOUT_MILLISECONDS;
            final Socket sock = new Socket();
            final SocketAddress sockaddr = new InetSocketAddress(BaseConfigurations.InternetCheckerConfig.PING_HOSTNAME,
                    BaseConfigurations.InternetCheckerConfig.PING_PORT);

            sock.connect(sockaddr, timeoutMs);
            sock.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
