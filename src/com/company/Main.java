package com.company;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    static ArrayList<Person> people = new ArrayList<>();





    public static void main(String[] args) throws FileNotFoundException {
        File f = new File("people.csv");
        Scanner fileScanner = new Scanner(f);
        fileScanner.nextLine();                                 //<---force ignore on first line of file
        while (fileScanner.hasNext()) {
            String fileContent = fileScanner.nextLine();
            String[] fileSplit = fileContent.split("\\,");
            Person person = new Person(Integer.valueOf(fileSplit[0]), fileSplit[1], fileSplit[2], fileSplit[3], fileSplit[4], fileSplit[5]);
            people.add(person);

        }

        Spark.get(
                "/",
                (request, response) -> {
                    String offset = request.queryParams("offset");
                    if (offset == null){
                        offset = "20";
                    }
                    int nextSet = Integer.valueOf(offset) + 20;

                    HashMap m = new HashMap();
                    ArrayList<Person> al2 = new ArrayList<Person>();
                    for (Person person : people){
                        if (person.id >= Integer.valueOf(offset) - 19  && person.id <= Integer.valueOf(offset)){
                            al2.add(person);
                        }
                    }


                    m.put("al2", al2);
                    m.put("offset", offset);
                    m.put("nextSet", nextSet);


                    return new ModelAndView(m, "home.html");
                },
                new MustacheTemplateEngine()
        );


    }

}
