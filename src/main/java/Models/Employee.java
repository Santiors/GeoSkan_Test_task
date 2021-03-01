package Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@JsonDeserialize(builder = Employee.EmployeeBuilder.class)
@Builder
@Data
public class Employee {
    @JsonIgnore
    private Integer id;
    private String emailId;
    private String firstName;
    private String lastName;
    private String username;


    public static class EmployeeBuilder{

    }
}

