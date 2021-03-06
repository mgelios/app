package mg.finance.repository;

import mg.finance.entity.Currency;
import mg.finance.entity.CurrencyStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CurrencyStatisticsRepository extends JpaRepository<CurrencyStatistics, UUID> {

    Optional<CurrencyStatistics> findFirstByCurrency(Currency currencyDBEntity);

    List<CurrencyStatistics> findAllByCurrency(Currency currencyDBEntity);

    void deleteAllByCurrency(Currency currencyDBEntity);
}
