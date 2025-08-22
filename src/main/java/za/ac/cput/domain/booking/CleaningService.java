/*CleaningService.java
 * Cleaning Service model class
 * Author: Adaeze Princess Ugbobuaku
 * Date: 11 May 2025*/

package za.ac.cput.domain.booking;

import jakarta.persistence.*;

@Entity
public class CleaningService {

    @Id
    private String cleaningServiceID;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_name", nullable = false, unique = true)
    //Ensures the enum is stored as a readable string (not ordinal numbers).
    //Stores enums as strings in the DB (e.g., "WAXING_AND_POLISHING" instead of 0).

    private ServiceName serviceName;
    private double priceOfService;
    private double duration;

    // Required by JPA
    protected CleaningService() {}


    private CleaningService(Builder builder) {
        this.cleaningServiceID = builder.cleaningServiceID;
        this.serviceName = builder.serviceName;
        this.priceOfService = builder.priceOfService;
        this.duration = builder.duration;
    }

    public String getCleaningServiceID() {
        return cleaningServiceID;
    }

    public ServiceName getServiceName() {
        return serviceName;
    }

    public double getPriceOfService() {
        return priceOfService;
    }

    public double getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "CleaningService{" +
                "cleaningServiceID='" + cleaningServiceID + '\'' +
                ", serviceName=" + serviceName +
                ", priceOfService=" + priceOfService +
                ", duration=" + duration +
                '}';
    }

    public static class Builder {
        private String cleaningServiceID;
        private ServiceName serviceName;
        private double priceOfService;
        private double duration;

        public Builder setCleaningServiceID(String cleaningServiceID) {
            this.cleaningServiceID = cleaningServiceID;
            return this;
        }

        public Builder setServiceName(ServiceName serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public Builder setPriceOfService(double priceOfService) {
            this.priceOfService = priceOfService;
            return this;
        }

        public Builder setDuration(double duration) {
            this.duration = duration;
            return this;
        }

        // Copy method
        public Builder copy(CleaningService cleaningService) {
            this.cleaningServiceID = cleaningService.cleaningServiceID;
            this.serviceName = cleaningService.serviceName;
            this.priceOfService = cleaningService.priceOfService;
            this.duration = cleaningService.duration;
            return this;
        }


        public CleaningService build() {
            return new CleaningService(this);
        }
    }
    public enum ServiceName {
        EXTERIOR_WASH,
        INTERIOR_CLEANING,
        WAXING_AND_POLISHING,
        CERAMIC_COATING,
        TIRE_AND_WHEEL_CLEANING,
        ENGINE_CLEANING,
    }
}
