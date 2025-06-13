package global;

import frames.GMainFrame;

public class GMain {
    // main method
    public static void main(String[] args) {
    	//create aggregation hierarchy
    	GMainFrame mainframe = new GMainFrame();
    	//hierarchy traverse
    	mainframe.initialize();
    }
}