public class line3d{
    private double x1;
    private double y1;
    private double z1;
    private double x2;
    private double y2;
    private double z2;

    public line3d(double xin1 , double yin1, double zin1, double xin2, double yin2, double zin2){
        this.x1 = xin1;
        this.y1 = yin1;
        this.z1 = zin1;
        this.x2 = xin2;
        this.y2 = yin2;
        this.z2 = zin2;
    }
    public double getX1(){
        return this.x1;
    }
    public double getY1(){
        return this.y1;
    }
    public double getZ1(){
        return this.z1;
    }
    public double getX2(){
        return this.x2;
    }
    public double getY2(){
        return this.y2;
    }
    public double getZ2(){
        return this.z2;
    }

    public void setX1(double newX){
        this.x1  = newX;
    }
    public void setY1(double newY){
        this.y1 = newY;
    }
    public void setZ1(double newZ){
        this.z1 = newZ;
    }
    public void setX2(double newX){
        this.x2  = newX;
    }
    public void setY2(double newY){
        this.y2 = newY;
    }
    public void setZ2(double newZ){
        this.z2 = newZ;
    }
}