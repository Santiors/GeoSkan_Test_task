package TestData;


import Models.Employee;
import Controllers.InputGeneratorController;

public class Employees {

    public static Employee getEmployeeRandomData(){
        return Employee.builder()
                .id(InputGeneratorController.getRandomIntegerNumber())
                .emailId(InputGeneratorController.getLatinStrWithoutSpaces(5)+"@gmail.com")
                .firstName(InputGeneratorController.getLatinStrWithoutSpaces(5))
                .lastName(InputGeneratorController.getLatinStrWithoutSpaces(5))
                .username(InputGeneratorController.getLatinStrWithoutSpaces(5))
                .build();
    }

}
