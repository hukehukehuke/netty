package com.huke.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author huke
 * @date 2022/09/01/下午5:05
 */
public class NIOFileChannel01 {

    public static void main(String[] args) throws Exception {
        String str = "Hello,world";
        FileOutputStream fileOutputStream = new FileOutputStream("/home/huke/work_space/Netty/src/main/java/com/huke/file/file.txt");

        FileChannel fileChannel = fileOutputStream.getChannel();

        //创建一个缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(str.getBytes());

        byteBuffer.flip();

        //将byteBuffer数据写入fileChannel
        fileChannel.write(byteBuffer);
        fileOutputStream.close();

    }
}
