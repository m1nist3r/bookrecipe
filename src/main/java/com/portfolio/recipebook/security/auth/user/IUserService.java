package com.portfolio.recipebook.security.auth.user;

import com.portfolio.recipebook.util.UserEmailAlreadyExistException;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> findAllUsers();

    Optional<User> findOneUser(Long id);

    User updateUser(User user) throws UserEmailAlreadyExistException;;

}
