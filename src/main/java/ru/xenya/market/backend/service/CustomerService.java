package ru.xenya.market.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.xenya.market.backend.data.entity.Customer;
import ru.xenya.market.backend.data.entity.User;
import ru.xenya.market.backend.repositories.CustomerRepository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.function.BiConsumer;

@Service
public class CustomerService implements FilterableCrudService<Customer>{

    private CustomerRepository repository;

    @Autowired
    public CustomerService(CustomerRepository repository) {
        super();
        this.repository = repository;
    }

    @Transactional(rollbackOn = Exception.class)
    public Customer saveCustomer(User currentUser, Long id,
                                 BiConsumer<User, Customer> customerFiller) {
        Customer customer;
        if (id == null) {
            customer = new Customer();
        } else {
            customer = load(id);
        }
        customerFiller.accept(currentUser, customer);
        return repository.save(customer);
    }

    @Transactional(rollbackOn = Exception.class)
    public Customer saveCustomer(Customer customer) {
        return repository.save(customer);
    }


    @Override
    public Page<Customer> findAnyMatching(Optional<String> filter,
                                          Pageable pageable) {

        if (filter.isPresent()){
            String repositoryFilter = "%" + filter.get() + "%";
            return getRepository()
                    .findByFullNameLikeIgnoreCaseOrAddressLikeIgnoreCase(
                            repositoryFilter, repositoryFilter, pageable
                    );
        } else {
            return find(pageable);
        }
    }

    public Page<Customer> find(Pageable pageable) {
        return repository.findBy(pageable);
    }

    @Override
    public long countAnyMatching(Optional<String> filter) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return getRepository().countByFullNameLikeIgnoreCaseOrAddressLikeIgnoreCaseOrPhoneNumberForSMSIgnoreCaseOrPhoneNumbersLikeIgnoreCase(
                    repositoryFilter, repositoryFilter, repositoryFilter, repositoryFilter);
        } else {
            return count();
        }

    }

    @Override
    public CustomerRepository getRepository() {
        return repository;
    }


    @Override
    public Customer createNew(User user) {
        return new Customer();
    }
}
