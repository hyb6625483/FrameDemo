package cn.ovo.learn.netty;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ObjectUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@SpringBootTest
class NettyApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void fileChannelTest() {
        // 通过文件输入流创建一个FileChannel
        try (FileChannel channel = new FileInputStream("data.txt").getChannel()) {
            // 构建缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(5);
            // 从channel读取数据至buffer中
            while (channel.read(buffer) != -1) {
                // 打印buffer中的数据
                // 切换至读模式
                buffer.flip();
                // hasRemaining()判断是否还有未读的数据
                while (buffer.hasRemaining()) {
                    // get()获取数据
                    byte b = buffer.get();
                    System.out.println((char) b);
                }
                // 切换至写模式
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void byteBufferExamTest() {
        // 黏包、半包解决方式
        ByteBuffer source = ByteBuffer.allocate(32);
        source.put("Hello,world\nI'm zhangsan\nHo".getBytes());
        split(source);
        source.put("w are you?\n".getBytes());
        split(source);
    }

    private void split(ByteBuffer source) {
        source.flip();
        for (int i = 0; i < source.limit(); i++) {
            // 使用get(i)找到'\n'的位置
            if (source.get(i) == '\n') {
                // 计算出该条数据的长度
                int length = i + 1 - source.position();
                // 创建指定长度的buffer
                ByteBuffer buffer = ByteBuffer.allocate(length);
                // 读取内容
                for (int j = 0; j < length; j++) {
                    buffer.put(source.get());
                }
                buffer.flip();
                System.out.println(StandardCharsets.UTF_8.decode(buffer));
                buffer.clear();
            }
        }
        source.compact();
    }

    @Test
    void fileChannelTransferTo() {
        try (FileChannel form = new FileInputStream("data.txt").getChannel();
             FileChannel to = new FileOutputStream("to.txt").getChannel()) {
            // 效率高，底层会利用操作系统的零拷贝进行优化
            // 拷贝2G以内数据
            // form.transferTo(0, form.size(), to);

            // 拷贝2G以上数据
            long size = form.size();
            for (long left = size; left > 0; ) {
                // 获取起始值
                long position = size - left;
                // 剩余值
                left -= form.transferTo(position, left, to);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void filesWalkFileTree() throws IOException {
        final AtomicInteger dirCount = new AtomicInteger(), fileCount = new AtomicInteger();
        final Path path = Paths.get(".");
        // 遍历指定文件夹
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            // 遍历到文件夹时的操作
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.println("===> " + dir);
                dirCount.incrementAndGet();
                return super.preVisitDirectory(dir, attrs);
            }

            // 遍历到文件时的操作
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println(file);
                fileCount.incrementAndGet();
                return super.visitFile(file, attrs);
            }
        });
        System.out.println(dirCount);
        System.out.println(fileCount);
    }

    @Test
    void bioServerTest() throws IOException {
        // 创建服务器Channel
        final ServerSocketChannel socketChannel = ServerSocketChannel.open();
        // 绑定端口
        socketChannel.bind(new InetSocketAddress(8080));
        // 创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(256);
        // 等待客户端连接
        List<SocketChannel> channels = new ArrayList<>();
        while (true) {
            log.info("connecting...");
            // 线程阻塞，等待客户端建立连接
            final SocketChannel accept = socketChannel.accept();
            log.info("connected... {}", socketChannel);
            channels.add(accept);
            for (SocketChannel channel : channels) {
                log.debug("before read... {}", channel);
                // 线程阻塞，等待客户端发送数据
                channel.read(buffer);
                buffer.flip();
                System.out.println(StandardCharsets.UTF_8.decode(buffer));
                buffer.clear();
                log.debug("after read... {}", channel);
            }
        }
    }

    @Test
    void bioClientTest() throws IOException {
        // 创建客户端
        SocketChannel socketChannel = SocketChannel.open();
        // 与服务端建立连接
        socketChannel.connect(new InetSocketAddress(8080));
        System.out.println("waiting.....");
    }

    @Test
    void nioServerTest() throws IOException {
        // 创建服务器Channel
        final ServerSocketChannel socketChannel = ServerSocketChannel.open();
        // 绑定端口
        socketChannel.bind(new InetSocketAddress(8080));
        // 设置阻塞模式，默认为阻塞
        socketChannel.configureBlocking(false);
        // 创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(256);
        // 等待客户端连接
        List<SocketChannel> channels = new ArrayList<>();
        while (true) {
            // 设置configureBlocking(false)之后，线程为非阻塞，等待客户端建立连接
            final SocketChannel accept = socketChannel.accept();
            if (!ObjectUtils.isEmpty(accept)) {
                log.info("connected... {}", socketChannel);
                // 设置为非阻塞模式
                accept.configureBlocking(false);
                channels.add(accept);
            }
            for (SocketChannel channel : channels) {
                // 线程非阻塞，接收客户端发送数据
                final int read = channel.read(buffer);
                if (read > 0) {
                    buffer.flip();
                    System.out.println(StandardCharsets.UTF_8.decode(buffer));
                    buffer.clear();
                    log.info("after read... {}", channel);
                }
            }
        }
    }

    @Test
    void nioClientTest() throws IOException {
        // 创建客户端
        SocketChannel socketChannel = SocketChannel.open();
        // 与服务端建立连接
        socketChannel.connect(new InetSocketAddress(8080));
        System.out.println("waiting.....");
    }
}
