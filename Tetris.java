import objectdraw.*;
import java.awt.*;
import java.util.*;

public class Tetris extends ActiveObject{
    private static int yoko=10,tate=20;
    private static double begin_y = 50.0,begin_x = 100.0;
    private double siz = 30;
    private static int damy = 256-1, del_def = 350;
    private int bx = 1, DELAY_TIME = 350;;
    int box[][] = new int[yoko+8][tate+8];
    public Tet_board tbn,tbnext;
    public int score = 0;
    DrawingCanvas canvas;
    FramedOval fr,fl,fd;
    boolean write_flag = false,wf_score = false;
    public Tetris(DrawingCanvas canv){
        for(int i=4;i<tate+4;i++){
            for(int j = 0;j< yoko+2;j++)box[j][i] = 0;
        }
        for(int j=0;j<4;j++){
            for(int i=4;i<tate+4;i++){
                box[j][i] = damy;
                box[yoko+j+4][i] = damy;
            }
            for(int i=0;i<yoko+8;i++){
                box[i][tate+j+4] = damy;
                box[i][j] = damy;
            }
        }
        canvas = canv;
        fl = new FramedOval(begin_x+siz*yoko+30,begin_y+siz*tate+30,30,30,canvas);
        fr = new FramedOval(begin_x+siz*yoko+80,begin_y+siz*tate+30,30,30,canvas);
        start();
    }
    
    public void yoko_move(Location pt){
        if(fr.contains(pt))yokoidou(0);
        else if(fl.contains(pt))yokoidou(1);
        else if(fd.contains(pt))fast_down();
        else rotate(0);
    }

    public void fast_down(){
        if(DELAY_TIME==del_def)DELAY_TIME = 100;
        else DELAY_TIME = del_def;
    }
    
    public void yokoidou(int n){//n:0 ->,1<-.
        int dx = 1;
        if(n==1)dx = -1;
        write_block();
        tbn.now_x += dx;
        if(!check())tbn.now_x -= dx;
        write_block();
    }
    
    public void run(){
        Random rand = new Random();
        tbnext = new Tet_board(rand.nextInt(7));
        while(true){
            tbn = tbnext;
            tbnext = new Tet_board(rand.nextInt(7));
            write_block();
            print();
            while(true){
                pause ( DELAY_TIME );
                if(!down()){
                    print();
                    score += erase();
                    write_flag = true;
                    break;
                }
                print();
            }
        }
    }

    public void rotate(int dir){
        if(dir==0){//左回り.
            write_block();
            tbn._rotate();
            if(!check()){
                tbn._rotate();tbn._rotate();tbn._rotate();
            }
            write_block();
        }else{
            write_block();
            tbn._rotate();tbn._rotate();tbn._rotate();
            if(!check()){
                tbn._rotate();
            }
            write_block();
        }
    }
    
    public void write_block(){
        for(int xx = 0;xx<tbn.bx_x;xx++){
            for(int yy=0;yy<tbn.bx_y;yy++){
                box[tbn.now_x+xx][tbn.now_y+yy] ^= tbn.bnow[xx][yy];
            }
        }
    }
    
    public boolean down(){
        write_block();
        tbn.now_y += 1;
        if(!check()){
            tbn.now_y -= 1;
            write_block();
            return false;
        }else{
            write_block();
            return true;
        }
    }
    public int _erase(){
        int ans = 0;
        for(int j = tate-1+4;j>=4;j--){
            int tmp = 0;
            for(int i = 4;i<4+yoko;i++){
                if(box[i][j] == 1)tmp++;
            }
            if(tmp == 10){
                for(int i = 4;i<4+yoko;i++){
                    box[i][j] = 0;
                }
                ans += 10;
                break;
            }
        }
        return ans;
    }

    public void erase_move(){//削除後の移動
        for(int y = tate-1+4;y>=4;y--){
            for(int x=4;x<4+yoko;x++){
                if(box[x][y]==0){
                    if(box[x][y-1]==1){
                        box[x][y] = 1;
                        box[x][y-1] = 0;
                    }
                }
            }
        }
    }
    public int erase(){
        int ans = 0,tmp = 0;
        while(true){
            tmp = _erase();
            ans += tmp;
            if(tmp==0)break;
            if(tmp!=0)erase_move();
            tmp = 0;
        }
        if(ans==0)wf_score = true; 
        return ans;
    }
    
    public boolean check(){
        int ans = 0;
        for(int xx = 0;xx<tbn.bx_x;xx++){
            for(int yy=0;yy<tbn.bx_y;yy++){
                ans += box[tbn.now_x+xx][tbn.now_y+yy] & tbn.bnow[xx][yy];
            }
        }
        if(ans!=0)return false;
        return true;
    }
    public void print(){
        canvas.clear();
        if(/*wf_score*/ true){
            new Text(String.valueOf(score),begin_x,begin_y+siz*tate+30,canvas).setFontSize(30);
            wf_score = false;
        }
        fl = new FramedOval(begin_x+siz*yoko+30,begin_y+siz*tate+30,50,50,canvas);
        fr = new FramedOval(begin_x+siz*yoko+100,begin_y+siz*tate+30,50,50,canvas);
        if(DELAY_TIME==del_def)fd = new FramedOval(begin_x+siz*yoko+65,begin_y+siz*tate+80,50,50,canvas);
        else{
            fd = new FramedOval(begin_x+siz*yoko+65,begin_y+siz*tate+80,50,50,canvas);
            new FilledOval(begin_x+siz*yoko+65,begin_y+siz*tate+80,50,50,canvas);
        }
        new FramedRect(begin_x-1,begin_y-1,siz*yoko+2,siz*tate+2,canvas);
        for(int i=0;i<yoko;i++){
            for(int j = 0;j<tate;j++){
                if(box[i+4][j+4]==bx){
                    new FilledRect(begin_x+i*siz,begin_y+j*siz,siz,siz,canvas);
                    new FramedRect(begin_x+i*siz,begin_y+j*siz,siz,siz,canvas).setColor(Color.white);
                }
            }
        }
        if(/*write_flag*/ true){
            for(int x=0;x<tbnext.bx_x;x++){
                for(int y = 0;y<tbnext.bx_y;y++){
                    if(tbnext.bnow[x][y]==bx){
                        new FilledRect(begin_x+siz*yoko+30+x*siz,begin_y+y*siz,siz,siz,canvas);
                        new FramedRect(begin_x+siz*yoko+30+x*siz,begin_y+y*siz,siz,siz,canvas).setColor(Color.white);
                    }
                }
            }
            write_flag = false;
        }
    }
}