package Domain;

public class Student implements hasId<Integer> {
    private int id;
    private String nume;
    private int grupa;
    private String email;
    private String prof_indrumator;
    private String username;

    public Student(int id, String nume, int grupa, String email, String prof_indrumator) {
        this.id = id;
        this.nume = nume.replace(";", ",");
        this.grupa = grupa;
        this.email = email;
        this.prof_indrumator = prof_indrumator.replace(";", ",");
        this.username = email.split("@")[0];
    }

    public Student(int id, String nume, int grupa, String email, String prof_indrumator, String username) {
        this.id = id;
        this.nume = nume.replace(";", ",");
        this.grupa = grupa;
        this.email = email;
        this.prof_indrumator = prof_indrumator.replace(";", ",");
        this.username = username;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public Integer getGrupa() {
        return grupa;
    }

    public void setGrupa(int grupa) {
        this.grupa = grupa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProf_indrumator() {
        return prof_indrumator;
    }

    public void setProf_indrumator(String prof_indrumator) {
        this.prof_indrumator = prof_indrumator;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return  "" + id + ";" + nume + ";" + grupa + ";" + email + ";" + prof_indrumator + ";" + username;
    }

    @Override
    public Integer getId(){
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        return id == student.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
