package cm.lao.tenant.api;

import cm.lao.tenant.dto.DemoDTO;
import cm.lao.tenant.service.DemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DemoResources implements DemoApi {

    private final DemoService demoService;

    @Override
    public ResponseEntity<List<DemoDTO>> fetchAllDemo(String fieldsToExtractCode) {
        return ResponseEntity.ok(demoService.fetchAll(fieldsToExtractCode));
    }
}
