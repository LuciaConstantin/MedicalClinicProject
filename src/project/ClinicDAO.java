package project;

public abstract class ClinicDAO <T> {
    public abstract void create(T t);
    public abstract T getById(long id);

}


