package game;
import javafx.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import chat.PeerToPeerSocket;
import handlers.ExitHandler;
import handlers.NewGameHandler;
import handlers.PanHandler;
import handlers.ResizeHandler;
import handlers.SoundHandler;
import handlers.VideoHandler;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import viewable.Viewable;
import viewable.towers.Tower;
import viewable.towers.TowerType;

public class TowerDefenseView extends Application implements Observer{
	private static final int VIEWABLE_ROWS = 8;
	private static final int VIEWABLE_COLS = 8;
	private Stage stage;
	private TowerDefenseController controller;
	private ViewModel model;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		PeerToPeerSocket p2p = new PeerToPeerSocket();
		Thread thread = new Thread(p2p);
		thread.start();
		Thread p2p2 = new Thread(new PeerToPeerSocket("localhost",7000));
		p2p2.start();
		controller = new TowerDefenseController(this);
		model = new ViewModel(900,1000, this);
		stage = primaryStage;
		BorderPane root = new BorderPane();
		
		root.setTop(createMenuBar());
		GridPane pane = createBoard();
		StackPane stack = new StackPane();
		stack.getChildren().add(pane);
		stack.getChildren().add(createChatBottom(p2p));
		stack.setPickOnBounds(false);
		
		root.setCenter(stack);
		
		primaryStage.setScene(new Scene(root, model.getWidth(), model.getHeight()));
		
		//primaryStage.getScene().widthProperty().addListener(new ResizeHandler(model, primaryStage, pane));
		//primaryStage.getScene().heightProperty().addListener(new ResizeHandler(model, primaryStage, pane));
		
		primaryStage.getScene().setOnMouseMoved(new PanHandler(model, primaryStage));
		primaryStage.setResizable(false);
		//loadMusic();
		primaryStage.show();
		primaryStage.sizeToScene();
	}

	public void update() {
		BorderPane pane = (BorderPane)stage.getScene().getRoot();
		while(controller.canMove()) {
			break;
		}
	}
	
	private BorderPane createChatBottom(PeerToPeerSocket p2p) {
		BorderPane box = new BorderPane();
		Button button = new Button();
		button.setText("Messages");
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					p2p.sendMessage("localhost", 7000, "Piece of Shit");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		box.setBottom(button);
		box.setPickOnBounds(false);
		return box;
	}
	
	private GridPane createBoard() throws FileNotFoundException {
		GridPane pane = new GridPane();
		
		Viewable[][] towers = controller.getBoard();
		for(int i =model.getCurrentRow();i<model.getCurrentRow()+VIEWABLE_ROWS;i++) {
			for(int j =model.getCurrentCol();j<model.getCurrentCol()+VIEWABLE_COLS;j++) {
				pane.add(getResource(towers[j][i], i, j), j, i);
			}
		}

		pane.setPickOnBounds(false);
		return pane;
	}
	
	private ImageView getResource(Viewable obj, int row, int col) throws FileNotFoundException {
		ImageView view;
		if(obj == null) {
			view = new ImageView(new Image(new FileInputStream(Viewable.getDefaultResource())));
			view.setUserData(obj);
		}else {
			view = new ImageView(new Image(new FileInputStream(obj.getResource())));
			view.setUserData(obj);
		}
		view.setFitHeight((double)model.getEffectiveBoardHeight()/VIEWABLE_ROWS);
		view.setFitWidth((double)model.getWidth()/VIEWABLE_COLS);
		
		view.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				System.out.println(e);
				ImageView v = (ImageView)e.getTarget();
				if(v.getUserData()==null) {
					controller.addTower(row, col, TowerType.BASICTOWER);
					e.consume();
				}else {
					System.out.println("tower");
				}
			}
		});
		return view;
	}
	
	private MenuBar createMenuBar() {
		// Create the menu bar.
		MenuBar bar = new MenuBar();
		
		bar.getMenus().add(createFileMenu());
		bar.getMenus().add(createOptionMenu());
		int menuHeight = 30;
		bar.setMinHeight(menuHeight);
		bar.setPrefHeight(menuHeight);
		bar.setMaxHeight(menuHeight);
		model.setMenuHeight(menuHeight);
		return bar;
	}
	
	private Menu createFileMenu() {
		// Create the file menu option, will hold save and exit commands.
		Menu file = new Menu();
		
		MenuItem newGame = new MenuItem();
		newGame.setText("New Game");
		newGame.setOnAction(new NewGameHandler());
		
		MenuItem exit = new MenuItem();
		exit.setText("Exit");
		exit.setOnAction(new ExitHandler());
		
		file.getItems().add(newGame);
		file.getItems().add(exit);
		file.setText("File");
		return file;
	}
	
	private Menu createOptionMenu() {
		// Create the option menu option, holds sound settings and game visual settings.
		Menu options = new Menu();
		
		MenuItem sound = new MenuItem();
		sound.setText("Sound");
		sound.setOnAction(new SoundHandler());
		
		MenuItem video = new MenuItem();
		video.setText("Video");
		video.setOnAction(new VideoHandler());
		
		options.getItems().add(sound);
		options.getItems().add(video);
		options.setText("Options");
		return options;
	}

	private void loadMusic() {
		try {
			String randomMusic = findRandomMusic();
			Media music = new Media(randomMusic);
			MediaPlayer player = new MediaPlayer(music);
			player.setOnEndOfMedia(new Runnable() {
				@Override
				public void run() {
					String randomMusic = "";
					while(randomMusic.equalsIgnoreCase("")) {
						try {
							randomMusic = findRandomMusic();
							break;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (URISyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					Media music = new Media(randomMusic);
					MediaPlayer player = new MediaPlayer(music);
					player.play();
					
				}
			});
			player.play();
		}catch(Exception ex) {
			// Show modal.
			System.out.println(ex.getMessage());
		}
	}
	
	private String findRandomMusic() throws IOException, URISyntaxException {
		File file = new File("./resources/music");
		List<File> files = new ArrayList<File>();
		for(File f: file.listFiles()) {
			files.add(f);
		}
		Collections.shuffle(files);
		return files.get(0).toURI().toString();
	}

	
	@Override
	public void update(Observable o, Object e) {
		// TODO Auto-generated method stub
		int i = Integer.parseInt(((String)e).split(" ")[0]);
		int j = Integer.parseInt(((String)e).split(" ")[1]);
		try {
			setBoard(i, j);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private void setBoard(int row, int col) throws FileNotFoundException {
		Viewable[][] board = controller.getBoard();
		Viewable obj = board[col][row];
		StackPane pane = (StackPane)((BorderPane)(stage.getScene().getRoot())).getCenter();
		GridPane grid = null;
		for(Node n : pane.getChildren()) {
			if(n instanceof GridPane) {
				grid = (GridPane)pane.getChildren().get(0);
			}
		}
		Node node = null;
		Node toRemove = null;
		for(Node n: grid.getChildren()) {
			if(GridPane.getColumnIndex(n)==col&&GridPane.getRowIndex(n)==row) {
				node = getResource(obj, row, col);
				toRemove = n;
			}
		}
		grid.getChildren().remove(toRemove);
		grid.add(node, col, row);
	}
}

