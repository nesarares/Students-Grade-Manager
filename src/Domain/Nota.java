package Domain;

public class Nota implements hasId<Integer> {
    private int id;
    private int id_student;
    private int id_tema;
    private double nota;
    private int saptamana_predare;

    public Nota(int id, int id_student, int id_tema, double nota, int saptamana_predare) {
        this.id = id;
        this.id_student = id_student;
        this.id_tema = id_tema;
        this.nota = nota;
        this.saptamana_predare = saptamana_predare;
    }

    public Nota(int id, int id_student, int id_tema, double nota) {
        this.id = id;
        this.id_student = id_student;
        this.id_tema = id_tema;
        this.nota = nota;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_student() {
        return id_student;
    }

    public void setId_student(int id_student) {
        this.id_student = id_student;
    }

    public int getId_tema() {
        return id_tema;
    }

    public void setId_tema(int id_tema) {
        this.id_tema = id_tema;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public int getSaptamana_predare() {
        return saptamana_predare;
    }

    public void setSaptamana_predare(int saptamana_predare) {
        this.saptamana_predare = saptamana_predare;
    }

    @Override
    public String toString() {
        return "" + id + ";" + id_student + ";" + id_tema + ";" + nota + ";" + saptamana_predare;
    }
}
