package data_management;

import com.data_management.DataStorage;
import com.data_management.MyWebSocketClient;
import org.junit.jupiter.api.Test;
import java.net.URI;
import static org.junit.jupiter.api.Assertions.*;

class WebSocketIntegrationTest {

    @Test
    void testDataParsingAndStorage() {
        DataStorage storage = DataStorage.getInstance();
        MyWebSocketClient client = new MyWebSocketClient(URI.create("ws://localhost:8080"), storage);

        // Simulate receiving a raw WebSocket message
        String mockMessage = "1,98.5,BloodSaturation,1715684400000";
        client.onMessage(mockMessage);

        // Verify it was parsed and stored correctly
        var records = storage.getRecords(1, 1715684400000L, 1715684400000L);
        assertFalse(records.isEmpty(), "Data should be stored in DataStorage after receipt");
        assertEquals(98.5, records.get(0).getMeasurementValue());
    }
}