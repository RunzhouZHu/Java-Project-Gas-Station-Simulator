import eduni.distributions.Normal;


public class Main {
    public static void main(String[] args) {
        Normal source = new Normal(1.0, 2);
        for (int i = 0; i < 10; i++) {
            System.out.println("sample " + i + "=" + source.sample());
        }
    }
}