package project.models;

public class MedicalServices {
    private long id;
    private String serviceName;
    private double servicePrice;
    private int serviceTime;

    public MedicalServices(String serviceName, double servicePrice, int serviceTime) {
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
        this.serviceTime = serviceTime;
    }

    public MedicalServices(long id, String serviceName, double servicePrice, int serviceTime) {
        this.id = id;
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
        this.serviceTime = serviceTime;
    }

    public int getServiceTime() {

        return serviceTime;
    }

    public String getServiceName() {

        return serviceName;
    }

    public double getServicePrice() {

        return servicePrice;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setServicePrice(double servicePrice) {
        this.servicePrice = servicePrice;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "MedicalServices{" +
                "serviceName='" + serviceName + '\'' +
                ", servicePrice=" + servicePrice +
                ", serviceTime=" + serviceTime +
                '}';
    }

}
