import objectdraw.*;
import java.awt.*;
import java.util.*;

public class Tet_board extends ActiveObject{
   public int[][] bnow = new int[3][3];
   public int bx_x,bx_y;
   public int now_x,now_y,type;
   public Tet_board(int typ){
        now_x = 8;
        now_y = 4;
        type  = typ;
        set(typ);
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
   public void _rotate(/*int type*/){
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
}