package dao;

import db.DB;
import model.Emprestimo;
import model.Livro;
import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO {

    private final LivroDAO livroDAO = new LivroDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public boolean registrarEmprestimo(int usuarioId, int livroId, LocalDate data){
        if(usuarioId <= 0 || livroId <= 0 || data == null) {
            System.out.println("\n❌ Dados inválidos.");
            return false;
        }

        Livro livro = livroDAO.buscarPorId(livroId);
        Usuario usuario = usuarioDAO.buscarPorId(usuarioId);

        if(livro == null){
            System.err.println("\n❌ Livro não encontrado.");
            return false;
        } else if(livro.isDisponivel() == false){
            System.err.println("\n❌ Livro não disponível para empréstimo.");
            return false;
        } else if (usuario == null){
            System.err.println("\n❌ Usuário não encontrado.");
            return false;
        }

        String sql = "INSERT INTO emprestimo (usuario_id, livro_id, data_emprestimo) " +
                "VALUES (?, ?, ?)";

        String updateSQL = "UPDATE livro SET disponivel = ? WHERE id = ?";

        try (Connection connection = DB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             PreparedStatement stmtUpdate = connection.prepareStatement(updateSQL)){

            connection.setAutoCommit(false);

            stmt.setInt(1, usuarioId);
            stmt.setInt(2, livroId);
            stmt.setDate(3, java.sql.Date.valueOf(data));

            int rows = stmt.executeUpdate();

            if(rows > 0){
                stmtUpdate.setBoolean(1, false);
                stmtUpdate.setInt(2, livroId);
                stmtUpdate.executeUpdate();

                connection.commit();
                System.out.println("\n✅ Empréstimo criado com sucesso!");
                return true;
            } else {
                connection.rollback();
                System.err.println("\n❌ Nenhum empréstimo foi criado.");
                return false;
            }
        }catch (SQLException e){
            System.err.println("\n❌ Não foi possível registrar um empréstimo: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean registrarDevolucao(int emprestimoId, LocalDate dataDevolucao){
        if(emprestimoId <= 0){
            System.err.println("\n❌ ID de empréstimo inválido.");
            return false;
        }

        if(dataDevolucao == null || dataDevolucao.isAfter(LocalDate.now())){
            System.err.println("\n❌ Data de devolução inválida.");
            return false;
        }

        String sql = "SELECT livro_id, data_emprestimo, data_devolucao " +
                "FROM emprestimo WHERE id = ?";

        try (Connection connection = DB.getConnection()){
            connection.setAutoCommit(false);

            try (PreparedStatement stmt = connection.prepareStatement(sql)){

                stmt.setInt(1, emprestimoId);

                try (ResultSet rs = stmt.executeQuery()) {
                    if(!rs.next()){
                        System.err.println("\n❌ Empréstimo não encontrado.");
                        connection.rollback();
                        return false;
                    }

                    if(rs.getDate("data_devolucao") != null){
                        System.err.println("\n❌ Livro já devolvido.");
                        connection.rollback();
                        return false;
                    }

                    LocalDate dataEmp = rs.getDate("data_emprestimo").toLocalDate();
                    if(dataDevolucao.isBefore(dataEmp)){
                        System.err.println("\n❌ Data de devolução não pode ser antes do empréstimo.");
                        connection.rollback();
                        return false;
                    }

                    int livroId = rs.getInt("livro_id");

                    String updateSQL = "UPDATE emprestimo SET data_devolucao = ? " +
                            "WHERE id = ? AND data_devolucao IS NULL";

                    try (PreparedStatement stmtUpdate = connection.prepareStatement(updateSQL)) {
                        stmtUpdate.setDate(1, java.sql.Date.valueOf(dataDevolucao));
                        stmtUpdate.setInt(2, emprestimoId);
                        int updateRows = stmtUpdate.executeUpdate();

                        if(updateRows == 0){
                            System.err.println("\n❌ Não foi possível registrar a devolução.");
                            connection.rollback();
                            return false;
                        }
                    }

                    // 2) Libera o livro
                    String updateLivro = "UPDATE livro SET disponivel = true " +
                            "WHERE id = ? AND disponivel = false";

                    try (PreparedStatement updateLivroStmt = connection.prepareStatement(updateLivro)) {
                        updateLivroStmt.setInt(1, livroId);
                        int rows = updateLivroStmt.executeUpdate();
                        if(rows == 0){
                            System.err.println("\n❌ Não foi possível liberar o livro (estado inconsistente).");
                            connection.rollback();
                            return false;
                        }
                    }
                }

                connection.commit();
                System.out.println("\n✅ Devolução de empréstimo registrada com sucesso!");
                return true;

            } catch (SQLException e) {
                try { connection.rollback(); } catch (SQLException ignore) {}
                System.err.println("\n❌ Não foi possível devolver o livro: " + e.getMessage());
                throw new RuntimeException(e);
            }

        } catch (SQLException e){
            System.err.println("\n❌ Não foi possível devolver o livro: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Emprestimo> listar(){

        List<Emprestimo> emprestimos = new ArrayList<>();

        String sql = "SELECT u.id AS usuario_id, u.nome, " +
                "l.id AS livro_id, l.titulo, " +
                "e.id AS emprestimo_id, " +
                "e.data_emprestimo, e.data_devolucao " +
                "FROM emprestimo e INNER JOIN usuario u ON u.id = e.usuario_id " +
                "INNER JOIN livro l ON l.id = e.livro_id " +
                "ORDER BY e.data_emprestimo DESC, u.nome, l.titulo";

        try (Connection connection = DB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)){

            try(ResultSet rs = stmt.executeQuery()){

                if(!rs.isBeforeFirst()){
                    System.err.println("\n❌ Não há empréstimos cadastrados.");
                    return emprestimos;
                }

                while(rs.next()){
                    int usuarioId = rs.getInt("usuario_id");
                    int livroId = rs.getInt("livro_id");

                    Usuario usuario = usuarioDAO.buscarPorId(usuarioId);
                    Livro livro = livroDAO.buscarPorId(livroId);

                    LocalDate dataEmprestimo = rs.getDate("data_emprestimo").toLocalDate();
                    LocalDate dataDev =  rs.getDate("data_devolucao") != null
                            ? rs.getDate("data_devolucao").toLocalDate()
                            : null;

                    emprestimos.add(new Emprestimo(usuario, livro, dataEmprestimo, dataDev));
                }
            }
            return emprestimos;
        }catch (SQLException e){
            System.err.println("\n❌ Não foi possível listar todos os empréstimos: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Emprestimo buscarPorId(int id){
        if(id <= 0){
            System.err.println("\n❌ ID de empréstimo inválido.");
            return null;
        }

        String sql = "SELECT e.id AS emprestimo_id, e.usuario_id, e.livro_id, " +
                "e.data_emprestimo, e.data_devolucao " +
                "FROM emprestimo e WHERE e.id = ?";

        try (Connection connection = DB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()){
                if(!rs.next()){
                    System.err.println("\n❌ Empréstimo não encontrado.");
                    return null;
                }

                int usuarioId = rs.getInt("usuario_id");
                int livroId = rs.getInt("livro_id");

                Usuario usuario = usuarioDAO.buscarPorId(usuarioId);
                Livro livro = livroDAO.buscarPorId(livroId);

                LocalDate dataEmprestimo = rs.getDate("data_emprestimo").toLocalDate();
                java.sql.Date devSql = rs.getDate("data_devolucao");
                LocalDate dataDev = (devSql != null) ? devSql.toLocalDate() : null;

                int emprestimoId = rs.getInt("emprestimo_id");
                return new Emprestimo(emprestimoId, usuario, livro, dataEmprestimo, dataDev);
            }
        }catch (SQLException e){
            System.err.println("\n❌ Erro ao buscar empréstimo por ID: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean deletar(int emprestimoId){
        if(emprestimoId <= 0){
            System.err.println("\n❌ ID de empréstimo inválido.");
            return false;
        }

        String selectSql = "SELECT data_devolucao FROM emprestimo WHERE id = ?";
        String deleteSql = "DELETE FROM emprestimo WHERE id = ?";

        try (Connection connection = DB.getConnection()){
            connection.setAutoCommit(false);

            try (PreparedStatement stmt = connection.prepareStatement(selectSql)) {
                stmt.setInt(1, emprestimoId);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        System.err.println("\n❌ Empréstimo não encontrado.");
                        connection.rollback();
                        return false;
                    }

                    java.sql.Date devSql = rs.getDate("data_devolucao");
                    if (devSql == null) {
                        System.err.println("\n❌ Empréstimo em aberto — devolva antes de excluir.");
                        connection.rollback();
                        return false;
                    }
                }
            }

            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSql)){
                deleteStmt.setInt(1, emprestimoId);
                int rows = deleteStmt.executeUpdate();
                if(rows == 0){
                    System.err.println("\n❌ Não foi possível excluir o empréstimo.");
                    connection.rollback();
                    return false;
                }
            }

            connection.commit();
            System.out.println("\n✅ Empréstimo excluído com sucesso!");
            return true;

        } catch (SQLException e){
            System.err.println("\n❌ Erro ao excluir empréstimo: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
