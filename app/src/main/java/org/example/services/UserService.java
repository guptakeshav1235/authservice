package org.example.services;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

import org.example.entities.UserInfo;
import org.example.models.UserInfoDto;
import org.example.repositories.UserRepository;
import org.example.utility.ValidationUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(user);
    }

    public UserInfo checkIfUserAlreadyExist(UserInfoDto userInfoDto) {
        return userRepository.findByUsername(userInfoDto.getUsername());
    }

    public Boolean signupUser(UserInfoDto userInfoDto) {
        if (userInfoDto.getEmail() != null && !ValidationUtility.isValidEmail(userInfoDto.getEmail())) {
            return false;
        }

        if (!ValidationUtility.isValidPassword(userInfoDto.getPassword())) {
            return false;
        }

        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
        if (Objects.nonNull(checkIfUserAlreadyExist(userInfoDto))) {
            return false;
        }

        String userId = UUID.randomUUID().toString();
        userRepository.save(new UserInfo(userId, userInfoDto.getUsername(),
                userInfoDto.getPassword(), new HashSet<>()));
        return true;
    }

}
