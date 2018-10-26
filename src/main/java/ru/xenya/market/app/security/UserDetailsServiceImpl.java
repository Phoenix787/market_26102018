//package ru.xenya.market.app.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Primary;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import ru.xenya.market.backend.data.entity.User;
//import ru.xenya.market.backend.repositories.UserRepository;
//
//import java.util.Collections;
//
//@Primary
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    @Autowired
//    public UserDetailsServiceImpl(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByEmailIgnoreCase(username);
//        if (user == null) {
//            throw new UsernameNotFoundException("Не существует пользователя с таким логином: " + username);
//        } else {
//
//            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPasswordHash(),
//                    Collections.singletonList(new SimpleGrantedAuthority(user.getRole())));
//        }
//    }
//}
