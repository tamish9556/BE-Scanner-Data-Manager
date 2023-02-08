package com.dashboard.service.ifc;
import com.dashboard.model.ScannerGroupType;
import com.dashboard.model.ScannerType;
import com.dashboard.model.UnifiedDataScanner;
import org.springframework.web.multipart.MultipartFile;

public interface DataDriverHandlerServiceIfc {
    public void handleAndParsingData(MultipartFile file, UnifiedDataScanner unifiedData);
    public ScannerType getScannerType();
    public ScannerGroupType getScannerGroupType();
}
