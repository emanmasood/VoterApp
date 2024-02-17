package com.example.polls.security.bruteForce;

import com.example.polls.model.User;
import com.example.polls.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("customerAccountService")
@Transactional("defaultTransactionManager")
public class DefaultCustomerAccountService  implements CustomerAccountService{

    @Resource
    private UserRepository userRepository;

    @Override
    public boolean loginDisabled(String username) {
        User user = userRepository.findByEmail(username);
        return user!=null ? user.isLoginDisabled() : false;
    }
}
