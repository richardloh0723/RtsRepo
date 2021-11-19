package com.rts.week12.streamsLecture;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class streams {
    public static void main(String[] args) {
        StudentStreamExample.example();
    }
}

class laptop {
    int id;
    String name;
    float price;

    public laptop(int id, String name, float price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}

class StreamExample {

    public static void example() {
        int price = 5000;
        List<laptop> laptopList = new ArrayList<laptop>();

        // Adding laptops
        laptopList.add(new laptop(1,"HP Laptop", 25000f));
        laptopList.add(new laptop(2,"Dell Laptop", 30000f));
        laptopList.add(new laptop(3,"Lenovo Laptop", 28000f));
        laptopList.add(new laptop(4,"Razer Laptop", 28000f));
        laptopList.add(new laptop(5,"Apple Laptop", 90000f));

        laptopList.add(new laptop(6,"Acer Laptop", 33000f));
        laptopList.add(new laptop(7,"Apple Macbook Pro Laptop", 29000f));
        laptopList.add(new laptop(8,"Razer Blade Laptop", 41000f));
        laptopList.add(new laptop(9,"Apple MacBook Air Laptop", 96000f));

        List<String> laptopNameList2 = laptopList.stream().filter(n->n.price > price) //filtering data
                .map(n->n.name + n.price) // fetching price
                .collect(Collectors.toList()); // collect the mapped
        for (String name : laptopNameList2)
            System.out.println(name);

        // as were filtering , getting object
        // objects that made that criteria will directly map the price into String
        // in parallelStream
        // (will start to happen concurrently.)
        List<String> laptopNameList3 = laptopList.parallelStream()
                .filter(n->n.price > price) //filtering data
                .map(n->n.name + n.price) // fetching price
                .collect(Collectors.toList()); // collect the mapped
        for(String name : laptopNameList3)
            System.out.println(name);
    }
}

class Student {
    String surname;
    int id;
    String nationality;
    String course;
    int year;

    public Student(String surname, int id, String nationality, String course, int year) {
        this.surname = surname;
        this.id = id;
        this.nationality = nationality;
        this.course = course;
        this.year = year;
    }


}

class StudentStreamExample {
    public static void example() {
        List<Student> studentList = new ArrayList<>();

        // adding students
        studentList.add(new Student("Gan",1,"China","CSDA",2019));
        studentList.add(new Student("Xue",2,"Singapore","CS",2020));
        studentList.add(new Student("Hua",3,"Malaysia","IoT",2021));
        studentList.add(new Student("Piao",4,"Indonesia","IT",2008));
        studentList.add(new Student("Ash",5,"Japan","BIS",2003));

        // pre-processing (years)
        List<String> studentNameList = studentList.stream().filter(s -> s.year >= 2019 && s.course.equals("IoT"))
                .map(s -> "My name is " + s.surname + ". I come from " + s.nationality
                + ". I study " + s.course + " in year " + s.year)
                .collect(Collectors.toList());

        for(String introduction : studentNameList)
            System.out.println(introduction);


    }
}