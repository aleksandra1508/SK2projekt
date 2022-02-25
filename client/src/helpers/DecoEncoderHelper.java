package helpers;

public class DecoEncoderHelper {

//     to change position(E4)to array of ints

    public static int[] decodePosition(String stand) {
        int[] arr = new int[2];

        switch (stand.charAt(0)) {
            case 'A':
                arr[0] = 1;
                
                break;
            case 'B':
                arr[0] = 2;
                break;
            case 'C':
                arr[0] = 3;
                break;
            case 'D':
                arr[0] = 4;
                break;
            case 'E':
                arr[0] = 5;
                break;
            case 'F':
                arr[0] = 6;
                break;
            case 'G':
                arr[0] = 7;
                break;
            case 'H':
                arr[0] = 8;
                break;
        }
        arr[1] = Integer.parseInt(stand.substring(1, 2));
        return arr;
    }

    public static String codePosition(int a, int b) {
        String stand="";
        switch (a) {
            case 1: stand="A";
                break;
            case 2: stand="B";
                break;
            case 3: stand="C";
                break;
            case 4: stand="D";
                break;
            case 5: stand="E";
                break;
            case 6: stand="F";
                break;
            case 7: stand="G";
                break;
            case 8: stand="H";
                break;
        }
        stand=stand+b;
        return stand;
    }
}
