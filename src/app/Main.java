package app;

import dao.CategoriaDAO;
import dao.EmprestimoDAO;
import dao.LivroDAO;
import dao.UsuarioDAO;
import model.Categoria;
import model.Emprestimo;
import model.Livro;
import model.Usuario;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static final UsuarioDAO usuarioDAO = new UsuarioDAO();
    public static final CategoriaDAO categoriaDAO = new CategoriaDAO();
    public static final EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
    public static final LivroDAO livroDAO = new LivroDAO();

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int opcao;

        do{
            System.out.println("\n==== SISTEMA DE BIBLIOTECA ====");
            System.out.println("1. Gerenciar Categorias");
            System.out.println("2. Gerenciar Livros");
            System.out.println("3. Gerenciar Usuários");
            System.out.println("4. Empréstimos");
            System.out.println("0. Sair");
            opcao = lerInt(sc,"Escolha uma opção: ");

            switch (opcao){
                case 1:
                    menuCategorias(sc);
                    break;
                case 2:
                    menuLivros(sc);
                    break;
                case 3:
                    menuUsuarios(sc);
                    break;
                case 4:
                    menuEmprestimos(sc);
                    break;
                case 0:
                    System.out.println("Encerrando o sistema...");
                    break;
                default:
                    System.err.println("❌ Opção inválida.\n");
            }
        }while(opcao != 0);
    }

    public static void menuCategorias(Scanner sc){
        int opcao;

        do{
            System.out.println("\n-- Gerenciar Categorias --");
            System.out.println("1. Cadastrar categoria");
            System.out.println("2. Listar categorias");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Atualizar categoria");
            System.out.println("5. Deletar categoria");
            System.out.println("0. Voltar ao menu principal");
            opcao = lerInt(sc, "Escolha uma opção: ");

            switch (opcao){
                case 1:
                    String nome = lerString(sc, "\nInforme o nome da categoria: ");
                    categoriaDAO.inserir(new Categoria(nome));
                    break;
                case 2:
                    List<Categoria> categorias = categoriaDAO.listar();
                    if(categorias.isEmpty()){
                        System.err.println("❌ Nenhuma categoria cadastradas.");
                        break;
                    }
                    System.out.println();
                    categorias.forEach(System.out::println);
                    break;
                case 3:
                    int idConsulta = lerInt(sc, "\nInforme o ID que deseja consultar: ");
                    Categoria categoria = categoriaDAO.buscarPorId(idConsulta);
                    if(categoria == null){
                        System.err.println("\n❌ Nenhuma categoria encontrada.");
                        break;
                    }
                    System.out.println("\n" + categoria);
                    break;
                case 4:
                    int idAtualizar = lerInt(sc, "\nInforme o ID da categoria que deseja atualizar: ");
                    String novoNome = lerString(sc, "\nInforme o novo nome da categoria: ");
                    categoriaDAO.atualizar(new Categoria(idAtualizar, novoNome));
                    break;
                case 5:
                    categoriaDAO.deletar(lerInt(sc, "\nInforme o ID da categoria que deseja excluir: "));
                    break;
                case 0:
                    break;
                default:
                    System.err.println("\n❌ Opção inválida!");
            }

        }while (opcao != 0);
    }

    public static void menuLivros(Scanner sc){
        int opcao;

        do{
            System.out.println("\n-- Gerenciar Livros --");
            System.out.println("1. Cadastrar livro");
            System.out.println("2. Listar livros");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Atualizar livro");
            System.out.println("5. Deletar livro");
            System.out.println("0. Voltar ao menu principal");
            opcao = lerInt(sc, "Escolha uma opção: ");

            switch (opcao){
                case 1:
                    String titulo = lerString(sc, "\nInforme o Título do livro: ");
                    String nomeAutor = lerString(sc, "Informe o nome do Autor: ");
                    int ano = lerInt(sc, "Informe o ano de publicação: ");

                    System.out.print("\nO livro está disponível para empréstimo?");
                    System.out.println("\n1.Sim\n2.Não");
                    int estaDisponivel = lerInt(sc, "Escolha uma opção: ");
                    boolean disponivel = estaDisponivel == 1;

                    System.out.println();
                    categoriaDAO.listar().forEach(System.out::println);
                    int idCategoria = lerInt(sc, "Informe o ID de uma das categorias acima para o livro: ");

                    Categoria categoria = categoriaDAO.buscarPorId(idCategoria);
                    if(categoria == null){
                        System.err.println("❌ Categoria inválida.");
                        break;
                    }

                    livroDAO.inserir(new Livro(titulo, nomeAutor, ano, disponivel, categoria));
                    break;
                case 2:
                    List<Livro> livros = livroDAO.listar();
                    if(livros.isEmpty()){
                        System.err.println("❌ Não há livros cadastrados.");
                        break;
                    }
                    livros.forEach(System.out::println);
                    break;
                case 3:
                    int idLivro = lerInt(sc, "\nInforme o ID do livro que deseja consultar: ");
                    Livro livro = livroDAO.buscarPorId(idLivro);
                    if(livro == null){
                        System.err.println("\n❌ Nenhum livro encontrado com o ID informado.");
                        break;
                    }
                    System.out.println(livro);
                    break;
                case 4:
                    int idLivroAtualizar = lerInt(sc, "\nInforme o ID do livro que deseja atualizar: ");
                    String tituloNovo = lerString(sc, "Informe o título do livro: ");
                    String autorNovo = lerString(sc, "Informe o autor do livro: ");

                    System.out.println();
                    int anoPublicacaoNovo = lerInt(sc, "Informe o ano de publicação do livro: ");

                    System.out.print("\nO livro está disponível para empréstimo?");
                    System.out.println("\n1.Sim\n2.Não");
                    int novoDisponivel = lerInt(sc, "Escolha uma opção: ");

                    System.out.println();
                    List<Categoria> categorias = categoriaDAO.listar();
                    if(categorias.isEmpty()){
                        System.err.println("❌ Nenhuma categoria cadastrada.");
                        break;
                    }
                    categorias.forEach(System.out::println);
                    int idNovaCategoria = lerInt(sc, "Informe o ID de uma das categorias acima para o livro: ");

                    Categoria novaCategoria = categoriaDAO.buscarPorId(idNovaCategoria);
                    if(novaCategoria == null){
                        System.err.println("❌ Categoria inválida.");
                        break;
                    }

                    Livro livroAtualizado = new Livro(idLivroAtualizar, tituloNovo, autorNovo, anoPublicacaoNovo ,(novoDisponivel == 1 ? true : false), novaCategoria);
                    livroDAO.atualizar(livroAtualizado);
                    break;
                case 5:
                    livroDAO.deletar(lerInt(sc, "\nInforme o ID do livro que deseja excluir: "));
                    break;
                case 0:
                    break;
                default:
                    System.err.println("\n❌ Opção inválida!");
            }

        }while (opcao != 0);
    }

    public static void menuUsuarios(Scanner sc){
        int opcao;

        do{
            System.out.println("\n-- Gerenciar Usuários --");
            System.out.println("1. Cadastrar usuário");
            System.out.println("2. Listar usuários");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Atualizar usuário");
            System.out.println("5. Deletar usuário");
            System.out.println("0. Voltar ao menu principal");
            opcao = lerInt(sc, "Escolha uma opção: ");

            switch (opcao){
                case 1:
                    String nome = lerString(sc, "\nInforme o nome do usuário: ");
                    String cpf = lerString(sc, "Informe o CPF do usuário: ");
                    String email = lerString(sc, "Informe o Email do usuário: ");

                    Usuario usuario = new Usuario(nome, cpf, email);
                    usuarioDAO.inserir(usuario);
                    break;
                case 2:
                    List<Usuario> usuarios = usuarioDAO.listar();
                    if(usuarios.isEmpty()){
                        System.err.println("\n❌ Não há usuários cadastrados.");
                        break;
                    }
                    usuarios.forEach(System.out::println);
                    break;
                case 3:
                    int id = lerInt(sc, "\nInforme o ID do usuário que deseja buscar: ");

                    Usuario usuarioEncontrado = usuarioDAO.buscarPorId(id);
                    if(usuarioEncontrado == null){
                        System.err.println("\n❌ Usuário não encontrado.");
                        break;
                    }
                    System.out.println(usuarioEncontrado);
                    break;
                case 4:
                    int idAtualizar = lerInt(sc, "\nInforme o ID do usuário que deseja atualizar: ");

                    String novoNome = lerString(sc, "Informe o nome do usuário: ");
                    String novoCpf = lerString(sc, "Informe o CPF do usuário: ");
                    String novoEmail = lerString(sc, "Informe o Email do usuário: ");

                    Usuario usuarioAtualizado = new Usuario(idAtualizar, novoNome, novoCpf, novoEmail);
                    usuarioDAO.atualizar(usuarioAtualizado);
                    break;
                case 5:
                    int idExcluir = lerInt(sc, "\nInforme o ID do usuário que deseja excluir: ");
                    usuarioDAO.deletar(idExcluir);
                    break;
                case 0:
                    break;
                default:
                    System.err.println("\n❌ Opção inválida!");
            }
        }while (opcao != 0);
    }

    public static void menuEmprestimos(Scanner sc){
        int opcao;
        do {
            System.out.println("\n-- Empréstimos --");
            System.out.println("1. Realizar empréstimo");
            System.out.println("2. Registrar devolução");
            System.out.println("3. Listar empréstimos");
            System.out.println("4. Buscar por ID");
            System.out.println("5. Excluir empréstimo (se devolvido)");
            System.out.println("0. Voltar ao menu principal");
            opcao = lerInt(sc, "Escolha uma opção: ");

            switch (opcao){
                case 1:
                    List<Usuario> usuarios = usuarioDAO.listar();
                    List<Livro> livros = livroDAO.listar();

                    if(usuarios.isEmpty()){
                        System.err.println("\n❌ Nenhum usuário cadastrado.");
                        break;
                    }
                    if(livros.isEmpty()){
                        System.err.println("\n❌ Nenhum livro cadastrado.");
                        break;
                    }
                    System.out.println();
                    usuarios.forEach(System.out::println);
                    int idUsuario = lerInt(sc, "\nInforme o ID do usuário dono do empréstimo: ");

                    System.out.println();
                    livros.forEach(System.out::println);
                    int idLivro = lerInt(sc, "\nInforme o ID do livro que será emprestado: ");

                    emprestimoDAO.registrarEmprestimo(idUsuario, idLivro, LocalDate.now());
                    break;
                case 2:
                    List<Emprestimo> emprestimos = emprestimoDAO.listar();
                    if(emprestimos.isEmpty()){
                        System.err.println("\n❌ Não há empréstimos cadastrados.");
                        break;
                    }
                    System.out.println();
                    emprestimos.forEach(System.out::println);
                    int idDevolucao = lerInt(sc, "\nInforme o ID do empréstimo que deseja devolver: ");
                    emprestimoDAO.registrarDevolucao(idDevolucao, LocalDate.now());
                    break;
                case 3:
                    List<Emprestimo> emprestimos1 = emprestimoDAO.listar();
                    emprestimos1.forEach(System.out::println);
                    break;
                case 4:
                    int idConsulta = lerInt(sc, "\nInforme o ID do empréstimo que deseja consultar: ");
                    Emprestimo emprestimo = emprestimoDAO.buscarPorId(idConsulta);
                    if(emprestimo != null){
                        System.out.println(emprestimo);
                    }
                    break;
                case 5:
                    int idExcluir = lerInt(sc, "\nInforme o ID do empréstimo que deseja excluir: ");
                    emprestimoDAO.deletar(idExcluir);
                    break;
                case 0:
                    break;
                default:
                    System.err.println("\n❌ Opção inválida!");
            }
        }while(opcao != 0);
    }

    //Métodos
    public static int lerInt(Scanner sc, String mensagem){
        while(true){
            try {
                System.out.print(mensagem);
                return Integer.parseInt(sc.nextLine());
            }catch (NumberFormatException e){
                System.out.println("\n❌ Entrada inválida. Digite um número inteiro.");
            }
        }
    }

    public static String lerString(Scanner sc, String mensagem){
        System.out.print(mensagem);
        return sc.nextLine();
    }
}
