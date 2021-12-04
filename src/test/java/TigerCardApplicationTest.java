import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

class TigerCardApplicationTest {

    TigerCardApplication tigerCardApplication;

    public static Stream<Arguments> ticketsProvider() {
        return Stream.of(
                Arguments.of(Arrays.asList(
                        "2007-12-01T10:15:30, ONE, ONE",
                        "2007-12-01T04:15:30, ONE, ONE"
                ), 55),
                Arguments.of(Arrays.asList(
                ), 0)
        );
    }

    @BeforeEach
    void setUp() {
        tigerCardApplication = new TigerCardApplication();
    }

    @AfterEach
    void tearDown() {
        tigerCardApplication = null;
    }

    @DisplayName("testing for different tickets")
    @ParameterizedTest(name = "{index} ==> test data")
    @MethodSource("ticketsProvider")
    void process(List<String> ticketStrings, int expectedFare) {
        Assertions.assertEquals(expectedFare, tigerCardApplication.process(ticketStrings));
    }
}