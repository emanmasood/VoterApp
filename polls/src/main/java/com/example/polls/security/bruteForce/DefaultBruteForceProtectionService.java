package com.example.polls.security.bruteForce;

import com.example.polls.DATAAdlib.AdlibUserRepository;
import com.example.polls.model.User;
import com.example.polls.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Service("bruteForceProtectionService")
@Transactional("defaultTransactionManager")
public class DefaultBruteForceProtectionService implements BruteForceProtectionService {

    //@Value("${jdj.security.failedlogin.count}")
    private int maxFailedLogins = 2;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AdlibUserRepository adlibUserRepository;

    //@Value("${jdj.brute.force.cache.max}")
    private int cacheMaxLimit =1000;

    private final ConcurrentHashMap<String, FailedLogin> cache;

    public DefaultBruteForceProtectionService() {
        this.cache = new ConcurrentHashMap<>(cacheMaxLimit); //setting max limit for cache
    }

    @Override
    public void registerLoginFailure(String username) {
        User user = userRepository.findByEmail(username);
        //User user = getUser(username);
        if (user != null && !user.isLoginDisabled()) {
          int  failedCounter = user.getFailedLoginAttempts();
            if (maxFailedLogins < failedCounter + 1) {
                user.setLoginDisabled(true); //disabling the account
                userRepository.save(user);
               // adlibUserRepository.save(user);

            } else {
                //let's update the counter
                int count = failedCounter ++ ;
              user.setFailedLoginAttempts(count);
                userRepository.save(user);
               // adlibUserRepository.save(user);

            }
        }
    }

    @Override
    public void resetBruteForceCounter(String username) {
        User user = getUser(username);
        if (user != null) {
            user.setFailedLoginAttempts(0);
            user.setLoginDisabled(false);
            userRepository.save(user);
        }
    }

    @Override
    public boolean isBruteForceAttack(String username) {
        User user = getUser(username);
        if (user != null) {
            return user.getFailedLoginAttempts() >= maxFailedLogins ? true : false;
        }
        return false;
    }

    protected FailedLogin getFailedLogin(final String username) {
        FailedLogin failedLogin = cache.get(username.toLowerCase());

        if (failedLogin == null) {
            //setup the initial data
            failedLogin = new FailedLogin(0, LocalDateTime.now());
            cache.put(username.toLowerCase(), failedLogin);
            if (cache.size() > cacheMaxLimit) {

                // add the logic to remve the key based by timestamp
            }
        }
        return failedLogin;
    }

    private User getUser(final String username) {
        return userRepository.findByEmail(username);
    }

    public int getMaxFailedLogins() {
        return maxFailedLogins;
    }

    public void setMaxFailedLogins(int maxFailedLogins) {
        this.maxFailedLogins = maxFailedLogins;
    }

    public class FailedLogin {

        private int count;
        private LocalDateTime date;

        public FailedLogin() {
            this.count = 0;
            this.date = LocalDateTime.now();
        }

        public FailedLogin(int count, LocalDateTime date) {
            this.count = count;
            this.date = date;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public LocalDateTime getDate() {
            return date;
        }

        public void setDate(LocalDateTime date) {
            this.date = date;
        }
    }
}