package itinerator;

import static org.mockito.Mockito.mock;

public class TestUtil {
    public static final double DELTA = 1e-9;

    public static <R, G extends R> G mockGeneric(Class<R> aRawClass) {
        R mock = mock(aRawClass);
        //noinspection unchecked
        return (G) mock;
    }
}
