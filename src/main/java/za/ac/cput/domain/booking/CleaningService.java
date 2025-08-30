package za.ac.cput.domain.booking;

import jakarta.persistence.*;

@Entity
public class CleaningService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cleaningServiceID;

    @Column(name = "service_name", nullable = false, length = 50)
    private String serviceName;

    private double priceOfService;
    private double duration;

    @Column(nullable = false)
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

    public Long getCleaningServiceID() {
        return cleaningServiceID;
    }

    public String getServiceName() {
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
                "cleaningServiceID=" + cleaningServiceID +
                ", serviceName='" + serviceName + '\'' +
                ", priceOfService=" + priceOfService +
                ", duration=" + duration +
                ", category='" + category + '\'' +
                '}';
    }

    public static class Builder {
        private Long cleaningServiceID;
        private String serviceName;
        private double priceOfService;
        private double duration;
        private String category;

        public Builder setCleaningServiceID(Long cleaningServiceID) {
            this.cleaningServiceID = cleaningServiceID;
            return this;
        }

        public Builder setServiceName(String serviceName) {
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
}