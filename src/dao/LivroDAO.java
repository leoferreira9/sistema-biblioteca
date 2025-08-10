package dao;

import db.DB;
import model.Categoria;
import model.Livro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    public boolean inserir (Livro livro){
        if(livro == null
                || livro.getAutor() == null || livro.getAutor().isBlank()
                || livro.getTitulo() == null || livro.getTitulo().isBlank()
                || livro.getAnoDePublicacao() <= 0
                || livro.getCategoria() == null || livro.getCategoria().getId() <= 0)
        {
            System.err.println("\n❌ Dados do livro inválidos para inserção.");
            return false;
        }

        String sql = "INSERT INTO livro " +
                "(titulo, autor, ano_de_publicacao, disponivel, categoria_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            stmt.setString(1, livro.getTitulo().trim());
            stmt.setString(2, livro.getAutor().trim());
            stmt.setInt(3, livro.getAnoDePublicacao());
            stmt.setBoolean(4, livro.isDisponivel());
            stmt.setInt(5, livro.getCategoria().getId());

            int rows = stmt.executeUpdate();

            if(rows > 0){
                try (ResultSet rs = stmt.getGeneratedKeys()){
                    if(rs.next()) livro.setId(rs.getInt(1));
                }
                System.out.println("\n✅ Livro adicionado com sucesso!");
            } else {
                System.err.println("\n❌ Nenhum livro foi adicionado.");
            }

            return rows > 0;
        }catch (SQLException e){
            System.err.println("\n ❌ Erro ao inserir Livro: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Livro> listar(){
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT l.id, l.titulo, l.autor, l.ano_de_publicacao, l.disponivel, " +
                "c.id AS cat_id, c.nome AS cat_nome " +
                "FROM livro l " +
                "LEFT JOIN categoria c ON c.id = l.categoria_id " +
                "ORDER BY c.nome, l.titulo";

        try (Connection connection = DB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)){

            try (ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    int livroId = rs.getInt("id");
                    String titulo = rs.getString("titulo");
                    String autor = rs.getString("autor");
                    int ano = rs.getInt("ano_de_publicacao");
                    boolean disponivel = rs.getBoolean("disponivel");

                    Categoria categoria = null;
                    int catId = rs.getInt("cat_id");
                    if(!rs.wasNull()){
                        categoria = new Categoria(catId, rs.getString("cat_nome"));
                    }

                    livros.add(new Livro(livroId, titulo, autor, ano, disponivel, categoria));
                }
            }
            return livros;
        }catch (SQLException e){
            System.err.println("\n ❌ Erro ao listar Livros: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Livro buscarPorId(int id){
        if(id <= 0) return null;

        Livro livro = null;

        String sql = "SELECT l.id, l.titulo, l.autor, l.ano_de_publicacao, l.disponivel, " +
                "c.id AS cat_id, c.nome AS cat_nome " +
                "FROM livro l " +
                "LEFT JOIN categoria c ON l.categoria_id = c.id " +
                "WHERE l.id = ?";

        try (Connection connection = DB.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    int livroId = rs.getInt("id");
                    String titulo = rs.getString("titulo");
                    String autor = rs.getString("autor");
                    int ano = rs.getInt("ano_de_publicacao");
                    boolean disponivel = rs.getBoolean("disponivel");

                    Categoria categoria = null;
                    int catId = rs.getInt("cat_id");
                    if(!rs.wasNull()){
                        categoria = new Categoria(catId, rs.getString("cat_nome"));
                    }

                    livro = new Livro(livroId, titulo, autor, ano, disponivel, categoria);
                }
            }
            return livro;
        }catch (SQLException e){
            System.err.println("\n ❌ Erro ao buscar livro por ID: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void atualizar(Livro livro){
        if(livro == null
                || livro.getId() <= 0
                || livro.getCategoria() == null || livro.getCategoria().getId() <= 0
                || livro.getAnoDePublicacao() <= 0
                || livro.getTitulo() == null  || livro.getTitulo().isBlank()
                || livro.getAutor() == null || livro.getAutor().isBlank()
        ){
            System.err.println("\n❌ Livro inválido para atualização.");
            return;
        }

        String sql = "UPDATE livro " +
                "SET titulo = ?, autor = ?, ano_de_publicacao = ?, " +
                "disponivel = ?, categoria_id = ? WHERE id = ?";

        try (Connection connection = DB.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setString(1, livro.getTitulo().trim());
            stmt.setString(2, livro.getAutor().trim());
            stmt.setInt(3, livro.getAnoDePublicacao());
            stmt.setBoolean(4, livro.isDisponivel());
            stmt.setInt(5, livro.getCategoria().getId());
            stmt.setInt(6, livro.getId());

            int rows = stmt.executeUpdate();

            if(rows > 0){
                System.out.println("\n✅ Livro atualizado com sucesso!");
            } else {
                System.err.println("\n❌ Nenhum livro encontrado com esse ID.");
            }

        }catch (SQLException e){
            System.err.println("\n❌ Erro ao atualizar livro: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void deletar(int id){
        if(id <= 0){
            System.err.println("\n❌ ID inválido.");
            return;
        }

        String sql = "DELETE FROM livro WHERE id = ?";

        try (Connection connection = DB.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();

            if(rows > 0){
                System.out.println("\n✅ Livro excluído com sucesso!");
            } else {
                System.err.println("\n❌ Livro não encontrado.");
            }
        }catch (SQLException e){
            System.err.println("\n❌ Erro ao excluir livro: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
