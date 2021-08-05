package com.example.testemployee;

import com.example.testemployee.domain.dao.EmployeeDAO;
import com.example.testemployee.domain.model.Employee;
import com.example.testemployee.exceptions.dto.ExceptionDTO;
import com.example.testemployee.properties.ApplicationProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestEmployeeApplicationTests {

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private EmployeeDAO employeeDAO;

    @BeforeEach
    public void cleanUp() {
        employeeDAO.deleteAll();
    }

    @Test
    void testEmployeeAPIGetCallWithGoodPublicCreds_shouldReturn200() {
        Employee savedEmployee = employeeDAO.save(new Employee("Elon"));
        ResponseEntity<Employee> employeeResponseEntity =
                template.withBasicAuth(applicationProperties.getPublicUsername(),
                                applicationProperties.getPublicPassword())
                        .getForEntity("/v1/employee/" + savedEmployee.getId(), Employee.class);
        assertThat(employeeResponseEntity.getBody()).isNotNull();
        assertThat(employeeResponseEntity.getStatusCode().value()).isEqualTo(200);
        assertThat(employeeResponseEntity.getBody().getName()).isEqualToIgnoringCase("elon");
    }

    @Test
    void testEmployeeAPIGetCallWithGoodPublicCredsWhichDoesntExists_shouldReturn400() {
        ResponseEntity<ExceptionDTO> employeeResponseEntity =
                template.withBasicAuth(applicationProperties.getPublicUsername(),
                                applicationProperties.getPublicPassword())
                        .getForEntity("/v1/employee/1", ExceptionDTO.class);
        assertThat(employeeResponseEntity.getBody()).isNotNull();
        assertThat(employeeResponseEntity.getStatusCode().value()).isEqualTo(400);
        assertThat(employeeResponseEntity.getBody().getExceptionList()).isNotEmpty();
        ExceptionDTO.Exception exception = employeeResponseEntity.getBody().getExceptionList().get(0);
        assertThat(exception.getErrorTitle()).containsIgnoringCase("Bad Request.");
        assertThat(exception.getErrorSource()).containsIgnoringCase("Employee with id: 1 doesn't exists.");
    }

    @Test
    void testEmployeeAPIGetCallWithBadPublicCreds_shouldReturn401() {
        Employee savedEmployee = employeeDAO.save(new Employee("Elon"));
        ResponseEntity<ExceptionDTO> employeeResponseEntity =
                template.withBasicAuth("badUser", "badPassword")
                        .getForEntity("/v1/employee/" + savedEmployee.getId(), ExceptionDTO.class);
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
                template.withBasicAuth(applicationProperties.getPublicUsername(),
                                applicationProperties.getPublicPassword())
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
                template.withBasicAuth(applicationProperties.getPublicUsername(),
                                applicationProperties.getPublicPassword())
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
                template.withBasicAuth(applicationProperties.getPublicUsername(),
                                applicationProperties.getPublicPassword())
                        .exchange("/v1/employee/1", HttpMethod.DELETE, null, ExceptionDTO.class);
        assertThat(employeeResponseEntity.getBody()).isNotNull();
        assertThat(employeeResponseEntity.getStatusCode().value()).isEqualTo(403);
        ExceptionDTO.Exception exception = employeeResponseEntity.getBody().getExceptionList().get(0);
        assertThat(exception.getErrorTitle()).containsIgnoringCase("AccessDenied.");
        assertThat(exception.getErrorSource()).containsIgnoringCase("Authorization Header.");
    }

    @Test
    void testEmployeeAPIPostCallWithGoodAdminCreds_shouldReturn200() {
        ResponseEntity<Employee> employeeResponseEntity =
                template.withBasicAuth(applicationProperties.getAdminUsername(),
                                applicationProperties.getAdminPassword())
                        .postForEntity("/v1/employee", new Employee("Zeff"), Employee.class);
        assertThat(employeeResponseEntity.getBody()).isNotNull();
        assertThat(employeeResponseEntity.getStatusCode().value()).isEqualTo(200);
        assertThat(employeeResponseEntity.getBody().getName()).isEqualToIgnoringCase("zeff");
    }

    @Test
    void testEmployeeAPIPutCallWithGoodAdminCreds_shouldReturn200() {
        Employee savedEmployee = employeeDAO.save(new Employee("Elon"));
        ResponseEntity<Employee> employeeResponseEntity =
                template.withBasicAuth(applicationProperties.getAdminUsername(),
                                applicationProperties.getAdminPassword())
                        .exchange("/v1/employee/" + savedEmployee.getId(), HttpMethod.PUT,
                                new HttpEntity<>(new Employee("Zeff")),
                                Employee.class);
        assertThat(employeeResponseEntity.getBody()).isNotNull();
        assertThat(employeeResponseEntity.getStatusCode().value()).isEqualTo(200);
        assertThat(employeeResponseEntity.getBody().getId()).isEqualTo(savedEmployee.getId());
        Employee employee = employeeDAO.findById(savedEmployee.getId()).orElse(null);
        assertThat(employee).isNotNull();
        assertThat(employee.getName()).isEqualToIgnoringCase("zeff");
    }


    @Test
    void testEmployeeAPIDeleteCallWithGoodAdminCreds_shouldReturn200() {
        Employee savedEmployee = employeeDAO.save(new Employee("Elon"));
        ResponseEntity<Employee> employeeResponseEntity =
                template.withBasicAuth(applicationProperties.getAdminUsername(),
                                applicationProperties.getAdminPassword())
                        .exchange("/v1/employee/" + savedEmployee.getId(), HttpMethod.DELETE, null, Employee.class);
        assertThat(employeeResponseEntity.getBody()).isNotNull();
        assertThat(employeeResponseEntity.getStatusCode().value()).isEqualTo(200);
        assertThat(employeeResponseEntity.getBody().getId()).isEqualTo(savedEmployee.getId());
        Employee employee = employeeDAO.findById(savedEmployee.getId()).orElse(null);
        assertThat(employee).isNull();
    }

}
