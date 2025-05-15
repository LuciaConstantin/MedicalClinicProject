package Project;

public class Diagnostic {
    private String name;
    private Treatment[] treatments;
    private String possibleCause;

    public Diagnostic(String name, Treatment[] treatments, String possibleCause) {
        this.name = name;
        this.treatments = treatments;
        this.possibleCause = possibleCause;
    }

    public String getName() {

        return name;
    }

    public Treatment[] getTreatments() {

        return treatments;
    }

    public String getPossibleCause() {

        return possibleCause;
    }


}
