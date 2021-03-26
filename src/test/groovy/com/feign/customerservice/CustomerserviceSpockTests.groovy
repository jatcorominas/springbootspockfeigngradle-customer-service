package com.feign.customerservice

import com.feign.customerservice.model.Customer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

@SpringBootTest
class CustomerserviceSpockTests extends Specification {
    @Autowired
    Environment environment

    def "application should start up properly"(){
        expect:
        environment != null
    }

    def "unit test of the application should return a list of Customers and an HttpStatus of OK"(){
        given:
        List<Customer> expectedCustomerList = new ArrayList<Customer>() {{ add(new Customer(7, "customer-7", 20)); add(new Customer( 8, "customer-8", 30)); add(new Customer( 9, "customer-9", 67)) }}
        TestRestTemplate mockRestTemplate = Mock{ getForEntity( _, List<Customer>.class) >> new ResponseEntity(new ArrayList<Customer>(){{add(new Customer(7,"customer-7", 20)); add(new Customer(8,"customer-8", 30)); add(new Customer(9, "customer-9", 67))}},HttpStatus.OK )}

        when:
        ResponseEntity<List<Customer>> entity = mockRestTemplate.getForEntity("http://localhost:8082/customers", List<Customer>.class)

        then:
        entity != null;
        entity.statusCode == HttpStatus.OK
        entity.hasBody()
        entity.getBody().size() == expectedCustomerList.size()
        List<Customer> actualCustomerList = entity.getBody()
        Customer expectedCustomer=null
        Customer actualCustomer=null
        for(int i = 0 ; i < expectedCustomerList.size(); i++){
            expectedCustomer = expectedCustomerList.get(i)
            actualCustomer = actualCustomerList.get(i)
            expectedCustomer.getAge()==actualCustomer.getAge()
            expectedCustomer.getName().equals(actualCustomer.getName())
            expectedCustomer.getId()==actualCustomer.getId()

        }
    }

    def "unit test of the application should return a Customer with id=3 and an HttpStatus of OK"(){
        given:
        TestRestTemplate mockRestTemplate = Mock{ getForEntity( _, Customer.class) >> new ResponseEntity( new Customer(3, "customer-3", 50), HttpStatus.OK) }

        when:
        ResponseEntity<Customer> entity = mockRestTemplate.getForEntity("http://locahost/customer/2", Customer.class)

        then:
        entity != null;
        entity.statusCode == HttpStatus.OK
        entity.hasBody() == true
        entity.body.getId() == 3
        entity.body.getName() == "customer-3"
        entity.body.getAge() == 50

    }

    def "unit test of the application should an null body and an HttpStatus of NOT_FOUND"(){
        given:
        TestRestTemplate mockRestTempate = Mock { getForEntity(_, Customer.class) >> new ResponseEntity( null, HttpStatus.NOT_FOUND)}

        when:
        ResponseEntity<Customer> entity = mockRestTempate.getForEntity( "http://localhost/customer/xxx", Customer.class)

        then:
        entity != null
        entity.statusCode == HttpStatus.NOT_FOUND
        entity.hasBody() == false
        entity.body == null
    }

    def "unit test of the application should return a null list of Customers and an HttpStatus of NOT_FOUND"(){
        given:
        TestRestTemplate mockRestTemplate = Mock { getForEntity(_, List<Customer>.class) >> new ResponseEntity( null, HttpStatus.NOT_FOUND)}

        when:
        ResponseEntity<List<Customer>> entity = mockRestTemplate.getForEntity("http://customers", List<Customer>.class)

        then:
        entity != null
        entity.statusCode == HttpStatus.NOT_FOUND
        entity.hasBody() == false
        entity.body == null
    }

    def "unit test of the application should return a null body and an HttpStatus of INTERNAL_SERVER_ERROR"(){
        given:
        TestRestTemplate mockRestTempate = Mock { getForEntity(_, Customer.class) >> new ResponseEntity( null, HttpStatus.INTERNAL_SERVER_ERROR)}

        when:
        ResponseEntity<Customer> entity = mockRestTempate.getForEntity( "http://localhost/customer/xxx", Customer.class)

        then:
        entity != null
        entity.statusCode == HttpStatus.INTERNAL_SERVER_ERROR
        entity.hasBody() == false
        entity.body == null
    }

    def "unit test of the application should an empty list of Customers and an HttpStatus of INTERNAL_SERVER_ERROR"(){
        given:
        TestRestTemplate mockRestTempate = Mock { getForEntity(_, List<Customer>.class) >> new ResponseEntity( null, HttpStatus.INTERNAL_SERVER_ERROR)}

        when:
        ResponseEntity<List<Customer>> entity = mockRestTempate.getForEntity( "http://localhost/customer/xxx", List<Customer>.class)

        then:
        entity != null
        entity.statusCode == HttpStatus.INTERNAL_SERVER_ERROR
        entity.hasBody() == false
        entity.body == null
    }

}
