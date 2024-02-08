import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Main {

    public static int turn = 0;
    //turn 0=player1, turn 1=player2

    public static int round = 1;

    public static boolean playing = true;

    public static int p0Lives, p1Lives;
    //range from 3-6

    public static String p0name,p1name;
    public static int rounds,lives,blanks;
    //rounds range from 2 to 7
    //lives and blanks:
    //10: 4,5,6
    //09: 3-6
    //08: 3-6
    //07: 3-4
    //06: 2-4
    //05: 2-3
    //04: 1-3
    //03: 1-2
    //02: 1-1

    public static Stack<String> bulletSequence = new Stack<>();
    public static ArrayList<String> p0Items = new ArrayList<>();
    public static ArrayList<String> p1Items = new ArrayList<>();
    public static int numberOfItems;
    //5 possible items, 8 total at most
    //each round you can have between 2-4

    public static boolean skipped = false;
    public static int damage = 1;
    public static double rand = Math.random();
    public static final String[] items = {"Beer", "Cigar", "Cuffs", "Glasses", "Saw"};

    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        //INTRODUCTION:
        intro();

        System.out.println("Begin the roulette!\n\nROUND 1!");
        playing = true;
        while(playing) {

            checkShotgun();
            printStats();

            if (turn == 0) System.out.println("\n" + p0name.toUpperCase() + "'S TURN!");
            else System.out.println("\n" + p1name.toUpperCase() + "'S TURN!");

            if (skipped) {
                System.out.println("TURN SKIPPED!");
                skipped = false;
            } else {
                boolean moveDone = false;
                while (!moveDone) {
                    System.out.println("Use item (type item name) or shoot (s/shoot)?");
                    String cmd = sc.next();
                    if (cmd.equalsIgnoreCase("s") || cmd.equalsIgnoreCase("shoot")) {
                        System.out.println("Shoot self (s/self) or " + (turn == 0 ? p1name : p0name) + "?");
                        String whotakesbullet = sc.next();
                        boolean correctName = whotakesbullet.equalsIgnoreCase(p0name) || whotakesbullet.equalsIgnoreCase(p1name) || whotakesbullet.equalsIgnoreCase("s");
                        while(!correctName) {
                            if(whotakesbullet.equalsIgnoreCase(p0name) || whotakesbullet.equalsIgnoreCase(p1name) || whotakesbullet.equalsIgnoreCase("s") || whotakesbullet.equalsIgnoreCase("self")) { correctName = true;
                            } else {
                                System.out.println("Bro enter the right name.");
                                whotakesbullet = sc.next();
                            }
                        }
                        updateShotgun(whotakesbullet);
                        moveDone = true;
                    } else {            //using item
                        boolean isItem = true;
                        if(cmd.equalsIgnoreCase("beer")) cmd = "Beer";
                        else if(cmd.equalsIgnoreCase("cigar")) cmd = "Cigar";
                        else if(cmd.equalsIgnoreCase("cuffs")) cmd = "Cuffs";
                        else if(cmd.equalsIgnoreCase("glasses")) cmd = "Glasses";
                        else if(cmd.equalsIgnoreCase("saw")) cmd = "Saw";
                        else {
                            System.out.println("Erm, not a valid move bro.");
                            isItem = false;
                        }
                        if (turn == 0 && isItem) {
                            if(p0Items.contains(cmd)) {
                                System.out.println(p0name + " uses " + cmd + ".");
                                useItem(cmd);
                            } else System.out.println("Bro you don't have a " + cmd + ".");
                        } else if (turn == 1 && isItem) {
                            if(p1Items.contains(cmd)) {
                                System.out.println(p1name + " uses " + cmd + ".");
                                useItem(cmd);
                            } else System.out.println("Bro you don't have " + cmd + ".");
                        }
                    }
                }
            }
            checkWin();
            turn = (turn + 1) % 2;
        }

        sc.close();
    }

    //Set player lives
    public static void intro() {
        System.out.println("Welcome to Buckshot Roulette!");
        System.out.println("Here are the rules:");
        System.out.println("1. Starting each round, the shotgun will be loaded with lives (has bullet) and blanks (no bullet).");
        System.out.println("2. You take turns shooting either yourself or your opponent, first to kill the other person wins.");
        System.out.println("3. Additionally, shooting yourself with a blank will give you another turn to shoot");
        System.out.println("4. You can use your tools (given at beginning of round) to aid your success.");
        System.out.println("5. Here are a list of items given at random:");
        System.out.println("5a. Beer: Removes the current bullet from the shotgun.");
        System.out.println("5b. Cigar: gain an extra life.");
        System.out.println("5c. Cuffs: Skips the dealer or your turn.");
        System.out.println("5d. Saw: Makes the bullet do double damage if it is live.");
        System.out.println("5e. Glasses: Checks the current round in the shotgun.");
        System.out.println("Enjoy the rounds!");
        System.out.println("Hit enter to continue when you're ready:");
        sc.nextLine();
        System.out.println("Let's start!\n.\n.\n.\n.\n.");

        System.out.println("Player 1, enter your name:");
        p0name = sc.next();
        System.out.println("Player 2, enter your name:");
        p1name = sc.next();

        rand = Math.random();
        //player0Lives and player1Lives:
        p0Lives = (int)(4*rand+3);
        p1Lives = p0Lives;
//        System.out.println(p0name + " Lives: " + p0Lives + ", "+ p1name+ " Lives: " + p1Lives);
    }

    public static void useItem(String itemUsed) {
        switch (itemUsed) {
            case "Beer" -> {
                String b = bulletSequence.pop();
                System.out.println("Bro just chugged a whole beer down and popped the current shotgun rack, it was a " + b + ".");
                if (b.equals("LIVE")) lives--;
                else blanks--;
                rounds--;
            }
            case "Cigar" -> {
                if (turn == 0) p0Lives++;
                else p1Lives++;
                System.out.println("Bro just smoked a good one, " + (turn == 0 ? p0name : p1name) + " now has " + (turn == 0 ? p0name : p1name) + " lives.");
            }
            case "Cuffs" -> {
                skipped = true;
                if (turn == 0) {
                    System.out.println("Come here " + p1name + " lemme put these cuffs on ya.");
                } else {
                    System.out.println("Come here " + p0name + " lemme put these cuffs on ya.");
                }
            }
            case "Saw" -> {
                System.out.println("Alright, double damage incoming.");
                damage = 2;
            }
            case "Glasses" -> {
                String b = bulletSequence.peek();
                System.out.println("Bro peeked, the current bullet is " + b + ".");
            }
        }
        if(turn == 0) p0Items.remove(itemUsed);
        else if (turn == 1) p1Items.remove(itemUsed);

        System.out.println(p0name + " Lives: " + p0Lives);
        System.out.println(p1name + " Lives: " + p1Lives);
        System.out.println(p0name + " Items: " + p0Items);
        System.out.println(p1name + " Items: " + p1Items);
    }

    public static void checkWin() {
        if(p1Lives <= 0) {
            System.out.println(p0name + " wins! Better luck next time ;)");
            playing = false;
        } else if (p0Lives <= 0) {
            System.out.println(p1name + " wins! Better luck next time ;)");
            playing = false;
        }
        if(p0Lives <= 0 || p1Lives <= 0) {
            System.out.println("Play again (Y/y)?");
            String cmd = sc.next();
            if(cmd.equalsIgnoreCase("y")) {
                playing = true;
                round++;
                System.out.println("Begin the roulette!\n\nROUND " + round + "!");
                rand = Math.random();
                p0Items.clear();
                p1Items.clear();
                bulletSequence.clear();
                //player0Lives and player1Lives:
                p0Lives = (int)(4*rand+3);
                p1Lives = p0Lives;
            } else {
                System.out.println("Good game :)");
                System.out.println("Rounds played: " + rounds);

            }
        }
    }

    public static void updateShotgun(String whotakesbullet){
        if(whotakesbullet.equalsIgnoreCase("s") || whotakesbullet.equalsIgnoreCase("self")) whotakesbullet = turn==0?p0name:p1name;
        String b = bulletSequence.pop();
        rounds = bulletSequence.size();
        if(b.equals("LIVE")) {
            System.out.println("BAM! " + whotakesbullet + " just got shot.");
            lives--;
            if(whotakesbullet.equals(p0name)) p0Lives=p0Lives-damage;
            else p1Lives=p1Lives-damage;
        }  else if (b.equals("BLANK")) {
            blanks--;
            System.out.println("Lucky bastard, " + whotakesbullet + " got lucky.");
            if((whotakesbullet.equals(p1name) && turn==1) || (whotakesbullet.equals(p0name) && turn==0)) skipped=true;
        }
        damage = 1;
    }


    public static void checkShotgun() {
        if (bulletSequence.isEmpty()) {
            System.out.println("\nShotgun is empty, reloading...");
            rand = Math.random();
            rounds = (int) (7*rand+2);
            if(rounds==8) { lives = (int) ((5-3+1)*rand+3); }
            else if(rounds==7) { lives = (int) ((4-3+1)*rand+3); }
            else if(rounds==6) { lives = (int) ((4-2+1)*rand+2); }
            else if(rounds==5) { lives = (int) ((3-2+1)*rand+2); }
            else if(rounds==4) { lives = (int) ((3-1+1)*rand+1); }
            else if(rounds==3) { lives = (int) ((2-1+1)*rand+1); }
            else if(rounds==2) { lives = 1; }
            blanks = rounds-lives;
            System.out.println("Lives: " + lives + ", Blanks: " + blanks);

            //Gun loading
            int tempLivesLoaded = 0, tempBlanksLoaded = 0;
            while (tempLivesLoaded != lives && tempBlanksLoaded != blanks) {
                rand = Math.random();
                if(rand < 0.5) {
                    bulletSequence.push("LIVE");
                    tempLivesLoaded++;
                } else {
                    bulletSequence.push("BLANK");
                    tempBlanksLoaded++;
                }
            }
            if(tempBlanksLoaded == blanks) {
                while(tempLivesLoaded != lives) {
                    bulletSequence.push("LIVE");
                    tempLivesLoaded++;
                }
            } else {
                while(tempBlanksLoaded != blanks) {
                    bulletSequence.push("BLANK");
                    tempBlanksLoaded++;
                }
            }

            //Item distribution
            rand = Math.random();
            numberOfItems = (int) ((4-2+1)*rand+2);

            for(int i = 0; i < numberOfItems; i++) {
                p0Items.add(items[(int) (5*rand)]);
                rand = Math.random();
            }
            for(int i = 0; i < numberOfItems; i++) {
                p1Items.add(items[(int) (5*rand)]);
                rand = Math.random();
            }
        }
    }

    public static void printStats() {
        System.out.println("\n--STATS--");
        System.out.println(p0name + ": " + p0Lives +" Lives, " + p1name + ": " + p1Lives + " Lives");
        System.out.println(p0name + " Items: " + p0Items);
        System.out.println(p1name + " Items: " + p1Items);
        //System.out.println(bulletSequence);
    }
}