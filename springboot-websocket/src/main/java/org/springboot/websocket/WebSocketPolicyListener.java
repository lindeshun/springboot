package org.springboot.websocket;


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
 
/**
 * 为了兼容IE8浏览器，WebSocket采用Adobe Flash来实现，而Flash由于安全策略问题，会向TCP:843端口发送请求文件，服务器应该返回安全策略文件给客户端。
 */
public class WebSocketPolicyListener implements Runnable {
    private ServerSocket server;
 
    public WebSocketPolicyListener(){
        try {
            server=new ServerSocket(843);
            System.out.println("Adobe Flash安全策略监听启动");
        } catch (IOException e) {
            System.err.println();
            e.printStackTrace();
        }
    }
 
    @Override
    public void run() {
        while(true){
            try {
                Socket s=server.accept();
                //获取安全策略请求文件
                BufferedInputStream bin=new BufferedInputStream(s.getInputStream());//打开文件流//
                //网络操作调用avaiable()方法时，可能数据并没有到达，这时需要等待数据变为可用
                int avaiable=0;
                //当有可用数据时，退出循环
                while(avaiable==0){
                    avaiable=bin.available();
                }
                byte[] buf=new byte[avaiable];
                int count=bin.read(buf);
                String request=new String(buf,0,count);
                System.out.println("安全策略文件请求：\n"+request);
 
                //如果是安全策略文件请求，将安全策略文件发给客户端
                if(request.indexOf("<policy-file-request/>")!=-1){
                    //获取文件路径，我放在src目录下
                    String path=WebSocketPolicyListener.class.getResource("/").getPath()+"websocket-policy.xml";
                    FileInputStream fin=new FileInputStream(path);
                    buf=new byte[1024];
                    StringBuilder sb=new StringBuilder();
                    while((count=fin.read(buf))>0){
                        s.getOutputStream().write(buf,0,count);
                        sb.append(new String(buf,0,count));
                    }
                    System.out.println("安全策略文件响应：\n"+sb.toString());
                    fin.close();//关闭文件流
                }
 
                s.close();//关闭Socket流
            } catch (IOException e) {
                e.printStackTrace();
            }
 
        }
    }
}
