package webserver;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.slf4j.LoggerFactory;

import util.HttpRequestUtils;

public class RequestLine {//요청라인의 method와 path를 처리한다. 
	
	private static final Logger log = (Logger) LoggerFactory.getLogger(RequestLine.class);
	
	private HttpMethod method;
	private String path;
	private Map<String, String> params = new HashMap<String, String>();

	public RequestLine(String requestLine) {
		
		((org.slf4j.Logger) log).debug("request line : {}", requestLine );
		String[] tokens = requestLine.split(" ");
		method = HttpMethod.valueOf(tokens[0]);
		
		if(method.isPost()) {
			path = tokens[1];
			return ;
		}
		int index = tokens[1].indexOf("?");
		
		if(index == -1 ) {
			path = tokens[1];
			return;
		}else {
			path = tokens[1].substring(0, index);
			//get 에서 ? 뒤로 넘어온~userId password name email 정보를 추출한다. 
			params = HttpRequestUtils.parseQueryString( tokens[1].substring(index+1));
		}
	}
	public HttpMethod getMethod() {
		return method;
	}
	
	public String getPath() {
		return path;
	}

	public Map<String, String> getParams(){
		
		return params;	
	}
}
