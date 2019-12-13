package org.springboot.websocket;

import java.io.FileNotFoundException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 
 * Title: SataApplication.java 
 * Description:   
 * Copyright: Copyright (c) 2019
 * Company: 浙江数蜂科技有限公司
 * @author lds
 * @date 2019年9月25日  
 * @version 1.0
 */
@SpringBootApplication
public class WebsocketApplication {

	public static void main(String[] args) throws FileNotFoundException {
		SpringApplication.run(WebsocketApplication.class, args);
		
		//new Thread(new WebSocketPolicyListener()).start();
	}

}
