package data_management;

import com.data_management.DataStorage;
import com.data_management.MyWebSocketClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.net.URI;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for WebSocket data handling and robustness.
 */
class WebSocketRobustnessTest {
    private DataStorage storage;
    private MyWebSocketClient client;

    @BeforeEach
    void setUp() {
        storage = DataStorage.getInstance();
        // Pointing to a dummy URI for testing parsing logic
        client = new MyWebSocketClient(URI.create("ws://localhost:8888"), storage);
    }

    @Test
    void testCorruptedDataFormat() {
        // Test 1: Data with missing fields (should be handled gracefully)
        String corruptedMsg = "1,98.5,BloodSaturation"; // Missing timestamp
        assertDoesNotThrow(() -> client.onMessage(corruptedMsg),
                "Client should catch parsing errors without crashing");
    }

    @Test
    void testInvalidNumberFormat() {
        // Test 2: Text where a double is expected
        String invalidValueMsg = "1,INVALID_DATA,BloodSaturation,1715684400000";
        assertDoesNotThrow(() -> client.onMessage(invalidValueMsg),
                "Client should handle NumberFormatExceptions internally");
    }

    @Test
    void testRealTimeAlertTriggering() {
        // Test 3: Integration check - does data receipt trigger the alert logic?
        // This triggers a critical high Systolic BP
        String criticalMsg = "10,200.0,SystolicBloodPressure,1715684400000";

        // This should not throw errors and should store data
        client.onMessage(criticalMsg);

        var records = storage.getRecords(10, 1715684400000L, 1715684400000L);
        assertFalse(records.isEmpty(), "Data should be stored even if it triggers an alert");
        assertEquals(200.0, records.get(0).getMeasurementValue());
    }
}