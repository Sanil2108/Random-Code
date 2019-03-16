
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.*;
import java.math.*;


public class RSA{

    private static final long MIN = 50;
    private static final long MAX = 200;

    private static boolean isPrime(long n){
        for(long i= (long)Math.sqrt(n);i<n;i++){
            if(n%i==0){
                return false;
            }
        }
        return true;
    }

    private static long findRandomPrime(){
        long random = ThreadLocalRandom.current().nextLong(MAX - MIN) + MIN;
        while(!isPrime(random)){
            random = ThreadLocalRandom.current().nextLong(MAX - MIN) + MIN;
        }
        return random;
    }

    private static long gcd(long a, long b){
        long min = (a>b)?b:a;
        long max = (a>b)?a:b;

        long q;
        long remainder;

        while(true){
            q = (long)(max/min);
            remainder = max % min;

            if(remainder == 0){
                break;
            }

            max = min;
            min = remainder;
        }

        return min;
    }

    private static long lcm(long a, long b){
        return (a*b)/gcd(a, b);
    }

    private static long modularMultiplicativeInverse(long x, long n){
        long min = x;
        long max = n;
        long p0 = 0;
        long p1 = 1;
        long q0 = -1;
        long q1 = -1;
        int count = 0;

        long q;
        long remainder;

        while(true){
            count++;

            q = (int)(max/min);
            remainder = max % min;

            if(count == 1){
                q0 = q;
            }

            if(count == 2){
                q1 = q;
            }

            if(count > 2){
                long temp_p = (p0 - p1 * q0) % n;
                p0 = p1;
                p1 = temp_p;

                q0 = q1;
                q1 = q;
            }

            if(remainder == 0){
                long temp_p = (p0 - p1 * q0) % n;
                p0 = p1;
                p1 = temp_p;

                q0 = q1;
                q1 = q;

                break;
            }

            max = min;
            min = remainder;
        }

        return p1;
    }

    private static Key RSA(){
        long p = findRandomPrime();
        long q;
        while(true){
            q = findRandomPrime();
            if(q!=p){
                break;
            }
        }
        long n = p*q;
        long lambda = lcm(p-1, q-1);
        
        long e;
        while(true){
            e = ThreadLocalRandom.current().nextLong(lambda - 2) + 2;
            if(gcd(e, lambda) == 1){
                break;
            }
        }
        
        long d = modularMultiplicativeInverse(e, lambda);

        
        Key key = new Key();
        key.setPrivateKeyD(d);
        key.setPublicKeyE(e);
        key.setPublicKeyN(n);
        
        return key;
    }

    public static String encrypt(String s, Key key){
        char[] charArray = s.toCharArray();
        String encrypted = "";
        for(char c:charArray){
            int m = (int)c;
            long encryptedM = (int)Math.pow(m, key.publicKeyE) % key.getPublicKeyN();

            encrypted += Base64.getEncoder().encodeToString(BigInteger.valueOf(encryptedM).toString().getBytes()) + "_";
        }

        return encrypted;
    }

    public static String decrypt(String s, Key key){
        char[] chars = new String(Base64.getDecoder().decode(s)).toCharArray();
        String decrypted = "";
        for(char c:chars){
            int m = (int)c;

            if(c == '_'){
                continue;
            }

            int mDecrypted = (int)( (int)Math.pow(m, key.privateKeyD) % key.getPublicKeyN());
            decrypted += (char)mDecrypted;
        }

        return decrypted;
    }


    public static class Key{

        private long privateKeyD;
        private long publicKeyN;
        private long publicKeyE;

        public Key(){}

        public long getPrivateKeyD() {
            return privateKeyD;
        }

        public void setPrivateKeyD(long privateKeyD) {
            this.privateKeyD = privateKeyD;
        }

        public long getPublicKeyN() {
            return publicKeyN;
        }

        public void setPublicKeyN(long publicKeyN) {
            this.publicKeyN = publicKeyN;
        }

        public long getPublicKeyE() {
            return publicKeyE;
        }

        public void setPublicKeyE(long publicKeyE) {
            this.publicKeyE = publicKeyE;
        }
    }

    public static void main(String[] args){
        String toCode = "Hello, world";
        Key k = RSA.RSA();
        String coded = RSA.encrypt(toCode, k);
        System.out.println(toCode);
        System.out.println(coded);
        System.out.println(RSA.decrypt(coded, k));
        // System.out.println(k.getPublicKeyE() +", "+k.getPublicKeyN()+", "+k.getPrivateKeyD());
    }
}