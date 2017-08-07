package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
<<<<<<< HEAD
import java.util.HashMap;
import java.util.Map;
=======
import java.util.Collection;
import java.util.Map;

import model.User;

>>>>>>> refs/remotes/origin/was-step1-bad-version
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import db.DataBase;
import model.Database;
import model.User;
import util.HttpRequestUtils;
import util.IOUtils;


import util.HttpRequestUtils;
import util.IOUtils;
import db.DataBase;

public class RequestHandler extends Thread {
<<<<<<< HEAD

	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	private Socket connection;
=======
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
>>>>>>> refs/remotes/origin/was-step1-bad-version

    private Socket connection;

<<<<<<< HEAD
	public void run() {

		// 클라이언
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
				connection.getPort());

		// 클라이언트와 서버의 통신을 inputstream 과 outputstream 을 이용한다.
		// try 안에 스트림을 얻어오면 catch 에서는 자동으로 close가 된다.

		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

			// TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

			// line 단위로 데이터를 얻어온다.
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

			String line = br.readLine();

			if (line == null) {
				return;
			}

			String url = HttpRequestUtils.getUrl(line);
			Map<String, String> headers = new HashMap<String, String>();
			
			while (!"".equals(line)) {

				log.debug("header :{}", line);
				line = br.readLine();
				String[] headerTokens = line.split(": ");
				if (headerTokens.length == 2) {
					headers.put(headerTokens[0], headerTokens[1]);
				}
			}
			
			log.debug("Content-Length : {}", headers.get("Content-Length"));
			
			if (url.startsWith("/user/create")) {

				String requestBody = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
				log.debug("Request Body: {}", requestBody);
				Map<String, String> params = HttpRequestUtils.parseQueryString(requestBody);
				User user = new User(params.get("userId"), params.get("password"), params.get("name"),params.get("email"));
				log.debug("User : {}", user);

				Database.addUser(user);
				DataOutputStream dos = new DataOutputStream(out);
				response302Header(dos);
				

			} else if (url.equals("/user/login")) {
					
				String requestBody = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
				log.debug("Request Body: {}", requestBody);
				Map<String, String> params = HttpRequestUtils.parseQueryString(requestBody);
				log.debug("userId :{}, password : {}", params.get("userId"), params.get("password"));
				User user = Database.getUsers(params.get("userId"));
				
				if (user == null) {
					log.debug("User Not Found");
					DataOutputStream dos = new DataOutputStream(out);
					response302Header(dos);

				}else if (user.getPassword().equals(params.get("password"))) {
					
					log.debug("login sucess!");
					DataOutputStream dos = new DataOutputStream(out);
					response302HeaderWithCookie(dos,"logined=true; path=/");

				} else {
					log.debug("Password Missmatch");
					DataOutputStream dos = new DataOutputStream(out);
					response302Header(dos);
				}
				
			} else {
				
				DataOutputStream dos = new DataOutputStream(out);
				byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
				
				response200Header(dos, body.length);
				responseBody(dos, body);
				
			}
		} catch (IOException e) {

			log.error(e.getMessage());

		}
	}
	
	
	private void response302HeaderWithCookie(DataOutputStream dos, String cookie) {
		
		log.debug("response302HeaderWithCookie : {}", cookie );
		
		try {
			
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			dos.writeBytes("Location: /index.html\r\n");// 클라이언트location 정보를 활용해서 재 요청 보내라.
			dos.writeBytes("Set-Cookie: " + cookie + "\r\n");
			dos.writeBytes("\r\n");
			
		} catch (IOException e) {
			log.error(e.getMessage());
		}	
	}
	
	private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {

		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	private void response302Header(DataOutputStream dos) {

		try {

			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			dos.writeBytes("Location: /index.html\r\n");// 클라이언트location 정보를 활용해서 재 요청 보내라.

			dos.writeBytes("\r\n");

		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	private void responseBody(DataOutputStream dos, byte[] body) {

		try {
			dos.write(body, 0, body.length);
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
=======
    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();
            if (line == null) {
                return;
            }

            log.debug("request line : {}", line);
            String[] tokens = line.split(" ");

            int contentLength = 0;
            boolean logined = false;
            while (!line.equals("")) {
                line = br.readLine();
                log.debug("header : {}", line);

                if (line.contains("Content-Length")) {
                    contentLength = getContentLength(line);
                }

                if (line.contains("Cookie")) {
                    logined = isLogin(line);
                }
            }

            String url = getDefaultUrl(tokens);
            if ("/user/create".equals(url)) {
                String body = IOUtils.readData(br, contentLength);
                Map<String, String> params = HttpRequestUtils.parseQueryString(body);
                User user = new User(params.get("userId"), params.get("password"), params.get("name"),
                        params.get("email"));
                log.debug("user : {}", user);
                DataBase.addUser(user);
                DataOutputStream dos = new DataOutputStream(out);
                response302Header(dos);
            } else if ("/user/login".equals(url)) {
                String body = IOUtils.readData(br, contentLength);
                Map<String, String> params = HttpRequestUtils.parseQueryString(body);
                User user = DataBase.findUserById(params.get("userId"));
                if (user != null) {
                    if (user.login(params.get("password"))) {
                        DataOutputStream dos = new DataOutputStream(out);
                        response302LoginSuccessHeader(dos);
                    } else {
                        responseResource(out, "/user/login_failed.html");
                    }
                } else {
                    responseResource(out, "/user/login_failed.html");
                }
            } else if ("/user/list".equals(url)) {
                if (!logined) {
                    responseResource(out, "/user/login.html");
                    return;
                }

                Collection<User> users = DataBase.findAll();
                StringBuilder sb = new StringBuilder();
                sb.append("<table border='1'>");
                for (User user : users) {
                    sb.append("<tr>");
                    sb.append("<td>" + user.getUserId() + "</td>");
                    sb.append("<td>" + user.getName() + "</td>");
                    sb.append("<td>" + user.getEmail() + "</td>");
                    sb.append("</tr>");
                }
                sb.append("</table>");
                byte[] body = sb.toString().getBytes();
                DataOutputStream dos = new DataOutputStream(out);
                response200Header(dos, body.length);
                responseBody(dos, body);
            } else if (url.endsWith(".css")) {
                responseCssResource(out, url);
            } else {
                responseResource(out, url);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private boolean isLogin(String line) {
        String[] headerTokens = line.split(":");
        Map<String, String> cookies = HttpRequestUtils.parseCookies(headerTokens[1].trim());
        String value = cookies.get("logined");
        if (value == null) {
            return false;
        }
        return Boolean.parseBoolean(value);
    }

    private void responseResource(OutputStream out, String url) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        response200Header(dos, body.length);
        responseBody(dos, body);
    }

    private void responseCssResource(OutputStream out, String url) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        response200CssHeader(dos, body.length);
        responseBody(dos, body);
    }

    private int getContentLength(String line) {
        String[] headerTokens = line.split(":");
        return Integer.parseInt(headerTokens[1].trim());
    }

    private String getDefaultUrl(String[] tokens) {
        String url = tokens[1];
        if (url.equals("/")) {
            url = "/index.html";
        }
        return url;
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200CssHeader(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
            dos.writeBytes("Location: /index.html \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302LoginSuccessHeader(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
            dos.writeBytes("Set-Cookie: logined=true \r\n");
            dos.writeBytes("Location: /index.html \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
>>>>>>> refs/remotes/origin/was-step1-bad-version
}




