package itinerator.datamodel;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

@Immutable
public interface Location {
    @Parameter
    double getLatitude();

    @Parameter
    double getLongitude();
}
