import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.geometry.Pos;
import javafx.scene.shape.Circle;
import javafx.animation.PathTransition;
import javafx.scene.shape.Polyline;
import javafx.geometry.Insets;
import javafx.scene.shape.Line;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Arrays;
import javafx.util.Duration;
import java.util.Random;
import javafx.scene.paint.Color;

public class transitionTest extends Application{

   final static int WSW = 700;//WSW is window size length
   final static int WSH = 800;//WSH is window size height
   final static int rimStartX = 300;
   final static int rimStartY = 150;
   final static int rimEndX = 300;
   final static int rimEndY = 200;
   static Pane pane = new Pane();
   public static Circle circle = new Circle(350, 175, 10);//Test Circle
   //These three object arrays are to be used together for organization.
   
   public static double[] turns;//used in order to hold the coordinates for the polylines.
         
   public static BorderPane borderPane = new BorderPane();
   public static Circle[] balls =  new Circle[10];
   public static PathTransition[] transits = new PathTransition[10];
   public static Polyline[] paths = new Polyline[10];
   public static int slotCount[] = new int[8];
   public static int i = 1;
   
   
            
   @Override
   public void start(Stage primaryStage){
      
      Line rimLeft = new Line ();
      rimLeft.setStartX(rimStartX + 20);
      rimLeft.setStartY(rimStartY + 20);
      rimLeft.setEndX(rimEndX + 20);
      rimLeft.setEndY(rimEndY + 20);
      pane.getChildren().add(rimLeft);
      
      Line rimRight = new Line();
      rimRight.setStartX(rimStartX + 80);
      rimRight.setStartY(rimStartY + 20);
      rimRight.setEndX(rimEndX + 80);
      rimRight.setEndY(rimEndY + 20);
      pane.getChildren().add(rimRight);
      
      
   //These lines should build off of each other to maintain consistency and reduce complication.
      
   //Designing The Outline of The Grid: Begin._________________________________________________________
      Line slopeLeft = new Line();
      slopeLeft.setStartX(rimLeft.getEndX());
      slopeLeft.setStartY(rimLeft.getEndY());
      slopeLeft.setEndX(rimEndX - 200);
      slopeLeft.setEndY(rimEndY + 300);
      pane.getChildren().add(slopeLeft);
      System.out.println("Bottom of slope left: " + slopeLeft.getEndY());
      
      Line slopeRight = new Line();
      slopeRight.setStartX(rimRight.getEndX());
      slopeRight.setStartY(rimRight.getEndY());
      slopeRight.setEndX(rimEndX + 300);
      slopeRight.setEndY(rimEndY + 300);
      pane.getChildren().add(slopeRight);
      System.out.println("Bottom of slope right: " + slopeRight.getEndY());
      
      Line sideLBasket = new Line();
      sideLBasket.setStartX(slopeLeft.getEndX());
      sideLBasket.setStartY(slopeLeft.getEndY());
      sideLBasket.setEndX(slopeLeft.getEndX());//This will be a vertical line. no change in x value.
      sideLBasket.setEndY(slopeLeft.getEndY() + 200);
      pane.getChildren().add(sideLBasket);
      
      Line sideRBasket = new Line();
      sideRBasket.setStartX(slopeRight.getEndX());
      sideRBasket.setStartY(slopeRight.getEndY());
      sideRBasket.setEndX(slopeRight.getEndX());//This will be a vertical line. no change in x value.
      sideRBasket.setEndY(slopeRight.getEndY() + 200);
      pane.getChildren().add(sideRBasket);
   
      Line bottom = new Line();
      bottom.setStartX(sideLBasket.getEndX());
      bottom.setStartY(sideLBasket.getEndY());
      bottom.setEndX(sideRBasket.getEndX());
      bottom.setEndY(sideRBasket.getEndY());
      pane.getChildren().add(bottom);
      
      //Designing The Outline of The Grid: End._________________________________________________________
      
      Line[] borders = new Line[8];
      Circle[] circles = new Circle[5];
      
      Button prompt = new Button("Click here to drop 10 balls.");
      
      borderPane.setCenter(pane);
      borderPane.setTop(prompt);
      borderPane.setAlignment(prompt, Pos.TOP_CENTER);
      borderPane.setMargin(prompt, new Insets(15,15,15,15));
      
      Scene scene = new Scene (borderPane, WSW, WSH);
   
      Polyline line = new Polyline(turns);
      
      for(int i = 0; i < balls.length; i++){
      
         directionMaker();
         paths[i] = new Polyline(turns);
         pane.getChildren().add(paths[i]);
         
         if(i != 0)
            paths[i].setStroke(Color.TRANSPARENT);
         
         balls[i] = new Circle(350,175,10);
         balls[i].setStroke(Color.BLACK);
         balls[i].setFill(Color.GREEN);
         
         transits[i] = new PathTransition();
         transits[i].setNode(balls[i]);
         transits[i].setDuration(Duration.millis(2500));
         transits[i].setCycleCount(1);
         transits[i].setPath(paths[i]);
         pane.getChildren().add(balls[i]);
      }
      
      double spacer = (sideRBasket.getEndX() - sideLBasket.getEndX()) / 8;//This is the spacing between each slot.
      
      //System.out.println ("Spacer: " + spacer); <- Developers: Uncomment to find out the spacing between the slots.
      
      for (int i = 0; i < borders.length; i++)
      {
         if (i == 0){
            borders[i] = new Line();
            borders[i].setStartX(sideLBasket.getEndX() + spacer);
            borders[i].setStartY(sideLBasket.getEndY());
            borders[i].setEndX(bottom.getStartX() + spacer);
            borders[i].setEndY(slopeLeft.getEndY());
            pane.getChildren().add(borders[i]);
         }
         
         else{
            borders[i] = new Line();
            borders[i].setStartX(borders[i - 1].getStartX() + spacer);
            borders[i].setStartY(borders[i - 1].getStartY());
            borders[i].setEndX(borders[i - 1].getStartX() + spacer);
            borders[i].setEndY(borders[i - 1].getEndY());
            pane.getChildren().add(borders[i]);
         }
      }
   
      makePegs(circles, sideLBasket.getStartX(), sideLBasket.getStartY(), spacer);
      
      PathTransition go = new PathTransition();
      go.setNode(balls[0]);
      go.setDuration(Duration.millis(2500));
      go.setPath(paths[0]);
      go.setCycleCount(1);
      paths[0].setStroke(Color.TRANSPARENT);
      
      prompt.setOnAction(//When the user-prompt button is clicked,
         e -> {
            paths[0].setStroke(Color.BLACK);//Show the path of the ball and begin the animation.
            go.play();
            borderPane.getChildren().remove(prompt);
         });
      
      go.setOnFinished(//After the first ball animation completes,
         e -> {
            paths[0].setStroke(Color.TRANSPARENT);//Show the path of the ball.
            playRemainingTransits();//Call the method to begin the recursion process and play the other
            //animations.
         });
          
      primaryStage.setScene(scene);//Self-explanatory. Setting the scene.
      primaryStage.setTitle("Bean Machine");//Setting the title of the stage
      primaryStage.show();//Displaying the stage.
   }
   
   public static void directionMaker(){//creates new coordinates for a line after calling another method for
   //randomly generated directions.
   
      turns = new double[32];
   
      turns[0] = circle.getCenterX();
      turns[1] = circle.getCenterY();
      turns[2] = circle.getCenterX();
      turns[3] = circle.getCenterY() + 70;
      char[] directions = giveDirections();
      
      double newY = turns[1];
      
      double[] slotCount = new double[8];
   
      for (int i = 4; i < turns.length; i = i + 4){
         
/*         if (directions[i] == 'L'){//If the direction in the array indicates a left turn...
            turns[i] = turns[i - 2] - (31.25);//translating to the left
            turns[i + 1] = turns[i - 1];//the next y value will be the same as the last y value.
         }
            
         else if(directions[i] == 'R' ){
            turns[i] = turns[i - 2] + (31.25);//translating to the right.
            turns[i + 1] = turns[i - 1];//the next y value will be the same as the last y value.
         }
  */       
         if (directions[i] == 'L'){//If the direction in the array indicates a left turn...
            turns[i] = turns[i - 2] - (31.25);//translating to the left
            turns[i + 1] = turns[i - 1];//the next y value will be the same as the last y value.
         }
             
         else if (directions[i] == 'R' ){
            turns[i] = turns[i - 2] + 31.25;//translating to the right.
            turns[i + 1] = turns[i - 1];//the next y value will be the same as the last y value.    
         }
         turns[i+2] = turns[i];
         turns[i+3] = turns[i+1]+40;
         turns[turns.length-1] = (700-checkSlots(turns[turns.length-2]));
      }
      
      for (int i = 0; i < turns.length; i++){
         if(i % 2 == 0)
            System.out.print("X: " + turns[i] + "\t");
         else
            System.out.print("Y: " + turns[i] + "\n");
      }
   }
   
   public static int checkSlots(double xValue){
     double[] slotPoints = {131.25, 193.75, 256.25, 318.75, 381.25, 443.75, 506.25, 568.75};
     int valueAtIndex = 0;
     for (int i = 0; i < slotPoints.length; i++){
       if (xValue == slotPoints[i]){
         slotCount[i]++;
         valueAtIndex = slotCount[i];
       }
     }
     return valueAtIndex*20;
   }
   
   public static char[] giveDirections(){
   
      Random rand = new Random();
   
      char[] leftOrRight = new char[32];
      
      leftOrRight[0] = 'H';
      leftOrRight[1] = 'H';
      leftOrRight[2] = 'H';
      leftOrRight[3] = 'H';
   
      for (int i = 4; i < leftOrRight.length; i++){
      
         boolean choice = rand.nextBoolean();
      
         if (leftOrRight[i] != 'H'){
         
            if(choice == true){
               if((i + 1) < leftOrRight.length){
                  leftOrRight[i] = 'L';
                  leftOrRight[i + 1] = 'H';
               }
            }
            
            if(choice == false){               
               if((i + 1) < leftOrRight.length){
                  leftOrRight[i] = 'R';
                  leftOrRight[i + 1] = 'H';
               }
            }
         
         }
      }
      
      String array = Arrays.toString(leftOrRight);
      array.replace("H,", "");
      System.out.println("The directions are: " + array);   
   
      return leftOrRight;
   }
   
   public void playRemainingTransits(){//Use of recursion in this method.
      
      if(i < balls.length){
         paths[i].setStroke(Color.BLACK);
         transits[i].play();
      }
     
      if((i + 1) <= balls.length){
         transits[i].setOnFinished(
            e -> {
               paths[i].setStroke(Color.TRANSPARENT);
               i++;
               playRemainingTransits();
               
            });
      }
   }

   /*
   The method below creates only the pegs for the design of the game grid. It also takes account of the spacing
   that had.
   */
   
   public static void makePegs(Circle[] circles, double initialX, double initialY, double spacer){
      
      int startX = 350;
      double startX2 = 350 - (spacer/2);
      int startY = 250;
      int height = 40;
      
      
      Circle circ = new Circle();
      circ.setCenterX(startX);
      circ.setCenterY(startY);
      circ.setRadius(5);
      pane.getChildren().add(circ);
      
      for(int i = 1; i<7; i++){
         if(i%2==0){
            Circle circs = new Circle();
            circs.setCenterX(startX);
            circs.setCenterY(startY+height*(i));
            circs.setRadius(5);
            pane.getChildren().add(circs);
            
            for(int j = i; j>0; j--){
               for(int k = j/2; k>0; k--){
                  Circle circs2 = new Circle();
                  circs2.setCenterX(startX-spacer*(k));
                  circs2.setCenterY(startY+height*(i));
                  circs2.setRadius(5);
                  pane.getChildren().add(circs2);
               
                  Circle circs3 = new Circle();
                  circs3.setCenterX(startX+spacer*(k));
                  circs3.setCenterY(startY+height*(i));
                  circs3.setRadius(5);
                  pane.getChildren().add(circs3);
               }
            }
         }
         if(i%2==1){
            Circle circs = new Circle();
            circs.setCenterX(startX2);
            circs.setCenterY(startY+height*(i));
            circs.setRadius(5);
            pane.getChildren().add(circs);
         
            Circle circs2 = new Circle();
            circs2.setCenterX(startX2+spacer);
            circs2.setCenterY(startY+height*(i));
            circs2.setRadius(5);
            pane.getChildren().add(circs2);
            
            for(int j = i; j>0; j--){
               for(int k = j/2; k>0; k--){
                  Circle circs3 = new Circle();
                  circs3.setCenterX(startX2+spacer*(k+1));
                  circs3.setCenterY(startY+height*(i));
                  circs3.setRadius(5);
                  pane.getChildren().add(circs3);
               
                  Circle circs4 = new Circle();
                  circs4.setCenterX(startX2-spacer*(k));
                  circs4.setCenterY(startY+height*(i));
                  circs4.setRadius(5);
                  pane.getChildren().add(circs4);
               }
            }
         }
      }
   }
 
   public static void main (String[] args){
   
      Application.launch(args);
   }
}
