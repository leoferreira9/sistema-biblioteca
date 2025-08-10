package model;

public class Categoria {

    private int id;
    private String nome;

    //Construtor para inserção (sem ID)
    public Categoria(String nome){
        this.nome = nome;
    }

    //Construtor completo (com ID)
    public Categoria(int id, String nome){
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return String.format("(ID: %s) | Categoria: %s", id, nome);
    }
}
