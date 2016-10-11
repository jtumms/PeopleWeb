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
    //static HashMap<Integer, Person> peopleHash = new HashMap();



    public static void main(String[] args) throws FileNotFoundException {
        File f = new File("people.csv");
        Scanner fileScanner = new Scanner(f);
        fileScanner.nextLine();                                 //<---force ignore on first line of file
        while (fileScanner.hasNext()) {
            String fileContent = fileScanner.nextLine();
            String[] fileSplit = fileContent.split("\\,");
            Person person = new Person(Integer.valueOf(fileSplit[0]), fileSplit[1], fileSplit[2], fileSplit[3], fileSplit[4], fileSplit[5]);
            people.add(person);
            //peopleHash.put(person.id, person);


        }

        Spark.get(
                "/",
                (request, response) -> {
                    String idWorking = request.queryParams("id");
                    HashMap m = new HashMap();
                    m.put("people", people);
                    return new ModelAndView(m, "home.html");
                },
                new MustacheTemplateEngine()
        );
    }

}
