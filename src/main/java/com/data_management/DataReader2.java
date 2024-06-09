package com.data_management;

import java.io.IOException;
import java.net.URISyntaxException;

public interface DataReader2 {
    /**
     * Connects to a WebSocket server and reads data continuously.
     *
     * @param url the WebSocket server URI
     * @param dataStorage the storage where data will be stored
     * @throws URISyntaxException if the URI is incorrect
     * @throws InterruptedException if the connection is interrupted
     */
    void readRealTimeData(String url, DataStorage dataStorage) throws URISyntaxException, InterruptedException;
}
