package Project;

import java.util.List;

public class Diagnostic {
    private String name;
    private List<Treatment> treatments;


    public Diagnostic(String name, List<Treatment> treatments) {
        this.name = name;
        this.treatments = treatments;

    }

    public String getName() {

        return name;
    }

    public List<Treatment> getTreatments() {
        return treatments;
    }

    public void setTreatments(List<Treatment> treatments) {
        this.treatments = treatments;
    }



}
