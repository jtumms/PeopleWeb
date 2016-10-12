package com.company;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

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
                    int prevSet = Integer.valueOf(offset) - 20;
                    boolean isFirst = false;
                    boolean isLast = false;
                    boolean theRest = true;
                    if (Integer.valueOf(offset) == 20){
                        isFirst = true;ArrayList<Person> ar = new ArrayList<Person>();
                        theRest = false;
                        isLast = false;
                    }

                    HashMap m = new HashMap();
                    ArrayList<Person> al2 = new ArrayList<Person>();
                    for (Person person : people){
                        if (person.id >= Integer.valueOf(offset) - 19  && person.id <= Integer.valueOf(offset)){
                            al2.add(person);
                        }
                    }
                    if (al2.size() < 20){
                        isLast = true;
                        theRest = false;
                        isFirst = false;
                    }


                    m.put("al2", al2);
                    m.put("offset", offset);
                    m.put("nextSet", nextSet);
                    m.put("prevSet", prevSet);
                    m.put("isFirst", isFirst);
                    m.put("isLast", isFirst);
                    m.put("theRest", theRest);


                    return new ModelAndView(m, "home.html");
                },
                new MustacheTemplateEngine()
        );
        Spark.get(
                "/person",
                (request, response) -> {
                    int id = Integer.valueOf(request.queryParams("id"));
                    Person p = people.get(id);
                    return new ModelAndView(p, "person.html");
                },
                new MustacheTemplateEngine()

        );

    }

}
