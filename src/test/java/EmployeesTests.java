import Auth.Auth;
import Controllers.EmployeeController;
import Paths.EmployeeSitePaths;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.junit.Assert;
import org.testng.annotations.Test;


public class EmployeesTests {

    @Test(enabled = true, priority = 0 , description = "Create Employee with Admin and User role")
    public void createEmployeeTestWithAdminAndUser(){
        Response AdminResponse = EmployeeController.createEmployee(Auth.AdminLogin,Auth.AdminPassword);
        int id = AdminResponse.then().extract().body().jsonPath().getInt("id");
        int AdminStatusCode = AdminResponse.getStatusCode();
        Assert.assertEquals(200, AdminStatusCode);
        System.out.println("OK");
        Response UserResponse = EmployeeController.createEmployee(Auth.UserLogin, Auth.UserPassword);
        int UserStatusCode = UserResponse.getStatusCode();
        Assert.assertEquals(403, UserStatusCode);
        System.out.println("Accessing the resource you were trying to reach is forbidden for your role");
        EmployeeController.deleteEmployee(Auth.AdminLogin,Auth.AdminPassword, id);
    }
    @Test (enabled = true, priority = 0 , description = "Create Employee without Auth")
    void createEmployeeTestWithoutAuth(){
        JSONObject requestBody = EmployeeController.getRequestBody();
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(requestBody.toString());
        Response response = request
                .baseUri(EmployeeSitePaths.baseURI)
                .basePath(EmployeeSitePaths.employeesPath)
                .post();
        int StatusCode = response.getStatusCode();
        Assert.assertEquals(401, StatusCode);
        System.out.println("Authentication information is missing or invalid");

    }

    @Test (enabled = true, priority = 0 , description = "Create Employee with wrong Auth data")
    void createEmployeeTestWithWrongAuth(){
        Response WrongDataResponse = EmployeeController.createEmployee(Auth.InvalidLogin,Auth.InvalidPassword);
        int WrongDataStatusCode = WrongDataResponse.getStatusCode();
        Assert.assertEquals(401, WrongDataStatusCode);
        System.out.println("Authentication information is missing or invalid");
    }
    //There is no 422 error code, with invalid entity fields it responds a 500 error code
    // TODO: 01.03.2021 Create a test for 422 error code when fixed
    @Test (enabled = true, priority = 0 , description = "Create Employee without Auth")
    void createEmployeeTestWithBadEntity(){
        JSONObject requestBody = EmployeeController.getRequestBodyWithInvalidFields();
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(requestBody);
        Response response = request
                .baseUri(EmployeeSitePaths.baseURI)
                .basePath(EmployeeSitePaths.employeesPath)
                .auth()
                .basic(Auth.AdminLogin,Auth.AdminPassword)
                .post();
        int StatusCode = response.getStatusCode();
        Assert.assertEquals(500, StatusCode);
        System.out.println("Incorrect employee (entity fields are not valid)");
    }

    @Test (enabled = true, priority = 0, description = "Create Employee with username or emailId exists")
    void createEmployeeTestWithUsernameOrEmailIdExists(){
        Response response = EmployeeController.createSameEmployee(Auth.AdminLogin,Auth.AdminPassword);
        int StatusCode = response.getStatusCode();
        Assert.assertEquals(409, StatusCode);
        System.out.println("Employee with username or emailId exists");
    }

    @Test (enabled = true, priority = 1, description = "Get All Employees with Admin and User role")
    void getEmployeeTestWithAdminAndUser() {
        Response Create = EmployeeController.createEmployee(Auth.AdminLogin,Auth.AdminPassword);
        int id = Create.then().extract().body().jsonPath().getInt("id");
        Response AdminResponse = EmployeeController.getAllEmployees(Auth.AdminLogin,Auth.AdminPassword);
        int AdminStatusCode = AdminResponse.getStatusCode();
        Assert.assertEquals(200, AdminStatusCode);
        Response UserResponse = EmployeeController.getAllEmployees(Auth.UserLogin,Auth.UserPassword);
        int UserStatusCode = UserResponse.getStatusCode();
        Assert.assertEquals(200, UserStatusCode);
        EmployeeController.deleteEmployee(Auth.AdminLogin,Auth.AdminPassword,id);
    }
    @Test (enabled = true, priority = 1, description = "Get All Employees with wrong Auth")
    void getEmployeeTestWithWrongAuth() {
        Response Create = EmployeeController.createEmployee(Auth.AdminLogin,Auth.AdminPassword);
        int id = Create.then().extract().body().jsonPath().getInt("id");
        Response WrongAuthResponse = EmployeeController.getAllEmployees(Auth.InvalidLogin,Auth.InvalidPassword);
        int WrongAuthStatusCode = WrongAuthResponse.getStatusCode();
        Assert.assertEquals(401, WrongAuthStatusCode);
        System.out.println("Authentication information is missing or invalid");
        EmployeeController.deleteEmployee(Auth.AdminLogin,Auth.AdminPassword,id);
    }
    @Test (enabled = true, priority = 1, description = "Get All Employees without Auth")
    void getEmployeeTestWithoutAuth() {
        Response Create = EmployeeController.createEmployee(Auth.AdminLogin,Auth.AdminPassword);
        int id = Create.then().extract().body().jsonPath().getInt("id");
        Response noAuthResponse = RestAssured
                .given()
                .baseUri(EmployeeSitePaths.baseURI)
                .basePath(EmployeeSitePaths.employeesPath)
                .when()
                .get();
        int StatusCode = noAuthResponse.getStatusCode();
        Assert.assertEquals(401, StatusCode);
        System.out.println("Authentication information is missing or invalid");
        EmployeeController.deleteEmployee(Auth.AdminLogin,Auth.AdminPassword,id);
    }

    @Test (enabled = false, priority = 1, description = "Get all Employees with a wrong role permissions")
    void getEmployeesTestWithWrongRolePermissions(){
        //There is no different roles except: ADMIN and USER (but they both allowed to get employees),so can't get 403 error code
        //TODO: 01.03.2021 Create a test for 403 error code, when have a role different from existing ones
    }

    @Test (enabled = true, priority = 1, description = "Get Employee by ID with Admin and User role")
    void getEmployeeByIDTestWithAdminAndUser() {
        Response Create = EmployeeController.createEmployee(Auth.AdminLogin,Auth.AdminPassword);
        int id = Create.then().extract().body().jsonPath().getInt("id");
        Response AdminResponse = EmployeeController.getEmployeeById(Auth.AdminLogin,Auth.AdminPassword, id);
        int AdminStatusCode = AdminResponse.getStatusCode();
        Assert.assertEquals(200, AdminStatusCode);
        Response UserResponse = EmployeeController.getEmployeeById(Auth.UserLogin,Auth.UserPassword, id);
        int UserStatusCode = UserResponse.getStatusCode();
        Assert.assertEquals(200, UserStatusCode);
        EmployeeController.deleteEmployee(Auth.AdminLogin,Auth.AdminPassword,id);
    }
    @Test (enabled = true, priority = 1, description = "Get Employee by ID without Auth")
    void getEmployeeByIDTestWithoutAuth() {
        Response Create = EmployeeController.createEmployee(Auth.AdminLogin,Auth.AdminPassword);
        int id = Create.then().extract().body().jsonPath().getInt("id");
        Response noAuthResponse = RestAssured
                .given()
                .baseUri(EmployeeSitePaths.baseURI)
                .basePath(EmployeeSitePaths.employeesPath+id)
                .when()
                .get();
        int StatusCode = noAuthResponse.getStatusCode();
        Assert.assertEquals(401, StatusCode);
        System.out.println("Authentication information is missing or invalid");
        EmployeeController.deleteEmployee(Auth.AdminLogin,Auth.AdminPassword,id);
    }
    @Test (enabled = true, priority = 1, description = "Get Employee by non exist ID")
    void getEmployeeByIDTestWithNonExistID() {
        Response Create = EmployeeController.createEmployee(Auth.AdminLogin,Auth.AdminPassword);
        int id = Create.then().extract().body().jsonPath().getInt("id");
        Response NonExistResponse = EmployeeController.getEmployeeById(Auth.AdminLogin,Auth.AdminPassword, id+1);
        int NonExistStatusCode = NonExistResponse.getStatusCode();
        Assert.assertEquals(404, NonExistStatusCode);
        System.out.println("The employee you were trying to reach is not found");
        EmployeeController.deleteEmployee(Auth.AdminLogin,Auth.AdminPassword,id);
    }

    @Test (enabled = false, priority = 1, description = "Get all Employees with a wrong role permissions")
    void getEmployeeByIDTestWithWrongRolePermissions(){
        //There is no different roles except: ADMIN and USER (but they both allowed to get employees),so can't get 403 error code
        //TODO: 01.03.2021 Create a test for 403 error code, when have a role different from existing ones
    }

    @Test(enabled = true, priority = 3, description = "Delete Employee by id with User role")
    void deleteEmployeeWithUser(){
        Response Create = EmployeeController.createEmployee(Auth.AdminLogin,Auth.AdminPassword);
        int id = Create.then().extract().body().jsonPath().getInt("id");
        Response userDelete = EmployeeController.deleteEmployee(Auth.UserLogin,Auth.UserPassword, id);
        int statusCode = userDelete.getStatusCode();
        Assert.assertEquals(403, statusCode);
        System.out.println("Accessing the resource you were trying to reach is forbidden for your role");
        EmployeeController.deleteEmployee(Auth.AdminLogin,Auth.AdminPassword,id);
    }
    @Test(enabled = true, priority = 3, description = "Delete Employee by id with Admin role")
    void deleteEmployeeWithAdmin(){
        Response Create = EmployeeController.createEmployee(Auth.AdminLogin,Auth.AdminPassword);
        int id = Create.then().extract().body().jsonPath().getInt("id");
        Response adminDelete = EmployeeController.deleteEmployee(Auth.AdminLogin,Auth.AdminPassword, id);
        int statusCode = adminDelete.getStatusCode();
        System.out.println(adminDelete.asString());
        Assert.assertEquals(200, statusCode);
    }
    @Test(enabled = true, priority = 3, description = "Delete Employee by id without Auth")
    void deleteEmployeeWithoutAuth(){
        Response Create = EmployeeController.createEmployee(Auth.AdminLogin,Auth.AdminPassword);
        int id = Create.then().extract().body().jsonPath().getInt("id");
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        Response noAuth = request
                .baseUri(EmployeeSitePaths.baseURI)
                .delete(EmployeeSitePaths.employeesPath + id);
        int statusCode = noAuth.getStatusCode();
        System.out.println(noAuth.asString());
        Assert.assertEquals(401, statusCode);
        System.out.println("Authentication information is missing or invalid");
        EmployeeController.deleteEmployee(Auth.AdminLogin,Auth.AdminPassword,id);
    }
    @Test(enabled = true, priority = 3, description = "Delete Employee by id with Invalid Auth")
    void deleteEmployeeWithInvalidAuth(){
        Response Create = EmployeeController.createEmployee(Auth.AdminLogin,Auth.AdminPassword);
        int id = Create.then().extract().body().jsonPath().getInt("id");
        Response adminDelete = EmployeeController.deleteEmployee(Auth.InvalidLogin,Auth.InvalidPassword, id);
        int statusCode = adminDelete.getStatusCode();
        System.out.println(adminDelete.asString());
        Assert.assertEquals(401, statusCode);
        System.out.println("Authentication information is missing or invalid");
        EmployeeController.deleteEmployee(Auth.AdminLogin,Auth.AdminPassword,id);
    }
    @Test(enabled = true, priority = 3, description = "Delete Employee by non exists ID")
    void deleteEmployeeWithAdminByNonExistsID(){
        Response Create = EmployeeController.createEmployee(Auth.AdminLogin,Auth.AdminPassword);
        int id = Create.then().extract().body().jsonPath().getInt("id");
        Response adminDelete = EmployeeController.deleteEmployee(Auth.AdminLogin,Auth.AdminPassword, id+1);
        int statusCode = adminDelete.getStatusCode();
        System.out.println(adminDelete.asString());
        Assert.assertEquals(404, statusCode);
        System.out.println("The employee you were trying to reach is not found");
        EmployeeController.deleteEmployee(Auth.AdminLogin,Auth.AdminPassword,id);
    }
    @Test(enabled = true, priority = 2, description = "Make a change in Employee by ID with Admin role")
    void putEmployeeWithAdmin(){
        Response Create = EmployeeController.createEmployee(Auth.AdminLogin,Auth.AdminPassword);
        int id = Create.then().extract().body().jsonPath().getInt("id");
        Response response =EmployeeController.updateEmployee(Auth.AdminLogin,Auth.AdminPassword, id);
        int statusCode = response.getStatusCode();
        System.out.println(response.asString());
        Assert.assertEquals(200, statusCode);
        EmployeeController.deleteEmployee(Auth.AdminLogin,Auth.AdminPassword,id);
    }

    @Test(enabled = true, priority = 2, description = "Make a change in Employee by ID with User role")
    void putEmployeeWithUser(){
        Response Create = EmployeeController.createEmployee(Auth.AdminLogin,Auth.AdminPassword);
        int id = Create.then().extract().body().jsonPath().getInt("id");
        Response response =EmployeeController.updateEmployee(Auth.UserLogin,Auth.UserPassword, id);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(403, statusCode);
        System.out.println("Accessing the resource you were trying to reach is forbidden for your role");
        EmployeeController.deleteEmployee(Auth.AdminLogin,Auth.AdminPassword,id);
    }
    @Test(enabled = true, priority = 2, description = "Make a change in Employee by ID without Auth")
    void putEmployeeWithoutAuth(){
        Response Create = EmployeeController.createEmployee(Auth.AdminLogin,Auth.AdminPassword);
        int id = Create.then().extract().body().jsonPath().getInt("id");
        JSONObject requestParams = EmployeeController.getRequestBody();
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(requestParams.toString());
        Response response = request
                .baseUri(EmployeeSitePaths.baseURI)
                .put(EmployeeSitePaths.employeesPath + id);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(401, statusCode);
        System.out.println("Authentication information is missing or invalid");
        EmployeeController.deleteEmployee(Auth.AdminLogin,Auth.AdminPassword,id);
    }
    @Test(enabled = true, priority = 2, description = "Make a change in Employee by ID with Invalid Auth")
    void putEmployeeWithInvalidAuth(){
        Response Create = EmployeeController.createEmployee(Auth.AdminLogin,Auth.AdminPassword);
        int id = Create.then().extract().body().jsonPath().getInt("id");
        Response response =EmployeeController.updateEmployee(Auth.InvalidLogin,Auth.InvalidPassword, id);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(401, statusCode);
        System.out.println("Authentication information is missing or invalid");
        EmployeeController.deleteEmployee(Auth.AdminLogin,Auth.AdminPassword,id);
    }
    @Test(enabled = true, priority = 2, description = "Make a change in Employee by ID with non exists ID")
    void putEmployeeWithNonExistsID(){
        Response Create = EmployeeController.createEmployee(Auth.AdminLogin,Auth.AdminPassword);
        int id = Create.then().extract().body().jsonPath().getInt("id");
        Response response =EmployeeController.updateEmployee(Auth.AdminLogin,Auth.AdminPassword, id+1);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(404, statusCode);
        System.out.println("The employee you were trying to reach is not found");
        EmployeeController.deleteEmployee(Auth.AdminLogin,Auth.AdminPassword,id);
    }


    //Server allows to update Employee with the same EmailID and UserName
    // TODO: 01.03.2021 create a test for 409 error code when fixed
    @Test(enabled = false, priority = 2, description = "Make a change in Employee by ID with the same emailid and username")
    void putEmployeeWithSameEmailAndUserName(){
        Response response = EmployeeController.updateSameEmployee(Auth.AdminLogin,Auth.AdminPassword);
        int StatusCode = response.getStatusCode();
        Assert.assertEquals(409, StatusCode);
        System.out.println("Employee with username or emailId exists");
        System.out.println(response.asString());
    }


    //There is no 422 error code, with invalid entity fields it responds a 500 error code
    // TODO: 01.03.2021 Create a test for 422 error code when fixed
    @Test(enabled = true, priority = 2, description = "Make a change in Employee by ID with Invalid entity fields")
    void putEmployeeWithBadEntity(){
        Response Create = EmployeeController.createEmployee(Auth.AdminLogin,Auth.AdminPassword);
        int id = Create.then().extract().body().jsonPath().getInt("id");
        JSONObject requestBody = EmployeeController.getRequestBodyWithInvalidFields();
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(requestBody);
        Response response = request
                .baseUri(EmployeeSitePaths.baseURI)
                .auth()
                .basic(Auth.AdminLogin,Auth.AdminPassword)
                .put(EmployeeSitePaths.employeesPath + id);
        int StatusCode = response.getStatusCode();
        Assert.assertEquals(500, StatusCode);
        System.out.println("Incorrect employee (entity fields are not valid)");
        EmployeeController.deleteEmployee(Auth.AdminLogin,Auth.AdminPassword,id);
    }
}
