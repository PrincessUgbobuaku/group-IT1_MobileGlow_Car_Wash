package za.ac.cput.service.payment;

import za.ac.cput.domain.payment.Card;
import za.ac.cput.service.IService;

import java.util.List;

public interface ICardService extends IService<Card, Long> {
    List<Card> getAll();
}