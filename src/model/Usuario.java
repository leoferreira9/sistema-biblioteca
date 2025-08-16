package model;

public class Usuario {

    private int id;
    private String nome;
    private String cpf;
    private String email;

    //Construtor completo (com ID)
    public Usuario(int id, String nome, String cpf, String email){
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
    }

    //Construtor incompleto (sem ID)
    public Usuario(String nome, String cpf, String email){
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format(
                "\nID: %s\nUsuário: %s\nCPF: %s\nEmail: %s",
                id, nome, cpf, email
        );
    }
}
