package com.roman;

import com.roman.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class TaskManager implements Serializable {
    @Serial
    private static final long serialVersionUID = 6433772720969614495L;
    private static Map<User, List<Task>> users = new HashMap<>();

    public static void main(String[] args) throws ClassNotFoundException, IOException {
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("Args are empty!");
        }

        deserialize();

        String functionName = args[0];
        switch (functionName) {
            case "-createUser" -> {
                String firstName = "";
                String lastName = "";
                String userName = "";
                String[] parameter;
                for (int i = 1; i < args.length; i++) {
                    parameter = args[i].split("=[\"']");
                    String value = parameter[1].substring(0, parameter[1].length() - 1);
                    switch (parameter[0]) {
                        case "-fn" -> firstName = value;
                        case "-ln" -> lastName = value;
                        case "-un" -> userName = value;
                        default -> throw new IllegalArgumentException("Illegal parameter: " + parameter[0]);
                    }
                }
                List<Task> existed = users.putIfAbsent(createUser(firstName, lastName, userName), new ArrayList<>());
                System.out.println(existed == null ? "User was added successfully." : "User is already in map.");
            }
            case "-showAllUsers" -> {
                showAllUsers();
            }
            case "-addTask" -> {
                String userName = "";
                String taskTitle = "";
                String taskDescription = "";
                String[] parameter;
                for (int i = 1; i < args.length; i++) {
                    parameter = args[i].split("=[\"']");
                    String value = parameter[1].substring(0, parameter[1].length() - 1);
                    switch (parameter[0]) {
                        case "-un" -> userName = value;
                        case "-tt" -> taskTitle = value;
                        case "-td" -> taskDescription = value;
                        default -> throw new IllegalArgumentException("Illegal parameter: " + parameter[0]);
                    }
                }
                addTask(userName, taskTitle, taskDescription);
            }
            case "-showTasks" -> {
                String[] parameter = args[1].split("=[\"']");
                if (parameter[0].equals("-un")) {
                    showTasks(parameter[1].substring(0, parameter[1].length() - 1));
                } else {
                    throw new IllegalStateException("Illegal parameter: " + parameter[0]);
                }
            }
            case "-deleteUser" -> {
                String[] parameter = args[1].split("=[\"']");
                if (parameter[0].equals("-un")) {
                    deleteUser(parameter[1].substring(0, parameter[1].length() - 1));
                } else {
                    throw new IllegalStateException("Illegal parameter: " + parameter[0]);
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + functionName);
        }

        serialize();
    }

    private static void serialize() throws IOException {
        boolean isSerialized = false;
        while (!isSerialized) {
            try (FileOutputStream fos = new FileOutputStream("taskManager.ser");
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(fos)) {
                objectOutputStream.writeObject(users);
                System.out.println("Serialization successful.");
                isSerialized = true;
            } catch (FileNotFoundException e) {
                Files.createFile(Path.of("taskManager.ser"));
            }
        }
    }

    private static void deserialize() throws ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream("taskManager.ser");
             ObjectInputStream objectInputStream = new ObjectInputStream(fis)) {
            users = (Map<User, List<Task>>) objectInputStream.readObject();
            System.out.println("Deserialization successful.");
        } catch (IOException e) {
            System.out.println("File with data is not found or is unavailable. We'll create a new one.");
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Program cannot deserialize an object.", e);
        }
    }

    private static User createUser(String firstName, String lastName, String userName) {
        if (userName == null || firstName == null || lastName == null) {
            throw new IllegalArgumentException("Parameters are empty: " + userName + ", " + firstName + ", " + lastName);
        }
        return new User(firstName, lastName, userName);
    }
    private static void showAllUsers() {
        users.forEach(
                (a, b) -> System.out.println(a.getFirstName() + ", " + a.getLastName() + ", " + b.size()
                ));
    }
    private static void addTask(String userName, String taskTitle, String taskDescription) {
        if (userName == null || taskTitle == null || taskDescription == null) {
            throw new IllegalArgumentException("Parameters are empty: " + userName + ", " + taskTitle + ", " + taskDescription);
        }
        List<Task> userTasks = null;
        for (var entry : users.entrySet()) {
            if (entry.getKey().getUserName().equals(userName)) {
                userTasks = entry.getValue();
                break;
            }
        }
        if (userTasks == null) {
            throw new NullPointerException("Map doesn't contain this user: " + userName);
        }
        userTasks.add(new Task(taskTitle, taskDescription));
        System.out.println("Task was added successfully.");
    }
    private static void showTasks(String userName) {
        if (userName == null) {
            throw new IllegalArgumentException("UserName is empty!");
        }
        List<Task> userTasks = null;
        for (var entry : users.entrySet()) {
            if (entry.getKey().getUserName().equals(userName)) {
                userTasks = entry.getValue();
                break;
            }
        }
        if (userTasks == null) {
            throw new NullPointerException("Map doesn't contain this user: " + userName);
        }
        userTasks.forEach(a -> {
            System.out.println(userName + " with task " + a.getTitle());
            System.out.println(a.getDescription());
        });
    }
    private static void deleteUser(String userName) {
        if (userName == null) {
            throw new IllegalArgumentException("UserName is empty.");
        }
        boolean removed = users.keySet().removeIf(key -> key.getUserName().equals(userName));
        System.out.println(removed ? "User was removed successfully." : "User wasn't removed.");
    }
}
