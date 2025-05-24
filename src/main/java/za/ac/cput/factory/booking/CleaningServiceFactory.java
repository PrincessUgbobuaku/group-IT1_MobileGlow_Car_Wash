package za.ac.cput.factory.booking;

import za.ac.cput.domain.booking.CleaningService;
import za.ac.cput.util.Helper;

public class CleaningServiceFactory {

    public static CleaningService createCleaningServiceFactory1(CleaningService.ServiceName serviceName, double priceOfService, double duration) {

        String cleaningServiceID = Helper.generateID();

        if (!Helper.isValidEnumValue(serviceName, CleaningService.ServiceName.class)
        || !Helper.validatePrice(priceOfService)
        || !Helper.validatePrice(duration)) {
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
