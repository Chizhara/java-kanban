package server;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ResponseBuilder {
    private ContentTypeHeader header;
    private int rCode;
    private String rBody;

    public ResponseBuilder(int rCode, String rBody, ContentTypeHeader contentTypeHeader) {
        this.rCode = rCode;
        this.rBody = rBody;
        this.header = contentTypeHeader;
    }

    public ResponseBuilder(int rCode, ContentTypeHeader contentTypeHeader) {
        this(rCode, "", contentTypeHeader);
    }

    public void writeResponse(HttpExchange exchange) throws IOException {
        if(rBody.isBlank()) {
            exchange.sendResponseHeaders(rCode, 0);
        } else {
            header.setHeader(exchange.getResponseHeaders());
            byte[] bytes = rBody.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(rCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }

    public static ResponseBuilder getWorseEndpointResponse() {
        return new ResponseBuilder(400, "Неккоректный эндпоинт", ContentTypeHeader.TEXT);
    }
}
