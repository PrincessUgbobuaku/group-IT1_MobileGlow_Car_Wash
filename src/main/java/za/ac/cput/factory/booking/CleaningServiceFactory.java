package za.ac.cput.factory.booking;

import za.ac.cput.domain.booking.CleaningService;
import za.ac.cput.util.Helper;

public class CleaningServiceFactory {

    public static CleaningService createCleaningService(String serviceName, double priceOfService, double duration, String category) {

        if (!Helper.isValidString(serviceName)
                || !Helper.isValidDouble(priceOfService)
                || !Helper.isValidDouble(duration)
                || !Helper.isValidString(category)) {
            return null;
        }

        return new CleaningService.Builder()
                // No ID setter here, JPA will auto-generate it
                .setServiceName(serviceName)
                .setPriceOfService(priceOfService)
                .setDuration(duration)
                .setCategory(category)
                .build();
    }
}