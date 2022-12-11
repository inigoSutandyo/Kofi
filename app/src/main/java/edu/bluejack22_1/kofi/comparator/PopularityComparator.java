package edu.bluejack22_1.kofi.comparator;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import edu.bluejack22_1.kofi.model.CoffeeShop;

public class PopularityComparator implements CoffeeShopComparator{

    Comparator<CoffeeShop> comparator = new Comparator<CoffeeShop>() {
        @Override
        public int compare(CoffeeShop c1, CoffeeShop c2) {
            int r1 = c1.getUserFavorites().size();
            int r2 = c2.getUserFavorites().size();
            Timestamp t1 = c1.getCreatedAt();
            Timestamp t2 = c2.getCreatedAt();
            int compare = r2-r1;
            // DESC
            return compare == 0 ? t2.toDate().compareTo(t1.toDate()) : compare;
        }
    };

    @Override
    public void sortCoffeeShop(ArrayList<CoffeeShop> coffeeShops) {
        Collections.sort(coffeeShops,comparator);
    }
}
