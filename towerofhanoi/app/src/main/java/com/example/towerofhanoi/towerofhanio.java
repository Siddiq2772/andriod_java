package com.example.towerofhanoi;

//tower of hanio project

public class towerofhanio {
    int[][] arr = new int[3][3];
    int[] top = new int[3];
    int vpop;
    int temp;

    towerofhanio() {
        for (int i = 2; i >= 0; i--) {
            arr[0][i] = 3 - i;
            top[i] = -1;
        }
        top[0] = 2;
    }

    public void print() {
        for (int i = 2; i >= 0; i--) {
            System.out.print("  [" + arr[0][i] + "]  [" + arr[1][i] + "]  [" + arr[2][i] + "]");
            System.out.println();
        }
    }

    public boolean check(int a) {
        if (top[a] == -1)
            return true;
        else
            return (arr[a][top[a] + 1] > vpop);

    }

    public void pop(int a) {
        a -= 1;
        try {
            if(top[a]!=-1){
                vpop = arr[a][top[a]];
                arr[a][top[a]] = 0;
                top[a]--;
                temp =a;
            }
            else{
            System.out.println("invalid choice!! try again" );
                
            }
            

        } catch (Exception e) {
            System.out.println("invalid choice!! try again" );
           
        }

    }

    public void push(int a) {
        a -= 1;
        try {
            if (check(a)) {
                top[a] ++;
                arr[a][top[a]] = vpop;
            } else
                throw new Exception();

        } catch (Exception e) {
            System.out.println("invalid choice!! try again");
            top[temp]++;
            arr[temp][top[temp]] = vpop;
            
        }

    }

    /*public static void main(String[] args) {
        towerofhanio t = new towerofhanio();
        Scanner sc = new Scanner(System.in);
        int count =0;
        while (t.top[2] != 2) {
            t.print();
            System.out.print("Enter to pop form: ");
            t.pop(sc.nextInt());
            System.out.print("Enter to push to: ");
            t.push(sc.nextInt());
            count++;
        }
        t.print();
        System.out.println("completed in "+count+" rounds");
        sc.close();
    }*/

}