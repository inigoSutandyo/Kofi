package edu.bluejack22_1.kofi.comparator;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import edu.bluejack22_1.kofi.model.CoffeeShop;

public class RatingComparator implements CoffeeShopComparator{

    Comparator<CoffeeShop> comparator = new Comparator<CoffeeShop>() {
        @Override
        public int compare(CoffeeShop c1, CoffeeShop c2) {
            Double rating1 = c1.getAverageRating();
            Double rating2 = c2.getAverageRating();
            Timestamp t1 = c1.getCreatedAt();
            Timestamp t2 = c2.getCreatedAt();
            int compare = Double.compare(rating2, rating1);
            // DESC
            return compare == 0 ? t2.toDate().compareTo(t1.toDate()) : compare;
        }
    };

    @Override
    public void sortCoffeeShop(ArrayList<CoffeeShop> coffeeShops) {
        Collections.sort(coffeeShops,comparator);
    }
}
