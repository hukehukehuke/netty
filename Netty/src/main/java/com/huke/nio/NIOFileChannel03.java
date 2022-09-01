package com.huke.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author huke
 * @date 2022/09/01/下午5:22
 */
public class NIOFileChannel03 {

    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("/home/huke/work_space/Netty/src/main/java/com/huke/file/file.txt");
        FileChannel fileInputStreamChannel = fileInputStream.getChannel();


        FileOutputStream fileOutputStream = new FileOutputStream("/home/huke/work_space/Netty/src/main/java/com/huke/file/2.txt");
        FileChannel outputStreamChannel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        while (true){
            byteBuffer.clear();
            int read = fileInputStreamChannel.read(byteBuffer);
            if(read == -1){ //表示讀取完畢
                break;
            }
            byteBuffer.flip();
            outputStreamChannel.write(byteBuffer);
        }

        fileInputStreamChannel.close();
        outputStreamChannel.close();
    }
}
