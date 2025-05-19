package project.models;

import java.util.List;

public class Diagnostic {
    private long id;
    private String name;
    private List<Treatment> treatments;

    public Diagnostic(String name, List<Treatment> treatments) {
        this.name = name;
        this.treatments = treatments;

    }

    public Diagnostic(long id, String name, List<Treatment> treatments) {
        this.id = id;
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


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Diagnostic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", treatments=" + treatments +
                '}';
    }
}
