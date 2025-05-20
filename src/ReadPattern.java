package src;

import java.io.BufferedReader;
import java.io.Reader;
import src.modele.UniversModel;

public class ReadPattern {

    public ReadPattern() {

    }

    public static void readPattern(UniversModel model, Reader reader) throws Exception {
        BufferedReader br = new BufferedReader(reader);

        String inputLine = null;
        int x = 0, y = 0;
        int paramArgument = 0;

        while ((inputLine = br.readLine()) != null) {
            if (inputLine.startsWith("x") || inputLine.startsWith("#")) {
                continue;
            }

            inputLine = inputLine.trim();

            for (int i = 0; i < inputLine.length(); i++) {
                char c = inputLine.charAt(i);
                int param = (paramArgument == 0 ? 1 : paramArgument);

                if (c == 'b') {
                    x += param;
                    paramArgument = 0;
                } else if (c == 'o') {
                    while (param-- > 0) {
                        model.setCellule(x++, y);
                    }
                    paramArgument = 0;
                } else if (c == '$') {
                    y += param;
                    x = 0;
                    paramArgument = 0;
                } else if ('0' <= c && c <= '9') {
                    paramArgument = 10 * paramArgument + c - '0';
                } else if (c == '!') {
                    return;
                } else {
                    usage("In the input, I saw the character " + c
                            + " which is illegal in the RLE subset I know how to handle.");
                }
            }
        }
    }

    static void usage(String s) {
        System.out.println(s);
        System.out.println("Usage:  java Driver <classname> <pattern.rle");
        System.exit(10);
    }
}
