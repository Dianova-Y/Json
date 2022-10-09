import java.io.*;
import com.google.gson.*;

public class Basket {
    private final String[] products;
    private final int[] prices;
    private final int[] numb;
    private int sum = 0;

    public Basket(String[] products, int[] prices) {
        this.products = products;
        this.prices = prices;
        this.numb = new int[products.length];
    }

    public Basket(String[] products, int[] prices, int[] numb) {
        this.products = products;
        this.prices = prices;
        this.numb = numb;
    }

    public void addToCart(int productNum, int numb) {
        this.numb[productNum] += numb;
    }

    public void printCart() {
        System.out.println("В вашей корзине:");
        for (int i = 0; i < products.length; i++) {
            if (numb[i] > 0) {
                System.out.println(products[i] + " " + numb[i] + " шт, " + prices[i] + " руб/шт. Итого: " + (numb[i] * prices[i]) + " руб");
                sum += (numb[i] * prices[i]);
            }
        }
        System.out.println("Сумма всей покупки: " + sum + " руб");
    }

    public void saveTxt(File file) throws IOException {
        try (PrintWriter out = new PrintWriter(file);) {
            for (int i = 0; i < products.length; i++) {
                if (numb[i] > 0) {
                    out.write(products[i] + " " + prices[i] + " " + numb[i] + "\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Basket loadFromTxtFile(File file) {
        Basket basket = null;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            int size = 5;
            String[] products = new String[size];
            int[] prices = new int[size];
            int[] numb = new int[size];
            String s;
            int i = 0;
            while ((s = br.readLine()) != null) {
                String[] parts = s.split(" ");
                products[i] = parts[0];
                prices[i] = Integer.parseInt(parts[1]);
                numb[i] = Integer.parseInt(parts[2]);
                i++;
            }
            basket = new Basket(products, prices, numb);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return basket;
    }



    public void saveJson(File file) {
        try (FileWriter writer = new FileWriter(file)) {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.setPrettyPrinting().create();
            writer.write(gson.toJson(this, Basket.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static Basket loadFromJson(File file) throws FileNotFoundException{
        Gson gson = new Gson();
        FileReader reader = new FileReader(file);
        return gson.fromJson(reader, Basket.class);
    }
}
