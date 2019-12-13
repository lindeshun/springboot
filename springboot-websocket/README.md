# java websocket通信

#### 项目介绍
springboot2 集成websocket

#### 软件架构
spring boot + websocket + jdk1.8

#### 使用说明

1. 引入websocket依赖
		
    ~~~
    <dependency>  
           <groupId>org.springframework.boot</groupId>  
           <artifactId>spring-boot-starter-websocket</artifactId>  
       </dependency>
    ~~~
    
2. 启用WebSocket的支持

    ~~~
    @Configuration  
    public class WebSocketConfig {  
    	
        @Bean  
        public ServerEndpointExporter serverEndpointExporter() {  
            return new ServerEndpointExporter();  
        }  
    } 
    ~~~

3.  编写Websocketserver

   ~~~
   @Component
   @ServerEndpoint("/webSocket")
   @Slf4j
   public class WebSocket {
   
       private Session session;
   
       private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();
   
       @OnOpen
       public void onOpen(Session session) {
           this.session = session;
           webSocketSet.add(this);
           log.info("【websocket消息】有新的连接, 总数:{}", webSocketSet.size());
       }
   
       @OnClose
       public void onClose() {
           webSocketSet.remove(this);
           log.info("【websocket消息】连接断开, 总数:{}", webSocketSet.size());
       }
   
       @OnMessage
       public void onMessage(String message) {
           log.info("【websocket消息】收到客户端发来的消息:{}", message);
       }
   
       public void sendMessage(String message) {
           for (WebSocket webSocket: webSocketSet) {
               log.info("【websocket消息】广播消息, message={}", message);
               try {
                   webSocket.session.getBasicRemote().sendText(message);
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
       }
   }
   ~~~

   4.  编写websocket客户端html文件

   ~~~
   <!DOCTYPE html>
   <html>
   <head>
   <meta charset="UTF-8">
   <title>WebSocket示例</title>
   </head>
   <body>
       <input id="text" type="text"/>
       <button onclick="send()">发送消息</button>
       <hr/>
       <button onclick="closeWebSocket()">关闭WebSocket连接</button>
       <hr/>
       <div id="message"></div>
   </body>
   
   <script type="text/javascript">
       var websocket = null;
       //判断当前浏览器是否支持WebSocket
       if ('WebSocket' in window) {
           // 不带参数的写法
           websocket = new WebSocket("ws://127.0.0.1:8080/websocket");
       }
       else {
           alert('当前浏览器 Not support websocket')
       }
   
       //连接发生错误的回调方法
       websocket.onerror = function () {
           setMessageInnerHTML("WebSocket连接发生错误");
       };
   
       //连接成功建立的回调方法
       websocket.onopen = function () {
           setMessageInnerHTML("WebSocket连接成功");
       }
   
       //接收到消息的回调方法
       websocket.onmessage = function (event) {
           setMessageInnerHTML(event.data);
       }
   
       //连接关闭的回调方法
       websocket.onclose = function () {
           setMessageInnerHTML("WebSocket连接关闭");
       }
   
       //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
       window.onbeforeunload = function () {
           closeWebSocket();
       }
   
       //将消息显示在网页上
       function setMessageInnerHTML(innerHTML) {
           document.getElementById('message').innerHTML += innerHTML + '<br/>';
       }
   
       //关闭WebSocket连接
       function closeWebSocket() {
           websocket.close();
       }
   
       //发送消息
       function send() {
           var message = document.getElementById('text').value;
           websocket.send(message);
       }
   </script>
   </html>
   ~~~

#### 兼容ie8

参考博客：
https://www.cnblogs.com/againn/p/8308875.html

https://blog.csdn.net/Beingccccc/article/details/81700416（java代码正确）

https://blog.csdn.net/bigbigtangcsdn/article/details/82015543（java代码有误，思路正确）
websocket.js下载地址：
https://github.com/gimite/web-socket-js