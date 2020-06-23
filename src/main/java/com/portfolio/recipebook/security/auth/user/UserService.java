package com.portfolio.recipebook.security.auth.user;

import com.portfolio.recipebook.util.UserEmailAlreadyExistException;
import com.portfolio.recipebook.util.UserNicknameAlreadyExistException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findOneUser(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    @Override
    public User updateUser(User user) throws UserEmailAlreadyExistException {
        if (emailExist(user.getEmail())) {
            throw new UserEmailAlreadyExistException(user.getEmail());
        } else if (nicknameExist(user.getNickname())) {
            throw new UserNicknameAlreadyExistException(user.getNickname());
        }
        return userRepository.save(user);
    }

    private boolean emailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private boolean nicknameExist(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }
}
