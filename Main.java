package application;
/*
 * NOT Flappy Bird 2018 (not to be confused with the game "Flappy Bird")
 * By: Max Hu and Jahn Kachura
 * Tested by: Noah Limpert 
 */
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class Main extends Application {

	double gravity = 0;

	public static StackPane root;
	public static StackPane game;
	public static Scene mainScene;
	public static Scene gameScene;
	public static Rectangle p1;

	public static Stage window; 
	public static ImageView birdView; 
	public double y = 0; //instance variable
	public double g = 0;
	public boolean move = true;
	public int score = 0; 
	public boolean moving = false; 
	public int pipeCount = 999; 
	public boolean[] scoreCheck;
	public int[] position;
	public boolean gameRunning = false; //will use later 
	private ArrayList<Rectangle> rectangleArrayList;

	//initializing buttons 
	Button playButton;
	Button leaderboardButton; 
	Button restart; //really sketchy
	Button test; 
	Text currentScore; 
	Text finalScore; 
	Text text;
	KeyFrame end; 
	Rectangle[] rectangle1 = new Rectangle[pipeCount]; 
	Rectangle[] rectangle2 = new Rectangle[pipeCount]; 
	AnimationTimer animation; 

	MediaPlayer player; 
	ImageView gameOver; 
	Ellipse ellipse; 


	@Override
	public void start(Stage primaryStage) throws Exception {

		//StackPane r = FXMLLoader.load(getClass().getResource("Layout.fxml"));

		//declaring the stackpane and mainscene that will be used

		window = primaryStage;

		root = new StackPane();
		game = new StackPane(); 

		mainScene = new Scene(root,750,450);
		gameScene = new Scene(game,750,450); 


		Image play = new Image("file:playbutton.png", 200, 200, true, true);
		ImageView playView = new ImageView(play);

		Image leaderboard = new Image("file:flappyleaderboard.png", 200, 200, true, true); 
		ImageView leaderboardView = new ImageView(leaderboard); 

		//declaring buttons 

		playButton = new Button("", playView);
		playButton.setBackground(Background.EMPTY);

		leaderboardButton = new Button(" ", leaderboardView); 
		leaderboardButton.setBackground(Background.EMPTY);

		restart = new Button(""); 
		restart.setTranslateX(290);
		restart.setTranslateY(185);
		restart.setBackground(Background.EMPTY);
		restart.setFont(Font.font(java.awt.Font.MONOSPACED, 33));
		restart.setTextFill(Color.GHOSTWHITE);
		


		playButton.setTranslateX(-125);
		playButton.setTranslateY(70);

		leaderboardButton.setTranslateX(125);
		leaderboardButton.setTranslateY(70);

		window.setResizable(false);

		//setting up the results of clicking the buttons
		playButton.setOnAction(e -> buttonClick(playButton));
		leaderboardButton.setOnAction(e -> buttonClick(leaderboardButton));

		//adding the buttons to root
		root.getChildren().add(playButton); 
		root.getChildren().add(leaderboardButton);

		//setting up the background 
		Image background = new Image("file:flappybackground.jpg");
		BackgroundImage myBI= new BackgroundImage(background,
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				BackgroundSize.DEFAULT);
		root.setBackground(new Background(myBI));
		game.setBackground(new Background(myBI));

		//setting up the title 
		Image title = new Image("file:flappytitle.png", 350, 125, true, true);
		ImageView titleView = new ImageView();
		titleView.setImage(title);
		titleView.setTranslateY(-50);
		root.getChildren().add(titleView);

		Image over = new Image("file:gameover.png");
		gameOver = new ImageView(over);
		gameOver.setFitHeight(400);
		gameOver.setFitWidth(400);
		gameOver.setPreserveRatio(true);



		Music(); 

		//setting up the window 
		window.setTitle("Flappy Bird");
		window.setScene(mainScene);
		window.show();


		currentScore = new Text("Current score "+score); 
		currentScore.setTranslateY(185); 
		currentScore.setTranslateX(-185);
		currentScore.setFont(Font.font(java.awt.Font.MONOSPACED, 33));
		currentScore.setFill(Color.GHOSTWHITE);


		finalScore = new Text("Your score: "+score);
		finalScore.setFont(Font.font(java.awt.Font.MONOSPACED, 35));


	}

	public void setSize(int i, int counter) {

		int rand = new Random().nextInt(3);

		if (rand==0) {
			//test case #1
			rectangle1[i].setHeight(200);
			rectangle1[i].setTranslateY(36);

			rectangle2[i].setHeight(100);
			rectangle2[i].setTranslateY(-222);

		}

		else if (rand==1) {
			//test case #2
			rectangle1[i].setHeight(100);
			rectangle1[i].setTranslateY(85);

			rectangle2[i].setHeight(200);
			rectangle2[i].setTranslateY(-178);

		}
		else {
			//test case #3
			rectangle1[i].setHeight(150);
			rectangle1[i].setTranslateY(61);

			rectangle2[i].setTranslateY(-192);
			rectangle2[i].setHeight(150);

		}
		rectangleArrayList.add(rectangle1[i]);
		rectangleArrayList.add(rectangle2[i]);

	}
	public void runningGame() {
		score = 0; 
		currentScore.setText("Current score: "+score);
		restart.setText("");

		game.getChildren().add(restart); 
		System.out.println("Bird spawns!");
		Image bird = new Image("file:bird.png");
		birdView = new ImageView(); 
		birdView.setImage(bird);

		birdView.setFitHeight(45);
		birdView.setFitWidth(45);
		birdView.setPreserveRatio(true);
		ellipse = new Ellipse();
		ellipse.setRadiusX(21);
		ellipse.setRadiusY(14);
		ellipse.setFill(Color.TRANSPARENT);

		game.getChildren().add(birdView); 
		game.getChildren().add(ellipse); 

		rectangleArrayList = new ArrayList<Rectangle>(); 
		position = new int[999]; 
		scoreCheck = new boolean[999]; 

		text = new Text("Press UP to start");
		text.setTranslateY(-50);
		text.setFont(Font.font(java.awt.Font.MONOSPACED, 50));
		text.setFill(Color.GHOSTWHITE);
		game.getChildren().add(text); 

		game.getChildren().add(currentScore); 

		int counter = 0; 
		//test
		for (int i=0; i<pipeCount; i++) {
			rectangle1[i] = new Rectangle();
			rectangle2[i] = new Rectangle();

			rectangle1[i].setFill(Color.rgb(66, 244, 113));
			rectangle2[i].setFill(Color.rgb(66, 244, 113));


			rectangle1[i].setWidth(75);

			rectangle2[i].setWidth(75);

			rectangle1[i].setTranslateX(600+counter);


			rectangle2[i].setTranslateX(600+counter);
			position[i] = 600 + counter; 
			counter+=250;

			setSize(i, counter); 
		}

		for (int i=0; i<pipeCount; i++) {
			game.getChildren().add(rectangle1[i]);
			game.getChildren().add(rectangle2[i]);


		}

		restart.setOnMouseClicked(e->
		{
			if (!move) {
				game.getChildren().clear();
				move = true; 
				runningGame(); 
			}

		});
		game.setOnKeyPressed(e->{
			if (e.getCode() == KeyCode.UP) {
				if (move) {
					begin();

				}
				else {
					game.getChildren().clear();
					move = true; 
					runningGame(); 
				}
			}
		}
				); 


		//		game.setOnMouseClicked(e->gravity());
		gravity(); 
	}


	public void begin() {
		//		y = birdView.getTranslateY()-20;   
		//		birdView.setTranslateY(y);

		
			animation.start();
		
		birdView.setManaged(false);
		ellipse.setManaged(false);
		gravity=-5.1; 
		//		birdView.setRotate(-30); //needs revising 
		for (int i=0; i<pipeCount; i++) {
			rectangle1[i].setManaged(false);
			rectangle2[i].setManaged(false);
		}
		text.setText("");


	}


	public void gravity() {
		animation = new AnimationTimer() {
			public void handle(long currentNanoTime) {
				if (!check()) {
					game.getChildren().add(gameOver); 
					animation.stop(); 
					restart.setText("RESTART");
					move = false; 
				}

				pipes(); 

				

					if(gravity >= 12) {
						gravity = 12;
					}
					else {
						gravity += 0.26;
					}

					birdView.setY(birdView.getY() + gravity);
					ellipse.setCenterY(ellipse.getCenterY()+gravity);
					//					System.out.println(gravity);

					if (gravity>=0 && gravity<2) {
						birdView.setRotate(0);
						ellipse.setRotate(0);
					}
					else if (gravity>-6 && gravity<-4) {
						birdView.setRotate(-30);
						ellipse.setRotate(-30);
					}
					else if (gravity>-4 && gravity<-2) {
						birdView.setRotate(-20);
						ellipse.setRotate(-20);
					}
					else if (gravity>-2 && gravity<0) {
						birdView.setRotate(-10);
						ellipse.setRotate(-10);
					}
					else if (gravity>=2 && gravity<2.5) {
						birdView.setRotate(10);
						ellipse.setRotate(10);
					}
					else if (gravity>=2.5 && gravity<3) {
						birdView.setRotate(20);
						ellipse.setRotate(20);
					}
					else if (gravity>=3 && gravity<3.5) {
						birdView.setRotate(30);
						ellipse.setRotate(30);
					}
					else if (gravity>=3.5 && gravity<4) {
						birdView.setRotate(40);
						ellipse.setRotate(40);
					}
					else if (gravity>=4.5 && gravity<5) {
						birdView.setRotate(50);
						ellipse.setRotate(50);
					}
					else if (gravity>=5 && gravity<5.5) {
						birdView.setRotate(60);
						ellipse.setRotate(60);
					}
					else if (gravity>=5.5 && gravity<6) {
						birdView.setRotate(70);
						ellipse.setRotate(70);
					}
					else if (gravity>=6 && gravity<6.5) {
						birdView.setRotate(80);
						ellipse.setRotate(80);
					}
					else if (gravity>=6.5 && gravity<7) {
						birdView.setRotate(90);
						ellipse.setRotate(90);
					}
				}

			
		};

	}




	public void pipes() {
		if (move) {
			for (int i=0; i<pipeCount; i++) {
				rectangle1[i].setX(rectangle1[i].getX()-2.2);
				rectangle2[i].setX(rectangle2[i].getX()-2.2);

				//				rectangle1[i].setFill(Color.color(Math.random(), Math.random(), Math.random())); 
				//				rectangle2[i].setFill(Color.color(Math.random(), Math.random(), Math.random())); 
				if (position[i]+rectangle1[i].getX()>-2 && position[i]+rectangle1[i].getX()<2 && !scoreCheck[i]) {
					score++; 
					currentScore.setText("Current score: "+score);
					System.out.println(score);
					scoreCheck[i] = true; 

				}
			}
		}
	}
	public void Music() {
		//Gets the music file and starts it
		String path = Main.class.getResource("/resources/Snow halation - Love Live!.mp3").toString();
		//chill, relaxing anime music for a tilting game 
		Media media = new Media(path);
		player = new MediaPlayer(media);
		player.setCycleCount(MediaPlayer.INDEFINITE);
		player.play();
	}

	public void buttonClick(Button btn) {
		if (btn.getText() == "") {
			System.out.println("Start button has been pressed. The game has started.");
			window.setScene(gameScene);
			runningGame(); 
		} 
		else if (btn.getText()==" ") {
			System.out.println("Leaderboard button has been pressed");
		}
		else {
		} 
	}

	public boolean check() {
		boolean flag = true; 
		
		if (ellipse.getCenterY()>=110) {
			flag = false; 
		}
		if (checkBounds(ellipse)) {
			flag = false; 
		}

		return flag; 
	}

	public boolean checkBounds(Ellipse ellipse) {
		// TODO Auto-generated method c
		boolean collision = false; 
		for (Rectangle rect : rectangleArrayList) {
			if (ellipse.getBoundsInParent().intersects(rect.getBoundsInParent())) {
				collision = true;
				
			}

		}
		return collision; 
	}

	public static void main(String args[]) {
		launch(args);
	}
}