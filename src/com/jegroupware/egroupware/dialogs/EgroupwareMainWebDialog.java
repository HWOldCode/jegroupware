/*
 * jegroupware - Egroupware Java Client
 * this is a java http/https egroupware client, for use please check the example
 *
 * @link https://www.hw-softwareentwicklung.de
 * @author Stefan Werfling <stefan.werfling-AT-hw-softwareentwicklung.de>
 * @package jegroupware
 * @copyright (c) 2012-15 by Stefan Werfling <stefan.werfling-AT-hw-softwareentwicklung.de>
 * @license http://opensource.org/licenses/GPL-2.0 GPL2 - GNU General Public License, version 2 (GPL-2.0)
 * @version 14.2.6
 */
package com.jegroupware.egroupware.dialogs;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * EgroupwareMainWebDialog
 * @see http://docs.oracle.com/javafx/2/webview/jfxpub-webview.htm#CEGHBDHF
 * @author Stefan Werfling
 */
public class EgroupwareMainWebDialog extends Application {
    
    private EgroupwareBrowser _browser;
    private Scene _scene;
    static private String _url;
    
    public EgroupwareMainWebDialog() {
        
    }
    
    @Override 
    public void start(Stage stage) {
        stage.setIconified(true);
        StackPane root = new StackPane();
        
        WebView view = new WebView();
        WebEngine engine = view.getEngine();
        
        engine.getLoadWorker().stateProperty().addListener(
            new ChangeListener<State>() {
              @Override public void changed(ObservableValue ov, State oldState, State newState) {

                    if (newState == Worker.State.SUCCEEDED) {
                        stage.setTitle(engine.getLocation());
                        System.out.println("called");
                    }
                    
                    if( engine.getLoadWorker().getException() != null && newState == State.FAILED ){
                        System.out.println(", " + engine.getLoadWorker().getException().toString());
                    }
                }
            });
        
        engine.load(this._url);
        root.getChildren().add(view);
        
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
        
        
        /*this._browser = new EgroupwareBrowser(this._url);
        this._scene = new Scene(this._browser, 750, 500, Color.web("#666970"));
        stage.setScene(this._scene);
        
        //this._scene.getStylesheets().add("webviewsample/BrowserToolbar.css");     
        stage.show();*/
    }
    
    public void show(String url) {
        this._url = url;
        launch();
    }

    private static class EgroupwareBrowser extends Region {

        final WebView _browser = new WebView();
        private WebEngine _webEngine = null;
        
        public EgroupwareBrowser(String url) {
            this._webEngine = this._browser.getEngine();
            
            this._webEngine.setOnError(new EventHandler<WebErrorEvent>() {

                @Override
                public void handle(WebErrorEvent t) {
                    System.out.println(t.getMessage());
                }
            });
            
            //this._webEngine.
            //this._browser.get
            //Map<String, String> extraHeaders = new HashMap<String, String>();
            //extraHeaders.put("Referer", "http://www.referer.tld/login.html");
            
            this._webEngine.setJavaScriptEnabled(true);
            this._webEngine.load(url);
            
            //apply the styles
            this.getStyleClass().add("browser");
            // load the web page
            //this._webEngine.load("http://www.oracle.com/products/index.html");
            //add the web view to the scene
            this.getChildren().add(this._browser);
        }
        
        private Node createSpacer() {
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            return spacer;
        }
 
        @Override 
        protected void layoutChildren() {
            double w = getWidth();
            double h = getHeight();
            layoutInArea(this._browser,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
        }
 
        @Override 
        protected double computePrefWidth(double height) {
            return 750;
        }
 
        @Override 
        protected double computePrefHeight(double width) {
            return 500;
        }
    }
}