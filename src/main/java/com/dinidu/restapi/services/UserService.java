package com.dinidu.restapi.services;


import com.dinidu.restapi.dtos.UserDTO;
import com.dinidu.restapi.exceptions.DuplicateResourceException;
import com.dinidu.restapi.exceptions.ResourceNotFoundException;
import com.dinidu.restapi.models.User;
import com.dinidu.restapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        log.debug("Fetching all users with pagination: {}", pageable);
        return userRepository.findAll(pageable).map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        log.debug("Fetching user by id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return convertToDTO(user);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserByUsername(String username) {
        log.debug("Fetching user by username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return convertToDTO(user);
    }

    public UserDTO createUser(UserDTO userDTO) {
        log.debug("Creating new user: {}", userDTO.getUsername());

        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new DuplicateResourceException("Username already exists: " + userDTO.getUsername());
        }

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + userDTO.getEmail());
        }

        User user = convertToEntity(userDTO);
        User savedUser = userRepository.save(user);
        log.info("User created successfully with id: {}", savedUser.getId());
        return convertToDTO(savedUser);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        log.debug("Updating user with id: {}", id);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Check for duplicates only if username/email changed
        if (!existingUser.getUsername().equals(userDTO.getUsername()) &&
                userRepository.existsByUsername(userDTO.getUsername())) {
            throw new DuplicateResourceException("Username already exists: " + userDTO.getUsername());
        }

        if (!existingUser.getEmail().equals(userDTO.getEmail()) &&
                userRepository.existsByEmail(userDTO.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + userDTO.getEmail());
        }

        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setFullName(userDTO.getFullName());
        existingUser.setStatus(userDTO.getStatus());

        User updatedUser = userRepository.save(existingUser);
        log.info("User updated successfully with id: {}", updatedUser.getId());
        return convertToDTO(updatedUser);
    }

    public void deleteUser(Long id) {
        log.debug("Deleting user with id: {}", id);
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
        log.info("User deleted successfully with id: {}", id);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> searchUsers(String name, Pageable pageable) {
        log.debug("Searching users by name: {}", name);
        return userRepository.findByNameContaining(name, pageable).map(this::convertToDTO);
    }

    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    private User convertToEntity(UserDTO userDTO) {
        return User.builder()
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .fullName(userDTO.getFullName())
                .status(userDTO.getStatus() != null ? userDTO.getStatus() : User.UserStatus.ACTIVE)
                .build();
    }
}
