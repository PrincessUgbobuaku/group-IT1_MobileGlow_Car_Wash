package za.ac.cput.domain.booking;

import jakarta.persistence.*;

@Entity
public class CleaningService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cleaningServiceId;

    private String serviceName;
    private double priceOfService;
    private double duration;

    @Column(nullable = false)
    private String category;

    // Constructors
    public CleaningService() {}

    private CleaningService(Builder builder) {
        this.cleaningServiceId = builder.cleaningServiceId;
        this.serviceName = builder.serviceName;
        this.priceOfService = builder.priceOfService;
        this.duration = builder.duration;
        this.category = builder.category;
    }

    // Getters & Setters

    public Long getCleaningServiceId() {
        return cleaningServiceId;
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
        return "CleaningService {\n" +
                "  cleaningServiceId = " + cleaningServiceId + ",\n" +
                "  serviceName = '" + serviceName + "',\n" +
                "  priceOfService = " + priceOfService + ",\n" +
                "  duration = " + duration + ",\n" +
                "  category = '" + category + "'\n" +
                '}';
    }


    // Builder
    public static class Builder {
        private Long cleaningServiceId;
        private String serviceName;
        private double priceOfService;
        private double duration;
        private String category;

        public Builder setCleaningServiceId(Long cleaningServiceId) {
            this.cleaningServiceId = cleaningServiceId;
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

        public Builder copy(CleaningService service) {
            this.cleaningServiceId = service.getCleaningServiceId();
            this.serviceName = service.getServiceName();
            this.priceOfService = service.getPriceOfService();
            this.duration = service.getDuration();
            this.category = service.getCategory();
            return this;
        }

        public CleaningService build() {
            return new CleaningService(this);
        }
    }
}

