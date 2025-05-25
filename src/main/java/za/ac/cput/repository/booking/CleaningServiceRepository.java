package za.ac.cput.repository.booking;

import za.ac.cput.repository.booking.impl.ICleaningServiceRepository;

public class CleaningServiceRepository /*implements ICleaningServiceRepository*/ {

    /*
    *  private static CleaningServiceRepository repository = null;
    private Map<String, CleaningService> cleaningServiceDB;

    private CleaningServiceRepository() {
        this.cleaningServiceDB = new HashMap<>();
    }

    public static CleaningServiceRepository getRepository() {
        if (repository == null) {
            repository = new CleaningServiceRepository();
        }
        return repository;
    }

    @Override
    public CleaningService create(CleaningService cleaningService) {
        cleaningServiceDB.put(cleaningService.getCleaningServiceID(), cleaningService);
        return cleaningService;
    }

    @Override
    public CleaningService read(String cleaningServiceID) {
        return cleaningServiceDB.get(cleaningServiceID);
    }

    @Override
    public CleaningService update(CleaningService cleaningService) {
        if (cleaningServiceDB.containsKey(cleaningService.getCleaningServiceID())) {
            cleaningServiceDB.put(cleaningService.getCleaningServiceID(), cleaningService);
            return cleaningService;
        }
        return null;
    }

    @Override
    public boolean delete(String cleaningServiceID) {
        return cleaningServiceDB.remove(cleaningServiceID) != null;
    }

    @Override
    public List<CleaningService> getAll() {
        return new ArrayList<>(cleaningServiceDB.values());
    }
    *
    * */
}
