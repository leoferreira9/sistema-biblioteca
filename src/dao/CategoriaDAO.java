package dao;

import db.DB;
import model.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public boolean inserir (Categoria categoria){
        if(categoria == null || categoria.getNome() == null || categoria.getNome().isBlank()){
            System.out.println("❌ Nome da categoria é obrigatório.");
            return false;
        }

        String sql = "INSERT INTO categoria (nome) VALUES (?)";

        try (Connection connection = DB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            stmt.setString(1, categoria.getNome().trim());
            int rows = stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()){
                if(rs.next()) categoria.setId(rs.getInt(1));
            }

            if(rows > 0){
                System.out.println("\n✅ Categoria adicionada com sucesso!");
            } else {
                System.err.println("\n❌ Nenhuma categoria foi adicionada.");
            }

            return rows > 0;
        }catch (SQLException e){
            System.err.println("\n❌ Não foi possível inserir uma categoria: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Categoria> listar(){
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT id, nome FROM categoria ORDER BY nome";

        try (Connection connection = DB.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){

            while(rs.next()){
                categorias.add(new Categoria(rs.getInt("id"), rs.getString("nome")));
            }

        }catch (SQLException e){
            System.err.println("\n❌ Erro ao listar categorias: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return categorias;
    }

    public Categoria buscarPorId(int id){
        if(id <= 0) return null;

        String sql = "SELECT id, nome FROM categoria WHERE id = ?";
        Categoria categoria = null;

        try (Connection connection = DB.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    categoria = new Categoria(rs.getInt("id"), rs.getString("nome"));
                }
            }
            return categoria;
        }catch (SQLException e){
            System.err.println("\n❌ Erro ao buscar categoria por ID: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void atualizar(Categoria categoria){
        if(categoria == null || categoria.getNome() == null || categoria.getId() <= 0 || categoria.getNome().isBlank()){
            System.err.println("\n❌ Categoria inválida para atualização.");
            return;
        }

        String sql = "UPDATE categoria SET nome = ? WHERE id = ?";

        try (Connection connection = DB.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setString(1, categoria.getNome());
            stmt.setInt(2, categoria.getId());

            int rows = stmt.executeUpdate();

            if(rows > 0){
                System.out.println("\n✅ Categoria atualizada com sucesso!");
            } else {
                System.out.println("\n❌ Nenhuma categoria encontrada com esse ID.");
            }

        }catch (SQLException e){
            System.err.println("\n❌ Erro ao atualizar categoria: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void deletar(int id){
        if(id <= 0){
            System.err.println("\n❌ ID inválido.");
            return;
        }

        String sql = "DELETE FROM categoria WHERE id = ?";

        try (Connection connection = DB.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setInt(1, id);

            int rows = stmt.executeUpdate();

            if(rows > 0){
                System.out.println("\n✅ Categoria excluída com sucesso!");
            } else {
                System.err.println("❌ Nenhuma categoria encontrada com esse ID.\n");
            }
        }catch (SQLException e){
            System.err.println("\n❌ Erro ao excluir categoria: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
