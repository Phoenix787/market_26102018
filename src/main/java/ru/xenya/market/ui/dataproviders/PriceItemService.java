//package ru.xenya.market.ui.dataproviders;
//
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Service;
//import ru.xenya.market.backend.data.entity.Price;
//import ru.xenya.market.backend.data.entity.PriceItem;
//import ru.xenya.market.backend.data.entity.User;
//import ru.xenya.market.backend.repositories.PriceItemRepository;
//import ru.xenya.market.backend.service.FilterableCrudService;
//import ru.xenya.market.backend.service.UserFriendlyDataException;
//
//import javax.transaction.Transactional;
//import java.util.Optional;
//
////todo как orderservice
//@Service
//public class PriceItemService implements FilterableCrudService<PriceItem> {
//
//    private final PriceItemRepository repository;
//    private Price currentPrice;
//
//    public Price getCurrentPrice() {
//        return currentPrice;
//    }
//
//    public void setCurrentPrice(Price currentPrice) {
//        this.currentPrice = currentPrice;
//    }
//
//    public PriceItemService(PriceItemRepository repository) {
//        super();
//        this.repository = repository;
//    }
//
//    @Override
//    public Page<PriceItem> findAnyMatching(Optional<String> filter, Pageable pageable) {
//        return null;
//    }
//
//    @Override
//    public long countAnyMatching(Optional<String> filter) {
//        return 0;
//    }
//
//    @Override
//    public JpaRepository<PriceItem, Long> getRepository() {
//        return repository;
//    }
//
//
//    @Override
//    public PriceItem createNew(User user) {
//        PriceItem priceItem = new PriceItem();
//
//        return priceItem;
//    }
//
//    @Override
//    public PriceItem save(User currentUser, PriceItem entity) {
//        try {
//            return FilterableCrudService.super.save(currentUser, entity);
//        } catch (DataIntegrityViolationException e) {
//            throw new UserFriendlyDataException(
//                    "Уже существует такая позиция прайса. Пожалуйста, выберите уникальное имя для позиции"
//            );
//        }
//    }
//}
