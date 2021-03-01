package Controllers;

import Auth.Auth;
import Paths.EmployeeSitePaths;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;

import static TestData.Employees.getEmployeeRandomData;

public class EmployeeController {

    public static JSONObject getRequestBody(){
        JSONObject requestBody = new JSONObject();
        requestBody.put("emailId",getEmployeeRandomData().getEmailId());
        requestBody.put("firstName",getEmployeeRandomData().getFirstName());
        requestBody.put("lastName",getEmployeeRandomData().getLastName());
        requestBody.put("username",getEmployeeRandomData().getUsername());
        return requestBody;
    }
    public static JSONObject getRequestBodyWithInvalidFields(){
        JSONObject requestBody = new JSONObject();
        requestBody.put("email",getEmployeeRandomData().getEmailId());
        requestBody.put("firstName",getEmployeeRandomData().getFirstName());
        requestBody.put("lastName",getEmployeeRandomData().getLastName());
        requestBody.put("username",getEmployeeRandomData().getUsername());
        return requestBody;
    }
    public static JSONObject getRequestBodyForSameUserNameOrEmailId(){
        JSONObject requestBody = new JSONObject();
        requestBody.put("emailId","test");
        requestBody.put("firstName",getEmployeeRandomData().getFirstName());
        requestBody.put("lastName",getEmployeeRandomData().getLastName());
        requestBody.put("username","test");
        return requestBody;
    }

    public static Response createEmployee(String login, String password){
        JSONObject requestBody = EmployeeController.getRequestBody();
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(requestBody.toString());
        Response response = request
                .baseUri(EmployeeSitePaths.baseURI)
                .basePath(EmployeeSitePaths.employeesPath)
                .auth()
                .basic(login,password)
                .post();
        return response;
    }
    public static Response createSameEmployee(String login, String password){
        JSONObject FirstRequestBody = EmployeeController.getRequestBodyForSameUserNameOrEmailId();
        RequestSpecification FirstRequest = RestAssured.given();
        FirstRequest.header("Content-Type", "application/json");
        FirstRequest.body(FirstRequestBody.toString());
        Response FirstResponse = FirstRequest
                .baseUri(EmployeeSitePaths.baseURI)
                .basePath(EmployeeSitePaths.employeesPath)
                .auth()
                .basic(login,password)
                .post();
        int id = FirstResponse.then().extract().body().jsonPath().getInt("id");
        JSONObject SecondRequestBody = EmployeeController.getRequestBodyForSameUserNameOrEmailId();
        RequestSpecification SecondRequest = RestAssured.given();
        SecondRequest.header("Content-Type", "application/json");
        SecondRequest.body(SecondRequestBody.toString());
        Response SecondResponse = SecondRequest
                .baseUri(EmployeeSitePaths.baseURI)
                .basePath(EmployeeSitePaths.employeesPath)
                .auth()
                .basic(login,password)
                .post();
        deleteEmployee(Auth.AdminLogin,Auth.AdminPassword,id);
        return SecondResponse;
    }
    public static Response deleteEmployee(String login, String password, int id){
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        Response response = request
                .baseUri(EmployeeSitePaths.baseURI)
                .auth()
                .basic(login,password)
                .delete(EmployeeSitePaths.employeesPath + id);
        return response;
    }
    public static Response getAllEmployees(String login, String password){
        Response response = RestAssured
                .given()
                .auth()
                .basic(login,password)
                .baseUri(EmployeeSitePaths.baseURI)
                .basePath(EmployeeSitePaths.employeesPath)
                .when()
                .get();
        response.prettyPrint();
        return response;
    }

    public static Response getEmployeeById(String login, String password, int id){
        Response response = RestAssured
                .given()
                .auth()
                .basic(login,password)
                .baseUri(EmployeeSitePaths.baseURI)
                .basePath(EmployeeSitePaths.employeesPath+id)
                .when()
                .get();
        response.prettyPrint();
        return response;
    }

    public static Response updateEmployee(String login, String password, int id){
        JSONObject requestParams = EmployeeController.getRequestBody();
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(requestParams.toString());
        Response response = request
                .baseUri(EmployeeSitePaths.baseURI)
                .auth()
                .basic(login,password)
                .put(EmployeeSitePaths.employeesPath + id);
        return response;
    }
    public static Response updateSameEmployee(String login, String password) {
        JSONObject FirstRequestBody = EmployeeController.getRequestBodyForSameUserNameOrEmailId();
        RequestSpecification FirstRequest = RestAssured.given();
        FirstRequest.header("Content-Type", "application/json");
        FirstRequest.body(FirstRequestBody.toString());
        Response FirstResponse = FirstRequest
                .baseUri(EmployeeSitePaths.baseURI)
                .basePath(EmployeeSitePaths.employeesPath)
                .auth()
                .basic(login,password)
                .post();
        int id = FirstResponse.then().extract().body().jsonPath().getInt("id");
        JSONObject requestParams = EmployeeController.getRequestBodyForSameUserNameOrEmailId();
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(requestParams.toString());
        Response SecondResponse = request
                .baseUri(EmployeeSitePaths.baseURI)
                .auth()
                .basic(login,password)
                .put(EmployeeSitePaths.employeesPath + id);
        deleteEmployee(Auth.AdminLogin, Auth.AdminPassword, id);
        return SecondResponse;
    }
}
