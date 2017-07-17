package uk.ac.aber.dcs.blockmotion.gui;

/**
 * Animator class to display animation and run menus
 * @author Chris Loftus  Rhys Evans
 * @version 2.0 (1st May 2017)
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import uk.ac.aber.dcs.blockmotion.model.*;
import uk.ac.aber.dcs.blockmotion.transformation.*;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Animator extends Application {

    private Button[][] gridArray;
    private GridPane grid;
    private Thread animation;
    private boolean doRun;
    private Footage footage;
    private Stage stage;
    private Scene scene;
    private Scanner in;
    private String fileName;
    // Variable to store whether or not any changes have been made to the footage
    private Boolean hasChanged = false;
    // Store the previous choice in order to redo
    private String lastChoice;

    // Slide numbers for transformation
    private int slideNL = 1;
    private int slideNR = 1;
    private int slideNU = 1;
    private int slideND = 1;


    // You will need more instance variables

    public static void main(String[] args){
        // This is how a javafx class is run.
        // This will cause the start method to run.
        // You don't need to change main.
        launch(args);
    }

    // This is the javafx main starting method. The JVM will run this
    // inside an object of this class (Animator). You do not need to
    // create that object yourself.
    @Override
    public void start(Stage primaryStage) throws IOException{

        // The Stage is where we place GUI stuff, such as a GridPane (see later).
        // I'll look more at this after Easter, but please read the
        // following comments
        stage = primaryStage;

        // In this version of the app we will drive
        // the app using a command line menu.
        // YOU ARE REQUIRED TO IMPLEMENT THIS METHOD
        runMenu();

        // This is how we can handle a window close event. I will talk
        // about the strange syntax later (lambdas), but essentially it's a Java 8
        // was of providing a callback function: when someone clicks the
        // close window icon then this bit of code is run, passing in the event
        // object that represents the click event.
        primaryStage.setOnCloseRequest((event) -> {
            // Prevent window from closing. We only want quitting via
            // the command line menu.
            event.consume();

            System.out.println("Please quit the application via the menu");
        });
    }

    private void runMenu() throws IOException {
        // The GUI runs in its own thread of control. Here
        // we start by defining the function we want the thread
        // to call using that Java 8 lambda syntax. The Thread
        // constructor takes a Runnable
        Runnable commandLineTask = () -> {

            // YOUR MENU CODE GOES HERE
            //String to contain the value entered by user
            String choice;
            fileName = "untitled.txt";
            // Initialize scanner
            in = new Scanner(System.in);

            // Do-while loop to run the menu whilst the user has not selected 'quit'
            do{
                printMenu();
                choice = in.next().toUpperCase();
                switch(choice){

                    case "L":
                        // Check if animation is running
                        if(doRun) {
                            System.err.println("Stop Animation First! (T)");
                        }
                        else{
                            // Check if any changes have been made to the footage
                            if(hasChanged){
                                promptSave();
                            }
                            load();
                        }
                        break;

                    case "S":
                        save();
                        break;

                    case "SA":
                        System.out.println("Enter file name: ");
                        fileName = in.next();
                        save();
                        break;

                    case "A":
                        if(doRun){
                            System.err.println("Animation is already running.");
                        }
                        else {
                            runAnimation();
                        }
                        break;

                    case "T":
                        terminateAnimation();
                        break;

                    case "E":
                        if(footage != null) {
                            runSubMenu();
                        }
                        else{
                            System.err.println("Load footage first!");
                        }
                        break;

                    case "Q":
                        // Check if any changes have been made to the footage
                        if(hasChanged){
                            // Keep prompting until valid input is received.
                            do {
                                promptSave();
                            }while(hasChanged);
                        }
                        terminateAnimation();
                        Platform.exit();
                        break;

                    default:
                        System.err.println("Invalid choice!");
                }
            } while (!choice.equals("Q"));

        };
        Thread commandLineThread = new Thread(commandLineTask);
        // This is how we start the thread.
        // This causes the run method to execute.
        commandLineThread.start();

    }


    /**
     * Load a footage file
     */
    private void load(){
        System.out.println("Enter file name to load: ");
        fileName = in.next();
        try {
            footage = new Footage();
            footage.load(fileName);
            System.out.print("File " + fileName + " loaded successfully\n");
            createGrid();
        }catch(IOException | NoSuchElementException exc){
            System.err.println("Error loading file! Check the file's name and contents.");
            footage = null;
        }
    }

    /**
     * Save a footage file
     */
    private void save(){
        if(footage == null) {
            System.err.println("Load footage first!");
        }else {
            try {
                footage.save(fileName);
                System.out.println("File saved as " + fileName + "\n");
                hasChanged = false;
            } catch (IOException e) {
                System.err.println("Could not save file. Try Again");
            }
        }
    }

    /**
     * Prompt user to save unsaved changes.
     */
    private void promptSave(){
        // Prompt user to save unsaved footage
        String input;
        System.out.println("Do you want to save the changes made to '" + fileName + "' first? (Y/N)");
        input = in.next().toUpperCase();

        switch(input){

            case "Y":
                save();
                hasChanged = false;
                break;

            case "N":
                hasChanged = false;
                break;

            default:
                System.err.println("Invalid choice!");
        }
    }

    /**
     * Check for bad input then change the slide number of a given direction
     * @return n
     */
    private int changeSlideN(int nIn){
        try {
            return in.nextInt();
        }catch(InputMismatchException e){
            // If input was bad return original input
            System.err.println("Value is not an intger!");
            // Discard input buffer
            in.nextLine();
            return nIn;
        }
    }


    /**
     * Print the different menu options
     */
    private void printMenu(){
        System.out.println("Welcome to Blockmotion!");
        System.out.println("-----------------------");
        System.out.println("L - Load footage file");
        System.out.println("S - Save footage file");
        System.out.println("SA - Save as footage file");
        System.out.println("A - Run footage animation");
        System.out.println("T - Stop footage animation");
        System.out.println("E - Edit current footage");
        System.out.println("Q - Quit");
        System.out.println("-----------------------");
        System.out.println("Enter option: ");
    }

    /**
     * Print the transformation sub menu
     */
    private void printSubMenu(){
        System.out.println("-----------------------");
        System.out.println("FH - Flip horizontal");
        System.out.println("FV - Flip vertical");
        System.out.println("SL - Slide left");
        System.out.println("SR - Slide right");
        System.out.println("SU - Slide up");
        System.out.println("SD - Slide down");
        System.out.println("NL - Slide left number. Currently= " + slideNL);
        System.out.println("NR - Slide right number. Currently= " + slideNR);
        System.out.println("NU - Slide up number. Currently= " + slideNU);
        System.out.println("ND - Slide down number. Currently= " + slideND);
        System.out.println("NA - Slide number for all directions.");
        System.out.println("A - Create frame and add to footage");
        System.out.println("D - Remove frame from footage");
        System.out.println("R - Repeat last operation");
        System.out.println("Q - Quit editing");
        System.out.println("-----------------------");
        System.out.println("Enter option: ");
    }

    /**
     * Run the submenu
     */
    private void runSubMenu(){

        String choice;
        in = new Scanner(System.in);

        do{
            printSubMenu();
            choice = in.next().toUpperCase();
            // Check if operation should redo, avoid NullPointer
            if (choice.equals("R") && lastChoice != null) {
                choice = lastChoice;
            }

            switch(choice){

                case "FH":
                    footage.transform(new Y());
                    hasChanged = true;
                    lastChoice = choice;
                    break;

                case "FV":
                    footage.transform(new X());
                    hasChanged = true;
                    lastChoice = choice;
                    break;

                case "SL":
                    // Create slide object and assign slide number and direction
                    Slide slideLeft = new Horizontal(Directions.LEFT);
                    slideLeft.setSlideN(slideNL);
                    footage.transform(slideLeft);
                    hasChanged = true;
                    lastChoice = choice;
                    break;

                case "SR":
                    Slide slideRight = new Horizontal(Directions.RIGHT);
                    slideRight.setSlideN(slideNR);
                    footage.transform(slideRight);
                    hasChanged = true;
                    lastChoice = choice;
                    break;

                case "SU":
                    Slide slideUp = new Vertical(Directions.UP);
                    slideUp.setSlideN(slideNU);
                    footage.transform(slideUp);
                    hasChanged = true;
                    lastChoice = choice;
                    break;

                case "SD":
                    Slide slideDown = new Vertical(Directions.DOWN);
                    slideDown.setSlideN(slideND);
                    footage.transform(slideDown);
                    hasChanged = true;
                    lastChoice = choice;
                    break;

                case "NL":
                    System.out.println("Enter Slide Number Left (as integer): ");
                    slideNL = changeSlideN(slideNL);
                    break;

                case "NR":
                    System.out.println("Enter Slide Number Right (as integer): ");
                    slideNR = changeSlideN(slideNR);
                    break;

                case "NU":
                    System.out.println("Enter Slide Number Up (as integer): ");
                    slideNU = changeSlideN(slideNU);
                    break;

                case "ND":
                    System.out.println("Enter Slide Number Down (as integer): ");
                    slideND = changeSlideN(slideND);
                    break;

                case "NA":
                    // Change slide number for all directions
                    System.out.println("Enter Slide Number For All Directions (as integer): ");
                    // Set all slide numbers to the first one (all the same)
                    slideNL = slideNR = slideNU = slideND = changeSlideN(slideNL);
                    break;

                case "A":
                    System.out.println("Enter new frame line by line: ");
                    String[] lines = new String[footage.getNumRows()];
                    Frame newFrame = new Frame(footage.getNumRows());
                    Boolean valid = true;
                    // Take input line by line
                    for(int i = 0; i < footage.getNumRows(); i++){
                        System.out.print(i+1 + ": ");
                        lines[i] = in.next();
                        // Verify input
                        if(lines[i].length() != footage.getNumRows()){
                            System.err.println("Error in Input! Check length of line");
                            valid = false;
                            break;
                        }
                    }
                    if(valid) {
                        // Restart animation to show changes
                        terminateAnimation();
                        newFrame.insertLines(lines);
                        footage.add(newFrame);
                        System.out.println("Frame added.");
                        hasChanged = true;
                        runAnimation();
                    }
                    break;

                case "D":
                    if(footage.getNumFrames() > 1) {
                        System.out.println("Enter the number of the frame to delete (1 - " + footage.getNumFrames() + "):");
                        terminateAnimation();
                        // Remove the frame from footage, pass method the correct index
                        try {
                            footage.remove(in.nextInt() - 1);
                            hasChanged = true;
                            System.out.println("Frame deleted.");
                        } catch (InputMismatchException | IndexOutOfBoundsException e) {
                            System.err.println("Invalid value!");
                            in.nextLine();
                        }

                        runAnimation();
                    }
                    else{
                        System.err.println("Cannot delete any more frames!");
                    }
                    break;

                case "R":
                    break;

                case "Q":
                    // Reset most recent operation
                    lastChoice = null;
                    break;

                default:
                    System.err.println("Invalid choice!");
            }
        }while(!choice.equals("Q"));


    }

    // An example method that you might like to call from your
    // menu. Whenever you need to do something in the GUI thread
    // from another thread you have to call Platform.runLater
    // This is a javafx method that queues your code ready for the GUI
    // thread to run when it is ready. We use that strange lambda Java 8 syntax
    // again although this time there are no parameters hence ()
    private void terminateAnimation() {

        doRun = false;
    }

    // Here is another example. This one is useful because it creates
    // the GUI grid. You will need to call this from the menu, e.g. when starting
    // an animation.
    private void createGrid() {
        Platform.runLater(() -> {

            // Update UI here
            //createGrid(numRows);   // E.g. let's create a 20 x 20 grid
            createGrid(footage.getNumRows());
        });
    }

    // I'll give you this method since I haven't done
    // much on javafx. This also creates a scene and adds it to the stage;
    // all very theatrical.
    private void createGrid(int numRows) {
        // Creates a grid so that we can display the animation. We will see
        // other layout panes in the lectures.
        grid = new GridPane();

        // We need to create a 2D array of javafx Buttons that we will
        // add to the grid. The 2D array makes it much easier to change
        // the colour of the buttons in the grid; easy lookup using
        // 2D indicies. Note that we make this read-only for this display
        // onlt grid. If you go for flair marks, then I'd imagine that you
        // could create something similar that allows you to edits frames
        // in the footage.
        gridArray = new Button[numRows][numRows];
        Button displayButton = null;
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numRows; col++) {  // The display is square
                displayButton = new Button();
                gridArray[row][col] = displayButton;
                displayButton.setDisable(true);
                grid.add(displayButton, col, row);
            }
        }

        // Create a scene to hold the grid of buttons
        // The stage will "shrink wrap" around the grid of buttons,
        // so we don't need to set its height and width.
        scene = new Scene(grid);
        stage.setScene(scene);
        scene.getStylesheets().add(Animator.class.getResource("styling.css").toExternalForm());

        // Make it resizable so that the window shrinks to fit the scene grid
        stage.setResizable(true);
        stage.sizeToScene();
        // Raise the curtain on the stage!
        stage.show();
        // Stop the user from resizing the window
        stage.setResizable(false);
        // Set title of stage
        stage.setTitle(fileName);
    }

    // I'll also give you this one too. This does the animation and sets colours for
    // the grid buttons. You will have to call this from the menu. You should not
    // need to change this code, unless you want to add more colours
    private void runAnimation() {
        if (footage == null) {
            System.err.println("Load the footage first");
        } else {
            Runnable animationToRun = () -> {

                Background yellowBg = new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY));
                Background blackBg = new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY));
                Background blueBg = new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY));
                Background whiteBg = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
                Background redBg = new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY));
                Background greenBg = new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY));

                doRun = true;
                int numFrames = footage.getNumFrames();
                int currentFrameIndex = 0;
                Background bck = null;
                while (doRun) {
                    if (currentFrameIndex >= numFrames - 1) currentFrameIndex = 0;
                    IFrame currentFrame = footage.getFrame(currentFrameIndex);
                    // Iterate through the current frame displaying appropriate colour
                    for (int row = 0; row < footage.getNumRows(); row++) {
                        for (int col = 0; col < footage.getNumRows(); col++) {
                            switch (currentFrame.getChar(row, col)) {
                                case 'l':
                                    bck = yellowBg;
                                    break;
                                case 'r':
                                    bck = blackBg;
                                    break;
                                case 'b':
                                    bck = blueBg;
                                    break;
                                case 'c':
                                    bck = redBg;
                                    break;
                                case 'g':
                                    bck = greenBg;
                                    break;
                                default:
                                    bck = whiteBg;
                            }
                            final int theRow = row;
                            final int theCol = col;
                            final Background backgroundColour = bck;
                            // This is another lambda callback. When the Platform
                            // GUI thread is ready it will run this code.
                            Platform.runLater(() -> {

                                // Update UI here
                                // We have to make this request on a queue that the GUI thread
                                // will run when ready.
                                gridArray[theRow][theCol].setBackground(backgroundColour);
                            });

                        }
                    }
                    try {
                        // This is how we delay for 200th of a second
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                    }
                    currentFrameIndex++;
                }
            };
            animation = new Thread(animationToRun);
            animation.start();
        }
    }
}

