package za.ac.cput.factory.booking;

import za.ac.cput.domain.booking.CleaningService;
import za.ac.cput.util.Helper;

public class CleaningServiceFactory {

    public static CleaningService createCleaningService(CleaningService.ServiceName serviceName, double priceOfService, double duration, String category) {

        String cleaningServiceID = Helper.generateID();

        if (!Helper.isValidEnumValue(serviceName, CleaningService.ServiceName.class)
                || !Helper.isValidDouble(priceOfService)
                || !Helper.validateDuration(duration)
                || !Helper.isValidString(category)) {
            return null;
        }

        return new CleaningService.Builder()
                .setCleaningServiceID(cleaningServiceID)
                .setServiceName(serviceName)
                .setPriceOfService(priceOfService)
                .setDuration(duration)
                .setCategory(category)
                .build();
    }
}