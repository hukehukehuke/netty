package com.huke.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * @author huke
 * @date 2022/09/01/下午5:28
 */
public class NIOFileChannel04 {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("");
        FileOutputStream fileOutputStream = new FileOutputStream("");

        FileChannel sourcch = fileInputStream.getChannel();

        FileChannel destch = fileOutputStream.getChannel();

        //完成文件拷貝
        destch.transferFrom(sourcch,0,sourcch.size());
    }
}
