package com.fragile.todofunctionalinterface.service;

import com.fragile.todofunctionalinterface.entity.User;
import com.fragile.todofunctionalinterface.exceptions.UserException;

public interface UserService {

    User findUserById(Long userId) throws UserException;

    User findUserProfileByJwt(String jwt) throws UserException;
}
