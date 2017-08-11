package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.slf4j.LoggerFactory;

import util.HttpRequestUtils;
import util.IOUtils;

public class HttpRequest {
	
	private static final Logger log = (Logger) LoggerFactory.getLogger(HttpRequest.class);
	
	private RequestLine requestLine;// 요청라인에서 GET or POST, path, GET에 데이터가 넘어오면 params에 데이터 처리. 
	private Map<String, String> headers = new HashMap<String, String>();//헤더 정보. 
	private Map<String, String> params = new HashMap<String,String>();// params 로그인시 사용자 정보, 회원가입시 사용자 정보.   
	
	
	
	public HttpRequest(InputStream in) {
		
		try {
			
			BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
			String line  =br.readLine();// 한줄을 읽음 -> 첫 번째 한 줄은 요청 라인임. 
			
			if( line == null ) {
				return ;
			}
			
			requestLine = new RequestLine(line);
			
			line = br.readLine();
			
			while( !line.equals("")) {// 요청 헤더를 전부 읽어 들임
				((org.slf4j.Logger) log).debug("header : {}", line );
				String[] tokens = line.split(":");
				headers.put(tokens[0].trim(), tokens[1].trim());
				line = br.readLine();
			}
			
			if( "POST".equals(getMethod())) {//POST 일경우 바디 쪽을 읽는다. 
				
				String body = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length:")));
				params = HttpRequestUtils.parseQueryString(body);
			}else {//get일경우 바로 일겅옴. 
				params = requestLine.getParams();
			}
			
			
		}catch(IOException io ) {
			((org.slf4j.Logger) log).error(io.getMessage());
		}
	}
	public HttpMethod getMethod() {
		return requestLine.getMethod();
	}
	
	public String getPath() {
		return requestLine.getPath();
	}
	
	public String getHeader(String name) {
		return headers.get(name);
	}
	
	public String getParameter(String name ) {
		return params.get(name);
	}
}
