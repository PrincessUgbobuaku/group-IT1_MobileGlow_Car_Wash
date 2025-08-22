package za.ac.cput.factory.booking;

import za.ac.cput.domain.booking.CleaningService;
import za.ac.cput.util.Helper;

public class CleaningServiceFactory {

    public static CleaningService createCleaningService(CleaningService.ServiceName serviceName, double priceOfService, double duration) {

        String cleaningServiceID = Helper.generateID();

        if (!Helper.isValidEnumValue(serviceName, CleaningService.ServiceName.class)
        || !Helper.isValidDouble(priceOfService)
        || !Helper.validateDuration(duration)) {
            return null;
        } else {
            return new CleaningService.Builder()
                    .setCleaningServiceID(cleaningServiceID)
                    .setServiceName(serviceName)
                    .setPriceOfService(priceOfService)
                    .setDuration(duration)
                    .build();
        }


    }
}
