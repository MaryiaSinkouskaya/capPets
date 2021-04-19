package pr3.provider;

import com.sap.cds.ql.cqn.CqnAnalyzer;
import com.sap.cds.ql.cqn.CqnSelect;
import com.sap.cds.reflect.CdsModel;
import org.springframework.stereotype.Component;

@Component
public class IdProvider {

    private final CqnAnalyzer analyzer;

    public IdProvider(CdsModel model) {
        this.analyzer = CqnAnalyzer.create(model);
    }

    public Integer getId(CqnSelect cqnSelect) {
        return (Integer) analyzer.analyze(cqnSelect).targetKeys().get("ID");
    }
}