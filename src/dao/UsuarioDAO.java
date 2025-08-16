package dao;

import db.DB;
import model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public boolean inserir(Usuario usuario){
        if(usuario == null
            || usuario.getCpf() == null || usuario.getCpf().isBlank()
            || usuario.getEmail() == null || usuario.getEmail().isBlank()
            || usuario.getNome() == null || usuario.getNome().isBlank())
        {
            System.err.println("\n❌ Dados do usuario inválidos para inserção.");
            return false;
        }

        String sql = "INSERT INTO usuario (nome, cpf, email) VALUES (?, ?, ?)";

        try (Connection connection = DB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            stmt.setString(1, usuario.getNome().trim());
            stmt.setString(2, usuario.getCpf().trim());
            stmt.setString(3, usuario.getEmail().trim());

            int rows = stmt.executeUpdate();

            if(rows > 0){
                try (ResultSet rs = stmt.getGeneratedKeys()){
                    if(rs.next()) usuario.setId(rs.getInt(1));
                }

                System.out.println("\n✅ Usuário adicionado com sucesso!");
            } else {
                System.err.println("\n❌ Nenhum usuário foi adicionado.");
            }
            return rows > 0;
        }catch (SQLException e){
            System.err.println("\n❌ Erro ao inserir usuário: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Usuario> listar(){
        List<Usuario> usuarios = new ArrayList<>();

        String sql = "SELECT id, nome, cpf, email FROM usuario ORDER BY nome";

        try (Connection connection = DB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)){

            try (ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
                    String cpf = rs.getString("cpf");
                    String email = rs.getString("email");
                    usuarios.add(new Usuario(id, nome, cpf, email));
                }
            }
            return usuarios;
        }catch (SQLException e){
            System.err.println("\n❌ Erro ao listar usuários: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Usuario buscarPorId(int id){
        if(id <= 0){
            System.err.println("\n❌ ID inválido para busca.");
            return null;
        }

        String sql = "SELECT id, nome, cpf, email FROM usuario WHERE id = ?";

        try (Connection connection = DB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()){
                if(rs.next()) {
                    return new Usuario(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("cpf"),
                            rs.getString("email")
                    );
                }
            }
            return null;
        }catch (SQLException e){
            System.err.println("\n❌ Erro ao buscar usuário por ID: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void atualizar(Usuario usuario){
        if(usuario == null
        || usuario.getId() <= 0
        || usuario.getNome() == null || usuario.getNome().isBlank()
        || usuario.getCpf() == null || usuario.getCpf().isBlank()
        || usuario.getEmail() == null || usuario.getEmail().isBlank())
        {
            System.err.println("\n❌ Dados do usuario inválidos para atualização.");
            return;
        }

        String sql = "UPDATE usuario SET nome = ?, cpf = ?, email = ? WHERE id = ?";

        try (Connection connection = DB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setString(1, usuario.getNome().trim());
            stmt.setString(2, usuario.getCpf().trim());
            stmt.setString(3, usuario.getEmail().trim());
            stmt.setInt(4, usuario.getId());

            int rows = stmt.executeUpdate();
            if(rows > 0){
                System.out.println("\n✅ Usuário atualizado com sucesso!");
            } else {
                System.err.println("❌ Nenhum usuário encontrado com esse ID.\n");
            }

        }catch (SQLException e){
            System.err.println("\n❌ Erro ao atualizar usuário: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean deletar(int id){
        if(id <= 0){
            System.err.println("\n❌ ID de usuário inválido.");
            return false;
        }

        String sql = "DELETE FROM usuario WHERE id = ?";

        try (Connection connection = DB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setInt(1, id);

            int rows = stmt.executeUpdate();
            if(rows > 0){
                System.out.println("\n✅ Usuário excluído com sucesso!");
            } else {
                System.err.println("\n❌ Nenhum usuário foi encontrado.");
            }

            return rows > 0;
        }catch (SQLException e){
            System.err.println("\n❌ Erro ao deletar usuário: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
