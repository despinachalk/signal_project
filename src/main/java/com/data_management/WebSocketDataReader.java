package com.data_management;

import java.net.URI;
import java.io.IOException;

public class WebSocketDataReader implements DataReader {
    private URI serverUri;

    public WebSocketDataReader(String url) {
        this.serverUri = URI.create(url);
    }

    @Override
    public void readData(DataStorage storage) throws IOException {
        MyWebSocketClient client = new MyWebSocketClient(serverUri, storage);
        client.connect(); // Starts the asynchronous connection
    }
}