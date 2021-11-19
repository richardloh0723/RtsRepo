package com.rts.week12.labhomework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) {
        streamQuestions.questions();
    }
}

class Car {
    int id;
    String brand;
    String model;
    String category;
    int price;

    public Car(int id, String brand, String model, String category, int price) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.category = category;
        this.price = price;
    }
}

class streamQuestions {
    public static void questions() {
        List<Car> carList = new ArrayList<Car>();

        //Adding Car
        carList.add(new Car(1,"Ford" ,"Everest" ,"SUV" ,228000));
        carList.add(new Car(2,"Toyota" ,"Avalon" ,"Sedan" ,420000));
        carList.add(new Car(3,"Honda" ,"Civic" ,"Sedan" ,530000));
        carList.add(new Car(4,"Honda" ,"CR-V" ,"SUV" ,430000));
        carList.add(new Car(5,"Suzuki" ,"Carry" ,"Minivan" ,540000));
        carList.add(new Car(6,"Ford" ,"Mustang" ,"Sport" ,762000));
        carList.add(new Car(7,"Toyota" ,"Runner" ,"SUV" ,320000));
        // 1.	Display all the distinct car brands available.
        //List<Float> LatopPriceList2=LatopList.stream()

       List<String> carBrands =  carList.stream()
                .filter(distinctByKey(c->c.brand))
                .map(c->c.brand)
                .collect(Collectors.toList());

       for(String brand : carBrands)
           System.out.println(brand);

    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

}
