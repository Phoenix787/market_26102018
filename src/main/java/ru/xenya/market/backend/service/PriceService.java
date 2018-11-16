package ru.xenya.market.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.xenya.market.backend.data.entity.Order;
import ru.xenya.market.backend.data.entity.Price;
import ru.xenya.market.backend.data.entity.User;
import ru.xenya.market.backend.repositories.PriceRepository;
import ru.xenya.market.ui.utils.converters.LocalDateToStringEncoder;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PriceService implements FilterableCrudService<Price> {

    private PriceRepository repository;
    private LocalDateToStringEncoder localDateToStringEncoder = new LocalDateToStringEncoder();

    @Autowired
    public PriceService(PriceRepository repository) {
        this.repository = repository;
    }


    @Transactional(rollbackOn = Exception.class)
    public Price savePrice(User currentUser, Long id, BiConsumer<User, Price> priceFiller){
        Price price;
        if (id == null) {
            price = new Price(currentUser);
        } else {
            price = load(id);
        }
        priceFiller.accept(currentUser, price);
        return repository.save(price);
    }

    @Transactional(rollbackOn = Exception.class)
    public Price savePrice(Price price) {
        return repository.save(price);
    }

    @Override
    public Page<Price> findAnyMatching(Optional<String> filter, Pageable pageable) {

        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return repository.findByDate(localDateToStringEncoder.decode(repositoryFilter), pageable);
        } else {
            return find(pageable);
        }

    }

    private Page<Price> find(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public long countAnyMatching(Optional<String> filter) {
        if (filter.isPresent()){
            String repositoryFilter = "%" + filter.get() + "%";
            return repository.countByDate(localDateToStringEncoder.decode(repositoryFilter));
        } else {
            return count();
        }
    }

    @Override
    public PriceRepository getRepository() {
        return repository;
    }

    @Override
    @Transactional
    public Price createNew(User currentUser) {
        Price price = new Price(currentUser);
        price.setDate(LocalDate.now());
        return price;
    }

    public List<Price> findPricesByDate(String filter) {
        String normalizeFilter = filter.toLowerCase();
        return repository.findAll()
                .stream().filter(price -> filterTextOf(price).contains(normalizeFilter))
                .sorted((((o1, o2) -> o1.getId().compareTo(o2.getId()))))
                .collect(Collectors.toList());
    }

    private String filterTextOf(Price price) {
        LocalDateToStringEncoder dateConverter = new LocalDateToStringEncoder();
        String filterableText = Stream.of(dateConverter.encode(price.getDate()))
                .collect(Collectors.joining("\t"));
        return filterableText.toLowerCase();
    }

    public Price findPriceByDefault(boolean isDefault) {
        return repository.findByDefaultPrice(isDefault);
    }


    @Transactional(rollbackOn = Exception.class)
    public Price addComment(User currentUser, Price price, String comment) {
        price.addHistoryItem(currentUser, comment);
        return repository.save(price);
    }
}
