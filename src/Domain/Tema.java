package Domain;

public class Tema implements hasId<Integer> {
    private int id;
    private String descriere;
    private int deadline;

    public Tema(int id, String descriere, int deadline) {
        this.id = id;
        this.descriere = descriere.replace(";", ",");
        this.deadline = deadline;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return "" + id + ";" + descriere + ";" + deadline;
    }

    @Override
    public Integer getId(){
        return id;
    }

}
