package Project;

public class MedicalServices {
    private String serviceName;
    private double servicePrice;
    private int serviceTime;

    public MedicalServices(String serviceName, double servicePrice, int serviceTime) {
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

}
