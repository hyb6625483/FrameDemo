package cn.ovo.learn.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class SocketClient {

    public static void main(String[] args) throws IOException {
        final SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(8080));
        socketChannel.write(StandardCharsets.UTF_8.encode("Hello!"));
        System.out.println("waiting...");
    }
}
