package com.huke.nio.groupchat;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @author huke
 * @date 2022/09/01/下午6:37
 */
public class GroupChatServer {

    private Selector selector;
    private ServerSocketChannel listenChannel;

    private static final int PORT = 6666;

    public GroupChatServer() {
        try {
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        try {
            while (true) {
                int count = selector.select(2000);
                if (count > 0) {
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        if (key.isAcceptable()) {
                            SocketChannel sc = listenChannel.accept();
                            sc.register(selector, SelectionKey.OP_READ);
                            System.out.println(sc.getRemoteAddress() + "上线");
                        }
                        if (key.isReadable()) {

                        }

                    }
                } else {
                    System.out.println("等待...............");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    private void readData(SelectionKey selectionKey) {

        SocketChannel channel = null;

        try {
            channel = (SocketChannel) selectionKey.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int count = channel.read(byteBuffer);
            if (count > 0) {
                String msg = new String(byteBuffer.array());
                System.out.println("from 客户端：" + msg);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void sendInfoOtherClients(String message,SocketChannel socketChannel){
        System.out.println("服务器转发消息中.........");

        selector.keys().forEach(key ->{
            Channel targetChannel = key.channel();
        });
    }
    public static void main(String[] args) {

    }
}
