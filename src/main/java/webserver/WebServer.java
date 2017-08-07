package webserver;

import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {
<<<<<<< HEAD
	
    private static final Logger log = LoggerFactory.getLogger(WebServer.class);
    
=======
    private static final Logger log = LoggerFactory.getLogger(WebServer.class);
>>>>>>> refs/remotes/origin/was-step1-bad-version
    private static final int DEFAULT_PORT = 8080;

    public static void main(String args[]) throws Exception {
    	
        int port = 0;
  
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
<<<<<<< HEAD
        try (ServerSocket listenSocket = new ServerSocket(port)) {
        	
=======

        try (ServerSocket listenSocket = new ServerSocket(port)) {
>>>>>>> refs/remotes/origin/was-step1-bad-version
            log.info("Web Application Server started {} port.", port);

            // 사용자의 요청이 있을 때 까지 대기한다.
            // 사용자의 요청이 많을수 있다.  while문으로 계속 요청을 잡는다.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
<<<<<<< HEAD
            	
            	//요청이 들어오면  클라이언트의 연결을 담당하는 소켓을 requestHandler에게 전달하면서 새로운 스레드를 실행하는 방식으로 멀티 스레드 프로그래밍을 지원하고 있다. 
=======
>>>>>>> refs/remotes/origin/was-step1-bad-version
                RequestHandler requestHandler = new RequestHandler(connection);
                requestHandler.start();
            }
        }
    }
}
