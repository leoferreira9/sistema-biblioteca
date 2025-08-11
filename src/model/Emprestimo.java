package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Emprestimo {

    private int id;
    private Usuario usuario;
    private Livro livro;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;

    //Construtor para inserção (sem ID)
    public Emprestimo (Usuario usuario, Livro livro, LocalDate dataEmprestimo, LocalDate dataDevolucao){
        this.usuario = usuario;
        this.livro = livro;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = dataDevolucao;
    }

    //Construtor completo (com ID)
    public Emprestimo (int id, Usuario usuario, Livro livro, LocalDate dataEmprestimo, LocalDate dataDevolucao){
        this.id = id;
        this.usuario = usuario;
        this.livro = livro;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = dataDevolucao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public boolean isAtivo(){
        return dataDevolucao == null;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String nomeUsuario = (usuario != null) ? usuario.getNome() : "-";
        String nomeLivro = (livro != null) ? livro.getTitulo() : "-";
        String devolucao = (dataDevolucao != null) ? formatter.format(dataDevolucao) : "Em aberto";
        String emprestimoFmt = (dataEmprestimo != null) ? formatter.format(dataEmprestimo) : "-";

        return String.format(
          "\nID: %d\nUsuário: %s\nLivro: %s\nData de empréstimo: %s\nData de devolução: %s",
          id, nomeUsuario, nomeLivro, emprestimoFmt, devolucao
        );
    }
}
