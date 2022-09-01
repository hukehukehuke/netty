package com.huke.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author huke
 * @date 2022/09/01/下午5:18
 */
public class NIOFileChannel02 {

    public static void main(String[] args) throws Exception {
        File file = new File("/home/huke/work_space/Netty/src/main/java/com/huke/file/file.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        FileChannel fileChannel = fileInputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        fileChannel.read(byteBuffer);

        System.out.println(new String(byteBuffer.array()));
    }
}
