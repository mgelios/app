package mg.finance.service.currency;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.finance.NBRBConfiguration;
import mg.finance.entity.Currency;
import mg.finance.entity.CurrencyStatistics;
import mg.finance.repository.CurrencyStatisticsRepository;
import mg.utils.JSONHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class CurrencyStatisticsService {

    private final NBRBConfiguration financeConfiguration;
    private final JSONHelper jsonHelper;
    private final CurrencyService currencyService;
    private final CurrencyStatisticsRepository currencyStatisticsRepository;
    private final CurrencyExternalApiService currencyExternalApiService;

    public Map<String, List<CurrencyStatistics>> getDefaultCurrencyStatistics() {
        return financeConfiguration.getDefaultStatisticsCurrencies().stream()
                .collect(Collectors.toMap(String::toString, this::getDefaultCurrencyStatisticsByAbbreviation));
    }

    public List<CurrencyStatistics> getDefaultCurrencyStatisticsByAbbreviation(String abbreviation) {
        Currency currency = currencyService.findCurrencyByAbbreviation(abbreviation);
        List<CurrencyStatistics> result = currencyStatisticsRepository.findAllByCurrency(currency);
        if (result.size() == 0 || !result.get(0).getUpdatedOn().isEqual(currency.getDate())) {
            result = updateCurrencyStatistics(currency);
        }
        return result;
    }

    public List<CurrencyStatistics> updateCurrencyStatistics(Currency currency) {
        JSONArray json = currencyExternalApiService.fetchCurrencyStatistics(
                String.valueOf(currency.getSystemId()));
        if (currencyStatisticsRepository.findAllByCurrency(currency).size() > 0) {
            currencyStatisticsRepository.deleteAllByCurrency(currency);
        }
        if (json != null) {
            return saveCurrencyStatistics(json, currency);
        }
        return currencyStatisticsRepository.findAllByCurrency(currency);
    }

    public List<CurrencyStatistics> saveCurrencyStatistics(JSONArray jsonArray, Currency currency) {
        if (jsonArray != null) {
            List<CurrencyStatistics> result = new ArrayList<>();
            for (Object item : jsonArray) {
                JSONObject jsonItem = (JSONObject) item;
                CurrencyStatistics statisticsDBEntity = CurrencyStatistics.builder()
                        .date(jsonHelper.getOffsetDateTime(jsonItem, "Date",
                                DateTimeFormatter.ISO_LOCAL_DATE_TIME, ZoneOffset.ofHours(3)))
                        .currency(currency)
                        .rate(jsonHelper.getDouble(jsonItem, "Cur_OfficialRate"))
                        .currencyId(jsonHelper.getLong(jsonItem, "Cur_ID"))
                        .updatedOn(currency.getDate())
                        .build();
                result.add(currencyStatisticsRepository.save(statisticsDBEntity));
            }
            return result;
        } else {
            return null;
        }
    }
}
