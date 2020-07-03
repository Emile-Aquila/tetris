import objectdraw.*;
import java.awt.*;

public class TetrisControl extends WindowController {
    Tetris tet;
    private static int yoko=10,tate=20;
    private static double begin_y = 50.0,begin_x = 100.0;
    private double siz = 30;
    boolean tmp = false;
    FramedOval fr,fl;
    @Override public void begin() {
        new FramedRect(begin_x,begin_y,siz*yoko,siz*tate,canvas);
    }

    @Override public void onMouseClick( Location pt ) {
        if(!tmp)tet = new Tetris(canvas);
        else{
        	tet.yoko_move(pt);
        }
        tmp = true;
    }

    @Override public void onMouseExit( Location point ){
    }
}
