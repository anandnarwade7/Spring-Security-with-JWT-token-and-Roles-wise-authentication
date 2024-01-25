package com.cyperts.ExcellML.UserAndRole;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cyperts.ExcellML.MailIntegration.MailRequest;
import com.cyperts.ExcellML.MailIntegration.MailRequestRepository;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

@RestController
@RequestMapping("/api")
public class HomeContoller {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private MailRequestRepository mailRequestRepository;

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/login")
	public ResponseEntity<Boolean> authenticateUser(@RequestBody LoginDto loginDto) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			return new ResponseEntity<>(true, HttpStatus.OK);
		} catch (BadCredentialsException ex) {
			return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
		}
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody SignUpDTO signUpDto) {
		System.out.println("check point 1");
		if (userRepository.existsByUsername(signUpDto.getUsername()) == null) {
			System.out.println("check point 2");
			return new ResponseEntity<>("Username is already exist!", HttpStatus.BAD_REQUEST);
		}
		System.out.println("check point 3");
		if (userRepository.existsByEmail(signUpDto.getEmail()) == null) {
			System.out.println("check point 4");
			return new ResponseEntity<>("Email is already exist!", HttpStatus.BAD_REQUEST);
		}
		User user = new User();
		System.out.println("check point 5");
		user.setFirstName(signUpDto.getFirstName());
		user.setUsername(signUpDto.getUsername());
		user.setEmail(signUpDto.getEmail());
		user.setMobileNo(signUpDto.getMobileNo());
		user.setLastName(signUpDto.getLastName());
		user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
		System.out.println("check point 6");
		Role roles = roleRepository.findByName("ROLE_ADMIN").get();
		user.setRoles(Collections.singleton(roles));
		System.out.println("check point 7");
		userRepository.save(user);
		System.out.println("check point 8");
		return new ResponseEntity<>("User is registered successfully!", HttpStatus.OK);
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/getUserData/{username}")
	public User getUserData(@PathVariable String username) {
		return userRepository.findByUsername(username);
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/user/{email}")
	public String getUserByEmail(@PathVariable String email) {
		System.out.println("Data fetched before fetching from DB:: ");
		User user = userRepository.findUserByEmail(email);
		System.out.println("Data fetched after :: ");
		if (user != null) {
			System.out.println("Inside IF block:: ");
			return "User data found";
		} else {
			System.out.println("Insdie else block:: ");
			return "User data not found";
		}
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PutMapping("/forgetpassword/{email}/{otp}")
	public String checkOTP(@PathVariable String email, @PathVariable String otp, @RequestBody User user) {
		System.out.println("Check0::  ");
		User userByEmail = userRepository.findUserByEmail(email);
		System.out.println("Check1::  ");
		System.out.println(userByEmail.toString());
		System.out.println("Check2::  ");

		String otpFromUser = otp;
		System.out.println("Check3:  ");

		System.out.println("OTP by user input::: " + otpFromUser);

		MailRequest byemailTo = mailRequestRepository.getByemailTo(email);
		System.out.println("Check4::  ");

		String otpFromDB = byemailTo.getOtp();
		System.out.println("Check5::  ");

		if (otpFromDB.equals(otpFromUser)) {
			System.out.println("In for block ::: ");
			user.setId(userByEmail.getId());
			user.setEmail(userByEmail.getEmail());
			user.setFirstName(userByEmail.getFirstName());
			user.setLastName(userByEmail.getLastName());
			user.setUsername(userByEmail.getUsername());
			user.setMobileNo(userByEmail.getMobileNo());
			user.setRoles(userByEmail.getRoles());
			user.setPassword(passwordEncoder.encode(user.getPassword()));

			userRepository.save(user);
			System.out.println("::::: User Updated ::::::: ");
		}
		return "Password updated successfully!";
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/update/{email}")
	public User updateUser(@PathVariable String email, @RequestBody User user) {
		System.out.println("check point0::: ");

		User userByEmail = userRepository.findUserByEmail(email);
		System.out.println("check point1::: ");
		if (userByEmail != null) {
			System.out.println("check point2::: ");

			user.setId(userByEmail.getId());
			user.setEmail(userByEmail.getEmail());
			user.setFirstName(user.getFirstName());
			user.setLastName(user.getLastName());
			user.setUsername(user.getUsername());
			user.setMobileNo(userByEmail.getMobileNo());
			user.setRoles(userByEmail.getRoles());
			user.setPassword(passwordEncoder.encode(userByEmail.getPassword()));
			System.out.println("check point3::: ");

			return userRepository.save(user);
		}
		return null;
	}
}