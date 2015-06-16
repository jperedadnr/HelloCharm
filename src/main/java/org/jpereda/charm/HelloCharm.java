package org.jpereda.charm;

import com.gluonhq.charm.down.common.JavaFXPlatform;
import com.gluonhq.charm.glisten.application.MobileApplication;
import static com.gluonhq.charm.glisten.application.MobileApplication.HOME_VIEW;
import com.gluonhq.charm.glisten.control.Dialog;
import com.gluonhq.charm.glisten.control.Icon;
import com.gluonhq.charm.glisten.layout.layer.MenuPopupView;
import com.gluonhq.charm.glisten.layout.layer.SidePopupView;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.charm.glisten.visual.Swatch;
import java.util.Optional;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class HelloCharm extends MobileApplication {
    
    private final Label labelDialog = new Label();
    private final Label labelCheck = new Label();
    private ToolBar homeToolbar;
    
    
    @Override
    public void init() throws Exception {
        super.init(); 
        
        /*
        Create default home view
        */
        addViewFactory(HOME_VIEW, ()->{
            View home = new View("Home View");
            
            VBox vBox = new VBox(20,new Label("Hello Charm!"),labelDialog,labelCheck);
            vBox.setAlignment(Pos.CENTER);
            
            home.setCenter(vBox);
            
            /*
            BOTTOM
            */
            homeToolbar = new ToolBar();
            
            Button menu = MaterialDesignIcon.MENU.button(e->showLayer("Side Menu"));
            
            Button dialog = MaterialDesignIcon.DIALPAD.button(e->showDialog());
            
            HBox hbox = new HBox();
            HBox.setHgrow(hbox, Priority.ALWAYS);
            
            Button more = MaterialDesignIcon.MORE_VERT.button(e->showLayer("Popup"));
            
            homeToolbar.getItems().addAll(menu,dialog,hbox,more);
            
            home.setBottom(homeToolbar);
            
            /*
            JavaFX thread
            */
            home.showingProperty().addListener((obs,b,b1)->{
                if(b1){
                    home.setSwatch(Swatch.AMBER);
                    
                    if(JavaFXPlatform.isDesktop()){
                        home.getScene().getWindow().setWidth(400);
                        home.getScene().getWindow().setHeight(700);
                    }
                }
            });
            
            return home;
        });
        
        /*
        Other View
        */
        
        addViewFactory("Other View", ()->{
            View otherView = new View("Other View");
            
            otherView.setCenter(new Button("Other view"));
            
            /*
            BOTTOM
            */
            ToolBar toolBar = new ToolBar();
            
            // Icon approach
            Button menu = new Button(null,new Icon(MaterialDesignIcon.MENU));
            menu.getStyleClass().add("icon-toggle");
            menu.setOnAction(e->showLayer("Side Menu"));
            
            toolBar.getItems().add(menu);
            
            otherView.setBottom(toolBar);
            
            return otherView;            
        });
        
        /*
        SideMenu
        */
        addLayerFactory("Side Menu", ()->{
            BorderPane borderPane = new BorderPane();
            borderPane.setStyle("-fx-background-color: -primary-swatch-100;");
            borderPane.setPrefWidth(200);
            
            Label labelTop = new Label("User not registered",MaterialDesignIcon.ACCOUNT_CIRCLE.graphic());
            HBox hbox = new HBox(labelTop);
            hbox.setPadding(new Insets(20));
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.setStyle("-fx-background-color: -primary-swatch-500;");
        
            borderPane.setTop(hbox);
            
            HBox homeView = new HBox(new Label("Home View",MaterialDesignIcon.HOME.graphic()));
            homeView.setAlignment(Pos.CENTER_LEFT);
            homeView.setPrefHeight(50);
            homeView.setStyle("-fx-padding: 0 10 0 10;-fx-border-color: #e0e0e0; -fx-border-width: 1 0 0 0;");
            homeView.setOnMouseClicked(e->{
                hideLayer("Side Menu");
                switchView(HOME_VIEW);
            });

            HBox otherView = new HBox(new Label("Other View",MaterialDesignIcon.CAMERA.graphic()));
            otherView.setAlignment(Pos.CENTER_LEFT);
            otherView.setPrefHeight(50);
            otherView.setStyle("-fx-padding: 0 10 0 10;-fx-border-color: #e0e0e0; -fx-border-width: 1 0 0 0;");
            otherView.setOnMouseClicked(e->{
                MobileApplication.getInstance().hideLayer("Side Menu");
                MobileApplication.getInstance().switchView("Other View");
            });

            ToggleButton check = new ToggleButton();
            check.getStyleClass().add("switch");
            check.selectedProperty().addListener((obs,b0,b1)->{
                labelCheck.setText("User selected: "+(b1?"On":"Off"));
            });
            
            HBox gap=new HBox();
            gap.setMinWidth(20);
            HBox.setHgrow(gap, Priority.ALWAYS);
            HBox settings = new HBox(new Label("Select option",MaterialDesignIcon.SETTINGS.graphic()),gap,check);
            settings.setAlignment(Pos.CENTER_LEFT);
            settings.setPrefHeight(50);
            settings.setStyle("-fx-padding: 0 10 0 10; -fx-border-color: #e0e0e0; -fx-border-width: 1 0 1 0;");
            
            HBox empty = new HBox();
            VBox.setVgrow(empty, Priority.ALWAYS);
        
            VBox center = new VBox();
            center.getChildren().addAll(homeView,otherView,settings,empty);
            borderPane.setCenter(center);
            
            SidePopupView sideMenu=new SidePopupView(borderPane, Side.LEFT);
            
            return sideMenu;
        });
        
        /*
        Popup
        */
        addLayerFactory("Popup", ()->{
            Menu menu=new Menu();
            MenuItem itemBlue = new MenuItem("Blue swatch",MaterialDesignIcon.STAR.graphic());
            itemBlue.setOnAction(e->{
                hideLayer("Popup");
                getView().setSwatch(Swatch.BLUE);
            });
            MenuItem itemAmber = new MenuItem("Amber swatch",MaterialDesignIcon.REFRESH.graphic());
            itemAmber.setOnAction(e->{
                hideLayer("Popup");
                getView().setSwatch(Swatch.AMBER);
            });
            menu.getItems().addAll(itemBlue,itemAmber);

            MenuPopupView menuPopupView = new MenuPopupView(homeToolbar.getItems().get(homeToolbar.getItems().size()-1), menu);
            return menuPopupView;
        });
        
    }
    
    private void showDialog() {
        HBox title = new HBox(10);
        title.setAlignment(Pos.CENTER_LEFT);
        title.getChildren().add(new ImageView(new Image(getClass().getResource("G_Grey_charm_opaque.png").toExternalForm(),64,64,true,true)));
        title.getChildren().add(new Label("Title"));
        Dialog<ButtonType> dialog = new Dialog();
        dialog.setContent(new Label("This is a dialog. \nDo you want to continue?"));
        dialog.setTitle(title);
        Button yes = new Button("Yes");
        yes.setOnAction(e->{dialog.setResult(ButtonType.YES); dialog.hide();});
        yes.setDefaultButton(true);
        Button no = new Button("No");
        no.setCancelButton(true);
        no.setOnAction(e->{dialog.setResult(ButtonType.NO); dialog.hide();});
        dialog.getButtons().addAll(yes,no);
        Platform.runLater(()->{
            Optional result = dialog.show();
            if(result.isPresent()){
                labelDialog.setText("User selected: "+(result.get().equals(ButtonType.YES)?"Yes":"No"));
            }
        });
    }

}
