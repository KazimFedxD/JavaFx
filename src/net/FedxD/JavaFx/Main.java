package net.FedxD.JavaFx;


import java.nio.file.*;
import java.util.ArrayList;
import java.util.Scanner;


class Main {
    public static void main(String[] args) {
        String filepath = "";
        String code;

        if (args.length == 1) {
            filepath = args[0];
        }

        if (!filepath.isEmpty()) {
            if (filepath.endsWith(".fx")) {
                Path path = Paths.get(filepath);
                try {
                    code = Files.readString(path);
                    Main.run(code, filepath);
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                    return;
                }
            }
            else {
                System.out.println("Error: Invalid file extension");
                return;
            }
        }
        else {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("JavaFx> ");
                code = scanner.nextLine();
                if (code.equals("exit()")) {
                    break;
                }
                Main.run(code, filepath);

            }

        }
    }
    private static void run(String code, String filepath) {
        System.out.println("Running code: " + code);
        Lexer lexer = new Lexer(code);
        LexerResult lexerResult = lexer.makeTokens();
        if (lexerResult.error != null) {
            System.out.println(lexerResult.error);
            return;
        }
        ArrayList<Token> tokens = lexerResult.tokens;
        System.out.println("[");
        tokens.forEach(token -> {
            System.out.println("\t" + token);
        });
        System.out.println("]");
    }
}