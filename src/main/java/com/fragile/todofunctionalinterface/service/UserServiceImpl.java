package com.fragile.todofunctionalinterface.service;

import com.fragile.todofunctionalinterface.testContainerConfig.JwtProvider;
import com.fragile.todofunctionalinterface.entity.User;
import com.fragile.todofunctionalinterface.exceptions.UserException;
import com.fragile.todofunctionalinterface.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    @Override
    public User findUserById(Long userId) throws UserException {
        Optional<User> foundUser = userRepository.findById(userId);
        if(foundUser.isPresent()){
            return foundUser.get();
        }
        throw new UserException("User not found with id : " + userId);
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws UserException {
        String userEmail = jwtProvider.getEmailFromToken(jwt);

        User user = userRepository.findByEmail(userEmail);
        if(user == null){
            throw new UserException("User not found");
        }
        return user;
    }
}

