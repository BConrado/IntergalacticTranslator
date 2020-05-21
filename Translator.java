import java.io.File;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
//Bruno Naibert Conrado
public class Translator {

    private static Map<String, String> words;
    private static Map<String, Double> minerals;

    private static double translateRomanNum(char caractere) {
        // retorns the singular roman algarism translated
        return Math.floor(Math.pow(10, "IXCM".indexOf(caractere)))
                + 5 * Math.floor(Math.pow(10, "VLD".indexOf(caractere)));
    }

    // method called when you wanted translated a entire roman algarism E.g. III = 3
    private static double translator(String num) {
        double result = 0;
        int numberAtRight = 0;
        for (int i = num.length() - 1; i >= 0; i--) {
            int value = (int) translateRomanNum(num.charAt(i));
            result += value * Math.signum(value + 0.5 - numberAtRight);
            numberAtRight = value;
        }

        return result;
    }

    // verify if the line received there`s no unknown word
    private static boolean verifyLineM(String[] vet) {
        for (int i = 0; i < vet.length - 4; i++) {
            if (!words.containsKey(vet[i])) {
                return false;
            }
        }
        return true;
    }

    // verify if the line received there`s no unknown word
    private static boolean verifyLineQ(String[] vet) {
        for (int i = 3; i < vet.length - 2; i++) {
            if (!words.containsKey(vet[i])) {
                return false;
            }
        }
        return true;
    }

    private static void exec(File file){
        try{
            Scanner input = new Scanner(file);
            while (input.hasNextLine()) {// starting reading all the lines of the file

                String line = input.nextLine(); // full line
                String[] lineArray = line.split(" "); // each word of the line become and independent item in the vector
                if (lineArray.length == 3) { // if the line have just 3 words it is the notation of roman algarism in
                                             // intergalactic language
                    words.put(lineArray[0], lineArray[2]);
                } else if (!lineArray[lineArray.length - 1].equals("?")) { // if it doesnt finish with ? it means that
                                                                           // its and aggregation for a mineral value
                    if (!verifyLineM(lineArray)) {
                        System.out.println("I have no idea what you are talking about"); // error theres an word that is
                                                                                         // not in the language(hashmap)
                    } else {
                        // boolean stop = true;
                        int a = 0;
                        String mineralValue = ""; //
                        for (int i = 0; i < lineArray.length; i++) {
                            if (words.containsKey(lineArray[i])) { // we read every word that is a mineral and put in a
                                                                   // single word for translate later
                                mineralValue = mineralValue + words.get(lineArray[i]);
                                a++;
                            }
                        }
                        double unitValue;
                        Double val = Double.parseDouble(lineArray[a + 2]);
                        // System.out.println(val);
                        // System.out.println(mineralValue);

                        unitValue = val / (translator(mineralValue)); // gets the value of one unit of a mineral

                        // System.out.println(unitValue);

                        minerals.put(lineArray[a], unitValue); // saves in a hashmap the name of the mineral and its
                                                               // unit price
                    }

                } else {
                    if (lineArray[1].equals("vale")) { // if the second word is vale we know that it will ask how much
                                                       // is an aggregation of words
                        boolean stop = false;
                        String answer = "";
                        String mineralValue = "";
                        for (int i = 2; i < lineArray.length - 1; i++) {
                            if (words.containsKey(lineArray[i])) {
                                mineralValue = mineralValue + words.get(lineArray[i]);// we read every word that is a
                                                                                      // mineral and put in a
                                                                                    // single word for translate later
                                answer = answer + " " + lineArray[i];
                            } else {
                                System.out.println("I have no idea what you are talking about"); // error theres an word
                                                                                                 // that is not in the
                                                                                                 // language(hashmap)
                                stop = true;
                                break;
                            }

                        }
                        if (!stop) {
                            Double x = translator(mineralValue); //prints the answer for the question
                            answer = answer + " is " + x;
                            System.out.println(answer);
                        }

                    } else { // if its an credit ask
                        if (!verifyLineQ(lineArray)) {
                            System.out.println("I have no idea what you are talking about"); // error theres an word
                                                                                             // that is not in the
                                                                                             // language(hashmap)
                        } else {
                            boolean stop = false;
                            String answer = "";
                            String mineralValue = "";
                            for (int i = 3; i < lineArray.length - 1; i++) {
                                if (words.containsKey(lineArray[i])) {
                                    mineralValue = mineralValue + words.get(lineArray[i]);
                                    answer = answer + " " + lineArray[i]; //
                                }
                            }
                            //get all the information and print the answer for the read question
                            String mineral = lineArray[lineArray.length - 2];
                            double x;

                            x = translator(mineralValue);

                            double answerValue = x * minerals.get(mineral);
                            answer = answer + " " + mineral + " " + "is " + answerValue;
                            System.out.println(answer); 
                        }

                    }
                }

            }
        }catch(Exception e) {
            System.out.println("Erro na leitura do arquivo");
        }
        
    }

    public static void main(String[] args) {
        words = new HashMap<String, String>(); // hashmap that contains the word and their value in roman
        minerals = new HashMap<String, Double>(); // hashmap that contains the mineral and their singular value
        File file = new File("entradas.txt");
        try {
            exec(file);
        } catch (Exception e) {
            System.out.println("Erro na execucao" + e);
        }

        // System.out.println(words); //testing the words
        // System.out.println(translator("MMVI")); test
        // System.out.println(minerals);
    }
}