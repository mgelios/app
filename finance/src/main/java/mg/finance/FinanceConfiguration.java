package mg.finance;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Configuration
@PropertySource("classpath:/finance.properties")
@ConfigurationProperties(prefix = "mg.finance")
@Data
public class FinanceConfiguration {

    private String currencyBaseUrl;
    private String currencyRateSuffix;
    private String currencyStatisticsSuffix;
    private String currencyStatisticsStartDate;
    private String currencyStatisticsEndDate;
    private String currencyUrlParamMode;
    private List<String> defaultCurrencies;
    private List<String> defaultStatisticsCurrencies;
}
