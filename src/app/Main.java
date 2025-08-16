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
            System.out.print("Escolha uma opção: ");
            opcao = sc.nextInt();
            sc.nextLine();

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
                    System.out.println("❌ Opção inválida.");
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
            System.out.print("Escolha uma opção: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao){
                case 1:
                    System.out.print("\nInforme o nome da categoria: ");
                    String nome = sc.nextLine();
                    categoriaDAO.inserir(new Categoria(nome));
                    break;
                case 2:
                    List<Categoria> categorias = categoriaDAO.listar();
                    if(categorias.isEmpty()){
                        System.out.println("❌ Nenhuma categoria cadastradas.");
                        break;
                    }
                    System.out.println();
                    categorias.forEach(System.out::println);
                    break;
                case 3:
                    System.out.print("\nInforme o ID que deseja consultar: ");
                    int idConsulta = sc.nextInt();
                    sc.nextLine();
                    Categoria categoria = categoriaDAO.buscarPorId(idConsulta);
                    if(categoria == null){
                        System.out.println("\n❌ Nenhuma categoria encontrada.");
                        break;
                    }
                    System.out.println("\n" + categoria);
                    break;
                case 4:
                    System.out.print("\nInforme o ID da categoria que deseja atualizar: ");
                    int idAtualizar = sc.nextInt();
                    sc.nextLine();
                    System.out.print("\nInforme o novo nome da categoria: ");
                    String novoNome = sc.nextLine();
                    categoriaDAO.atualizar(new Categoria(idAtualizar, novoNome));
                    break;
                case 5:
                    System.out.print("\nInforme o ID da categoria que deseja excluir: ");
                    categoriaDAO.deletar(sc.nextInt());
                    break;
                case 0:
                    break;
                default:
                    System.out.println("\n❌ Opção inválida!");
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
            System.out.print("Escolha uma opção: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao){
                case 1:
                    System.out.print("\nInforme o Título do livro: ");
                    String titulo = sc.nextLine();
                    System.out.print("Informe o nome do Autor: ");
                    String nomeAutor = sc.nextLine();
                    System.out.print("Informe o ano de publicação: ");
                    int ano = sc.nextInt();

                    System.out.print("\nO livro está disponível para empréstimo?");
                    System.out.println("\n1.Sim\n2.Não");
                    System.out.print("Escolha uma opção: ");
                    int estaDisponivel = sc.nextInt();
                    sc.nextLine();
                    boolean disponivel = estaDisponivel == 1;

                    System.out.println();
                    categoriaDAO.listar().forEach(System.out::println);
                    System.out.print("Informe o ID de uma das categorias acima para o livro: ");
                    int idCategoria = sc.nextInt();
                    sc.nextLine();

                    Categoria categoria = categoriaDAO.buscarPorId(idCategoria);
                    if(categoria == null){
                        System.out.println("❌ Categoria inválida.");
                        break;
                    }

                    livroDAO.inserir(new Livro(titulo, nomeAutor, ano, disponivel, categoria));
                    break;
                case 2:
                    List<Livro> livros = livroDAO.listar();
                    if(livros.isEmpty()){
                        System.out.println("❌ Não há livros cadastrados.");
                        break;
                    }
                    livros.forEach(System.out::println);
                    break;
                case 3:
                    System.out.print("\nInforme o ID do livro que deseja consultar: ");
                    int idLivro = sc.nextInt();
                    sc.nextLine();
                    Livro livro = livroDAO.buscarPorId(idLivro);
                    if(livro == null){
                        System.out.println("\n❌ Nenhum livro encontrado com o ID informado.");
                        break;
                    }
                    System.out.println(livro);
                    break;
                case 4:
                    System.out.print("\nInforme o ID do livro que deseja atualizar: ");
                    int idLivroAtualizar = sc.nextInt();
                    sc.nextLine();

                    System.out.println("Informe o título do livro: ");
                    String tituloNovo = sc.nextLine();

                    System.out.println("Informe o autor do livro: ");
                    String autorNovo = sc.nextLine();

                    System.out.println("Informe o ano de publicação do livro: ");
                    int anoPublicacaoNovo = sc.nextInt();
                    sc.nextLine();

                    System.out.print("\nO livro está disponível para empréstimo?");
                    System.out.println("\n1.Sim\n2.Não");
                    System.out.print("Escolha uma opção: ");
                    int novoDisponivel = sc.nextInt();
                    sc.nextLine();

                    System.out.println();
                    List<Categoria> categorias = categoriaDAO.listar();
                    if(categorias.isEmpty()){
                        System.out.println("❌ Nenhuma categoria cadastrada.");
                        break;
                    }
                    categorias.forEach(System.out::println);
                    System.out.print("Informe o ID de uma das categorias acima para o livro: ");
                    int idNovaCategoria = sc.nextInt();
                    sc.nextLine();

                    Categoria novaCategoria = categoriaDAO.buscarPorId(idNovaCategoria);
                    if(novaCategoria == null){
                        System.out.println("❌ Categoria inválida.");
                        break;
                    }

                    Livro livroAtualizado = new Livro(idLivroAtualizar, tituloNovo, autorNovo, anoPublicacaoNovo ,(novoDisponivel == 1 ? true : false), novaCategoria);
                    livroDAO.atualizar(livroAtualizado);
                    break;
                case 5:
                    System.out.print("\nInforme o ID do livro que deseja excluir: ");
                    livroDAO.deletar(sc.nextInt());
                    break;
                case 0:
                    break;
                default:
                    System.out.println("\n❌ Opção inválida!");
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
            System.out.print("Escolha uma opção: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao){
                case 1:
                    System.out.print("\nInforme o nome do usuário: ");
                    String nome = sc.nextLine();
                    System.out.print("Informe o CPF do usuário: ");
                    String cpf = sc.nextLine();
                    System.out.print("Informe o Email do usuário: ");
                    String email = sc.nextLine();

                    Usuario usuario = new Usuario(nome, cpf, email);
                    usuarioDAO.inserir(usuario);
                    break;
                case 2:
                    List<Usuario> usuarios = usuarioDAO.listar();
                    if(usuarios.isEmpty()){
                        System.out.println("\n❌ Não há usuários cadastrados.");
                        break;
                    }
                    usuarios.forEach(System.out::println);
                    break;
                case 3:
                    System.out.print("\nInforme o ID do usuário que deseja buscar: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    Usuario usuarioEncontrado = usuarioDAO.buscarPorId(id);
                    if(usuarioEncontrado == null){
                        System.out.println("\n❌ Usuário não encontrado.");
                        break;
                    }
                    System.out.println(usuarioEncontrado);
                    break;
                case 4:
                    System.out.print("\nInforme o ID do usuário que deseja atualizar: ");
                    int idAtualizar = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Informe o nome do usuário: ");
                    String novoNome = sc.nextLine();
                    System.out.print("Informe o CPF do usuário: ");
                    String novoCpf = sc.nextLine();
                    System.out.print("Informe o Email do usuário: ");
                    String novoEmail = sc.nextLine();

                    Usuario usuarioAtualizado = new Usuario(idAtualizar, novoNome, novoCpf, novoEmail);
                    usuarioDAO.atualizar(usuarioAtualizado);
                    break;
                case 5:
                    System.out.print("\nInforme o ID do usuário que deseja excluir: ");
                    int idExcluir = sc.nextInt();
                    usuarioDAO.deletar(idExcluir);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("\n❌ Opção inválida!");
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
            System.out.print("Escolha uma opção: ");
            opcao = sc.nextInt();

            switch (opcao){
                case 1:
                    List<Usuario> usuarios = usuarioDAO.listar();
                    List<Livro> livros = livroDAO.listar();

                    if(usuarios.isEmpty()){
                        System.out.println("\n❌ Nenhum usuário cadastrado.");
                        break;
                    }
                    if(livros.isEmpty()){
                        System.out.println("\n❌ Nenhum livro cadastrado.");
                        break;
                    }
                    System.out.println();
                    usuarios.forEach(System.out::println);
                    System.out.print("\nInforme o ID do usuário dono do empréstimo: ");
                    int idUsuario = sc.nextInt();
                    sc.nextLine();

                    System.out.println();
                    livros.forEach(System.out::println);
                    System.out.print("\nInforme o ID do livro que será emprestado: ");
                    int idLivro = sc.nextInt();
                    sc.nextLine();

                    emprestimoDAO.registrarEmprestimo(idUsuario, idLivro, LocalDate.now());
                    break;
                case 2:
                    List<Emprestimo> emprestimos = emprestimoDAO.listar();
                    if(emprestimos.isEmpty()){
                        System.out.println("\n❌ Não há empréstimos cadastrados.");
                        break;
                    }
                    System.out.println();
                    emprestimos.forEach(System.out::println);
                    System.out.print("\nInforme o ID do empréstimo que deseja devolver: ");
                    int idDevolucao = sc.nextInt();
                    emprestimoDAO.registrarDevolucao(idDevolucao, LocalDate.now());
                    break;
                case 3:
                    List<Emprestimo> emprestimos1 = emprestimoDAO.listar();
                    emprestimos1.forEach(System.out::println);
                    break;
                case 4:
                    System.out.print("\nInforme o ID do empréstimo que deseja consultar: ");
                    int idConsulta = sc.nextInt();
                    Emprestimo emprestimo = emprestimoDAO.buscarPorId(idConsulta);
                    if(emprestimo != null){
                        System.out.println(emprestimo);
                    }
                    break;
                case 5:
                    System.out.print("\nInforme o ID do empréstimo que deseja excluir: ");
                    int idExcluir = sc.nextInt();
                    emprestimoDAO.deletar(idExcluir);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("\n❌ Opção inválida!");
            }
        }while(opcao != 0);
    }
}
