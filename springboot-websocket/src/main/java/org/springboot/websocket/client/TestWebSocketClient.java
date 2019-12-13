package org.springboot.websocket.client;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * java实现websocket客户端
 * @author wb-lds551323
 *
 */
public class TestWebSocketClient extends WebSocketClient {
    
    private final Logger LOGGER = LoggerFactory.getLogger(TestWebSocketClient.class);
    
    public TestWebSocketClient(URI serverUri) {
        super(serverUri);
    }
    
    public TestWebSocketClient(URI serverUri, Draft protocolDraft) {
        super(serverUri, protocolDraft);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        LOGGER.info("Open a WebSocket connection on client. ");
    }
    
    @Override
    public void onClose(int arg0, String arg1, boolean arg2) {
        LOGGER.info("Close a WebSocket connection on client. ");
    }

    @Override
    public void onMessage(String msg) {
        LOGGER.info("WebSocketClient receives a message: " + msg);
    }

    @Override
    public void onError(Exception exception) {
        LOGGER.error("WebSocketClient exception. ", exception);
    }

    public static void main(String[] args) {
    	String serverUrl = "ws://127.0.0.1:8080/websocket";
		URI recognizeUri;
		try {
			recognizeUri = new URI(serverUrl);
			//协议版本为RFC 6455
			TestWebSocketClient client = new TestWebSocketClient(recognizeUri, new Draft_6455());
			//client.connect();
			client.connectBlocking();
			client.send("This is a message from client. ");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    			
	}
    
}
