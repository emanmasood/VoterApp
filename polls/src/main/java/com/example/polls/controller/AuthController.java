package com.example.polls.controller;

import com.example.polls.DATAAdlib.AdlibUserRepository;
import com.example.polls.DATAAlrakka.AlrakkaUserRepository;
import com.example.polls.DATADamas.DamasUserRepository;
import com.example.polls.DATADaraa.DaraaUserRepository;
import com.example.polls.DATADeralzoor.DeralzoorUserRepository;
import com.example.polls.DATAHalab.HalabUserRepository;
import com.example.polls.DATAHama.HamaUserRepository;
import com.example.polls.DATAHasakah.HasakahUserRepository;
import com.example.polls.DATAHoms.HomsUserRepository;
import com.example.polls.DATALatakia.LatakiaUserRepository;

import com.example.polls.DATAQuneitra.QuneitraUserRepository;
import com.example.polls.DATARural.RuralUserRepository;
import com.example.polls.DATASwaida.SwaidaUserRepository;
import com.example.polls.DATATartous.TartousUserRepository;
import com.example.polls.dto.Otp;
import com.example.polls.dto.StoreOtp;
import com.example.polls.exception.AppException;
import com.example.polls.model.*;
import com.example.polls.payload.*;
import com.example.polls.repository.Admin4Repository;
import com.example.polls.repository.RoleRepository;
import com.example.polls.repository.TelecomeRepository;
import com.example.polls.repository.UserRepository;
import com.example.polls.security.JwtTokenProvider;
import com.example.polls.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;
    @Autowired
    OtpService otpService;

    @Autowired
    private TelecomeRepository telecomeRepository;

    @Autowired
    Admin4Repository admin4Repository;

    @Autowired
    DamasUserRepository damasUserRepository;

    @Autowired
    AdlibUserRepository adlibUserRepository;
    @Autowired
    AlrakkaUserRepository alrakkaUserRepository;
    @Autowired
    DaraaUserRepository daraaUserRepository;
    @Autowired
    DeralzoorUserRepository deralzoorUserRepository;
    @Autowired
    HalabUserRepository halabUserRepository;
    @Autowired
    HamaUserRepository hamaUserRepository;
    @Autowired
    HasakahUserRepository hasakahUserRepository;
    @Autowired
    HomsUserRepository homsUserRepository;
    @Autowired
    LatakiaUserRepository latakiaUserRepository;
    @Autowired
    RuralUserRepository ruralUserRepository ;
    @Autowired
    QuneitraUserRepository quneitraUserRepository;
    @Autowired
    SwaidaUserRepository swaidaUserRepository;
    @Autowired
    TartousUserRepository tartousUserRepository;

    @Autowired
    RestTemplate  restTemplate;
    private boolean admin;


    @PostMapping("/signin")
    @Transactional
    @Qualifier("defaultTransactionManager")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> requestEntity = new HttpEntity<>(loginRequest, headers);

       return restTemplate.exchange("http://signinService/api/auth/signin",HttpMethod.POST,requestEntity,JwtAuthenticationResponse.class);
//        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
//        if (existingAuth != null && existingAuth.isAuthenticated()) {
//            return ResponseEntity.badRequest().body("User is already authenticated.");
//        }
//
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginRequest.getUsernameOrEmail(),
//                        loginRequest.getPassword()
//                )
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String jwt = tokenProvider.generateToken(authentication);
//
//        User user = userRepository.findByUsernameOrEmail(loginRequest.getUsernameOrEmail(),
//                loginRequest.getUsernameOrEmail())
//                .orElseThrow(() ->
//                new UsernameNotFoundException("User not found with username or email : " + loginRequest.getUsernameOrEmail())
//        );
//
//        admin = false;
//        System.out.print(userRepository.roleName(user.getId()));
//
//        if(userRepository.roleName(user.getId()).equals(RoleName.ROLE_ADMIN))
//            admin = true;
//
//        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt,admin));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        Telecome telecomeUser = telecomeRepository.findByIdnum(signUpRequest.getIdnum());

        if(telecomeUser.equals(null))
            return ResponseEntity.badRequest().body(new ApiResponse(false,"Sorry! Your information are not true"));

        if (!(telecomeUser.getCity().equals(signUpRequest.getCity())) && !(telecomeUser.getPhone().equals(signUpRequest.getPhone())))
            return ResponseEntity.badRequest().body(new ApiResponse(false,"Sorry! Your information are not true"));

        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword(), signUpRequest.getPhone(), signUpRequest.getIdnum(),
                signUpRequest.getGender(),signUpRequest.getCity());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);


        otpService.send(user.getPhone());


        Admin4 admin = admin4Repository.getAdmin4(user.getCity());
        Admin3 admin3 = admin.getAdmin3();
        Admin2 admin2 = admin3.getAdmin2();
        Admin1 admin1 = admin2.getAdmin1();
        System.out.println( "this is name of :"+admin1.getAdmin1Name_en());
        if("Damascus".equalsIgnoreCase(admin1.getAdmin1Name_en())){
            User user1= damasUserRepository.save(user);
        }
        if("Idleb".equalsIgnoreCase(admin1.getAdmin1Name_en())){
            User user1= adlibUserRepository.save(user);
        }
        if("Al-Hasakeh".equalsIgnoreCase(admin1.getAdmin1Name_en())){
            User user1= hasakahUserRepository.save(user);
        }
        if("Ar-Raqqa".equalsIgnoreCase(admin1.getAdmin1Name_en())){
            User user1= alrakkaUserRepository.save(user);
        }
        if("As-Sweida".equalsIgnoreCase(admin1.getAdmin1Name_en())){
            User user1= swaidaUserRepository.save(user);
        }
        if("Quneitra".equalsIgnoreCase(admin1.getAdmin1Name_en())){
            User user1= quneitraUserRepository.save(user);
        }
        if("Lattakia".equalsIgnoreCase(admin1.getAdmin1Name_en())){
            User user1= latakiaUserRepository.save(user);
        }
        if("Aleppo".equalsIgnoreCase(admin1.getAdmin1Name_en())){
            User user1= halabUserRepository.save(user);
        }
        if("Hama".equalsIgnoreCase(admin1.getAdmin1Name_en())){
            User user1= hamaUserRepository.save(user);
        }
        if("Homs".equalsIgnoreCase(admin1.getAdmin1Name_en())){
            User user1= homsUserRepository.save(user);
        }
        if("Dar'a".equalsIgnoreCase(admin1.getAdmin1Name_en())){
            User user1= daraaUserRepository.save(user);
        }
        if("Deir-ez-Zor".equalsIgnoreCase(admin1.getAdmin1Name_en())){
            User user1= deralzoorUserRepository.save(user);
        }
        if("Tartous".equalsIgnoreCase(admin1.getAdmin1Name_en())){
            User user1= tartousUserRepository.save(user);
        }
        if("Rural Damascus".equalsIgnoreCase(admin1.getAdmin1Name_en())){
            User user1= ruralUserRepository.save(user);
        }



        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }

    @PostMapping("/otp")
    public ResponseEntity<ApiResponse> verifyotp(@RequestBody Otp otp){

        if(otp.getOtp()== StoreOtp.getOtp())
            return ResponseEntity.badRequest().body(new ApiResponse(true,"Otp number is Correct"));

        else
            return ResponseEntity.badRequest().body(new ApiResponse(false,"Otp number is not Correct"));

    }
    @PostMapping("/adminSignup")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating admin's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword(), signUpRequest.getPhone(), signUpRequest.getIdnum(),
                signUpRequest.getGender(),signUpRequest.getCity());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new AppException("Admin Role not set."));

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "Admin registered successfully"));
    }
}
