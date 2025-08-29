package za.ac.cput.repository;

@Deprecated
public interface IRepository<Type, ID> {
    Type create(Type t); //create Employee object and insert it into database
    Type read(ID id);
    Type update(Type t);
    boolean delete(ID id);
}
