import objectdraw.*;
import java.awt.*;
import java.util.*;

public class Tetris extends ActiveObject{
    private static int yoko=10,tate=20;
    private static double begin_y = 50.0,begin_x = 100.0;
    private double siz = 30;
    private static int damy = 256-1;
    private int bx = 1;
    private int DELAY_TIME = 350;
    private static int del_def = 350;
    int box[][] = new int[yoko+8][tate+8];
    int bnow[][] = new int[3][3];
    int now_x=3,now_y=3;//今見てるブロックの座標
    int bx_x=3,bx_y=3;//今見てるブロックのx,yのサイズ
    int rot = 0;//rot = 0,1,2,3//回転を表す.
    public int typ = 0;
    public int score = 0;
    DrawingCanvas canvas;
    FramedOval fr,fl,fd;
    
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
        else rotate(typ,0);
    }

    public void fast_down(){
        if(DELAY_TIME==del_def)DELAY_TIME = 100;
        else DELAY_TIME = del_def;
    }
    
    public void yokoidou(int n){//n:0 ->,1<-.
        int dx = 1;
        if(n==1)dx = -1;
        write_block();
        now_x += dx;
        if(!check())now_x -= dx;
        write_block();
    }
    
    public void run(){
        Random rand = new Random();
        while(true){
            int nxt = rand.nextInt(7);
            make_block(nxt);
            write_block();
            print();
            while(true){
                pause ( DELAY_TIME );
                if(!down()){
                    print();
                    score += erase();
                    break;
                }
                print();
            }
        }
    }

    public void rotate(int type,int dir){
        if(dir==0){//左回り.
            write_block();
            _rotate(type);
            if(!check()){
                _rotate(type);
                _rotate(type);
                _rotate(type);
            }
            write_block();
        }else{
            write_block();
            _rotate(type);
            _rotate(type);
            _rotate(type);
            if(!check()){
                _rotate(type);
            }
            write_block();
        }
    }
    
    public void write_block(){
        System.out.println("----------");
        System.out.println(bx_x);
        System.out.println(bx_y);
        for(int xx = 0;xx<bx_x;xx++){
            for(int yy=0;yy<bx_y;yy++){
                box[now_x+xx][now_y+yy] ^= bnow[xx][yy];
            }
        }
    }
    
    public boolean down(){
        write_block();
        now_y += 1;
        if(!check()){
            now_y -= 1;
            write_block();
            return false;
        }else{
            write_block();
            return true;
        }
    }

    public void _rotate(int type){
        switch(type){//0 : 3*3, 1:4*1, 2:2*2
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                int tmp[][] = new int[3][3];
                tmp[1][1] = bnow[1][1];
                tmp[0][0] = bnow[2][0];
                tmp[0][2] = bnow[0][0];
                tmp[2][2] = bnow[0][2];
                tmp[2][0] = bnow[2][2];
                tmp[0][1] = bnow[1][0];
                tmp[1][2] = bnow[0][1];
                tmp[2][1] = bnow[1][2];
                tmp[1][0] = bnow[2][1];
                bnow = tmp;
                break;
            case 6:
                int tp = bx_x;
                bx_x = bx_y;
                bx_y = tp;
                int tpb[][] = new int[bx_x][bx_y];
                for(int x=0;x<bx_x;x++){
                    for(int y=0;y<bx_y;y++){
                        tpb[x][y] = bnow[y][x];
                    }
                }
                bnow = tpb;
                break;
            default:
                break;
        }
    }
    public void make_block(int type){
        typ = type;
        set(type);
        now_x = 8;
        now_y = 4;
    }

    public void set(int type){
        bx_x = 3;
        bx_y = 3;
        int tmp[][] = new int[3][3];
        int tp[][] = new int[2][2];
        int tpl[][] = new int[1][4];
        switch(type){
            case 0:
                tmp[0][1] = 1;
                tmp[1][1] = 1;
                tmp[1][0] = 1;
                tmp[2][1] = 1;
                break;
            case 1:
                tmp[2][1] = 1;
                tmp[0][2] = 1;
                tmp[1][2] = 1;
                tmp[2][2] = 1;
                break;
            case 2:
                tmp[0][1] = 1;
                tmp[0][2] = 1;
                tmp[1][2] = 1;
                tmp[2][2] = 1;
                break;
            case 3:
                tmp[0][1] = 1;
                tmp[1][1] = 1;
                tmp[1][2] = 1;
                tmp[2][2] = 1;                
                break;
            case 4:
                tmp[1][1] = 1;
                tmp[1][2] = 1;
                tmp[0][2] = 1;
                tmp[2][1] = 1;
                break;
            case 5:
                bx_x = 2;
                bx_y = 2;
                tp[0][0] = 1;
                tp[0][1] = 1;
                tp[1][0] = 1;
                tp[1][1] = 1;
                break;
            case 6:
                tpl[0][0] = 1;
                tpl[0][1] = 1;
                tpl[0][2] = 1;
                tpl[0][3] = 1;
                bx_x = 1;
                bx_y = 4;
                break;
            default:
                break;
        }
        bnow = tmp;
        if(type == 5)bnow = tp;
        if(type == 6)bnow = tpl;
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
        int ans = 0;
        int tmp = 0;
        while(true){
            tmp = _erase();
            ans += tmp;
            if(tmp==0)break;
            if(tmp!=0)erase_move();
            tmp = 0;
        }
        return ans;
    }
    
    public boolean check(){
        int ans = 0;
        for(int xx = 0;xx<bx_x;xx++){
            for(int yy=0;yy<bx_y;yy++){
                ans += box[now_x+xx][now_y+yy] & bnow[xx][yy];
            }
        }
        if(ans!=0)return false;
        return true;
    }
    public void print(){
        canvas.clear();
        //score += erase();
        new Text(String.valueOf(score),begin_x,begin_y+siz*tate+30,canvas).setFontSize(30);
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
                if(box[i+4][j+4]==bx)new FilledRect(begin_x+i*siz,begin_y+j*siz,siz,siz,canvas);
                new FramedRect(begin_x+i*siz,begin_y+j*siz,siz,siz,canvas).setColor(Color.white);
            }
        }
    }
}