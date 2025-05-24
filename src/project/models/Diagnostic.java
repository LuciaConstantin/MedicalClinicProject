package project.models;

import java.util.List;

public class Diagnostic {
    private long id;
    private String name;
    private String doctorNotes;
    private List<Treatment> treatments;

    public Diagnostic(String name, String doctorNotes, List<Treatment> treatments) {
        this.name = name;
        this.doctorNotes = doctorNotes;
        this.treatments = treatments;

    }

    public Diagnostic(long id, String name,String doctorNotes,  List<Treatment> treatments) {
        this.id = id;
        this.name = name;
        this.doctorNotes = doctorNotes;
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

    public String getDoctorNotes() {
        return doctorNotes;
    }

    public void setDoctorNotes(String doctorNotes) {
        this.doctorNotes = doctorNotes;
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
