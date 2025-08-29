package za.ac.cput.domain.booking;

import jakarta.persistence.*;

@Entity
public class CleaningService {

    @Id
    private String cleaningServiceID;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_name", nullable = false, unique = true, length = 50)
    private ServiceName serviceName;

    private double priceOfService;
    private double duration;

    @Column  // You can remove nullable=false if category is optional
    private String category;

    // Required by JPA
    protected CleaningService() {}

    private CleaningService(Builder builder) {
        this.cleaningServiceID = builder.cleaningServiceID;
        this.serviceName = builder.serviceName;
        this.priceOfService = builder.priceOfService;
        this.duration = builder.duration;
        this.category = builder.category;
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

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "CleaningService{" +
                "cleaningServiceID='" + cleaningServiceID + '\'' +
                ", serviceName=" + serviceName +
                ", priceOfService=" + priceOfService +
                ", duration=" + duration +
                ", category='" + category + '\'' +
                '}';
    }

    public static class Builder {
        private String cleaningServiceID;
        private ServiceName serviceName;
        private double priceOfService;
        private double duration;
        private String category;

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

        public Builder setCategory(String category) {
            this.category = category;
            return this;
        }

        public Builder copy(CleaningService cleaningService) {
            this.cleaningServiceID = cleaningService.cleaningServiceID;
            this.serviceName = cleaningService.serviceName;
            this.priceOfService = cleaningService.priceOfService;
            this.duration = cleaningService.duration;
            this.category = cleaningService.category;
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
        BASIC_HAND_WASH,
        TOUCHLESS_CAR_WASH,
        FOAM_CANNON_PRE_WASH
    }
}