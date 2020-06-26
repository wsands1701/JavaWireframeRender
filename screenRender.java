//import java.awt.event.*; 
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.plaf.ColorUIResource;

public class screenRender extends Canvas{
    private BufferStrategy strategy;
    //set to Int to remove unwanted behavior
    public int zDir = 2;
    public int zCenterCube1 = 300;
    public screenRender(){
        JFrame container = new JFrame("3d Rendering Demo");
        JPanel panel = (JPanel) container.getContentPane();
        panel.setPreferredSize(new Dimension(800,600));
        panel.setLayout(null);
        setBounds(0,0,800,600);
        panel.add(this);
        setIgnoreRepaint(true);
        container.pack();
		container.setResizable(false);
		container.setVisible(true);
        container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
        });
        createBufferStrategy(2);
        strategy = getBufferStrategy();
        renderLoop();
    }
    public void renderLoop() {
        long duration = 0;
        long fps=0;
        ArrayList<line3d> lineList = buildCubeLines();
        ArrayList<line3d> cube1 = buildGenericCube(200, 200, 300, 100);
        ArrayList<line3d> pyr1 = buildPyramid(600, 200, 100, 150);
        while(true){
            long startTime = System.nanoTime(); 
            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
            //g.setColor(new Color(0,239,255));
            g.setColor(Color.black);
            g.fillRect(0,0,800,600);
            g.setColor(Color.white);
            g.drawLine(0, 300, 800, 300);
            g.drawLine(0, 600, 400, 300);
            g.drawLine(800, 600, 400, 300);
            int[] xRoad = {0, 400, 800};
            int[] yRoad = {600, 300, 600};
            g.setColor(Color.gray);
            g.fillPolygon(xRoad, yRoad , 3);
            g.setColor(Color.yellow);
            g.drawLine(400, 600, 400, 300);
            // g.setColor(new Color(42,186,90));
            // int[] x1Grass = {0,400,0};
            // int[] x2Grass = {800,400,800};
            // int[] y1Grass = {300,300,600};
            // int[] y2Grass = {300,300,600};
            // g.fillPolygon(x1Grass, y1Grass, 3);
            // g.fillPolygon(x2Grass, y2Grass, 3);
            drawLines(lineList, g);
            drawLines(cube1, g);
            drawLines(pyr1, g);
            g.drawString("FrameTime:"+duration+" ms" , 10, 10);
            g.drawString("FPS:"+fps, 10, 25);
            g.dispose();
            strategy.show();
            try { Thread.sleep(5); } catch (Exception e) {}
            long endTime = System.nanoTime();
            duration = (endTime - startTime); 
            duration = duration/1000000; 
            fps = 1000/duration;
            lineList = rotateCube(lineList, 0.004 , 400, 250);
            cube1=moveZ(cube1);
            cube1 = rotateCube(cube1, -.003, 200, zCenterCube1);
            pyr1 = rotateCube(pyr1, .004, 600, 100);
        }
    } 
    public void drawLines(ArrayList<line3d> lineList, Graphics2D g){
        for(line3d l: lineList){
            double x1 = l.getX1();
            double x2 = l.getX2();
            double y1 = l.getY1();
            double y2 = l.getY2();
            double z1 = l.getZ1();
            double z2 = l.getZ2();
            //Do scaling 
            double maxDistance = 2000;
            //Round to nearest pixel
            int rx1 = scaleX(x1, z1, maxDistance);
            int rx2 = scaleX(x2, z2, maxDistance);
            int ry1 = scaleY(y1, z1, maxDistance);
            int ry2 = scaleY(y2, z2, maxDistance);
            // System.out.println( rx1 + "," + ry1 );
            // System.out.println( rx2 + "," + ry2 );
            g.setColor(Color.green);
            g.drawLine(rx1, ry1, rx2, ry2);
        }
    } 
    public ArrayList<line3d> moveZ(ArrayList<line3d> list){
        boolean flipDir = false;
        for(line3d l:list){
            double z1 = l.getZ1();
            double z2 = l.getZ2();
            l.setZ1(z1+zDir);
            l.setZ2(z2+zDir);
            z1 = l.getZ1();
            z2 = l.getZ2();
            if(z1>1200||z1<10||z2>1200||z2<10){
                flipDir = true;
            }
        }
        if(flipDir){
            zDir = -1*zDir;
        }
        zCenterCube1+=zDir;
        return list;
    } 
    public int scaleX(double x, double z, double maxDistance){
        double scaledX = x;
        double scaleFactor = z/maxDistance;
        //find distance from center
        double diff = Math.abs(400-scaledX);
        double ammountTowardsCenter = scaleFactor*diff;
        if(scaledX>=400){
            scaledX = scaledX - Math.abs(ammountTowardsCenter);
        }
        else{
            scaledX = scaledX + Math.abs(ammountTowardsCenter);
        }
        return (int)Math.round(scaledX);
    }
    public int scaleY(double y, double z, double maxDistance){
        double scaledY = y;
        double scaleFactor = z/maxDistance;
        //find distance from center
        double diff = Math.abs(300-scaledY);
        double ammountTowardsCenter = scaleFactor*diff;
        if(scaledY>=300){
            scaledY = scaledY - Math.abs(ammountTowardsCenter);
        }
        else{
            scaledY = scaledY + Math.abs(ammountTowardsCenter);
        }
        return (int)Math.round(scaledY);
    }
    public ArrayList<line3d> buildCubeLines(){
        ArrayList<line3d> cubeLines = new ArrayList<line3d>();
        cubeLines.add(new line3d(300, 200, 150, 500, 200, 150));
        cubeLines.add(new line3d(300, 200, 150, 300, 400, 150));
        cubeLines.add(new line3d(500, 400, 150, 300, 400, 150));
        cubeLines.add(new line3d(500, 200, 150, 500, 400, 150));

        cubeLines.add(new line3d(300, 200, 350, 500, 200, 350));
        cubeLines.add(new line3d(300, 200, 350, 300, 400, 350));
        cubeLines.add(new line3d(500, 400, 350, 300, 400, 350));
        cubeLines.add(new line3d(500, 200, 350, 500, 400, 350));

        cubeLines.add(new line3d(300, 200, 350, 300, 200, 150));
        cubeLines.add(new line3d(500, 400, 350, 500, 400, 150));
        cubeLines.add(new line3d(500, 200, 350, 500, 200, 150));
        cubeLines.add(new line3d(300, 400, 350, 300, 400, 150));
        return cubeLines;
    }
    public ArrayList<line3d> buildGenericCube(double xCenter, double yCenter, double zCenter, double size){
        ArrayList<line3d> newCube = new ArrayList<line3d>();
        double v1x = xCenter - (size/2);
        double v1y = yCenter - (size/2);
        double v1z = zCenter + (size/2);
        double v2x = xCenter + (size/2);
        double v2y = yCenter - (size/2);
        double v2z = zCenter + (size/2);
        double v3x = xCenter + (size/2);
        double v3y = yCenter + (size/2); 
        double v3z = zCenter + (size/2);
        double v4x = xCenter - (size/2);
        double v4y = yCenter + (size/2);
        double v4z = zCenter + (size/2);
        double v5x = xCenter - (size/2);
        double v5y = yCenter - (size/2);
        double v5z = zCenter - (size/2);
        double v6x = xCenter + (size/2);
        double v6y = yCenter - (size/2);
        double v6z = zCenter - (size/2);
        double v7x = xCenter + (size/2);
        double v7y = yCenter + (size/2);
        double v7z = zCenter - (size/2);
        double v8x = xCenter - (size/2);
        double v8y = yCenter + (size/2);
        double v8z =  zCenter - (size/2);
        newCube.add(new line3d(v1x, v1y, v1z, v2x, v2y, v2z));
        newCube.add(new line3d(v3x, v3y, v3z, v2x, v2y, v2z));
        newCube.add(new line3d(v3x, v3y, v3z, v4x, v4y, v4z));
        newCube.add(new line3d(v1x, v1y, v1z, v4x, v4y, v4z));

        newCube.add(new line3d(v5x, v5y, v5z, v6x, v6y, v6z));
        newCube.add(new line3d(v7x, v7y, v7z, v6x, v6y, v6z));
        newCube.add(new line3d(v7x, v7y, v7z, v8x, v8y, v8z));
        newCube.add(new line3d(v5x, v5y, v5z, v8x, v8y, v8z));

        newCube.add(new line3d(v1x, v1y, v1z, v5x, v5y, v5z));
        newCube.add(new line3d(v2x, v2y, v2z, v6x, v6y, v6z));
        newCube.add(new line3d(v3x, v3y, v3z, v7x, v7y, v7z));
        newCube.add(new line3d(v4x, v4y, v4z, v8x, v8y, v8z));

        return newCube;
    }
    //given a list and and direction and speed, roate the cube (speed is degrees per iteration)
    public ArrayList<line3d> rotateCube(ArrayList<line3d> cubeLines, double speed, double x_0, double z_0){
        for (line3d line : cubeLines) {
            //For horizontal rotation, all y values stay the same, and x and z change
            double x1 = line.getX1();
            double x2 = line.getX2();

            double z1 = line.getZ1();
            double z2 = line.getZ2();

            double newX1 = x_0+ (x1-x_0)*Math.cos(speed) - (z1 - z_0)*Math.sin(speed);
            double newZ1 = z_0 + (x1-x_0)*Math.sin(speed) + (z1 - z_0)* Math.cos(speed);

            double newX2 = x_0+ (x2 - x_0)*Math.cos(speed) - (z2 - z_0)*Math.sin(speed);
            double newZ2 = z_0 + (x2-x_0)*Math.sin(speed) + (z2 - z_0)* Math.cos(speed);

            line.setX1(newX1);
            line.setX2(newX2);

            line.setZ1(newZ1);
            line.setZ2(newZ2);
        }
        return cubeLines;
    }
    public ArrayList<line3d> buildPyramid(double xCenter, double yCenter, double zCenter, double size){
        ArrayList<line3d> newPyramid = new ArrayList<line3d>();
        double x1 = xCenter - (size/2);
        double y1 = yCenter + (size/2);
        double z1 = zCenter  + (size/2);
        double x2 = xCenter + (size/2);
        double y2 = yCenter + (size/2);
        double z2 = zCenter + (size/2);
        double x3 = xCenter + (size/2);
        double y3 = yCenter + (size/2);
        double z3 = zCenter - (size/2);
        double x4 = xCenter - (size/2);
        double y4 = yCenter + (size/2);
        double z4 = zCenter - (size/2);
        double x5 = xCenter;
        double y5 = yCenter-(size/2);
        double z5 = zCenter;

        newPyramid.add(new line3d(x1, y1, z1, x2, y2, z2));
        newPyramid.add(new line3d(x3, y3, z3, x2, y2, z2));
        newPyramid.add(new line3d(x3, y3, z3, x4, y4, z4));
        newPyramid.add(new line3d(x1, y1, z1, x4, y4, z4));

        newPyramid.add(new line3d(x1, y1, z1, x5, y5, z5));
        newPyramid.add(new line3d(x2, y2, z2, x5, y5, z5));
        newPyramid.add(new line3d(x3, y3, z3, x5, y5, z5));
        newPyramid.add(new line3d(x4, y4, z4, x5, y5, z5));

        return newPyramid;
    }       
}