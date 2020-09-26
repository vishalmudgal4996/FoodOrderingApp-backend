package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CustomerBusinessService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";

    @Transactional
    public CustomerEntity signUp(CustomerEntity customerEntity) throws SignUpRestrictedException {

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        final String password = customerEntity.getPassword();

        if(customerDao.customerByContactNumber(customerEntity.getContactNumber())!=null){
            throw new SignUpRestrictedException("SGR-001", "This contact number is already registered! Try other contact number.");
        }
        else if (customerEntity.getFirstName() == null || customerEntity.getEmail() == null ||
                customerEntity.getContactNumber() == null || customerEntity.getPassword() == null) {
            throw new SignUpRestrictedException("SGR-005", "Except last name all fields should be filled");
        }
        else if(!pattern.matcher(customerEntity.getEmail()).matches()){
            throw new SignUpRestrictedException("SGR-002", "Invalid email-id format!");
        }
        else if(!customerEntity.getContactNumber().matches("[0-9]+") || customerEntity.getContactNumber().length() != 10){
            throw new SignUpRestrictedException("SGR-003", "Invalid contact number!");
        }
        else if(customerEntity.getPassword().length() < 8
                || !customerEntity.getPassword().matches(".*[0-9]{1,}.*")
                || !customerEntity.getPassword().matches(".*[A-Z]{1,}.*")
                || !customerEntity.getPassword().matches(".*[#@$%&*!^]{1,}.*")){
            throw new SignUpRestrictedException("SGR-004", "Weak password!");
        }

        final String[] encryptedText = passwordCryptographyProvider.encrypt(password);
        customerEntity.setSalt(encryptedText[0]);
        customerEntity.setPassword(encryptedText[1]);

        return customerDao.customerSignUp(customerEntity);
    }

}
