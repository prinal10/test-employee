package com.example.testemployee;

import com.example.testemployee.domain.dao.EmployeeDAO;
import com.example.testemployee.domain.model.Employee;
import com.example.testemployee.exceptions.dto.ExceptionDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestEmployeeApplicationTests {

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private EmployeeDAO employeeDAO;

    public void addEmployee(List<Employee> employees) {
        employeeDAO.saveAllAndFlush(employees);
    }

    @Test
    void testEmployeeAPIGetCallWithGoodPublicCreds() {
        addEmployee(List.of(new Employee(1L, "Elon")));
        ResponseEntity<Employee> employeeResponseEntity =
                template.withBasicAuth("pubUser", "pubPassword").getForEntity("/v1/employee/1", Employee.class);
        assertThat(employeeResponseEntity.getBody()).isNotNull();
        assertThat(employeeResponseEntity.getStatusCode().value()).isEqualTo(200);
        assertThat(employeeResponseEntity.getBody().getName()).isEqualToIgnoringCase("elon");
    }

    @Test
    void testEmployeeAPIGetCallWithBadPublicCreds() {
        addEmployee(List.of(new Employee(1L, "Elon")));
        ResponseEntity<ExceptionDTO> employeeResponseEntity =
                template.withBasicAuth("badUser", "badPassword")
                        .getForEntity("/v1/employee/1", ExceptionDTO.class);
        assertThat(employeeResponseEntity.getBody()).isNotNull();
        assertThat(employeeResponseEntity.getStatusCode().value()).isEqualTo(401);
        assertThat(employeeResponseEntity.getBody().getExceptionList()).isNotEmpty();
        ExceptionDTO.Exception exception = employeeResponseEntity.getBody().getExceptionList().get(0);
        assertThat(exception.getErrorTitle()).containsIgnoringCase("Unauthorized.");
        assertThat(exception.getErrorSource()).containsIgnoringCase("Authorization Header.");
    }

    @Test
    void testEmployeeAPIPostCallWithGoodPublicCreds_shouldReturn403() {
        ResponseEntity<ExceptionDTO> employeeResponseEntity =
                template.withBasicAuth("pubUser", "pubPassword")
                        .postForEntity("/v1/employee", new Employee("Zeff"), ExceptionDTO.class);
        assertThat(employeeResponseEntity.getBody()).isNotNull();
        assertThat(employeeResponseEntity.getStatusCode().value()).isEqualTo(403);
        ExceptionDTO.Exception exception = employeeResponseEntity.getBody().getExceptionList().get(0);
        assertThat(exception.getErrorTitle()).containsIgnoringCase("AccessDenied.");
        assertThat(exception.getErrorSource()).containsIgnoringCase("Authorization Header.");
    }

    @Test
    void testEmployeeAPIPutCallWithGoodPublicCreds_shouldReturn403() {
        ResponseEntity<ExceptionDTO> employeeResponseEntity =
                template.withBasicAuth("pubUser", "pubPassword")
                        .exchange("/v1/employee/1", HttpMethod.PUT, new HttpEntity<>(new Employee("Zeff")),
                                ExceptionDTO.class);
        assertThat(employeeResponseEntity.getBody()).isNotNull();
        assertThat(employeeResponseEntity.getStatusCode().value()).isEqualTo(403);
        ExceptionDTO.Exception exception = employeeResponseEntity.getBody().getExceptionList().get(0);
        assertThat(exception.getErrorTitle()).containsIgnoringCase("AccessDenied.");
        assertThat(exception.getErrorSource()).containsIgnoringCase("Authorization Header.");
    }

    @Test
    void testEmployeeAPIDeleteCallWithGoodPublicCreds_shouldReturn403() {
        ResponseEntity<ExceptionDTO> employeeResponseEntity =
                template.withBasicAuth("pubUser", "pubPassword")
                        .exchange("/v1/employee/1", HttpMethod.DELETE, null, ExceptionDTO.class);
        assertThat(employeeResponseEntity.getBody()).isNotNull();
        assertThat(employeeResponseEntity.getStatusCode().value()).isEqualTo(403);
        ExceptionDTO.Exception exception = employeeResponseEntity.getBody().getExceptionList().get(0);
        assertThat(exception.getErrorTitle()).containsIgnoringCase("AccessDenied.");
        assertThat(exception.getErrorSource()).containsIgnoringCase("Authorization Header.");
    }

}
