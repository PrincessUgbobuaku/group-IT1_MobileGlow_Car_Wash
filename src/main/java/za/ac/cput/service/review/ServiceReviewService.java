package za.ac.cput.service.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.review.ServiceReview;
import za.ac.cput.repository.review.IServiceReviewRepository;
import za.ac.cput.service.IService;

import java.util.Date;
import java.util.List;

@Service
public class ServiceReviewService implements IServiceReviewService {

    private final IServiceReviewRepository serviceReviewRepository;

    @Autowired
    public ServiceReviewService(IServiceReviewRepository serviceReviewRepository) {
        this.serviceReviewRepository = serviceReviewRepository;
    }

    @Override
    public ServiceReview create(ServiceReview serviceReview) {
        return serviceReviewRepository.save(serviceReview);
    }

    @Override
    public ServiceReview read(Long aLong) {
       return serviceReviewRepository.findById(aLong).orElse(null);
    }

    @Override
    public ServiceReview update(ServiceReview serviceReview) {
        return serviceReviewRepository.save(serviceReview);
    }

    @Override
    public boolean delete(Long aLong) {
        serviceReviewRepository.deleteById(aLong);
        return true;
    }

    @Override
    public List<ServiceReview> getAll() {
        return serviceReviewRepository.findAll();
    }


}