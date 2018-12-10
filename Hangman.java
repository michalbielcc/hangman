import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.time.Instant;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.Writer;
import java.io.FileWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.IndexOutOfBoundsException;

public class Hangman {


  private static ArrayList<String> readFile(String path) {
    
      File text = new File(path);
      ArrayList<String> pairs = new ArrayList<String>();

      try {
      Scanner sc = new Scanner(text);
      while(sc.hasNextLine()) {
        String line = sc.nextLine();
        pairs.add(line);
        }
      }
      catch (FileNotFoundException ex) {
        System.out.println("no such file");
      }      
    return pairs;
  }


  private static void saveToFile(String data, String location) throws IOException {

    try {
      Writer wr = new FileWriter(location, true);
      wr.write(data);
      wr.flush();
      wr.close();
    }
    catch (IOException ex) {
      System.out.println("no such file");
    }
  }


    private static ArrayList<String> getRandomPair() {

    ArrayList<String> counCap = new ArrayList<String>();
    Random rand = new Random();
    int index = rand.nextInt(183);
    String pair = readFile("/home/zed/java/countries_and_capitals.txt").get(index).replace(" | ", ",");
    for (String item : pair.split(",")) {
      counCap.add(item);
    }     
    return counCap;
  }


  private static String getInput(String message) {

    Scanner reader = new Scanner(System.in); 
    System.out.print(message);
    String choice = reader.nextLine();
    return choice;
  }


  private static String getName() {
    
    ArrayList<String> output = new ArrayList<String>();
    String input = getInput("Enter Your name: ");
    for (String letter : input.split("")) {
      output.add(letter);
    }
    if (input.length() < 12) {
      for (int count = 0; count < 12 - input.length(); count ++) {
        output.add(" ");
      }
    }
    while (output.size() > 12) {
      output.remove(output.size() - 1);      
    }
    String name = String.join("", output);
    clearScreen();
    return name;
  }
    
  
  private static String getDate() {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    LocalDateTime date = LocalDateTime.now();
    String formatedDate = date.format(formatter);
    return formatedDate;
  }


  private static void clearScreen() { 

    System.out.print("\033[H\033[2J");  
    System.out.flush();  
  } 


  private static void gameWon(long time, int guessCount) {

    clearScreen();
    System.out.println("Congrats, You have won!");
    System.out.println("\nIt took You " + time + " seconds to finish game");
    System.out.println("\nIt took You " + guessCount + " guesses to finish game" + "\n");
  }


  private static void printHighScore() {

    ArrayList<String> scoreList = new ArrayList<String>(readFile("/home/zed/java/highscore.txt"));
    System.out.println("Hall of fame" + "\n");
    try {
      for (int index = 0; index < 10; index ++) {
        System.out.println(scoreList.get(index));
      }
    }
    catch (IndexOutOfBoundsException ex) {
      ;
    }
  }

  private static void printScore() throws IOException {
    ArrayList<Integer> times = getTimeFromHighscore();
    ArrayList<String> output = new ArrayList<String>(readFile("/home/zed/java/highscore.txt"));
    ArrayList<String> toPrint = new ArrayList<String>();
    ArrayList<String> split = new ArrayList<String>();
    ArrayList<Integer> toCompare = new ArrayList<Integer>();

    for (int time : times) {
      for (int line = 0; line < output.size(); line ++) {
        for (String character : output.get(line).split("")) {
          split.add(character);
        }
        if (time == (convertToInt(split.get(40) + split.get(41) + split.get(42)))) {
          toPrint.add(output.get(line));
          output.remove(line);
          
        }
      split.clear();
      }
    }
    try {
      for (int index = 0; index < 10; index++) {
        System.out.println(toPrint.get(index));
      }
    }
    catch (IndexOutOfBoundsException ex) {
      ;
    }
  }


  private static ArrayList<Integer> getTimeFromHighscore() {
    ArrayList<String> output = new ArrayList<String>(readFile("/home/zed/java/highscore.txt"));
    ArrayList<Integer> times = new ArrayList<Integer>();
    ArrayList<String> split = new ArrayList<String>();
    for (String line : output) {
      for (String character : line.split("")) {
        split.add(character);
      }
      int time = convertToInt(split.get(40) + split.get(41) + split.get(42));
      times.add(time);
      split.clear();
    }
    times.sort(null);
    return times;
  }


  private static int convertToInt(String data) {

    String output = data.replace(" ", "");
    return Integer.parseInt(output);
  }


  private static void printLife(int life) {

    int lives = life;

    String color = null;

    switch (lives) {
      case 1: color = "\u001B[38:2:255:0:0m\u001B[1m";
        break;
      case 2: color = "\u001B[38:2:255:80:80m\u001B[1m";
        break; 
      case 3: color = "\u001B[38:2:250:100:255m\u001B[1m";
        break;
      case 4: color = "\u001B[38:2:40:40:190m\u001B[1m";
        break;
      case 5: color = "\u001B[38:2:250:150:50m\u001B[1m";
        break;
      case 6: color = "\u001B[38:2:0:255:0m\u001B[1m";
        break;
      case 7: color = "\u001B[38:2:250:250:150m\u001B[1m";
        break;
      case 8: color = "\u001B[38:2:255:150:250m\u001B[1m";
        break; 
      case 9: color = "\u001B[38:2:153:204:255m\u001B[1m";
        break;
      case 10: color = "\u001B[38:2:100:255:255m\u001B[1m";
        break;

    }
    System.out.println("\n" + color + "Lives left: " + lives + "\u001B[0m");
  }


  private static String formatGameTimeAndGuessCount(String data) {

    String output = null;
    if (data.length() == 2) {
      output = data + " ";
    } 
    if (data.length() == 1) {
      output = data + "  ";
    }
    return output;
  }


  private static void playRound() throws IOException {

    int life = 10;
    Integer guessCount = 0;
    ArrayList<String> tried = new ArrayList<String>();
    ArrayList<String> pair = new ArrayList<String>();
    for (String item : getRandomPair()) {
      pair.add(item);
    }
    String country = pair.get(0);
    ArrayList<String> capital = new ArrayList<String>();
    for (String letter : (pair.get(1).toUpperCase()).split("")) {
      capital.add(letter);
    }
    ArrayList<String> hidden = new ArrayList<String>();
    for (String letter : capital) {
      hidden.add("_");
    }
    Instant start = Instant.now();
    clearScreen();

    while (life > 0) {
      System.out.println(String.join("", hidden));
      System.out.println(String.join("", capital));
      if (tried.size() > 0) {
      System.out.println("\nAlready tried: " + tried.toString().replace("[", "").replace("]", ""));
      }
      printLife(life);
      if (life < 2) {
        System.out.println("\n\u001B[91mHint: what is capital of " + pair.get(0) + " ?\u001B[0m");
      }
      String userInput = getInput("\nEnter letter or word, nothing to exit: ").toUpperCase();
      if (!userInput.replace(" ", "").chars().allMatch(Character::isLetter) || tried.contains(userInput)) {
        clearScreen();
        continue;
      }
      if (userInput.length() == 1) {
        tried.add(userInput);        
        if (capital.contains(userInput)) {
          for (int index = 0; index < hidden.size(); index++) {
            if (capital.get(index).equals(userInput)) {
              hidden.remove(index);
              hidden.add(index, userInput);
            }     
          }
      } else {
        life --;
      }

      } else if (userInput.length() > 1) {
        tried.add(userInput);
        
          if (!userInput.equals(String.join("",capital))) {
          life -= 2;
          }    
      } else if (userInput.length() < 1) {
        
        clearScreen();
        System.out.println("Thanks For Playing!");
        System.exit(0);
      }
    guessCount ++;
    if (String.join("", hidden).equals(String.join("", capital)) || userInput.equals(String.join("",capital))) {
      Instant finish = Instant.now();
      Long time = Duration.between(start, finish).toMillis() / 1000;
      gameWon(time, guessCount);
      String formatedDate = getDate();
      String sep = " | ";
      String name = getName();
      String guesses = formatGameTimeAndGuessCount(guessCount.toString());
      String lengthOfGame = formatGameTimeAndGuessCount(time.toString());
      saveToFile(name + sep + formatedDate + sep + guesses + sep + lengthOfGame + sep + String.join("",capital) + "\n", "highscore.txt");
      printScore();
      break;
      }
    clearScreen();
    }  
    if (life < 1) {
    System.out.println("Game over. Better luck next time!");
    }
  }


  private static void startGame() {
    try {
    playRound();
    }
    catch (IOException ex) {
      System.out.println("no such file");
    }
    String choice = getInput("\nWant to play again? [Y] for yes ").toUpperCase();
    if (choice.equals("Y")) {
      startGame();
    }
    clearScreen();
    System.out.println("Thanks for playing!");
  }


  public static void main(String[] args) {

    startGame();
 
  }    
}
