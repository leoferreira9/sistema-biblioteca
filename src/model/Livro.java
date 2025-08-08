package model;

public class Livro {

    private int id;
    private String titulo;
    private String autor;
    private int anoDePublicacao;
    private boolean disponivel;
    private Categoria categoria;

    //Construtor para inserção (sem ID)
    public Livro(String titulo, String autor, int anoDePublicacao, boolean disponivel, Categoria categoria) {
        this.titulo = titulo;
        this.autor = autor;
        this.anoDePublicacao = anoDePublicacao;
        this.disponivel = disponivel;
        this.categoria = categoria;
    }

    //Construtor completo (com ID)
    public Livro(int id, String titulo, String autor, int anoDePublicacao, boolean disponivel, Categoria categoria) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.anoDePublicacao = anoDePublicacao;
        this.disponivel = disponivel;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getAnoDePublicacao() {
        return anoDePublicacao;
    }

    public void setAnoDePublicacao(int anoDePublicacao) {
        this.anoDePublicacao = anoDePublicacao;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return String.format(
                "ID: %d\nTítulo: %s\nAutor: %s\nAno de publicação: %d\nDisponível: %s\nCategoria: %s",
                id, titulo, autor, anoDePublicacao, (disponivel ? "Sim" : "Não"), categoria != null ? categoria.getNome() : "Sem categoria"
        );
    }
}
