import com.dashboard.model.Severity;
import com.dashboard.model.Status;
import com.dashboard.prisma.model.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.*;

public class PrismaScannerModeResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == PrismaFile.class;
    }
    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        List<Report> reports = new ArrayList<>();
        List<String> applicableRules = new ArrayList<>();
        applicableRules.add("applicableRules");
        Map<String, Object> additionalProperties = new HashMap<>();
        additionalProperties.put("additionalPropertiesKey", "additionalPropertiesValue");
        reports.add(new Report("textReport1", 567, Severity.HIGH.toString(), 12.4, Status.FAILED.toString(), "cve",
                "cause","description", "title1", "vecStr", "exploit", new RiskFactors(new AttackComplexityLow(additionalProperties),
                new AttackVectorNetwork(additionalProperties), new DoS(additionalProperties), new HasFix(additionalProperties), new HighSeverity(additionalProperties),
                new RecentVulnerability(additionalProperties), additionalProperties), "link", "type", "packageName",
                "packageVersion", 5, "templates", false, false, 9, 3, applicableRules,
                "discovered", "functionLayer", additionalProperties));
        reports.add(new Report("textReport2", 3168, Severity.HIGH.toString(), 1.4, Status.FAILED.toString(), "cve",
                "cause","description", "title2", "vecStr", "exploit", new RiskFactors(new AttackComplexityLow(additionalProperties),
                new AttackVectorNetwork(additionalProperties), new DoS(additionalProperties), new HasFix(additionalProperties), new HighSeverity(additionalProperties),
                new RecentVulnerability(additionalProperties), additionalProperties), "link", "type", "packageName",
                "packageVersion", 3, "templates", false, false, 6, 5, applicableRules,
                "discovered", "functionLayer", additionalProperties));
        EntityInfo entityInfo = new EntityInfo(reports,"75skn234567","entityType","hostName",new Date(),
                "onDistro","distroVersion","distroRelease","distro",false,
                "467dfj8",new Date(),5,3,1,2,
                "topLayer","0.0.0",new Date(),"ERROR",12,"status",false);
        PrismaFile prismaFile = new PrismaFile(entityInfo,"p2717v8", new Date(), "job","build",false,"0.0.0");
        return prismaFile;
    }
}
