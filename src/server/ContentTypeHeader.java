package server;

import com.sun.net.httpserver.Headers;

public enum ContentTypeHeader {
    JSON("application/json"),
    TEXT("text/plain");
    final String headerContent;
    ContentTypeHeader(String headerContent) {
        this.headerContent = headerContent;
    }

    public void setHeader(Headers headers){
        headers.set("Content-Type" ,headerContent);
    }
}
