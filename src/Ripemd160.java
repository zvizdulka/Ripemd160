import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Ripemd160 {
    int k0 = 0x00000000;
    int k1 = 0x5A827999;
    int k2 = 0x6ED9EBA1;
    int k3 = 0x8F1BBCDC;
    int k4 = 0xA953FD4E;
    int k0s = 0x50A28BE6;
    int k1s = 0x5C4DD124;
    int k2s = 0x6D703EF3;
    int k3s = 0x7A6D76E9;
    int k4s = 0x00000000;
    int h0 = 0x67452301;
    int h1 = 0xEFCDAB89;
    int h2 = 0x98BADCFE;
    int h3 = 0x10325476;
    int h4 = 0xC3D2E1F0;
    int[] r = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
            7, 4, 13, 1, 10, 6, 15, 3, 12, 0, 9, 5, 2, 14, 11, 8,
            3, 10, 14, 4, 9, 15, 8, 1, 2, 7, 0, 6, 13, 11, 5, 12,
            1, 9, 11, 10, 0, 8, 12, 4, 13, 3, 7, 15, 14, 5, 6, 2,
            4, 0, 5, 9, 7, 12, 2, 10, 14, 1, 3, 8, 11, 6, 15, 13};
    int[] rs = new int[]{5, 14, 7, 0, 9, 2, 11, 4, 13, 6, 15, 8, 1, 10, 3, 12,
            6, 11, 3, 7, 0, 13, 5, 10, 14, 15, 8, 12, 4, 9, 1, 2,
            15, 5, 1, 3, 7, 14, 6, 9, 11, 8, 12, 2, 10, 0, 4, 13,
            8, 6, 4, 1, 3, 11, 15, 0, 5, 12, 2, 13, 9, 7, 10, 14,
            12, 15, 10, 4, 1, 5, 8, 7, 6, 2, 13, 14, 0, 3, 9, 11};
    int[] s = new int[]{11, 14, 15, 12, 5, 8, 7, 9, 11, 13, 14, 15, 6, 7, 9, 8,
            7, 6, 8, 13, 11, 9, 7, 15, 7, 12, 15, 9, 11, 7, 13, 12,
            11, 13, 6, 7, 14, 9, 13, 15, 14, 8, 13, 6, 5, 12, 7, 5,
            11, 12, 14, 15, 14, 15, 9, 8, 9, 14, 5, 6, 8, 6, 5, 12,
            9, 15, 5, 11, 6, 8, 13, 12, 5, 12, 13, 14, 11, 8, 5, 6};
    int[] ss = new int[]{8, 9, 9, 11, 13, 15, 15, 5, 7, 7, 8, 11, 14, 14, 12, 6,
            9, 13, 15, 7, 12, 8, 9, 11, 7, 7, 12, 7, 6, 15, 13, 11,
            9, 7, 15, 11, 8, 6, 6, 14, 12, 13, 5, 14, 13, 13, 7, 5,
            15, 5, 8, 11, 14, 14, 6, 14, 6, 9, 12, 9, 12, 5, 15, 8,
            8, 5, 12, 9, 12, 5, 14, 6, 8, 13, 6, 5, 15, 13, 11, 11};
    long size;
    int countT;
    int[][] all;
    public byte[] firstSecond(byte[] s){
        size = s.length * 8;
        int rightsize;
        int a = (s.length + 1) % 64;
        if(a != 56){
            rightsize = s.length + 1 + (56 - a);
        }
        else {
            rightsize = a;
        }
        byte[] n = new byte[rightsize + 8];
        for(int i = 0; i < rightsize; i++){
            if(i < s.length) {
                n[i] = s[i];
            }
            else {
                if (i == s.length) {
                    n[i] = (byte) 0x80;
                } else {
                    n[i] = (byte) 0x00;
                }
            }
        }
        for(int i = 0; i < 8; i++) {
            n[rightsize + i] = (byte) (size >> (8 * i));
        }
        return n;
    }
    public void part(byte[] a) {
        countT = a.length / 64;
        all = new int[countT][16];
        int k = 0;
        for (int i = 0; i < countT; i++) {
            for (int j = 0; j < 16; j++) {
                all[i][j] = ((int) a[k]) & 0xff | (((int) a[k + 1]) & 0xff) << 8
                        | (((int) a[k + 2]) & 0xff) << 16 | (((int) a[k + 3]) & 0xff) << 24;
                k += 4;
            }
        }
    }
    public int functionF(int j, int x, int y, int z){
        if(0 <= j && j <= 15){
            return x ^ y ^ z;
        }
        if(16 <= j && j <= 31){
            return (x & y) | ((~x) & z);
        }
        if(32 <= j && j <= 47) {
            return (x | (~y)) ^ z;
        }
        if(48 <= j && j <= 63) {
            return (x & z) | (y & (~z));
        }
        if(64 <= j && j <= 79) {
            return x ^ (y | (~z));
        }
        return -1;
    }
    public int getK(int j){
        if(0 <= j && j <= 15){
            return k0;
        }
        if(16 <= j && j <= 31){
            return k1;
        }
        if(32 <= j && j <= 47) {
            return k2;
        }
        if(48 <= j && j <= 63) {
            return k3;
        }
        if(64 <= j && j <= 79) {
            return k4;
        }
        return -1;
    }
    public int getKs(int j){
        if(0 <= j && j <= 15){
            return k0s;
        }
        if(16 <= j && j <= 31){
            return k1s;
        }
        if(32 <= j && j <= 47) {
            return k2s;
        }
        if(48 <= j && j <= 63) {
            return k3s;
        }
        if(64 <= j && j <= 79) {
            return k4s;
        }
        return -1;
    }
    public byte[] algorithm(String string){
        byte buffer[] = string.getBytes();
        part(firstSecond(buffer));
        int A;
        int B;
        int C;
        int D;
        int E;
        int As;
        int Bs;
        int Cs;
        int Ds;
        int Es;
        int T;
        for(int i = 0; i < countT; i++){
            A = h0;
            B = h1;
            C = h2;
            D = h3;
            E = h4;
            As = h0;
            Bs = h1;
            Cs = h2;
            Ds = h3;
            Es = h4;
            for(int j = 0; j < 80; j++){
                int t = A + functionF(j, B, C, D) + all[i][r[j]] + getK(j);
                T = Integer.rotateLeft(t, s[j]) + E;
                A = E;
                E = D;
                D = Integer.rotateLeft(C, 10);
                C = B;
                B = T;
                t = As + functionF(79 - j, Bs, Cs, Ds) + all[i][rs[j]] + getKs(j);
                T = Integer.rotateLeft(t, ss[j]) + Es;
                As = Es;
                Es = Ds;
                Ds = Integer.rotateLeft(Cs, 10);
                Cs = Bs;
                Bs = T;
            }
            T = h1 + C + Ds;
            h1 = h2 + D + Es;
            h2 = h3 + E + As;
            h3 = h4 + A + Bs;
            h4 = h0 + B + Cs;
            h0 = T;
        }
        byte[] hashCode = getByteArray(h0, h1, h2, h3, h4);
        //System.out.println("hash: " + printByteArray(hashCode));
        return hashCode;
    }
    private byte[] getByteArray(int... h) {
        byte[] hashCode = new byte[h.length * 4];
        for (int i = 0; i < h.length; i++) {
            for(int j = 0; j < 4; j++){
                hashCode[4 * i + j] = (byte) (h[i] >> 8 * j);
            }
        }
        return hashCode;
    }
    public static String printByteArray(byte[] array) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            String value = Integer.toHexString(((int) array[i]) & 0xff);
            value = (value.length() == 1) ? ("0" + value) : value;
            sb.append(value);
        }
        return sb.toString();
    }
    public void q(String s){
        int n = 10000;
        int countCycles = 1000;
        byte[] hashY = algorithm(s);
        int y0 = hashY[0];
        byte[] hashes;
        int[] yi = new int[n];
        double s4 = 0;
        double s6 = 0;
        double s8 = 0;
        double s10 = 0;
        double s12 = 0;
        double s14 = 0;
        double s16 = 0;
        double k4 = 0;
        double k6 = 0;
        double k8 = 0;
        double k10 = 0;
        double k12 = 0;
        double k14 = 0;
        double k16 = 0;
        for(int l = 0; l < countCycles; l++) {
            System.out.println(l);
            String[] words = generateRandomWords(n);
            int k = 0;
            y0 = (((int) hashY[0]) & 0xf0) >> 4;
            for (int i = 0; i < n; i++) {
                hashes = algorithm(words[i]);
                yi[i] = (((int) hashes[0]) & 0xf0) >> 4;
            }
            k = 0;
            for (int i = 0; i < n; i++) {
                if (yi[i] == y0) {
                    break;
                } else {
                    k++;
                }
            }
            s4 = s4 + k;
            //System.out.println("4bit второй прообраз: " + k);
            k = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if ((i != j) && (yi[i] == yi[j])) {
                        break;
                    }
                    k++;
                }
            }
            k4 = k4 + k;
            //System.out.println("4bit коллизия: " + k);
            y0 = (((int) hashY[0]) & 0xfc) >> 2;
            for (int i = 0; i < n; i++) {
                hashes = algorithm(words[i]);
                yi[i] = (((int) hashes[0]) & 0xfc) >> 2;
            }
            k = 0;
            for (int i = 0; i < n; i++) {
                if (yi[i] == y0) {
                    break;
                } else {
                    k++;
                }
            }
            s6 = s6 + k;
            //System.out.println("6bit второй прообраз: " + k);
            k = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if ((i != j) && (yi[i] == yi[j])) {
                        break;
                    }
                    k++;
                }
            }
            k6 = k6 + k;
            //System.out.println("6bit коллизия: " + k);
            y0 = hashY[0];
            for (int i = 0; i < n; i++) {
                yi[i] = algorithm(words[i])[0];
            }
            k = 0;
            for (int i = 0; i < n; i++) {
                if (yi[i] == y0) {
                    break;
                } else {
                    k++;
                }
            }
            s8 = s8 + k;
            //System.out.println("8bit второй прообраз: " + k);
            k = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if ((i != j) && (yi[i] == yi[j])) {
                        break;
                    }
                    k++;
                }
            }
            k8 = k8 + k;
            //System.out.println("8bit коллизия: " + k);
            y0 = (((int) hashY[0]) & 0xff) << 2 | (((int) hashY[1]) & 0xc0) >> 6;
            for (int i = 0; i < n; i++) {
                hashes = algorithm(words[i]);
                yi[i] = (((int) hashes[0]) & 0xff) << 2 | (((int) hashes[1]) & 0xc0) >> 6;
            }
            k = 0;
            for (int i = 0; i < n; i++) {
                if (yi[i] == y0) {
                    break;
                } else {
                    k++;
                }
            }
            s10 = s10 + k;
            //System.out.println("10bit второй прообраз: " + k);
            k = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if ((i != j) && (yi[i] == yi[j])) {
                        break;
                    }
                    k++;
                }
            }
            k10 = k10 + k;
            //System.out.println("10bit коллизия: " + k);
            y0 = (((int) hashY[0]) & 0xff) << 4 | (((int) hashY[1]) & 0xf0) >> 4;
            for (int i = 0; i < n; i++) {
                hashes = algorithm(words[i]);
                yi[i] = (((int) hashes[0]) & 0xff) << 4 | (((int) hashes[1]) & 0xf0) >> 4;
            }
            k = 0;
            for (int i = 0; i < n; i++) {
                if (yi[i] == y0) {
                    break;
                } else {
                    k++;
                }
            }
            s12 = s12 + k;
            //System.out.println("12bit второй прообраз: " + k);
            k = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if ((i != j) && (yi[i] == yi[j])) {
                        break;
                    }
                    k++;
                }
            }
            k12 = k12 + k;
            //System.out.println("12bit коллизия: " + k);
            /*
            y0 = (((int) hashY[0]) & 0xff) << 6 | (((int) hashY[1]) & 0xfc) >> 2;
            for (int i = 0; i < n; i++) {
                hashes = algorithm(words[i]);
                yi[i] = (((int) hashes[0]) & 0xff) << 6 | (((int) hashes[1]) & 0xfc) >> 2;
            }
            k = 0;
            for (int i = 0; i < n; i++) {
                if (yi[i] == y0) {
                    break;
                } else {
                    k++;
                }
            }
            s14 = s14 + k;
            System.out.println("14bit второй прообраз: " + k);
            k = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if ((i != j) && (yi[i] == yi[j])) {
                        break;
                    }
                    k++;
                }
            }
            k14 = k14 + k;
            System.out.println("14bit коллизия: " + k);
            y0 = (((int) hashY[0]) & 0xff) << 8 | (((int) hashY[1]) & 0xfc);
            for (int i = 0; i < n; i++) {
                hashes = algorithm(words[i]);
                yi[i] = (((int) hashes[0]) & 0xff) << 8 | (((int) hashes[1]) & 0xfc);
            }
            k = 0;
            for (int i = 0; i < n; i++) {
                if (yi[i] == y0) {
                    break;
                } else {
                    k++;
                }
            }
            s16 = s16 + k;
            System.out.println("16bit второй прообраз: " + k);
            k = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if ((i != j) && (yi[i] == yi[j])) {
                        break;
                    }
                    k++;
                }
            }
            k16 = k16 + k;
            System.out.println("16bit коллизия: " + k);
            System.out.println("l " + l);
            */
        }
        s4 = s4 / countCycles;
        s6 = s6 / countCycles;
        s8 = s8 / countCycles;
        s10 = s10 / countCycles;
        s12 = s12 / countCycles;
        k4 = k4 / countCycles;
        k6 = k6 / countCycles;
        k8 = k8 / countCycles;
        k10 = k10 / countCycles;
        k12 = k12 / countCycles;
        System.out.println("4bit второй прообраз: " + s4);
        System.out.println("6bit второй прообраз: " + s6);
        System.out.println("8bit второй прообраз: " + s8);
        System.out.println("10bit второй прообраз: " + s10);
        System.out.println("12bit второй прообраз: " + s12);
        //System.out.println("14bit второй прообраз: " + s14 / countCycles);
        //System.out.println("16bit второй прообраз: " + s16 / countCycles);
        System.out.println("4bit коллизия: " + k4);
        System.out.println("6bit коллизия: " + k6);
        System.out.println("8bit коллизия: " + k8);
        System.out.println("10bit коллизия: " + k10);
        System.out.println("12bit коллизия: " + k12);
        //System.out.println("14bit коллизия: " + k14 / countCycles);
        //System.out.println("16bit коллизия: " + k16 / countCycles);
        try {
            FileWriter file = new FileWriter("прообраз.txt");
            StringBuilder str = new StringBuilder();
            str.append(40 + " " + s4 + "\n");
            str.append(60 + " " + s6 + "\n");
            str.append(80 + " " + s8 + "\n");
            str.append(100 + " " + s10 + "\n");
            str.append(120 + " " + s12 + "\n");
            file.write(str.toString());
            file.close();
            file = new FileWriter("коллизия.txt");
            str = new StringBuilder();
            str.append(4 + " " + k4 + "\n");
            str.append(6 + " " + k6 + "\n");
            str.append(8 + " " + k8 + "\n");
            str.append(10 + " " + k10 + "\n");
            str.append(12 + " " + k12 + "\n");
            file.write(str.toString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String[] generateRandomWords(int numberOfWords){
        String[] randomStrings = new String[numberOfWords];
        Random random = new Random();
        for(int i = 0; i < numberOfWords; i++) {
            char[] word = new char[random.nextInt(5) + 30]; // words of length 3 through 10. (1 and 2 letter words are boring.)
            for(int j = 0; j < word.length; j++) {
                word[j] = (char)('a' + random.nextInt(40));
            }
            randomStrings[i] = new String(word);
            //System.out.println(randomStrings[i]);
        }
        return randomStrings;
    }

    public static void main(String[] args) {
        Ripemd160 r = new Ripemd160();
        Scanner in = new Scanner(System.in);
        System.out.print("Input a string: ");
        String str = in.nextLine();
        System.out.println("hash: " + printByteArray(r.algorithm(str)));
        //r.q("njoikmnono");
        //r.q("0000");
        //r.q("qwertyuiopasdfgh");
    }
}
